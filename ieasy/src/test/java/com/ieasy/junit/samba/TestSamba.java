package com.ieasy.junit.samba;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class TestSamba {

	private static TestSamba sambaUtil = null;

	private static String remoteSmbShare = "smb://vm-yhq:yhqmcq@192.168.112.131/backup";

	private TestSamba() {
	}

	public static synchronized TestSamba getInstance() {
		if (null == sambaUtil) {
			sambaUtil = new TestSamba();
		}
		return sambaUtil;
	}

	public static void putSmb() {
		InputStream in = null;
		OutputStream out = null;
		try {
			// 获取图片
			File localFile = new File("D:/ieasy.log");
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS_");
			SmbFile smbFile = new SmbFile(remoteSmbShare + "/" + fmt.format(new Date()) + localFile.getName());
			smbFile.connect(); // 尝试连接

			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(smbFile));

			byte[] buffer = new byte[4096];
			int len = 0; // 读取长度
			while ((len = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush(); // 刷新缓冲的输出流
		} catch (Exception e) {
			String msg = "发生错误：" + e.getLocalizedMessage();
			System.out.println(msg);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public static void getSmb(String fileDirPath, String localDir) {
		InputStream in = null;
		OutputStream out = null;
		try {
			SmbFile remoteFile = new SmbFile(remoteSmbShare + "/" + fileDirPath);
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
		putSmb();
		getSmb("20140910181224515_ieasy.log", "c:\\") ;
	}

}
