package com.hbsd.rjxy.miaomiao.ych.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.utils.CircleImageView;
import com.hbsd.rjxy.miaomiao.ych.view.FollowActivity;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class FollowAdapter extends BaseAdapter {
    private List<Cat> dataSource=null;
    private Context context=null;
    private int item_layout_id;
//    private Inflater inflater;
    public FollowAdapter(Context context,
                         List<Cat> dataSource,
                         int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;

        this.item_layout_id = item_layout_id;
    }
    @Override
    public int getCount() {
        if(dataSource==null){
            return 0;
        }
        else{
            return dataSource.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        Cat cat=dataSource.get(position);
//        View newView=inflater.inflate(R.layout.activity_follow_cat,null);
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.item_follow_cat, null);
            viewHolder.btn_unfollow=(Button) convertView.findViewById(R.id.btn_unfollow);
            viewHolder.cat_head=(CircleImageView) convertView.findViewById(R.id.img_chead);
            viewHolder.cat_intro=(TextView) convertView.findViewById(R.id.tv_cintro);
            viewHolder.cat_name=(TextView)convertView.findViewById(R.id.tv_cname);
        }
        viewHolder.cat_name.setText(cat.getCname());
        viewHolder.cat_intro.setText(cat.getCintro());
        Glide.with(context).load(cat.getHid()).into(viewHolder.cat_head);
        viewHolder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView cat_intro;
        TextView cat_name;
        CircleImageView cat_head;
        Button btn_unfollow;
    }
}
