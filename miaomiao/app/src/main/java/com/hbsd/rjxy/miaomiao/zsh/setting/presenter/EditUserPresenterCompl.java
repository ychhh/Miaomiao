package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.EditProfileView;

import org.greenrobot.eventbus.EventBus;
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

public class EditUserPresenterCompl implements EditUserPresenter {
    EditProfileView editProfileView;
    Callback callback;
    OkHttpUtils okHttpUtils;

    public EditUserPresenterCompl(EditProfileView editProfileView) {
        this.editProfileView = editProfileView;
    }

    @Override
    public boolean editUser(String jsonStr) {

        callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("任务", "失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("已经成功接收到修改之后的客户信息", "真好");
            }
        };
        String url = Constant.GET_USER_URL + "edit";
        okHttpUtils = new OkHttpUtils();
        okHttpUtils.postJson(url, jsonStr, callback);
        EventBus.getDefault().post(jsonStr);
        editProfileView.Okfinish();
        return true;
    }


}
