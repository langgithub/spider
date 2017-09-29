package com.champion.spider.utils;

import java.io.*;
import java.util.Scanner;

/**
 * Created by root on 2017/9/7.
 */
public class HandDaMa {

    public String scannerAfterSave(InputStream inputStream,String savePath) throws IOException {
        FileOutputStream fos=new FileOutputStream(savePath);
		byte[] buff=new byte[1024];
		int len=0;
		while((len=inputStream.read(buff))!=-1){
			fos.write(buff, 0, len);
		}
		fos.flush();
		fos.close();

		System.out.println("输入code:");
		Scanner scanner=new Scanner(System.in);
		String nextLine = scanner.nextLine();
        return  nextLine;
    }

    public OutputStream ouputstreamAfterSave(InputStream inputStream,String savePath) throws IOException {
        FileOutputStream fos=new FileOutputStream(savePath);
        byte[] buff=new byte[1024];
        int len=0;
        while((len=inputStream.read(buff))!=-1){
            fos.write(buff, 0, len);
        }
        fos.flush();
        fos.close();
        return new FileOutputStream(new File(savePath));
    }

}
