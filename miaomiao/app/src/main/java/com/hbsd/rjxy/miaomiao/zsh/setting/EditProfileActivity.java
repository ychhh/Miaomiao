package com.hbsd.rjxy.miaomiao.zsh.setting;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;

import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.EditUserPresenterCompl;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.EditProfileView;

import org.json.JSONException;
import org.json.JSONObject;


public class EditProfileActivity extends AppCompatActivity implements EditProfileView {
    private Button btn_back;
    private Button btn_commit;
    private TextView tx_reName;
    private ImageView tx_reImg;
    private TextView tx_reSbp;
    private TextView tx_reSex;

   private Intent intent;
   private User user;
   private EditUserPresenterCompl editUserPresenterCompl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /*接收我的界面传来的数据*/
        intent=getIntent();
        String str=intent.getStringExtra("user");
        Gson gson=new Gson();
        user=gson.fromJson(str,User.class);

        /*控件*/
        btn_back=findViewById(R.id.self_back);
        btn_commit=findViewById(R.id.self_reCommit);
        tx_reImg=findViewById(R.id.self_reImg);
        tx_reName=findViewById(R.id.self_reName);
        tx_reSbp=findViewById(R.id.self_reSbp);
        tx_reSex=findViewById(R.id.self_reSex);
        /*数据初始化*/
        initView();
        /*大监听*/
        ButtonClickLinstener buttonClickLinstener=new ButtonClickLinstener();

        //绑定监听
        btn_back.setOnClickListener(buttonClickLinstener);
        btn_commit.setOnClickListener(buttonClickLinstener);




    }
    public void initView(){

        /*TODO
        *   图片的设置
        *   */
        tx_reSex.setText(user.getUserSex());
        tx_reSbp.setText(user.getUserIntro());
        tx_reName.setText(user.getUserName());
        editUserPresenterCompl=new EditUserPresenterCompl(this);




    }
    /*实现接口方法，结束当前界面*/
    @Override
    public void Okfinish() {
        finish();
    }

    public class ButtonClickLinstener implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.self_back:{
                    finish();
                    break;
                }
                case R.id.self_reCommit:{



                    String newName=tx_reName.getText().toString();

                    String newsbp=tx_reSbp.getText().toString();

                    String newSex=tx_reSex.getText().toString();


                        //Log.e("读取到当下想修改的用户名为",newName);
                        JSONObject obj=new JSONObject();
                        try {
                            obj.put("uid",user.getUserId());
                            obj.put("newName",newName);
                            obj.put("newIntro",newsbp);
                            obj.put("newSex",newSex);
                            String jsonStr=obj.toString();
                            //Log.e("json",jsonStr);

                            editUserPresenterCompl.editUser(jsonStr);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    break;

                }

            }
        }
    }


}
