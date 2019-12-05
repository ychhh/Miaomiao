package com.hbsd.rjxy.miaomiao.zlc.utils;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RequestUtil {


    /**
     * 拿到request当中的json字符串
      * @param request
     * @return
     */
    public static String getJson(HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len = is.read(buffer)) != -1){
                bos.write(buffer,0,len);
            }
            String str = new String(bos.toByteArray());
            bos.close();
            return str;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
