package com.hbsd.rjxy.miaomiao.entity;

public class Subscription_record {

    private int srid;
    private int cid;
    private int uid;
    private int subscription_status;
    private String subscription_time;

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
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

    public int getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(int subscription_status) {
        this.subscription_status = subscription_status;
    }

    public String getSubscription_time() {
        return subscription_time;
    }

    public void setSubscription_time(String subscription_time) {
        this.subscription_time = subscription_time;
    }

    @Override
    public String toString() {
        return "Subscription_record{" +
                "srid=" + srid +
                ", cid=" + cid +
                ", uid=" + uid +
                ", subscription_status=" + subscription_status +
                ", subscription_time='" + subscription_time + '\'' +
                '}';
    }
}
