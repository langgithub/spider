package com.champion.spider.processor;

import com.champion.spider.download.Downloader;
import com.champion.spider.download.WebRequest;

import java.util.List;
import java.util.Map;

public interface PageProcessor {


    public Map<String,Object> process(Downloader client, WebRequest request);

    public List<WebRequest> seedCreate();


}

