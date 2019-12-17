package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Comment;
import com.hbsd.rjxy.miaomiao.entity.RecordLikes;
import com.hbsd.rjxy.miaomiao.ljt.login.PhoneLoginActivity;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.utils.TimeUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.PleaseLoginActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.LOGIN_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_DISLIKE_COMMENT;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_LIKE_COMMENT;

/*
    TODO    /comment/like  和  /comment/like 点赞逻辑的完善


 */

public class CommentAdapter extends BaseQuickAdapter<Comment, CommentViewHolder> {

    private Context context;
    private String currentTime;


    //每次点完赞或者取消点赞，我都要更新这个！！！！！！！
    private List<RecordLikes> recordLikes;


    private String currentId;
    private int miid;

    Gson gson = new Gson();


    public CommentAdapter(int layoutResId, @Nullable List<Comment> data, Context context, String currentTime, @Nullable List<RecordLikes> recordLikes,@Nullable String currentId,int miid) {
        super(layoutResId, data);
        this.context = context;
        this.currentTime = currentTime;
        this.recordLikes = recordLikes;
        if(currentId!=null){
            this.currentId = currentId;
        }
        this.miid = miid;
        Log.e("currentTime",""+currentTime);
    }

    @Override
    protected void convert(@NonNull CommentViewHolder helper, Comment item) {


        /*
            TODO    头像path为空就使用默认的系统头像
                根据uid查询用户的头像和username
         */

        if(item.getUhead().equals("")||item.getUhead() == null){
            Glide.with(context)
                    .load(R.drawable.u45)
                    .override(Target.SIZE_ORIGINAL)
                    .into((ImageView) helper.getView(R.id.civ_comment_head));
        }else{
            Glide.with(context)
                    .load(item.getUhead())
                    .override(Target.SIZE_ORIGINAL)
                    .into((ImageView) helper.getView(R.id.civ_comment_head));
        }

        helper.setText(R.id.comment_uname,item.getUname());


        helper.setText(R.id.tv_comment_content,item.getCocontent());


        /*
            TODO    时间的逻辑判断
                （1）同一天同一小时，判断多少分钟以前
                            一分钟以内是‘刚刚’
                （2）同一天不同小时，判断多少小时以前
                （3）不同天，判断多少天以前
         */
        TimeUtils timeUtils = new TimeUtils(currentTime);
        helper.setText(R.id.tv_comment_publish_time,timeUtils.compareTime(item.getPublishTime()));


        int flag = 0;
        for(int i = 0 ; i < recordLikes.size();i++){
            if(recordLikes.get(i).getCoid() == item.getCoid()){
                flag = 1;
                helper.setImageBitmap(R.id.iv_comment_like, BitmapFactory.decodeResource(context.getResources(),R.drawable.comment_like_pressed));
                helper.setLiked(true);
                Log.e("设置了",item.getCocontent()+"''''''"+"点赞");
            }
        }
        if(flag == 0){
            helper.setImageBitmap(R.id.iv_comment_like, BitmapFactory.decodeResource(context.getResources(),R.drawable.comment_like_unpressed));
            helper.setLiked(false);
            Log.e("设置了",item.getCocontent()+"''''''"+"没点赞");
        }



        helper.getView(R.id.iv_comment_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    TODO    用户点击喜爱的逻辑
                        先判断是否登录
                 */
                //登录判断
                SharedPreferences sp = context.getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
                String uid = sp.getString("uid","default");
                if(uid.equals("default")) {
                    context.startActivity(new Intent(context, PhoneLoginActivity.class));
                }else{
                    if (helper.isLiked()) {
                    /*
                        TODO    先判断是否有锁，没有锁，上锁，开始请求
                     */
                        if (helper.isLikeLock()) {
                            //当前有锁
                        } else {
                            //当前无锁
                            helper.setImageBitmap(R.id.iv_comment_like, BitmapFactory.decodeResource(context.getResources(), R.drawable.comment_like_unpressed));
                            item.setColike(item.getColike() - 1);
                            if (item.getColike() != 0) {
                                helper.setText(R.id.comment_like_account, item.getColike() + "");
                            } else {
                                helper.setText(R.id.comment_like_account, "");
                            }
                            helper.setLiked(false);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("coid", item.getCoid());
                                jsonObject.put("uid", currentId);
                                jsonObject.put("miid", miid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            OkHttpUtils.getInstance().postJson(URL_DISLIKE_COMMENT, jsonObject.toString(), new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                    recordLikes = gson.fromJson(response.body().string(), new TypeToken<List<RecordLikes>>() {
                                    }.getType());
                                    helper.setLikeLock(false);

                                }
                            });
                        }
                    } else {
                        if (helper.isLikeLock()) {
                            //当前有锁
                        } else {
                            helper.setImageBitmap(R.id.iv_comment_like, BitmapFactory.decodeResource(context.getResources(), R.drawable.comment_like_pressed));
                            item.setColike(item.getColike() + 1);
                            helper.setText(R.id.comment_like_account, item.getColike() + "");
                            helper.setLiked(true);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("coid", item.getCoid());
                                jsonObject.put("uid", currentId);
                                jsonObject.put("miid", miid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OkHttpUtils.getInstance().postJson(URL_LIKE_COMMENT, jsonObject.toString(), new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    recordLikes = gson.fromJson(response.body().string(), new TypeToken<List<RecordLikes>>() {
                                    }.getType());
                                    helper.setLikeLock(false);


                                }
                            });
                        }

                    }
                }



            }
        });

        /*
            TODO    评论的点赞数判断逻辑
                <1000时不用缩写
                >1000时使用1.1k
         */
        if(item.getColike() >= 1000){

        }
        if(item.getColike() != 0){
            helper.setText(R.id.comment_like_account,item.getColike()+"");
        }else{
            helper.setText(R.id.comment_like_account,"");
        }


    }
}
