package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.SelfMainView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class GetUserPresenterCompl implements GetUserPresenter {
    User user;
    SelfMainView selfMainView;
    OkHttpUtils okHttpUtils;
    String url;
    NewCallBack callBack;

    public GetUserPresenterCompl(SelfMainView selfMainView) {
        this.selfMainView = selfMainView;
    }

    @Override
    public User getUser(Integer uid) {
        okHttpUtils = new OkHttpUtils();
        url = Constant.GET_USER_URL + "find";
        callBack = new NewCallBack();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            okHttpUtils.postJson(url, jsonObject.toString(), callBack);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public class NewCallBack implements okhttp3.Callback {

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

            Log.e("获取用户信息", "失败");
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            user = new User();
            String json = response.body().string();
            try {
                JSONObject obj = new JSONObject(json);
                Integer id = obj.getInt("uid");

                String name = obj.getString("uName");
                String intro = obj.getString("uIntro");
                String sex = obj.getString("uSex");
                String hpath = obj.getString("hpath");
                user.setUserName(name);
                user.setUserIntro(intro);
                user.setUserSex(sex);
                user.setUserId(id);
                user.sethPath(hpath);
                selfMainView.initUserView(user);
                //Log.e("当前用户为：",user.getUserName()+user.getUserSex()+user.getUserIntro());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    /*使用OKHttp获取用户信息*/


}

