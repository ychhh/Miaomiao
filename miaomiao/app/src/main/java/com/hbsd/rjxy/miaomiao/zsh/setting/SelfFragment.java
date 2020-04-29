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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String[] TITLES = {"First", "Second"};
    private DrawerLayout mDrawer_layout;//DrawerLayout容器
    private RelativeLayout mMenu_layout_right;//右边抽屉
    private ArrayList<Map<String, Object>> listItems = null;
    private AddItemAdapter adapter = null;
    private Button btn_setting;
    private Button btn_editF;
    private Button tx_order;
    private Button btn_editPwd;
    private GetUserPresenterCompl getUserPresenterCompl;
    private User user;
    private TextView tx_intro;
    private ListView menu_listview_r;
    private Gson gson;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ImageView imgView;
    private String imgUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.self_main,
                container,
                false
        );


        if (EventBus.getDefault().isRegistered(this)) {

        } else {
            EventBus.getDefault().register(this);
        }



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*控件*/

        mDrawer_layout = view.findViewById(R.id.drawer_layout);
        mMenu_layout_right = view.findViewById(R.id.menu_layout_right);
        menu_listview_r = mMenu_layout_right.findViewById(R.id.menu_listView_r);
        btn_setting = view.findViewById(R.id.btn_setting);
        btn_editF = view.findViewById(R.id.btn_editF);
        tx_order = view.findViewById(R.id.self_order);
        btn_editPwd=view.findViewById(R.id.btn_self_edPwd);
        tx_intro = view.findViewById(R.id.self_main_intro);
        imgView=view.findViewById(R.id.img_head);
        getUserPresenterCompl = new GetUserPresenterCompl(this);




        /*通过sp获取当下user的uid*/

        sp = this.getActivity().getSharedPreferences(Constant.LOGIN_SP_NAME, MODE_PRIVATE);

        gson=new Gson();
        int uid=Integer.parseInt(sp.getString("uid","0"));
        if(uid==0){
            Intent intent=new Intent(getActivity(), PhoneLoginActivity.class);
            startActivity(intent);
        }
        user = new User();
        user.setId(uid);

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

    public void initData() {
        getUserPresenterCompl.getUser(user.getId());

    }


    /*实现接口方法，获取当下的用户信息*/
    @Override
    public void initUserView(User user0) {

        user=user0;
        Log.e("user",user.getId()+"和"+user.getUserName()+user.getUserIntro()+user.getUserSex());
        if(tx_intro!=null){
            Log.e("user",user.getUserName()+user.getUserIntro()+user.getUserSex());
            tx_intro.setText(user.getUserName());

        }
        Log.e("打印userPath",user.getHeadId());
        if(!user.getHeadId().equals("null")){
            imgUrl=user.getHeadId();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        RequestOptions options = new RequestOptions().circleCrop();
                        Glide.with(getActivity()).load(user.getHeadId()).apply(options).into(imgView);


                }
            });




    }
        else{
            user.setHeadId("http://q20jftoug.bkt.clouddn.com/23c425fd06e548b0850712dbc4dee741.jpeg");
    }


    }

    @Override
    public void refresh() {
        initUserView(user);
    }

    /*抽屉的初始化*/
    public void initDrawerList() {
        String[] titles = {"个人名片", "我的订阅", "修改密码", "小程序"};

        listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", titles[i]);
            map.put("map", titles[i]);
            listItems.add(map);
        }
        adapter = new AddItemAdapter(this.getContext(), listItems, R.layout.self_setting_item);


    }

    /*实现全局按钮的监听*/
    private void initEvent() {

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        btn_setting.setOnClickListener(buttonClickListener);
        btn_editF.setOnClickListener(buttonClickListener);
        tx_order.setOnClickListener(buttonClickListener);
        btn_editPwd.setOnClickListener(buttonClickListener);
    }


    /*全局监听类*/
    public class ButtonClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_setting: {

                    if (mDrawer_layout.isDrawerOpen(mMenu_layout_right)) {
                        mDrawer_layout.closeDrawer(mMenu_layout_right);
                    } else {
                        mDrawer_layout.openDrawer(mMenu_layout_right);
                    }
                    break;
                }
                case R.id.btn_editF: {
                    Log.e("btn_edit", user.getId() + "");
                    /*如果未登录，则跳到登录界面*/
                    if ((user.getId() + "").equals("0")) {
                        Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
                        startActivity(intent);
                    }
                    /*如果登录了，跳到修改信息界面*/
                    else {

                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        String str = gson.toJson(user);
                        intent.putExtra("user", str);
                        startActivity(intent);
                    }


                    break;
                }
                case R.id.self_order: {
                    /*如果未登录，则跳到登录界面*/
                    if ((user.getId() + "").equals("0")) {
                        Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getActivity(), FollowActivity.class);
//                        intent.putExtra("uid", user.getUserTel());//原 todo
                        intent.putExtra("uid", user.getUserPhone());
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.btn_self_edPwd:{

                    /*TODO
                        修改密码
                    * */

                    if ((user.getId() + "").equals("0")) {
                        /*如果用户处于非登录*/
                        Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
                        startActivity(intent);
                    }
                    /*如果用户处于登录状态但从未设置过密码*/
                    else if ((sp.getString("hasPassword", "false").equals("false"))) {
                        editor.putString("hasPassword", "true");
                        editor.commit();
                        Intent intent = new Intent(getActivity(), EditPwdWithoutOldActivity.class);
                        String str = gson.toJson(user);
                        intent.putExtra("user", str);
                        startActivity(intent);
                    } else {
                        /*有旧密码的情况下修改密码*/
                        Intent intent = new Intent(getActivity(), EditPwdWithOldActivity.class);
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
    public void onGetMessage(String str) {

        try {
            JSONObject jsonObject=new JSONObject(str);


            user.setUserIntro(jsonObject.getString("newIntro"));
            user.setUserName(jsonObject.getString("newName"));
            user.setUserSex(jsonObject.getString("newSex"));
            String qiNiuImgPath="http://"+Constant.QINIU_URL+"/"+jsonObject.getString("newHpath");
            imgUrl=qiNiuImgPath;
            /*Test*/
            user.setHeadId(imgUrl);
            Log.e("修改回来之后的user",user.getUserName()+user.getHeadId());
            Log.e("头像的位置",qiNiuImgPath);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        refresh();

    }

    /**
     * 右侧列表点击事件
     *
     * @author busy_boy
     */
    private class DrawerItemClickListenerRight implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //根据行号判断所选为哪个item
            if (user.getId() == 0) {

                Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
                startActivity(intent);
            } else {
                switch (position) {
                    case 0:

                        Intent intent0 = new Intent(getActivity(), ShowCardActivity.class);

                        String str = gson.toJson(user);
                        intent0.putExtra("user", str);
                        startActivity(intent0);

                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), FollowActivity.class);
                        intent.putExtra("uid", user.getUserPhone());
                        startActivity(intent);
                        break;
                    case 2: {
                        if ((user.getId() + "").equals("0")) {
                            /*如果用户处于非登录*/
                            Intent intent2 = new Intent(getActivity(), PhoneLoginActivity.class);
                            startActivity(intent2);
                        }
                        /*如果用户处于登录状态但从未设置过密码*/
                        else if ((sp.getString("hasPassword", "false").equals("false"))) {
                            editor.putString("hasPassword", "true");
                            editor.commit();
                            Intent intent1 = new Intent(getActivity(), EditPwdWithoutOldActivity.class);
                            String sss = gson.toJson(user);
                            intent1.putExtra("user", sss);
                            startActivity(intent1);
                        } else {
                            /*有旧密码的情况下修改密码*/
                            Intent intent00 = new Intent(getActivity(), EditPwdWithOldActivity.class);
                            String s = gson.toJson(user);
                            intent00.putExtra("user", s);
                            startActivity(intent00);
                        }

                        break;

                    }
                    case 3: {
                        Toast.makeText(view.getContext(), "正在开发", Toast.LENGTH_SHORT).show();


                    }

                    mDrawer_layout.closeDrawer(mMenu_layout_right);//关闭mMenu_layout
                }
            }


        }
    }
}