package com.lich0079.iteye_backup_blog;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileUtil {

	public static void writeToFile(String title, String content) throws IOException{
        	String path = FileUtil.class .getResource("/").getPath()+title+".html";
        	System.out.println("write to "+path);
        	Writer out = new BufferedWriter(new OutputStreamWriter(
        		    new FileOutputStream(path), "UTF-8"));
        	out.write(content);
        	out.close();
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("111");
		Thread.sleep(10000);
		System.out.println("222");
	}
}
