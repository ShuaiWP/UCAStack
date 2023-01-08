package com.ucas.ucastack.service;



import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import com.ucas.ucastack.dao.elasticsearch.PostRepository;
import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.util.PageQueryUtil;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	@Lazy
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	/**
	 * insert a post into elasticsearch server
	 * 
	 * @param post
	 */
	public void savePost(Post post) {
		elasticsearchRestTemplate.save(post);
	}

	/**
	 * delete a post from elasticsearch server
	 * 
	 * @param post
	 */
	public void deleteByPostId(Long postId) {
		postRepository.deleteById(postId);
	}

	public void updatePost(Post post) {
		elasticsearchRestTemplate.save(post);
	}

	/**
	 * search posts from elasticsearch server
	 * 
	 * @param query params
	 * @return list of posts
	 */
	public List<Post> searchPost(PageQueryUtil queryUtil) {
		// build query condition
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		if (queryUtil.containsKey("keyword")) {
			queryBuilder.withQuery(QueryBuilders.multiMatchQuery(queryUtil.get("keyword"), "postTitle", "postContent"));
		} else {
			queryBuilder.withQuery(QueryBuilders.matchAllQuery());
		}
		if (queryUtil.containsKey("categoryId")) {
			queryBuilder.withFilter(QueryBuilders.termQuery("postCategoryId", queryUtil.get("categoryId")));
		}
		if (queryUtil.containsKey("period")) {
			LocalDate date = LocalDate.now();
			if (queryUtil.get("period") == "hot7") {
				date = LocalDate.now().minusDays(7);
			} else if (queryUtil.get("period") == "hot30") {
				date = LocalDate.now().minusDays(30);
			}
			Date period = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			String strDateFormat = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
			queryBuilder
					.withFilter(QueryBuilders.rangeQuery("createTime").format("yyyy-MM-dd HH:mm:ss").gte(sdf.format(period)));
		}
		if (queryUtil.containsKey("orderBy")) {
			if (queryUtil.get("orderBy") == "new") {
				queryBuilder.withSorts((SortBuilders.fieldSort("createTime").order(SortOrder.DESC)));
			} else if (queryUtil.get("orderBy") == "comment") {
				queryBuilder.withSorts(SortBuilders.fieldSort("postComments").order(SortOrder.DESC));
			}
		} else {
			queryBuilder.withSorts(SortBuilders.fieldSort("lastUpdateTime").order(SortOrder.DESC));
			queryBuilder.withSorts(SortBuilders.fieldSort("postViews").order(SortOrder.DESC));
			queryBuilder.withSorts(SortBuilders.fieldSort("postComments").order(SortOrder.DESC));
		}
		SearchHits<Post> posts = elasticsearchRestTemplate.search(queryBuilder.build(), Post.class);
		return posts.stream().map(SearchHit::getContent).collect(Collectors.toList());
	}
}


