
package com.ucas.ucastack;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.service.PostService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTests {

	@Autowired
	private PostService postService;

	@Test
	void testInsertResult() {
		Post post1 = new Post();
		post1.setPublishUserId(1L);
		post1.setPostTitle("test");
		post1.setPostContent("test");
		post1.setPostCategoryId(1);
		post1.setPostCategoryName("提问");
		int result1 = postService.savePost(post1);
		assertNotEquals(result1, 0);
		Long postId1=post1.getPostId();

		postService.delPost(1L, postId1);
	}
}
