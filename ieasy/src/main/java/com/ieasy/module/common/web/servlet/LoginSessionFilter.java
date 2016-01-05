package com.ieasy.module.common.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.device.BrowserUtils;
import com.ieasy.module.system.web.form.LoginSession;

public class LoginSessionFilter implements Filter {

	private String[] excludeUrls ;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.excludeUrls = filterConfig.getInitParameter("excludeUrls").split(",") ;

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req ;
		HttpServletResponse response = (HttpServletResponse) resp ;
		
		boolean flag = false ;
		String requestUri = request.getRequestURI();
		
		//判断URL地址是否不需验证，这里的不需验证只针对登陆或登出及后台主页
		for(int i=0; i<excludeUrls.length; i++) {
			if (requestUri.indexOf(excludeUrls[i]) > -1) {
				flag = true ;
				chain.doFilter(request, response) ;
			}
		}
		
		/**
		 * flag为false的话则是需要验证的URL地址，验证地址前先判断该用户是否已经登陆 
		 * 如果使用Spring MVC的拦截器来拦截未登陆跳转，会发生response不能调用的异常
		 */
		if(!flag) {
			LoginSession loginSession = (LoginSession) request.getSession().getAttribute(Const.USER_SESSION);
			if(null == loginSession) {
				if (BrowserUtils.isAjaxReqeuest(request)) {
					response.setContentType("text/html;charset=utf-8");
					PrintWriter out = response.getWriter();
					out.print("您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
					out.flush();
					out.close();
					out = null ;
					return ;
				} else {
					request.getRequestDispatcher("/common/errors/noSession.jsp").forward(request, response);
					return;
				}
			}
			chain.doFilter(request, response) ;
		}
	}

	@Override
	public void destroy() {
	}

}
