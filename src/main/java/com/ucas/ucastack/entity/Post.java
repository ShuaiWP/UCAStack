package com.ucas.ucastack.entity;

import java.util.Date;
// 帖子类，对应数据库中的post表
public class Post {
    private Long postId;
    private Long publishUserId;
    private String postTitle;
    private Integer postCategoryId;
    private String postCategoryName;
    private Byte postStatus;
    private Long postViews;
    private Long postComments;
    private Long postCollects;
    private Date lastUpdateTime;
    private Date createTime;
    private String postContent;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(Long publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Integer getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(Integer postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public String getPostCategoryName() {
        return postCategoryName;
    }

    public void setPostCategoryName(String postCategoryName) {
        this.postCategoryName = postCategoryName;
    }

    public Byte getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(Byte postStatus) {
        this.postStatus = postStatus;
    }

    public Long getPostViews() {
        return postViews;
    }

    public void setPostViews(Long postViews) {
        this.postViews = postViews;
    }

    public Long getPostComments() {
        return postComments;
    }

    public void setPostComments(Long postComments) {
        this.postComments = postComments;
    }

    public Long getPostCollects() {
        return postCollects;
    }

    public void setPostCollects(Long postCollects) {
        this.postCollects = postCollects;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", publishUserId=" + publishUserId +
                ", postTitle='" + postTitle + '\'' +
                ", postCategoryId=" + postCategoryId +
                ", postCategoryName='" + postCategoryName + '\'' +
                ", postStatus=" + postStatus +
                ", postViews=" + postViews +
                ", postComments=" + postComments +
                ", postCollects=" + postCollects +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createTime=" + createTime +
                ", postContent='" + postContent + '\'' +
                '}';
    }
}