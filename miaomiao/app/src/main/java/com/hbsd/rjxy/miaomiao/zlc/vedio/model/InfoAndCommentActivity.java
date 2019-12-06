package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.publish.model.PublishActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

import static com.hbsd.rjxy.miaomiao.utils.Constant.QINIU_URL;

public class InfoAndCommentActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.cc_viewPager)
    ViewPager viewPager;

    @BindView(R.id.tv_videocomment)
    TextView tvComment;

    List<Fragment> fragments;
    List<LocalMedia> selectResultList;

    PopupWindow popupWindow;
    View popupView;

    @BindView(R.id.pb_upload)
    NumberProgressBar progressBar;

    private int type = -1;  //0：视频，1：图片，2：纯文字


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(EventInfo eventInfo){
        progressBar.setProgress((int)Math.round((double)eventInfo.getContentMap().get("progress")*100));
        if((int)Math.round((double)eventInfo.getContentMap().get("progress")*100) == 100){
            progressBar.setVisibility(View.INVISIBLE);
            Log.e("url",""+eventInfo.getContentString());
            Intent intent = new Intent(InfoAndCommentActivity.this, PublishActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type",type);
            bundle.putSerializable("url",eventInfo.getContentString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catinfo_comment_layout);

        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        fragments.add(new CatinfoFragment());
        fragments.add(new CommentFragment());
        viewPager.setAdapter(new CustomPageAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        EventBus.getDefault().register(this);


        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindow(v);
            }
        });



    }

    private class CustomPageAdapter extends FragmentPagerAdapter {


        public CustomPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }




    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK){
            selectResultList = PictureSelector.obtainMultipleResult(data);
            for(LocalMedia localMedia : selectResultList){

            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid","1");
                jsonObject.put("cid","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(InfoAndCommentActivity.this, PublishActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("type",type);
            bundle.putSerializable("url",selectResultList.get(0).getPath());
            intent.putExtras(bundle);
            startActivity(intent);

//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setMax(100);
//            progressBar.setProgress(0);
//            OkHttpUtils.getInstance().postJson("http://10.7.87.224:8080/publish/getToken", jsonObject.toString(), new Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    String token = response.body().string();
//                    String dataPath = selectResultList.get(0).getPath();
//                    String key = new File(selectResultList.get(0).getPath()).getName();
//                    new UploadUtils(token, dataPath, key).upload(new UpCompletionHandler() {
//                        @Override
//                        public void complete(String key, ResponseInfo info, JSONObject response) {
//                            if(info.isOK()){
//                                Toast.makeText(InfoAndCommentActivity.this,"上传完成",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(InfoAndCommentActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
//                                Log.e("erro","upload fail info:"+info);
//                            }
//                        }
//                    }, new UpProgressHandler() {
//                        @Override
//                        public void progress(String key, double percent) {
//                            Log.e("progress",":"+percent);
//                            Map<String,Double> map = new HashMap<>();
//                            map.put("progress",percent);
//                            EventInfo eventInfo = new EventInfo();
//                            eventInfo.setContentMap(map);
//                            eventInfo.setContentString(QINIU_URL+"/"+key);
//                            EventBus.getDefault().post(eventInfo);
//                        }
//                    }, new UpCancellationSignal() {
//                        @Override
//                        public boolean isCancelled() {
//                            return false;
//                        }
//                    });
//                }
//            });

        }
    }


    private void popupwindow(View v) {
        popupView = LayoutInflater.from(this).inflate(R.layout.publishpopupwindow_layout,null);
        popupWindow=new PopupWindow(popupView, dip2px(this,300) , dip2px(this,200), true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        Button btnVideo=popupView.findViewById(R.id.btn_shortvideo);
        Button btnPic=popupView.findViewById(R.id.btn_pic);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionForVideo();
                type = 0;
                popupWindow.dismiss();

            }
        });
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askPermissionForPic();
                type = 1;
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(v, Gravity.CENTER,0,20);
    }

    //  将物理像素装换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void askPermissionForVideo(){
        String[] perms = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

        if(EasyPermissions.hasPermissions(this,perms)){
            startSelectVideo();
        }else{
            EasyPermissions.requestPermissions(this,"程序必须的权限",4513,perms);
        }
    }

    private void askPermissionForPic(){
        String[] perms = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

        if(EasyPermissions.hasPermissions(this,perms)){
            startSelectPic();
        }else{
            EasyPermissions.requestPermissions(this,"程序必须的权限",4513,perms);
        }
    }


    private void startSelectVideo(){
        PictureSelector.create(InfoAndCommentActivity.this)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .isWeChatStyle(true)// 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
                .theme(R.style.picture_WeChat_style)
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                .querySpecifiedFormatSuffix(PictureMimeType.ofMP4())// 查询指定后缀格式资源
                .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩 true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(30)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(2)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(30)//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void startSelectPic(){
        PictureSelector.create(InfoAndCommentActivity.this)
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
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(type == 0){
            startSelectVideo();
        }else if(type == 1){
            startSelectPic();
        }else{
            //TODO
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"没有授予权限不能上传哦~",Toast.LENGTH_SHORT).show();
    }



}
