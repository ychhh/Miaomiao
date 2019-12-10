package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

public class CommentViewHolder extends BaseViewHolder {

    private boolean liked;



    public CommentViewHolder(View view) {
        super(view);
        liked = false;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
