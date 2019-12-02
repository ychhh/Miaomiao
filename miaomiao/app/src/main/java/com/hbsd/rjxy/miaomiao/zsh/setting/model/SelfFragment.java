package com.hbsd.rjxy.miaomiao.zsh.setting.model;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hbsd.rjxy.miaomiao.R;

public class SelfFragment extends Fragment {
    private ActionBarDrawerToggle drawerbar;
    private DrawerLayout main_drawer_layout;
    private LinearLayout main_right_drawer_layout;
    private View view;
    private Button btn_setting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(
                R.layout.activity_main_setting,
                container,
                false
        );

        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initEvent();
    }
    private void initView(){
        //'我的'界面
        main_drawer_layout= view.findViewById(R.id.main_self_drawer_setting);
        //右边菜单
        main_right_drawer_layout=view.findViewById(R.id.main_self_Right_drawer_layout);

        //设置菜单内容之外其他区域的背景色
       // main_drawer_layout.setScrimColor(Color.rgb(0,0,0));
        //setting键
        btn_setting=view.findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRightLayout(v);
            }
        });

    }
    private void initEvent(){
        drawerbar=new ActionBarDrawerToggle(getActivity(),main_drawer_layout,R.string.open,R.string.close);

    }
    public void openRightLayout(View view){
        if(main_drawer_layout.isDrawerOpen(main_right_drawer_layout)){
            main_drawer_layout.closeDrawer(main_right_drawer_layout);
        }else{
            main_drawer_layout.openDrawer(main_right_drawer_layout);
        }
    }






    }


