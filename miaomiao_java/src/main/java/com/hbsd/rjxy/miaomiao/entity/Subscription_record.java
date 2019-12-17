package com.hbsd.rjxy.miaomiao.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.util.Date;
//import java.sql.Date;

//import java.util.Date;
@Entity
@Table(name = "subscription_record")
public class Subscription_record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int srid;
    @Column(name = "cid")
    private int cid;
    @Column(name = "uid")
    private int uid;
    private int subscription_status;

    public Date getSubscription_time() {
        return subscription_time;
    }

    public void setSubscription_time(Date subscription_time) {
        this.subscription_time = subscription_time;
    }

    @Column(name = "subscription_time")
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

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public int getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(int subscription_status) {
        this.subscription_status = subscription_status;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Subscription_record{" +
                "srid=" + srid +
                ", cid=" + cid +
                ", uid=" + uid +
                ", subscription_status=" + subscription_status +
                ", subscription_time=" + subscription_time +
                '}';
    }
}
