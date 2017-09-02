package com.champion.spider.utils;

/**
 * Some constants of Http protocal.
 * 
 * @author xulixiang@champion-credit.com
 * @version 1.0.0
 * @date 2016.5.30
 */
public abstract class HttpConstant {

    public static abstract class Method {

        public static final String GET = "GET";

        public static final String HEAD = "HEAD";

        public static final String POST = "POST";

        public static final String PUT = "PUT";

        public static final String DELETE = "DELETE";

        public static final String TRACE = "TRACE";

        public static final String CONNECT = "CONNECT";

    }

    public static abstract class Header {

        public static final String REFERER = "Referer";

        public static final String USER_AGENT = "User-Agent";
    }

}

