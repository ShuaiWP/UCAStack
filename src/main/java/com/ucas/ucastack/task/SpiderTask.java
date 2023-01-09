package com.ucas.ucastack.task;

import com.ucas.ucastack.dao.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

@Component
public class SpiderTask {

    @Autowired
    private PostCommentPipeline postCommentPipeline;

    // start spider every 6 hours
    @Scheduled(fixedDelay = 6 * 60 * 60 * 1000)
    public void SpiderTask() {
        Spider.create(new PostProcessor())
                .addUrl("https://gkder.ucas.ac.cn/api/discussions?include=user%2ClastPostedUser%2Ctags%2Ctags.parent%2CfirstPost%2CrecipientUsers%2CrecipientGroups&sort&page%5Boffset%5D=0")
                .thread(10)
                .setScheduler(new FileCacheQueueScheduler("src/main/resources/url"))
                .addPipeline(postCommentPipeline)
                .run();
    }
}
