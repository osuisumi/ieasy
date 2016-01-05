package com.ieasy.basic.util.ftp;

import java.io.File;
import java.net.URL;
import java.util.List;

public class FtpTest {

	public static void main(String[] args)throws Exception {
		test1();
	}
	
	public static void test()throws Exception{
		FtpSession session = new FtpSession("192.168.9.102", 2221, "hxrainbow", "hxrainbow@123");
//		session.setCharset("GBK");
		//很奇怪, 默认传输模式下上传文件为0字节
		session.useBinaryType(); //
		
		session.open();
		
//		session.upload("d:/key.txt");
		List<String> nameList = session.getRootList();
		for(String name : nameList){
			System.out.println(name);
		}
		session.close();
	}
	
	public static void test1()throws Exception{
		
		FtpTool.upload(new URL("ftp://localhost:21/test1"), "admin", "123", new File("d:/test/附件.txt"));
	}
	
}
