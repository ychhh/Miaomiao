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

public class MyCatsAdapter extends BaseQuickAdapter<List<Cat>, BaseViewHolder> {

    private Context context;

    public MyCatsAdapter(int layoutResId, @Nullable List<List<Cat>> data, Context context) {
        super(layoutResId, data);
        Log.e(TAG, "MyCatsAdapter: " + data);
        this.context = context;

    }

    public MyCatsAdapter(@Nullable List<List<Cat>> data) {
        super(data);
    }

    public MyCatsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, List<Cat> item) {
        Log.e(TAG, "convert: item:" + item.size());
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i) != null) {
                Log.e(TAG, "convert i: " + i);
                if (i%3 == 0)
                    Glide.with(context).load(item.get(i).getHpath()).into((ImageView) helper.getView(R.id.img_cat1));
                if (i%3 == 1)
                    Glide.with(context).load(item.get(i).getHpath()).into((ImageView) helper.getView(R.id.img_cat2));
                if (i%3 == 2)
                    Glide.with(context).load(item.get(i).getHpath()).into((ImageView) helper.getView(R.id.img_cat3));
            }
        }
//


//    @Override
//    protected void convert(@NonNull BaseViewHolder helper, Cat item) {
//        if (item.getCid()%3==0){
//        Glide.with(context).load(item.getHid()).into((ImageView) helper.getView(R.id.img_cat1));}
//        else if (item.getCid()%3==1){
//            Glide.with(context).load(item.getHid()).into((ImageView) helper.getView(R.id.img_cat2));
//        }else {
//            Glide.with(context).load(item.getHid()).into((ImageView) helper.getView(R.id.img_cat3));
//        }
//        helper.getView(R.id.img_cat1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.putExtra("cid",item.getCid());
//                Log.e(TAG, "convert: 跳转"+item.getCid() );
//            }
//        });

    }
}