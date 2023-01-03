package com.ucas.ucastack.entity;

import java.util.Date;

/**
 * 论坛用户
 */
public class User {
    private Long userId;

    private String loginName;

    private String passwordMd5;

    private String nickName;

    private String headImgUrl;

    private String gender;

    private String location;

    private String introduce;

    private Byte userStatus;

    private Date lastLoginTime;

    private Date createTime;

    public User(Long userId, String loginName, String passwordMd5, String nickName, String headImgUrl, String gender, String location, String introduce, Byte userStatus, Date lastLoginTime, Date createTime) {
        this.userId = userId;
        this.loginName = loginName;
        this.passwordMd5 = passwordMd5;
        this.nickName = nickName;
        this.headImgUrl = headImgUrl;
        this.gender = gender;
        this.location = location;
        this.introduce = introduce;
        this.userStatus = userStatus;
        this.lastLoginTime = lastLoginTime;
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginName='" + loginName + '\'' +
                ", passwordMd5='" + passwordMd5 + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", introduce='" + introduce + '\'' +
                ", userStatus=" + userStatus +
                ", lastLoginTime=" + lastLoginTime +
                ", createTime=" + createTime +
                '}';
    }
}