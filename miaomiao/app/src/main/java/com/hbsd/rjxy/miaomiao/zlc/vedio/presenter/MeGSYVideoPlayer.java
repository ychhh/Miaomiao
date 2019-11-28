package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;

import com.hbsd.rjxy.miaomiao.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import moe.codeest.enviews.ENPlayView;

public class MeGSYVideoPlayer extends StandardGSYVideoPlayer {

    ImageView imageView;

    public MeGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    protected void onClickUiToggle() {
        super.onClickUiToggle();
    }


    @Override
    protected void updateStartImage() {
        super.updateStartImage();
        if (mStartButton instanceof ENPlayView) {
            ENPlayView enPlayView = (ENPlayView) mStartButton;
            enPlayView.setDuration(500);
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                enPlayView.play();
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                enPlayView.pause();
            } else {
                enPlayView.pause();
            }
            enPlayView.setVisibility(GONE);
        } else if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_error_selector);
            } else {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            }
            imageView.setVisibility(GONE);
        }
    }

    @Override
    public void onSurfaceUpdated(Surface surface) {
        super.onSurfaceUpdated(surface);
        if(imageView != null && imageView.getVisibility()== View.VISIBLE){
            imageView.setVisibility(INVISIBLE);
        }
    }

    
    public void getImageView(ImageView imageView){
        this.imageView = imageView;
    }

    //写一个方法把imageView恢复显示
    public void visibleImage(){
        this.imageView.setVisibility(VISIBLE);
    }
}
