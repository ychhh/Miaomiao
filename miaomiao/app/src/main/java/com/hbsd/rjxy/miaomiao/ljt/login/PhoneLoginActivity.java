package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.ljt.login.presenter.IPhoneLoginPresenter;
import com.hbsd.rjxy.miaomiao.ljt.login.presenter.PhoneLoginPresenterCompl;
import com.hbsd.rjxy.miaomiao.ljt.login.view.IPhoneLoginView;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.MainActivity;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class PhoneLoginActivity extends AppCompatActivity implements IPhoneLoginView {

    private Button btnPwdLogin;
    private EditText etPhone;
    private EditText etPhoneCode;
    private Button btnGetPhoneCode;
    private ImageView ivLogin;
    private Button btnLoginProblems;
    private RadioButton rbAgree;
    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器
    private IPhoneLoginPresenter iPhoneLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        init();
        findViews();
        iPhoneLoginPresenter=new PhoneLoginPresenterCompl(this);
    }

    private void findViews() {
        btnPwdLogin = findViewById(R.id.btn_pwdLogin);
        etPhone = findViewById(R.id.et_phone);
        etPhoneCode = findViewById(R.id.et_phoneCode);
        btnGetPhoneCode = findViewById(R.id.btn_getPhoneCode);
        ivLogin = findViewById(R.id.iv_login);
        btnLoginProblems = findViewById(R.id.btn_loginProblems);
        rbAgree = findViewById(R.id.rb_agree);
        mTimeCount = new TimeCount(60000, 1000);
    }

    /**
     * 初始化事件接收器
     */
    private void init() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                        iPhoneLoginPresenter.doLogin(etPhone.getText().toString());
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PhoneLoginActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表

                    }
                } else {
                    int status = 0;
                    try {
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");
                        status = object.optInt("status");
                        if (!TextUtils.isEmpty(des)) {
                            return;
                        }
                    } catch (Exception e) {
                        SMSLog.getInstance().w(e);
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_pwdLogin://密码登录
                Intent intent = new Intent(PhoneLoginActivity.this, PasswordLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_loginProblems://登录遇到问题
                Intent intent1 = new Intent(PhoneLoginActivity.this, ProblemsLoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_getPhoneCode://获取验证码
                if (!etPhone.getText().toString().trim().equals("")) {
                    if (checkTel(etPhone.getText().toString().trim())) {
                        if (rbAgree.isChecked()) {
                            SMSSDK.getVerificationCode("+86", etPhone.getText().toString());//获取验证码
                            mTimeCount.start();
                        }else{
                            Toast.makeText(this, "请阅读并同意用户协议！", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(PhoneLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PhoneLoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_login://登录
                if (!etPhone.getText().toString().trim().equals("")){
                    if (checkTel(etPhone.getText().toString().trim())) {
                        if (!etPhoneCode.getText().toString().trim().equals("")) {
                            SMSSDK.submitVerificationCode("+86",etPhone.getText().toString().trim(),
                                    etPhoneCode.getText().toString().trim());//提交验证
                        }else{
                            Toast.makeText(PhoneLoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(PhoneLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PhoneLoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 正则匹配手机号码
     *
     * @param tel
     * @return
     */
    public boolean checkTel(String tel) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    /**
     * 登录成功之后
     * @param result
     * @param uid
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
            startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class)); //页面跳转
        }
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {//计时中
            btnGetPhoneCode.setClickable(false);
            btnGetPhoneCode.setText(l / 1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {//计时结束
            btnGetPhoneCode.setClickable(true);
            btnGetPhoneCode.setText("获取验证码");
        }
    }
}
