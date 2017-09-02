package com.champion.spider.utils;

/**
 * Created by root on 2017/8/24.
 */
public class ClassUtils {
    public static String convertToSetMethond(String setter){
        return "set"+setter.substring(0,1).toUpperCase()+setter.substring(1,setter.length());
    }
    public static String convertToGetMethond(String setter){
        return "get"+setter.substring(0,1).toUpperCase()+setter.substring(1,setter.length());
    }
    public static void main(String[] args){
        System.out.print(ClassUtils.convertToSetMethond("sSd"));
    }
}
