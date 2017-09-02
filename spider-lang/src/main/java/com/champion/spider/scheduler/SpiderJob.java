package com.champion.spider.scheduler;

import com.champion.spider.pipeline.Pipeline;
import com.champion.spider.processor.PageProcessor;

/**
 * Created by root on 2017/8/24.
 */
public interface SpiderJob extends PageProcessor,Pipeline {
}
