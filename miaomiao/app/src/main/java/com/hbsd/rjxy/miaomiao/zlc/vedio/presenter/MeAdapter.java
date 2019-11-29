package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Muti_infor;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.InfoAndCommentActivity;

import java.util.List;

public class MeAdapter extends BaseQuickAdapter<Muti_infor,MeViewHolder> implements View.OnClickListener {

    private Context context;

    public MeAdapter(int layoutResId, @Nullable List data,Context context) {
        super(layoutResId, data);
        this.context = context;
    }


    @Override
    protected void convert(final MeViewHolder helper, Muti_infor item) {

        int cid = item.getCatId();
        //通过cid请求头像
        helper.getView(R.id.iv_cathead).setOnClickListener(this::onClick);

        //设置小鱼干的点击事件
        helper.getView(R.id.iv_feed).setOnClickListener(this::onClick);

        //设置订阅的点击事件
        helper.getView(R.id.iv_subscribe).setOnClickListener(this::onClick);

        //设置评论的点击事件
        helper.getView(R.id.iv_comment).setOnClickListener(this::onClick);

        //设置评论数量
        helper.setTag(R.id.tv_comment_amount,item.getMiCommentCount());


        helper.gsyVideoPlayer.setUpLazy(item.getMiPath(),true,null,null,"title");
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
                .load(item.getMiCover())
                .into(helper.iv_thumb);
        helper.gsyVideoPlayer.getImageView(helper.iv_thumb);
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
        switch (v.getId()){
            case R.id.iv_cathead:
                //点击头像  do something
                Toast.makeText(context,"这是头像",Toast.LENGTH_SHORT).show();

                //跳转
                context.startActivity(new Intent(context, InfoAndCommentActivity.class));


                break;

            case R.id.iv_feed:
                //点击小鱼干，发送请求
                Toast.makeText(context,"这是小鱼干",Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_subscribe:
                //点击订阅 do something
                Toast.makeText(context,"这是订阅",Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_comment:
                //点击评论，跳转
                Toast.makeText(context,"这是评论",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}


