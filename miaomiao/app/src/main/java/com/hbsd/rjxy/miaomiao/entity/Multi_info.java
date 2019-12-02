package com.hbsd.rjxy.miaomiao.entity;


import java.util.Date;

/**
 *
 * 信息表的实体类
 */
public class Multi_info {

    private int miid;
    private int type;//0：视频，1：图片，2：纯文字
    private int cid;
    private int uid;
    private String mpath;  //图片或视频的路径
    private Date mupload_time;  //发布时间
    private String mcontent;   //文字内容
    private int mvisited;  //只有视频才有观看次数
    private int mstatus;//0：正常，1：锁定，2：删除，3：封禁
    private int mcomment_count;//评论数
    private String mformat;//视频或图片的格式
    private int mhot;//获得鱼干的数量
    private int maddress;//发布地点    ccid
    private String mcover;//封面图路径
    private float mrecommended;//推荐系数
    private String mtag;//标签，逗号隔开

    public int getMiid() {
        return miid;
    }

    public void setMiid(int miid) {
        this.miid = miid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMpath() {
        return mpath;
    }

    public void setMpath(String mpath) {
        this.mpath = mpath;
    }

    public Date getMupload_time() {
        return mupload_time;
    }

    public void setMupload_time(Date mupload_time) {
        this.mupload_time = mupload_time;
    }

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent;
    }

    public int getMvisited() {
        return mvisited;
    }

    public void setMvisited(int mvisited) {
        this.mvisited = mvisited;
    }

    public int getMstatus() {
        return mstatus;
    }

    public void setMstatus(int mstatus) {
        this.mstatus = mstatus;
    }

    public int getMcomment_count() {
        return mcomment_count;
    }

    public void setMcomment_count(int mcomment_count) {
        this.mcomment_count = mcomment_count;
    }

    public String getMformat() {
        return mformat;
    }

    public void setMformat(String mformat) {
        this.mformat = mformat;
    }

    public int getMhot() {
        return mhot;
    }

    public void setMhot(int mhot) {
        this.mhot = mhot;
    }

    public int getMaddress() {
        return maddress;
    }

    public void setMaddress(int maddress) {
        this.maddress = maddress;
    }

    public String getMcover() {
        return mcover;
    }

    public void setMcover(String mcover) {
        this.mcover = mcover;
    }

    public float getMrecommended() {
        return mrecommended;
    }

    public void setMrecommended(float mrecommended) {
        this.mrecommended = mrecommended;
    }

    public String getMtag() {
        return mtag;
    }

    public void setMtag(String mtag) {
        this.mtag = mtag;
    }

    @Override
    public String toString() {
        return "Multi_info{" +
                "miid=" + miid +
                ", type=" + type +
                ", cid=" + cid +
                ", uid=" + uid +
                ", mpath='" + mpath + '\'' +
                ", mupload_time=" + mupload_time +
                ", mcontent='" + mcontent + '\'' +
                ", mvisited=" + mvisited +
                ", mstatus=" + mstatus +
                ", mcomment_count=" + mcomment_count +
                ", mformat='" + mformat + '\'' +
                ", mhot=" + mhot +
                ", maddress=" + maddress +
                ", mcover='" + mcover + '\'' +
                ", mrecommended=" + mrecommended +
                ", mtag='" + mtag + '\'' +
                '}';
    }
}
