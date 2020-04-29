package com.hbsd.rjxy.miaomiao.zsh.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.login.PhoneLoginActivity;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserPresenterCompl;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.SelfMainView;

import org.greenrobot.eventbus.EventBus;

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
public class MyselfFragment extends Fragment implements SelfMainView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static User user;

    private View view;
    private ImageView img_settting;
    private TextView tx_userName;
    private ImageView img_userHead;
    private TextView tx_fsNumber;
    private TextView tx_gzNumber;
    private TextView tx_mbNumber;
    private TextView tx_wdzy;
    private GetUserPresenterCompl getUserPresenterCompl;
    private Gson gson;
    private SharedPreferences sp;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyselfFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyselfFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyselfFragment newInstance(String param1, String param2) {
        MyselfFragment fragment = new MyselfFragment();

        user.setUserName("it is ok");
        user.setUserId(151);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_myself,
                container,
                false);



        return view;
                //inflater.inflate(R.layout.fragment_myself, container, false);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void refresh() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*TODO
        *   获取控件*/
        tx_userName=view.findViewById(R.id.tx_userName);



        /*TODO
        *   通过sp获取当下用户的uid，uid默认为0，如果uid为0，跳转到登录界面，如果uid不为0，则通过uid进行服务器数据访问*/
        sp = this.getActivity().getSharedPreferences(Constant.LOGIN_SP_NAME, MODE_PRIVATE);
        gson=new Gson();
        int uid=Integer.parseInt(sp.getString("uid","0"));
        if(uid==0){
            Intent intent=new Intent(getActivity(), PhoneLoginActivity.class);
            startActivity(intent);
        }
        /*
         TODO
          这块内容要请求服务器访问数据库获得的，因为暂时数据库在修改，所以这块先虚拟数据了
          接下来记得改
           initData();
        */
        initData(uid);

        /*TODO
            在获取完数据之后，将用户相关信息都进行视图渲染
        */
        initUserView(user);

//        mDrawer_layout = view.findViewById(R.id.drawer_layout);
//        mMenu_layout_right = view.findViewById(R.id.menu_layout_right);
//        menu_listview_r = mMenu_layout_right.findViewById(R.id.menu_listView_r);
//        btn_setting = view.findViewById(R.id.btn_setting);
//        btn_editF = view.findViewById(R.id.btn_editF);
//        tx_order = view.findViewById(R.id.self_order);
//        btn_editPwd=view.findViewById(R.id.btn_self_edPwd);
//        tx_intro = view.findViewById(R.id.self_main_intro);
//        imgView=view.findViewById(R.id.img_head);
//        getUserPresenterCompl = new GetUserPresenterCompl(this);




        /*通过sp获取当下user的uid*/

//        sp = this.getActivity().getSharedPreferences(Constant.LOGIN_SP_NAME, MODE_PRIVATE);
//
//        gson=new Gson();
//        int uid=Integer.parseInt(sp.getString("uid","0"));
//        if(uid==0){
//            Intent intent=new Intent(getActivity(), PhoneLoginActivity.class);
//            startActivity(intent);
//        }
//        user = new User();
//        user.setUserId(uid);

        /*初始化UserData*/

//        initData();
        /*初始化抽屉*/
//        initDrawerList();
//        menu_listview_r.setAdapter(adapter);

        /*监听setting按钮*/
//        initEvent();


        //监听菜单
//        menu_listview_r.setOnItemClickListener(new DrawerItemClickListenerRight());


    }
    public void initData(int uid) {
        /*记得修改数据，这块目前是模拟数据*/
        //user=getUserPresenterCompl.getUser(user.getUserId());
        user = new User();
        user.setUserId(uid);
        user =new User();
        user.setUserName("ok");
        user.setUserId(25);
        user.setUserSex("女");
        user.setUserCatCount(0);
        user.setUserIntro("我是风儿你是沙");
        user.sethPath("http://q20jftoug.bkt.clouddn.com/23c425fd06e548b0850712dbc4dee741.jpeg");
    }
    @Override
    public void initUserView(User user) {

        if(user.getUserName()!=null){
            tx_userName.setText(user.getUserName());
        }
        if(!user.gethPath().equals("null")){
            String imgUrl=user.gethPath();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        RequestOptions options = new RequestOptions().circleCrop();
                        Glide.with(getActivity()).load(user.gethPath()).apply(options).into(img_userHead);


                }
            });




    }
        else{
            user.sethPath("http://q20jftoug.bkt.clouddn.com/23c425fd06e548b0850712dbc4dee741.jpeg");
    }

    }
}
