package com.ucas.ucastack.dao;


import com.ucas.ucastack.entity.PostCategory;

import java.util.List;

public interface PostCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(PostCategory record);

    int insertSelective(PostCategory record);

    PostCategory selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(PostCategory record);

    int updateByPrimaryKey(PostCategory record);

    List<PostCategory> getPostCategories();
}