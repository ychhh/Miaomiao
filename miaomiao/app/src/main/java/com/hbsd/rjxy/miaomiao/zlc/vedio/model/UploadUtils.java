package com.hbsd.rjxy.miaomiao.zlc.vedio.model;


import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import org.json.JSONObject;
import java.io.File;
import java.util.UUID;

public class UploadUtils {
    /*

    使用方法：1.构造方法传三个参数
                1.token     从服务器端获取，接口自己写，调用VideoService.getToken()
                2.dataPath  需要上传的文件的路径
                3.key       文件的完整文件名
l
               (简单上传)
             2.调用upload()方法     取消上传，调用setCancelled()方法

             （有额外需求的如获取 上传进度，上传完成，取消上传）     这里取消上传怎么用请看这个类的UpCancellationSignal如何做的就行了
             3.调用upload(UpCompletionHandler ch,UpProgressHandler ph,UpCancellationSignal cs)方法


     */


    private String token;   //从我服务器获取的token
    private File data;      //上传文件
    private String dataPath; //上传路径      二选一
    private String key;     //  文件名
    private Configuration config;
    private Recorder recorder;      //分片上传的上传片记录器
    private UploadManager uploadManager;
    private UpCompletionHandler completionHandler;      //完成回调
    private UpProgressHandler progressHandler;          //进度回调
    private UpCancellationSignal cancellationSignal;    //取消上传回调
    private UploadOptions options;
    private boolean isCancelled = false;


    /**
     *
     * @param token     videoService.getToken()自己写自己的token获取接口，服务器端的service方法是videoService.getToken()
     * @param dataPath  文件路径        其实也可以传一个文件，但我还没写
     * @param key       文件名（带后缀）
     */
    UploadUtils(String token,String dataPath,String key){
        this.token = token;
        this.dataPath = dataPath;
        //生成在七牛服务器的文件名
        int dotPos = key.lastIndexOf(".");
        String fileExt = key.substring(dotPos + 1).toLowerCase();
        this.key = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        /*

        指定zone具体区域
        zon0 华东
        zon1 华北
        zon2 华南
        FixedZone.zon0

        AutoZone.autoZone 自动识别上传区域

        初始化配置
         */

        config = new Configuration.Builder()
                .connectTimeout(10)
                .useHttps(false)
                .responseTimeout(60)
                .recorder(recorder)
                .zone(FixedZone.zone0)
                .build();
        uploadManager = new UploadManager(config);
        //初始化 三个回调，完成，
        initHandler();
    }




    /**
     * 不需要回调的上传调用这个方法
     */
    public void upload(){
        uploadManager.put(
                dataPath,
                key,
                token,
                completionHandler,
                options);
    }


    /**
     * 需要重写三个回调方法的调用这个方法
     *
     * @param completionHandler
     * @param progressHandler
     * @param cancellationSignal
     */
    public void upload(UpCompletionHandler completionHandler,UpProgressHandler progressHandler,UpCancellationSignal cancellationSignal){
        options = new UploadOptions(null,null,false,progressHandler,cancellationSignal);
        uploadManager.put(
                dataPath,
                key,
                token,
                completionHandler,
                options);
    }


    private void initHandler(){

        //完成回调
        completionHandler = new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if(info.isOK()){
                    Log.e("qiniu","upload success");
                    Log.e("qiniu","文件地址:"+key);
                }else{
                    //上传服务器
                    Log.e("qiniu","upload fail");
                    Log.e("qiniu","info:"+info);
                }
            }
        };



        //进度回调
        progressHandler = new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                Log.e("qiniu",key+":"+percent);
            }
        };

        //取消上传回调
        cancellationSignal = new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return isCancelled;
            }
        };

        //option
        options = new UploadOptions(null,null,false,progressHandler,cancellationSignal);


    }

    public void setCancelled(){
        isCancelled = true;
    }



}
