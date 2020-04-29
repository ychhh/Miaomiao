package com.hbsd.rjxy.miaomiao.zsh.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.login.PhoneLoginActivity;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.EditPwdPresenterCompl;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.EditPwdView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditPwdWithOldActivity extends AppCompatActivity implements EditPwdView {
    private TextView tx_oldPwd;
    private TextView tx_newPwd;
    private TextView tx_confirmPwd;
    private Button btn_commit;
    private Intent intent;
    private User user;
    private ImageView show_old;
    private ImageView show_new;
    private ImageView show_confirm;

    private EditPwdPresenterCompl editPwdPresenterCompl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd_with_old);
        /*接收我的界面传来的数据*/
        intent=getIntent();
        String str=intent.getStringExtra("user");
        Gson gson=new Gson();
        user=gson.fromJson(str, User.class);
        tx_oldPwd=findViewById(R.id.self_old_pwd);
        tx_newPwd=findViewById(R.id.self_new_pwd);
        tx_confirmPwd=findViewById(R.id.self_confirm_pwd);
        btn_commit=findViewById(R.id.self_btn_pwd_commit);
        show_old=findViewById(R.id.iv_show_old_pwd);
        show_new=findViewById(R.id.iv_login_show_pwd);
        show_confirm=findViewById(R.id.iv_login_show_confirm_pwd);
        show_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tx_oldPwd.getInputType()==128){
                    tx_oldPwd.setInputType(129);
                    show_old.setImageResource(R.drawable.eye_close);
                }
                else if(tx_oldPwd.getInputType()==129){
                    tx_oldPwd.setInputType(128);
                    show_old.setImageResource(R.drawable.eye_open);
                }
            }
        });
        show_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tx_newPwd.getInputType()==128){
                    tx_newPwd.setInputType(129);
                    show_new.setImageResource(R.drawable.eye_close);
                }
                else if(tx_newPwd.getInputType()==129){
                    tx_newPwd.setInputType(128);
                    show_new.setImageResource(R.drawable.eye_open);
                }
            }
        });
        show_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tx_confirmPwd.getInputType()==128){
                    tx_confirmPwd.setInputType(129);
                    show_confirm.setImageResource(R.drawable.eye_close);
                }
                else if(tx_confirmPwd.getInputType()==129){
                    tx_confirmPwd.setInputType(128);
                    show_confirm.setImageResource(R.drawable.eye_open);
                }
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = tx_oldPwd.getText().toString();
                String newPwd = tx_newPwd.getText().toString();
                String confirmPwd = tx_confirmPwd.getText().toString();
                if (confirmPwd.equals(newPwd)) {
                    Log.e("editPwdWithOldActivity","两次密码一致");
                    editPwdPresenterCompl = new EditPwdPresenterCompl(EditPwdWithOldActivity.this);
                    editPwdPresenterCompl.editPwdWithOld(user.getId(), oldPwd, newPwd);
                } else {
                    Toast.makeText(EditPwdWithOldActivity.this, "确认密码两次输入不一致", Toast.LENGTH_SHORT).show();
                }


            }


            });
    }

    @Override
    public void okFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(EditPwdWithOldActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

            }
        });

        finish();



    }

    @Override
    public void failFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(EditPwdWithOldActivity.this, "修改失败", Toast.LENGTH_SHORT).show();

            }
        });

        finish();




    }
}
