package com.champion.spider.dfs.ha;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;


/**
 * Created by root on 2017/8/17.
 */
public class LockWatcher implements Watcher {

    private static final Logger LOG=Logger.getLogger(LockWatcher.class);
    private String waitPath;
    private DistributedLock distributedLock;
    private DoTemplate doTemplate;

    public LockWatcher(DistributedLock distributedLock, DoTemplate doTemplate) {
        this.distributedLock = distributedLock;
        this.doTemplate = doTemplate;
    }

    public void process(WatchedEvent event) {

        if(event.getType()==Event.EventType.NodeDeleted&&event.getPath().equals(distributedLock.getWaitPath())){
            LOG.info(Thread.currentThread().getName()+"收到情报，排我前面的家伙已掉，我是不是可以出山了");
            try {
                if(distributedLock.checkMinPath()){
                    doSomeThing();
                    distributedLock.unLock();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void doSomeThing() {
        LOG.info(Thread.currentThread().getName()+"获取锁成功，赶紧干活");
        doTemplate.dodo();
        TestLock.threadSemphore.countDown();
    }
}
