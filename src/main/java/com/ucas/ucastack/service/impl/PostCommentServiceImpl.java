package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.dao.PostCommentMapper;
import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.dao.UserMapper;
import com.ucas.ucastack.entity.*;
import com.ucas.ucastack.service.PostCommentService;
import com.ucas.ucastack.util.BeanUtil;
import com.ucas.ucastack.util.PageResult;
import com.ucas.ucastack.util.PageQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private PostCommentMapper PostCommentMapper;

    @Autowired
    private PostMapper PostMapper;

    @Autowired
    private UserMapper UserMapper;

	@Override
    @Transactional
    public Boolean addPostComment(PostComment postComment) {
        Post Post = PostMapper.selectByPrimaryKey(postComment.getPostId());
        if (Post == null) {
            return false;
        }
        User User = UserMapper.selectByPrimaryKey(postComment.getCommentUserId());

        if (User == null || User.getUserStatus().intValue() == 1) {
            //账号被封禁
            return false;
        }
        Post.setPostComments(Post.getPostComments() + 1);

        return PostCommentMapper.insertSelective(postComment) > 0 && PostMapper.updateByPrimaryKeySelective(Post) > 0;
    }

    @Override
    @Transactional
    public Boolean delPostComment(Long commentId, Long userId) {

        PostComment PostComment = PostCommentMapper.selectByPrimaryKey(commentId);
        //评论不存在，不能删除
        if (PostComment == null) {
            return false;
        }

        User User = UserMapper.selectByPrimaryKey(userId);

        if (User == null || User.getUserStatus().intValue() == 1) {
            //账号被封禁
            return false;
        }

        Post Post = PostMapper.selectByPrimaryKey(PostComment.getPostId());

        //评论所关联的帖子不存在，不能删除
        if (Post == null) {
            return false;
        }

        Long commentCount = Post.getPostComments() - 1;
        if (commentCount >= 0) {
            Post.setPostComments(commentCount);
        }

        if (userId.equals(PostComment.getCommentUserId()) || userId.equals(Post.getPublishUserId())) {
            //这条评论所关联的user或者这条评论所关联帖子的user都可以删除该评论
            //即评论者和发帖者都可以删帖
            return PostCommentMapper.deleteByPrimaryKey(commentId) > 0 && PostMapper.updateByPrimaryKeySelective(Post) > 0;
        }

        return false;
    }

	@Override
    public PageResult getCommentsByPostId(Long postId, int commentPage) {
        Map params = new HashMap();
        params.put("postId", postId);
        params.put("page", commentPage);
        params.put("limit", 6);//每页展示6条评论
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        //查询评论数据
        int total = PostCommentMapper.getTotalComments(pageUtil);
        List<PostComment> commentList = PostCommentMapper.findCommentList(pageUtil);
        List<CommentListEntity> CommentListEntities = new ArrayList<>();
        //数据格式转换
        if (!CollectionUtils.isEmpty(commentList)) {
            CommentListEntities = BeanUtil.copyList(commentList, CommentListEntity.class);
            //当前评论者的userId
            List<Long> userIds = CommentListEntities.stream().map(CommentListEntity::getCommentUserId).collect(Collectors.toList());
            //被回复评论的评论者userId
            List<Long> parentUserIds = CommentListEntities.stream().map(CommentListEntity::getParentCommentUserId).collect(Collectors.toList());
            //分别查询user数据
            List<User> Users = UserMapper.selectByPrimaryKeys(userIds);
            List<User> parentUsers = UserMapper.selectByPrimaryKeys(parentUserIds);
            if (!CollectionUtils.isEmpty(Users)) {
                //封装user数据
                Map<Long, User> UserMap = Users.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (entity1, entity2) -> entity1));
                for (CommentListEntity CommentListEntity : CommentListEntities) {
                    if (UserMap.containsKey(CommentListEntity.getCommentUserId())) {
                        //设置头像字段和昵称字段
                        User tempUser = UserMap.get(CommentListEntity.getCommentUserId());
                        CommentListEntity.setHeadImgUrl(tempUser.getHeadImgUrl());
                        CommentListEntity.setNickName(tempUser.getNickName());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(parentUsers)) {
                //添加被回复者的信息
                Map<Long, User> parentUserMap = parentUsers.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (entity1, entity2) -> entity1));
                for (CommentListEntity CommentListEntity : CommentListEntities) {
                    if (parentUserMap.containsKey(CommentListEntity.getParentCommentUserId())) {
                        //在评论内容前加上"@xxx "
                        User tempUser = parentUserMap.get(CommentListEntity.getParentCommentUserId());
                        CommentListEntity.setCommentBody("@" + tempUser.getNickName() + "：" + CommentListEntity.getCommentBody());
                    }
                }
            }


        }
        PageResult pageResult = new PageResult(CommentListEntities, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }		 
    @Override
    public List<RecentCommentListEntity> getRecentCommentListByUserId(Long userId) {
        List<PostComment> recentCommentList = PostCommentMapper.getRecentCommentListByUserId(userId);
        List<RecentCommentListEntity> recentCommentListEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recentCommentList)) {
            recentCommentListEntities = BeanUtil.copyList(recentCommentList, RecentCommentListEntity.class);
            //帖子id
            List<Long> postIds = recentCommentList.stream().map(PostComment::getPostId).collect(Collectors.toList());
            //查询帖子数据
            List<Post> Posts = PostMapper.selectByPrimaryKeys(postIds);
            if (!CollectionUtils.isEmpty(Posts)) {
                //封装帖子数据
                Map<Long, Post> PostMap = Posts.stream().collect(Collectors.toMap(Post::getPostId, Function.identity(), (entity1, entity2) -> entity1));
                for (RecentCommentListEntity recentCommentListEntity : recentCommentListEntities) {
                    if (PostMap.containsKey(recentCommentListEntity.getPostId())) {
                        //设置帖子标题
                        Post Post = PostMap.get(recentCommentListEntity.getPostId());
                        recentCommentListEntity.setPostTitle(Post.getPostTitle());
                    }
                }
            }
        }
        return recentCommentListEntities;
    }


    //TODO: 此服务的实现只实现了爬虫需要的部分功能，其余功能还需要 @华为 实现 （savePostComment 的保存也需要更多的判断）
    //此函数仅仅用于爬虫部分的评论，正常添加的评论利用上方的addPostComment

    @Override
    public int savePostComment(PostComment comment) {
        if (comment.getCommentId() == -1) { // the data is fetched online
            return PostCommentMapper.insertIncrement(comment);
        }

        return 0;
    }

}
