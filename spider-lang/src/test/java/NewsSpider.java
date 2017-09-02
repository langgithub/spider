import com.alibaba.fastjson.JSONArray;
import com.champion.spider.download.Downloader;
import com.champion.spider.download.Page;
import com.champion.spider.download.WebRequest;
import com.champion.spider.pipeline.ConsolePipeline;
import com.champion.spider.scheduler.Spider;
import com.champion.spider.scheduler.SpiderJob;
import com.champion.spider.selector.Html;
import com.champion.spider.selector.Selectable;
import com.champion.spider.structure.SpiderStructure;
import com.champion.spider.structure.WebStructure;
import com.champion.spider.utils.RedisUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by root on 2017/8/22.
 */
public class NewsSpider implements SpiderJob{

    @Override
    public Map<String, Object> process(Downloader client, WebRequest request) {
        Map<String,Object> map=null;
        try {
            List<WebStructure> list = SpiderStructure.getWebStructure();
            for (WebStructure webStructure:list){
                if(Pattern.compile(webStructure.getPath()).matcher(request.getUrl()).find()){
//                    Page page = client.get(request);
//                    map=new HashMap<>();
//                    HashMap<String, String> pageList = webStructure.getPageList();
//                    if(webStructure.getStyle().toLowerCase().equals("html")){
//                        Html html = page.getHtml();
//                        HashMap<String,String> items=new HashMap();
//                        List<Selectable> nodes = html.$(webStructure.getPageListReg()).nodes();
//                        for(Selectable selectable:nodes){
//                            for (Map.Entry<String, String> item:pageList.entrySet()){
//                                String value = selectable.$(item.getValue()).get();
//                                items.put(item.getKey(),value);
//                            }
//                        }
//                        map.put("pageList",items);
//                    }else if(webStructure.getStyle().toLowerCase().equals("json")){
//                        JSONArray json = page.getJson(webStructure.getPageListReg());
//                        HashMap<String,String> items=new HashMap();
//                        for (int j=0;j<json.size();j++){
//                            String[] con = json.get(j).toString().split(",");
//                            for (Map.Entry<String, String> i:pageList.entrySet()){
//                                if(i.getValue().contains("=")){
//                                    String[] grap = i.getValue().split("=");
//                                    if("item".equals(grap[0])){
//                                        int index=Integer.parseInt(grap[1]);
//                                        items.put(i.getKey(),con[index]);
//                                    }
//                                }else{
//                                    items.put(i.getKey(),i.getValue());
//                                }
//                            }
//                        }
//                        map.put("pageList",items);
//                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<WebRequest> seedCreate() {
        return null;
    }

    public static void main(String[] args){

        List<String>  list=new ArrayList<>();
        list.add("http://datainterface.eastmoney.com//EM_DataCenter/js.aspx?type=SR&sty=HYSR&sc=437&js=var%20AzcYhkGE={%22data%22:[(x)],%22pages%22:%22(pc)%22,%22update%22:%22(ud)%22,%22count%22:%22(count)%22}&ps=25&p=1&mkt=0&stat=0&rt=50110346");
        list.add("http://datainterface.eastmoney.com//EM_DataCenter/js.aspx?type=SR&sty=HYSR&sc=437&js=var%20AzcYhkGE={%22data%22:[(x)],%22pages%22:%22(pc)%22,%22update%22:%22(ud)%22,%22count%22:%22(count)%22}&ps=25&p=2&mkt=0&stat=0&rt=50110346");
        list.add("http://www.cnblogs.com/resentment/p/5872798.html");
        Spider.create(new NewsSpider()).addUrl(list).addPipeline(new ConsolePipeline()).thread(1).run();
    }


    @Override
    public void save(Map<String, Object> items, WebRequest request) {

    }
}
