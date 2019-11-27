package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hbsd.rjxy.miaomiao.R;

public class PhoneLoginActivity extends AppCompatActivity {

    private Button btnPwdLogin;
    private EditText etPhone;
    private EditText etPhoneCode;
    private Button btnGetCode;
    private ImageView ivLogin;
    private Button btnLoginProblems;
    private RadioButton rbAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        findViews();
        setListeners();
    }

    private void findViews(){
        btnPwdLogin=findViewById(R.id.btn_pwdLogin);
        etPhone=findViewById(R.id.et_phone);
        etPhoneCode=findViewById(R.id.et_phoneCode);
        btnGetCode=findViewById(R.id.btn_getPhoneCode);
        ivLogin=findViewById(R.id.iv_login);
        btnLoginProblems=findViewById(R.id.btn_loginProblems);
        rbAgree=findViewById(R.id.rb_agree);
    }

    private void setListeners(){
        //密码登录
        btnPwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhoneLoginActivity.this,PasswordLoginActivity.class);
                startActivity(intent);
            }
        });
        //登录
        ivLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //登录遇到问题
        btnLoginProblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhoneLoginActivity.this,ProblemsLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
