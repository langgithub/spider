package com.champion.spider.download;

import java.io.IOException;
import java.net.Proxy;

public interface Downloader {

    public Page get(String url) throws IOException;
    public Page get(WebRequest request) throws IOException;
    public void setProxy(Proxy proxy);
    public void setHeader(String key,String value);
    public void setReadTimeOut(int readTimeOut);
    public void setClientTimeOut(int clientTimeOut);

}
