package com.champion.spider.download;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.champion.spider.selector.Html;
import com.champion.spider.selector.Order;
import com.champion.spider.selector.Selectable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page {

    private StringBuffer content;
    private Html html;
    private JSONObject json;

    public Page(StringBuffer content) {
        this.content = content;
    }

    public Selectable getHtml(String order) {
        Html html=new Html(content.toString());
        Selectable selectable=html.$("html");
        Map<String, String> map = Order.getOrder(order);
        selectable = getSelectable(selectable, map);
        return selectable;
    }

    public Selectable getSelectable(Selectable selectable, Map<String, String> map) {
        for(Map.Entry<String, String> s:map.entrySet()){
            if(s.getKey().equals("css")){
                selectable = selectable.$(s.getValue());
            }else if(s.getKey().equals("reg")){
                selectable=selectable.regex(s.getValue());
            }
        }
        return selectable;
    }

    public JSONArray getJson(String pageListReg) {
        String[] reg = pageListReg.split("->");
        Pattern pattern=null;
        String need=content.toString();
        for (int i=0;i<reg.length;i++){
            String[] a = reg[i].split("@");
            if("reg".equals(a[0])){
                pattern=Pattern.compile(a[1]);
                Matcher matcher = pattern.matcher(need);
                if(matcher.find()){
                    need=matcher.group(1);
                }else {
                    throw new IllegalArgumentException("json解析，正则不匹配");
                }
            }else if("item".equals(a[0])){
                need = JSONObject.parseObject(need).getString(a[1]);
            }
        }
        return  JSONArray.parseArray(need);
    }
}

