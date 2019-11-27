package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.entity.Muti_infor;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.MeBufferReader;
import com.hbsd.rjxy.miaomiao.zlc.vedio.presenter.IVideoPreseter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.hbsd.rjxy.miaomiao.utils.Constant.INIT_VIDEO_URL;


/**
 * 初始化视频信息的异步任务类
 */
public class VideoPreseter extends AsyncTask<Object,Object,String>{

    private Context context;
    private User user;

    public VideoPreseter(Context context, @Nullable User user) {
        this.context = context;
        this.user = user;
    }


    @Override
    protected String doInBackground(Object... objects) {
        try {
            URL url = new URL(INIT_VIDEO_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            if(user != null){
                OutputStream os = con.getOutputStream();
                //如果已经登录，将userId传出去
                Gson gson = new Gson();
                os.write(gson.toJson(user).getBytes());
            }

            InputStream is = con.getInputStream();
            byte[] buffer = MeBufferReader.readInputStream(is);
            return new String(buffer);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Gson gson = new Gson();
        List<Muti_infor> videoList = gson.fromJson(s,new TypeToken<List<Muti_infor>>(){}.getType());
        if(videoList != null){
            EventBus.getDefault().post(videoList);
        }else{
            //没有拿到返回结果
        }
    }
}
