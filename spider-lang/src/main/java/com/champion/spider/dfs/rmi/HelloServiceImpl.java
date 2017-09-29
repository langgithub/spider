package com.champion.spider.dfs.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
 
/**
 * RMI服务实现
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
 
    protected HelloServiceImpl() throws RemoteException {
    }
 
    @Override
    public String sayHello(String name) throws RemoteException {
        return String.format("Hello %s", name);
    }
}