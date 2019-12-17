package com.hbsd.rjxy.miaomiao.ych.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.ych.view.AddCatActivity;
import com.hbsd.rjxy.miaomiao.ych.view.MyCatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.FIND_SUB_CAT;

public class MyCatFragment extends Fragment {
    List<Cat> cats=new ArrayList<>();
    List<List<Cat>> catss= new ArrayList<List<Cat>>();
    RecyclerView recyclerView;
    String TAG="MyCatActivity";
    Handler handler;
    Button btn_addcat;
    Gson gson=new Gson();
    Map<String,String> map1=new HashMap<String, String>();
    Map<String,String> map=new HashMap<String, String>();
    int x=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.activity_my_cat, null);
        recyclerView = view.findViewById(R.id.recycler);
        btn_addcat=view.findViewById(R.id.btn_addcat);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, "handleMessagecat: " + msg.obj);
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(msg.obj.toString()).getAsJsonArray();
//                for (JsonElement element : jsonarray) {
//                    Cat c=gson.fromJson(element, Cat.class);
//                    cats.add(c);
//                    Log.e(TAG, "handleMessage:gsonc "+element );
//                }
                for (int i = 0; i < jsonarray.size(); i++) {
//                    Log.e(TAG, "handleMessage: jsonarray.size()："+jsonarray.size() );
                    Cat c = gson.fromJson(jsonarray.get(i), Cat.class);

                    cats.add(c);
                    if (cats.size() == 3) {
                        Log.e(TAG, "handleMessage: cats:" + cats);
                        catss.add(cats);
                        cats = new ArrayList<>();
                    }
                }
                catss.add(cats);
                Log.e(TAG, "handleMessage: catsssize" + catss.size());
                Log.e(TAG, "handleMessage: catss" + catss);
                MyCatsAdapter adapter = new MyCatsAdapter(R.layout.item_my_cat, catss, getContext());
                catss.clear();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }
        };
        btn_addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddCatActivity.class);
                startActivity(intent);
            }
        });
        map1.put("uid", "2");
        OkHttpUtils.getInstance().postForm(FIND_SUB_CAT, map1, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                Message msg = Message.obtain();
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
        return view;
    }
}
