package com.ucas.ucastack;

import org.springframework.test.context.junit4.SpringRunner;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.util.PageQueryUtil;
import com.ucas.ucastack.service.ElasticsearchService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElasticsearchServiceTests {

	@Autowired
	private ElasticsearchService elasticsearchService;

	@Test
	void testTimeConvert() {
		LocalDate date = LocalDate.now();
		date = LocalDate.now().minusDays(7);
		Date period = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String strDateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		sdf.format(period);
		System.out.println("period" + period);
	}

	@Test
	void testSearch() {
		Post post1 = new Post();
		post1.setPostId(Long.MAX_VALUE);
		post1.setPostTitle("test wy");
		post1.setCreateTime(new Date());
		elasticsearchService.savePost(post1);
		Post post2 = new Post();
		post2.setPostId(Long.MAX_VALUE - 1);
		post2.setPostTitle("test wy");
		LocalDate date = LocalDate.now();
		date = LocalDate.now().minusDays(8);
		Date postdate2 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		post2.setCreateTime(postdate2);
		elasticsearchService.savePost(post2);
		Map<String, Object> params = new HashMap();
		params.put("page", 0);
		params.put("limit", 10);
		params.put("keyword", "test");
		params.put("orderBy", "new");

		PageQueryUtil pageUtil1 = new PageQueryUtil(params);
		List<Post> posts = elasticsearchService.searchPost(pageUtil1);

		assertEquals(posts.size(), 2);
		assert(posts.get(0).getPostId() == post1.getPostId());

		params.put("period", "hot7");

		PageQueryUtil pageUtil2 = new PageQueryUtil(params);
		posts = elasticsearchService.searchPost(pageUtil2);

		assertEquals(posts.size(), 1);

		Post postResult = posts.get(0);

		assertEquals(post1.getPostId(), postResult.getPostId());
		assertEquals(post1.getPostTitle(), postResult.getPostTitle());

		elasticsearchService.deleteByPostId(post1.getPostId());
		elasticsearchService.deleteByPostId(post2.getPostId());

	}
}
