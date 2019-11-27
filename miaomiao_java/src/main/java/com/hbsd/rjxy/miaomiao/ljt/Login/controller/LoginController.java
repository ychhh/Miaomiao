package com.hbsd.rjxy.miaomiao.ljt.Login.controller;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.Login.service.LoginService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping("/password")
    @ResponseBody
    public void loginUser(HttpServletRequest request, HttpServletResponse response){
        System.out.println("/password=======================");
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
                String phone = object.getString("tel");
                String password=object.getString("pwd");
                System.out.println(phone+"====="+password);
                User user=loginService.findUserByTel(phone);
                if (user==null){
                    //user 为空，表示未注册， 提醒用户使用手机验证码方式注册
                    res.put("result","false");
                }else {
                    if (user.getPwd().equals(password)){
                        res.put("result","true");
                        res.put("uid",user.getUid());
                    }else{
                        res.put("result", "error");
                    }
                }
            }
            is.close();
            response.getWriter().append(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public User loginUser(@RequestParam("tel")String tel,@RequestParam("pwd")String pwd){
//        System.out.println(tel+"=================================="+pwd);
//        return null;
//    }
}
