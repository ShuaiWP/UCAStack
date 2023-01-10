package com.ucas.ucastack.task;

import com.ucas.ucastack.common.Constants;
import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.PostComment;
import com.ucas.ucastack.service.PostService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PostProcessor implements PageProcessor {

    private Site mySite = Site.me()
            .addCookie(Constants.COOKIE_KEY1, Constants.COOKIE_VALUE1)
            .addCookie(Constants.COOKIE_KEY2, Constants.COOKIE_VALUE2)
            .addCookie(Constants.COOKIE_KEY3, Constants.COOKIE_VALUE3)
            .addCookie(Constants.COOKIE_KEY4, Constants.COOKIE_VALUE4)
            .addCookie(Constants.COOKIE_KEY5, Constants.COOKIE_VALUE5)
            .setRetryTimes(3)
            .setCharset("utf-8");

    @Override
    public void process(Page page) {
        // analyze current page url
        String pageUrl = page.getUrl().get();
        if (pageUrl.contains(Constants.BASE_LIST_URL)) {      // current page contains list of posts
            // top 20 most hot posts
            // get the id of posts
            String jsonText = page.getJson().get();
            JsonPathSelector jsonPathSelector = new JsonPathSelector("$.data[*].id");
            List<String> ids = jsonPathSelector.selectList(jsonText);
            // add target post pages into TargetRequests
            for (String id: ids) {
                page.addTargetRequest(Constants.BASE_POST_URL + id);
            }

        } else {
            String jsonText = page.getJson().get();
            JsonPathSelector jsonPathSelector;

            // get post id
            jsonPathSelector = new JsonPathSelector("$.data.id");
            String postId = jsonPathSelector.select(jsonText);
            // get all comments
            jsonPathSelector = new JsonPathSelector("$.included[?(@.type=='posts')].attributes.contentHtml");
            List<String> comments = jsonPathSelector.selectList(jsonText);
            // get create time of all comments
            jsonPathSelector = new JsonPathSelector("$.included[?(@.type=='posts')].attributes.createdAt");
            List<String> commentCreateDates = jsonPathSelector.selectList(jsonText);

            assert comments.size() == commentCreateDates.size();

            List<String> usefulComments = new ArrayList<>();        // list to store "useful" comments
            List<Date> usefulCreateTime = new ArrayList<>();        // list to store "useful" create time
            for (int i = 0; i < comments.size(); i++) {
                // filter the useful comment content (comments that are not reply)
                Html html = new Html(comments.get(i));
                if (html.css("a.PostMention").get() == null) {
                    usefulComments.add(comments.get(i));
                    // get the creation time of this "useful" comment
                    usefulCreateTime.add(string2Date(commentCreateDates.get(i)));
                }
            }

            List<PostComment> commentValues = new ArrayList<>();
            // parse json to create entities of Comment
            for (int i = 1; i < usefulComments.size(); i++) {
                PostComment postComment = new PostComment();
                // set comment id ("-1" means the id is set to the max comment_id in database plus 1)
                postComment.setCommentId(-1L);
                // set post id
                postComment.setPostId(Long.parseLong(postId));
                // set comment user id (uid=1 means the "tourist")
                postComment.setCommentUserId(1L);
                // set comment body
                postComment.setCommentBody(usefulComments.get(i));
                // set parent comment user id (always 0, meaning no parent)
                postComment.setParentCommentUserId(0L);
                // set comment create time
                postComment.setCommentCreateTime(usefulCreateTime.get(i));
                // set whether the comment is deleted (always 0, meaning not deleted)
                postComment.setIsDeleted(new Byte("0"));

                commentValues.add(postComment);
            }

            // parse json to create an entity of Post
            Post post = new Post();
            // set post id
            post.setPostId(Long.parseLong(postId));
            // set publisher id (uid=1 means the "tourist")
            post.setPublishUserId(1L);
            // get post title
            jsonPathSelector = new JsonPathSelector("$.data.attributes.title");
            String postTitle = jsonPathSelector.select(jsonText);
            post.setPostTitle(postTitle);
            // set post category id ("1" for "question")
            post.setPostCategoryId(1);
            // set post category name ("question")
            post.setPostCategoryName("提问");
            // set post status ("1" for "approval")
            post.setPostStatus(new Byte("1"));
            // get number of post views
            jsonPathSelector = new JsonPathSelector("$.data.attributes.viewCount");
            String views = jsonPathSelector.select(jsonText);
            post.setPostViews(Long.parseLong(views));
            // get number of post comments
            post.setPostViews((long) (usefulComments.size() - 1));
            // set number of post collects (always 0)
            post.setPostCollects(0L);
            // get last update time
            jsonPathSelector = new JsonPathSelector("$.data.attributes.lastPostedAt");
            String lastUpdate = jsonPathSelector.select(jsonText);
            Date update = string2Date(lastUpdate);
            post.setLastUpdateTime(update);
            // get post create time
            jsonPathSelector = new JsonPathSelector("$.data.attributes.createdAt");
            String createTime = jsonPathSelector.select(jsonText);
            Date create = string2Date(createTime);
            post.setLastUpdateTime(create);
            // get post content
            // the first comment is the content of the post
            post.setPostContent(comments.get(0));

            // save in ResultItems
            page.putField("posts", post);
            System.out.println(commentValues);
            page.putField("comments", commentValues);
        }

    }

    private Date string2Date(String isoDate) {
        LocalDateTime date = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // "GMT+8" —— China timezone
        return Date.from(date.atZone(ZoneId.of("GMT+8")).toInstant());
    }

    @Override
    public Site getSite() {
        return this.mySite;
    }
}
