package com.ieasy.basic.util.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ieasy.basic.util.file.FileUtils;

public class FtpTool {
	
	private static final Logger log = LoggerFactory.getLogger(FtpTool.class);
	
	public static void download(URL url, String username, String password, File file){
		download(url, username, password, "UTF-8", file);
	}
	public static void download(URL url, String username, String password, String encoding, File file){
		FTPClient client = new FTPClient();
		client.setCharset(encoding);
		client.setType(FTPClient.TYPE_BINARY); 
		//连接到指定的FTP服务器(域名或IP) 不指定端口，则使用默认端口21
		try {
			int port = url.getPort();
			if(port < 1){
				port = 21;
			}
			client.connect(url.getHost(), port);
			//登录验证
			client.login(username, password);
//			client.changeDirectory(url.getPath());
			client.download(url.getFile(), file);
			client.logout();
			//安全退出
			client.disconnect(true);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}
	
	public static void downloadFolder(URL url, String username, String password, File folder){
		downloadFolder(url, username, password, "UTF-8", folder);
	}
	public static void downloadFolder(URL url, String username, String password, String encoding, File folder){
		FTPClient client = new FTPClient();
		client.setCharset(encoding);
		client.setType(FTPClient.TYPE_BINARY); 
		//连接到指定的FTP服务器(域名或IP) 不指定端口，则使用默认端口21
		try {
			int port = url.getPort();
			if(port < 1){
				port = 21;
			}
			client.connect(url.getHost(), port);
			//登录验证
			client.login(username, password);
			downloadFolder(client, url, folder);
			client.logout();
			//安全退出
			client.disconnect(true);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}
	private static void downloadFolder(FTPClient client, URL url, File folder)throws Exception{
		client.changeDirectory(url.getPath());
		FTPFile[] ftpFiles = client.list();
		if(!folder.exists()){
			folder.mkdirs();
		}
		for(FTPFile ftpFile : ftpFiles){
			String name = ftpFile.getName();
			if(ftpFile.getType() == FTPFile.TYPE_FILE){
				client.changeDirectory(url.getPath());
				File f = new File(folder, name);
				client.download(name, f);
			}else if(ftpFile.getType() == FTPFile.TYPE_DIRECTORY){
				downloadFolder(client, newURL(url, name), new File(folder, name));
			}
		}
	}
	
	public static URL newURL(URL parentUrl, String child)throws MalformedURLException {
		String path = parentUrl.getPath();
		if(!path.endsWith("/")){
			path += "/";
		}
		path += child;
		return new URL(parentUrl, path);
	}
	
	public static void upload(URL url, String username, String password, File file){
		upload(url, username, password, "UTF-8", file);
	}
	public static void upload(URL url, String username, String password, String encoding, File file){
		FTPClient client = new FTPClient();
		client.setCharset(encoding);
		client.setType(FTPClient.TYPE_BINARY); 
		//连接到指定的FTP服务器(域名或IP) 不指定端口，则使用默认端口21
		try {
			int port = url.getPort();
			if(port < 1){
				port = 21;
			}
			client.connect(url.getHost(), port);
			//登录验证
			client.login(username, password);
			client.changeDirectory(url.getPath());
			client.upload(file);
			client.logout();
			//安全退出
			client.disconnect(true);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}
	
	public static void uploadFolder(URL url, String username, String password, File folder){
		uploadFolder(url, username, password, "UTF-8", folder);
	}
	public static void uploadFolder(URL url, String username, String password, String encoding, File folder){
		FTPClient client = new FTPClient();
		client.setCharset(encoding);
		client.setType(FTPClient.TYPE_BINARY); 
		//连接到指定的FTP服务器(域名或IP) 不指定端口，则使用默认端口21
		try {
			int port = url.getPort();
			if(port < 1){
				port = 21;
			}
			client.connect(url.getHost(), port);
			//登录验证
			client.login(username, password);
			client.changeDirectory(url.getPath());
			String[] files = folder.list();
			for(String file : files){
				File f = new File(folder, file);
				if(f.isDirectory()){
					uploadFolder(client, url, f);
				}else{
					client.changeDirectory(url.getPath());
					client.upload(f);
				}
			}
			client.logout();
			//安全退出
			client.disconnect(true);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}
	private static void uploadFolder(FTPClient client, URL parentUrl, File folder)throws Exception{
		client.changeDirectory(parentUrl.getPath());
		if(!existsFtpDir(client, folder.getName())){
			client.createDirectory(folder.getName());
		}
		client.changeDirectory(folder.getName());
		
		String[] fileList = folder.list();
		if(fileList == null){
			return ;
		}
		
		for(String file : fileList){
			File f = new File(folder, file);
			if(f.isDirectory()){
				uploadFolder(client, newURL(parentUrl, folder.getName()), f);
			}else if(f.isFile()){
				client.changeDirectory(parentUrl.getPath());
				client.changeDirectory(folder.getName());
				client.upload(f);
			}
		}
	}
	
	private static boolean existsFtpDir(FTPClient client, String dir)throws Exception{
		FTPFile[] ftpFiles = client.list();
		for(FTPFile ftpFile : ftpFiles){
			if((ftpFile.getType() == FTPFile.TYPE_DIRECTORY) && (dir.equals(ftpFile.getName()))){
				return true;
			}
		}
		return false;
	}
	
	public static boolean exists(URL url, String username, String password){
		return exists(url, username, password, "UTF-8");
		
	}
	public static boolean exists(URL url, String username, String password, String encoding){
		FTPClient client = new FTPClient();
		client.setCharset(encoding);
		client.setType(FTPClient.TYPE_BINARY);
		try {
			int port = url.getPort();
			if(port < 1){
				port = 21;
			}
			String[] welcome = client.connect(url.getHost(), port);
			log.info("welcome = " + welcome.length);
			//登录验证
			client.login(username, password);
			String path = url.getPath();
			log.info("path = " + path);
			if(path.indexOf(".") != -1){//文件
				int index = path.lastIndexOf("/");
				String p = path.substring(0, index);
				String f = path.substring(index + 1);
				log.info("p = " + p + ", f = " + f);
				client.changeDirectory(p);
				FTPFile[] ftpFiles = client.list();
				for(FTPFile ftpFile : ftpFiles){
					if((ftpFile.getType() == FTPFile.TYPE_FILE) && (ftpFile.getName().equals(f))){
						return true;
					}
				}
				return false;
			}else{//文件夹
				client.changeDirectory(path);
			}
			client.logout();
			//安全退出
			client.disconnect(true);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return false;
		}
		
		return true;
	}
	
	//FTP服务器之间复制文件夹
	public static void copyFolder(URL sourceUrl, String sourceUsername, String sourcePassword, String sourceEncoding, URL destUrl, String destUsername, String destPassword, String destEncoding, String tmpDir)throws IOException{
		downloadFolder(sourceUrl, sourceUsername, sourcePassword, sourceEncoding, new File(tmpDir));
		uploadFolder(destUrl, destUsername, destPassword, destEncoding, new File(tmpDir));
		FileUtils.deleteDirectory(tmpDir);
	}
	public static void copyFolder(URL sourceUrl, String sourceUsername, String sourcePassword, URL destUrl, String destUsername, String destPassword, String tmpDir)throws IOException{
		copyFolder(sourceUrl, sourceUsername, sourcePassword, "UTF-8", destUrl, destUsername, destPassword, "UTF-8", tmpDir);
	}
	
	public static void delete(URL url, String username, String password){
		delete(url, username, password, "UTF-8");
	}
	public static void delete(URL url, String username, String password, String encoding){
		FTPClient client = new FTPClient();
		client.setCharset(encoding);
		client.setType(FTPClient.TYPE_BINARY); 
		//连接到指定的FTP服务器(域名或IP) 不指定端口，则使用默认端口21
		try {
			int port = url.getPort();
			if(port < 1){
				port = 21;
			}
			client.connect(url.getHost(), port);
			//登录验证
			client.login(username, password);
			client.deleteFile(url.getPath());
			client.logout();
			//安全退出
			client.disconnect(true);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}
	
	public static void main(String args[])throws Exception{
		FtpTool.uploadFolder(new URL("ftp://localhost:21/test/"), "admin", "123", new File("d:/test"));
//		FtpTool.downloadFolder(new URL("ftp://localhost:21/test/"), "admin", "123", new File("d:/dbtest"));
//		changeDir(new URL("ftp://localhost:21/test/"));
		System.out.println("OK!");
	}

}
