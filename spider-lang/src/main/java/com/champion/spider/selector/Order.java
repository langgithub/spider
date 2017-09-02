package com.champion.spider.selector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 2017/9/2.
 */
public class Order {

    public static Map<String,String> getOrder(String or){
        Map<String,String> map=new HashMap<>();
        String[] reg = or.split("->");
        for (int i=0;i<reg.length;i++){
            String[] a = reg[i].split("@");
            map.put(a[0],a[1]);
        }
        return  map;
    }
}
