package com.ieasy.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.ILogService;
import com.ieasy.module.system.web.form.LogForm;
import com.ieasy.module.system.web.form.LoginLogForm;


@Controller
@RequestMapping("/admin/system/log")
public class LogAction extends BaseController {
	
	@Inject
	private ILogService logService ;
	
	@RequestMapping("/log_main_UI.do")
	public String log_main_UI() {
		return Const.SYSTEM + "log_main_UI" ;
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody Msg delete(LogForm form) {
		return this.logService.delete(form);
	}
	

	@RequestMapping("/datagrid.do")
	public @ResponseBody DataGrid datagrid(LogForm form) {
		return this.logService.datagrid(form) ;
	}
	
	
	
	/******************************************************************/
	@RequestMapping("/login_log_main_UILL.do")
	public String login_log_main_pageLL() {
		return Const.SYSTEM + "login_log_main_UI" ;
	}
	
	@RequestMapping("/deleteLL.do")
	public @ResponseBody Msg deleteLL(LoginLogForm form) {
		return this.logService.deleteLL(form);
	}
	

	@RequestMapping("/datagridLL.do")
	public @ResponseBody DataGrid datagridLL(LoginLogForm form) {
		return this.logService.datagridLL(form) ;
	}
	
	@RequestMapping("/doNotNeedAuth_loginTimeChartLL.do")
	@ResponseBody
	public List<Long> doNotNeedAuth_loginTimeChartLL(){
		return this.logService.loginTimeChartLL() ;
	}
}

