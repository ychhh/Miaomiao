package com.hbsd.rjxy.miaomiao.ych.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.CircleImageView;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.wq.CatFCActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.FIND_SUB_CAT;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_UNFOLLOW_CAT;

public class FollowActivity extends Activity {
    ListView listView;
    String TAG="FollowActivity";
    Gson gson=new Gson();
    List<Cat> cats=new ArrayList<>();
    Handler handler;
    BaseAdapter adapter=null;
    Map<String,String> map=new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_cat);
        listView=findViewById(R.id.list_follow);
//        img_back=findViewById(R.id.follow_back);
        map.put("uid","1");
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.e(TAG, "handleMessagecat: "+msg.obj );
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(msg.obj.toString()).getAsJsonArray();
                for (JsonElement element : jsonarray) {
                    Cat cat= gson.fromJson(element, Cat.class);
                    cats.add(cat);
                }
                Log.e(TAG, "handleMessage cats: "+cats.toString() );
                adapter=new FollowAdapter(FollowActivity.this,cats,R.layout.item_follow_cat);
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
    public class FollowAdapter extends BaseAdapter {
        private List<Cat> dataSource=null;
        private Context context=null;
        private int item_layout_id;
        //    private Inflater inflater;
        public FollowAdapter(Context context,
                             List<Cat> dataSource,
                             int item_layout_id) {
            this.context = context;
            this.dataSource = dataSource;

            this.item_layout_id = item_layout_id;
        }
        @Override
        public int getCount() {
            return  dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(context);
            Cat cat=dataSource.get(position);
//        View newView=inflater.inflate(R.layout.activity_follow_cat,null);
            ViewHolder viewHolder=null;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView = inflater.inflate(item_layout_id, null);
                viewHolder.btn_unfollow=(Button) convertView.findViewById(R.id.btn_unfollow);
                viewHolder.cat_head=(CircleImageView) convertView.findViewById(R.id.img_chead);
                viewHolder.cat_intro=(TextView) convertView.findViewById(R.id.tv_cintro);
                viewHolder.cat_name=(TextView)convertView.findViewById(R.id.tv_cname);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.cat_name.setText(cat.getCatName());
            viewHolder.cat_intro.setText(cat.getCatIntro());
            Glide.with(context).load(cat.getCatHead()).into(viewHolder.cat_head);
            viewHolder.cat_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(FollowActivity.this, CatFCActivity.class);
                    intent.putExtra("uid",cat.getId());//todo  uid?cid?
                    startActivity(intent);

                }
            });
            viewHolder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataSource.remove(position);
                    Map<String,String> map=new HashMap<>();
                    map.put("uid","2");
                    map.put("cid","2");
                    Log.e(TAG, "onClick: "+map.get("uid")+map.get("cid") );
                    OkHttpUtils.getInstance().postForm(URL_UNFOLLOW_CAT, map, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(TAG, "onFailure: " );
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.e(TAG, "onResponse: " );
                            Looper.prepare();
                            Toast.makeText(FollowActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder{
            TextView cat_intro;
            TextView cat_name;
            CircleImageView cat_head;
            Button btn_unfollow;
        }
    }
}
