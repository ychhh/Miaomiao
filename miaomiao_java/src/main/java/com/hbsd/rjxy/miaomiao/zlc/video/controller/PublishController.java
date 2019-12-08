package com.hbsd.rjxy.miaomiao.zlc.video.controller;


import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.zlc.utils.RequestUtil;
import com.hbsd.rjxy.miaomiao.zlc.video.service.VideoService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PublishController {

    @Autowired
    VideoService videoService;
    Gson gson = new Gson();


    @RequestMapping("/publish/getToken")
    @ResponseBody
    public String getToken(HttpServletRequest request, HttpServletResponse response){
        //验证验证猫是否存在并且属于这个人
        try {
            JSONObject jsonObject = new JSONObject(RequestUtil.getJson(request));
            //TODO 调catService的方法，根据cid查询uid是否合理

            System.out.println(jsonObject.get("cid")+","+jsonObject.get("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return videoService.getToken();
    }




}
