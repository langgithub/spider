package com.champion.spider.pipeline;

import com.champion.spider.download.Downloader;
import com.champion.spider.download.Page;
import com.champion.spider.download.WebRequest;

import java.util.List;

public interface Pipeline {

    List<WebRequest> seedCreate();

    Downloader setClientOption(Downloader client);

    boolean checkPage(Page page);

    void save(List<Object> list, WebRequest request);
}

