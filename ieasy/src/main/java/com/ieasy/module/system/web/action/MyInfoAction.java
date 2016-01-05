package com.ieasy.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.IMyInfoService;
import com.ieasy.module.system.web.form.PersonForm;
import com.ieasy.module.system.web.form.UserForm;


@Controller
@RequestMapping("/admin/system/myinfo")
public class MyInfoAction extends BaseController {
	
	@Inject
	private IMyInfoService myInfoService ;
	
	@RequestMapping("/my_info_form_UI.do")
	public String my_info_form_UI() {
		return Const.SYSTEM + "my_info_form_UI" ;
	}
	
	@RequestMapping("/get_my_info.do")
	public @ResponseBody PersonForm get_my_info(PersonForm form) throws Exception {
		return this.myInfoService.getMyInfo(form) ;
	}
	
	@RequestMapping("/doNotNeedAuth_updateMyInfo.do")
	public @ResponseBody Msg doNotNeedAuth_updateMyInfo(PersonForm form) throws Exception {
		return this.myInfoService.updateMyInfo(form) ;
	}
	
	@RequestMapping("/my_pwd_form_UI.do")
	public String my_user_form_UI() {
		return Const.SYSTEM + "my_pwd_form_UI" ;
	}
	
	@RequestMapping("/doNotNeedAuth_modifyPwd.do")
	public @ResponseBody Msg doNotNeedAuth_modifyPwd(UserForm form) throws Exception {
		return this.myInfoService.modifyMyPwf(form) ;
	}

}

