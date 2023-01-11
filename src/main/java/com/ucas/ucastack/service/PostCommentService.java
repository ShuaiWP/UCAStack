package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.*;
import com.ucas.ucastack.util.PageResult;

import java.util.List;

public interface PostCommentService {


    // 增加一条评论
    Boolean addPostComment(PostComment postComment);

    // 删除一条评论
    Boolean delPostComment(Long commentId, Long userId);


    PageResult getCommentsByPostId(Long postId, Integer commentPage);

    public int savePostComment(PostComment comment);

    List<RecentCommentListEntity> getRecentCommentListByUserId(Long userId);
}
