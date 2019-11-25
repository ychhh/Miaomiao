package com.hbsd.rjxy.miaomiao.entity;


import java.util.Date;

/**
 *
 * 信息表的实体类
 */
public class Muti_infor {
    private int miId;
    private int type;//0：视频，1：图片，2：纯文字
    private int catId;
    private int userId;
    private String miPath;  //图片或视频的路径
    private Date miUpdateTime;  //发布时间
    private String miContent;   //文字内容
    private int miVisited;  //只有视频才有观看次数
    private int miStatus;//0：正常，1：锁定，2：删除，3：封禁
    private int miCommentCount;//评论数
    private String miFormat;//视频或图片的格式
    private int miHot;//获得鱼干的数量
    private int miAddress;//发布地点    ccid
    private String miCover;//封面图路径
    private float miRecommended;//推荐系数
    private String miTag;//标签，逗号隔开

    public Muti_infor() {
    }

    /**
     * 视频的构造方法
     */
    public Muti_infor(int miId, int type, int catId, int userId, String miPath, Date miUpdateTime, String miContent, int miVisited, int miStatus, int miCommentCount, String miFormat, int miHot, int miAddress, String miCover, float miRecommended, String miTag) {
        this.miId = miId;
        this.type = type;
        this.catId = catId;
        this.userId = userId;
        this.miPath = miPath;
        this.miUpdateTime = miUpdateTime;
        this.miContent = miContent;
        this.miVisited = miVisited;
        this.miStatus = miStatus;
        this.miCommentCount = miCommentCount;
        this.miFormat = miFormat;
        this.miHot = miHot;
        this.miAddress = miAddress;
        this.miCover = miCover;
        this.miRecommended = miRecommended;
        this.miTag = miTag;
    }


    /**
     * 图片的构造方法
     * @return
     */
    public Muti_infor(int miId, int type, int catId, int userId, String miPath, Date miUpdateTime, String miContent, int miStatus, String miFormat, int miAddress, String miTag) {
        this.miId = miId;
        this.type = type;
        this.catId = catId;
        this.userId = userId;
        this.miPath = miPath;
        this.miUpdateTime = miUpdateTime;
        this.miContent = miContent;
        this.miStatus = miStatus;
        this.miFormat = miFormat;
        this.miAddress = miAddress;
        this.miTag = miTag;
    }

    /**
     * 纯文字的构造方法
     * @return
     */
    public Muti_infor(int miId, int type, int catId, int userId, Date miUpdateTime, String miContent, int miStatus, int miAddress) {
        this.miId = miId;
        this.type = type;
        this.catId = catId;
        this.userId = userId;
        this.miUpdateTime = miUpdateTime;
        this.miContent = miContent;
        this.miStatus = miStatus;
        this.miAddress = miAddress;
    }

    /**
     *
     *              Getter   Setter   方法
     */
    public int getMiId() {
        return miId;
    }

    public void setMiId(int miId) {
        this.miId = miId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMiPath() {
        return miPath;
    }

    public void setMiPath(String miPath) {
        this.miPath = miPath;
    }

    public Date getMiUpdateTime() {
        return miUpdateTime;
    }

    public void setMiUpdateTime(Date miUpdateTime) {
        this.miUpdateTime = miUpdateTime;
    }

    public String getMiContent() {
        return miContent;
    }

    public void setMiContent(String miContent) {
        this.miContent = miContent;
    }

    public int getMiVisited() {
        return miVisited;
    }

    public void setMiVisited(int miVisited) {
        this.miVisited = miVisited;
    }

    public int getMiStatus() {
        return miStatus;
    }

    public void setMiStatus(int miStatus) {
        this.miStatus = miStatus;
    }

    public int getMiCommentCount() {
        return miCommentCount;
    }

    public void setMiCommentCount(int miCommentCount) {
        this.miCommentCount = miCommentCount;
    }

    public String getMiFormat() {
        return miFormat;
    }

    public void setMiFormat(String miFormat) {
        this.miFormat = miFormat;
    }

    public int getMiHot() {
        return miHot;
    }

    public void setMiHot(int miHot) {
        this.miHot = miHot;
    }

    public int getMiAddress() {
        return miAddress;
    }

    public void setMiAddress(int miAddress) {
        this.miAddress = miAddress;
    }

    public String getMiCover() {
        return miCover;
    }

    public void setMiCover(String miCover) {
        this.miCover = miCover;
    }

    public float getMiRecommended() {
        return miRecommended;
    }

    public void setMiRecommended(float miRecommended) {
        this.miRecommended = miRecommended;
    }

    public String getMiTag() {
        return miTag;
    }

    public void setMiTag(String miTag) {
        this.miTag = miTag;
    }

    @Override
    public String toString() {
        return "Muti_infor{" +
                "miId=" + miId +
                ", type=" + type +
                ", catId=" + catId +
                ", userId=" + userId +
                ", miPath='" + miPath + '\'' +
                ", miUpdateTime=" + miUpdateTime +
                ", miContent='" + miContent + '\'' +
                ", miVisited=" + miVisited +
                ", miStatus=" + miStatus +
                ", miCommentCount=" + miCommentCount +
                ", miFormat='" + miFormat + '\'' +
                ", miHot=" + miHot +
                ", miAddress=" + miAddress +
                ", miCover='" + miCover + '\'' +
                ", miRecommended=" + miRecommended +
                ", miTag='" + miTag + '\'' +
                '}';
    }
}
