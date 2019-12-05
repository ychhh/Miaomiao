package com.hbsd.rjxy.miaomiao.ljt.Login.test;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.Login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/test/login")
public class Test {
    @Autowired
    private LoginService loginService;
    @RequestMapping("/password")
    @ResponseBody
    public void save(){
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String temp=sdf.format(date);
        Date time=null;
        try {
            time = sdf.parse(temp);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        loginService.saveUser("13653323743",time);
    }

    @RequestMapping("/findById")
    @ResponseBody
    public User findUserByuid(){
        return loginService.findUserById(1);
    }
}
