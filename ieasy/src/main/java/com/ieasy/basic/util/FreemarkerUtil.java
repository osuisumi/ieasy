package com.ieasy.basic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ieasy.module.system.web.form.UserForm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtil {
	private static FreemarkerUtil util;
	private static Configuration cfg = new Configuration();
	private FreemarkerUtil() {
	}
	
	public static FreemarkerUtil getInstance(String pname) {
		if(util==null) {
			cfg.setEncoding(Locale.getDefault(), "UTF-8") ;
			cfg.setClassicCompatible(true) ;
			cfg.setClassForTemplateLoading(FreemarkerUtil.class, pname);
			
			util = new FreemarkerUtil();
		}
		return util;
	}
	
	private Template getTemplate(String fname) {
		try {
			Template template = cfg.getTemplate(fname, "UTF-8"); 
			template.setEncoding("UTF-8");
			return template ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过标准输出流输出模板的结果
	 * @param root 数据对象
	 * @param fname 模板文件
	 */
	public void sprint(Map<String,Object> root,String fname) {
		try {
			Template template = cfg.getTemplate(fname, "UTF-8"); 
			template.setEncoding("UTF-8");
			template.process(root, new PrintWriter(System.out));
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 基于文件的输出
	 * @param root
	 * @param fname
	 * @param outpath
	 */
	public void fprint(Map<String,Object> root,String fname,String outpath) {
		try {
			getTemplate(fname).process(root, new FileWriter(outpath));
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回生成的HTML
	 * @param root
	 * @param fname
	 * @return
	 */
	public String templateToString(Map<String,Object> root,String fname) {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(getTemplate(fname), root) ;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean generate(Map<String,Object> root, String fname, String outpath) {
		boolean flag = false ;
		
		FileOutputStream fos = null ;
		OutputStreamWriter osw = null ;
		BufferedWriter bw = null ;
			try {
				Template tmp = getTemplate(fname) ;
				File file = new File(outpath) ;
				fos = new FileOutputStream(file) ;
				osw = new OutputStreamWriter(fos,"UTF-8") ;
				bw = new BufferedWriter(osw) ;
				//输出HTML
				tmp.process(root, bw) ;
				flag = true ;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			} finally {
				try {
					if(null != bw) 
						bw.flush() ;
					if(null != bw) 
						osw.flush() ;
					if(null != bw) 
						fos.flush() ;
					
					if(null != bw) 
						bw.close() ;
					if(null != osw) 
						osw.close() ;
					if(null != fos) 
						fos.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		return flag ;
	}
	
	
	public static void main(String[] args) {
		UserForm u = new UserForm() ;
		u.setId("1") ;
		u.setName("鸟语") ;
		
		List<UserForm> list = new ArrayList<UserForm>() ;
		
		Map<String,Object> root = new HashMap<String, Object>() ;
		root.put("title", "你好") ;
		root.put("aaa", "aa") ;
		root.put("list", list) ;
		
		FreemarkerUtil.getInstance("/ftl").sprint(root, "/test.ftl") ;
	}
}

