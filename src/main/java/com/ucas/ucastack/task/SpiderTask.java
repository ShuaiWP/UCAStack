package com.ucas.ucastack.task;

import com.ucas.ucastack.common.Constants;
import com.ucas.ucastack.dao.PostMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class SpiderTask {

    @Autowired
    private PostCommentPipeline postCommentPipeline;

    // start spider every 2 hours
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000)
    public void SpiderTask() {
        Spider.create(new PostProcessor())
                .addUrl(Constants.BASE_LIST_URL + 0)
                .thread(10)
                .setScheduler(new FileCacheQueueScheduler("src/main/resources/url").setDuplicateRemover(new PostDuplicateRemover()))
                .addPipeline(postCommentPipeline)
                .run();
    }
}
