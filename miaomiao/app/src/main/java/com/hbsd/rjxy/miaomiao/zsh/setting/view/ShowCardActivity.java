package com.hbsd.rjxy.miaomiao.zsh.setting.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.GetUserPresenterCompl;

public class ShowCardActivity extends AppCompatActivity {
    private Button btn_back;
    private GetUserPresenterCompl getUserPresenterCompl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card);





        btn_back=findViewById(R.id.self_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
