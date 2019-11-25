package com.hbsd.rjxy.miaomiao.zlc.vedio.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface IMainFragmentView {

    public View initFragment(LayoutInflater inflater, ViewGroup container);
    public void initPlayPosition(Context context);

}
