package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import okhttp3.Call;
import okhttp3.Callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.Response;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.SelfFragment;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class EditProfileActivity extends AppCompatActivity {
    private Button btn_back;
    private Button btn_commit;
    private TextView tx_reName;
    private ImageView tx_reImg;
    private TextView tx_reSbp;
    private TextView tx_reSex;
    private OkHttpUtils okHttpUtils;
    private  okhttp3.Callback callback;
   private  Integer id;
   private Intent intent;
   private EditUserPresenterCompl editUserPresenterCompl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        intent=getIntent();
        id=intent.getIntExtra("id",0);
        initData();
        btn_back=findViewById(R.id.self_back);
        btn_commit=findViewById(R.id.self_reCommit);
        tx_reImg=findViewById(R.id.self_reImg);
        tx_reName=findViewById(R.id.self_reName);
        tx_reSbp=findViewById(R.id.self_reSbp);
        tx_reSex=findViewById(R.id.self_reSex);
        //大监听
        ButtonClickLinstener buttonClickLinstener=new ButtonClickLinstener();
        //绑定监听
        btn_back.setOnClickListener(buttonClickLinstener);
        btn_commit.setOnClickListener(buttonClickLinstener);




    }
    public void initData(){
        editUserPresenterCompl=new EditUserPresenterCompl(this);
        editUserPresenterCompl.initUser(id);


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
                    JSONObject obj=new JSONObject();
                    try {
                        obj.put("uid",id);
                        obj.put("newName",newName);
                        obj.put("newIntro",newsbp);
                        obj.put("newSex",newSex);
                        String jsonStr=obj.toString();
                        callback=new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e("任务","shibai");
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                Log.e("已经成功接收到修改之后的客户信息","zhenhao");
                            }
                        };
                        String url=Constant.GET_USER_URL+"edit";
                        okHttpUtils=new OkHttpUtils();
                        okHttpUtils.postJson(url,jsonStr,callback);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("新的用户名",newName);
                    Intent intent0=new Intent("android.intent.action.CART_BROADCAST");
                    intent0.putExtra("data","refresh");
                    LocalBroadcastManager.getInstance(EditProfileActivity.this).sendBroadcast(intent0);
                    sendBroadcast(intent0);
                    finish();
                    break;

                }

            }
        }
    }


}
