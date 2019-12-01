package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    private View popupView;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        findViews();
        //init
        popupView = LayoutInflater.from(this).inflate(R.layout.popup_window, null);
        passwordLoginPresenter = new PasswordLoginPresenterCompl(this);
    }

    private void findViews() {
        etPhone = findViewById(R.id.et_phone);
        etPwd = findViewById(R.id.et_pwd);
        ivLogin = findViewById(R.id.iv_login);
        btnLoginProblems = findViewById(R.id.btn_loginProblems);
        rbAgree = findViewById(R.id.rb_agree);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login:
                if (etPwd.getText().length() == 0 || etPhone.getText().length() == 0) {
                    Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (rbAgree.isChecked()) {
                        String tel = etPhone.getText().toString();
                        String pwd = etPwd.getText().toString();
                        passwordLoginPresenter.doLogin(tel, pwd);
                    } else {
                        Toast.makeText(this, "请阅读并同意用户协议！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_loginProblems:
                Intent intent = new Intent(PasswordLoginActivity.this, ProblemsLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 对登录请求的结果进行操作
     *
     * @param result 结果（1.true 登录成功，uid值有效 2.error登录密码错误 3.false 未注册）
     * @param uid    用户id
     */
    @Override
    public void onLoginResult(String result, int uid) {
        if (result.equals("true")) {
            //登录成功后将用户id进行存储
            SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", uid + "");
            editor.commit();
            Log.e("用户登录的id", uid + "");
        } else if (result.equals("error")) {
            Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
        } else if (result.equals("false")) {
            showPopupWindow();
        }
    }

    public void showPopupWindow() {
        if (popupWindow == null || !popupWindow.isShowing()) {
            popupWindow = new PopupWindow();
        }
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        //设置popupwindow能否响应外部事件（模态,点击其他地方内容无效）
        popupWindow.setOutsideTouchable(false);
        //设置popupwindow能否响应点击事件
        popupWindow.setTouchable(true);
        Button btnCancle = popupView.findViewById(R.id.btn_cancel);
        Button btnOk = popupView.findViewById(R.id.btn_ok);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordLoginActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
            }
        });
        popupWindow.showAtLocation(findViewById(R.id.ll_parent), Gravity.CENTER, 0, 0);
    }
}
