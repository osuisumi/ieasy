package com.ieasy.basic.util.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.connectors.FTPProxyConnector;
import it.sauronsoftware.ftp4j.connectors.HTTPTunnelConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpSession {
	
	private static final Logger log = LoggerFactory.getLogger(FtpSession.class);
	
	private String host;
	private int port;
	private String username;
	private String password;
	private FTPClient client;
	
	/**
	 * 匿名用户, 密码任意设置
	 * @param host String FTP服务器
	 * @param port int FTP服务端口
	 */
	public FtpSession(String host, int port) {
		this(host, port, "anonymous", "");
	}
	
	public FtpSession(String host, int port, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		client = new FTPClient();
	}
	
	/**
	 * 设置字符编码
	 * @param charset String 字符编码
	 */
	public void setCharset(String charset){
		client.setCharset(charset); //设置字符编码
	}
	
	
	// * 设置传输模式 . TYPE_TEXTUAL ASC码, TYPE_BINARY 二进制, TYPE_AUTO 自动选择(根据文件内容)
	 
	public void useTextualType(){
		client.setType(FTPClient.TYPE_TEXTUAL); 
	}
	
	public void useBinaryType(){
		client.setType(FTPClient.TYPE_BINARY); 
	}
	
	public void useAutoType(){
		client.setType(FTPClient.TYPE_AUTO);
	}
	
	/**
	 * 使用SSL套接字连接
	 */
	public void useSSLConnector(){
//		client.setConnector(new SSLConnector());
	}
	
	/**
	 *使用FTP代理连接器
	 */
	public void useFtpProxyConnector(String proxyHost, int proxyPort){
		client.setConnector(new FTPProxyConnector(proxyHost, proxyPort));
	}
	
	/**
	 *使用HTTP代理连接器
	 */
	public void useHttpProxyConnector(String proxyHost, int proxyPort){
		client.setConnector(new HTTPTunnelConnector(proxyHost, proxyPort));
	}
	
	public void open(){
		try {
			//连接到指定的FTP服务器(域名或IP) 不指定端口，则使用默认端口21
			client.connect(host, port);
			//登录验证
			client.login(username, password);
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (FTPIllegalReplyException e) {
			log.error(e.getMessage(), e);
		} catch (FTPException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void close(){
		try {
			//安全退出
			client.disconnect(true);
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (FTPIllegalReplyException e) {
			log.error(e.getMessage(), e);
		} catch (FTPException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 将本地文件上传到FTP服务器
	 * @param localFile String 本地文件
	 */
	public void upload(String localFile){
		try {
			//上传 本地 文件localFile到FTP服务器的当前目录
			client.upload(new java.io.File(localFile), new DefaultTransferListener());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
	}
	
	/**
	 * 获取根目录列表
	 * @return List<String>
	 */
	public List<String> getRootList(){
		List<String> nameList = new ArrayList<String>();
		try {
			FTPFile[] list = client.list();
			for(FTPFile ftpFile : list){
				nameList.add(ftpFile.getName());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
		
		return nameList;
	}

}

//上传和下载文件时， 监听文件传输的状态
class DefaultTransferListener implements FTPDataTransferListener {
	private static final Logger log = LoggerFactory.getLogger(DefaultTransferListener.class);

    //文件开始上传或下载时触发
    public void started() {
    	log.info("开始向FTP服务器上传文件. ");
    }
    //显示已经传输的字节数
    public void transferred(int length) {
    	log.info("已经传输的字节数 : " + length);
    }
    //文件传输完成时，触发
    public void completed() {
    	log.info("向FTP服务器上传文件完成. ");
    }
    //传输放弃时触发
    public void aborted() {
        // Transfer aborted
    	log.info("放弃向FTP服务器上传文件. ");
    }
    //传输失败时触发
    public void failed() {
    	log.info("向FTP服务器上传文件失败. ");
    }
}
