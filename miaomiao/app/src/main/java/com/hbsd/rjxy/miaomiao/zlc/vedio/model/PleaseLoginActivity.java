package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hbsd.rjxy.miaomiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PleaseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Unbinder un;
    @BindView(R.id.iv_backfrom_pleaselogin)
    ImageView backward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needlogin_layout);
        un = ButterKnife.bind(this);

        backward.setOnClickListener(this::onClick);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        un.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_backfrom_pleaselogin:
                //返回主页面，修改视图    eventbus



                this.finish();
                break;
        }
    }
}
