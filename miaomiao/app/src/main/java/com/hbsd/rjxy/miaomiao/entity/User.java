package com.hbsd.rjxy.miaomiao.entity;


import java.util.Date;

/**
 *
 * user表的实体类
 */
public class User {

    private int userId;
    private String userTel;
    private String userSex;
    private String userPassword;
    private String userName;
    private String userIntro;
    private String hPath;
    private int isVip;
    private int isAdmain;
    private int bannedId;
    private int userCatCount;
    private Date userRegistTime;
    private Date userLastLogin;

    public User() {
    }

    public User(int userId, String userTel, String userSex, String userPassword,
                String userName, String userIntro, String hPath, int isVip,
                int isAdmain, int bannedId, int userCatCount, Date userRegistTime,
                Date userLastLogin) {
        this.userId = userId;
        this.userTel = userTel;
        this.userSex = userSex;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userIntro = userIntro;
        this.hPath = hPath;
        this.isVip = isVip;
        this.isAdmain = isAdmain;
        this.bannedId = bannedId;
        this.userCatCount = userCatCount;
        this.userRegistTime = userRegistTime;
        this.userLastLogin = userLastLogin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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


    public String gethPath() {
        return hPath;
    }

    public void sethPath(String hPath) {
        this.hPath = hPath;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsAdmain() {
        return isAdmain;
    }

    public void setIsAdmain(int isAdmain) {
        this.isAdmain = isAdmain;
    }

    public int getBannedId() {
        return bannedId;
    }

    public void setBannedId(int bannedId) {
        this.bannedId = bannedId;
    }

    public int getUserCatCount() {
        return userCatCount;
    }

    public void setUserCatCount(int userCatCount) {
        this.userCatCount = userCatCount;
    }

    public Date getUserRegistTime() {
        return userRegistTime;
    }

    public void setUserRegistTime(Date userRegistTime) {
        this.userRegistTime = userRegistTime;
    }

    public Date getUserLastLogin() {
        return userLastLogin;
    }

    public void setUserLastLogin(Date userLastLogin) {
        this.userLastLogin = userLastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userTel='" + userTel + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", userIntro='" + userIntro + '\'' +
                ", hPath='" + hPath + '\'' +
                ", isVip=" + isVip +
                ", isAdmain=" + isAdmain +
                ", bannedId=" + bannedId +
                ", userCatCount=" + userCatCount +
                ", userRegistTime=" + userRegistTime +
                ", userLastLogin=" + userLastLogin +
                '}';
    }
}
