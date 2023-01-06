package com.ucas.ucastack.task;

import com.ucas.ucastack.dao.PostMapper;
import com.ucas.ucastack.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class PostProcessor implements PageProcessor {

    @Autowired
    private PostMapper postMapper;

    private Site mySite = Site.me()
            .addCookie("_abfpc", "0062d41a0ac87a0c837f2a91546043b3af70760a_2.0")
            .addCookie("cna", "efc73112ebefda929cbd400e16986e3e")
            .addCookie("sepuser", "\"bWlkPWExMGViNGQyLWI1MGEtNGU2Mi04YWUyLWRiYTc4ZTA4YjVjNA==  \"")
            .addCookie("flarum_remember", "UPk8qXxO8Fst7eDH9rv9hIWvPMk7PUGuhpntCZvz")
            .addCookie("flarum_session", "znDPKjFskuffohqlzOb8aJiqgfvLCOV9gQsHPcQt")
            .setRetryTimes(3)
            .setCharset("utf-8");
    private static final String baseListUrl = "https://gkder.ucas.ac.cn/api/discussions?include=user%2ClastPostedUser%2Ctags%2Ctags.parent%2CfirstPost%2CrecipientUsers%2CrecipientGroups&sort&page%5Boffset%5D=";
    private static final String basePostUrl = "https://gkder.ucas.ac.cn/api/discussions/";


    @Override
    public void process(Page page) {
        // analyze current page url
        String pageUrl = page.getUrl().get();
        if (pageUrl.contains(baseListUrl)) {      // current page contains list of posts
            // get the most hot 100 posts
            // add other list of pages into TargetRequests
            for (int i = 20; i < 100; i += 20) {
                page.addTargetRequest(baseListUrl + i);
            }

            // get the id of posts
            String jsonText = page.getJson().get();
            JsonPathSelector jsonPathSelector = new JsonPathSelector("$.data[*].id");
            List<String> ids = jsonPathSelector.selectList(jsonText);
            // add target post pages into TargetRequests
            for (String id: ids) {
                page.addTargetRequest(basePostUrl + id);
            }

        } else {
            String jsonText = page.getJson().get();
            // get all comments
            JsonPathSelector jsonPathSelector = new JsonPathSelector("$.included[?(@.type=='posts')].attributes.contentHtml");
            List<String> comments = jsonPathSelector.selectList(jsonText);

            // parse json to create an entity of Post
            Post post = new Post();
            // get post id
            jsonPathSelector = new JsonPathSelector("$.data.id");
            String id = jsonPathSelector.select(jsonText);
            post.setPostId(Long.parseLong(id));
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
            // TODO: some comments needed to be removed, the number here is for test
            jsonPathSelector = new JsonPathSelector("$.data.attributes.commentCount");
            String commentCount = jsonPathSelector.select(jsonText);
            post.setPostViews(Long.parseLong(commentCount));
            // set number of post collects (always 0)
            post.setPostCollects(0L);
            // get last update time
            jsonPathSelector = new JsonPathSelector("$.data.attributes.lastPostedAt");
            String lastUpdate = jsonPathSelector.select(jsonText);
            LocalDateTime date = LocalDateTime.parse(lastUpdate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            Date update = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
            post.setLastUpdateTime(update);
            // get post create time
            jsonPathSelector = new JsonPathSelector("$.data.attributes.createdAt");
            String createTime = jsonPathSelector.select(jsonText);
            date = LocalDateTime.parse(createTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            Date create = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
            System.out.println(create);
            post.setLastUpdateTime(create);
            // get post content
            // the first comment is the content of the post
            post.setPostContent(comments.get(0));

            // save in ResultItem
            page.putField("posts", post);
        }

    }

    @Override
    public Site getSite() {
        return this.mySite;
    }
}
