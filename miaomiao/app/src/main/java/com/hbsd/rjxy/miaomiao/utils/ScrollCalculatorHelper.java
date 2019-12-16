package com.hbsd.rjxy.miaomiao.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import com.hbsd.rjxy.miaomiao.R;

/**
 * 控制自动播放的工具类
 */
public class ScrollCalculatorHelper {
    boolean firstRun = true;
    private int firstVisible = 0;
    private int lastVisible = 0;
    private int visibleCount = 0;
    private int playId;
    private int rangeTop;
    private int rangeBottom;
    private PlayRunnable runnable;

    //++用作封面图的控制
    ImageView imageView;

    private Handler playHandler = new Handler();

    public ScrollCalculatorHelper(int playId, int rangeTop, int rangeBottom) {
        this.playId = playId;
        this.rangeTop = rangeTop;
        this.rangeBottom = rangeBottom;
    }

    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        switch (scrollState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                playVideo(view);
                break;
        }
    }

    public void isNowPlaying(View view){
        imageView = view.findViewById(R.id.iv_thumb);
        //如果当前不是最后一个视频
        if(imageView != null && imageView.getVisibility() == View.INVISIBLE && imageView != null){
            imageView.setVisibility(View.VISIBLE);
        }

    }

    public void onScroll(RecyclerView view, int firstVisibleItem, int lastVisibleItem, int visibleItemCount) {
        //如果第一次执行，播放
        if(firstRun){
            visibleCount = 1;
            firstRun = false;
            return;
        }

        if (firstVisible == firstVisibleItem) {
            return;
        }
        firstVisible = firstVisibleItem;
        lastVisible = lastVisibleItem;
        if(firstVisible == lastVisible){
            visibleCount = 1;
        }

    }



    void playVideo(RecyclerView view) {

        if (view == null) {
            return;
        }

        //++
        imageView = view.findViewById(R.id.iv_thumb);

        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();

        GSYBaseVideoPlayer gsyBaseVideoPlayer = null;

        boolean needPlay = false;
        for (int i = 0; i < visibleCount; i++) {
            if (layoutManager.getChildAt(i) != null && layoutManager.getChildAt(i).findViewById(playId) != null) {
                GSYBaseVideoPlayer player = (GSYBaseVideoPlayer) layoutManager.getChildAt(i).findViewById(playId);
                Rect rect = new Rect();
                player.getLocalVisibleRect(rect);
                int height = player.getHeight();
                //说明第一个完全可视
                if (rect.top == 0 && rect.bottom == height) {
                    gsyBaseVideoPlayer = player;

                    //恢复封面图
                    imageView = layoutManager.getChildAt(0).findViewById(R.id.iv_thumb);
                    if(imageView.getVisibility() == View.INVISIBLE && imageView != null){
                        imageView.setVisibility(View.VISIBLE);
                    }

                    if ((player.getCurrentPlayer().getCurrentState() == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL
                            || player.getCurrentPlayer().getCurrentState() == GSYBaseVideoPlayer.CURRENT_STATE_ERROR)) {
                        needPlay = true;
                    }
                    break;
                }

            }
        }

        if (gsyBaseVideoPlayer != null && needPlay) {
            if (runnable != null) {
                GSYBaseVideoPlayer tmpPlayer = runnable.gsyBaseVideoPlayer;
                Log.e("onScrollStateChanged","onScrollStateChanged");
                playHandler.removeCallbacks(runnable);
                runnable = null;
                if (tmpPlayer == gsyBaseVideoPlayer) {
                    return;
                }
            }
            runnable = new PlayRunnable(gsyBaseVideoPlayer);
            //降低频率
            playHandler.postDelayed(runnable, 100);
        }else{
        }
    }

    private class PlayRunnable implements Runnable {

        GSYBaseVideoPlayer gsyBaseVideoPlayer;

        public PlayRunnable(GSYBaseVideoPlayer gsyBaseVideoPlayer) {
            this.gsyBaseVideoPlayer = gsyBaseVideoPlayer;
        }

        @Override
        public void run() {
            boolean inPosition = false;
            //如果未播放，需要播放
            if (gsyBaseVideoPlayer != null) {
                int[] screenPosition = new int[2];
                gsyBaseVideoPlayer.getLocationOnScreen(screenPosition);
                int halfHeight = gsyBaseVideoPlayer.getHeight() / 2;
                int rangePosition = screenPosition[1] + halfHeight;
                //中心点在播放区域内
                if (rangePosition >= rangeTop && rangePosition <= rangeBottom) {
                    inPosition = true;
                }
                if (inPosition) {
                    //设置状态位，如果允许非wifi情况下自动播放，则直接调用原生的startPlayLogic()方法
//                    startPlayLogic(gsyBaseVideoPlayer, gsyBaseVideoPlayer.getContext());
//                    gsyBaseVideoPlayer.startPlayLogic();
                    startMePlayLogic(gsyBaseVideoPlayer);
                }
            }else{

            }
        }
    }


    /***************************************自动播放的点击播放确认******************************************/
    private void startPlayLogic(GSYBaseVideoPlayer gsyBaseVideoPlayer, Context context) {
        if (!com.shuyu.gsyvideoplayer.utils.CommonUtil.isWifiConnected(context)) {
            //这里判断是否wifi
            showWifiDialog(gsyBaseVideoPlayer, context);
            return;
        }
        startMePlayLogic(gsyBaseVideoPlayer);

    }

    private void showWifiDialog(final GSYBaseVideoPlayer gsyBaseVideoPlayer, Context context) {
        if (!NetworkUtils.isAvailable(context)) {
            Toast.makeText(context, context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.no_net), Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi));
        builder.setPositiveButton(context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                gsyBaseVideoPlayer.startPlayLogic();  包含在下面的方法中了
                startMePlayLogic(gsyBaseVideoPlayer);
            }
        });
        builder.setNegativeButton(context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void startMePlayLogic(GSYBaseVideoPlayer gsyBaseVideoPlayer){
        gsyBaseVideoPlayer.startPlayLogic();

        //播放过程的回调，开始播放隐藏封面
//        gsyBaseVideoPlayer.setVideoAllCallBack( );


    }



}
