package com.champion.spider.dfs.rmi;

/**
 * 服务发布
 */
public class Server {
 
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("please using command: java Server <rmi_host> <rmi_port>");
            System.exit(-1);
        }
 
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        ServiceProvider provider = new ServiceProvider();
        SpiderService spiderService=new SpiderServiceImpl();
        provider.publish(spiderService, host, port);
 
        Thread.sleep(Long.MAX_VALUE);
    }
}