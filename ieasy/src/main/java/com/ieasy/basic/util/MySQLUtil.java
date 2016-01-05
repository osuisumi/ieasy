package com.ieasy.basic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.ieasy.basic.util.file.FileUtils;

/**
 * 数据库备份和恢复
 * 
 * @author Administrator
 * 
 */
public class MySQLUtil {
	private static MySQLUtil util = null;
	/**
	 * 要备份的文件名
	 */
	private String filename;
	/**
	 * 需要备份到哪个文件夹中
	 */
	private String backupDir;
	/**
	 * 需要备份的几个基本信息
	 */
	private String database;
	private String username;
	private String password;

	private MySQLUtil() {
	}

	public static MySQLUtil getInstance() {
		if (util == null)
			util = new MySQLUtil();
		return util;
	}

	public void setCfg(String backupDir, String filename, String database, String username, String password) {
		this.filename = filename;
		this.backupDir = backupDir;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void backup() throws Exception {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		String outStr = null;
		String inStr = null;
		String fPath = null ;
		try {
			fPath = backupDir + File.separator + filename;
			FileUtils.createFile(fPath);
			
			Runtime rt = Runtime.getRuntime();
			Process child = rt.exec("mysqldump -h localhost -u" + username + " -p" + password + " " + database);
			in = child.getInputStream();
			isr = new InputStreamReader(in, "utf-8");

			StringBuffer sb = new StringBuffer("");

			br = new BufferedReader(isr);
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			fos = new FileOutputStream(fPath);
			osw = new OutputStreamWriter(fos, "utf-8");
			osw.write(outStr);
			
			osw.flush();
		} catch (Exception e) {
			FileUtils.deleteFile(fPath) ;
			throw new Exception(e) ;
		} finally {
			try {
				if (in != null)
					in.close();
				if (isr != null)
					isr.close();
				if (br != null)
					br.close();
				if (osw != null)
					osw.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void resume() throws Exception {
		BufferedReader br = null;
		OutputStream out = null;
		OutputStreamWriter writer = null;

		String outStr = null;
		String inStr = null;
		try {
			String fPath = backupDir + File.separator + filename;
			Runtime rt = Runtime.getRuntime();

			Process proc = rt.exec("mysql -u" + username + " -p" + password + " " + database);
			out = proc.getOutputStream();

			StringBuffer sb = new StringBuffer("");

			br = new BufferedReader(new InputStreamReader(new FileInputStream(fPath), "utf8"));
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			writer = new OutputStreamWriter(out, "utf8");
			writer.write(outStr);

			// 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
			writer.flush();
		} catch (Exception e) {
			throw new Exception("数据库恢复失败!") ;
		} finally {
			try {
				if (out != null)
					out.close();
				if (br != null)
					br.close();
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
