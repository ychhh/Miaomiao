package com.hbsd.rjxy.miaomiao.ych.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.wq.CatFCActivity;
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
import static com.hbsd.rjxy.miaomiao.utils.Constant.FIND_USER_CAT;

public class MyCatFragment extends Fragment {
    List<Cat> cats=new ArrayList<>();
    List<List<Cat>> catss= new ArrayList<List<Cat>>();
    RecyclerView recyclerView;
    String TAG="MyCatFragment";
    Handler handler;
    Button btn_addcat;
    Gson gson=new Gson();
    Map<String,String> map1=new HashMap<String, String>();
    Map<String,String> map=new HashMap<String, String>();
    int x=0;
    MyCatsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(), R.layout.activity_my_cat, null);//todo ljt更改
        View view=inflater.inflate(R.layout.activity_my_cat, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        btn_addcat=view.findViewById(R.id.btn_addc);
        map1.put("uid", "1");
        btn_addcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: "+"tiaozhuan" );
                Intent intent=new Intent(getActivity(), AddCatActivity.class);
                startActivity(intent);
            }
        });

        OkHttpUtils.getInstance().postForm(FIND_USER_CAT, map1, new Callback() {
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
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                catss.clear();
                cats.clear();
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
                adapter = new MyCatsAdapter(R.layout.item_my_cat, catss, getContext());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        };
//        btn_addcat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), AddCatActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }
    public class MyCatsAdapter extends BaseQuickAdapter<List<Cat>, BaseViewHolder> {

        private Context context;

        public MyCatsAdapter(int layoutResId, @Nullable List<List<Cat>> data, Context context) {
            super(layoutResId, data);
            Log.e(TAG, "MyCatsAdapter: " + data);
            this.context = context;

        }

        public MyCatsAdapter(@Nullable List<List<Cat>> data) {
            super(data);
        }

        public MyCatsAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, List<Cat> item) {

            Log.e(TAG, "convert: item:" + item.size());
            for (int i = 0; i < item.size(); i++) {
                if (item.get(i) != null) {
                    Log.e(TAG, "convert i: " + i);
                    if (i%3 == 0)
                        Glide.with(context).load(item.get(i).getCatHead()).into((ImageView) helper.getView(R.id.img_cat1));
                    if (i%3 == 1)
                        Glide.with(context).load(item.get(i).getCatHead()).into((ImageView) helper.getView(R.id.img_cat2));
                    if (i%3 == 2)
                        Glide.with(context).load(item.get(i).getCatHead()).into((ImageView) helper.getView(R.id.img_cat3));
                }
            }

//            adapter.notifyDataSetChanged();
            helper.getView(R.id.img_cat1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), CatFCActivity.class);
                    intent.putExtra("cid",item.get(0).getId());
                    v.getContext().startActivity(intent);
                }
            });
            helper.getView(R.id.img_cat2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), CatFCActivity.class);
                    intent.putExtra("cid",item.get(1).getId());
                    v.getContext().startActivity(intent);
                }
            });
            helper.getView(R.id.img_cat3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), CatFCActivity.class);
                    intent.putExtra("cid",item.get(2).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
