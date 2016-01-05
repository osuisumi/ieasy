package com.ieasy.module.common.service;

import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.web.form.LoginUser;

public class BaseService {

	protected LoginUser currentUser  ;

	public LoginUser getCurrentUser() {
		if(null != WebContextUtil.getCurrentUser()) {
			return WebContextUtil.getCurrentUser().getUser() ;
		} else {
			return null ;
		}
	}

	public void setCurrentUser(LoginUser currentUser) {
		this.currentUser = currentUser;
	}

	
}
