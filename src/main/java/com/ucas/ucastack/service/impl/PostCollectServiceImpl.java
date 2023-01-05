package com.ucas.ucastack.service.impl;


import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.service.PostCollectService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostCollectServiceImpl implements PostCollectService {

    @Override
    public Object validUserCollect(Long userId, Long postId) {
        return null;
    }


}
