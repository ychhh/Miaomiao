package com.hbsd.rjxy.miaomiao.entity;

import java.util.Date;

/**
 * @Description  
 * @Author  
 * @Date 2020-04-22 
 */


public class User {

	private Integer id;

	private String userPhone;

	private String userSex;

	private String userPwd;

	private String userName;

	private String userIntro;

	private String headId;//此字段暂时存储头像位置

	private Integer isVip;

	private Integer isAdmin;

	private Integer bannedId;

	private Integer catCount;

	private Integer version;

	private Integer deleted;

	private Date lastLogin;

	private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getBannedId() {
        return bannedId;
    }

    public void setBannedId(Integer bannedId) {
        this.bannedId = bannedId;
    }

    public Integer getCatCount() {
        return catCount;
    }

    public void setCatCount(Integer catCount) {
        this.catCount = catCount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
