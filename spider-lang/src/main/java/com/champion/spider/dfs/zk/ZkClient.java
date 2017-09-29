package com.champion.spider.dfs.zk;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by root on 2017/8/17.
 */
public class ZkClient implements Watcher {

    private static final Logger LOG=Logger.getLogger(ZkClient.class);
    private static ZkClient zkClient=null;
    protected ZooKeeper zooKeeper=null;
    protected CountDownLatch countDownLatch=new CountDownLatch(1);
    private static  final  String CONNECTION_STRING="192.168.205.11:2181";
    private static  final  int SESSION_TIMEOUT=10000;

    private ZkClient(){
    }

    public static ZkClient getInstance() throws IOException, InterruptedException {
        //双重锁，粒度小
        if(zkClient==null){
            synchronized (ZkClient.class){
                if(zkClient==null){
                    zkClient=new ZkClient();
                }
            }
        }
        return zkClient;
    }

    public ZooKeeper connect(String hosts,int SESSIN_TIMEOUT) throws IOException, InterruptedException {
        zooKeeper=new ZooKeeper(hosts,SESSIN_TIMEOUT,this);
        countDownLatch.await();
        LOG.info("AbstractZookeeper.connct()");
        return zooKeeper;
    }


    public void process(WatchedEvent event) {
        if(event.getState()== Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }

    public boolean exists(String path){
        boolean falg=false;
        try {
            if(zooKeeper.exists(path,false)!=null){
                falg=true;
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return falg;
    }

    // 创建 ZNode
    public void createNode(String rootPath,String url) {
        try {
            byte[] data = url.getBytes();
            String path = zooKeeper.create(rootPath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);    // 创建一个临时性且有序的 ZNode
            LOG.info("create zookeeper node ("+path+" => "+url+")");
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }
}
