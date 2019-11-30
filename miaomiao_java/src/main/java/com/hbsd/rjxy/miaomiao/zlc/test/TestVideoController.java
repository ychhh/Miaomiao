package com.hbsd.rjxy.miaomiao.zlc.test;


import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.zlc.video.video.VideoService;
import com.sun.deploy.net.HttpResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
public class TestVideoController {

    @Autowired
    VideoService videoService;

    Gson gson = new Gson();


    @RequestMapping("/testVideo")
    @ResponseBody
    public String findAll(){
        return gson.toJson(videoService.findAll());
    }


    @RequestMapping("/addHot")
    @ResponseBody
    public int findAll(HttpServletRequest request, HttpServletResponse response){
        int miid = Integer.parseInt(request.getParameter("miid"));
        return videoService.addHotByFish(miid);
    }

    @RequestMapping("/pagingVideo")
    @ResponseBody
    public String findVideoByPaging(HttpServletRequest request, HttpServletResponse response){
        int page = Integer.parseInt(request.getParameter("page"));
        return gson.toJson(videoService.findVideoPaging(page));
    }





}
