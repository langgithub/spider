package com.champion.spider.dfs.ha;

import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * Created by root on 2017/8/17.
 */
public class TestLock {

    private static final Logger LOG=Logger.getLogger(TestLock.class);

    private static final String CONNECTION_STRING="192.168.205.11:2181";
    public  static final int THREAD_NUM=10;
    public  static CountDownLatch threadSemphore=new CountDownLatch(THREAD_NUM);
    private static  final  String GROUP_PATH="/disLocks";
    private static  final  String SUB_PATH="/disLocks/sub";
    private static  final  int SESSION_TIMEOUT=10000;

    public static  void  main(String[] agrs){
        for (int i=0;i<THREAD_NUM;i++){
            final int threadId=i;
            new Thread(){
                @Override
                public void run() {

                    new LockService(GROUP_PATH,CONNECTION_STRING).doService(new DoTemplate() {
                        public void dodo() {
                            LOG.info("我要修改一个文件......"+threadId);
                        }
                    });
                }
            }.start();
        }
        try{
            threadSemphore.await();
            LOG.info("所有线程运行结束");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
