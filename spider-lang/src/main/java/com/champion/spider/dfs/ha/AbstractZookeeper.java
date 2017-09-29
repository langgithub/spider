package com.champion.spider.dfs.ha;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by root on 2017/8/17.
 */
public class AbstractZookeeper implements Watcher {

    private static final Logger LOG=Logger.getLogger(AbstractZookeeper.class);
    protected ZooKeeper zooKeeper;
    protected CountDownLatch countDownLatch=new CountDownLatch(1);

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
    public void close() throws InterruptedException {
        zooKeeper.close();
    }
}
