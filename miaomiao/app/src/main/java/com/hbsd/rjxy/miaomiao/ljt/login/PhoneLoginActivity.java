package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.annotation.Nullable;
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
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.EditTextUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.MainActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
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
    private ImageView ivClear;
    private RadioButton rbAgree;
    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器
    private IPhoneLoginPresenter iPhoneLoginPresenter;

    private static final String TAG = "PhoneLoginActivity";
    private static final String APP_ID = "1110459446";//官方获取的APPID
    private Tencent mTencent;
    private UserInfo mUserInfo;
    private BaseUiListener mIUiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID,PhoneLoginActivity.this.getApplicationContext());
        iPhoneLoginPresenter=new PhoneLoginPresenterCompl(this);
        init();
        findViews();
        EditTextUtils.clearButtonListener(etPhone, ivClear);
    }

    private void findViews() {
        btnPwdLogin = findViewById(R.id.btn_pwdLogin);
        etPhone = findViewById(R.id.et_phone);
        etPhoneCode = findViewById(R.id.et_phoneCode);
        btnGetPhoneCode = findViewById(R.id.btn_getPhoneCode);
        rbAgree = findViewById(R.id.rb_agree);
        mTimeCount = new TimeCount(60000, 1000);
        ivClear=findViewById(R.id.iv_clear);
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
                        Log.e("验证码","验证成功");
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
                    try {
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");
                        if (status>0 && !TextUtils.isEmpty(des)) {
                            Log.e("status"+status,des);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (status==468){
                                        Toast.makeText(PhoneLoginActivity.this,"验证码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
                Intent intent1 = new Intent(PhoneLoginActivity.this, LoginProblemsActivity.class);
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
            case R.id.btn_login://登录
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
            case R.id.btn_showService:
                Intent intent2=new Intent(this,ShowServiceActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_rtn:
                finish();
                break;
            case R.id.login_qq:
                /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
                 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
                 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
                mIUiListener = new BaseUiListener();
                //all表示获取所有权限
                mTencent.login(PhoneLoginActivity.this, "all", mIUiListener);
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

    /**
     *
     * @param result
     * @param object
     */
    @Override
    public void onLoginResult(String result,JSONObject object) {
        if (result.equals("true")) {
            //登录成功后将用户id进行存储,是否有密码进行存储
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.LOGIN_SP_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                String uid = object.getString("uid");
                editor.putString("uid", uid);
                String username=object.getString("username");
                editor.putString(Constant.LOGIN_USERNAME,username);
                String userHeadPath=object.getString("userHeadPath");
                editor.putString(Constant.LOGIN_HEADPATH,userHeadPath);
                String hasPassword=object.getString("hasPassword");
                editor.putString(Constant.HAS_PASSWORD,hasPassword);
                Log.e("用户登录的信息",uid+username+userHeadPath+hasPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.commit();
            SharedPreferences sp=getSharedPreferences(Constant.PUBLISH_SP_NAME,MODE_PRIVATE);
            sp.edit().putString(Constant.REMIND_PUBLISH_ONCE,"NEEDREMIND").commit();
            Intent intent=new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(PhoneLoginActivity.this, MainActivity.class);
            startActivity(intent); //页面跳转
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

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(PhoneLoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        //todo 用户qq授权登录成功之后，传到后台登录数据，拿到uid，存储qq相关信息
                        Log.e(TAG, "登录成功" + response.toString());
                        //QQ登录成功后将用户信息进行存储进行存储
                        JSONObject object = (JSONObject) response;
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.LOGIN_SP_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        try {
                            editor.putString("qqOpenid", openID);
                            String username=object.getString("nickname");
                            editor.putString(Constant.LOGIN_USERNAME,username);
                            String userHeadPath=object.getString("figureurl");
                            editor.putString(Constant.LOGIN_HEADPATH,userHeadPath);
                            String gender=object.getString("gender");
                            editor.putString("gender",gender);
                            Log.e("QQ用户登录的信息",openID+username+userHeadPath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                        SharedPreferences sp=getSharedPreferences(Constant.PUBLISH_SP_NAME,MODE_PRIVATE);
                        sp.edit().putString(Constant.REMIND_PUBLISH_ONCE,"NEEDREMIND").commit();
                        Intent intent=new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(PhoneLoginActivity.this, MainActivity.class);
                        startActivity(intent); //页面跳转
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(PhoneLoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(PhoneLoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
