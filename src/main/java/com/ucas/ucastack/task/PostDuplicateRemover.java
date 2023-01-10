package com.ucas.ucastack.task;

import com.ucas.ucastack.common.Constants;
import org.apache.commons.io.IOUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PostDuplicateRemover implements DuplicateRemover {
    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap());
    private final String fileUrlAllName = "src/main/resources/url/gkder.ucas.ac.cn.urls.txt";

    public PostDuplicateRemover() {
    }

    public boolean isDuplicate(Request request, Task task) {
        // do not add entry url to the urls.txt
        if (request.getUrl().contains(Constants.BASE_LIST_URL)) {
            return false;
        }

        String line;
        BufferedReader fileUrlReader = null;
        try {
            fileUrlReader = new BufferedReader(new FileReader(fileUrlAllName));
            while ((line = fileUrlReader.readLine()) != null) {
                urls.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileUrlReader != null) {
                IOUtils.closeQuietly(fileUrlReader);
            }
        }

        if (urls.contains(request.getUrl())) {
            return true;
        }

        return !this.urls.add(this.getUrl(request));
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    public void resetDuplicateCheck(Task task) {
        this.urls.clear();
    }

    public int getTotalRequestsCount(Task task) {
        return this.urls.size();
    }

}
