package com.ieasy.module.system.web.action;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.IACLService;
import com.ieasy.module.system.service.IMenuService;
import com.ieasy.module.system.web.form.ACLForm;
import com.ieasy.module.system.web.form.MenuForm;

@Controller
@RequestMapping("/admin/system/acl")
public class ACLAction extends BaseController {
	
	@Inject
	private IACLService aclService ;
	
	@Inject
	private IMenuService menuService ;
	
	@RequestMapping("/acl_user_UI.do")
	public String acl_user_UI(ACLForm form, Model model) {
		return Const.SYSTEM + "acl_user_UI" ;
	}
	
	@RequestMapping("/acl_role_UI.do")
	public String acl_role_UI(ACLForm form, Model model) {
		return Const.SYSTEM + "acl_role_UI" ;
	}
	
	@RequestMapping("/acl_dept_UI.do")
	public String acl_dept_UI(ACLForm form, Model model) {
		return Const.SYSTEM + "acl_dept_UI" ;
	}
	
	@RequestMapping("/acl_position_UI.do")
	public String acl_position_UI(ACLForm form, Model model) {
		return Const.SYSTEM + "acl_position_UI" ;
	}
	
	
	@RequestMapping("/grantPermits.do")
	public @ResponseBody Msg grantPermits(ACLForm form) {
		return this.aclService.grantPermits(form) ;
	}

	@RequestMapping("/getPermits.do")
	public @ResponseBody ACLForm getPermits(ACLForm form) throws Exception {
		return this.aclService.getPermits(form) ;
	}
	
	
	@RequestMapping("/allMenuTree.do")
	public @ResponseBody List<MenuForm> allMenuTree() throws Exception {
		return this.menuService.getAllMenuTree(null) ;
	}
	
	
	
	
}
