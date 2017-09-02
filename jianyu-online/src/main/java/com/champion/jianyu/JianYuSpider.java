package com.champion.jianyu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.champion.jianyu.pojo.Content;
import com.champion.jianyu.pojo.ListUrl;
import com.champion.jianyu.pojo.ListUrlCondition;
import com.champion.jianyu.service.ContentService;
import com.champion.jianyu.service.ListUrlConditionService;
import com.champion.jianyu.service.ListUrlService;
import com.champion.jianyu.service.impl.ContentServiceImpl;
import com.champion.spider.download.*;
import com.champion.spider.pipeline.ConsolePipeline;
import com.champion.spider.scheduler.RedisScheduler;
import com.champion.spider.scheduler.Scheduler;
import com.champion.spider.scheduler.Spider;
import com.champion.spider.scheduler.SpiderJob;
import com.champion.spider.selector.Html;
import com.champion.spider.selector.Order;
import com.champion.spider.selector.Selectable;
import com.champion.spider.structure.ListStructure;
import com.champion.spider.structure.SpiderStructure;
import com.champion.spider.structure.WebStructure;
import com.champion.spider.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by root on 2017/8/25.
 */
public class JianYuSpider implements SpiderJob {

    private Logger LOG=Logger.getLogger(JianYuSpider.class);

    public static final String[] AreaName = {"北京","天津","河北","山西","内蒙古","吉林","辽宁","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东",
            "河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆"};
    //{"拟建","预告","招标","邀标","询价","竞谈","单一","竞价","变更","中标","成交","废标","流标","合同","验收","违规","其他"};
    public static final String[] InformationType={"招标"};
    private static String[] searchWord={"金融","征信","分析","数据","系统","挖掘","终端","监管","模型","建模","项目","招标","风控","供应商","服务商","采购招标","项目招标","风险管理"};
    private final static String ContentBaseUrl="https://www.zhaobiao.info/article/content/";
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    private  SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  static ApplicationContext applicationContext;

    @Value("${cluster}")
    private  String cluster = "master"; // 集群

    @Value("${task}")
    private  String task = "AppSmt"; // 任务

    @Value("${polltime}")
    private  int polltime=5000; //休眠

    @Value("${redis_addr}")
    private String redis_addr; //redisIP

    @Value("${redis_port}")
    private int redis_port; //redis 端口

    @Value("${threadnum}")
    private  int threadnum = 1; // 线程数

    @Value("${strStartTime}")
    private String strStartTime;

    @Value("${strEndTime}")
    private String strEndTime;

    @Value("${proxy}")
    private String proxy;

    @Autowired
    private ListUrlConditionService listUrlConditionServiceImpl;

    @Autowired
    private ListUrlService listUrLServiceImpl;

    @Autowired
    private ContentService contentServiceImpl;

    /**
     * 核心解析器
     * @param client
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> process(Downloader client, WebRequest request) {

        client=setClientOption(client);
        //所有网站容器
        Map<String,Object> map=new HashMap<>();
        try {
            //所有的网站
            List<WebStructure> list = SpiderStructure.getWebStructure();
            for (WebStructure webStructure:list){
                //发送请求
                Page page = client.get(request);
                //当前页面下所有要爬取的列表
                List<ListStructure> dataList = webStructure.getDataList();
                List<List<Map>> allItems=new ArrayList<>();
                for (ListStructure listStructure:dataList){
                    List<Map> listItems=new ArrayList<>();
                    //路径匹配
                    if(Pattern.compile(webStructure.getPath()).matcher(request.getUrl()).find()){
                        //单个列表处理
                        HashMap<String, String> pageList = listStructure.getPageList();
                        HashMap<String,String> items=new HashMap();
                        //根据处理类型进入相应解析器
                        if(webStructure.getStyle().toLowerCase().equals("html")){
                            Selectable html = page.getHtml(listStructure.getPageListReg());
                            for (Map.Entry<String, String> item:pageList.entrySet()){
                                Map<String, String> oderMap = Order.getOrder(item.getValue());
                                Selectable selectable=page.getSelectable(html,oderMap);
                                items.put(item.getKey(),selectable.get());
                            }
                            listItems.add(items);
                        }else if(webStructure.getStyle().toLowerCase().equals("json")){
                            JSONArray json = page.getJson(listStructure.getPageListReg());
                            if(json!=null){
                                for (int j=0;j<json.size();j++){
                                    JSONObject item = json.getJSONObject(j);
                                    for (Map.Entry<String, String> i:pageList.entrySet()){
                                        String[] grap = i.getValue().split("@");
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
                map.put("data",allItems);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return map;
    }

    @Override
    public List<WebRequest> seedCreate() {
        List<WebRequest> list=new ArrayList<>();
//        try{
//            List<ListUrlCondition> listUrlConditions = listUrlConditionServiceImpl.queryUrlCondition();
//            list=new ArrayList<>();
//            for(ListUrlCondition obj:listUrlConditions){
//                obj.setPageNum(obj.getPageNum()+1);
//                String memberEventUrlCondition = JSON.toJSONString(obj);
//                WebRequest webRequst=new WebRequest("https://www.jianyu360.com/front/pcAjaxReq");
//                webRequst.setMethod(HttpMethod.POST);
//                Map<String, String> extras=new HashMap<>();
//                extras.put("area",obj.getArea());
//                extras.put("searchvalue",obj.getKeyword());
//                String[] split = obj.getPublishtime().split("_");
//                extras.put("publishtime",sdf.parse(split[0]).getTime()/1000+"_"+sdf.parse(split[1]).getTime()/1000);
//                extras.put("subtype",obj.getSubtype());
//                extras.put("selectType","all");
//                extras.put("reqType","bidSearch");
//                extras.put("pageSize","50");
//                extras.put("pageNumber",obj.getPageNum()+"");
//                webRequst.setExtras(extras);
//                webRequst.setUrlAndParam(memberEventUrlCondition);
//                list.add(webRequst);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        List<ListUrl> listUrls = listUrLServiceImpl.queryListUrl();
        for (ListUrl listUrl:listUrls){
            WebRequest webRequest=new WebRequest("https://www.zhaobiao.info/article/content/"+listUrl.getId()+".html");
            webRequest.setMethod(HttpMethod.GET);
            webRequest.setUrlAndParam(JSON.toJSONString(listUrl));
            list.add(webRequest);
        }
        return list;
    }

    /**
     * client参数配置
     * @param client
     * @return
     */
    private Downloader setClientOption(Downloader client) {
        Jedis jedis = RedisUtils.getJedis();
        try {

            client.setClientTimeOut(5000);
            client.setReadTimeOut(30000);
            client.setHeader("Cookie","userid_secure=GycHKzoDfglQejwESQIwOV9yZH4RKkgfIBNwCA==");
            if (proxy.equals("true") && jedis != null) {
                String memberProxySet = "";
                memberProxySet = jedis.srandmember("proxy:Set:ProxySet");
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

    /**
     * 对象保存
     * @param items
     * @param request
     */
    @Override
    public void save(Map<String, Object> items,WebRequest request) {

        if ((List<List<Map>>)items.get("data")==null){
            return;
        }
        List allWeb= (ArrayList) items.get("data");
        for (int j=0;j<allWeb.size();j++){
            List allList=(List)allWeb.get(j);
            if(Pattern.compile("https://www.jianyu360.com/front/pcAjaxReq").matcher(request.getUrl()).find()) {
                for (int i = 0; i < allList.size(); i++) {
                    Map map = (Map) allList.get(i);
                    ListUrl pageList = JSONObject.parseObject(JSON.toJSONString(map), ListUrl.class);
                    pageList.setStatus(0);
                    pageList.setTs(sdf.format(new Date()));
                    try {
                        listUrLServiceImpl.insert(pageList);
                    } catch (Exception e) {
                        if (!e.getMessage().contains("PRIMARY")) {
                            e.printStackTrace();
                        }
                    }
                }
                ListUrlCondition listUrlCondition = JSON.parseObject(request.getUrlAndParam(), ListUrlCondition.class);
                if (allList.size() < 50 || listUrlCondition.getPageNum() >= 10) {
                    if (allList.size() == 0) {
                        LOG.warn("没有匹配到pageList 相关内容，请检查>>>>>>>>" + request.getExtras().toString());
                    }
                    listUrlCondition.setStatus(1);
                    listUrlCondition.setTotal((50 * (listUrlCondition.getPageNum() - 1) + allList.size()));
                }
                listUrlCondition.setTs(sdf.format(new Date()));
                listUrlConditionServiceImpl.update(listUrlCondition);
                LOG.info("数据保存成功>>>>>>" + listUrlCondition.toString());
            }else if(Pattern.compile("https://www.zhaobiao.info/article/content").matcher(request.getUrl()).find()){
                ListUrl listUrl = JSON.parseObject(request.getUrlAndParam(), ListUrl.class);
                for (int i = 0; i < allList.size(); i++) {
                    Map map = (Map) allList.get(i);
                    Content content = JSONObject.parseObject(JSON.toJSONString(map), Content.class);
                    content.setId(listUrl.getId());
                    contentServiceImpl.insert(content);
                }
                listUrl.setStatus(1);
                listUrl.setTs(sdf.format(new Date()));
                listUrLServiceImpl.update(listUrl);
                LOG.info("数据保存成功>>>>>>" + listUrl.toString());
            }
        }
    }

    /**
     * 爬虫入口
     * @param args
     */
    public static void main(String[] args){
        applicationContext = new ClassPathXmlApplicationContext("spring/application*");
        JianYuSpider processer= (JianYuSpider) applicationContext.getBean("jianYuSpider");
//        processer.createUrl();
        processer.doTask();
    }


    /**
     * 任务调度
     * @throws ParseException
     */
    private void doTask(){
        Spider.create(this).setScheduler(new RedisScheduler("jianyu:online")).thread(threadnum).run();
    }

    /**
     * 种子生产
     */
    private void createUrl() {
        List<String> list=new ArrayList<>();
        for(String area:AreaName){
            for (String search:searchWord){
                ListUrlCondition listUrlCondition=new ListUrlCondition();
                listUrlCondition.setKeyword(search);
                listUrlCondition.setArea(area);
                listUrlCondition.setPublishtime("2017-08-01_2017-08-01");
                listUrlCondition.setSubtype("招标");
                listUrlCondition.setPageNum(0);
                listUrlCondition.setTotal(0);
                listUrlCondition.setStatus(0);
                listUrlCondition.setTs(sdf.format(new Date()));
                listUrlConditionServiceImpl.insert(listUrlCondition);
            }
        }
    }
}
