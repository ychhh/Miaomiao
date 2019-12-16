package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.R;

import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import com.hbsd.rjxy.miaomiao.utils.MeBufferReader;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.InfoAndCommentActivity;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.ADD_HOT_URl;
import static com.hbsd.rjxy.miaomiao.utils.Constant.LOGIN_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_GET_CAT;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_SUBSCRIBE_CAT;

public class MeAdapter extends BaseQuickAdapter<Multi_info, MeViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Subscription_record> subscriptionRecords;
    private List<Cat> cats;
    Gson gson = new Gson();

    public MeAdapter(int layoutResId, @Nullable List data, Context context, List<Subscription_record> subscriptionRecords) {
        super(layoutResId, data);
        this.context = context;
        if (subscriptionRecords != null) {
            this.subscriptionRecords = subscriptionRecords;
        }
        cats = new ArrayList<>();
    }


    @Override
    protected void convert(final MeViewHolder helper, Multi_info item) {

        //设置视频小鱼干数量  评论数量
        helper.setText(R.id.tv_video_fish, "" + item.getMhot()).setText(R.id.tv_comment_amount, "" + item.getMcomment_count());

        if (subscriptionRecords != null) {
            int flag = 0;
            for (Subscription_record subscriptionRecord : subscriptionRecords) {
                if (subscriptionRecord.getCid() == item.getCid()) {
                    helper.getView(R.id.iv_subscribe_plus).setVisibility(View.INVISIBLE);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                helper.getView(R.id.iv_subscribe_plus).setVisibility(View.VISIBLE);
                helper.getView(R.id.iv_subscribe_plus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                /*
                    TODO:   （1）先判断是否登录了，没登录，去登陆！
                            （2）已经登陆了，那之前在fragment里一定拿到过订阅列表的信息
                            （3）对比订阅列表的cid和本视频的cid，判断是否显示这个订阅的视图
                            （4）订阅：
                                        （1）动画效果，（2）订阅的业务逻辑

                 */

                        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        animator.setDuration(1000);
                        animator.start();
                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0f);
                        animator1.setDuration(1500);
                        animator1.start();
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(v, "scaleY", 1f, 0f);
                        animator2.setDuration(1500);
                        animator2.start();
                        animator2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                v.setVisibility(View.INVISIBLE);
                                ObjectAnimator animator3 = ObjectAnimator.ofFloat(helper.getView(R.id.iv_subscribe_success), "alpha", 0f, 1f, 0f);
                                animator3.setDuration(1000);
                                animator3.start();
                                helper.getView(R.id.iv_subscribe_success).setVisibility(View.VISIBLE);
                                animator3.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        helper.getView(R.id.iv_subscribe_success).setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        });

                        SharedPreferences sp = context.getSharedPreferences(LOGIN_SP_NAME, Context.MODE_PRIVATE);
                        String uid = sp.getString("uid", "1");
                        Map<String, String> map = new HashMap<>();
                        map.put("uid", uid);
                        map.put("cid", item.getCid() + "");
                        OkHttpUtils.getInstance().postForm(URL_SUBSCRIBE_CAT, map, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }


                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                subscriptionRecords =
                                        gson.fromJson(response.body().string(), new TypeToken<List<Subscription_record>>() {
                                        }.getType());
                                Log.e("updated SubList", "" + subscriptionRecords.toString());

                                //我要更新这个subscriptionRecords
                            }
                        });


                    }
                });
            }
        }


        helper.setText(R.id.tv_videocontent, item.getMcontent());




        int cid = item.getCid();
        //通过cid请求头像

        Glide.with(context)
                .load(R.drawable.u45)
                .into((ImageView) helper.getView(R.id.iv_cathead));
        new GetChead(item.getCid(),helper,context).execute();
        helper.getView(R.id.iv_cathead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(createIntent(item, 0));
            }
        });

        //设置小鱼干的点击事件
        helper.getView(R.id.iv_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击小鱼干，发送请求  判断是否登录过

                //判断已经登陆过
                item.setMhot(item.getMhot() + 1);
                helper.setText(R.id.tv_video_fish, "" + item.getMhot());
                new FeedPresenter(context, item).execute();
            }
        });

        //设置订阅的点击事件
        helper.getView(R.id.iv_subscribe).setOnClickListener(this::onClick);

        //设置评论的点击事件
        helper.getView(R.id.iv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(createIntent(item, 1));
            }
        });

        //设置评论数量
        helper.setText(R.id.tv_comment_amount, item.getMcomment_count() + "");


        helper.gsyVideoPlayer.setUpLazy(item.getMpath(), true, null, null, "title");
        //标题    不可见
        helper.gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //返回摁钮不可见
        helper.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按钮功能  不可见
        helper.gsyVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        helper.gsyVideoPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        helper.gsyVideoPlayer.setReleaseWhenLossAudio(false);

        //小屏时不触摸滑动
        helper.gsyVideoPlayer.setIsTouchWiget(false);

        //设置循环播放
        helper.gsyVideoPlayer.setLooping(true);

        helper.gsyVideoPlayer.changeUiToNormal();

        //加载封面
        Glide.with(context)
                .load(item.getMcover())
                .into(helper.iv_thumb);
        helper.gsyVideoPlayer.setImageView(helper.iv_thumb);

        //设置双击显示小鱼
        helper.gsyVideoPlayer.setRelativeLayout(helper.getView(R.id.popFish));
    }

    /*
        TODO    type传0是点击头像，1点击评论图片
     */
    private Intent createIntent(Multi_info item, int type) {
        Intent intent = new Intent(context, InfoAndCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("miid", item.getMiid());
        bundle.putSerializable("cid", item.getCid());
        if (type == 0) {
            bundle.putSerializable("from", "head");
        } else if (type == 1) {
            bundle.putSerializable("from", "commentPic");
        }
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onViewRecycled(@NonNull MeViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_subscribe:
                //点击订阅 do something
                Toast.makeText(context, "这是订阅", Toast.LENGTH_SHORT).show();
                break;


        }
    }
}

class GetChead extends AsyncTask<Object,Object,String>{

    GetChead(int cid ,MeViewHolder helper,Context context){
        this.cid = cid;
        this.helper = helper;
        this.context= context;
    }
    private MeViewHolder helper;
    private int cid;
    private Context context;
    Gson gson = new Gson();

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Cat cat = gson.fromJson(s,Cat.class);
        Glide.with(context)
                .load(cat.getHpath())
                .into((ImageView) helper.getView(R.id.iv_cathead));
        Log.e("asda",""+cat.getHpath());

    }

    @Override
    protected String doInBackground(Object... objects) {
        try {
            URL url = new URL(URL_GET_CAT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            OutputStream os = con.getOutputStream();
            JSONObject jo = new JSONObject();
            jo.put("cid",cid);
            os.write(jo.toString().getBytes());


            InputStream is = con.getInputStream();
            byte[] buffer = MeBufferReader.readInputStream(is);
            return new String(buffer);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}


