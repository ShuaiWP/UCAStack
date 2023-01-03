package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.entity.CommentListEntity;
import com.ucas.ucastack.service.PostCommentService;
import com.ucas.ucastack.util.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    /**
     * 以下是用来测试的方法，构造了数据，需要修改
     * @param postId
     * @param commentPage
     * @return
     */
    @Override
    public PageResult getCommentsByPostId(Long postId, Integer commentPage) {
        List<CommentListEntity> commentListEntities = new ArrayList<>();
        CommentListEntity c1 = new CommentListEntity((long)11, (long)17, (long)4, "@lucy：不用", (long)4, new Date("Wed Nov 16 19:04:37 CST 2022"), "lucy", "/images/avatar/default.png");
        CommentListEntity c2 = new CommentListEntity((long)10, (long)17, (long)4, "wy也想知道", (long)0, new Date("Wed Nov 16 19:04:03 CST 2022"), "lucy", "/images/avatar/default.png");
        commentListEntities.add(c1);
        commentListEntities.add(c2);
        PageResult pageResult = new PageResult(commentListEntities, 2, 6, 1);
        return pageResult;
    }
}
