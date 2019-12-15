package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import okhttp3.Call;
import okhttp3.Callback;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.GlideEngine;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.InfoAndCommentActivity;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.UploadUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.EditProfileView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.hbsd.rjxy.miaomiao.utils.Constant.PERMISSION_NECESSARY;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PICTURESELECT_CAMERA;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PICTURESELECT_VIDEO;
import static com.hbsd.rjxy.miaomiao.utils.Constant.UPLOAD_USERHEAD_TOKEN_URL;


public class EditProfileActivity extends AppCompatActivity implements EditProfileView,EasyPermissions.PermissionCallbacks{
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
    private User user;
    private UploadUtils uploadUtils;
    List<LocalMedia> selectResultList;

    PopupWindow popupWindow;
    View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /*接收我的界面传来的数据*/
        intent = getIntent();
        String str = intent.getStringExtra("user");
        //todo 拿到服务器端图片地址
        qiNiuImgPath=intent.getStringExtra("hpath");
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
        /*TODO
         *   图片的设置
         *   */
        tx_reSex.setText(user.getUserSex());
        tx_reSbp.setText(user.getUserIntro());
        tx_reName.setText(user.getUserName());
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
                    String newName = tx_reName.getText().toString();
                    String newsbp = tx_reSbp.getText().toString();
                    String newSex = tx_reSex.getText().toString();
                    //Log.e("读取到当下想修改的用户名为",newName);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("uid", user.getUserId());
                        obj.put("newName", newName);
                        obj.put("newIntro", newsbp);
                        obj.put("newSex", newSex);
                        obj.put("newHpath",qiNiuImgPath);
                        String jsonStr = obj.toString();
                        Log.e("json", jsonStr);
                        //开始上传头像到服务器
                        startRealUpload(uploadUtils);
                        //todo 有待解决因为网络原因暂时拿不到服务器端图片地址的情况
                        editUserPresenterCompl.editUser(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case R.id.tv_changeHead:
                    //动态申请权限
                    popupwindow(v);
//                    ActivityCompat.requestPermissions(EditProfileActivity.this,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    break;
            }
        }
    }

//    //用户允许权限
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100) {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, 200);
//        }
//    }

//    //选择图片返回后执行此方法
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 200 && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            Cursor cursor = getContentResolver().query(uri, null, null,
//                    null, null);
//            if (cursor.moveToFirst()) {
//                localImgPath = cursor.getString(cursor.getColumnIndex("_data"));
//                Log.e("imgPath", localImgPath);
//                RequestOptions options = new RequestOptions().circleCrop();
//                //返回到界面时显示图片
//                Glide.with(this)
//                        .load(localImgPath)
//                        .apply(options)
//                        .into(iv_reImg);
//                //准备上传头像到服务器端
//                startUploadProgress();
//            }
//
//        }
//    }

    /**
     * 获取token，准备上传
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
                uploadUtils = new UploadUtils(token, localImgPath, new File(localImgPath).getName());
                qiNiuImgPath = uploadUtils.getKey();//服务器端图片名称，包含后缀
                Log.e("Edit--qiniuImgPath",qiNiuImgPath);
            }
        });
    }

    /**
     * 开始上传
     * @param uploadUtils
     */
    private void startRealUpload(UploadUtils uploadUtils) {
        uploadUtils.upload();
    }

    /*
       这是点击选择头像上传之后弹出的选择框
     */
    private void popupwindow(View v) {
        popupView = LayoutInflater.from(this).inflate(R.layout.uploadhead_popupwidow,null);
        popupWindow=new PopupWindow(popupView, dip2px(this,350) , dip2px(this,90), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);

        Button btnAlbum = popupView.findViewById(R.id.btn_album);
        Button btnCamera = popupView.findViewById(R.id.btn_camera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionForCamera();
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionForAlbum();
            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.showAtLocation(v, Gravity.CENTER,0,20);
    }

    private void askPermissionForAlbum(){
        if(EasyPermissions.hasPermissions(this,PERMISSION_NECESSARY)){
            startAlbum();
        }else{
            EasyPermissions.requestPermissions(this,"打开相册必须的权限",5001,PERMISSION_NECESSARY);
        }
    }


    private void askPermissionForCamera(){
        if(EasyPermissions.hasPermissions(this,PERMISSION_NECESSARY)){
            startCamera();
        }else{
            EasyPermissions.requestPermissions(this,"拍摄照片必须的权限",5002,PERMISSION_NECESSARY);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICTURESELECT_CAMERA:
                    Log.e("上传","拍照");
                    selectResultList = PictureSelector.obtainMultipleResult(data);
                    localImgPath=selectResultList.get(0).getPath();
                    Log.e("地址",selectResultList.get(0).getPath());
                    int dotPos = selectResultList.get(0).getPath().lastIndexOf(".");
                    String fileExt = selectResultList.get(0).getPath().substring(dotPos + 1).toLowerCase();
                    Log.e("选择的类型是",""+fileExt);
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    //图片选择结果回调
                    Log.e("上传","相册选择");
                    selectResultList = PictureSelector.obtainMultipleResult(data);
                    localImgPath=selectResultList.get(0).getPath();
                    Log.e("地址",selectResultList.get(0).getPath());
                    int dotPos1 = selectResultList.get(0).getPath().lastIndexOf(".");
                    String fileExt1 = selectResultList.get(0).getPath().substring(dotPos1 + 1).toLowerCase();
                    Log.e("选择的类型是",""+fileExt1);
                    break;

            }
        }
    }

    /*
    启动相册
     */
    private void startAlbum() {
        PictureSelector.create(EditProfileActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .isWeChatStyle(true)// 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
                .theme(R.style.picture_WeChat_style)
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewVideo(true)// 是否可预览视频 true or false
//                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩 true or false
//                .openClickSound(true)// 是否开启点击声音 true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .videoQuality(1)// 视频录制质量 0 or 1 int
//                .videoMaxSecond(30)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(2)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond(15)//视频秒数录制 默认60s int
                .enableCrop(false)// 是否裁剪 true or false
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);
        popupWindow.dismiss();
    }

    /*
    启动相机
     */
    private void startCamera(){
        PictureSelector.create(EditProfileActivity.this)
                .openCamera(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .forResult(PICTURESELECT_CAMERA);       //request码是相机请求
        popupWindow.dismiss();
    }



    /**
     *分别返回授权成功和失败的权限
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode == 5001){
            startAlbum();
        }else if(requestCode == 5002){
            startCamera();
        }

    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"没有授予权限不能上传哦~",Toast.LENGTH_SHORT).show();
    }

    //  将物理像素换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
