package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.dao.PostCategoryMapper;
import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.dao.UserMapper;
import com.ucas.ucastack.entity.*;
import com.ucas.ucastack.service.PostService;
import com.ucas.ucastack.util.BeanUtil;
import com.ucas.ucastack.util.ElasticSearchHelper;
import com.ucas.ucastack.util.PageQueryUtil;
import com.ucas.ucastack.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostCategoryMapper postCategoryMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int savePost(Post post) {
        User user = userMapper.selectByPrimaryKey(post.getPublishUserId());
        if (user == null || user.getUserStatus().intValue() == 1) {
            //账号已被封禁
            return 0;
        }

        PostCategory postCategory = postCategoryMapper.selectByPrimaryKey(post.getPostCategoryId());
        if (postCategory == null) {
            //分类数据错误
            return 0;
        }

        Post existPost = postMapper.selectByPrimaryKey(post.getPostId());
        if (existPost != null) {
            // the post already existed
            return 0;
        }

        return postMapper.insertSelective(post);
    }

    @Override
    public Post getPostById(Long postId) {
        return postMapper.selectByPrimaryKey(postId);
    }

    @Override
    public Post getPostForDetail(Long postId) {
        Post post = postMapper.selectByPrimaryKey(postId);
        if (post != null) {
            post.setPostViews(post.getPostViews() + 1);
            postMapper.updateByPrimaryKeySelective(post);
        }
        return post;
    }

    @Override
    public int updatePost(Post post) {
        User user = userMapper.selectByPrimaryKey(post.getPublishUserId());

        if (user == null || user.getUserStatus().intValue() == 1) {
            //账号已被封禁
            return 0;
        }

        PostCategory postCategory = postCategoryMapper.selectByPrimaryKey(post.getPostCategoryId());
        if (postCategory == null) {
            //分类数据错误
            return 0;
        }

        return postMapper.updateByPrimaryKeySelective(post);
    }

    @Override
    public int delPost(Long userId, Long postId) {
        User user = userMapper.selectByPrimaryKey(userId);

        if (user == null || user.getUserStatus().intValue() == 1) {
            //账号已被封禁
            return 0;
        }

        Post post = postMapper.selectByPrimaryKey(postId);
        // 对象不为空且发帖人为当前登录用户才可以删除
        if (post != null && post.getPublishUserId().equals(userId)) {
            return postMapper.deleteByPrimaryKey(postId);
        }
        return 0;
    }

    @Override
    public PageResult getPostPageForIndex(PageQueryUtil pageUtil) {
        //查询帖子数据
        int total = postMapper.getTotalPostsNum(pageUtil);
        List<Post> postList = postMapper.findPostList(pageUtil);
        List<PostListEntity> postListEntities = new ArrayList<>();
        //数据格式转换
        if (!CollectionUtils.isEmpty(postList)) {
            postListEntities = BeanUtil.copyList(postList, PostListEntity.class);
            List<Long> userIds = postListEntities.stream().map(PostListEntity::getPublishUserId).collect(Collectors.toList());
            //查询user数据
            List<User> users = userMapper.selectByPrimaryKeys(userIds);
            if (!CollectionUtils.isEmpty(users)) {
                //封装user数据
                Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (entity1, entity2) -> entity1));
                for (PostListEntity postListEntity : postListEntities) {
                    if (userMap.containsKey(postListEntity.getPublishUserId())) {
                        //设置头像字段和昵称字段
                        User tempUser = userMap.get(postListEntity.getPublishUserId());
                        postListEntity.setHeadImgUrl(tempUser.getHeadImgUrl());
                        postListEntity.setNickName(tempUser.getNickName());
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(postListEntities, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult getPostPageByElasticSearchForIndex(PageQueryUtil pageUtil) {
        //查询帖子数据
        Map<String, Object> map = ElasticSearchHelper.searchByES(pageUtil);
        int total = (Integer)map.get("total");
        List<Post> postList = (List<Post>)map.get("postList");
//        int total = postMapper.getTotalPostsNum(pageUtil);
//        List<Post> postList = postMapper.findPostList(pageUtil);
        List<PostListEntity> postListEntities = new ArrayList<>();
        //数据格式转换
        if (!CollectionUtils.isEmpty(postList)) {
            postListEntities = BeanUtil.copyList(postList, PostListEntity.class);
            List<Long> userIds = postListEntities.stream().map(PostListEntity::getPublishUserId).collect(Collectors.toList());
            //查询user数据
            List<User> users = userMapper.selectByPrimaryKeys(userIds);
            if (!CollectionUtils.isEmpty(users)) {
                //封装user数据
                Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (entity1, entity2) -> entity1));
                for (PostListEntity postListEntity : postListEntities) {
                    if (userMap.containsKey(postListEntity.getPublishUserId())) {
                        //设置头像字段和昵称字段
                        User tempUser = userMap.get(postListEntity.getPublishUserId());
                        postListEntity.setHeadImgUrl(tempUser.getHeadImgUrl());
                        postListEntity.setNickName(tempUser.getNickName());
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(postListEntities, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public List getHotTopicPostList() {
        List<Post> PostList = postMapper.getHotTopicPostList();
        List<HotTopicPostListEntity> hotTopicPostList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(PostList)) {
            hotTopicPostList = BeanUtil.copyList(PostList, HotTopicPostListEntity.class);
        }
        return hotTopicPostList;
    }

    @Override
    public List<Post> getMyPostList(Long userId) {
        return postMapper.getMyPostList(userId, "all");
    }

    @Override
    public List<Post> getRecentPostListByUserId(Long userId) {
        return postMapper.getMyPostList(userId, "recent");
    }
}
