package com.ieasy.module.system.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.IpUtil;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.device.BrowserUtils;
import com.ieasy.basic.util.device.UserAgent;
import com.ieasy.basic.util.device.UserAgentUtil;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.common.web.servlet.ValidCodeServlet;
import com.ieasy.module.system.service.ILogService;
import com.ieasy.module.system.service.IUserService;
import com.ieasy.module.system.web.form.AuthForm;
import com.ieasy.module.system.web.form.LoginLogForm;
import com.ieasy.module.system.web.form.LoginSession;
import com.ieasy.module.system.web.form.LoginUser;

@Controller
@RequestMapping("/system/login")
public class LoginAction extends BaseController {
	
	@Inject
	private IUserService userService ;

	@Inject
	private ILogService logService ;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Msg login(LoginUser form, HttpServletRequest request, HttpSession session) throws Exception {
		Msg msg = new Msg() ;
		Map<String, Object> maps = new HashMap<String, Object>() ;
		
		if(null == form || form.getAccount() == null || "".equals(form.getAccount().trim())) {
			return new Msg(false, "账号不能为空！") ;
		}
		
		Integer count = (Integer)session.getAttribute("login_error") ;
		if(null == count) count = 0 ;
		if(count >= 3) {
			if(null == form.getValidCode() || "".equals(form.getValidCode().trim())) {
				return new Msg(false, "验证码不能为空！") ;
			}
			String validCode = (String) session.getAttribute(ValidCodeServlet.VALIDATE_CODE) ;
			if(null == validCode) return new Msg(false, "验证码失效，请点击验证码获取新的验证码！") ;
			if(!form.getValidCode().equalsIgnoreCase(validCode)) {
				return new Msg(false, "验证码不正确！") ;
			}
		}
		
		
		LoginUser user = this.userService.loginCheck(form) ;
		if(null != user) {
			if(user.getStatus() == 1)
				return new Msg(false, "你的账号已被锁定，无法登陆系统，解锁请联系管理员！") ;
			
			String ipAddr = IpUtil.getIpAddr(request) ;
			
			AuthForm auth = this.userService.getAuth(user.getUser_id()) ;
			LoginSession loginSession = new LoginSession() ;
			user.setIp(ipAddr) ;
			loginSession.setUser(user) ;
			loginSession.setAuth(auth) ;
			session.setAttribute(Const.USER_SESSION, loginSession) ;
			
			//保存用户的设备信息
			UserAgent userAgent = UserAgentUtil.getUserAgent(BrowserUtils.getAgent(request)) ;
			LoginLogForm lf = new LoginLogForm() ;
			lf.setLoginAccount(user.getAccount()) ;
			lf.setName(user.getEmp_name()) ;
			lf.setLoginTime(new Date()) ;
			lf.setIp(ipAddr) ;
			lf.setBrowserType(userAgent.getBrowserType()) ;
			lf.setBrowserVersion(userAgent.getBrowserVersion()) ;
			lf.setPlatformType(userAgent.getPlatformType()) ;
			lf.setDetail(BrowserUtils.getAgent(request)) ;
			this.logService.addLL(lf) ;
			
			session.removeAttribute(ValidCodeServlet.VALIDATE_CODE) ;
			session.removeAttribute("login_error") ;
			
			maps.put("tx", user.getEmp_tx()) ;
			msg.setObj(maps) ;
			msg.setStatus(true); 
			msg.setMsg("验证成功，正在访问...") ;
			return msg ;
		} else {
			count = count+1 ;
			session.setAttribute("login_error", count) ;
			
			if(count >= 3) maps.put("v", true) ;
			msg.setStatus(false); 
			msg.setMsg("登陆失败！请检查账号密码") ;
			msg.setObj(maps) ;
			return msg ;
		}
	}
	
	@RequestMapping("/getCurrentAuthMenu.do")
	public @ResponseBody Object getCurrentAuthMenu(HttpSession session) {
		LoginSession ls = (LoginSession)session.getAttribute(Const.USER_SESSION) ;
		if(null != ls) {
			AuthForm auth = ls.getAuth() ;
			//返回的是拼装的JSON字符串，需将字符串转换为JSON对象
			return JSON.parse("["+auth.getAuthTree()+"]") ;
		} else {
			return "" ; 
		}
	}
	
	@RequestMapping("/login_error.do")
	@ResponseBody
	public Msg login_error(HttpServletRequest request, HttpSession session) throws Exception {
		Integer count = (Integer)session.getAttribute("login_error") ;
		if(null == count) count = 0 ;
		if(count >= 3) {
			return new Msg(true) ;
		} else{
			return new Msg(false);
		}
	}
	
	@RequestMapping("/logout.do")
	@ResponseBody
	public Msg logout(HttpServletRequest request, HttpSession session) throws Exception {
		session.invalidate() ;
		return new Msg(true) ;
	}
	
}
