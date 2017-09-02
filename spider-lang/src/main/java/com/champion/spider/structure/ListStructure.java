package com.champion.spider.structure;

import java.util.HashMap;

/**
 * Created by root on 2017/8/30.
 */
public class ListStructure {

    private String pageListReg;
    private HashMap<String,String> pageList;

    public String getPageListReg() {
        return pageListReg;
    }

    public void setPageListReg(String pageListReg) {
        this.pageListReg = pageListReg;
    }

    public HashMap<String, String> getPageList() {
        return pageList;
    }

    public void setPageList(HashMap<String, String> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return "ListStructure{" +
                "pageListReg='" + pageListReg + '\'' +
                ", pageList=" + pageList +
                '}';
    }
}
