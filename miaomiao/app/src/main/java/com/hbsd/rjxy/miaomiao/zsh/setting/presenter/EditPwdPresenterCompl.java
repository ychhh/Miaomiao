package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import android.util.Log;

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

public class EditPwdPresenterCompl implements EditPwdPresenter {


    @Override
    public void editPwdWithoutOld(Integer id,String newPwd) {
        /*TODO
            直接将id,新的密码发送到服务器
        */
        OkHttpUtils utils=new OkHttpUtils();
        String url= Constant.GET_USER_URL+"editPwd";
        JSONObject obj=new JSONObject();

        try {
            obj.put("uid",id);
            String edPwd=EncodeUtil.encodeToString(newPwd);
            obj.put("newPwd",edPwd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str=obj.toString();

        utils.postJson(url, str, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("修改密码","失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("修改密码","成功");
            }
        });

    }

    @Override
    public void editPwdWithOld(Integer id,String oldPwd, String newPwd) {
        /*TODO
            将id，旧密码，新密码发送到服务器
            先进行id查询，再进行旧密码匹配，最后修改为新密码
        */

    }
}
