package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.hbsd.rjxy.miaomiao.R;


public class MeViewHolder extends BaseViewHolder {

    public MeGSYVideoPlayer gsyVideoPlayer;
//    public StandardGSYVideoPlayer gsyVideoPlayer;
    public ImageView iv_thumb;

    public MeViewHolder(View view) {
        super(view);
        gsyVideoPlayer = view.findViewById(R.id.videoPlayer);
        iv_thumb = view.findViewById(R.id.iv_thumb);
    }
}
