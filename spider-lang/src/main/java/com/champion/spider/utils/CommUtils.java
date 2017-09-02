package com.champion.spider.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtils {
	
	public static String getValueFromProperties(String filePath, String keyname) {
		Properties properties = new Properties();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			properties.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(keyname);
	}
	
	public static void setValueToProperties(String filePath, String keyname, String keyvalue) {   
		Properties properties = new Properties();
        try {   
        	BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
			properties.load(reader);
			properties.setProperty(keyname, keyvalue); 
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));                
			properties.store(writer, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
        	 e.printStackTrace();
        }   
    }   
	
	public static List<String> concateList(List<String> list1,List<String> list2) {   
		 List<String> resultList = new ArrayList<String>();
		 
		 if (list1 == null || list1.size() == 0) return list2;
		 if (list2 == null || list2.size() == 0) return list1;
		 
		 for (String item1:list1) {
			 for (String item2:list2) {
				 resultList.add(item1 + "," + item2);
		     }
	     }
		 
		 return resultList;
    } 
	
	public static void SaveError(String errStr) {
		try {
			String fileName = System.getProperty("user.dir") + "/errLog.txt";
			
			File file = new File(fileName);      
			if (!file.exists()) {
				file.createNewFile();
			}
			
            FileWriter writer = new FileWriter(file, true);
            
            errStr = errStr + System.getProperty("line.separator");
            writer.write(errStr);
            writer.flush();
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * 基于baseUrl，补全html代码中的链接
	 * @param baseUrl
	 * @param html
	 * @return
	 */
	public static String fixUrl(String baseUrl, String html)
    {
 		String regex = "(?is)(href|src)=(\"|\')(?!https?://)([^(\"|\')]+)(\"|\')";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		
		while (matcher.find()) {
			String head = matcher.group(1);
			String link = matcher.group(3);

			try {
				URI base = new URI(baseUrl);;
				URI abs = base.resolve(link);
				URL absURL = abs.toURL();
				
				html = html.replace(matcher.group(), String.format("%s=\"%s\"", head, absURL.toString()));
				
			} catch (Exception e) {
				continue;
			}
		}
        
        return html;
    }
}
