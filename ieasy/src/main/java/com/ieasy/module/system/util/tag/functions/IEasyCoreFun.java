package com.ieasy.module.system.util.tag.functions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.web.form.AuthForm;


/**
 * 自定义函数库
 * @author Administrator
 *
 */
public class IEasyCoreFun {

	public static boolean hasPermit(String auth) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		
		//判断是否加上项目名称，是URL路径的才加
		if(auth.indexOf(".do") != -1) {
			if(auth.indexOf(request.getContextPath()) == -1) {
				auth = request.getContextPath() + auth ;
			}
		}
		
		AuthForm af = WebContextUtil.getCurrentUserAuth() ;
		List<String> authUrls = af.getAuthUrl() ;
		if(authUrls.contains(auth)) {
			return true ;
		} else {
			return false ;
		}
	}
	
}
