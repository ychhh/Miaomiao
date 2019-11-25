package com.hbsd.rjxy.miaomiao.zlc.vedio.view;


import android.view.View;

/**
 *
 * 主页的视图层接口
 *
 */
public interface IMainView {

    public View getTabSpecView(int i);//初始化tabSpec

    public void mTranslucent();//透明状态栏

    public void initTab();//初始化选项卡

    public void updateTabHost(View v);//更新选项卡
}
