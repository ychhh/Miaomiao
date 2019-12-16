package com.hbsd.rjxy.miaomiao.zlc.video.controller;


import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.ych.cat.service.CatService;
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
public class VideoController {

    @Autowired
    VideoService videoService;
    @Autowired
    CatService catService;

    Gson gson = new Gson();


    @RequestMapping("/video/findAllVideo")
    @ResponseBody
    public String findAll(){
        return gson.toJson(videoService.findAll());
    }


    /**
     * 传参miid
     * 返回值如果是0代表异常
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/video/addHot")
    @ResponseBody
    public int findAll(HttpServletRequest request, HttpServletResponse response){
        JSONObject receive = null;
        try {
            receive = new JSONObject(RequestUtil.getJson(request));
            return videoService.addHotByFish(receive.getInt("miid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
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


    @RequestMapping("/video/pagingVideoByUid")
    @ResponseBody
    public String findVideoByUid(HttpServletRequest request, HttpServletResponse response){
        JSONObject receive = null;
        try {
            receive = new JSONObject(RequestUtil.getJson(request));
            return gson.toJson(videoService.findVideoPagingByUid(receive.getInt("page"),receive.getInt("uid")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "fail";

    }


    @RequestMapping("/video/getCat")
    @ResponseBody
    public String getCat(HttpServletRequest request, HttpServletResponse response){
        JSONObject re = null;
        try {
            re = new JSONObject(RequestUtil.getJson(request));
            return gson.toJson(catService.findAllByCid(re.getInt("cid")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "fail";
    }





}
