package com.ucas.ucastack.entity;

import java.util.Date;

/**
 * 评论列表-实体类
 * 页面展示时需要的字段与评论实体类不同，因此新增了这个类
 */
public class CommentListEntity {

    private Long commentId;

    private Long postId;

    private Long commentUserId;

    private String commentBody;

    private Long parentCommentUserId;

    private Date commentCreateTime;

    private String nickName;

    private String headImgUrl;


    public CommentListEntity() {
    }

    public CommentListEntity(Long commentId, Long postId, Long commentUserId, String commentBody, Long parentCommentUserId, Date commentCreateTime, String nickName, String headImgUrl) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentUserId = commentUserId;
        this.commentBody = commentBody;
        this.parentCommentUserId = parentCommentUserId;
        this.commentCreateTime = commentCreateTime;
        this.nickName = nickName;
        this.headImgUrl = headImgUrl;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Long getParentCommentUserId() {
        return parentCommentUserId;
    }

    public void setParentCommentUserId(Long parentCommentUserId) {
        this.parentCommentUserId = parentCommentUserId;
    }

    public Date getCommentCreateTime() {
        return commentCreateTime;
    }

    public void setCommentCreateTime(Date commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
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

    @Override
    public String toString() {
        return "BBSCommentListEntity{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", commentUserId=" + commentUserId +
                ", commentBody='" + commentBody + '\'' +
                ", parentCommentUserId=" + parentCommentUserId +
                ", commentCreateTime=" + commentCreateTime +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                '}';
    }
}