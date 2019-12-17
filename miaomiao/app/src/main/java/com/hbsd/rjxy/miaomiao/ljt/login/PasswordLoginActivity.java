package com.hbsd.rjxy.miaomiao.ljt.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.TextView;
import android.widget.Toast;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.ljt.login.presenter.IPasswordLoginPresenter;
import com.hbsd.rjxy.miaomiao.ljt.login.presenter.PasswordLoginPresenterCompl;
import com.hbsd.rjxy.miaomiao.ljt.login.view.IPasswordLoginView;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.EditTextUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


//注意协议部分的设置
public class PasswordLoginActivity extends AppCompatActivity implements IPasswordLoginView {

    private EditText etPhone;
    private EditText etPwd;
    private ImageView ivLogin;
    private ImageView ivEye;
    private ImageView ivClearPhone;
    private ImageView ivClearPwd;
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
        EditTextUtils.clearButtonListener(etPhone, ivClearPhone);
        EditTextUtils.clearAndShowButtonListener(etPwd, ivClearPwd,ivEye);
    }

    private void findViews() {
        etPhone = findViewById(R.id.et_phone);
        etPwd = findViewById(R.id.et_pwd);
        ivLogin = findViewById(R.id.iv_login);
        ivEye=findViewById(R.id.iv_eye);
        ivEye.setImageResource(R.drawable.eye_close);
        ivClearPhone=findViewById(R.id.iv_clearPhone);
        ivClearPwd=findViewById(R.id.iv_clearPwd);
        btnLoginProblems = findViewById(R.id.btn_loginProblems);
        rbAgree = findViewById(R.id.rb_agree);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login:
                if (etPwd.getText().length() == 0 || etPhone.getText().length() == 0) {
                    Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkTel(etPhone.getText().toString().trim())){
                        if (rbAgree.isChecked()) {
                            String tel = etPhone.getText().toString();
                            String pwd = etPwd.getText().toString();
                            passwordLoginPresenter.doLogin(tel, pwd);
                        } else {
                            Toast.makeText(this, "请阅读并同意用户协议！", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(PasswordLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_loginProblems:
                Intent intent = new Intent(PasswordLoginActivity.this, LoginProblemsActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_rtn:
                finish();
                break;
            case R.id.btn_showService:
                Intent intent1=new Intent(this,ShowServiceActivity.class);
                startActivity(intent1);
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
     * 对登录请求的结果进行操作
     *
     * @param result 结果（1.true 登录成功，uid值有效 2.error登录密码错误 3.false 未注册 4.null 未设置密码）
     * @param
     */
    @Override
    public void onLoginResult(String result, JSONObject object) {
        if (result.equals("true")) {
            //登录成功后将用户id进行存储
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.LOGIN_SP_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                String uid = object.getString("uid");
                editor.putString("uid", uid);
                String username=object.getString("username");
                editor.putString(Constant.LOGIN_USERNAME,username);
                String userHeadPath=object.getString("userHeadPath");
                editor.putString(Constant.LOGIN_HEADPATH,userHeadPath);
                Log.e("用户登录的信息",uid+username+userHeadPath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.commit();
            SharedPreferences sp=getSharedPreferences(Constant.PUBLISH_SP_NAME,MODE_PRIVATE);
            sp.edit().putString(Constant.REMIND_PUBLISH_ONCE,"NEEDREMIND").commit();
            Intent intent=new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(PasswordLoginActivity.this, MainActivity.class);
            startActivity(intent); //页面跳转
        } else if (result.equals("error")) {
            Looper.prepare();
            Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        } else if (result.equals("false")) {
            Looper.prepare();
            showPopupWindow(result);
            Looper.loop();
        }else if (result.equals("null")){
            Looper.prepare();
            showPopupWindow(result);
            Looper.loop();
        }
    }

    public void showPopupWindow(String result) {
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
        TextView tvText=popupView.findViewById(R.id.tv_popupText);
        Button btnOk = popupView.findViewById(R.id.btn_ok);
        if(result.equals("false")){
            tvText.setText("您还未注册！");
            btnOk.setText("去注册");
        }else if(result.equals("null")){
            tvText.setText("当前账户未设置密码！");
            btnOk.setText("验证码登录");
        }
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
