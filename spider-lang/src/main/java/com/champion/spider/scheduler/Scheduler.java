package com.champion.spider.scheduler;

import com.champion.spider.download.WebRequest;

public interface Scheduler {

    public void put(WebRequest request);

    public WebRequest poll();

}

