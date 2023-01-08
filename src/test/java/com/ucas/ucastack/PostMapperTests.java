package com.ucas.ucastack;

import org.springframework.test.context.junit4.SpringRunner;

import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.entity.Post;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostMapperTests {

	@Autowired
	private PostMapper bbsPostMapper;

	@Test
	void testInsertResult() {
		Post post1 = new Post();
		post1.setPublishUserId(0L);
		post1.setPostTitle("test");
		post1.setPostContent("test");
		post1.setPostCategoryId(0);
		post1.setPostCategoryName("test");
		Post post2 = new Post();
		post2.setPublishUserId(0L);
		post2.setPostTitle("test");
		post2.setPostContent("test");
		post2.setPostCategoryId(0);
		post2.setPostCategoryName("test");
		int result1 = bbsPostMapper.insertSelective(post1);
		int result2 = bbsPostMapper.insertSelective(post2);
		Long postId1=post1.getPostId();
		Long postId2=post2.getPostId();
		assertEquals(postId1+1, postId2);

		bbsPostMapper.deleteByPrimaryKey(postId1);
		bbsPostMapper.deleteByPrimaryKey(postId2);
	}
}
