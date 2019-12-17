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


    /**
     * 根据cid查询所有multi，时间倒序
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/video/getAll")
    @ResponseBody
    public String getAll(HttpServletRequest request, HttpServletResponse response){
        JSONObject receive = null;
        try {
            receive = new JSONObject(RequestUtil.getJson(request));
            return gson.toJson(videoService.getAllByCid(receive.getInt("cid")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "fail";
    }




}
