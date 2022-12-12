package com.ucas.ucastack.service;


import com.ucas.ucastack.entity.PostCategory;

import java.util.List;

public interface PostCategoryService {

    List<PostCategory> getPostCategories();

    PostCategory selectById(Integer categoryId);
}
