package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.ljt.login.presenter.IPasswordLoginPresenter;
import com.hbsd.rjxy.miaomiao.ljt.login.presenter.PasswordLoginPresenterCompl;
import com.hbsd.rjxy.miaomiao.ljt.login.view.IPasswordLoginView;

//注意协议部分的设置
public class PasswordLoginActivity extends AppCompatActivity implements IPasswordLoginView {

    private EditText etPhone;
    private EditText etPwd;
    private ImageView ivLogin;
    private Button btnLoginProblems;
    private RadioButton rbAgree;
    private IPasswordLoginPresenter passwordLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        findViews();
        //init
        passwordLoginPresenter=new PasswordLoginPresenterCompl(this);
    }

    private void findViews(){
        etPhone=findViewById(R.id.et_phone);
        etPwd=findViewById(R.id.et_pwd);
        ivLogin=findViewById(R.id.iv_login);
        btnLoginProblems=findViewById(R.id.btn_loginProblems);
        rbAgree=findViewById(R.id.rb_agree);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_login:
                if (etPwd.getText().length()==0|| etPhone.getText().length()==0){
                    Toast.makeText(this,"手机号或密码不能为空！",Toast.LENGTH_SHORT).show();
                }else{
                    if (rbAgree.isChecked()){
                        String tel=etPhone.getText().toString();
                        String pwd=etPwd.getText().toString();
                        passwordLoginPresenter.doLogin(tel,pwd);
                    }else{
                        Toast.makeText(this,"请阅读并同意用户协议！",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_loginProblems:
                Intent intent=new Intent(PasswordLoginActivity.this,ProblemsLoginActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onLoginResult(String result, int uid) {
        if (result.equals("true")){
            Log.e("用户登录的id",uid+"");
        }else if(result.equals("error")){
            Toast.makeText(this,"密码错误！",Toast.LENGTH_SHORT).show();
        }else if(result.equals("false")){
            Toast.makeText(this,"您还未注册！",Toast.LENGTH_SHORT).show();
        }
    }
}
