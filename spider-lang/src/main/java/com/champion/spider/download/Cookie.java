package com.champion.spider.download;

/**
 * Created by lang on 2017/8/21.
 */
public class Cookie {
    private String key;
    private String value;
    private String domain;
    private String path;
    private String expires;
    private String secure;//="secure"
    private String HTTPOnly;//="HTTP-Only"

    public Cookie() {
    }

    public Cookie(String domain, String key, String value) {
        this.key = key;
        this.value = value;
        this.domain = domain;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public String getHTTPOnly() {
        return HTTPOnly;
    }

    public void setHTTPOnly(String HTTPOnly) {
        this.HTTPOnly = HTTPOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cookie cookie = (Cookie) o;

        return key.equals(cookie.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
