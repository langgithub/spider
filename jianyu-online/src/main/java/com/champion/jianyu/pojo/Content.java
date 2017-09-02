package com.champion.jianyu.pojo;

public class Content {
    private String id;

    private String content;

    public Content(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public Content() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}