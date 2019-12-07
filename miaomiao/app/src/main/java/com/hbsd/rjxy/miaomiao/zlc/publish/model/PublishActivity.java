package com.hbsd.rjxy.miaomiao.zlc.publish.model;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hbsd.rjxy.miaomiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    @BindView(R.id.et_edit)
    EditText etEdit;            //编辑内容EditText
    @BindView(R.id.pb_publish)
    NumberProgressBar progressBar;  //上传进度条
    @BindView(R.id.ll_publish_back)
    LinearLayout llPublish;         //返回点击布局
    @BindView(R.id.btn_publish)
    Button btnPublish;              //发布按钮
    @BindView(R.id.tv_log)
    TextView tvLog;             //上传状态提示文字

    private boolean isUploadComplete = false ;  // 判断当前activity的状态是否已经完成了上传（只有在type=0/1的时候判断）
    private boolean isCanceled = false ; //取消上传的参数，onDestroy和返回的点击事件（判断是否已经上传完成）中都要修改这个变量
    private boolean etEmpty = true;     //et是否是空的
    private int type = -1;  //当前编辑的模式  0  1  2
    /*
        TODO
            在点击发布时先判断是否存在草稿，sp中的have_draft是否为true（default="false"）
            纯文本方式的
            (1)如果存在草稿，直接跳转到这个activity并且bundle中的isdraft为true，其他跳转方式的isdraft都是false
            <----存在草稿的情况--->
            先转成multi_info，如果是文本形式，那么直接拿到内容然后修改et就行了
            如果是视频或者图片，再去sp中拿have_canceled_file，如果是true，那么去拿文件path，重新启动上传文件的流程
            <-----不存在草稿的情况---->
            选择完视频/图片之后跳转到这个activity，intent中传递一个bundle对象，里面有用户选择的（1）type，0视频，1图片，2纯文字
            （2）cid，uid，mformat，
            （3）如果是视频的话获取视频第一帧的图片
            生成multi_info对象
            如果是 type 为 0/1，进度条可视，开始进行：
            <-----文件上传的流程------>
                1.向服务器请求token，
                在onResponse回调中初始化UploadUtils，调用上传方法，传入三个匿名内部类实现上传失败/完成，上传进度，取消上传的功能
                         （1）上传失败，改变tvLog为可视，修改文字为上传失败！，提示用户是否重新上传，打印log，向服务器发送错误信息info，点击（1）是-->重新调用文件上传方法（修改tvLog可视状态），（2）否-->activity跳转
                         （2）上传成功,改变tvLog为可视，修改文字为上传成功！，开启线程，3.5秒之后隐藏进度条和tvLog
                                如果是视频上传，使用之前的token继续上传视频的第一帧图片，上传完成的回调中修改isUploadComplete为true
                         （3）取消上传，在onDestroy方法里修改isCancel为true，点击返回的时候修改isCancel为true（这里需要测试是否修改完成之后立刻停止了上传）
                在onFailure回调中提示用户连接服务器失败，是否重试，用户点击(1)是-->重新请求token，(2)否-->activity跳转
                2.保存本地草稿，如果编辑到一半，突然用户退出了，突然想返回了，那么onDestroy和返回的点击事件中都应该先判断当前发布类型是否是纯文本类型
                *****询问用户是否保存草稿，用户点击是再做如下判断
                    （1）纯文本类型，判断文本内容是否为空，如果不为空那么在sp中记录下这次发布的multi_info，并且修改sp中的have_draft为true
                    （2）视频/图片类型，先判断是否上传完成，如果上传成功那么保存multi_info，修改sp中的have_draft为true
                                        如果上传未完成，取消上传，保存文件path到sp，修改have_canceled_file为true（下次进入的时候重新上传）
            <---发布按钮--->
            （1）先判断type，如果是纯文本，文本内容不为空，发布
            （2）判断isUploadComplete是否是true，不是true提示用户不可发送
            （3）发布之前生成date
            （4）intent启动，传入的bundle中有一个type，一个multi_info，里面是json，上传在朋友圈的activity中开启线程
            （5）如果上传成功，什么都不用动，上传失败，那么移除这个multi_info，提示用户发布失败
            <------视频，图片的展示，最后再做------>
            放在下面，因为会弹出键盘，所以在键盘之上，et之下，图片限制只能上传一张
            （多图上传，断点续传，后续完善）
            <----存草稿--->
            sp存type
            纯文本的multi_info  存 textdraftbody
            视频/图片的multi_info 存 draftbody
            未上传文件的时候have_canceled_file  （boolean）true
            canceled_file_path  （string）文件路径

     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);

        //修改透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        ButterKnife.bind(this);


        etEdit.addTextChangedListener(this);
        etEdit.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getInt("type");

        Log.e("isdraft",""+bundle.getSerializable("isdraft"));
        Log.e("type",""+bundle.getSerializable("type"));
        Log.e("path",""+bundle.getSerializable("path"));
        Log.e("dasdas","type:"+type+"etEmpty:"+etEmpty);

        initButton();


    }

    public void initButton(){
        if(type == 2){
            if(etEmpty){
                btnPublish.setBackground(getResources().getDrawable(R.drawable.btn_invalid));
                btnPublish.setTextColor(getResources().getColor(R.color.btnInvalidText));
            }
        }
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(etEdit.getText().toString().equals("")){
            etEmpty = true;
        }else{
            etEmpty = false;
            if(type == 2){
                btnPublish.setBackground(getResources().getDrawable(R.drawable.btn_selector));
                btnPublish.setTextColor(getResources().getColor(R.color.white));
            }
        }
        if(type == 2 && etEmpty){
            btnPublish.setBackground(getResources().getDrawable(R.drawable.btn_invalid));
            btnPublish.setTextColor(getResources().getColor(R.color.btnInvalidText));
        }
        Log.e("whatch","sssss");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
