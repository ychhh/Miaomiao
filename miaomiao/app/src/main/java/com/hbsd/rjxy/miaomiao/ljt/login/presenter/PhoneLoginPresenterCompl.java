package com.hbsd.rjxy.miaomiao.ljt.login.presenter;

import android.os.AsyncTask;

import com.hbsd.rjxy.miaomiao.ljt.login.view.IPhoneLoginView;
import com.hbsd.rjxy.miaomiao.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PhoneLoginPresenterCompl implements IPhoneLoginPresenter{

    IPhoneLoginView iPhoneLoginView;

    public PhoneLoginPresenterCompl(IPhoneLoginView iPhoneLoginView) {
        this.iPhoneLoginView = iPhoneLoginView;
    }

    @Override
    public void doLogin(String tel) {
        LoginTask task=new LoginTask();
        task.execute(new Object[]{tel});
    }

    private class LoginTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //创建URL对象
            try {
                URL url = new URL(Constant.LOGIN_URL+"phone");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                OutputStream outputStream = connection.getOutputStream();

                //发送JSON格式的字符串到服务器
                JSONObject object=new JSONObject();
                object.put("tel",objects[0]);
                outputStream.write(object.toString().getBytes());
                outputStream.close();

                InputStream is=connection.getInputStream();
                byte []buffer=new byte[255];
                int len=is.read(buffer);
                String content=new String(buffer,0,len);
                is.close();
                return content;
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
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String content=o.toString();
            JSONObject response= null;
            try {
                response = new JSONObject(content);
                String result=response.getString("result");//1.密码正确(附带返回用户id)
                if (result.equals("true")){
                    iPhoneLoginView.onLoginResult(result,response.getInt("uid"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
