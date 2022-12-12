package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.util.PageQueryUtil;
import com.ucas.ucastack.util.PageResult;

import java.util.List;

public interface PostService {

    int savePost(Post post);

    Post getPostById(Long postId);

    // 获取详情 并且 浏览数加1
    Post getPostForDetail(Long postId);

    int updatePost(Post post);

    int delPost(Long userId, Long postId);

    // 首页帖子列表
    PageResult getPostPageForIndex(PageQueryUtil pageUtil);

    // 近期热议帖子列表
    List getHotTopicPostList();

    // 根据userId查询发布的所有帖子
    List<Post> getMyPostList(Long userId);


    // 根据userId获取最近发帖列表
    List<Post> getRecentPostListByUserId(Long userId);
}
