package com.hbsd.rjxy.miaomiao.zlc.publish.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zlc.vedio.model.UploadUtils;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.hbsd.rjxy.miaomiao.utils.Constant.PUBLISH_SP_NAME;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PUBLISH_URL_PUBLISH;
import static com.hbsd.rjxy.miaomiao.utils.Constant.PUBLISH_URL_TOKEN;
import static com.hbsd.rjxy.miaomiao.utils.Constant.QINIU_URL;

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
    private boolean needToUpload = false;   //是否需要上传
    private String path;
    private Multi_info multi_info;

    private PopupWindow saveWindow;     //保存的popupwindow
    private PopupWindow exitWindow;     //退出编辑的popupwindow

    Gson gson = new Gson();

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
            <---测试-->
            未上传失败后重新开启上传的情况

     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void publishReceive(EventInfo eventInfo){
        /*
            TODO    目前的场景有三个
                （1）上传成功：修改tbLog，开启线程3.5秒后隐藏tbLog和pb

         */
        if("dismissProgressbar".equals(eventInfo.getContentString())){
            initProgressbar();
        }

        if("startUpload".equals(eventInfo.getContentString())){
            showProgressbar();
            progressBar.setMax(100);
            progressBar.setProgress(0);
            tvLog.setText(0+"%");
        }
        if("uploadFinish".equals(eventInfo.getContentString())){
            tvLog.setText("上传成功");
            Log.e("上传后的文件url是",""+eventInfo.getContentMap().get("url"));
            //上传完成，把multi_info的mpath设置一下
            multi_info.setMpath((String) eventInfo.getContentMap().get("url"));
            multi_info.setType(type);
            needToUpload = false;
            isUploadComplete = true;
            initButton();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(3500);
                        EventInfo eventInfo = new EventInfo();
                        eventInfo.setContentString("dismissProgressbar");
                        EventBus.getDefault().post(eventInfo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        if("uploadError".equals(eventInfo.getContentString())){
            tvLog.setText("上传失败，3秒后重新上传");
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(3500);
                        startUploadProgress();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        if("progressUpdate".equals(eventInfo.getContentString())){
            int progressNum = (int) Math.round((double)eventInfo.getContentMap().get("progress")*100);
            tvLog.setText(progressNum+"%");
            progressBar.setProgress(progressNum);
        }
        if("finishPublishing".equals(eventInfo.getContentString())){
            finish();
        }


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);



        //修改透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        etEdit.addTextChangedListener(this);
        etEdit.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        checkDraft(bundle);

        initButton();
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布的逻辑
                /*
                    TODO    如果是视频/图片发布，修改have_draft属性，如果是纯文本，修改haveTextDraft
                 */
                SharedPreferences sp = getSharedPreferences(PUBLISH_SP_NAME,MODE_PRIVATE);
                if(type != 2){
                    sp.edit().putBoolean("have_draft",false).commit();
                }else{
                    sp.edit().putBoolean("have_text_draft",false).commit();
                }
                multi_info.setType(type);
                multi_info.setCid(1);
                multi_info.setUid(1);
                multi_info.setMcontent(etEdit.getText().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                multi_info.setMupload_time(sdf.format(System.currentTimeMillis()));
                OkHttpUtils.getInstance().postJson(PUBLISH_URL_PUBLISH, gson.toJson(multi_info), new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.e("....finish publish",""+response.body().string());
                        EventInfo eventInfo = new EventInfo();
                        eventInfo.setContentString("finishPublishing");
                        EventBus.getDefault().post(eventInfo);
                    }
                });
            }
        });

        initProgressbar();
        if(type != 2 && needToUpload && !isUploadComplete){
            startUploadProgress();
        }


    }

    /*
        TODO    启动上传流程  （1）获取token
     */
    private void startUploadProgress() {

        /*
            TODO    获取token
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid",1);
            jsonObject.put("uid",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpUtils.getInstance().postJson(PUBLISH_URL_TOKEN, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(PublishActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String token = response.body().string();
                startRealUpload(token);
            }
        });


    }

    private void startRealUpload(String token) {
        UploadUtils uploadUtils = new UploadUtils(token,path,new File(path).getName());
        Log.e("token",":"+token);
        Log.e("path",":"+path);
        Log.e("key",":"+new File(path).getName());
        EventInfo eventInfo = new EventInfo();
        eventInfo.setContentString("startUpload");
        EventBus.getDefault().post(eventInfo);
        uploadUtils.upload(new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if(info.isOK()){
                    //上传成功
                    Map<String,String> fileMap = new HashMap<>();
                    fileMap.put("url","http://"+QINIU_URL+"/"+uploadUtils.getKey());
                    eventInfo.setContentMap(fileMap);
                    eventInfo.setContentString("uploadFinish");
                    EventBus.getDefault().post(eventInfo);
                }else{
                    //上传失败，打印错误信息
                    Log.e("upload fail",""+info);
                    eventInfo.setContentString("uploadError");
                    //停止上传就行了吧应该，如果上传失败了用户也会自己自己退出的
//                    EventBus.getDefault().post(eventInfo);
                }
            }
        }, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                eventInfo.setContentString("progressUpdate");
                Map<String,Double> progressMap = new HashMap<>();
                progressMap.put("progress",percent);
                eventInfo.setContentMap(progressMap);
                EventBus.getDefault().post(eventInfo);
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return isCanceled;
            }
        });


    }


    /*
        TODO    隐藏提示文字和进度条，用到的时候再说
     */
    public void initProgressbar(){
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setProgress(0);
        tvLog.setVisibility(View.INVISIBLE);
    }

    public void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
        tvLog.setVisibility(View.VISIBLE);
    }




    /*
        TODO    如果是草稿，（1）纯文本草稿，（2）已经上传完成的视频/图片草稿,（3）未上传完成的
                如果不是草稿
                    ******无论是不是草稿，checkDraft之后的multi_info都存在了******
     */
    public void checkDraft(Bundle bundle){

        type = (int) bundle.getSerializable("type");
        if(bundle.getSerializable("isdraft").equals("true")){
            Log.e("检查草稿","是草稿");
            multi_info = gson.fromJson((String) bundle.getSerializable("draftbody"),Multi_info.class);
            Log.e("草稿的multi_info",""+multi_info.toString());
            //检查是否需要上传
            if(type == 2){
                //文本，直接转multi_info
                multi_info = gson.fromJson((String) bundle.getSerializable("draftbody"),Multi_info.class);
            }else{
                //文件，如果未上传完成，需要path
                if(bundle.getSerializable("iscanceled").equals("true")){
                    needToUpload = true;
                    path = (String) bundle.getSerializable("canceled_file_path");
                }else{
                    needToUpload = false;
                    isUploadComplete = true;
                }
            }
            etEdit.setText(multi_info.getMcontent());
        }else{
            /*
                TODO ：   拿到uid  cid
            */
            Log.e("检查草稿","不是草稿");
            if(type != 2){
                path = (String) bundle.getSerializable("path");
                Log.e("检查草稿","设置当前path为:"+ path);
            }
            needToUpload = true;
            isUploadComplete = false;
            multi_info = new Multi_info();
        }
        Log.e("检查草稿","设置当前type为:"+type);
    }

    /*
        TODO    初始化button的状态
            （未上传完成或者文本形式并且et为空）
     */
    public void initButton(){
        if(type == 0 || type == 1){
            if(!isUploadComplete){
                buttonInactive();
            }else{
                buttonActive();
            }
        }
        if(type == 2){
            if(etEmpty){
                buttonInactive();
            }else{
                buttonActive();
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
                //使按钮变为不活跃状态
                buttonActive();
            }
        }
        if(type == 2 && etEmpty){
            buttonInactive();
        }
        if(type != 2 && !isUploadComplete){
            buttonInactive();
        }
        Log.e("whatch","sssss");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



    public void buttonActive(){
        btnPublish.setEnabled(true);
        btnPublish.setBackground(getResources().getDrawable(R.drawable.btn_selector));
        btnPublish.setTextColor(getResources().getColor(R.color.white));
    }

    public void buttonInactive(){
        btnPublish.setEnabled(false);
        btnPublish.setBackground(getResources().getDrawable(R.drawable.btn_invalid));
        btnPublish.setTextColor(getResources().getColor(R.color.btnInvalidText));
    }


    /*
        TODO
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //返回键的监听
            if(checkNeedSave()){
                //弹出是否保留此次编辑的popupwindow
                popupSaveWindow();

            }else{
                //弹出是否退出此次编辑的popupwindow

            }
        }



        return super.onKeyDown(keyCode, event);
    }


    /*
        TODO    除了文本形式的编辑内容为空时  true，询问是否退出此次编辑（取消---黑色 ，退出----红色）
                其他情况都返回false    保留此次编辑？ （不保留----黑色，保留---蓝色）
     */
    public boolean checkNeedSave(){
        if(type == 2){
            if(etEmpty){
                return false;
            }
        }
        return true;
    }


    public void popupSaveWindow(){

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        this.getWindow().setAttributes(lp);
        View view = getLayoutInflater().inflate(R.layout.publish_save_popupwindow,null);
        saveWindow = new PopupWindow(view, dip2px(this,350) , dip2px(this,150), true);
        saveWindow.setFocusable(true);
        saveWindow.setOutsideTouchable(false);
        saveWindow.setTouchable(true);
        saveWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });


        Button btnIgnore = view.findViewById(R.id.btn_publish_save_left);
        btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                        TODO    不保留，检查上传是否完成，如果未完成，取消上传
                 */

                SharedPreferences sp = getSharedPreferences(PUBLISH_SP_NAME,MODE_PRIVATE);
                if(!isUploadComplete && type != 2){
                    //取消上传
                    isCanceled = true;
                }

                if(type == 2){
                    sp.edit().putBoolean("have_text_draft",false).commit();
                }else{

                    sp.edit().putBoolean("have_draft",false).commit();
                }
                saveWindow.dismiss();
                finish();
            }
        });

        Button btnSave = view.findViewById(R.id.btn_publish_save_right);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(PUBLISH_SP_NAME,MODE_PRIVATE);
                if(type == 2){
                    //设置uid，cid，发布日期不设置，文字内容mcontent，type
                    multi_info.setCid(1);
                    multi_info.setUid(1);
                    multi_info.setMcontent(etEdit.getText().toString());
                    sp.edit().putBoolean("have_text_draft",true).putString("textdraftbody",gson.toJson(multi_info)).commit();
                }else{
                    /*
                        TODO：文件草稿，先判断是否上传完成
                     */
                    if(isUploadComplete && !needToUpload){
                        //上传已经完成，url已经存进去了
                        multi_info.setUid(1);
                        multi_info.setCid(1);
                        multi_info.setMcontent(etEdit.getText().toString());
                        sp.edit().putBoolean("have_draft",true)
                                .putBoolean("have_canceled_file",false)
                                .putString("draftbody",gson.toJson(multi_info))
                                .putInt("type",type).commit();
                    }else{
                        //上传未完成，先取消上传
                        isCanceled = true;
                        multi_info.setType(type);
                        multi_info.setUid(1);
                        multi_info.setCid(1);
                        multi_info.setMcontent(etEdit.getText().toString());
                        sp.edit().putBoolean("have_draft",true)
                                .putBoolean("have_canceled_file",true)
                                .putString("canceled_file_path",path)
                                .putInt("type",type)
                                .putString("draftbody",gson.toJson(multi_info))
                                .commit();
                    }
                }
                saveWindow.dismiss();
                finish();
            }
        });
        saveWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,20);


    }

    //  将物理像素换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
