package com.hbsd.rjxy.miaomiao.zlc.video.controller;


import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.zlc.utils.RequestUtil;
import com.hbsd.rjxy.miaomiao.zlc.video.video.VideoService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class VideoController {

    @Autowired
    VideoService videoService;

    Gson gson = new Gson();


    @RequestMapping("/video/findAllVideo")
    @ResponseBody
    public String findAll(){
        return gson.toJson(videoService.findAll());
    }


    /**
     * 传参miid
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/video/addHot")
    @ResponseBody
    public int findAll(HttpServletRequest request, HttpServletResponse response){
        int miid = Integer.parseInt(request.getParameter("miid"));
        return videoService.addHotByFish(miid);
    }


    /**
     * 传参page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/video/pagingVideo")
    @ResponseBody
    public String findVideoByPaging(HttpServletRequest request, HttpServletResponse response){
        JSONObject receive = null;
        try {
            receive = new JSONObject(RequestUtil.getJson(request));
            return gson.toJson(videoService.findVideoPaging(receive.getInt("page")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "fail";
    }




}
