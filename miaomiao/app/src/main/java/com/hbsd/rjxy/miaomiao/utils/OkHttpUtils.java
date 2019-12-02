package com.hbsd.rjxy.miaomiao.utils;

import android.os.Looper;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 *
 * OkHttp单例模式
 *
 * Json，表单，表单-文件，get四种方式
 *
 */
public class OkHttpUtils {

    private static OkHttpUtils mOkHttpUtils;
    private OkHttpClient mOkHttpClient;

    //Json请求
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //表单-文件请求
    private static final MediaType FILE = MediaType.parse("application/octet-stream");

    //超时时间
    public static final int TIMEOUT = 1000*60;

    private OkHttpUtils(){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    public static OkHttpUtils getInstance(){
        if(mOkHttpUtils == null){
            synchronized (OkHttpUtils.class){
                if(mOkHttpUtils == null){
                    return mOkHttpUtils = new OkHttpUtils();
                }
            }
        }
        return mOkHttpUtils;
    }


    //json方式
    public void postJson(String url, String json, Callback callback){
//        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody jsonBody = RequestBody.Companion.create(json,JSON);
        Request request = new Request.Builder().url(url).post(jsonBody).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }



    //表单方式
     public void postForm(String url, Map<String,String> map, Callback callback){
         MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
         for(String key : map.keySet()){
             builder.addFormDataPart(key,map.get(key));
         }
         RequestBody requestBody = builder.build();
         Request request = new Request.Builder()
                 .url(url)
                 .post(requestBody)
                 .build();
         Call call = mOkHttpClient.newCall(request);
         call.enqueue(callback);
     }



    //表单，文件方式
    public void postFormWithFile(String url, Map<String,String> map,List<LocalMedia> files, Callback callback){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(String key : map.keySet()){
            builder.addFormDataPart(key,map.get(key));
        }

        for(LocalMedia localMedia : files){
            RequestBody fileBody = RequestBody.Companion.create(new File(localMedia.getPath()),FILE);
            if(localMedia.getPictureType().equals("image/jpeg")){
                builder.addFormDataPart("type","jpeg");
            }else if(localMedia.getPictureType().equals("video/mp4")){
                builder.addFormDataPart("type","mp4");
            }else{
                return;
            }
            String[] args = localMedia.getPath().split("/");
            builder.addFormDataPart("file",""+args[args.length-1],fileBody);
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }


    //get方式
    public void get(String url,Callback callback){
        Request request = new Request.Builder().url(url).get().build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
