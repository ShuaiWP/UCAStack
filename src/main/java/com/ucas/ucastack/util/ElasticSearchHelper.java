package com.ucas.ucastack.util;

import com.ucas.ucastack.entity.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ucastack
 * @description: es搜索功能的具体实现
 */
public class ElasticSearchHelper {
    public static Map<String, Object> searchByES(PageQueryUtil pageUtil) {
        Map<String, Object> map = new HashMap<>();
        // 实现具体的搜索功能，返回
        // 1. 满足条件的数目int total，具体参考PostMapper.xml中的getTotalPostsNum的实现
        // 2. 返回满足条件的所有条目列表List<Post> postList，具体参考PostMapper.xml中的findPostList的实现
        int total = 1;
        List<Post> postList = new ArrayList<>();
//      postList里面单个元素为  Post [Hash = 32053572, postId=17, publishUserId=4, postTitle=java如何学习, postCategoryId=1, postCategoryName=提问, postStatus=1, postViews=15, postComments=2, postCollects=0, lastUpdateTime=Tue Jan 03 23:11:08 CST 2023, createTime=Wed Nov 16 19:03:35 CST 2022, postContent=null]
        Post post = new Post();
        post.setPostCategoryId(15);
        postList.add(post);
        map.put("total", total);
        map.put("postList",postList);
        return map;
    }
}
