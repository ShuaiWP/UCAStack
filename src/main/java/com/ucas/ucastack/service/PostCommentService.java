package com.ucas.ucastack.service;

import com.ucas.ucastack.util.PageResult;

public interface PostCommentService {
    PageResult getCommentsByPostId(Long postId, Integer commentPage);
}
