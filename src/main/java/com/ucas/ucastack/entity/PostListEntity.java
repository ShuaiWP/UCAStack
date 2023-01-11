package com.ucas.ucastack.entity;

import java.util.Date;

/**
 * 帖子列表实体类
 * 页面展示列表帖子的字段与帖子实体类相比少了content,post_category_id,post_status
 * last_update_time,增加了nickName与headImgUrl
 */
public class PostListEntity {
    private Long postId;

    private Long publishUserId;

    private String postCategoryName;

    private String postTitle;

    private Long postViews;

    private Long postComments;

    private Long postCollects;

    private String nickName;

    private String headImgUrl;

    private Date createTime;

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

    public String getPostCategoryName() {
        return postCategoryName;
    }

    public void setPostCategoryName(String postCategoryName) {
        this.postCategoryName = postCategoryName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PostListEntity{" +
                "postId=" + postId +
                ", publishUserId=" + publishUserId +
                ", postCategoryName='" + postCategoryName + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postViews=" + postViews +
                ", postComments=" + postComments +
                ", postCollects=" + postCollects +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}