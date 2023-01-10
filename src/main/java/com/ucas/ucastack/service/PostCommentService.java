package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.*;
import com.ucas.ucastack.util.PageResult;

import java.util.List;

public interface PostCommentService {
    PageResult getCommentsByPostId(Long postId, Integer commentPage);

    public int savePostComment(PostComment comment);

    List<RecentCommentListEntity> getRecentCommentListByUserId(Long userId);
}
