package com.ieasy.module.system.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.service.IGlobalService;
import com.ieasy.module.system.web.form.GlobalForm;

@Controller
@RequestMapping("/admin/system/global")
public class GlobalAction extends BaseController {

	@Autowired
	private IGlobalService globalService ;
	
	@RequestMapping("/global_main_UI.do")
	public String global_main_UI(){
		return  Const.SYSTEM + "global_main_UI" ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public GlobalForm get(String id){
		return this.globalService.get(id) ;
	}
	
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg edit(GlobalForm form){
		return this.globalService.update(form) ;
	}
	
	
	@RequestMapping("/getWebAppCtxGlobal.do")
	@ResponseBody
	public GlobalForm getWebAppCtxGlobal(){
		return WebContextUtil.getGlobal() ;
	}

}
