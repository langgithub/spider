package com.champion.spider.structure;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 2017/8/24.
 */
public class Structure {


    public List<Map<String,Object>> webStructure;

    public List<Map<String, Object>> getWebStructure() {
        return webStructure;
    }

    public void setWebStructure(List<Map<String, Object>> webStructure) {
        this.webStructure = webStructure;
    }

    @Override
    public String toString() {
        return "structure{" +
                "webStructure=" + webStructure +
                '}';
    }
}
