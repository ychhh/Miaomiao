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
    public String getSubByUid(int uid){
        return gson.toJson(subService.findAllByUid(uid));
    }
    @RequestMapping("/findbycid")
    @ResponseBody
    public String getSubByCid(int cid){
        return gson.toJson(subService.findAllByCid(cid));
    }
    @RequestMapping("/findbyuidandcid")
    @ResponseBody
    public String getSubByUidAndCid(int uid,int cid){
        return gson.toJson(subService.findOneByUidAndCid(uid,cid));
    }
    @RequestMapping("/follow")
    @ResponseBody
    public int follow(int uid,int cid){
        return subService.follow(uid,cid);
    }
    @RequestMapping("/unfollow")
    @ResponseBody
    public int unfollow(int uid,int cid){
        return subService.unfollow(uid,cid);
    }


}