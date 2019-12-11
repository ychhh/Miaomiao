package com.hbsd.rjxy.miaomiao.ych.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCatsAdapter extends BaseQuickAdapter<Cat, BaseViewHolder> {

    private Context context;
    public MyCatsAdapter(int layoutResId, @Nullable List<Cat> data,Context context) {
        super(layoutResId, data);
        Log.e(TAG, "MyCatsAdapter: 11111111111111111" );
        this.context=context;
    }


    public MyCatsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Cat item) {
        Glide.with(context).load(item.getHid()).into((ImageView) helper.getView(R.id.img_cat1));
//        Glide.with(context).load(item.getHid()).into((ImageView) helper.getView(R.id.img_cat2));
        helper.getView(R.id.img_cat1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("cid",item.getCid());
                Log.e(TAG, "convert: 跳转" );
            }
        });

//        context.startActivity();
    }

//    @Override
//    protected void convert(@NonNull BaseViewHolder helper, Map<String, String> item) {
//        Log.e(TAG, "MyCatsAdapter: 2222222222" );
//        Glide.with(context).load(item.get("0")).into((ImageView) helper.getView(R.id.img_cat1));
//        Glide.with(context).load(item.get("1")).into((ImageView) helper.getView(R.id.img_cat2));
//        Glide.with(context).load(item.get("2")).into((ImageView) helper.getView(R.id.img_cat3));
//        helper.getView(R.id.img_cat1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent;
//            }
//        });
//    }

}
