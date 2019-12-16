package com.hbsd.rjxy.miaomiao.ych.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class AddCatActivity extends Activity {
    ImageView img_back;
    ImageView cat_head;
    TextView tx_head;
    private List<LocalMedia> selectResultList = null;
    private SharedPreferences sp;
    private Gson gson = new Gson();
    private DatePickerDialog dpd;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);
        img_back=findViewById(R.id.img_back);
        cat_head=findViewById(R.id.cat_head);
        tx_head=findViewById(R.id.tx_head);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cat_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });
    }
    private void askPermission(){
        String[] perms = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

        if(EasyPermissions.hasPermissions(this,perms)){
            startSelectPic();
        }else{
            EasyPermissions.requestPermissions(AddCatActivity.this,"程序必须的权限",4513,perms);
        }
    }

    private void startSelectPic(){
        PictureSelector.create(AddCatActivity.this)
                .openGallery(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)//图片列表点击缩放效果
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(1,1)//一比一剪裁
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK){
            selectResultList = PictureSelector.obtainMultipleResult(data);
            for(LocalMedia localMedia : selectResultList){
                Log.e("Path:",""+localMedia.getCutPath());
            }
            uploadFile();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
    //上传文件
    private void uploadFile() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //表单参数
       // builder.addFormDataPart("uid",""+user.getUid());
        /**
         * pictureType中，图片是image/jpeg，视频是video/mp4
         *
         * 文件名可以通过path的/分割字符串如何提出最后一段就是文件名
         *
         */
        for(LocalMedia localMedia : selectResultList){
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),new File(localMedia.getCutPath()));
            Log.e("Cutpath",""+localMedia.getCutPath());
            if(localMedia.getMimeType().equals("image/jpeg")){
                builder.addFormDataPart("type","jpeg");
            }else if(localMedia.getMimeType().equals("video/mp4")){
                builder.addFormDataPart("type","mp4");
            }else{
                //剪裁的话就不是jpeg了
//                return;
            }
            //获取剪裁后的路径，而不是getPath
            String[] args = localMedia.getCutPath().split("/");
            builder.addFormDataPart("file",""+args[args.length-1],fileBody);
            String filename=localMedia.getCutPath();
            cat_head.setImageURI(Uri.fromFile(new File(filename)));
        }

//        RequestBody requestBody = builder.build();
//        Request request = new Request.Builder()
//                .url(URL_HEAD_UPLOAD)
//                .post(requestBody)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
                //TODO 网络连接超时
               // EventInfo<String,String,User> eventInfo = new EventInfo();
//                Map<String ,String> map = new HashMap<>();
//                map.put("status","failUpload");
//                eventInfo.setContentMap(map);
//                EventBus.getDefault().post(eventInfo);
            }

//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                EventInfo<String,String,User> eventInfo = new EventInfo();
//                Map<String ,String> map = new HashMap<>();
//                map.put("status","finishUpload");
//                eventInfo.setContentString(response.body().string());
//                eventInfo.setContentMap(map);
//                EventBus.getDefault().post(eventInfo);
//            }
//        });
//        tx_head.setText("正在上传....");
    }

