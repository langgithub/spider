package com.champion.spider.dfs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by root on 2017/9/5.
 */
public interface SpiderService extends Remote {

    public long start(String path) throws RemoteException;
    public void stop(Long pid) throws RemoteException;
}
