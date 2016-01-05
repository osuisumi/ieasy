package com.ieasy.module.system.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.ChangeCharset;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.module.common.dto.UserExcelDto;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.IUserService;
import com.ieasy.module.system.web.form.UserForm;

@Controller
@RequestMapping("/admin/system/user")
public class UserAction extends BaseController {

	private Logger logger = Logger.getLogger(UserAction.class) ;
	
	@Inject
	private IUserService userService ;
	
	@RequestMapping("/user_main_UI.do")
	public String user_main_UI() {
		return Const.SYSTEM + "user_main_UI" ;
	}
	
	@RequestMapping("/user_form_UI.do")
	public String user_form_UI(UserForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "user_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(UserForm form) throws Exception {
		this.logService.add("添加用户账号信息") ;
		return this.userService.add(form) ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(UserForm form) throws Exception {
		this.logService.add("删除用户账号记录") ;
		return this.userService.delete(form) ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(UserForm form) throws Exception {
		this.logService.add("修改用户账号信息", "账号：["+form.getAccount()+"]") ;
		return this.userService.update(form) ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public UserForm get(UserForm form) throws Exception {
		return this.userService.get(form) ;
	}
	
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(UserForm form) throws Exception {
		return this.userService.datagrid(form) ;
	}
	

	@RequestMapping("/import_user_data.do")
	public String import_user_data(){
		return Const.SYSTEM + "user_import_user_data" ;
	}
	
	@RequestMapping("/doNotNeedAuth_parseExcelData.do")
	public @ResponseBody Msg doNotNeedAuth_parseExcelData(String datafile){
		return this.userService.parseDataInsert(datafile) ;
	}
	
	@RequestMapping("/exportUserData.do")
	public void export(HttpServletResponse resp){
		try {
			resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ;
			resp.setHeader("Content-disposition", "attachment;filename="+ChangeCharset.toISO_8859_1("用户账号信息")+".xlsx");
			
			List<UserExcelDto> objs = this.userService.exportBasicUserInfo() ;
			Map<String, String> datas = new HashMap<String, String>() ;
			datas.put("title", "用户账号信息") ;
			datas.put("total", String.valueOf(objs.size())) ;
			datas.put("date", DateUtils.formatYYYYMMDD(new Date())) ;
			ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas, "resources/excel/template/user.xlsx", resp.getOutputStream(), objs, UserExcelDto.class, true) ;
		} catch (IOException e) {
			logger.error(e.getMessage()) ;
		}
	}
	
	@RequestMapping("/user_lock_account.do")
	public String user_lock_account(UserForm form, Model mode){
		return Const.SYSTEM + "user_lock_account" ;
	}
	
	@RequestMapping("/doNotNeedAuth_batchLockAccount.do")
	@ResponseBody
	public Msg batchLockAccount(UserForm form){
		return this.userService.batchLockAccount(form) ;
	}
	
	@RequestMapping("/batchClearPwd.do")
	@ResponseBody
	public Msg batchClearPwd(UserForm form){
		return this.userService.batchClearPwd(form) ;
	}
	
	@RequestMapping("/user_reset_pwd.do")
	public String user_reset_pwd_UI(UserForm form, Model mode){
		return Const.SYSTEM + "user_reset_pwd" ;
	}
	
	@RequestMapping("/doNotNeedAuth_batchResetPwd.do")
	@ResponseBody
	public Msg batchResetPwd(UserForm form){
		return this.userService.batchResetPwd(form) ;
	}
	

	@RequestMapping("/doNotNeedAuth_search_UI.do")
	public String doNotNeedAuth_search_UI(UserForm form){
		return Const.SYSTEM + "user_search_UI" ;
	}
	
	@RequestMapping("/user_role.do")
	public String user_role(UserForm form, Model mode){
		return Const.SYSTEM + "user_role" ;
	}
	
	@RequestMapping("/doNotNeedAuth_batchRole.do")
	@ResponseBody
	public Msg batchRole(UserForm form){
		return this.userService.batchUserRole(form) ;
	}
	@RequestMapping("/user_account.do")
	public String user_account(Model mode, String emp_id) {
		mode.addAttribute("emp_id", emp_id) ;
		return Const.SYSTEM + "user_account" ;
	}
	
	@RequestMapping("/doNotNeedAuth_userCreateAccount.do")
	@ResponseBody
	public Msg userCreateAccount(UserForm form) throws Exception {
		return this.userService.createAccount(form) ;
	}
	
}
