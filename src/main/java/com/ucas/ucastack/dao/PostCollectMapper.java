package com.ucas.ucastack.dao;

import com.ucas.ucastack.entity.PostCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostCollectMapper {
    int deleteByPrimaryKey(Long recordId);

    int insert(PostCollect record);

    int insertSelective(PostCollect record);

    PostCollect selectByPrimaryKey(Long recordId);

    PostCollect selectByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    List<PostCollect> listByUserId(@Param("userId") Long userId);

    int updateByPrimaryKeySelective(PostCollect record);

    int updateByPrimaryKey(PostCollect record);
}
