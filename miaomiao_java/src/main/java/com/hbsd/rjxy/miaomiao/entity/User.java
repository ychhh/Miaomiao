package com.hbsd.rjxy.miaomiao.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User 的实体类
 */
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private String tel;
    private String usex;
    private String pwd;
    private String username;
    private String uintro;
    private String hpath;
    private Integer hid;
    private Integer is_vip;
    private Integer is_admin;
    private Integer bid;
    private Integer cat_count;
    private Date uregist;
    private Date ulast_login;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUintro() {
        return uintro;
    }

    public void setUintro(String uintro) {
        this.uintro = uintro;
    }

    public Integer getHid() {
        return hid;
    }

    public void setHid(Integer hid) {
        this.hid = hid;
    }

    public Integer getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(Integer is_vip) {
        this.is_vip = is_vip;
    }

    public Integer getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Integer is_admin) {
        this.is_admin = is_admin;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getCat_count() {
        return cat_count;
    }

    public void setCat_count(Integer cat_count) {
        this.cat_count = cat_count;
    }

    public Date getUregist() {
        return uregist;
    }

    public void setUregist(Date uregist) {
        this.uregist = uregist;
    }

    public Date getUlast_login() {
        return ulast_login;
    }

    public void setUlast_login(Date ulast_login) {
        this.ulast_login = ulast_login;
    }

    public String getHpath() {
        return hpath;
    }

    public void setHpath(String hpath) {
        this.hpath = hpath;
    }
}
