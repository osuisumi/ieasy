package com.ieasy.module.common.auth;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.device.BrowserUtils;
import com.ieasy.module.system.web.form.LoginSession;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

	private List<String> excludeUrls;// 不需要拦截的资源

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI();

		//不需验证的资源
		for (String str : excludeUrls) {
			if (requestUri.indexOf(str) > -1) {
				return super.preHandle(request, response, handler);
			}
		}
		
		LoginSession loginSession = (LoginSession) request.getSession().getAttribute(Const.USER_SESSION);
		//验证访问资源
		if (!loginSession.getAuth().getAuthUrl().contains(requestUri)) {
			if (BrowserUtils.isAjaxReqeuest(request)) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print("您没有访问此资源的权限！请联系超管赋予您<br/>[" + requestUri + "]<br/>的资源访问权限！");
				out.flush();
				out.close();
				return false;
			} else {
				request.setAttribute("msg", requestUri);
				request.getRequestDispatcher("/common/errors/noSecurity.jsp").forward(request, response);
				return false;
			}
		}

		return super.preHandle(request, response, handler);
	}
}
