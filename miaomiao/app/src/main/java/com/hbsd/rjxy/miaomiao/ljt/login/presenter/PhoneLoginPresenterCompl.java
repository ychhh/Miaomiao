package com.hbsd.rjxy.miaomiao.ljt.login.presenter;

import com.hbsd.rjxy.miaomiao.ljt.login.view.IPhoneLoginView;
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

public class PhoneLoginPresenterCompl implements IPhoneLoginPresenter{

    IPhoneLoginView iPhoneLoginView;

    public PhoneLoginPresenterCompl(IPhoneLoginView iPhoneLoginView) {
        this.iPhoneLoginView = iPhoneLoginView;
    }

    @Override
    public void doLogin(String tel) {
        JSONObject object=new JSONObject();
        //发送JSON格式的字符串到服务器
        try {
            object.put("tel", EncodeUtil.encodeToString(tel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Callback callback=new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(content);
                    String result=jsonObject.getString("result");//1.密码正确(附带返回用户id)
                    if (result.equals("true")){
                        iPhoneLoginView.onLoginResult(result,jsonObject.getInt("uid"),jsonObject.getString("hasPasswod"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        OkHttpUtils.getInstance().postJson(Constant.LOGIN_URL+"phone",object.toString(),callback);

    }

}
