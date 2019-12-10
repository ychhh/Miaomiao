package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class GetUserPresenterCompl implements GetUserPresenter {
    User user;
    Activity activity;
    public GetUserPresenterCompl(Activity activity){
        this.activity=activity;
    }
    @Override
    public User getUser(Integer uid) {
         GetUserTask task=new GetUserTask();
         task.execute(new Object[]{uid});


        return null;
    }
   public class GetUserTask extends AsyncTask {


       /**
        * Override this method to perform a computation on a background thread. The
        * specified parameters are the parameters passed to {@link #execute}
        * by the caller of this task.
        * <p>
        * This method can call {@link #publishProgress} to publish updates
        * on the UI thread.
        *
        * @param objects The parameters of the task.
        * @return A result, defined by the subclass of this task.
        * @see #onPreExecute()
        * @see #onPostExecute
        * @see #publishProgress
        */
       @Override
       protected Object doInBackground(Object[] objects) {
           try {
               URL url=new URL(Constant.GET_USER_URL+"find");
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               connection.setRequestMethod("POST");
               OutputStream outputStream = connection.getOutputStream();

               //发送JSON格式的字符串到服务器
               JSONObject object=new JSONObject();
               object.put("uid",objects[0]);
               outputStream.write(object.toString().getBytes());
               outputStream.close();
               //接收当前用户的信息
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
           if (o != null) {
               String content = o.toString();
               JSONObject response = null;

               try {

                   response = new JSONObject(content);
                   user= new User();
                   Integer id=response.getInt("uid");
                   String name=response.getString("uName");
                   String intro = response.getString("uIntro");
                   String sex = response.getString("uSex");
                   TextView tx_intro=activity.findViewById(R.id.self_sbp);
                   user.setUserName(name);
                   user.setUserIntro(intro);
                   user.setUserSex(sex);
                   user.setUserId(id);



                   /* 修改ui内容*/
                  // TextView tx_name=activity.findViewById(R.id.self_name);



                   tx_intro.setText(intro);





               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }
    }
}
