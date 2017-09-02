package com.champion.jianyu.pojo;

public class ListUrlCondition {
    private String keyword;

    private String area;

    private String subtype;

    private String publishtime;

    private Integer pageNum;

    private Integer total;

    private String ts;

    private Integer status;

    public ListUrlCondition(String keyword, String area, String subtype, String publishtime, Integer pageNum, Integer total, String ts, Integer status) {
        this.keyword = keyword;
        this.area = area;
        this.subtype = subtype;
        this.publishtime = publishtime;
        this.pageNum = pageNum;
        this.total = total;
        this.ts = ts;
        this.status = status;
    }

    public ListUrlCondition() {
        super();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype == null ? null : subtype.trim();
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime == null ? null : publishtime.trim();
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts == null ? null : ts.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ListUrlCondition{" +
                "keyword='" + keyword + '\'' +
                ", area='" + area + '\'' +
                ", subtype='" + subtype + '\'' +
                ", publishtime='" + publishtime + '\'' +
                ", pageNum=" + pageNum +
                ", total=" + total +
                ", ts='" + ts + '\'' +
                ", status=" + status +
                '}';
    }
}