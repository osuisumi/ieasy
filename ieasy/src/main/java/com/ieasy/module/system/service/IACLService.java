package com.ieasy.module.system.service;

import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.web.form.ACLForm;

public interface IACLService {

	public Msg grantPermits(ACLForm form);
	
	public ACLForm getPermits(ACLForm form) ;

}
