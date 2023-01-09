package com.ucas.ucastack.task;

import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.PostComment;
import com.ucas.ucastack.service.PostCommentService;
import com.ucas.ucastack.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class PostCommentPipeline implements Pipeline {

    @Autowired
    private PostService postService;
    @Autowired
    private PostCommentService postCommentService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // get Post object
        Post post = resultItems.get("posts");
        // save post in database
        if (post != null) {
            postService.savePost(post);
        }

        // get list of PostComment object
        List<PostComment> comments = resultItems.get("comments");
        if (comments != null) {
            for (PostComment comment : comments) {
                if (comment != null) {
                    // save comment in database
                    postCommentService.savePostComment(comment);
                }
            }
        }
    }
}
