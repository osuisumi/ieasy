package com.ieasy.basic.util.ftp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClient {

	private static final Logger LOG = LoggerFactory.getLogger(SftpClient.class);
			
	private String ftpHost;
	private int ftpPort;
	private String ftpUserName;
	private String ftpPassword;
	
	private Session session;
	private ChannelSftp channel;
	
	public SftpClient(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword) {
		super();
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
		this.ftpUserName = ftpUserName;
		this.ftpPassword = ftpPassword;
	}
	
	public void connect() throws JSchException{
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        LOG.debug("Session created.");
        if (ftpPassword != null) {
            session.setPassword(ftpPassword); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(0); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        LOG.debug("Session connected.");

        LOG.debug("Opening Channel.");
        channel = (ChannelSftp)session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        LOG.debug("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
                + ", returning: " + channel);
	}
	
	public void close(){
		if(channel != null){
			channel.disconnect();
		}
		if(session != null){
			session.disconnect();
		}
	}
	
	public void cd(String path) throws SftpException{
		if(channel != null){
			channel.cd(path);
		}
	}
	
	public List<ChannelSftp.LsEntry> ls() throws SftpException{
		List<ChannelSftp.LsEntry> list = new ArrayList<ChannelSftp.LsEntry>();
		
		if(channel != null){
			Vector<?> vector = channel.ls(".");
	        for(Object v : vector){
	        	ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry)v;
	        	list.add(lsEntry);
	        }
		}
		
		return list;
	}
	
}
