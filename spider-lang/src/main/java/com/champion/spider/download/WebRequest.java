package com.champion.spider.download;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WebRequest implements Serializable,Comparable<WebRequest> {

    private String url;

    private HttpMethod method;

    private Map<String, String> extras;

    private String  strExtras;

    private String charSet;

    private int priority = 0;

    private String urlAndParam;

    private boolean next;

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    private WebRequest(){}

    public WebRequest(String url) {
        this.url = url;
        this.strExtras="";
        this.extras=new HashMap<>();
        this.next=false;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public WebRequest putExtra(String key, String value) {
        if (extras == null) {
            extras = new HashMap<String, String>();
        }
        extras.put(key, value);
        return this;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getStrExtras() throws UnsupportedEncodingException {
        if (strExtras!=null&&!"".equals(strExtras))
            return strExtras;
        if(extras.keySet().size()!=0){
            for(Map.Entry<String, String> entries:extras.entrySet()){
                if(charSet!=null&&!charSet.equals("")){
                    strExtras+=entries.getKey()+"="+ URLEncoder.encode(entries.getValue(),charSet)+"&";
                }else{
                    strExtras+=entries.getKey()+"="+entries.getValue()+"&";
                }
            }
            strExtras=strExtras.substring(0,strExtras.length()-1);
        }
        return strExtras;
    }

    @Override
    public int compareTo(WebRequest o) {
        if (this.priority < o.priority) {
            return -1;
        } else if (this.priority == o.priority) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getPriority() {
        return priority;
    }

    public String getUrlAndParam() {
        return urlAndParam;
    }

    public void setUrlAndParam(String urlAndParam) {
        this.urlAndParam = urlAndParam;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStrExtras(String strExtras) {
        this.strExtras = strExtras;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCharSet() {
        return charSet;
    }
}
