package com.ieasy.module.system.web.form;

import java.io.Serializable;

/**
 * 用户Session信息模型
 * @author home-work
 *
 */
public class LoginSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private LoginUser user ;					//用户登陆的基本信息
	
	private AuthForm auth ;						//用户的权限信息
	
	
	public AuthForm getAuth() {
		return auth;
	}

	public void setAuth(AuthForm auth) {
		this.auth = auth;
	}

	public LoginUser getUser() {
		return user;
	}

	public void setUser(LoginUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "LoginSession [user=" + user + ", auth=" + auth + "]";
	}
}
