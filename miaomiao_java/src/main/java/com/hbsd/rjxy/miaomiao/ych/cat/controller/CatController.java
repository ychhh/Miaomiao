package com.hbsd.rjxy.miaomiao.ych.cat.controller;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.ych.cat.service.CatService;
import com.hbsd.rjxy.miaomiao.ych.subscription.service.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RequestMapping("/cat")
@Controller
public class CatController {
    @Autowired
    private CatService catService;
    Gson gson = new Gson();
    @RequestMapping("/all")
    @ResponseBody
    public String find(){
        return gson.toJson(catService.findAll());
    }
    @RequestMapping("/findbyuid")
    @ResponseBody
    public String getSubByUid(int uid){
        return gson.toJson(catService.findAllByUid(uid));
    }
    @RequestMapping("/findbycid")
    @ResponseBody
    public String getSubByCid(int cid){
        return gson.toJson(catService.findAllByCid(cid));
    }
    @RequestMapping("/setcathead")
    @ResponseBody
    public String setCatHead(String url){
        Cat cat=new Cat();
        cat.setHpath(url);
        System.out.println(cat.toString());
        return gson.toJson(catService.saveCat(cat));
    }
    @RequestMapping("/setcatinfo")
    @ResponseBody
    public String setCatInfo(String str){
        System.out.println(str);
        Cat cat= gson.fromJson(str,Cat.class);
        System.out.println(gson.toJson(cat));
        return gson.toJson(catService.saveCat(cat));
    }
}
