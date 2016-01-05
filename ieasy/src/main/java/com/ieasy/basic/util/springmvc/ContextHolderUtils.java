package com.ieasy.basic.util.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
* @ClassName: ContextHolderUtils 
* @Description: TODO(上下文工具类) 
* @author  张代浩 
* @date 2012-12-15 下午11:27:39 
*
 */
public class ContextHolderUtils {
	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes() ;
		if(null != requestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest() ;
			return request ;
		} else {
			return null ;
		} 

	}
	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		if(null != getRequest()) {
			return getRequest().getSession();
		} else {
			return null ;
		}
	}

}
