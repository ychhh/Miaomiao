package com.hbsd.rjxy.miaomiao.zsh.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserCardCompl;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserCardPresenter;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.ShowCardView;

import org.greenrobot.eventbus.EventBus;

public class ShowCardActivity extends AppCompatActivity implements ShowCardView {
    private Button btn_back;
    Intent intent;
    private User user;
    private GetUserCardPresenter getUserPresenterCompl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_card);
        intent=getIntent();
        Gson gson=new Gson();
        String str=intent.getStringExtra("user");
        user=gson.fromJson(str,User.class);


        initData();
        btn_back=findViewById(R.id.self_back);
        btn_back  .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void initData(){


        getUserPresenterCompl=new GetUserCardCompl(this);
        getUserPresenterCompl.getUserCard();


    }

    @Override
    public void initView() {
        TextView tx_reName=findViewById(R.id.self_name);
        TextView tx_intro=findViewById(R.id.self_intro);
        TextView tx_id=findViewById(R.id.self_id);
        TextView tx_sex=findViewById(R.id.self_sex);
        tx_intro.setText(user.getUserIntro());
        tx_reName.setText(user.getUserName());
        tx_id.setText(user.getUserId()+"");
        tx_sex.setText(user.getUserSex());
    }
}
