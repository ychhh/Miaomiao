package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

public class CommentViewHolder extends BaseViewHolder {

    private boolean liked;
    private boolean likeLock;



    public CommentViewHolder(View view) {
        super(view);
        liked = false;
        likeLock = false;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isLikeLock() {
        return likeLock;
    }

    public void setLikeLock(boolean likeLock) {
        this.likeLock = likeLock;
    }
}
