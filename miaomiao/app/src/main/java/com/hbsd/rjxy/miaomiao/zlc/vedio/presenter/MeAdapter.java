package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Muti_infor;

import java.util.List;

public class MeAdapter extends BaseQuickAdapter<Muti_infor,MeViewHolder> {

    private Context context;

    public MeAdapter(int layoutResId, @Nullable List data,Context context) {
        super(layoutResId, data);
        this.context = context;
    }


    @Override
    protected void convert(final MeViewHolder helper, Muti_infor item) {
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

//        helper.gsyVideoPlayer.loadCoverImage("http://www.zin4ever.top/924Cakeprj/images/dreamcake.png",R.drawable.thumbimage);

        Glide.with(context)
                .load(item.getMiCover())
                .into(helper.iv_thumb);
//        helper.iv_thumb.setImageResource(R.mipmap.thumbimage1);
//        helper.gsyVideoPlayer.getImageView(helper.iv_thumb);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}


