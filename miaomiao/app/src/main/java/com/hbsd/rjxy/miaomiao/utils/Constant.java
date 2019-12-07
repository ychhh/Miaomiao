package com.hbsd.rjxy.miaomiao.utils;

import android.Manifest;
import android.content.Intent;

/**
 *
 * 常量类
 * 要多写注释
 *
 */
public class Constant {


    public static String[] TAB_STRING = {"首页","圈子","我的猫","直播","我的"}; //tabhost中的名词

    public static String INIT_VIDEO_URL = "http://10.7.87.224:8080/video/pagingVideo";//请求视频的url


    public static String ADD_HOT_URl = "http://10.7.87.224:8080/video/addHot";//点击小鱼干发送的Url


    public static String LOGIN_URL="http://10.7.88.158:8080/login/";//登录的URL

    public static int RECOMMEND_PAGE_DEFAULT = 1;   //推荐视频的当前页

    public static final String QINIU_URL = "q20jftoug.bkt.clouddn.com";   //七牛服务器地址

    //调用图片选择器必须要的权限
    public static final String[] PERMISSION_NECESSARY = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

    public static final int PICTURESELECT_VIDEO = 121;
    public static final int PICTURESELECT_CAMERA = 122;

    public static final String PUBLISH_SP_NAME = "PUBLISH_PREFERENCE";  //草稿，以及其他和用户发布相关的sharepreference

    /*
            TODO    是否提醒过用户请减少使用纯文本发布功能的提示
            第一次登录时改为 NEEDREMIND
            之后是 DONTREMIND
     */
    public static final String REMIND_PUBLISH_ONCE = "REMIND_PUBLISH_ONCE";

    public static final String PUBLISH_URL_TOKEN = "http://10.7.87.224:8080/publish/getToken";//发布获取token




}
