package com.hbsd.rjxy.miaomiao.ych.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.ych.model.FollowAdapter;

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

public class FollowActivity extends Activity {
    ListView listView;
    String TAG="FollowActivity";
    Gson gson=new Gson();
    List<Cat> cats=new ArrayList<>();
    Handler handler;
    Map<String,String> map=new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_cat);
        listView=findViewById(R.id.list_follow);
        map.put("uid","1");
        Log.e(TAG, "onCreate: 123" );
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, "handleMessagecat: "+msg.obj );
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(msg.obj.toString()).getAsJsonArray();
                for (JsonElement element : jsonarray) {
                    cats.add(gson.fromJson(element, Cat.class));
                }
                Log.e(TAG, "handleMessage: "+cats.toString() );
                Adapter adapter=new FollowAdapter(FollowActivity.this,cats,R.layout.item_follow_cat);
                listView.setAdapter((ListAdapter) adapter);
            }
        };
        OkHttpUtils.getInstance().postForm(FIND_SUB_CAT, map, new Callback() {
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





    }
}
