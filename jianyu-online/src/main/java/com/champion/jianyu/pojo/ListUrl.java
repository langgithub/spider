package com.champion.jianyu.pojo;

public class ListUrl {
    private String id;

    private String area;

    private String areaadd;

    private String detail;

    private String href;

    private String publishtime;

    private String stypeadd;

    private String subtype;

    private String title;

    private String toptype;

    private String type;

    private Integer status;

    private String ts;

    public ListUrl(String id, String area, String areaadd, String detail, String href, String publishtime, String stypeadd, String subtype, String title, String toptype, String type, Integer status, String ts) {
        this.id = id;
        this.area = area;
        this.areaadd = areaadd;
        this.detail = detail;
        this.href = href;
        this.publishtime = publishtime;
        this.stypeadd = stypeadd;
        this.subtype = subtype;
        this.title = title;
        this.toptype = toptype;
        this.type = type;
        this.status = status;
        this.ts = ts;
    }

    public ListUrl() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAreaadd() {
        return areaadd;
    }

    public void setAreaadd(String areaadd) {
        this.areaadd = areaadd == null ? null : areaadd.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime == null ? null : publishtime.trim();
    }

    public String getStypeadd() {
        return stypeadd;
    }

    public void setStypeadd(String stypeadd) {
        this.stypeadd = stypeadd == null ? null : stypeadd.trim();
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype == null ? null : subtype.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getToptype() {
        return toptype;
    }

    public void setToptype(String toptype) {
        this.toptype = toptype == null ? null : toptype.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts == null ? null : ts.trim();
    }
}