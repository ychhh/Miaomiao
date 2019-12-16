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

        // todo 添加username，hpath
        Callback callback=new Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(content);
                    String result=jsonObject.getString("result");//1.密码正确(附带返回用户id)2.密码错误3.手机号未注册
                    if (result.equals("true")){
                        iPasswordLoginView.onLoginResult(result,jsonObject.getInt("uid"));
                    }else if(result.equals("error")){
                        iPasswordLoginView.onLoginResult(result,0);
                    }else if (result.equals("false")){
                        iPasswordLoginView.onLoginResult(result,0);
                    }else if(result.equals("null")){
                        iPasswordLoginView.onLoginResult(result,0);
                    }
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
