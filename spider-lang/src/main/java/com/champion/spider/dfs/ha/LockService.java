package com.champion.spider.dfs.ha;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by root on 2017/8/17.
 */
public class LockService {
    private static final Logger LOG=Logger.getLogger(LockService.class);

    private String CONNECTION_STRING;
    private static  final  int THREAD_NUM=10;
    public  static CountDownLatch threadSemphore=new CountDownLatch(THREAD_NUM);
    private String GROUP_PATH;
    private static  final  int SESSION_TIMEOUT=10000;
    AbstractZookeeper az=new AbstractZookeeper();

    public LockService(String CONNECTION_STRING, String GROUP_PATH) {
        this.CONNECTION_STRING = CONNECTION_STRING;
        this.GROUP_PATH = GROUP_PATH;
    }

    public void  doService(DoTemplate doTemplate){
        try {
            ZooKeeper zk=az.connect(CONNECTION_STRING,SESSION_TIMEOUT);
            DistributedLock dc=new DistributedLock(zk,GROUP_PATH);
            LockWatcher lw=new LockWatcher(dc,doTemplate);
            dc.setWatcher(lw);
            dc.createPath(GROUP_PATH,"该节点由线程"+Thread.currentThread().getName()+"创建");
            boolean rs=dc.getLock();
            if(rs==true){
                lw.doSomeThing();
                dc.unLock();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
