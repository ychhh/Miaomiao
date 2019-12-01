package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.MeBufferReader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hbsd.rjxy.miaomiao.utils.Constant.INIT_VIDEO_URL;
import static com.hbsd.rjxy.miaomiao.utils.Constant.RECOMMEND_PAGE_DEFAULT;


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
            OutputStream os = con.getOutputStream();
            if(user != null){
                //如果已经登录，将userId传出去
                Gson gson = new Gson();
                os.write(gson.toJson(user).getBytes());
            }else{
                JSONObject jo = new JSONObject();
                jo.put("page",RECOMMEND_PAGE_DEFAULT);
                os.write(jo.toString().getBytes());
            }

            InputStream is = con.getInputStream();
            byte[] buffer = MeBufferReader.readInputStream(is);
            return new String(buffer);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Gson gson = new Gson();
        List<Multi_info> videoList = gson.fromJson(s,new TypeToken<List<Multi_info>>(){}.getType());
        EventInfo<String,String,Multi_info> videoEvent = new EventInfo<>();
        if(videoList != null){
            //如果拿到了视频数据，则放到eventinfo的list中去
            videoEvent.setContentList(videoList);
            EventBus.getDefault().post(videoEvent);
            Log.e("videoList",""+videoList.toString());
        }else{
            //没有拿到设置eventinfo为无效
            videoEvent.setAvailable(false);
            Map<String,String> map = new HashMap<>();
            map.put("status","complete");
            videoEvent.setContentMap(map);
            EventBus.getDefault().post(videoEvent);
        }
    }
}
