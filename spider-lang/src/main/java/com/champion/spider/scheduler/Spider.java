package com.champion.spider.scheduler;

import com.champion.spider.download.Downloader;
import com.champion.spider.download.WebClient;
import com.champion.spider.download.WebRequest;
import com.champion.spider.pipeline.ConsolePipeline;
import com.champion.spider.pipeline.Pipeline;
import com.champion.spider.processor.PageProcessor;
import com.champion.spider.thread.CountableThreadPool;
import com.champion.spider.utils.PooledClientFactory;
import com.champion.spider.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import sun.security.provider.ConfigFile;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Spider implements Runnable{

    protected List<Pipeline> pipelines = new ArrayList<Pipeline>();

    protected PageProcessor pageProcessor;

    protected Scheduler scheduler;

    protected String uuid;

    protected Logger logger = Logger.getLogger(getClass());

    protected CountableThreadPool threadPool;

    protected ExecutorService executorService;

    protected PooledClientFactory clientPool;

    protected int threadNum = 1;

    protected AtomicInteger status = new AtomicInteger(status_INIT);

    protected final static int status_INIT = 0;

    protected final static int status_RUNNING = 1;

    protected final static int status_STOPPED = 2;

    protected boolean destroyWhenExit = true;

    protected boolean exitWhenComplete = false;

    private boolean master=true;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private int emptySleepTime = 5000;

    private int waitNewUrlCount = 0;

    public Spider addPipeline(Pipeline pipeline) {
        checkIfRunning();
        this.pipelines.add(pipeline);
        return this;
    }

    public Spider setScheduler(Scheduler scheduler) {
        checkIfRunning();
        this.scheduler=scheduler;
        return this;
    }

    public Spider(SpiderJob spiderJob){
        this.pageProcessor=spiderJob;
        this.pipelines.add(spiderJob);
    }

    public static Spider create(SpiderJob spiderJob) {
        return new Spider(spiderJob);
    }

    protected void initComponent() {
        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        if(scheduler==null){
            this.scheduler=new PriorityScheduler();
        }
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
        if (clientPool == null) {
            clientPool= PooledClientFactory.getInstance(threadNum);
        }
    }

    @Override
    public void run() {
        checkRunningstatus();
        initComponent();
        logger.info("Spider " + getUUID() + " started!");
        while (!Thread.currentThread().isInterrupted() && status.get() == status_RUNNING) {
            WebRequest request = scheduler.poll();
            if (request == null) {
                if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                    break;
                }
                waitNewUrl();
                waitNewUrlCount++;
                if (waitNewUrlCount > 2&&master) {
                    List<WebRequest> webRequests = pageProcessor.seedCreate();
                    if(webRequests==null||webRequests.size()==0){
                        break;
                    }
                    addWebRequest(webRequests);
                }
            } else {
                waitNewUrlCount = 0;
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Downloader downloader=null;
                        try {
                            downloader=clientPool.getClient();
                            Map<String, Object> items = pageProcessor.process(downloader, request);
                            if(null== items){
                                addRequest(request);
                            }else{
                                saveDataToPipline(items,request);
                            }
                        } catch (Exception e) {
                            logger.error("process request " + request + " error", e);
                            addRequest(request);
                        } finally {
                            if(downloader!=null) {
                                try {
                                    clientPool.returnClient(downloader);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            signalNewUrl();
                        }
                    }
                });
            }
        }
        status.set(status_STOPPED);
        if (destroyWhenExit) {
            close();
        }
    }


    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

    private void waitNewUrl() {
        logger.info("暂无种子，请等待>>>>"+waitNewUrlCount);
        newUrlLock.lock();
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    private void saveDataToPipline(Map<String, Object> items,WebRequest request) {
        for (Pipeline pipline:pipelines){
            pipline.save(items,request);
        }
    }

    private void checkRunningstatus() {
        while (true) {
            int statusNow = status.get();
            if (statusNow == status_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (status.compareAndSet(statusNow, status_RUNNING)) {
                break;
            }
        }
    }

    public boolean isSpiderStopped() {
        if (status.get() == status_RUNNING) {
            return false;
        }
        return true;
    }

    private void addRequest(WebRequest request) {
        scheduler.put(request);
    }

    public void close() {
        destroyEach(pageProcessor);
        for (Pipeline pipeline : pipelines) {
            destroyEach(pipeline);
        }
        threadPool.shutdown();
    }

    private void destroyEach(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkIfRunning() {
        if (status.get() == status_RUNNING) {
            throw new IllegalStateException("Spider is already running!");
        }
    }

    public void stop() {
        if (status.compareAndSet(status_RUNNING, status_STOPPED)) {
            logger.info("Spider " + getUUID() + " stop success!");
        } else {
            logger.info("Spider " + getUUID() + " stop fail!");
        }
    }

    public Spider thread(int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    public String getUUID() {
        if (uuid != null) {
            return uuid;
        }
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public Spider addUrl(List<String> urls) {
        for (String url : urls) {
            addRequest(new WebRequest(url));
        }
        signalNewUrl();
        return this;
    }
    public Spider addWebRequest(List<WebRequest> requests) {
        for (WebRequest request : requests) {
            addRequest(request);
        }
        signalNewUrl();
        return this;
    }
}

