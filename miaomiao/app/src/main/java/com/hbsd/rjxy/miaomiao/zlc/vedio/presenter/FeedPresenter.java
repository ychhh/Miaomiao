package com.hbsd.rjxy.miaomiao.zlc.vedio.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbsd.rjxy.miaomiao.entity.EventInfo;
import com.hbsd.rjxy.miaomiao.entity.MultiInfor;
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

import static com.hbsd.rjxy.miaomiao.utils.Constant.ADD_HOT_URl;
import static com.hbsd.rjxy.miaomiao.utils.Constant.INIT_VIDEO_URL;
import static com.hbsd.rjxy.miaomiao.utils.Constant.RECOMMEND_PAGE_DEFAULT;

public class FeedPresenter extends AsyncTask<Object,Object,String>{



    private Context context;
    private MultiInfor multi_info;

    public FeedPresenter(Context context, @NonNull MultiInfor multi_info) {
        this.context = context;
        this.multi_info = multi_info;
    }


    @Override
    protected String doInBackground(Object... objects) {
        try {
            URL url = new URL(ADD_HOT_URl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            OutputStream os = con.getOutputStream();
            JSONObject jo = new JSONObject();
            jo.put("miid",multi_info.getId());
            os.write(jo.toString().getBytes());


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
        Log.e("小鱼干收到的返回信息",""+s);
    }
}
