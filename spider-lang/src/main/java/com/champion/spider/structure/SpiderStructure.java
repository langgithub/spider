package com.champion.spider.structure;

import org.ho.yaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 2017/8/23.
 */
public class SpiderStructure {

    public static List<WebStructure> getWebStructure(InputStream path) throws FileNotFoundException {
        Structure structure = Yaml.loadType(path, Structure.class);
        List<WebStructure> list=new ArrayList<>();
        for (Map<String,Object> map: structure.getWebStructure()){
            WebStructure webStructure=new WebStructure();
            webStructure.setName((String) map.get("name"));
            webStructure.setStyle((String) map.get("style"));
            webStructure.setPath((String) map.get("path"));
            webStructure.setMethod((String) map.get("method"));
            webStructure.setJob((String) map.get("job"));
            List<Map<String,Object>> data = (List<Map<String, Object>>) map.get("data");

            List<ListStructure> dataList=new ArrayList<>();
            for (Map<String,Object> listMap:data){
                ListStructure listStructure=new ListStructure();
                listStructure.setPageListReg((String) listMap.get("pageListReg"));
                listStructure.setPageList((HashMap)listMap.get("pageList"));
                dataList.add(listStructure);
            }
            webStructure.setDataList(dataList);
            list.add(webStructure);
        }
        return list;
    }

    public static void main(String[] args) throws FileNotFoundException {
//        List<WebStructure> webStructure = SpiderStructure.getWebStructure(new File(""));
//        webStructure.forEach(webStructure1 -> System.out.println(webStructure1.toString()));
    }

}
