package com.ieasy.module.system.service;

import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.web.form.PersonForm;
import com.ieasy.module.system.web.form.UserForm;

public interface IMyInfoService {

	public PersonForm getMyInfo(PersonForm form) ;
	
	public Msg updateMyInfo(PersonForm form) ;
	
	public Msg modifyMyPwf(UserForm form) ;
	
}
