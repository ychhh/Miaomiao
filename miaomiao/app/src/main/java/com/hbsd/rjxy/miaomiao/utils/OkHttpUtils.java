package com.hbsd.rjxy.miaomiao.utils;

import android.os.Looper;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {

    private static OkHttpUtils mOkHttpUtils;
    private OkHttpClient mOkHttpClient;

    //Json请求
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");




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




}
