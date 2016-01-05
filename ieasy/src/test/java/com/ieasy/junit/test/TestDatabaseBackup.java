package com.ieasy.junit.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.Test;

import com.ieasy.basic.util.MySQLUtil;
import com.ieasy.basic.util.file.FileUtils;

public class TestDatabaseBackup {

	@Test
	public void mysqlBackupTest() {
		try {
			/*StringBuffer cmd = new StringBuffer();
			cmd.append("cmd /c mysqldump" + " " + "ieasy");
			cmd.append(" -h localhost ") ;
			cmd.append(" -u " + "root") ;
			cmd.append(" -p" + "yhqmcq") ;
			cmd.append(" --default-character-set=utf8 --result-file=" + "+d://work//dir//ieasy.sql+") ;
			
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(cmd.toString());*/
			
			/*String t = "cmd /c dir c://" ;
			Process p = Runtime.getRuntime().exec(t) ;
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream())) ;
			String s = null ;
			
			while((s = r.readLine()) != null) {
				System.out.println(s);
			}
			r.close() ;*/
			
			String cmd = "cmd /c mysqldump -uroot -pyhqmcq ieasy" ;
			Process proc = Runtime.getRuntime().exec(cmd) ;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream())) ;
			BufferedWriter bw = new BufferedWriter(new FileWriter("c://cc//ieasy.sql")) ;
			String str = null ;
			
			while((str = br.readLine()) != null) {
				bw.write(str) ;
				bw.newLine() ;
			}
			
			br.close() ;
			bw.flush() ;
			bw.close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMysqlUtilBackup() {
		MySQLUtil msu = MySQLUtil.getInstance() ;
		msu.setCfg("ieasy", "c://abc", "ieasy", "root", "yhqmcq") ;
		try {
			msu.backup() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMysqlUtilResume() {
		MySQLUtil msu = MySQLUtil.getInstance() ;
		msu.setCfg("ieasy", "c://abc", "ieasy", "root", "yhqmcq") ;
		try {
			msu.resume() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testw() {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			String cmd = "cmd /c mysqldump -uroot -pyhqmcq ieasy";
			Process proc = Runtime.getRuntime().exec(cmd);
			
			String outputPath = "c:\\a.sql" ;
			FileUtils.createFile(outputPath) ;
			br = new BufferedReader(new InputStreamReader(proc.getInputStream(),"UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));
			String str = null;
			while((str=br.readLine())!=null) {
				bw.write(str);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br!=null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(bw!=null) bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testr() {
		String s = "c:\\" ;
		String ss = "a" ;
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			String cmd = "cmd /c mysql -uroot -pyhqmcq ieasy";
			Process proc = Runtime.getRuntime().exec(cmd);
			bw = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
			br = new BufferedReader(new FileReader(s+File.separator+ss+".sql"));
			String str = null;
			while((str=br.readLine())!=null) {
				bw.write(str);
				bw.newLine();
			}
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br!=null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(bw!=null) bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
