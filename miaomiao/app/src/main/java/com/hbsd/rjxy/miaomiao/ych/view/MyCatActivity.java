package com.hbsd.rjxy.miaomiao.ych.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.ych.model.MyCatsAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.FIND_SUB_CAT;

public class MyCatActivity extends Activity {
    List<Cat> cats=new ArrayList<>();
    List<List<Cat>> catss= new ArrayList<List<Cat>>();
    RecyclerView recyclerView;
    String TAG="MyCatActivity";
    Handler handler;
    Gson gson=new Gson();
    Map<String,String> map1=new HashMap<String, String>();
    Map<String,String> map=new HashMap<String, String>();
    int x=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cat);
        recyclerView=findViewById(R.id.recycler);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, "handleMessagecat: "+msg.obj );
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(msg.obj.toString()).getAsJsonArray();
//                for (JsonElement element : jsonarray) {
//                    Cat c=gson.fromJson(element, Cat.class);
//                    cats.add(c);
//                    Log.e(TAG, "handleMessage:gsonc "+element );
//                }
                for (int i=0;i<jsonarray.size();i++){
//                    Log.e(TAG, "handleMessage: jsonarray.size()："+jsonarray.size() );
                    Cat c=gson.fromJson(jsonarray.get(i), Cat.class);

                    cats.add(c);
                    if (cats.size()==3){
                        Log.e(TAG, "handleMessage: cats:"+cats );
                        catss.add(cats);
                        cats=new ArrayList<>();
                    }
                }
                catss.add(cats);
                Log.e(TAG, "handleMessage: catsssize"+catss.size() );
                Log.e(TAG, "handleMessage: catss"+catss );
                MyCatsAdapter adapter=new MyCatsAdapter(R.layout.item_my_cat,catss,MyCatActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyCatActivity.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }
        };
        map1.put("uid","2");
        OkHttpUtils.getInstance().postForm(FIND_SUB_CAT, map1, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                Message msg=Message.obtain();
                msg.obj=response.body().string();
                handler.sendMessage(msg);
            }
        });

//        map.put("img1","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575951502205&di=94392c75e406ce38a767558d793816ac&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180211%2F01%2F1518283482-zxHFomPtAE.jpg");
//        maps.add(map);
//        Log.e(TAG, "onCreate: "+11111111 );
//        map.put("img2","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575951502205&di=94392c75e406ce38a767558d793816ac&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180211%2F01%2F1518283482-zxHFomPtAE.jpg");
//        maps.add(map);
//        map.put("img3","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575951502205&di=94392c75e406ce38a767558d793816ac&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180211%2F01%2F1518283482-zxHFomPtAE.jpg");

    }
}
