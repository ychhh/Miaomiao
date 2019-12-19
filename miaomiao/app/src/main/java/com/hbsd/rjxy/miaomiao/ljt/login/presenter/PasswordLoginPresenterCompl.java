package com.hbsd.rjxy.miaomiao.ljt.login.presenter;

import com.hbsd.rjxy.miaomiao.ljt.login.view.IPasswordLoginView;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.utils.EncodeUtil;
import com.hbsd.rjxy.miaomiao.utils.OkHttpUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PasswordLoginPresenterCompl implements IPasswordLoginPresenter{

    IPasswordLoginView iPasswordLoginView;

    public PasswordLoginPresenterCompl(IPasswordLoginView iPasswordLoginView) {
        this.iPasswordLoginView = iPasswordLoginView;
    }

    @Override
    public void doLogin(String tel, String pwd) {
        JSONObject object=new JSONObject();
        try {
            //发送JSON格式的字符串到服务器(Base64加密)
            object.put("tel", EncodeUtil.encodeToString(tel));
            object.put("pwd",EncodeUtil.encodeToString(pwd));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Callback callback=new Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(content);
                    String result=jsonObject.getString("result");
                    iPasswordLoginView.onLoginResult(result,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        };
        OkHttpUtils.getInstance().postJson(Constant.LOGIN_URL+"password",object.toString(),callback);
    }
}
