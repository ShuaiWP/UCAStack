package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.Post;

import java.util.List;

public interface PostCollectService {
    Object validUserCollect(Long userId, Long postId);

    /**
     * 根据用户ID，获取用户收藏的帖子列表
     */
//    List<Post> getCollectRecordsByUserId(Long userId);
}
