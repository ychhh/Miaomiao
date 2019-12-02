package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbsd.rjxy.miaomiao.R;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;

import moe.codeest.enviews.ENPlayView;

public class MeGSYVideoPlayer extends StandardGSYVideoPlayer{

    ImageView imageView;
    RelativeLayout relativeLayout;
    boolean isDoubleClicking = false;
    Thread mThread;

    public MeGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeUiToClear();
    }

    //双击逻辑      //双击一次之后继续点击会有小鱼干出来
    @Override
    protected void touchDoubleUp() {
        relativeLayout.setVisibility(VISIBLE);
        if(mThread == null || !mThread.isAlive()){
            mThread = new Thread(){
                @Override
                public void run() {
                    try {
                        isDoubleClicking = true;
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    relativeLayout.setVisibility(INVISIBLE);
                    isDoubleClicking = false;
                }
            };
            mThread.start();
        }
    }

    @Override
    protected void onClickUiToggle() {
        //单击逻辑替换成双击暂停逻辑
        if (!mHadPlay) {
            return;
        }
        if(mCurrentState == CURRENT_STATE_PLAYING && !isDoubleClicking){
            //如果正在播放
            try {
                onVideoPause();
                changeUiToClear();//去掉ui的变化
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(mCurrentState == CURRENT_STATE_PAUSE && !isDoubleClicking){
            //如果是暂停的状态
            startAfterPrepared();
            changeUiToClear();//去掉ui的变化
        }

        //因为调用的是播放按钮点击的逻辑方法
//        clickStartIcon();
    }


    //去掉中心开始暂停按钮
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
            enPlayView.setVisibility(INVISIBLE);
        } else if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_error_selector);
            } else {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            }
            imageView.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onSurfaceUpdated(Surface surface) {

        super.onSurfaceUpdated(surface);
        if(imageView != null && imageView.getVisibility()== View.VISIBLE){
            imageView.setVisibility(INVISIBLE);
        }

    }

    
    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }
    public void setRelativeLayout(RelativeLayout relativeLayout){this.relativeLayout = relativeLayout;}

    //写一个方法把imageView恢复显示
    public void visibleImage(){
        this.imageView.setVisibility(VISIBLE);
    }


    //去掉进度条

    @Override
    public void onPrepared() {
        super.onPrepared();
        changeUiToClear();
    }


    @Override
    protected void changeUiToNormal() {
        changeUiToClear();
    }

    @Override
    protected void changeUiToPreparingShow() {
        changeUiToClear();
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        changeUiToClear();
    }


}
