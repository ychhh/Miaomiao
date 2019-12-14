package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zlc.vedio.view.IMainView;


import com.hbsd.rjxy.miaomiao.zsh.setting.SelfFragment;


import static com.hbsd.rjxy.miaomiao.utils.Constant.TAB_STRING;

public class MainActivity extends AppCompatActivity implements IMainView ,View.OnClickListener {

    private FragmentTabHost  tabHost = null;
    private Class[] tabClass = {MainFragment.class,Fragment2.class, Fragment2.class, Fragment2.class, SelfFragment.class};
    private ImageView iv_tabSpec = null;
    private boolean secondDown = false; //两次返回键退出





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化登录用户   如果未登陆过，传空，如果登陆过，根据id请求获取user信息，创建user实体

        if(true){
            //未登录
//           new VideoPresenter(getApplicationContext(),null).execute();
        }else{

        }

        mTranslucent();//透明状态栏

        initTab();



    }



    @Override
    public void initTab() {
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        for(int i = 0 ; i < TAB_STRING.length ; i++){
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(TAB_STRING[i])
                    .setIndicator(getTabSpecView(i));
            tabHost.addTab(tabSpec,tabClass[i],null);
            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    updateTabHost(tabHost);
                }
            });
        }
        tabHost.setCurrentTab(0);
        updateTabHost(tabHost);


    }




    @Override
    public View getTabSpecView(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabhost_layout,null);
        iv_tabSpec = view.findViewById(R.id.iv_tabSpec);
        if(i == 1 || i == 3){

//            Glide.with(view)
//                    .load(R.drawable.tab_cat_image)
//                    .centerInside()
//                    .into(iv_tabSpec);

        }

        //对tabSpec具体内容进行

        TextView textView = view.findViewById(R.id.tv_tabname);
        textView.setText(TAB_STRING[i]);

        return view;
    }

    @Override
    public void mTranslucent() {
        //修改透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){



        }


    }

    @Override
    public void updateTabHost(View v) {
        TabHost tabHost = (TabHost)v;
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view =  tabHost.getTabWidget().getChildAt(i);
            iv_tabSpec = view.findViewById(R.id.iv_tabSpec);
            TextView textView = view.findViewById(R.id.tv_tabname);

            if(tabHost.getCurrentTab() == i){
                //字体变色
                textView.setTextColor(getResources().getColor(R.color.white));
            }else{
                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }



        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(secondDown == true){
                    finish();
                }

                if(secondDown == false){
                    //返回键的监听
                    secondDown = true;
                    Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            secondDown = false;

                        }
                    }.start();
                    return false;
                }
                break;
        }





        return super.onKeyDown(keyCode, event);
    }
}
