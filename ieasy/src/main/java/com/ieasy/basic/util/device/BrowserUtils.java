package com.ieasy.basic.util.device;

import javax.servlet.http.HttpServletRequest;

/**
 * 和浏览器有关的工具类
 *
 * @author HenryYan
 *
 */
public class BrowserUtils {

	/**
	 * 判断是否为IE浏览器
	 */
	public static boolean isIE(HttpServletRequest request) {
		String agent = getAgent(request);
		if (null != agent && -1 != agent.indexOf("MSIE")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为IE11浏览器
	 */
	public static boolean isIE11(HttpServletRequest request) {
		String agent = getAgent(request);
		if (null != agent && -1 != agent.indexOf("rv:11.0")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为FireFox浏览器
	 */
	public static boolean isMozilla(HttpServletRequest request) {
		String agent = getAgent(request);
		if (null != agent && -1 != agent.indexOf("Mozilla")) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为Chrome或者Safari
	 */
	public static boolean isWebKit(HttpServletRequest request) {
		String agent = getAgent(request);
		if (null != agent && -1 != agent.indexOf("WebKit")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取浏览器USER-AGENT
	 */
	public static String getAgent(HttpServletRequest request) {
		return request.getHeader("USER-AGENT") ;
	}
	
	/**
	 * 是否Ajax异步请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxReqeuest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if(null != requestType) 
			return true ;
		else 
			return false ;
	}

}
