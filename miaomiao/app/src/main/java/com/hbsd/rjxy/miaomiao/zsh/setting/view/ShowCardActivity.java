package com.hbsd.rjxy.miaomiao.zsh.setting.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserCardCompl;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserCardPresenter;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserPresenterCompl;

public class ShowCardActivity extends AppCompatActivity {
    private Button btn_back;
    Intent intent;
    int uid;
    private GetUserCardPresenter getUserPresenterCompl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_card);
        intent=getIntent();
        uid= intent.getIntExtra("uid",0);
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
        getUserPresenterCompl.getUserCard(uid);
        Log.e("个人名片","已经完成");

    }
}
