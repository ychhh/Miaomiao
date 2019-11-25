package com.hbsd.rjxy.miaomiao.zlc.vedio.model;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Muti_infor;
import com.hbsd.rjxy.miaomiao.utils.ScrollCalculatorHelper;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.MeAdapter;
import com.hbsd.rjxy.miaomiao.zlc.vedio.view.IMainFragmentView;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import java.util.List;


public class MainFragment extends Fragment implements IMainFragmentView {


    private RecyclerView recyclerView;
    private MeAdapter adapter;
    private List<Muti_infor> videoList;         //这里只显示视频，所以是videoList
    ScrollCalculatorHelper scrollCalculatorHelper ;
    private int playTop;
    private int playBottom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initFragment(inflater,container);

        recyclerView = view.findViewById(R.id.rv_main);

        initPlayPosition(getContext());



        return view;
    }


    /**
     * 初始化fragment的视图
     * @param inflater
     * @param container
     * @return
     */
    @Override
    public View initFragment(LayoutInflater inflater,ViewGroup container) {
        View newView = inflater.inflate(
                R.layout.main_fragment,
                container,
                false
        );
        return newView;
    }

    @Override
    public void initPlayPosition(Context context) {
        //限定范围为屏幕一半的上下偏移180
        playTop = CommonUtil.getScreenHeight(context) / 2 - CommonUtil.dip2px(context, 300);
        playBottom = CommonUtil.getScreenHeight(context) / 2 + CommonUtil.dip2px(context, 300);


    }
}
