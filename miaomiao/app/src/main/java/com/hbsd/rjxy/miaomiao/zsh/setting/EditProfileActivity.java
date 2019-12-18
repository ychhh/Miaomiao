package com.hbsd.rjxy.miaomiao.zsh.setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.os.BuildCompat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pub.devrel.easypermissions.EasyPermissions;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.GlideEngine;


import com.hbsd.rjxy.miaomiao.zsh.setting.presenter.EditUserPresenterCompl;

import com.hbsd.rjxy.miaomiao.zlc.vedio.model.UploadUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.EditProfileView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hbsd.rjxy.miaomiao.utils.Constant.UPLOAD_USERHEAD_TOKEN_URL;


public class EditProfileActivity extends AppCompatActivity implements EditProfileView {
    private Button btn_back;
    private Button btn_commit;
    private TextView tx_reName;
    private ImageView iv_reImg;
    private TextView tv_changeHead;
    private TextView tx_reSbp;
    private TextView tx_reSex;
    private Integer id;
    private Intent intent;
    private EditUserPresenterCompl editUserPresenterCompl;
    private String localImgPath;
    private String qiNiuImgPath;
    private User user;
    private UploadUtils uploadUtils;
    List<LocalMedia> selectResultList;
    private boolean isEditedHead = false;
    private boolean isPrepared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /*接收我的界面传来的数据*/
        intent = getIntent();
        String str = intent.getStringExtra("user");
        //
        qiNiuImgPath = intent.getStringExtra("hpath");
        Gson gson = new Gson();
        user = gson.fromJson(str, User.class);

        /*控件*/
        btn_back = findViewById(R.id.self_back);
        btn_commit = findViewById(R.id.self_reCommit);
        iv_reImg = findViewById(R.id.iv_reImg);
        tx_reName = findViewById(R.id.self_reName);
        tx_reSbp = findViewById(R.id.self_reSbp);
        tx_reSex = findViewById(R.id.self_reSex);
        tv_changeHead = findViewById(R.id.tv_changeHead);

        /*数据初始化*/
        initView();
        /*大监听*/
        ButtonClickLinstener buttonClickLinstener = new ButtonClickLinstener();
        //绑定监听
        btn_back.setOnClickListener(buttonClickLinstener);
        btn_commit.setOnClickListener(buttonClickLinstener);
        tv_changeHead.setOnClickListener(buttonClickLinstener);
    }

    public void initView() {
        tx_reSex.setText(user.getUserSex());
        tx_reSbp.setText(user.getUserIntro());
        tx_reName.setText(user.getUserName());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load(user.gethPath()).apply(options).into(iv_reImg);
        editUserPresenterCompl = new EditUserPresenterCompl(this);
    }

    /*实现接口方法，结束当前界面*/
    @Override
    public void Okfinish() {
        finish();
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
                    buttonInactive();
                    if (isEditedHead) {
                        //准备上传
                        if (isPrepared == true) {
                            startRealUpload(uploadUtils);
                            postEditMsg();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "网络连接较慢,稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        postEditMsg();
                    }
                    break;
                }
                case R.id.tv_changeHead:
                    //动态申请权限
                    isEditedHead = true;
                    askPermission();
                    break;
                case R.id.iv_reImg:
                    isEditedHead = true;
                    askPermission();
                    break;
            }
        }
    }

    /**
     * 获取token，准备上传
     */
    private void startUploadProgress() {
        isPrepared = false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.getInstance().postJson(UPLOAD_USERHEAD_TOKEN_URL, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(EditProfileActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String token = response.body().string();
                uploadUtils = new UploadUtils(token, localImgPath, new File(localImgPath).getName());
                qiNiuImgPath = uploadUtils.getKey();//服务器端图片名称，包含后缀
                isPrepared = true;
                Log.e("Edit--qiniuImgPath", qiNiuImgPath);
            }
        });
    }

    /**
     * 开始上传
     *
     * @param uploadUtils
     */
    private void startRealUpload(UploadUtils uploadUtils) {
        uploadUtils.upload(new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    //上传成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_changeHead.setText("点击更换头像");
                            buttonActive();
                        }
                    });

                } else {
                    //上传失败，打印错误信息
                    Log.e("upload fail", "" + info);

                }
            }
        }, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                tv_changeHead.setText("正在上传···");
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return false;
            }
        });
    }

    private void askPermission() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startSelectPic();
        } else {
            EasyPermissions.requestPermissions(EditProfileActivity.this, "程序必须的权限", 4513, perms);
        }
    }

    /**
     * 选择图片
     */
    private void startSelectPic() {
        PictureSelector.create(EditProfileActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .isWeChatStyle(true)// 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
                .theme(R.style.picture_WeChat_style)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)//图片列表点击缩放效果
                .enableCrop(false)// 是否裁剪 true or false
                .withAspectRatio(1, 1)//一比一剪裁
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    /**
     * 选择图片完成后
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectResultList = PictureSelector.obtainMultipleResult(data);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P || BuildCompat.isAtLeastQ()) {
                localImgPath = selectResultList.get(0).getAndroidQToPath();
            } else {
                localImgPath = selectResultList.get(0).getPath();
            }
            //返回到界面时显示图片
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(this)
                    .load(localImgPath)
                    .apply(options)
                    .into(iv_reImg);
            startUploadProgress();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void postEditMsg() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", user.getUserId());
            obj.put("newName", tx_reName.getText().toString());
            obj.put("newIntro", tx_reSbp.getText().toString());
            obj.put("newSex", tx_reSex.getText().toString());
            obj.put("isEditedHead", isEditedHead);

            if (isEditedHead) {
                obj.put("newHpath", qiNiuImgPath);
            }
            String jsonStr = obj.toString();
            Log.e("json", jsonStr);

            EventBus.getDefault().post(jsonStr);
            OkHttpUtils.getInstance().postJson(Constant.GET_USER_URL + "edit", jsonStr, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Looper.prepare();
                    Toast.makeText(EditProfileActivity.this, "保存失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Looper.prepare();
                    buttonActive();
                    Toast.makeText(EditProfileActivity.this, "信息保存成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buttonActive() {
        btn_back.setEnabled(true);
    }

    public void buttonInactive() {
        btn_back.setEnabled(false);
    }
}
