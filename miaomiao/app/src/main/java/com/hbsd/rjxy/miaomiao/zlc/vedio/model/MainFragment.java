package com.hbsd.rjxy.miaomiao.zlc.vedio.model;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zlc.vedio.view.IMainFragmentView;


public class MainFragment extends Fragment implements IMainFragmentView {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initFragment(inflater,container);




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
}
