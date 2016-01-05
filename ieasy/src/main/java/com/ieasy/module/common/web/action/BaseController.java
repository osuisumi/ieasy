package com.ieasy.module.common.web.action;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ieasy.basic.util.springmvc.DateEditor;
import com.ieasy.module.system.service.ILogService;

/**
 * @程序编写者：杨浩泉
 * @日期：2013-6-30
 * @类说明：基础控制器
 * 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {
	
	protected HttpServletRequest request ; 

	protected HttpServletResponse response ;

    protected HttpSession session ;
	
	@Inject
	protected ILogService logService ;
	
	
	/**
	 * 每次执行请求前都会先执行它再执行请求
	 * @param request
	 * @param response
	 */
	@ModelAttribute
	public void setServletApi(HttpServletRequest request, HttpServletResponse response) {
		this.request = request ;
		this.response = response ;
		this.session = request.getSession() ;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public ILogService getLogService() {
		return logService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new DateEditor());

		/**
		 * 防止XSS攻击
		 */
		//binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
	}

	/**
	 * 用户跳转JSP页面
	 * 此方法不考虑权限控制
	 * @param folder 路径
	 * @param jspName JSP名称(不加后缀)
	 * @return 指定JSP页面
	 */
	@RequestMapping("/{folder}/{jspName}")
	public String redirectJsp(@PathVariable String folder, @PathVariable String jspName) {
		return "/" + folder + "/" + jspName;
	}

}
