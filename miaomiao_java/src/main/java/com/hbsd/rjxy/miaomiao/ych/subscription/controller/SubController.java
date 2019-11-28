package com.hbsd.rjxy.miaomiao.ych.subscription.controller;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.ych.subscription.service.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@RequestMapping("/sub")
@Controller
public class SubController {
    @Autowired
    private SubService subService;
    Gson gson = new Gson();
    @RequestMapping("/all")
    @ResponseBody
    public String find(){
        return gson.toJson(subService.findall());
    }
    @RequestMapping("/findbyuid")
    @ResponseBody
    public String update(int uid){
        return subService.findOneByUid(uid).toString();
    }
}
