package com.hbsd.rjxy.miaomiao.zlc.video.controller;

import com.google.gson.Gson;
import com.hbsd.rjxy.miaomiao.entity.Comment;
import com.hbsd.rjxy.miaomiao.entity.Multi_info;
import com.hbsd.rjxy.miaomiao.zlc.utils.RequestUtil;
import com.hbsd.rjxy.miaomiao.zlc.video.service.CommentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    Gson gson = new Gson();


    /**
     * 查询本视频所有的评论
     * 基本用不到
     * 需要传一个Multi_info的Json对象
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/comment/findCommentsByMiid")
    @ResponseBody
    public String findCommentsByMiid(HttpServletRequest request, HttpServletResponse response){
        Multi_info multi_info = gson.fromJson(RequestUtil.getJson(request),Multi_info.class);
        return gson.toJson(commentService.findCommentsByMiid(multi_info));
    }


    /**
     * 传一个JSONObject 包含两个key    miid 和 page
     * 返回值如果是fail代表返回失败
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/comment/findCommentsByMiidAndPage")
    @ResponseBody
    public String findCommentsByMiidAndPage(HttpServletRequest request, HttpServletResponse response){
        try {
            JSONObject receive = new JSONObject(RequestUtil.getJson(request));
            return gson.toJson(commentService.findCommentsByMiidAndPage(receive.getInt("miid"),receive.getInt("page")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "fail";
    }
    /**
     * 添加评论，传一个comment对象
     *
     * 成功返回success，失败返回fail
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/comment/addComment")
    @ResponseBody
    public String addComment(HttpServletRequest request, HttpServletResponse response){
        Comment comment = gson.fromJson(RequestUtil.getJson(request), Comment.class);
        if(commentService.addComment(comment) == 1){
            return "success";
        }
        return "fail";
    }


    /**
     * 删除一条评论   传一个comment对象
     * 成功返回success，失败返回fail
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/comment/deleteCommentByCoid")
    @ResponseBody
    public String deleteCommentByCoid(HttpServletRequest request, HttpServletResponse response){
        Comment comment = gson.fromJson(RequestUtil.getJson(request), Comment.class);
        if(commentService.deleteCommentByCoid(comment) == 1){
            return "success";
        }
        return "fail";
    }



}
