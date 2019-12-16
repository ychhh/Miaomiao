package com.hbsd.rjxy.miaomiao.zsh.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;

import com.hbsd.rjxy.miaomiao.ljt.login.PhoneLoginActivity;
import com.hbsd.rjxy.miaomiao.utils.Constant;

import com.hbsd.rjxy.miaomiao.ych.view.FollowActivity;

import com.hbsd.rjxy.miaomiao.zsh.setting.model.AddItemAdapter;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserPresenterCompl;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.SelfMainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/*
    TODO
        我的界面由fragment碎片组成，Activity依托zlc的MainActivity
        <!--initData-->
        通过sp获取uid,通过GetUserPresenter的实现者SelfMainView来获取并返回user
        <!--initView-->
        对右侧抽屉的初始化,运用自定义的AddItemAdapter
        <!---refresh-->
        利用从EditUserPresenterCompl中传过来的修改后的Json(user),进行当下用户的刷新

*/
public class SelfFragment extends Fragment implements SelfMainView {
    public View view;
    public static final String[] TITLES = { "First", "Second" };
    private DrawerLayout mDrawer_layout;//DrawerLayout容器
    private RelativeLayout mMenu_layout_right;//右边抽屉
    private ArrayList<Map<String, Object>> listItems=null;
    private AddItemAdapter adapter =null;
    private Button btn_setting;
    private Button btn_editF;
    private Button tx_order;
    private Button btn_self_pwd;
    private GetUserPresenterCompl getUserPresenterCompl;
    private User user;
    private  TextView tx_intro;
    private ListView menu_listview_r;
    private Gson gson;
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.self_main,
                container,
                false
        );

        if(EventBus.getDefault().isRegistered(this)) {

        }
        else{
            EventBus.getDefault().register(this);
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*控件*/

        mDrawer_layout = view.findViewById(R.id.drawer_layout);
        mMenu_layout_right =  view.findViewById(R.id.menu_layout_right);
        menu_listview_r = mMenu_layout_right.findViewById(R.id.menu_listView_r);
        btn_setting=view.findViewById(R.id.btn_setting);
        btn_editF=view.findViewById(R.id.btn_editF);
        tx_order=view.findViewById(R.id.self_order);

        btn_self_pwd=view.findViewById(R.id.btn_self_pwd);

        tx_intro=view.findViewById(R.id.self_main_intro);

        getUserPresenterCompl=new GetUserPresenterCompl(this);

        user=new User();
        gson=new Gson();
        /*通过sp获取当下user的uid*/
        sp=this.getActivity().getSharedPreferences(Constant.LOGIN_SP_NAME,MODE_PRIVATE);
        int uid=Integer.parseInt(sp.getString("uid","8"));
        uid=8;
        user.setUserId(uid);

        /*初始化UserData*/
        initData();
        /*初始化抽屉*/
        initDrawerList();
        menu_listview_r.setAdapter(adapter);

        /*监听setting按钮*/
        initEvent();



        //监听菜单
        menu_listview_r.setOnItemClickListener(new DrawerItemClickListenerRight());




    }

    public void initData(){
        getUserPresenterCompl.getUser(user.getUserId());

    }


    /*实现接口方法，获取当下的用户信息*/
    @Override
    public void initUserView(User user0) {

        user=user0;

        if(tx_intro!=null){
            Log.e("user",user.getUserName()+user.getUserIntro()+user.getUserSex());
            tx_intro.setText(user.getUserIntro());
        }


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
    /*实现全局按钮的监听*/
    private void initEvent(){

        ButtonClickListener buttonClickListener=new ButtonClickListener();
        btn_setting.setOnClickListener(buttonClickListener);
        btn_editF.setOnClickListener(buttonClickListener);
        tx_order.setOnClickListener(buttonClickListener);
        btn_self_pwd.setOnClickListener(buttonClickListener);
    }


    /*全局监听类*/
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
                    if(user.getUserId()==0){
                        Intent intent=new Intent(getActivity(), PhoneLoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);

                        String str = gson.toJson(user);
                        intent.putExtra("user", str);
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.self_order:{


                    /*TODO
                        我的订阅
                    * */


                    Intent intent=new Intent(getActivity(), FollowActivity.class);
                    intent.putExtra("uid",user.getUserId());
                    startActivity(intent);
                    break;
                }
                case R.id.btn_self_pwd:{
                    /*TODO
                        修改密码
                    * */

                    if(sp.getString(Constant.LOGIN_SP_NAME,"null").equals("null")){
                        Intent intent=new Intent(getActivity(), EditPwdWithoutOldActivity.class);
                        String str = gson.toJson(user);
                        intent.putExtra("user", str);
                        startActivity(intent);
                    }
                    else{
                        Intent intent=new Intent(getActivity(), EditPwdWithOldActivity.class);
                        String str = gson.toJson(user);
                        intent.putExtra("user", str);
                        startActivity(intent);
                         }

                }


            }

        }
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(String str){
        Log.e("event=",str);
        user=gson.fromJson(str,User.class);
        refresh();
    }


    @Override
    public void refresh() {


        tx_intro.setText(user.getUserIntro());






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
            //根据行号判断所选为哪个item
            if(user.getUserId()==0){
                Intent intent=new Intent(getActivity(), PhoneLoginActivity.class);
                startActivity(intent);
            }
            else{
                switch (position)
                {
                    case 0:
                        Intent intent0=new Intent(getActivity(), ShowCardActivity.class);
                        Gson gson=new Gson();
                        String str=gson.toJson(user);
                        intent0.putExtra("user",str);
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



}