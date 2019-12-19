package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zlc.publish.model.PublishActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PERMISSION_NECESSARY;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PICTURESELECT_CAMERA;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PICTURESELECT_VIDEO;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PUBLISH_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.QINIU_URL;
import static com.hbsd.rjxy.miaomiao.utils.Constant.REMIND_PUBLISH_ONCE;


/*
    TODO    用户退出登录一定要删除sp中的草稿信息
 */
public class InfoAndCommentActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.cc_viewPager)
    ViewPager viewPager;

    @BindView(R.id.tv_videocomment)
    TextView tvComment;

    List<Fragment> fragments;
    List<LocalMedia> selectResultList;

    PopupWindow popupWindow;
    View popupView;

    PopupWindow popupConfirmWindow;
    View popupConfirmView;

    @BindView(R.id.pb_upload)
    NumberProgressBar progressBar;

    private int type = -1;  //0：视频，1：图片，2：纯文字

    private int miid;
    private int currentItem;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catinfo_comment_layout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle.getSerializable("from").equals("commentPic")){
            currentItem = 1;
        }else{
            currentItem = 0;
        }
        Log.e("currentItem",""+currentItem);
        miid = (int) bundle.getSerializable("miid");

        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        fragments.add(new CatinfoFragment());
        fragments.add(new CommentFragment(miid));
        viewPager.setAdapter(new CustomPageAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        viewPager.setCurrentItem(currentItem);

        SharedPreferences sp = getSharedPreferences(PUBLISH_SP_NAME,MODE_PRIVATE);

        tvComment.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = new Intent(InfoAndCommentActivity.this,PublishActivity.class);
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
            TODO    纯文本草稿

         */
        tvComment.setOnLongClickListener(new View.OnLongClickListener() {
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

                        Intent intent = new Intent(InfoAndCommentActivity.this,PublishActivity.class);
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
        Intent intent = new Intent(InfoAndCommentActivity.this, PublishActivity.class);
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
        PictureSelector.create(InfoAndCommentActivity.this)
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
        PictureSelector.create(InfoAndCommentActivity.this)
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
