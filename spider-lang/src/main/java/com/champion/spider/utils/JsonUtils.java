package com.champion.spider.utils;

/**
 * Json utils
 * 
 * @author xulixiang@champion-credit.com
 * @version 1.0.0
 * @date 2016.7.13
 */
public class JsonUtils {

	 /**
     * 判断一个字符串是否能够转换成JSONArray,仅考虑首尾格式是否符合
     * @param content
     * @return
     */
    public static boolean isJSONArrayStr(String content){
        boolean isOrNot = false;
        content = content.trim();
        if(content.startsWith("\"[") && content.endsWith("]\"")) {
        	isOrNot = true;
        }
        else if(content.startsWith("[") && content.endsWith("]")) {
        	isOrNot = true;
        }
        else if(content.startsWith("\"{") && content.endsWith("}\"")) {
        	isOrNot = true;
        }
        else if(content.startsWith("{") && content.endsWith("}")) {
        	isOrNot = true;
        }
        
        return isOrNot;
    }
    
    /**
     * 删除文本中多余的双引号
     * @param s
     * @return
     */
    public static String formatJsonStr(String s){
        char[] temp = s.toCharArray();
        int n = temp.length;
        for(int i = 1; i < n - 2; i++){
            if(temp[i] == ':' && (temp[i + 1] == '"' || temp[i + 1] == '\'')){
                for(int j = i + 2;j < n; j++){
                    if(temp[j-1] != '\\' && temp[j] == '"'){
                    	if(temp[j + 1] != '}' &&  temp[j + 1] != ','){
                    		temp[j] = '”';
                    	}else if(temp[j + 1] == '}' || temp[j + 1] == ','){
                        	break ;
                        }
                    }
                    if(temp[j-1] != '\\' && temp[j] == '\''){
                    	if(temp[j + 1] != '}' &&  temp[j + 1] != ','){
                    		temp[j] = '’';
                    	}else if(temp[j + 1] == '}' || temp[j + 1] == ','){
                        	break ;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }
    
    /**
     * 删除文本中非utf8格式字符
     * @param s
     * @return
     */
    public static String removeFourChar(String content, String charset){

		try {
			byte[] conbyte = content.getBytes(charset);
			
			for (int i = 0; i < conbyte.length;) {
	            if ((conbyte[i] & 0xF8) == 0xF0) {
	                for (int j = 0; j < 4; j++) {                          
	                    conbyte[i+j]=0x20;                     
	                }  
	                i += 4;
	            }
	            else {
	            	i += 1;
	            }
	        }
			
			return new String(conbyte,charset);
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        
		return null;
    }

    
    /**
     * 将字符串中属于JSONArray对象的部分子串提取出来
     * @param content
     * @return
     */
    public static String getJSONArrayStr(Boolean jsonFormat, String content, String charset){
        /**
         * 首先判断传入的字符串是否能够转化成JSONArray类型的对象，
         * 如果不行，则返回null，如果满足，则继续向下执行。
         */
        if(!isJSONArrayStr(content))
        {
            return null;
        }
        
        String regex = "(\\\\u003c|u003c)[^\\\\u4e00-\\\\u9fa5]+(\\\\u003e|u003e)";
        content = content.replaceAll(regex, "");
        content = content.replaceAll("(\\\\u0026amp;|u0026amp;|#xA;|#xA)", "");
        content = content.replaceAll("(\\\\u0027|u0027)", "'");
        content = content.replaceAll("(\\[ly\\])", "");
        content = content.replaceAll("\\\\\\\"", "\\\"").replaceAll("\\\\\\\\", "\\\\");
        content = content.replaceAll("\\\\%", "").replaceAll("\\\\,", ",");
        content = content.replaceAll("\\\\", "");
        if (!jsonFormat) {
	        content = formatJsonStr(content);
	        content = removeFourChar(content,charset);
        
	        /**
	         * 存放JSONArray数组的起始与终止位置，用于从整
	         * 个字符串中提取出JSONArray对象对应的子串部分。
	         */
	        int start = -1;
	        int end = -1;
	        int bracket = 0; //0：中括号，1：大括号
	        if (content.indexOf("[") >=0 && content.indexOf("[") < content.indexOf("{")) {
	        	start = content.indexOf("[");
	        	bracket = 0;
	        } else {
	        	start = content.indexOf("{");
	        	bracket = 1;
	        }
        
	        if (bracket == 0) {
	        	end = content.lastIndexOf("]");
	        } else {
	        	end = content.lastIndexOf("}");
	        }
	        /**
	         * 由于待处理的字符串中存在一些转义字符，
	         * \"替换成"
	         * \\替换成\
	         */
        
        	content = content.substring(start, end + 1);
        }
        return content;
    }
}
