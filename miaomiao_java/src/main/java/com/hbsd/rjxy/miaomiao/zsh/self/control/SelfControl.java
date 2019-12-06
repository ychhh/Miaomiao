package com.hbsd.rjxy.miaomiao.zsh.self.control;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zsh.self.service.SelfService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/self")
public class SelfControl {

    @Autowired
    private SelfService selfService;
    @RequestMapping("/updateProfile")
    @ResponseBody
    public void updateUser(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        InputStream is= null;
        String param=null;
        try {
            is = request.getInputStream();
            byte[]buffer=new byte[255];
            int len=is.read(buffer);
            param=new String(buffer,0,len);
            JSONObject object=new JSONObject(param);
            JSONObject res=new JSONObject();
            if(object.length()!=0){
                String newName=object.getString("newName");
                int uid=object.getInt("uid");
                System.out.println(newName+uid);





                }

            is.close();
            response.getWriter().append(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void testUpdate(String name,int uid){
        selfService.updateUserNameById(name,uid);
    }

}
