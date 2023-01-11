package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.*;
import com.ucas.ucastack.util.PageResult;

import java.util.List;

public interface PostCommentService {																 

    // 增加一条评论
    Boolean addPostComment(PostComment postComment);

    // 删除一条评论
    Boolean delPostComment(Long commentId, Long userId);


    // 帖子下详情的评论
    PageResult getCommentsByPostId(Long postId, int commentPage);

    public int savePostComment(PostComment comment);

    // 个人中心的最近评论列表						
    List<RecentCommentListEntity> getRecentCommentListByUserId(Long userId);
}
