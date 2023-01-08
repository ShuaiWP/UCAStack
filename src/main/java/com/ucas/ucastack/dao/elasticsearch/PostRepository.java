package com.ucas.ucastack.dao.elasticsearch;

import com.ucas.ucastack.entity.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ElasticsearchRepository<Post, Long> {

}
