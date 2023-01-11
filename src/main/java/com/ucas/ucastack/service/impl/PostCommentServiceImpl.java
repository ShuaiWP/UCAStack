package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.dao.PostCommentMapper;
import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.dao.UserMapper;
import com.ucas.ucastack.entity.*;
import com.ucas.ucastack.service.PostCommentService;
import com.ucas.ucastack.util.BeanUtil;
import com.ucas.ucastack.util.PageQueryUtil;
import com.ucas.ucastack.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private PostCommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional
    public Boolean addPostComment(PostComment postComment) {
        Post Post = postMapper.selectByPrimaryKey(postComment.getPostId());
        if (Post == null) {
            return false;
        }
        User User = userMapper.selectByPrimaryKey(postComment.getCommentUserId());

        if (User == null || User.getUserStatus().intValue() == 1) {
            //账号被封禁
            return false;
        }
        Post.setPostComments(Post.getPostComments() + 1);

        return commentMapper.insertSelective(postComment) > 0 && postMapper.updateByPrimaryKeySelective(Post) > 0;
    }

    @Override
    @Transactional
    public Boolean delPostComment(Long commentId, Long userId) {

        PostComment PostComment = commentMapper.selectByPrimaryKey(commentId);
        //评论不存在，不能删除
        if (PostComment == null) {
            return false;
        }

        User User = userMapper.selectByPrimaryKey(userId);

        if (User == null || User.getUserStatus().intValue() == 1) {
            //账号被封禁
            return false;
        }

        Post Post = postMapper.selectByPrimaryKey(PostComment.getPostId());

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
            return commentMapper.deleteByPrimaryKey(commentId) > 0 && postMapper.updateByPrimaryKeySelective(Post) > 0;
        }

        return false;
    }



    /**
     * 以下是用来测试的方法，构造了数据，需要修改
     * @param postId
     * @param commentPage
     * @return
     */
    @Override
    public PageResult getCommentsByPostId(Long postId, Integer commentPage) {
//        List<CommentListEntity> commentListEntities = new ArrayList<>();
//        CommentListEntity c1 = new CommentListEntity((long)11, (long)17, (long)4, "@lucy：不用", (long)4, new Date("Wed Nov 16 19:04:37 CST 2022"), "lucy", "/images/avatar/default.png");
//        CommentListEntity c2 = new CommentListEntity((long)10, (long)17, (long)4, "wy也想知道", (long)0, new Date("Wed Nov 16 19:04:03 CST 2022"), "lucy", "/images/avatar/default.png");
//        commentListEntities.add(c1);
//        commentListEntities.add(c2);
//        PageResult pageResult = new PageResult(commentListEntities, 2, 6, 1);
//        return pageResult;
        Map params = new HashMap();
        params.put("postId", postId);
        params.put("page", commentPage);
        params.put("limit", 6);//每页展示6条评论
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        //查询评论数据
        int total = commentMapper.getTotalComments(pageUtil);
        List<PostComment> commentList = commentMapper.findCommentList(pageUtil);
        List<CommentListEntity> bbsCommentListEntities = new ArrayList<>();
        //数据格式转换
        if (!CollectionUtils.isEmpty(commentList)) {
            bbsCommentListEntities = BeanUtil.copyList(commentList, CommentListEntity.class);
            //当前评论者的userId
            List<Long> userIds = bbsCommentListEntities.stream().map(CommentListEntity::getCommentUserId).collect(Collectors.toList());
            //被回复评论的评论者userId
            List<Long> parentUserIds = bbsCommentListEntities.stream().map(CommentListEntity::getParentCommentUserId).collect(Collectors.toList());
            //分别查询user数据
            List<User> bbsUsers = userMapper.selectByPrimaryKeys(userIds);
            List<User> parentUsers = userMapper.selectByPrimaryKeys(parentUserIds);
            if (!CollectionUtils.isEmpty(bbsUsers)) {
                //封装user数据
                Map<Long, User> bbsUserMap = bbsUsers.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (entity1, entity2) -> entity1));
                for (CommentListEntity bbsCommentListEntity : bbsCommentListEntities) {
                    if (bbsUserMap.containsKey(bbsCommentListEntity.getCommentUserId())) {
                        //设置头像字段和昵称字段
                        User tempUser = bbsUserMap.get(bbsCommentListEntity.getCommentUserId());
                        bbsCommentListEntity.setHeadImgUrl(tempUser.getHeadImgUrl());
                        bbsCommentListEntity.setNickName(tempUser.getNickName());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(parentUsers)) {
                //添加被回复者的信息
                Map<Long, User> parentUserMap = parentUsers.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (entity1, entity2) -> entity1));
                for (CommentListEntity bbsCommentListEntity : bbsCommentListEntities) {
                    if (parentUserMap.containsKey(bbsCommentListEntity.getParentCommentUserId())) {
                        //在评论内容前加上"@xxx "
                        User tempUser = parentUserMap.get(bbsCommentListEntity.getParentCommentUserId());
                        bbsCommentListEntity.setCommentBody("@" + tempUser.getNickName() + "：" + bbsCommentListEntity.getCommentBody());
                    }
                }
            }


        }
        PageResult pageResult = new PageResult(bbsCommentListEntities, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    //TODO: 此服务的实现只实现了爬虫需要的部分功能，其余功能还需要 @华为 实现 （savePostComment 的保存也需要更多的判断）

    @Override
    public int savePostComment(PostComment comment) {
        if (comment.getCommentId() == -1) { // the data is fetched online
            return commentMapper.insertIncrement(comment);
        }

        return 0;
    }

    @Override
    public List<RecentCommentListEntity> getRecentCommentListByUserId(Long userId) {
        List<PostComment> recentCommentList = commentMapper.getRecentCommentListByUserId(userId);
        List<RecentCommentListEntity> recentCommentListEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recentCommentList)) {
            recentCommentListEntities = BeanUtil.copyList(recentCommentList, RecentCommentListEntity.class);
            //帖子id
            List<Long> postIds = recentCommentList.stream().map(PostComment::getPostId).collect(Collectors.toList());
            //查询帖子数据
            List<Post> bbsPosts = postMapper.selectByPrimaryKeys(postIds);
            if (!CollectionUtils.isEmpty(bbsPosts)) {
                //封装帖子数据
                Map<Long, Post> bbsPostMap = bbsPosts.stream().collect(Collectors.toMap(Post::getPostId, Function.identity(), (entity1, entity2) -> entity1));
                for (RecentCommentListEntity recentCommentListEntity : recentCommentListEntities) {
                    if (bbsPostMap.containsKey(recentCommentListEntity.getPostId())) {
                        //设置帖子标题
                        Post bbsPost = bbsPostMap.get(recentCommentListEntity.getPostId());
                        recentCommentListEntity.setPostTitle(bbsPost.getPostTitle());
                    }
                }
            }
        }
        return recentCommentListEntities;
    }
}
