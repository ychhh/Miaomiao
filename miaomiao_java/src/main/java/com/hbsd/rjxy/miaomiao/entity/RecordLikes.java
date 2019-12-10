package com.hbsd.rjxy.miaomiao.entity;


import javax.persistence.*;

@Entity
@Table(name = "recordlikes")
public class RecordLikes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rlid;
    private int coid;
    private int uid;
    private int miid;
    private int rltype;//1就是喜欢，0就是取消


    public int getRlid() {
        return rlid;
    }

    public void setRlid(int rlid) {
        this.rlid = rlid;
    }

    public int getCoid() {
        return coid;
    }

    public void setCoid(int coid) {
        this.coid = coid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRltype() {
        return rltype;
    }

    public void setRltype(int rltype) {
        this.rltype = rltype;
    }

    public int getMiid() {
        return miid;
    }

    public void setMiid(int miid) {
        this.miid = miid;
    }

    @Override
    public String toString() {
        return "RecordLikes{" +
                "rlid=" + rlid +
                ", coid=" + coid +
                ", uid=" + uid +
                ", miid=" + miid +
                ", rltype=" + rltype +
                '}';
    }
}
