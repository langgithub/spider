package com.champion.spider.pipeline;

import com.champion.spider.download.WebClient;
import com.champion.spider.download.WebRequest;

import java.util.Map;

public interface Pipeline {

    public void save(Map<String,Object> items, WebRequest request);
}

