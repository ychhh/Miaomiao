package com.hbsd.rjxy.miaomiao.ljt.login;

//import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hbsd.rjxy.miaomiao.R;

public class ShowProblemActivity extends AppCompatActivity {

    private TextView tvShowProblem;
    private String[]details={"请检查您输入的手机号是否正确，是否连接网络",
            "更换手机号操作请登录后在资料页内申请更改"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_problem);
        findViews();
        Intent intent=getIntent();
        int pNum=intent.getIntExtra("pNum",0);
        if (pNum>0){
            tvShowProblem.setText(details[pNum-1]);
        }

    }

    private void findViews(){
        tvShowProblem=findViewById(R.id.tv_showProblem);
    }

    public void onClicked(View v){
        switch (v.getId()){
            case R.id.iv_rtn:
                finish();
                break;
        }
    }
}
