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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 密码登录
     * @param request
     * @param response
     */
    @RequestMapping("/password")
    @ResponseBody
    public void loginUser(HttpServletRequest request, HttpServletResponse response){
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
                    //user 为空，表示未注册， 提示用户进行验证码登录
                    res.put("result","false");
                }else {
                    if (user.getPwd()!=null){//用户已经设置密码
                        if (user.getPwd().equals(password)){//密码正确
                            res.put("result","true");
                            res.put("uid",user.getUid());
                        }else {
                            res.put("result","error");
                        }
                    }else{//用户未设置密码，提示用户手机验证码登录，可登录后设置密码
                        res.put("result","null");
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


    /**
     * 手机验证码方式登录
     * @param request
     * @param response
     */
    @RequestMapping("/phone")
    @ResponseBody
    public void loginUserForPhone(HttpServletRequest request, HttpServletResponse response){
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
                User user=loginService.findUserByTel(phone);
                if (user==null){
                    //user 为空，表示未注册， 进行注册
                    Date date=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String temp=sdf.format(date);
                    Date time=null;
                    try {
                        time = sdf.parse(temp);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    loginService.saveUser(phone,time);
                    User userSave=loginService.findUserByTel(phone);
                    res.put("result","true");
                    res.put("uid",userSave.getUid());
                }else {
                    res.put("result","true");
                    res.put("uid",user.getUid());
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
}
