package com.champion.spider.dfs.rmi;

/**
 * RMI客户端
 */
public class Client {
 
    public static void main(String[] args) throws Exception {
        ServiceConsumer consumer = new ServiceConsumer();
        SpiderService spiderService = consumer.lookup();
        spiderService.start("cmd /c start E:\\房源\\58\\start.bat");
        Thread.sleep(3000);
    }
}