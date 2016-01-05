package com.ieasy.basic.util.samba;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.util.ConfigUtil;

/**
 * 访问远程共享目录工具类
 * @author Administrator
 *
 */
public class SambaUtil {

	private static Log log = LogFactory.getLog(SambaUtil.class);

	private static SambaUtil sambaUtil = null;

	
	//远程共享目录的根路径
	private static String remoteSmbDir ;
	
	static {
		remoteSmbDir = ConfigUtil.get("remoteSmbDir") ;
	}

	private SambaUtil() {}

	public static synchronized SambaUtil getInstance() {
		if (null == sambaUtil) {
			sambaUtil = new SambaUtil();
		}
		return sambaUtil;
	}

	
	/**
	 * 上传文件到远程共享目录
	 * @param remoteDri 要上传到远程共享目录的文件路径，如：/a.txt（文件的名称以这个为准）
	 * @param filepath 要上传的文件路径
	 */
	public static void putSmb(String remoteDri, String filepath) {
		File localFile = new File(filepath);
		putSmb(remoteDri, localFile) ;
	}
	
	/**
	 * 上传文件到远程共享目录
	 * @param remoteDri 要上传到远程共享目录的文件路径，如：/a.txt（文件的名称以这个为准）
	 * @param filepath 要上传的文件路径
	 */
	public static void putSmb(String remoteDri, File localFile) {
		InputStream in = null;
		OutputStream out = null;
		SmbFile smbFile = null ;
		try {
			long startTime = System.currentTimeMillis() ;
			smbFile = new SmbFile(remoteSmbDir + remoteDri);
		
			smbFile.connect(); // 尝试连接
			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(smbFile));
			
			byte[] buffer = new byte[4096];
			int len = 0; // 读取长度
			while ((len = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush(); // 刷新缓冲的输出流
			long endTime = System.currentTimeMillis() ;
			log.info("文件上传完成，耗时["+((endTime-startTime))+" 毫秒] 路径：" + smbFile) ;
		} catch (MalformedURLException e) {
			e.printStackTrace() ;
		} catch (FileNotFoundException e) {
			log.error("找不到文件", e);
		} catch (SmbException e) {
			log.error("连接远程共享目录超时：" + smbFile, e);
			throw new ServiceException("连接远程共享目录超时") ;
		} catch (UnknownHostException e) {
			log.error("链接远程主机失败", e);
		} catch (IOException e) {
			log.error("文件读取异常", e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 从远程共享目录中下载文件
	 * @param remoteFilePath 要从远程共享目录中下载的文件路径，以“/”开头，如：/abc/test.txt
	 * @param localDir 文件下载到本地目录中的哪个目录里，如：c:\\
	 */
	public static void getSmb(String remoteFilePath, String localDir) {
		InputStream in = null;
		OutputStream out = null;
		try {
			long startTime = System.currentTimeMillis() ;
			SmbFile remoteFile = new SmbFile(remoteSmbDir + remoteFilePath);
			remoteFile.connect();
			
			String fileName = remoteFile.getName();
			File localFile = new File(localDir + File.separator + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
			long endTime = System.currentTimeMillis() ;
			log.info("文件下载完成，耗时["+((endTime-startTime))+" 毫秒] 下载文件："+remoteFilePath+"  输出路径：" + localDir) ;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		putSmb("/ieasy.log", "D:\\ieasy.log") ;
		//getSmb("/dotNetFx40_Full_x86_x64.exe", "D:") ;
	}
}
