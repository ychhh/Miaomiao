package com.hbsd.rjxy.miaomiao.utils;

import android.Manifest;

/**
 *
 * 常量类
 * 要多写注释
 *
 */
public class Constant {



    public static String[] TAB_STRING = {"首页","圈子","我的猫","直播","我的"}; //tabhost中的名词

    public static String LOGIN_URL="http://47.94.171.160:8081/login/";//登录的URL

    public static String UPLOAD_USERHEAD_TOKEN_URL="http://47.94.171.160:8081/Self/getToken";//上传用户头像获取token

    public static String GET_USER_URL="http://47.94.171.160:8081/Self/";//操作当前登录的用户信息
    //http://10.7.88.102:8080/Self/
    //http://47.94.171.160:8081/Self/
    public static int RECOMMEND_PAGE_DEFAULT = 1;   //推荐视频的当前页
    public static int SUBSCRIBE_PAGE_DEFAULT = 1;   //推荐视频的当前页


    public static final String QINIU_URL = "q20jftoug.bkt.clouddn.com";   //七牛服务器地址

    //调用图片选择器必须要的权限
    public static final String[] PERMISSION_NECESSARY = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

    public static final int PICTURESELECT_VIDEO = 121;
    public static final int PICTURESELECT_CAMERA = 122;

    public static final String PUBLISH_SP_NAME = "PUBLISH_PREFERENCE";  //草稿，以及其他和用户发布相关的sharepreference
    public static final String LOGIN_SP_NAME="loginInfo";//与登录有关的sp,存储用户id（uid），是否有密码（hasPassword）
    public static final String HAS_PASSWORD="hasPassword";// 登录后用户是否有密码，true：有，false：无
    public static final String LOGIN_USERNAME="username";//登录后用户的用户名
    public static final String LOGIN_HEADPATH="userHeadPath";//登录后用户的头像地址无地址为"null"

    /*
            TODO    是否提醒过用户请减少使用纯文本发布功能的提示
            第一次登录时改为 NEEDREMIND
            之后是 DONTREMIND
     */
    public static final String REMIND_PUBLISH_ONCE = "REMIND_PUBLISH_ONCE";

    //
    public static final String INIT_VIDEO_URL = "http://10.7.87.224:8081/video/pagingVideo";//请求视频的url
    public static final String ADD_HOT_URl = "http://10.7.87.224:8081/video/addHot";//点击小鱼干发送的Url
    public static final String PUBLISH_URL_TOKEN = "http://10.7.87.224:8081/publish/getToken";//发布获取token
    public static final String PUBLISH_URL_PUBLISH = "http://10.7.87.224:8081/publish/publish";//发布

    public static final String FIND_USER_CAT = "http://10.7.87.224:8081/cat/findbyuid";//通过uid寻找猫
    public static final String FIND_SUB_CAT = "http://10.7.87.224:8081/sub/findsubcatbyuid";//通过uid寻找订阅的猫
    public static final String UNFOLLOW = "http://10.7.87.224:8081/sub/unfollow";
    public static final String CAT_HEAD_TOKEN="http://10.7.87.224:8081/cat/gettoken";
    public static final String PUBLISH_URL_COMMENT = "http://10.7.87.224:8081/comment/addComment";//发布评论
    public static final String URL_FINDCOMMENTPAGING = "http://10.7.87.224:8081/comment/findCommentsByMiidAndPage";//分页查询当前视频的评论
    public static final String URL_GET_TIME = "http://10.7.87.224:8081/comment/getTime";//获取服务器时间
    public static final String URL_LIKE_COMMENT = "http://10.7.87.224:8081/comment/like";   //点赞评论
    public static final String URL_DISLIKE_COMMENT = "http://10.7.87.224:8081/comment/dislike";//取消点赞评论
    public static final String URL_GET_RECORD = "http://10.7.87.224:8081/comment/getRecord";//获取评论记录
    public static final String URL_GET_HEADANDNAME = "http://10.7.87.224:8081/comment/getHeadAndName";//获取用户头像和昵称
    public static final String URL_GET_SUBSCRIPTION_LIST = "http://10.7.87.224:8081/sub/findbyuid";//获取用户订阅列表
    public static final String URL_SUBSCRIBE_CAT = "http://10.7.87.224:8081/sub/follow";//订阅猫
    public static final String URL_UNFOLLOW_CAT = "http://10.7.87.224:8081/sub/unfollow";//取消订阅猫
    public static final String INIT_SUBSCRIBE_VIDEO_LIST = "http://10.7.87.224:8081/video/pagingVideoByUid";//订阅视频

    public static final String URL_YUN = "47.94.171.160";
    public static final String URL_ZZZ = "10.7.87.224";

    public static final String URL_GET_CAT = "http://10.7.87.224:8081/video/getCat";//通过cid找猫
    public static final String URL_ADD_CAT_HEAD = "http://10.7.87.224:8081/cat/setcathead";//通过cid找猫
    public static final String URL_ADD_CAT_INFO = "";//通过cid找猫
    //192.168.11.211

}

