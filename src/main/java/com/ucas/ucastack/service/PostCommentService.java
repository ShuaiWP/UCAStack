package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.PostCategory;
import com.ucas.ucastack.entity.PostComment;
import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.util.PageResult;

public interface PostCommentService {
    PageResult getCommentsByPostId(Long postId, Integer commentPage);

    public int savePostComment(PostComment comment);
}
