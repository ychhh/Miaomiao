package com.hbsd.rjxy.miaomiao.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "subscription_record")
public class Subscription_record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int suid;
    private int cid;
    private int uid;
    private int subscription_status;
    private Date subscription_time;

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

    public Date getSubscription_time() {
        return subscription_time;
    }

    public void setSubscription_time(Date subscription_time) {
        this.subscription_time = subscription_time;
    }

    public int getSuid() {
        return suid;
    }

    public void setSuid(int suid) {
        this.suid = suid;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "suid=" + suid +
                ", cid=" + cid +
                ", uid=" + uid +
                ", subscription_status=" + subscription_status +
                ", subscription_time=" + subscription_time +
                '}';
    }
}
