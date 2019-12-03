package com.hbsd.rjxy.miaomiao.ljt.login.presenter;

import android.os.AsyncTask;

import com.hbsd.rjxy.miaomiao.ljt.login.view.IPasswordLoginView;
import com.hbsd.rjxy.miaomiao.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PasswordLoginPresenterCompl implements IPasswordLoginPresenter{

    IPasswordLoginView iPasswordLoginView;

    public PasswordLoginPresenterCompl(IPasswordLoginView iPasswordLoginView) {
        this.iPasswordLoginView = iPasswordLoginView;
    }

    @Override
    public void doLogin(String tel, String pwd) {
        LoginTask task=new LoginTask();
        task.execute(new Object[]{tel,pwd});
    }

    private class LoginTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            //创建URL对象
            try {
                URL url = new URL(Constant.LOGIN_URL+"password");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                OutputStream outputStream = connection.getOutputStream();

                //发送JSON格式的字符串到服务器
                JSONObject object=new JSONObject();
                object.put("tel",objects[0]);
                object.put("pwd",objects[1]);
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
            if(o!=null){
                String content=o.toString();
                JSONObject response= null;
                try {
                    response = new JSONObject(content);
                    String result=response.getString("result");//1.密码正确(附带返回用户id)2.密码错误3.手机号未注册
                    if (result.equals("true")){
                        iPasswordLoginView.onLoginResult(result,response.getInt("uid"));
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
        }
    }
}
