//package com.champion.spider.utils;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.commons.io.IOUtils;
//import com.alibaba.fastjson.JSON;
//
//public class CaptchaUtils {
//
//	public static String getCaptcha(StringBuilder yzmId, InputStream inCaptcha) {
//		String captcha = "";
//
//		// 上传验证码图片
//		String BOUNDARY = "---------------------------68163001211748"; // boundary就是request头和上传文件内容的分隔符
//		String uploadUrl = "http://bbb4.hyslt.com/api.php?mod=php&act=upload";
//
//		//参数
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("user_name", "winchampion");
//		paramMap.put("user_pw", "Winchampion2017");
//		paramMap.put("yzm_minlen", "1");
//		paramMap.put("yzm_maxlen", "5");
//		paramMap.put("yzmtype_mark", "0");
//		paramMap.put("zztool_token", "winchampion");
//
//		try {
//			URL url = new URL(uploadUrl);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + BOUNDARY);
//			connection.setConnectTimeout(20000);
//			connection.setReadTimeout(20000);
//
//			OutputStream out = new DataOutputStream(connection.getOutputStream());
//
//			if (paramMap != null) {
//				StringBuffer strBuf = new StringBuffer();
//				Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
//				while (iter.hasNext()) {
//					Entry<String, String> entry = iter.next();
//					String inputName = entry.getKey();
//					String inputValue = entry.getValue();
//					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
//					strBuf.append(inputValue);
//				}
//				out.write(strBuf.toString().getBytes());
//			}
//
//			if (inCaptcha != null) {
//				String filename = "t1.jpg";
//				String contentType = "image/jpeg";// 这里看情况设置
//				StringBuffer strBuf = new StringBuffer();
//				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//				strBuf.append("Content-Disposition: form-data; name=\"" + "upload" + "\"; filename=\"" + filename + "\"\r\n");
//				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
//				out.write(strBuf.toString().getBytes());
//				DataInputStream in = new DataInputStream(inCaptcha);
//				int bytes = 0;
//				byte[] bufferOut = new byte[1024];
//				while ((bytes = in.read(bufferOut)) != -1) {
//					out.write(bufferOut, 0, bytes);
//				}
//				in.close();
//			}
//			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
//			out.write(endData);
//			out.flush();
//			out.close();
//
//			// 读取URLConnection的响应
//			InputStream in = connection.getInputStream();
//
//			// 结果输出
////			String strBout = IOUtils.toString(in, "utf-8");
////			System.out.println("结果输出为：" + strBout);
//
//			in.close();
//
//			yzmId.delete(0, yzmId.length());
//			yzmId.append(JSON.parseObject(strBout).getJSONObject("data").get("id").toString());
//			captcha =  JSON.parseObject(strBout).getJSONObject("data").get("val").toString();
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return captcha;
//	}
//
//	public static void setCaptchaError(String yzm_id) {
//
//		String BOUNDARY = "---------------------------68163001211748"; // boundary就是request头和上传文件内容的分隔符
//		String errUrl = "http://bbb4.hyslt.com/api.php?mod=php&act=error";
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("user_name", "winchampion");
//		paramMap.put("user_pw", "Winchampion2017");
//		paramMap.put("yzm_id", yzm_id);
//
//		try {
//			URL url = new URL(errUrl);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("content-type","multipart/form-data; boundary=" + BOUNDARY);
//			connection.setConnectTimeout(30000);
//			connection.setReadTimeout(30000);
//
//			OutputStream out = new DataOutputStream(connection.getOutputStream());
//
//			if (paramMap != null) {
//				StringBuffer strBuf = new StringBuffer();
//				Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
//				while (iter.hasNext()) {
//					Entry<String, String> entry = iter.next();
//					String inputName = entry.getKey();
//					String inputValue = entry.getValue();
//					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
//					strBuf.append(inputValue);
//				}
//				out.write(strBuf.toString().getBytes());
//			}
//
//			// 读取URLConnection的响应
//			InputStream in = connection.getInputStream();
//
//			// 结果输出
////			String strBout = IOUtils.toString(in, "utf-8");
////			System.out.println("异常上报结果为：" + strBout);
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
