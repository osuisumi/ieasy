package com.ieasy.module.common.web.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;

import com.ieasy.basic.util.SystemPath;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.springmvc.ContextHolderUtils;
import com.ieasy.module.system.web.form.AuthForm;
import com.ieasy.module.system.web.form.DictJson;
import com.ieasy.module.system.web.form.GlobalForm;
import com.ieasy.module.system.web.form.LoginSession;

public class WebContextUtil {
	
	private static WebApplicationContext wac;
	
	/**
	 * 上下文
	 */
	private static ServletContext sc ;
	
	/**
	 * 附件上传的根目录
	 */
	private static String uploadDir ;
	
	/**
	 * 获得附件上传的根路径
	 */
	private static String attachedPath ;
	
	/**
	 * 数据字典
	 */
	public static Map<String, List<DictJson>> dicts ;
	
	/**
	 * 获取系统全局参数
	 */
	public static GlobalForm global ;
	
	/**
	 * 上下文的绝对路径
	 * @param realPath
	 * @return
	 */
	public static String getRealPath(String realPath) {
		return getSc().getRealPath(realPath);
	}
	
	/**
	 * 项目的相对路径
	 * @return
	 */
	public static String getWebAppContextPath() {
		return getSc().getContextPath() ;
	}
	

	public static String getAttachedPath() {
		return attachedPath;
	}

	public static void setAttachedPath(String attachedPath) {
		WebContextUtil.attachedPath = attachedPath;
	}

	
	public static GlobalForm getGlobal() {
		return global;
	}

	public static void setGlobal(GlobalForm global) {
		WebContextUtil.global = global;
	}

	public static String getAttachedPath(String path) {
		if(null != path && !"".equals(path.trim())) {
			attachedPath = getRealPath(getUploadDir() + SystemPath.getSeparator() + path) ;
		} else {
			attachedPath = getRealPath(getUploadDir()) ;
		}
		return attachedPath;
	}

	public static Map<String, List<DictJson>> getDicts() {
		return dicts;
	}

	public static void setDicts(Map<String, List<DictJson>> dicts) {
		WebContextUtil.dicts = dicts;
	}

	public static String getUploadDir() {
		return uploadDir;
	}

	public static void setUploadDir(String uploadDir) {
		WebContextUtil.uploadDir = uploadDir;
	}

	private WebContextUtil(){}
	
	public static void setWac(WebApplicationContext wac) {
		WebContextUtil.wac = wac;
	}
	
	public static WebApplicationContext getWac() {
		return wac;
	}
	
	public static Object getBean(String name) {
		return wac.getBean(name);
	}

	public static ServletContext getSc() {
		return sc;
	}

	public static void setSc(ServletContext sc) {
		WebContextUtil.sc = sc;
	}
	
	/**
	 * 获取当前登陆用户对象
	 * @return
	 */
	public static LoginSession getCurrentUser() {
		if(null != ContextHolderUtils.getSession()) {
			return (LoginSession)ContextHolderUtils.getSession().getAttribute(Const.USER_SESSION) ;
		} else {
			return null ;
		}
	}
	
	/**
	 * 获取当前用户的权限
	 * @return
	 */
	public static AuthForm getCurrentUserAuth() {
		HttpSession session = ContextHolderUtils.getSession() ;
		if(null != session) {
			LoginSession ls = (LoginSession)session.getAttribute(Const.USER_SESSION) ;
			if(null != ls) {
				return ls.getAuth() ;
			}
		}
		return null ;
	}
	
}
