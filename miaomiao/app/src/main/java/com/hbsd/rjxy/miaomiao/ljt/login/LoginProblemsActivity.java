package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hbsd.rjxy.miaomiao.R;

public class LoginProblemsActivity extends AppCompatActivity {

    private ImageView ivRtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_problems);
        findViews();
    }
    private void findViews(){
        ivRtn=findViewById(R.id.iv_rtn);
    }

    public void onClicked(View v){
        switch (v.getId()){
            case R.id.iv_rtn:
                finish();
                break;
            case R.id.btn_problem1:
                showProblem(1);
                break;
            case R.id.btn_problem2:
                showProblem(2);
                break;
        }
    }
    private void showProblem(int num){
        Intent intent=new Intent(LoginProblemsActivity.this,ShowProblemActivity.class);
        intent.putExtra("pNum",num);
        startActivity(intent);
    }
}
