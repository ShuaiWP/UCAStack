package com.ucas.ucastack.service.impl;


import com.ucas.ucastack.dao.PostCollectMapper;
import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.PostCollect;
import com.ucas.ucastack.service.PostCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostCollectServiceImpl implements PostCollectService {

    @Autowired
    private PostCollectMapper postCollectMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    public Object validUserCollect(Long userId, Long postId) {
        return null;
    }

    @Override
    public List<Post> getCollectRecordsByUserId(Long userId) {
        List<PostCollect> bbsPostCollects = postCollectMapper.listByUserId(userId);

        if (!CollectionUtils.isEmpty(bbsPostCollects)) {
            List<Long> postIds = bbsPostCollects.stream().map(PostCollect::getPostId).collect(Collectors.toList());
            List<Post> bbsPosts = postMapper.selectByPrimaryKeys(postIds);
            return bbsPosts;
        }

        return null;
    }


}
