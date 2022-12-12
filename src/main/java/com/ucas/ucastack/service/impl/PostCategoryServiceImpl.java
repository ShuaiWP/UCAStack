package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.dao.PostCategoryMapper;
import com.ucas.ucastack.entity.PostCategory;
import com.ucas.ucastack.service.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCategoryServiceImpl implements PostCategoryService {

    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @Override
    public List<PostCategory> getPostCategories() {
        return postCategoryMapper.getPostCategories();
    }

    @Override
    public PostCategory selectById(Integer categoryId) {
        return postCategoryMapper.selectByPrimaryKey(categoryId);
    }
}
