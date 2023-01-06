package com.ucas.ucastack.task;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class PostCommentPipeline implements Pipeline {

    @Autowired
    private PostService postService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // get Post object
        Post post = resultItems.get("posts");

        if (post != null) {
            postService.savePost(post);
        }
    }
}
