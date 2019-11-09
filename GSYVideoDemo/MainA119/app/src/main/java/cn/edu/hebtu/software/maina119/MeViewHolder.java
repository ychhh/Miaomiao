package cn.edu.hebtu.software.maina119;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


public class MeViewHolder extends BaseViewHolder {

    public StandardGSYVideoPlayer gsyVideoPlayer;
    public ImageView iv_thumb;

    public MeViewHolder(View view) {
        super(view);
        gsyVideoPlayer = view.findViewById(R.id.videoPlayer);
        iv_thumb = view.findViewById(R.id.iv_thumb);
    }
}
