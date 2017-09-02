package com.champion.spider.pipeline;

import com.champion.spider.download.WebRequest;
import org.apache.log4j.Logger;

import java.util.Map;
public class ConsolePipeline implements Pipeline {

    private static final Logger LOG=Logger.getLogger(ConsolePipeline.class);

    @Override
    public void save(Map<String, Object> items, WebRequest request) {
        LOG.info("this is ConsolePipeline");
        items.forEach((k,v)->LOG.info(k+ ":\t" + v));
    }
}
