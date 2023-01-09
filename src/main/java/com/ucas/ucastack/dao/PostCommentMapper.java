package com.ucas.ucastack.dao;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.PostComment;
import com.ucas.ucastack.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 帖子评论的mapper
public interface PostCommentMapper {
    // 根据评论ID删除对应评论
	int deleteByPrimaryKey(Long commentId);
	
	// 将对应的评论记录到数据库
    int insert(PostComment record);

	// 将对应的评论记录到数据库，但record中不一定所有字段都是有值的
    int insertSelective(PostComment record);

    // 将对应的评论记录到数据库，但record中不一定所有字段都是有值的
    int insertIncrement(PostComment record);

	// 根据评论ID查询对应评论
    PostComment selectByPrimaryKey(Long commentId);

	// 根据评论ID更新对应评论，但record中不一定所有字段都是有值的
    int updateByPrimaryKeySelective(PostComment record);

	// 根据评论ID更新对应评论
    int updateByPrimaryKey(PostComment record);

	// 统计评论数目
    int getTotalComments(PageQueryUtil pageUtil);

	// 返回需求的所有评论
    List<PostComment> findCommentList(PageQueryUtil pageUtil);

	// 根据用户ID返回该用户近期的所有评论
    List<PostComment> getRecentCommentListByUserId(@Param("userId") Long userId);

}