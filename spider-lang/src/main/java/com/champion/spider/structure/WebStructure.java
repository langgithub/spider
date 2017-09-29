package com.champion.spider.structure;

import java.util.List;

/**
 * Created by root on 2017/8/24.
 */
public class WebStructure {

    private String name;
    private String path;
    private String style;
    private String method;
    private String job;

    private List<ListStructure> dataList;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<ListStructure> getDataList() {
        return dataList;
    }

    public void setDataList(List<ListStructure> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "WebStructure{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", style='" + style + '\'' +
                ", method='" + method + '\'' +
                ", dataList=" + dataList +
                '}';
    }
}
