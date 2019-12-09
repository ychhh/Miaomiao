package com.hbsd.rjxy.miaomiao.entity;

public class Comment {

    private int coid;
    private int miid;
    private int colike;
    private int costatus;
    private int uid;
    private String cocontent;
    private String publishTime;

    public int getCoid() {
        return coid;
    }

    public void setCoid(int coid) {
        this.coid = coid;
    }

    public int getMiid() {
        return miid;
    }

    public void setMiid(int miid) {
        this.miid = miid;
    }

    public int getColike() {
        return colike;
    }

    public void setColike(int colike) {
        this.colike = colike;
    }

    public int getCostatus() {
        return costatus;
    }

    public void setCostatus(int costatus) {
        this.costatus = costatus;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCocontent() {
        return cocontent;
    }

    public void setCocontent(String cocontent) {
        this.cocontent = cocontent;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "coid=" + coid +
                ", miid=" + miid +
                ", colike=" + colike +
                ", costatus=" + costatus +
                ", uid=" + uid +
                ", cocontent='" + cocontent + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}
