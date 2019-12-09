package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Comment;

import java.util.List;

/*
    TODO    /comment/like  和  /comment/like 点赞逻辑的完善


 */

public class CommentAdapter extends BaseQuickAdapter<Comment, CommentViewHolder> {

    private Context context;


    public CommentAdapter(int layoutResId, @Nullable List<Comment> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull CommentViewHolder helper, Comment item) {
        helper.setText(R.id.tv_comment_content,item.getCocontent());




        helper.getView(R.id.iv_comment_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    TODO    用户点击喜爱的逻辑
                 */
                if(helper.isLiked()){
                    helper.setImageBitmap(R.id.iv_comment_like, BitmapFactory.decodeResource(context.getResources(),R.drawable.comment_like_unpressed));
                    if(item.getColike() != 0){
                        helper.setText(R.id.comment_like_account,item.getColike()+"");
                    }else{
                        helper.setText(R.id.comment_like_account,"");
                    }
                    helper.setLiked(false);
                }else{
                    helper.setImageBitmap(R.id.iv_comment_like, BitmapFactory.decodeResource(context.getResources(),R.drawable.comment_like_pressed));
                    helper.setText(R.id.comment_like_account,(item.getColike()+1)+"");
                    helper.setLiked(true);
                }
            }
        });

        /*
            TODO    评论的点赞数判断逻辑
                <1000时不用缩写
                >1000时使用1.1k
         */
        if(item.getColike() >= 1000){

        }
        if(item.getColike() != 0){
            helper.setText(R.id.comment_like_account,item.getColike()+"");
        }else{
            helper.setText(R.id.comment_like_account,"");
        }


    }
}
