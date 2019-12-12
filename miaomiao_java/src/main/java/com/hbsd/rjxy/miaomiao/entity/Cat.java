package com.hbsd.rjxy.miaomiao.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "cat")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    private int uid;
    private int miid;
    private String hpath;
    private int chot;
    private int is_ste;
    private Date cregist;
    private Date update_time;
    private Date cbirthday;
    private float cweight;
    private String cname;
    private String csex;
    private String cbreed;
    private String ctoy;
    private String cintro;
    private String csource;
    private String cfood;

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

    public int getMiid() {
        return miid;
    }

    public void setMiid(int miid) {
        this.miid = miid;
    }

    public String getHid() {
        return hpath;
    }

    public void setHid(String hpath) {
        this.hpath = hpath;
    }

    public int getChot() {
        return chot;
    }

    public void setChot(int chot) {
        this.chot = chot;
    }

    public int getIs_ste() {
        return is_ste;
    }

    public void setIs_ste(int is_ste) {
        this.is_ste = is_ste;
    }

    public Date getCregist() {
        return cregist;
    }

    public void setCregist(Date cregist) {
        this.cregist = cregist;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getCbirthday() {
        return cbirthday;
    }

    public void setCbirthday(Date cbirthday) {
        this.cbirthday = cbirthday;
    }

    public float getCweight() {
        return cweight;
    }

    public void setCweight(float cweight) {
        this.cweight = cweight;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCsex() {
        return csex;
    }

    public void setCsex(String csex) {
        this.csex = csex;
    }

    public String getCbreed() {
        return cbreed;
    }

    public void setCbreed(String cbreed) {
        this.cbreed = cbreed;
    }

    public String getCtoy() {
        return ctoy;
    }

    public void setCtoy(String ctoy) {
        this.ctoy = ctoy;
    }

    public String getCintro() {
        return cintro;
    }

    public void setCintro(String cintro) {
        this.cintro = cintro;
    }

    public String getCsource() {
        return csource;
    }

    public void setCsource(String csource) {
        this.csource = csource;
    }

    public String getCfood() {
        return cfood;
    }

    public void setCfood(String cfood) {
        this.cfood = cfood;
    }
}
