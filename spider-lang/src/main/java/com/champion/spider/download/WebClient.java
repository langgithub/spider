package com.champion.spider.download;


import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by lang on 2017/8/19.
 */
public class WebClient implements Downloader{

    private static Logger LOG=Logger.getLogger(WebClient.class);

    private HttpURLConnection httpURLConnection;

    private Proxy proxy;

    private WebRequest request;

    private int clientTimeOut=5000;

    private int readTimeOut=30000;

    private String charSet="utf-8";

    private  Map<String, String> headler;

    private List<Cookie> cookies;

    private Page page;

    public WebClient(){
        this.headler=new HashMap<>();
        this.cookies=new ArrayList<>();
    }

    @Override
    public void setProxy(Proxy proxy) {
        this.proxy=proxy;
    }

    @Override
    public void setHeader(String key,String value) {
        if (key!=null&&value!=null)
            headler.put(key,value);
    }

    @Override
    public Page get(String url) throws IOException {
        request=new WebRequest(url);
        request.setMethod(HttpMethod.GET);
        if(proxy==null){
            httpURLConnection= (HttpURLConnection) new URL(request.getUrl()).openConnection();
        }else {
            httpURLConnection= (HttpURLConnection) new URL(request.getUrl()).openConnection(proxy);
        }
        //流读入，默认true
        httpURLConnection.setDoInput(true);
        //post请求不能使用缓存，默认true
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setConnectTimeout(clientTimeOut);
        httpURLConnection.setReadTimeout(readTimeOut);
        httpURLConnection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charSet));
        praseHeadler(httpURLConnection);
        int len=0;
        char[] buffer=new char[1024];
        StringBuffer strBuffer=new StringBuffer();
        while ((len=bufferedReader.read(buffer))!=-1){
            strBuffer.append(buffer,0,len);
        }
        bufferedReader.close();
        page=new Page(strBuffer);
        return page;
    }

    @Override
    public Page get(WebRequest request) throws IOException {
        Map<String, String> cookies = request.getCookies();
        if(cookies!=null){
            for (Map.Entry<String,String> cook:cookies.entrySet()){
                String cookies1 = headler.get("cookies");
                cookies1=cookies1+";"+cook.getKey()+"="+cook.getValue();
                headler.put("cookies",cookies1);
            }
        }
        if(proxy==null){
            httpURLConnection= (HttpURLConnection) new URL(request.getUrl()).openConnection();
        }else {
            httpURLConnection= (HttpURLConnection) new URL(request.getUrl()).openConnection(proxy);
        }
        if(request.getMethod()==HttpMethod.POST){
            httpURLConnection.setDoOutput(true);
        }
        httpURLConnection.setDoInput(true);
        //post请求不能使用缓存，默认true
        httpURLConnection.setUseCaches(false);
        if(headler!=null) {
            for (Map.Entry<String, String> head:headler.entrySet()) {
                httpURLConnection.setRequestProperty(head.getKey(),head.getValue());
            }
        }
        httpURLConnection.setConnectTimeout(clientTimeOut);
        httpURLConnection.setReadTimeout(readTimeOut);
        httpURLConnection.connect();
        if(request.getMethod()==HttpMethod.POST){
            if(request.getStrExtras()==null){
                LOG.info("post 请求参数为空，请检查！！！");
                throw new NullPointerException("post 请求参数为空，请检查！！！");
            }
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(request.getStrExtras().getBytes());
            outputStream.flush();
            outputStream.close();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charSet));
        int len=0;
        char[] buffer=new char[1024];
        StringBuffer strBuffer=new StringBuffer();
        while ((len=bufferedReader.read(buffer))!=-1){
            strBuffer.append(buffer,0,len);
        }
        bufferedReader.close();
        page=new Page(strBuffer);
        return page;
    }

    private void praseHeadler(HttpURLConnection httpURLConnection) {
        String key="";
        cookies=new ArrayList<>();
        for(int i = 1; (key = httpURLConnection.getHeaderFieldKey(i)) != null; i++){
            if(key.equalsIgnoreCase("set-cookie")){
                String cookieVal = httpURLConnection.getHeaderField(i);
                String[] splitCookies = cookieVal.split(";");
                Cookie cookie=new Cookie();
                for (String keyCookie:splitCookies) {
                    if(keyCookie.contains("=")){
                        String[] key_value = keyCookie.split("=");
                        if (keyCookie.contains("path")){
                            cookie.setPath(key_value[1]);
                        }else if(keyCookie.contains("expires")){
                            cookie.setExpires(key_value[1]);
                        }else if(keyCookie.contains("domain")){
                            cookie.setDomain(key_value[1]);
                        }else {
                            cookie.setKey(key_value[0]);
                            cookie.setValue(key_value[1]);
                        }
                    }
                    if(keyCookie.contains("secure")){
                        cookie.setSecure("secure");
                    }else if(keyCookie.contains("HTTP-Only")){
                        cookie.setHTTPOnly("HTTP-Only");
                    }
                }
                for (int j=0;j<cookies.size();j++) {
                    Cookie item = cookies.get(j);
                    if(item.equals(cookie)){
                        item.setValue(cookie.getValue());
                        item.setDomain(cookie.getDomain());
                        item.setExpires(cookie.getExpires());
                        item.setPath(cookie.getPath());
                        item.setSecure(cookie.getSecure());
                        item.setHTTPOnly(cookie.getHTTPOnly());
                    }else{
                        cookies.add(cookie);
                        j++;
                    }
                }
                if(cookies.size()==0){
                    cookies.add(cookie);
                }
            }
        }

    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookie(Cookie cookie){
        cookies.add(cookie);
    }


    @Override
    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    @Override
    public void setClientTimeOut(int clientTimeOut) {
        this.clientTimeOut = clientTimeOut;
    }
}
