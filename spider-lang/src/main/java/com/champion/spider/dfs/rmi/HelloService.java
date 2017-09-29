package com.champion.spider.dfs.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
 
/**
 * RMI服务接口
 */
public interface HelloService extends Remote {
 
    String sayHello(String name) throws RemoteException;
}