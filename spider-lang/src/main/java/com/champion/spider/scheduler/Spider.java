package com.champion.spider.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.champion.spider.dfs.ha.DoTemplate;
import com.champion.spider.dfs.ha.LockService;
import com.champion.spider.download.Downloader;
import com.champion.spider.download.Page;
import com.champion.spider.download.ProxySet;
import com.champion.spider.download.WebRequest;
import com.champion.spider.pipeline.Pipeline;
import com.champion.spider.selector.Order;
import com.champion.spider.selector.Selectable;
import com.champion.spider.structure.ListStructure;
import com.champion.spider.structure.SpiderStructure;
import com.champion.spider.structure.WebStructure;
import com.champion.spider.thread.CountableThreadPool;
import com.champion.spider.utils.PooledClientFactory;
import com.champion.spider.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class Spider implements Runnable{

    protected Logger logger = Logger.getLogger(getClass());

    protected List<Pipeline> pipelines = new ArrayList<Pipeline>();

    protected Scheduler scheduler;

    protected CountableThreadPool threadPool;

    protected ExecutorService executorService;

    protected PooledClientFactory clientPool;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private CountDownLatch countDownLatch=new CountDownLatch(1);

    protected AtomicInteger status = new AtomicInteger(status_INIT);

    private String CONNECTION_STRING="192.168.205.11:2181";

    private String GROUP_PATH="/disLocks";

    protected final static int status_INIT = 0;

    protected final static int status_RUNNING = 1;

    protected final static int status_STOPPED = 2;

    protected boolean destroyWhenExit = true;

    protected boolean exitWhenComplete = false;

    public static boolean master=false;

    protected int emptySleepTime = 5000;

    protected int waitNewUrlCount = 0;

    protected int polltime = 50;

    protected int threadNum = 1;

    private List<WebStructure> list=null;

    protected String uuid;

    private String proxy;

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

    public Spider setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }
    public Spider(boolean master){
        this.master=master;
    }
    public Spider(String CONNECTION_STRING,String GROUP_PATH){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new LockService(CONNECTION_STRING,GROUP_PATH).doService(new DoTemplate() {
                    public void dodo() {
                        Spider.master=true;
                        logger.info("当前爬虫是主节点》》》》》");
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
    public static Spider createJobs(List<SpiderJob> jobs,String CONNECTION_STRING,String GROUP_PATH) {
        Spider spider = new Spider(CONNECTION_STRING,GROUP_PATH);
        for (SpiderJob spiderJob:jobs){
            spider.pipelines.add(spiderJob);
        }
        return spider;
    }
    public static Spider createJobs(List<SpiderJob> jobs,boolean master) {
        Spider spider = new Spider(master);
        for (SpiderJob spiderJob:jobs){
            spider.pipelines.add(spiderJob);
        }
        return spider;
    }

    protected void initComponent() {
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

    public String getPid(){
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        logger.info("当前进程的标识为："+name);
        int index = name.indexOf("@");
        if (index != -1) {
           return name.substring(0, index);
        }
        return null;
    }

    private void savePidToZk(String pid) {
        try {
            FileOutputStream fos=new FileOutputStream(new File(System.getProperty("user.dir")+"/config/pid"));
            fos.write(pid.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        checkRunningstatus();
        initComponent();
        //保存pid 到zookeeper
        savePidToZk(getPid());
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
                    int count=0;
                    for (Pipeline pipeline:pipelines){
                        List<WebRequest> webRequests = pipeline.seedCreate();
                        if(webRequests==null||webRequests.size()==0){
                            count++;
                            if(count==pipelines.size()){
                                status.incrementAndGet();
                                break;
                            }
                            continue;
                        }
                        addWebRequest(webRequests);
                    }
                }
            } else {
                waitNewUrlCount = 0;
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Downloader downloader=null;
                        try {
                            downloader=clientPool.getClient();
                            Map<String, Object> items = /*pageProcessor.process(downloader, request)*/process(downloader, request);
                            if(null== items){
                                addRequest(request);
                            }else{
                                saveDataToPipline(items,request);
                            }
                        } catch (Exception e) {
                            logger.error("url:["+request.getUrl()+"]"+e.getMessage());
                            logger.error(e.getMessage());
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
                try {
                    Thread.sleep(polltime);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        status.set(status_STOPPED);
        if (destroyWhenExit) {
            close();
        }
    }

    public Map<String, Object> process(Downloader client, WebRequest request) {

        //所有网站容器
        Map<String,Object> map=new HashMap<>();
        try {
            //所有的网站
            List<WebStructure> list = getList();
            for (WebStructure webStructure:list){
                //当前页面下所有要爬取的列表
                List<ListStructure> dataList = webStructure.getDataList();
                List<List<Map>> allItems=new ArrayList<>();
                for (ListStructure listStructure:dataList){
                    List<Map> listItems=new ArrayList<>();
                    //路径匹配
                    if(Pattern.compile(webStructure.getPath()).matcher(request.getUrl()).find()){
                        Page page =null;
                        for (Pipeline pipeline:pipelines){
                            if(pipeline.getClass().getSimpleName().equals(webStructure.getJob())){
                                client=pipeline.setClientOption(client);
                                page = client.get(request);
                                boolean b = pipeline.checkPage(page);
                                if(!b){
                                    return null;
                                }
                            }
                        }
                        //单个列表处理
                        HashMap<String, String> pageList = listStructure.getPageList();
                        //根据处理类型进入相应解析器
                        if(webStructure.getStyle().toLowerCase().equals("html")){
                            List<Selectable> nodes = page.getHtml(listStructure.getPageListReg()).nodes();
                            for (Selectable select:nodes){
                                HashMap<String,String> items=new HashMap();
                                for (Map.Entry<String, String> item:pageList.entrySet()){
                                    Map<String, String> oderMap = Order.getOrder(item.getValue());
                                    Selectable selectable = page.getSelectable(select, oderMap);
                                    items.put(item.getKey(),selectable.get());
                                }
                                listItems.add(items);
                            }
                        }else if(webStructure.getStyle().toLowerCase().equals("json")){
                            JSONArray json = page.getJson(listStructure.getPageListReg());
                            if(json!=null){
                                for (int j=0;j<json.size();j++){
                                    JSONObject item = json.getJSONObject(j);
                                    HashMap<String,String> items=new HashMap();
                                    for (Map.Entry<String, String> i:pageList.entrySet()){
                                        String[] grap = i.getValue().split("=");
                                        if("item".equals(grap[0])){
                                            items.put(i.getKey(),item.getString(grap[1]));
                                        }
                                    }
                                    listItems.add(items);
                                }
                            }
                        }
                        allItems.add(listItems);
                    }
                }
                map.put(webStructure.getName(),allItems);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * client参数配置
     * @param client
     * @return
     */
    private Downloader setClientOption(Downloader client) {
        Jedis jedis = RedisUtils.getJedis();
        try {
            client.setClientTimeOut(10000);
            client.setReadTimeOut(30000);

            client.setHeader("platform","android");
            if (proxy.equals("true") && jedis != null) {
                String memberProxySet = jedis.srandmember("proxy:Set:ProxySet");
                if (StringUtils.isNotBlank(memberProxySet)) {
                    ProxySet proxySet = JSON.parseObject(memberProxySet, ProxySet.class);
                    client.setProxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxySet.getHost()
                            ,Integer.parseInt(proxySet.getPort()))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisUtils.returnResource(jedis);
            }
        }
        return client;
    }

    public List<WebStructure> getList() {
        if(list==null){
            synchronized (Spider.class){
                if(list==null){
                    try {
                        list= SpiderStructure.getWebStructure(Spider.class.getClassLoader().getResourceAsStream("item.yaml"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
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
        List<WebStructure> list = getList();
        for (WebStructure web:list){
            List allWeb= (ArrayList) items.get(web.getName());
            if(allWeb==null||allWeb.size()==0){
                continue;
            }
            for (int j=0;j<allWeb.size();j++){
                List allList=(List)allWeb.get(j);
                if(Pattern.compile(web.getPath()).matcher(request.getUrl()).find()){
                    for (Pipeline pipeline:pipelines){
                        if(pipeline.getClass().getSimpleName().equals(web.getJob())){
                            pipeline.save(allList,request);
                        }
                    }
                }
            }
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
        for (Pipeline pipeline : pipelines) {
            destroyEach(pipeline);
        }
        threadPool.shutdown();
        countDownLatch.countDown();
        logger.info("爬虫爬取完成,即将退出！！！"+getUUID());
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

    public Spider setPolltime(int polltime) {
        checkIfRunning();
        this.polltime = polltime;
        return this;
    }


}

