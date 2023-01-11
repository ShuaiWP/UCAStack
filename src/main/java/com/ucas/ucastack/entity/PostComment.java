package com.ucas.ucastack.entity;

import java.util.Date;

public class PostComment {
    private Long commentId;
    private Long postId;
    private Long commentUserId;
    private String commentBody;
    private Long parentCommentUserId;
    private Date commentCreateTime;
    private Byte isDeleted;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDelete) {
        this.isDeleted = isDelete;
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", commentUserId=" + commentUserId +
                ", commentBody='" + commentBody + '\'' +
                ", parentCommentUserId=" + parentCommentUserId +
                ", commentCreateTime=" + commentCreateTime +
                ", isDelete=" + isDeleted +
                '}';
    }
}
