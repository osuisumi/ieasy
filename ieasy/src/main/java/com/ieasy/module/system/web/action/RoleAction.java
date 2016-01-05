package com.ieasy.module.system.web.action;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.IRoleService;
import com.ieasy.module.system.web.form.RoleForm;

@Controller
@RequestMapping("/admin/system/role")
public class RoleAction extends BaseController {

	@Inject
	private IRoleService roleService ;
	
	@RequestMapping("/role_main_UI.do")
	public String role_main_UI() {
		return Const.SYSTEM + "role_main_UI" ;
	}
	
	@RequestMapping("/role_form_UI.do")
	public String role_form_UI(RoleForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "role_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(RoleForm form) throws Exception {
		return this.roleService.add(form) ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(RoleForm form) throws Exception {
		return this.roleService.delete(form) ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(RoleForm form) throws Exception {
		return this.roleService.update(form) ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public RoleForm get(RoleForm form) throws Exception {
		return this.roleService.get(form) ;
	}
	
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(RoleForm form) throws Exception {
		return this.roleService.datagrid(form) ;
	}
	
	@RequestMapping("/doNotNeedAuth_datagrid.do")
	@ResponseBody
	public DataGrid doNotNeedAuth_datagrid(RoleForm form) throws Exception {
		return this.roleService.datagrid(form) ;
	}
	
	@RequestMapping("/combo_datagrid.do")
	@ResponseBody
	public DataGrid combo_datagrid(RoleForm form) throws Exception {
		return this.roleService.datagrid(form) ;
	}
	
}
