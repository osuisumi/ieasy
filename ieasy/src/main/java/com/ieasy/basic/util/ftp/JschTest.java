package com.ieasy.basic.util.ftp;

import java.util.Date;
import java.util.List;

import com.ieasy.basic.util.date.DateUtils;
import com.jcraft.jsch.ChannelSftp;

public class JschTest {

	public static void main(String[] args)throws Exception {
		test1();
	}
	
	public static void test1()throws Exception{
		String ftpUserName = "root";
		String ftpPassword = "BX0zsFsSu5CG";
		String ftpHost = "203.195.147.47";
		int ftpPort = 22;
		
		SftpClient sftpClient = new SftpClient(ftpHost, ftpPort, ftpUserName, ftpPassword);
		sftpClient.connect();
		sftpClient.cd("/hedata/backup");
		List<ChannelSftp.LsEntry> list = sftpClient.ls();
		
		long lastTime = 0;
		ChannelSftp.LsEntry lastLsEntry = null;
		
		for(ChannelSftp.LsEntry lsEntry : list){
			String backupFileName = lsEntry.getFilename();
        	if(backupFileName.startsWith("phoenix_") && (!backupFileName.startsWith("phoenix_pay_"))){
        		long time = parseTime(backupFileName); 
        		if(time > lastTime){
        			lastTime = time;
        			lastLsEntry = lsEntry;
        		}
        	}
		}
		
		System.out.println(lastLsEntry.getFilename());
		
		sftpClient.close();
	}
	
	public static void test2(){
		String s = "phoenix_20131029180001.gz";
		long time = parseTime(s);
		System.out.println(new Date(time));
	}
	
	private static long parseTime(String backupFileName){
		String timeString = backupFileName.split("_")[1].split("\\.")[0];
		return DateUtils.parse(timeString, "yyyyMMddHHmmss").getTime();
	}

}
