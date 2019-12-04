package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hbsd.rjxy.miaomiao.R;

public class ShowServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service);
    }
    public void onClicked(View v){
        switch (v.getId()){
            case R.id.iv_rtn:
                finish();
                break;
        }
    }
}
