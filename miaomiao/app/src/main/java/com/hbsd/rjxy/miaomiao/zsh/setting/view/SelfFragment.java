package com.hbsd.rjxy.miaomiao.zsh.setting.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zsh.setting.model.AddItemAdapter;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.EditProfileActivity;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserPresenterCompl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SelfFragment extends Fragment {
    public View view;
    public static final String[] TITLES = { "First", "Second" };
    private DrawerLayout mDrawer_layout;//DrawerLayout容器
    private RelativeLayout mMenu_layout_right;//右边抽屉
    private ArrayList<Map<String, Object>> listItems=null;
    private AddItemAdapter adapter =null;
    private Button btn_setting;
    private Button btn_editF;
    private Button tx_order;
    private GetUserPresenterCompl getUserPresenterCompl;
    private Integer uid;
    private  TextView tx_intro;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.self_main,
                container,
                false
        );
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDrawer_layout = view.findViewById(R.id.drawer_layout);
        mMenu_layout_right =  view.findViewById(R.id.menu_layout_right);
        ListView menu_listview_r = mMenu_layout_right.findViewById(R.id.menu_listView_r);
        btn_setting=view.findViewById(R.id.btn_setting);
        btn_editF=view.findViewById(R.id.btn_editF);
        tx_order=view.findViewById(R.id.self_order);
        tx_intro=view.findViewById(R.id.self_intro);

        initDrawerList();
        initUserData();
        Activity activity=this.getActivity();

//        Log.e("获取到用户信息",user.getUserName()+"2019年12月7日");
        menu_listview_r.setAdapter(adapter);
        //监听setting按钮
        initEvent();



        //监听菜单
        menu_listview_r.setOnItemClickListener(new DrawerItemClickListenerRight());


        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){

                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);


    }
    public void refresh(){
        mDrawer_layout = view.findViewById(R.id.drawer_layout);
        mMenu_layout_right =  view.findViewById(R.id.menu_layout_right);
        ListView menu_listview_r = mMenu_layout_right.findViewById(R.id.menu_listView_r);
        btn_setting=view.findViewById(R.id.btn_setting);
        btn_editF=view.findViewById(R.id.btn_editF);
        tx_order=view.findViewById(R.id.self_order);
        tx_intro=view.findViewById(R.id.self_intro);

        initDrawerList();
        initUserData();
        Activity activity=this.getActivity();

//        Log.e("获取到用户信息",user.getUserName()+"2019年12月7日");
        menu_listview_r.setAdapter(adapter);
        //监听setting按钮
        initEvent();



        //监听菜单
        menu_listview_r.setOnItemClickListener(new DrawerItemClickListenerRight());

    }


    public void initUserData(){
        /*获取uid*/
       // sharedPreferences=this.getActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        //String id= (String) sharedPreferences.getString("uid");
        uid=8;
        getUserPresenterCompl=new GetUserPresenterCompl(this.getActivity());
        getUserPresenterCompl.getUser(uid);

       // Log.e("接收到用户",user.getUserName());

    }
    public void initDrawerList(){
        String[] titles={"个人名片","我的订阅","修改密码","小程序"};

        listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<titles.length;i++){
            Map<String ,Object> map=new HashMap<>();
            map.put("title",titles[i]);
            map.put("map",titles[i]);
            listItems.add(map);
        }
        adapter=new AddItemAdapter(this.getContext(),listItems,R.layout.self_setting_item);



    }
    private void initEvent(){

        ButtonClickListener buttonClickListener=new ButtonClickListener();
        btn_setting.setOnClickListener(buttonClickListener);
        btn_editF.setOnClickListener(buttonClickListener);
        tx_order.setOnClickListener(buttonClickListener);
    }
    public class ButtonClickListener implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_setting:{

                    if(mDrawer_layout.isDrawerOpen(mMenu_layout_right)){
                        mDrawer_layout.closeDrawer(mMenu_layout_right);
                    }
                    else {
                        mDrawer_layout.openDrawer(mMenu_layout_right);
                    }
                    break;
                }
                case R.id.btn_editF:{
                    Intent intent=new Intent(getActivity(), EditProfileActivity.class);
                    intent.putExtra("id",uid);
                    startActivity(intent);

                    break;
                }
                case R.id.self_order:{


                    break;
                }

            }

        }
    }

    /**
     * 右侧列表点击事件
     * @author busy_boy
     *
     */
    private class DrawerItemClickListenerRight implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;

            //根据item点击行号判断启用哪个Fragment
            switch (position)
            {
                case 0:
                    Intent intent0=new Intent(getActivity(),ShowCardActivity.class);
                    Integer uid=8;
                    intent0.putExtra("uid",uid);
                    startActivity(intent0);

                    break;
                case 1:
                    Intent intent1=new Intent(getActivity(),ShowCardActivity.class);
                    startActivity(intent1);
                    break;
                case 2:{
                    Intent intent2=new Intent(getActivity(),ShowCardActivity.class);
                    startActivity(intent2);
                    break;

                }
                case 3:{
                    break;
                }
            }

            mDrawer_layout.closeDrawer(mMenu_layout_right);//关闭mMenu_layout
        }
    }


}
