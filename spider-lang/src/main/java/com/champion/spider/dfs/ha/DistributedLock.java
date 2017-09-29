package com.champion.spider.dfs.ha;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import sun.rmi.runtime.Log;

import java.util.Collections;
import java.util.List;

/**
 * Created by root on 2017/8/17.
 */
public class DistributedLock {

    private static final Logger LOG=Logger.getLogger(DistributedLock.class);
    private ZooKeeper zk=null;
    private String selfPath;
    private String waitPath;
    private String LOG_PREFIX_OF_THREAD=Thread.currentThread().getName();
    private String GROUP_PATH;
    private String SUB_PATH;
    private Watcher watcher;

    public Watcher getWatcher() {
        return watcher;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

    public DistributedLock(ZooKeeper zk,String GROUP_PATH) {
        this.zk = zk;
        this.GROUP_PATH=GROUP_PATH;
        this.SUB_PATH=GROUP_PATH+"/sub";
    }

    public boolean checkMinPath() throws Exception {
        List<String> subNodes=zk.getChildren(GROUP_PATH,false);
        Collections.sort(subNodes);
        int index=subNodes.indexOf(selfPath.substring(GROUP_PATH.length()+1));
        switch (index){
            case -1:
                LOG.error(LOG_PREFIX_OF_THREAD+"本节点已不在了。。。"+selfPath);
                return false;
            case 0:
                LOG.info(LOG_PREFIX_OF_THREAD+"子节点中，我果然老大"+selfPath);
                return true;
            default:
                this.waitPath=GROUP_PATH+"/"+subNodes.get(index-1);
                LOG.info(LOG_PREFIX_OF_THREAD+"获取子节点中，排在我前面的"+waitPath);
                try{
                    zk.getData(waitPath,this.watcher,new Stat());
                    return false;
                }catch (Exception e){
                    if(zk.exists(waitPath,false)==null){
                        LOG.info(LOG_PREFIX_OF_THREAD+"子节点中，排在我前面的"+waitPath+"已失效，幸福来得太突然");
                        return checkMinPath();
                    }else{
                        throw e;
                    }
                }

        }
    }

    public boolean getLock() throws Exception {
        selfPath=zk.create(SUB_PATH,null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        LOG.info(LOG_PREFIX_OF_THREAD+"创建锁路径："+selfPath);
        if(checkMinPath()){
            return true;
        }
        return false;
    }

    /**
     *
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public boolean createPath(String path,String data) throws KeeperException, InterruptedException {
        if(zk.exists(path,false)==null){
            String cPath = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            LOG.info(LOG_PREFIX_OF_THREAD+"节点创建成功，path:"+cPath+",content"+data);
        }
        return true;
    }

    public void unLock() {
        try{
            if(zk.exists(this.selfPath,false)==null){
                LOG.error(LOG_PREFIX_OF_THREAD+"本节点已不在了...");
                return;
            }
            zk.delete(this.selfPath,-1);
            LOG.info(LOG_PREFIX_OF_THREAD+"删除本节点："+selfPath);
            zk.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getWaitPath(){
        return waitPath;
    }
}
