//package com.champion.spider.utils;
//
//import net.sourceforge.tess4j.*;
//
//import java.awt.Color;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.net.URL;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.http.HttpHost;
//
//
//class Point{
//	int x;
//	int y;
//	public Point(int k, int h) {
//		// TODO 自动生成的构造函数存根
//		this.x = k;
//		this.y = h;
//	}
//	public void setPoint(int x,int y){
//		this.x=x;
//		this.y=y;
//	}
//	public int getX()
//	{
//		return x;
//	}
//	public int getY(){
//		return y;
//	}
//}
//
///**
// * tess4j OCR utils
// *
// * @author xulixiang@champion-credit.com
// * @version 1.0.0
// * @date 2016.7.16
// */
//public class OCRUtils {
//
//	public static String OCR_FILE = System.getProperty("user.dir") + "/t.jpg";
//	public static String OCR_FILE1 = System.getProperty("user.dir") + "/t1.jpg";
//
//	public static String getOCRString(String url, Map<String, String> headers, HttpHost proxy, String type) {
//		String result = null;
//
//    	try {
//    		InputStream inStream = getImageInputStream(url, headers, proxy);
//    		BufferedImage bi = ImageIO.read(inStream);
//    		bi = convertToBinaryImage(bi);
//			bi = clearNoise(bi, 255, 1);
//			bi = clearBurr(bi);
//
////    		File file = new File (OCR_FILE);
////    		ImageIO.write(bi, "JPEG", file);
//
////			bi = preProcessImage(bi);
////			bi = clearLine(bi);
//
////    		file = new File (OCR_FILE1);
////    		ImageIO.write(bi, "JPEG", file);
//
//    		//识别验证码
//            ITesseract instance = new Tesseract();  // JNA Interface Mapping
//            //if (type.equals("SHGSJ")) {
//            //	instance.setLanguage("chi_sim");
//            //}
//            result = instance.doOCR(bi);
//
//            inStream.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//        return result;
//    }
//
//	public static void downloadImage(InputStream inStream) throws Exception {
//		if (inStream == null) return;
//
//        byte[] data = readInputStream(inStream);
//
//        File imageFile = new File(System.getProperty("user.dir") + "/t.jpg");
//        FileOutputStream outStream = new FileOutputStream(imageFile);
//
//        outStream.write(data);
//        outStream.close();
//    }
//
//	public static InputStream getImageInputStream(String url, Map<String, String> headers, HttpHost proxyHost) throws Exception {
//		//当前代理
//		Proxy proxy = null;
//		if (proxyHost != null) {
//			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost.getAddress(), proxyHost.getPort()));
//		}
//
//		URL console = new URL(url);
//
//		if (url != null && url.contains("https")) {
//			X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
//				public void checkClientTrusted(X509Certificate[] chain,
//						String authType) throws CertificateException {
//				}
//
//				public void checkServerTrusted(X509Certificate[] chain,
//						String authType) throws CertificateException {
//				}
//
//				public X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//			};
//			HostnameVerifier hnv = new HostnameVerifier() {
//		        public boolean verify(String hostname, SSLSession session) {
//		            return true;
//		        }
//			};
//
//			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
//			SSLContext sslContext = null;
//			try {
//				sslContext = SSLContext.getInstance("TLS");
//				X509TrustManager[] xtmArray = new X509TrustManager[] {xtm};
//				sslContext.init(null, xtmArray, new java.security.SecureRandom());
//
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			}
//
//			HttpsURLConnection httpsConn = null;
//			if (proxy != null) {
//				httpsConn = (HttpsURLConnection) console.openConnection(proxy);
//			} else {
//				httpsConn = (HttpsURLConnection) console.openConnection();
//			}
//
//			httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
//			httpsConn.setHostnameVerifier(hnv);
//
//	        if (headers != null) {
//	            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
//	            	httpsConn.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
//	            }
//	        }
//	        httpsConn.connect();
//
//	        InputStream inStream = httpsConn.getInputStream();
//	        return inStream;
//
//		} else {
//			HttpURLConnection conn = null;
//			if (proxy != null) {
//				conn = (HttpURLConnection) console.openConnection(proxy);
//			} else {
//				conn = (HttpURLConnection) console.openConnection();
//			}
//
//			conn.setRequestMethod("GET");
//			conn.setConnectTimeout(60 * 1000);
//
//
//			if (headers != null) {
//	            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
//	            	conn.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
//	            }
//	        }
//			conn.connect();
//
//			InputStream inStream = conn.getInputStream();
//	        return inStream;
//		}
//    }
//
//	public static byte[] readInputStream(InputStream inStream) throws Exception {
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//
//        int len = 0;
//        while ((len = inStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, len);
//        }
//
//        byte[] result = outStream.toByteArray();
//        outStream.close();
//
//        return result;
//    }
//
//	/**
//	 * 获取二值化阈值
//	 * @param bi
//	 * @return
//	 */
//    public static int GetDgGrayValue(BufferedImage bi)
//    {
//        int[] pixelNum = new int[256];           //图象直方图，共256个点
//        int n, n1, n2;
//        int total;                              //total为总和，累计值
//        double m1, m2, sum, csum, fmax, sb;     //sb为类间方差，fmax存储最大方差值
//        int k, t, q;
//        int threshValue = 1;                      // 阈值
//
//        //生成直方图
//        for (int i =0; i < bi.getWidth() ; i++)
//        {
//            for (int j = 0; j < bi.getHeight(); j++)
//            {
//                //返回各个点的颜色，以RGB表示
//                pixelNum[bi.getRGB(i, j)&0xff]++;            //相应的直方图加1
//            }
//        }
//        //直方图平滑化
//        for (k = 0; k <= 255; k++)
//        {
//            total = 0;
//            for (t = -2; t <= 2; t++)              //与附近2个灰度做平滑化，t值应取较小的值
//            {
//                q = k + t;
//                if (q < 0)                     //越界处理
//                    q = 0;
//                if (q > 255)
//                    q = 255;
//                total = total + pixelNum[q];    //total为总和，累计值
//            }
//            pixelNum[k] = (int)((float)total / 5.0 + 0.5);    //平滑化，左边2个+中间1个+右边2个灰度，共5个，所以总和除以5，后面加0.5是用修正值
//        }
//        //求阈值
//        sum = csum = 0.0;
//        n = 0;
//        //计算总的图象的点数和质量矩，为后面的计算做准备
//        for (k = 0; k <= 255; k++)
//        {
//            sum += (double)k * (double)pixelNum[k];     //x*f(x)质量矩，也就是每个灰度的值乘以其点数（归一化后为概率），sum为其总和
//            n += pixelNum[k];                       //n为图象总的点数，归一化后就是累积概率
//        }
//
//        fmax = -1.0;                          //类间方差sb不可能为负，所以fmax初始值为-1不影响计算的进行
//        n1 = 0;
//        for (k = 0; k < 256; k++)                  //对每个灰度（从0到255）计算一次分割后的类间方差sb
//        {
//            n1 += pixelNum[k];                //n1为在当前阈值遍前景图象的点数
//            if (n1 == 0) { continue; }            //没有分出前景后景
//            n2 = n - n1;                        //n2为背景图象的点数
//            if (n2 == 0) { break; }               //n2为0表示全部都是后景图象，与n1=0情况类似，之后的遍历不可能使前景点数增加，所以此时可以退出循环
//            csum += (double)k * pixelNum[k];    //前景的“灰度的值*其点数”的总和
//            m1 = csum / n1;                     //m1为前景的平均灰度
//            m2 = (sum - csum) / n2;               //m2为背景的平均灰度
//            sb = (double)n1 * (double)n2 * (m1 - m2) * (m1 - m2);   //sb为类间方差
//            if (sb > fmax)                  //如果算出的类间方差大于前一次算出的类间方差
//            {
//                fmax = sb;                    //fmax始终为最大类间方差（otsu）
//                threshValue = k;              //取最大类间方差时对应的灰度的k就是最佳阈值
//            }
//        }
//        return threshValue;
//    }
//
//	/**
//	 * 二值化，判断像素点是白色还是黑色
//	 * @param colorRGBInt
//	 * @param threshold 通过工具估计出干扰像素与正常字符RGB阈值
//	 * @return 0=White,1=Black
//	 */
//	public static int isWhiteOrBlack(int colorRGBInt, int threshold) {
//        Color color = new Color(colorRGBInt);
//        if (color.getRed() >= threshold && color.getGreen() >= threshold  && color.getBlue() >= threshold) {
//            return 0;
//        }
//        return 1;
//    }
//
//	/**
//	 * 二值化
//	 * @param bi
//	 * @return
//	 */
//	public static BufferedImage convertToBinaryImage(BufferedImage bi) {
//		int minx = bi.getMinX();
//        int miny = bi.getMinY();
//        int width = bi.getWidth();
//        int height = bi.getHeight();
//
//        int threshold = GetDgGrayValue(bi);
//
//        for (int x = minx; x < width; ++x) {
//            for (int y = miny; y < height; ++y) {
//                if (isWhiteOrBlack(bi.getRGB(x, y), threshold) == 0) {
//                	bi.setRGB(x, y, Color.WHITE.getRGB());
//                } else {
//                	bi.setRGB(x, y, Color.BLACK.getRGB());
//                }
//            }
//        }
//        return bi;
//    }
//
//	/**
//	 * 去噪点
//	 * @param bi
//	 * @return
//	 */
//	public static BufferedImage clearNoise(BufferedImage bi, int dgGrayValue, int MaxNearPoints)
//	{
//	    int nearDots = 0;
//	    int[][] pixelFlag = new int[bi.getWidth()][bi.getHeight()];
//
//	    //逐点判断
//		for (int i = 0; i < bi.getWidth(); i++) {
//		    for (int j = 0; j < bi.getHeight(); j++)
//		    {
//		        if ((bi.getRGB(i, j)&0xff) < dgGrayValue)
//		        {
//		            nearDots = 0;
//		            //判断周围8个点是否全为空
//					if (i == 0 || i == bi.getWidth() - 1 || j == 0 || j == bi.getHeight() - 1)  //边框全去掉
//					{
//						pixelFlag[i][j] = 1;
//					}
//					else
//					{
//					    if ((bi.getRGB(i - 1, j - 1)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i, j - 1)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i + 1, j - 1)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i - 1, j)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i + 1, j)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i - 1, j + 1)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i, j + 1)&0xff) < dgGrayValue) nearDots++;
//					    if ((bi.getRGB(i + 1, j + 1)&0xff) < dgGrayValue) nearDots++;
//					}
//
//					if (nearDots <= MaxNearPoints) {
//						pixelFlag[i][j] = 1;
//					}
//		        }
//		        else {  //背景
//		        	pixelFlag[i][j] = 1;
//		        }
//		    }
//		}
//
//		for (int i = 0; i < bi.getWidth(); i++) {
//		    for (int j = 0; j < bi.getHeight(); j++)
//		    {
//		    	if (pixelFlag[i][j] == 1) {
//		    		bi.setRGB(i, j, Color.WHITE.getRGB());
//		    	}
//		    }
//		}
//
//		return bi;
//	}
//
//	/**
//	 * 去刺点
//	 * @param bi
//	 * @return
//	 */
//	public static BufferedImage clearBurr(BufferedImage bi)
//	{
//	    int[][] pixelFlag = new int[bi.getWidth()][bi.getHeight()];
//
//	    //逐点判断
//	    for (int i = 0; i < bi.getWidth() - 1; i++)
//		{
//	    	for (int j = 0; j < bi.getHeight() - 1; j++)
//		    {
//		    	//行消除
//		        if ((bi.getRGB(i, j)&0xff) == 0 && (bi.getRGB(i - 1, j)&0xff) == 255 && (bi.getRGB(i + 1, j)&0xff) == 255)
//		        {
//				    if ((bi.getRGB(i, j - 1)&0xff) == 255 || (bi.getRGB(i, j + 1)&0xff) == 255) {
//				    	if ((bi.getRGB(i - 1, j + 1)&0xff) == 0 || (bi.getRGB(i + 1, j - 1)&0xff) == 0 ||
//					    	 	(bi.getRGB(i - 1, j - 1)&0xff) == 0 || (bi.getRGB(i + 1, j + 1)&0xff) == 0) {
//				    		pixelFlag[i][j] = 0;
//				    	}
//				    	else {
//				    		pixelFlag[i][j] = 1;
//				    	}
//
//				    	if ((bi.getRGB(i, j - 1)&0xff) == 0 && (bi.getRGB(i - 1, j + 1)&0xff) == 255 && (bi.getRGB(i + 1, j + 1)&0xff) == 255) {
//				    		pixelFlag[i][j] = 1;
//				    	}
//				    	if ((bi.getRGB(i, j + 1)&0xff) == 0 && (bi.getRGB(i - 1, j - 1)&0xff) == 255 && (bi.getRGB(i + 1, j - 1)&0xff) == 255) {
//				    		pixelFlag[i][j] = 1;
//				    	}
//					}
//		        }
//
//		        //列消除
//		        if ((bi.getRGB(i, j)&0xff) == 0 && (bi.getRGB(i, j - 1)&0xff) == 255 && (bi.getRGB(i, j + 1)&0xff) == 255)
//		        {
//				    if (((bi.getRGB(i - 1, j)&0xff) == 255 || (bi.getRGB(i + 1, j)&0xff) == 255) ) {
//				    	if ((bi.getRGB(i - 1, j + 1)&0xff) == 0 || (bi.getRGB(i + 1, j - 1)&0xff) == 0 ||
//				    			(bi.getRGB(i - 1, j - 1)&0xff) == 0 || (bi.getRGB(i + 1, j + 1)&0xff) == 0) {
//				    		pixelFlag[i][j] = 0;
//				    	}
//				    	else {
//				    		pixelFlag[i][j] = 1;
//				    	}
//
//				    	if ((bi.getRGB(i - 1, j)&0xff) == 0 && (bi.getRGB(i + 1, j - 1)&0xff) == 255 && (bi.getRGB(i + 1, j + 1)&0xff) == 255) {
//				    		pixelFlag[i][j] = 1;
//				    	}
//				    	if ((bi.getRGB(i + 1, j)&0xff) == 0 && (bi.getRGB(i - 1, j - 1)&0xff) == 255 && (bi.getRGB(i - 1, j + 1)&0xff) == 255) {
//				    		pixelFlag[i][j] = 1;
//				    	}
//				    }
//
//		        }
//		    }
//		}
//
//		for (int i = 0; i < bi.getWidth(); i++) {
//		    for (int j = 0; j < bi.getHeight(); j++)
//		    {
//		    	if (pixelFlag[i][j] == 1) {
//		    		bi.setRGB(i, j, Color.WHITE.getRGB());
//		    	}
//		    }
//		}
//
//		return bi;
//	}
//
//	/**
//	 * 去干扰线
//	 * @param bi
//	 * @return
//	 */
//	public static BufferedImage clearLine(BufferedImage bi)
//	{
//		int[][] pixelFlag = new int[bi.getWidth()][bi.getHeight()];
//		List<Point> pointList = new ArrayList<Point>();
//
//		//单像素，暂清除
//	    for (int i = 0; i < bi.getWidth(); i++){
//	    	for (int j = 0; j < bi.getHeight(); j++){
//	    		pixelFlag[i][j] = 0;
//
//	    		if ((bi.getRGB(i, j)&0xff) == 0 && (bi.getRGB(i-1, j)&0xff) == 255 && (bi.getRGB(i+1, j)&0xff) == 255){
//	    			pixelFlag[i][j] = 1;
//		        }
//	    		if ((bi.getRGB(i, j)&0xff) == 0 && (bi.getRGB(i, j-1)&0xff) == 255 && (bi.getRGB(i, j+1)&0xff) == 255){
//	    			pixelFlag[i][j] = 1;
//		        }
//		    }
//		}
//
//	    //逐点判断
//	    for (int i = 0; i < bi.getWidth(); i++){
//	    	for (int j = 0; j < bi.getHeight(); j++){
//		        //右上方
//		        if ((bi.getRGB(i, j)&0xff) == 0){
//		        	pointList.clear();
//
//		        	int k = i, h = j;
//		        	int jumpXFlag = 0;
//
//		        	pointList.add(new Point(k,h));
//	        		while (k < bi.getWidth() && h >= 0) {
//	        			if ((bi.getRGB(k + 1, h)&0xff) == 0) {//水平线
//		        		    k = k + 1;
//		        		    pointList.add(new Point(k,h));
//		        		} else if ((bi.getRGB(k + 1, h)&0xff) == 255 && (bi.getRGB(k + 1, h - 1)&0xff) == 0) {//本行结束，向上跳跃一行
//		        			k = k + 1;
//		        			h = h - 1;
//		        			jumpXFlag++;
//		        			pointList.add(new Point(k,h));
//		        		} else {
//		        			break;
//		        		}
//	        		}
//
//	        		if (jumpXFlag < 4 && pointList.size() < 30) {
//		        		for (Point point : pointList) {
//		        			pixelFlag[point.getX()][point.getY()] = 0;
//		        		}
//		        	}
//		        }
//
//		        //右下方
//		        /*if ((bi.getRGB(i, j)&0xff) == 0)
//		        {
//		        	pointList.clear();
//
//		        	int k = i, h = j;
//		        	int jumpXFlag = 0;
//
//		        	pointList.add(new Point(k,h));
//	        		while (k < bi.getWidth() && h >= 0) {
//	        			if ((bi.getRGB(k + 1, h)&0xff) == 0) {//水平线
//		        		    k = k + 1;
//		        		    pointList.add(new Point(k,h));
//		        		} else if ((bi.getRGB(k + 1, h)&0xff) == 255 && (bi.getRGB(k + 1, h + 1)&0xff) == 0) {//本行结束，向上跳跃一行
//		        			k = k + 1;
//		        			h = h + 1;
//		        			jumpXFlag++;
//		        			pointList.add(new Point(k,h));
//		        		} else {
//		        			break;
//		        		}
//	        		}
//
//	        		if (jumpXFlag < 4 && pointList.size() < 30) {
//		        		for (Point point : pointList) {
//		        			pixelFlag[point.getX()][point.getY()] = 0;
//		        		}
//		        	}
//		        }*/
//
//		    }
//		}
//
//	    for (int i = 0; i < bi.getWidth(); i++) {
//		    for (int j = 0; j < bi.getHeight(); j++)
//		    {
//		    	if (pixelFlag[i][j] == 1) {
//		    		bi.setRGB(i, j, Color.WHITE.getRGB());
//		    	}
//		    }
//		}
//		return bi;
//	}
//
//
//	/**
//	 * 预处理图像
//	 * @param bi
//	 * @return
//	 */
//	public static BufferedImage preProcessImage(BufferedImage image) {
//		BufferedImage bi = convertToBinaryImage(image);
//
//		int width = image.getWidth();
//        int height = image.getHeight();
//        int minx = image.getMinX();
//        int miny = image.getMinY();
//
//        for (int x = minx; x < width; x++) {
//            for (int y = miny; y < height; y++) {
//            	//y方向上，像素点在18到45之间
//            	if (y < 18 || y > 45) {
//            		bi.setRGB(x, y, Color.WHITE.getRGB());
//            	} else if (x == minx){
//            		//黑色像素周边像素连续性处理
//                    if (0 == (bi.getRGB(x, y) & 0xff) ) {
//                    	if ((bi.getRGB(x+1, y-1)&0xff) == 0 && ((bi.getRGB(x, y-1)&0xff) == 0 && (bi.getRGB(x+1, y)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else if ((bi.getRGB(x+1, y+1)&0xff) == 0 && ((bi.getRGB(x, y+1)&0xff) == 0 && (bi.getRGB(x+1, y)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else {
//                    		bi.setRGB(x, y, Color.WHITE.getRGB());
//                    	}
//                    }
//
//            	} else if (x == width - 1) {
//            		//黑色像素周边像素连续性处理
//                    if (0 == (bi.getRGB(x, y) & 0xff) ) {
//                    	if ((bi.getRGB(x-1, y-1)&0xff) == 0 && ((bi.getRGB(x, y-1)&0xff) == 0 && (bi.getRGB(x-1, y)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else if ((bi.getRGB(x-1, y+1)&0xff) == 0 && ((bi.getRGB(x-1, y)&0xff) == 0 && (bi.getRGB(x, y+1)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else {
//                    		bi.setRGB(x, y, Color.WHITE.getRGB());
//                    	}
//                    }
//
//            	} else {
//            		//黑色像素周边像素连续性处理
//                    if (0 == (bi.getRGB(x, y) & 0xff) ) {
//                    	if ((bi.getRGB(x+1, y-1)&0xff) == 0 && ((bi.getRGB(x, y-1)&0xff) == 0 && (bi.getRGB(x+1, y)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else if ((bi.getRGB(x+1, y+1)&0xff) == 0 && ((bi.getRGB(x, y+1)&0xff) == 0 && (bi.getRGB(x+1, y)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else if ((bi.getRGB(x-1, y-1)&0xff) == 0 && ((bi.getRGB(x, y-1)&0xff) == 0 && (bi.getRGB(x-1, y)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else if ((bi.getRGB(x-1, y+1)&0xff) == 0 && ((bi.getRGB(x-1, y)&0xff) == 0 && (bi.getRGB(x, y+1)&0xff) == 0)) {
//                    		bi.setRGB(x, y, Color.BLACK.getRGB());
//                    	} else {
//                    		bi.setRGB(x, y, Color.WHITE.getRGB());
//                    	}
//                    }
//            	}
//            }
//        }
//
//        //x或y方向上存在连续5个或5个以上黑色像素
//        for (int x = minx; x < width; x++) {
//            for (int y = miny; y < height; y++) {
//            	if (y < 18 || y > 45) {
//            		continue;
//            	} else if (0 == (bi.getRGB(x, y) & 0xff)) {
//            		//x方向连续性
//            		int countX = 1;
//            		int m = x;
//            		while (m - 1 >= minx) {
//            			if (0 == (bi.getRGB(m - 1, y) & 0xff)) {
//            				countX++;
//            				m--;
//            			} else {
//            				break;
//            			}
//            		}
//            		m = x;
//            		while (m + 1 <= width - 1) {
//            			if (0 == (bi.getRGB(m + 1, y) & 0xff)) {
//            				countX++;
//            				m++;
//            			} else {
//            				break;
//            			}
//            		}
//
//            		//y方向连续性
//            		int countY = 1;
//            		int n = y;
//            		while (n - 1 >= miny) {
//            			if (0 == (bi.getRGB(x, n - 1) & 0xff)) {
//            				countY++;
//            				n--;
//            			} else {
//            				break;
//            			}
//            		}
//            		n = y;
//            		while (n + 1 <= height - 1) {
//            			if (0 == (bi.getRGB(x, n + 1) & 0xff)) {
//            				countY++;
//            				n++;
//            			} else {
//            				break;
//            			}
//            		}
//
//            		if (countX >= 5 || countY >= 5) {
//            			bi.setRGB(x, y, Color.BLACK.getRGB());
//            		} else {
//            			bi.setRGB(x, y, Color.WHITE.getRGB());
//            		}
//            	}
//            }
//        }
//        return bi;
//    }
//
//
//}
//
