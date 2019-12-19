package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.ljt.login.PhoneLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PleaseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_backward)
    ImageView backward;

    @BindView(R.id.btn_jumpTologin)
    Button jump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needlogin_layout);

        ButterKnife.bind(this);

        backward.setOnClickListener(this::onClick);
        jump.setOnClickListener(this::onClick);







    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_backward:

                //返回键逻辑

                this.finish();
                break;

            case R.id.btn_jumpTologin:

                //登陆逻辑
                startActivity(new Intent(PleaseLoginActivity.this, PhoneLoginActivity.class));
                finish();

                break;

        }

    }
}
