package com.ucas.ucastack.dao;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;
// 帖子所对应的mapper接口
public interface PostMapper {
    // 根据postId从数据库删除帖子
    int deleteByPrimaryKey(Long postId);

    // 根据具体的帖子插入记录到数据库，Post里面的所有字段都是有值的
    int insert(Post record);

    // 根据具体的帖子插入记录到数据库，Post里面的字段不一定都有值
    int insertSelective(Post record);

    // 根据postId从数据库中查询某个帖子，返回的Post里面包含了content内容
    Post selectByPrimaryKey(Long postId);

    // 根据传入的postIds查询数据库的多个帖子，返回的单个Post不包含content
    List<Post> selectByPrimaryKeys(@Param("postIds") List<Long> postIds);

    // 根据postId更新帖子，参数Post里面可以有空字段
    int updateByPrimaryKeySelective(Post record);

    // 根据postId更新帖子，参数Post里面的所有字段都是有值的，并且content也是有值的
    int updateByPrimaryKeyWithBLOBs(Post record);

    // 根据postId更新帖子，参数Post里面的所有字段都是有值的，但content也是无值的
    int updateByPrimaryKey(Post record);

    // 根据PageQueryUtil Map参数，统计满足条件的数目
    int getTotalPostsNum(PageQueryUtil pageUtil);

    // 根据PageQueryUtil Map参数，统计满足条件的Post
    List<Post> findPostList(PageQueryUtil pageUtil);

    // 近七天的热议帖子，默认读取6条数据
    List<Post> getHotTopicPostList();

    // 根据userId获得他在period时间段内的帖子
    List<Post> getMyPostList(@Param("userId") Long userId, @Param("period") String period);
}