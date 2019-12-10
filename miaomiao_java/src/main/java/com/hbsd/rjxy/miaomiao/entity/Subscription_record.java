package com.hbsd.rjxy.miaomiao.entity;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.util.Date;
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
    private int subscriibe_status;
    private Date subscribe_time;

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

    public int getSubscriibe_status() {
        return subscriibe_status;
    }

    public void setSubscriibe_status(int subscriibe_status) {
        this.subscriibe_status = subscriibe_status;
    }

    public Date getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(Date subscribe_time) {
        this.subscribe_time = subscribe_time;
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
                ", subscriibe_status=" + subscriibe_status +
                ", subscribe_time=" + subscribe_time +
                '}';
    }
}
