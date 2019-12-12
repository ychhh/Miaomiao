package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import okhttp3.Call;
import okhttp3.Callback;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.UploadUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.hbsd.rjxy.miaomiao.utils.Constant.UPLOAD_USERHEAD_TOKEN_URL;


public class EditProfileActivity extends AppCompatActivity {
    private Button btn_back;
    private Button btn_commit;
    private TextView tx_reName;
    private ImageView iv_reImg;
    private TextView tv_changeHead;
    private TextView tx_reSbp;
    private TextView tx_reSex;
    private OkHttpUtils okHttpUtils;
    private okhttp3.Callback callback;
    private Integer id;
    private Intent intent;
    private EditUserPresenterCompl editUserPresenterCompl;
    private String localImgPath;
    private String qiNiuImgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        initData();
        btn_back = findViewById(R.id.self_back);
        btn_commit = findViewById(R.id.self_reCommit);
        iv_reImg = findViewById(R.id.iv_reImg);
        tx_reName = findViewById(R.id.self_reName);
        tx_reSbp = findViewById(R.id.self_reSbp);
        tx_reSex = findViewById(R.id.self_reSex);
        tv_changeHead=findViewById(R.id.tv_changeHead);
        //大监听
        ButtonClickLinstener buttonClickLinstener = new ButtonClickLinstener();
        //绑定监听
        btn_back.setOnClickListener(buttonClickLinstener);
        btn_commit.setOnClickListener(buttonClickLinstener);
        tv_changeHead.setOnClickListener(buttonClickLinstener);

    }

    public void initData() {
        editUserPresenterCompl = new EditUserPresenterCompl(this);
        editUserPresenterCompl.initUser(id);


    }

    public class ButtonClickLinstener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.self_back: {
                    finish();
                    break;
                }
                case R.id.self_reCommit: {
                    String newName = tx_reName.getText().toString();
                    String newsbp = tx_reSbp.getText().toString();
                    String newSex = tx_reSex.getText().toString();
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("uid", id);
                        obj.put("newName", newName);
                        obj.put("newIntro", newsbp);
                        obj.put("newSex", newSex);
                        String jsonStr = obj.toString();
                        callback = new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e("任务", "shibai");
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                Log.e("已经成功接收到修改之后的客户信息", "zhenhao");
                            }
                        };
                        String url = Constant.GET_USER_URL + "edit";
                        okHttpUtils = new OkHttpUtils();
                        okHttpUtils.postJson(url, jsonStr, callback);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("新的用户名", newName);
                    Intent intent0 = new Intent("android.intent.action.CART_BROADCAST");
                    intent0.putExtra("data", "refresh");
                    LocalBroadcastManager.getInstance(EditProfileActivity.this).sendBroadcast(intent0);
                    sendBroadcast(intent0);
                    finish();
                    break;
                }
                case R.id.tv_changeHead:
                    //动态申请权限
                    ActivityCompat.requestPermissions(EditProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    break;

            }
        }
    }

    //用户允许权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 200);
        }
    }

    //选择图片返回后执行此方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null,
                    null, null);
            if (cursor.moveToFirst()) {
                localImgPath = cursor.getString(cursor.getColumnIndex("_data"));
                Log.e("imgPath", localImgPath);
                RequestOptions options = new RequestOptions().circleCrop();
                Glide.with(this)
                        .load(localImgPath)
                        .apply(options)
                        .into(iv_reImg);
                //上传头像到服务器端
                startUploadProgress();
            }

        }
    }

    /**
     * 获取token
     */
    private void startUploadProgress() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpUtils.getInstance().postJson(UPLOAD_USERHEAD_TOKEN_URL, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String token = response.body().string();
                startRealUpload(token);
            }
        });
    }

    private void startRealUpload(String token) {
        UploadUtils uploadUtils = new UploadUtils(token, localImgPath, new File(localImgPath).getName());
        qiNiuImgPath=uploadUtils.getKey();//服务器端图片名称，包含后缀
        uploadUtils.upload();
    }
}
