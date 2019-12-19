package com.hbsd.rjxy.miaomiao.ych.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.os.BuildCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.GlideEngine;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.MainActivity;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.UploadUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
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

import static com.hbsd.rjxy.miaomiao.utils.Constant.CAT_HEAD_TOKEN;
import static com.hbsd.rjxy.miaomiao.utils.Constant.QINIU_URL;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_ADD_CAT_HEAD;
import static com.hbsd.rjxy.miaomiao.utils.Constant.URL_ADD_CAT_INFO;

public class AddCatActivity extends Activity {
    ImageView img_back;
    String key;
    Handler handler;
    ImageView cat_head;
    TextView tx_head;
    private List<LocalMedia> selectResultList = null;
    private SharedPreferences sp;
    private Gson gson = new Gson();
    private DatePickerDialog dpd;
    private OkHttpClient okHttpClient;
    private Cat cat=new Cat();
    String uid;
    String filename="cat_head";
    String filepath;
    String TAG="AddCatActivity";
    private EditText et_name;
    private EditText et_intro;
    private EditText et_food;
    private EditText et_toy;
    private EditText et_breed;
    private EditText et_weight;
    private EditText et_sex;
    private TextView tv_xz;
    private TextView tv_meetday;
    Button commit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);
        tv_meetday=findViewById(R.id.meetday);
        img_back = findViewById(R.id.img_back);
        cat_head = findViewById(R.id.cat_head);
        tx_head = findViewById(R.id.tx_head);
        et_breed=findViewById(R.id.et_breed);
        et_food=findViewById(R.id.et_food);
        et_intro=findViewById(R.id.et_intro);
        et_name=findViewById(R.id.et_cname);
        et_weight=findViewById(R.id.et_cweight);
        et_toy=findViewById(R.id.et_toy);
        et_sex=findViewById(R.id.et_csex);
        commit=findViewById(R.id.btn_tj);
        tv_xz=findViewById(R.id.tv_xz);
        Log.e(TAG, "onCreate: 111111111111" );
        okHttpClient=new OkHttpClient();
        tv_xz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                dpd = new DatePickerDialog(AddCatActivity.this,this::onDateSet,1999,0,1);
                dpd.show();
            }
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year+"年"+(month+1)+"月"+dayOfMonth+"日";
                Log.e(TAG, "onDateSet: 12345600"+date );
                tv_meetday.setText(date);
            }
//
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Map map=new HashMap();
                String breed= et_breed.getText().toString();
                Log.e(TAG, "onClick: "+breed );
                cat.setCbreed(breed);
                cat.setUid(1);
                cat.setCfood(et_food.getText().toString());
                cat.setCname(et_name.getText().toString());
                cat.setCintro(et_intro.getText().toString());
                cat.setCsex(et_sex.getText().toString());
                cat.setCtoy(et_toy.getText().toString());
                SimpleDateFormat simpleDateFormat = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    simpleDateFormat = new SimpleDateFormat("yyyy年mm月dd");
                }
                try {
                    Date date = simpleDateFormat.parse(tv_meetday.getText().toString());
                    cat.setCbirthday( date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String str= gson.toJson(cat);
                Log.e(TAG, "onClick: str:"+str );
                map.put("str",str);
                OkHttpUtils.getInstance().postForm(URL_ADD_CAT_INFO, map, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(TAG, "onFailure: sbbbbbbbb" );
                        Toast.makeText(AddCatActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.e(TAG, "onResponse: cggggggggg" );
//                        Toast.makeText(AddCatActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AddCatActivity.this, MainActivity.class);
                        startActivity(intent);
//                        finish();
                    }
                });
            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                cat= (Cat) msg.obj;
            }
        };
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cat_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onCreate: 22222222222" );
                askPermission();
            }
        });
    }

    private void askPermission() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
            startSelectPic();
        } else {
            EasyPermissions.requestPermissions(AddCatActivity.this, "程序必须的权限", 4513, perms);
        }
    }

    private void startSelectPic() {
        PictureSelector.create(AddCatActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())
                .theme(R.style.picture_WeChat_style)
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
                .withAspectRatio(1, 1)//一比一剪裁
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: 3333333333333333" );
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectResultList = PictureSelector.obtainMultipleResult(data);
            String QPath;
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.P|| BuildCompat.isAtLeastQ()){
                QPath=selectResultList.get(0).getAndroidQToPath();
            }else {
                QPath=selectResultList.get(0).getPath();
            }
            for (LocalMedia localMedia : selectResultList) {
                Log.e("QPath:", "" + localMedia.getCutPath());
            }
            uploadFile();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
        for (LocalMedia localMedia : selectResultList) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), new File(localMedia.getCutPath()));
            Log.e("Cutpath", "" + localMedia.getCutPath());
            if (localMedia.getMimeType().equals("image/jpeg")) {
                builder.addFormDataPart("type", "jpeg");
            } else if (localMedia.getMimeType().equals("video/mp4")) {
                builder.addFormDataPart("type", "mp4");
            } else {
                //剪裁的话就不是jpeg了
//                return;
            }
            //获取剪裁后的路径，而不是getPath
            String[] args = localMedia.getCutPath().split("/");
            builder.addFormDataPart("file", "" + args[args.length - 1], fileBody);
            filepath = localMedia.getCutPath();
            filename =new File(filepath).getName();
            cat_head.setImageURI(Uri.fromFile(new File(filepath)));
        }
//        UploadUtils uploadUtils = new UploadUtils();
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(CAT_HEAD_TOKEN)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+"请求失败" );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "onResponse: "+response.body().string() );
                Log.e(TAG, "onResponse: "+filename );
                Log.e(TAG, "onResponse: "+filepath );
                UploadUtils uploadUtils=new UploadUtils(response.body().string()+"",filepath,filename);

                key=uploadUtils.getKey() ;
                Log.e(TAG, "onResponse: key："+key );
                uploadUtils.upload();
                updateData(key);
                tx_head.setText("上传成功");
            }
        });

        tx_head.setText("正在上传....");
    }
    public String updateData(String key){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = builder.addFormDataPart("url","http://"+QINIU_URL+"/"+key).build();
        Request request = new Request.Builder()
                .url(URL_ADD_CAT_HEAD)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: sb" );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                cat=gson.fromJson(response.body().string(),Cat.class);
                Message msg=Message.obtain();
                msg.obj=cat;
                Log.e(TAG, "onResponse: cg" );
                handler.sendMessage(msg);
            }
        });

        return null;
    }
}

