package com.hbsd.rjxy.miaomiao.zlc.vedio.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.MeAdapter;

public interface IMainFragmentView {

    View initFragment(LayoutInflater inflater, ViewGroup container);

    void initPlayPosition(Context context);

    RecyclerView initRecyclerView();

    MeAdapter initAdapter();

    void setTextViewColor(TextView selectedView, TextView unselectedView);


}
