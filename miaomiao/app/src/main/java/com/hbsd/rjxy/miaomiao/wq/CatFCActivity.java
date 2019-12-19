package com.hbsd.rjxy.miaomiao.wq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zlc.publish.model.PublishActivity;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.hbsd.rjxy.miaomiao.utils.Constant.PERMISSION_NECESSARY;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PICTURESELECT_VIDEO;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PUBLISH_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.REMIND_PUBLISH_ONCE;

public class CatFCActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    /** Called when the activity is first created. */
    private TabHost tabHost;
    private ImageButton ibfanhui;
    private ImageButton ibfabu;
    private RecyclerView mRvTextList;


    private List<LocalMedia> selectResultList;
    private View popupView;
    private PopupWindow popupWindow;
    private int type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_fc);
        findViews();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        handle(bundle);

        mRvTextList=findViewById(R.id.my_recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(CatFCActivity.this);
//        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRvTextList.setLayoutManager(new LinearLayoutManager(CatFCActivity.this,RecyclerView.VERTICAL,true));
        mRvTextList.setAdapter(new TextListAdapter(this));


        try{
            tabHost = (TabHost) this.findViewById(R.id.TabHost01);
            tabHost.setup();

            tabHost.addTab(tabHost.newTabSpec("tab_1")
                    .setContent(R.id.L1)
                    .setIndicator("one",this.getResources().getDrawable(R.drawable.miao_head)));
            tabHost.addTab(tabHost.newTabSpec("tab_2")
                    .setContent(R.id.L2)
                    .setIndicator("two", this.getResources().getDrawable(R.drawable.miao_head)));
            tabHost.addTab(tabHost.newTabSpec("tab_3")
                    .setContent(R.id.L3)
                    .setIndicator("three", this.getResources().getDrawable(R.drawable.miao_head)));
            tabHost.setCurrentTab(0);
        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("EXCEPTION", ex.getMessage());
        }
    }

    //获取cid
    private void handle(Bundle bundle) {

    }

    private void  findViews(){
        ibfanhui = findViewById(R.id.fanhui);
        ibfabu = findViewById(R.id.fabu);
    }
    public void  onClick(View v){
        switch (v.getId()){
            case R.id.fanhui:
                finish();
//                Intent intent = new Intent(MainActivity.this,CatMainActivity.class);
//                startActivity(intent);
                break;
            case R.id.fabu:
                Intent intent1 = new Intent(CatFCActivity.this, PublishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("","");
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;

        }
    }





//    private class setOnLongClickListener implements View.OnLongClickListener{
//
//        @Override
//        public boolean onLongClick(View v) {
//            Intent intent1 = new Intent(MainActivity.this,fabu_wenzi.class);
//            startActivity(intent1);
//            return true;
////            return false;
//        }
//    }




    /*
        TODO:   下面是zlc写的发布按钮的逻辑
     */

    public void initPublishButton(){
        SharedPreferences sp = getSharedPreferences(PUBLISH_SP_NAME,MODE_PRIVATE);

        /*
            TODO:点击事件
         */
        ibfabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    TODO    （文件草稿）
                        判断sp中have_draft是否为true（default = "false"）
                        从sp中拿这次draft的type（int）
                        bundel里面添加type（0，1）  isdraft（true，false）
                        检查是否存在未上传完成的文件  have_canceled_file（default = false）
                        如果存在，去canceled_file_path拿文件path
                        bundel添加iscanceled（true，false）  canceled_file_path
                        bundel添加draftbody
                        跳转

                 */
                if(checkHaveDraft(sp)){
                    int type = sp.getInt("type",-1);
                    if(type == 0 || type == 1){
                        Intent intent = new Intent(CatFCActivity.this,PublishActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("type",type);
                        bundle.putSerializable("isdraft","true");
                        //检查是否有未上传完成的图片
                        if(sp.getBoolean("have_canceled_file",false)){
                            String path = sp.getString("canceled_file_path","");
                            bundle.putSerializable("iscanceled","true");
                            bundle.putSerializable("canceled_file_path",path);
                        }else{
                            String path = sp.getString("finished_file_path","");
                            bundle.putSerializable("iscanceled","false");
                            bundle.putSerializable("finished_file_path",path);
                        }
                        bundle.putSerializable("draftbody",sp.getString("draftbody",""));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }else{
                    popupwindow(v);
                }
            }
        });

        /*
            TODO:长点击事件
         */

        ibfabu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*
                    TODO    用户第一次点击会提示用户这个功能还在测试中，请尽量分享图片或视频
                            （用户第一次登录后在sp中写一个REMIND_PUBLISH_ONCE = "NEEDREMIND"）

                 */
                if(sp.getString(REMIND_PUBLISH_ONCE,"NEEDREMIND").equals("NEEDREMIND")){
                    /*
                        TODO    提醒用户
                            修改sp，不再提醒
                     */
                    popupConfirmWindow(v);
                    sp.edit().putString(REMIND_PUBLISH_ONCE,"DONTREMIND").commit();

                }else{
                    //直接调用
                    if(checkHaveTextDraft(sp)){

                        Intent intent = new Intent(CatFCActivity.this,PublishActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("type",2);
                        bundle.putSerializable("isdraft","true");
                        bundle.putSerializable("draftbody",sp.getString("textdraftbody",""));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        startPublishActivity(2,null);
                    }

                }
                //不传递点击事件
                return true;
            }
        });

    }

    /*
        TODO    检查是否存在视频/图片形式的草稿
     */
    private boolean checkHaveDraft(SharedPreferences sp) {
        boolean have_draft = sp.getBoolean("have_draft",false);
        if(have_draft){
            return true;
        }
        return false;


    }



    /*
        TODO    检查石头存在纯文本形式的草稿
     */
    private boolean checkHaveTextDraft(SharedPreferences sp){
        boolean have_text_draft = sp.getBoolean("have_text_draft",false);
        if(have_text_draft){
            return true;
        }
        return false;
    }


    /*
        TODO
            三种方式
            （1）拍摄视频过来的
            （2）相册选择了视频
            （3）相册选择了图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //适配了安卓Q
        if(requestCode == PICTURESELECT_VIDEO && resultCode == Activity.RESULT_OK){
            Log.e("这是","拍摄视频");
            selectResultList = PictureSelector.obtainMultipleResult(data);
            String Qpath;
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P || BuildCompat.isAtLeastQ()){
                Qpath = selectResultList.get(0).getAndroidQToPath();
            }else{
                Qpath = selectResultList.get(0).getPath();
            }
            int dotPos = Qpath.lastIndexOf(".");
            String fileExt = Qpath.substring(dotPos + 1).toLowerCase();
            Log.e("选择的类型是",""+fileExt);
            Log.e("文件大小为：",""+selectResultList.get(0).getSize());
            type = 0;
            startPublishActivity(type,Qpath);

        }else if(requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK){
            Log.e("这是","相册选择");

            selectResultList = PictureSelector.obtainMultipleResult(data);
            String Qpath;
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P || BuildCompat.isAtLeastQ()){
                Qpath = selectResultList.get(0).getAndroidQToPath();
            }else{
                Qpath = selectResultList.get(0).getPath();
            }
            int dotPos = Qpath.lastIndexOf(".");
            String fileExt = Qpath.substring(dotPos + 1).toLowerCase();
            Log.e("选择的类型是",""+fileExt);
            Log.e("路径：",""+Qpath);
            if("mp4".equals(fileExt)){
                type = 0;
            }else if("jpg".equals(fileExt) || "png".equals(fileExt) || "jpeg".equals(fileExt)){
                type = 1;
            }else{
                Log.e("未能识别的文件格式",""+fileExt);
            }
            if(type != -1){
                startPublishActivity(type,Qpath);
            }
        }else{
            Log.e("未识别的requestCode",""+requestCode);
        }

    }

    /*
        TODO
            这是用于提示用户，在用户第一次使用纯文本发布功能的时候

     */
    private void popupConfirmWindow(View v) {
        popupView = LayoutInflater.from(this).inflate(R.layout.publish_remind_popupwindow,null);
        popupWindow=new PopupWindow(popupView, dip2px(this,350) , dip2px(this,150), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);

        Button btnConfirm = popupView.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                        TODO    以type = 2 跳转
                 */
                startPublishActivity(2,null);
                popupWindow.dismiss();
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

    /*i
        TODO    跳转方法，传入一个type类型和一个nullable的String path
     */
    private void startPublishActivity(int i, @Nullable String path) {
        Intent intent = new Intent(CatFCActivity.this, PublishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("type",i);
        bundle.putSerializable("isdraft","false");
        if(i == 2){
            //纯文本

        }else{
            //视频
            bundle.putSerializable("path",path);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /*
    TODO    这是点击发布之后弹出的选择框
     */
    private void popupwindow(View v) {
        popupView = LayoutInflater.from(this).inflate(R.layout.publishpopupwindow_layout,null);
        popupWindow=new PopupWindow(popupView, dip2px(this,350) , dip2px(this,90), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);

        Button btnAlbum = popupView.findViewById(R.id.btn_album);
        Button btnVideo = popupView.findViewById(R.id.btn_video);

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionForVideo();
                type = 0;
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

    //  将物理像素换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void askPermissionForAlbum(){
        if(EasyPermissions.hasPermissions(this,PERMISSION_NECESSARY)){
            startAlbum();
        }else{
            EasyPermissions.requestPermissions(this,"打开相册必须的权限",5000,PERMISSION_NECESSARY);
        }
    }

    private void askPermissionForVideo(){
        if(EasyPermissions.hasPermissions(this,PERMISSION_NECESSARY)){
            startVideo();
        }else{
            EasyPermissions.requestPermissions(this,"拍摄视频必须的权限",4999,PERMISSION_NECESSARY);
        }
    }

    /*
    启动相机
     */
    private void startVideo(){
        PictureSelector.create(CatFCActivity.this)
                .openCamera(PictureMimeType.ofVideo())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .recordVideoSecond(15)//视频秒数录制 默认60s int
                .forResult(PICTURESELECT_VIDEO);       //request码是相机请求
        popupWindow.dismiss();
    }


    /*
    启动相册
     */
    private void startAlbum() {
        PictureSelector.create(CatFCActivity.this)
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
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩 true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(30)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(2)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(15)//视频秒数录制 默认60s int
                .enableCrop(false)// 是否裁剪 true or false
                .rotateEnabled(false)//禁止旋转
                .forResult(PictureConfig.CHOOSE_REQUEST);
        popupWindow.dismiss();
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode == 5000){
            startAlbum();
        }else if(requestCode == 4999){
            startVideo();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"没有授予权限不能上传哦~",Toast.LENGTH_SHORT).show();
    }


}
