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
import com.ieasy.module.system.service.IOrgService;
import com.ieasy.module.system.web.form.OrgForm;

@Controller
@RequestMapping("/admin/system/org")
public class OrgAction extends BaseController {

	@Inject
	private IOrgService orgService ;
	
	@RequestMapping("/org_main_UI.do")
	public String org_main_UI() {
		return Const.SYSTEM + "org_main_UI" ;
	}
	
	@RequestMapping("/org_form_UI.do")
	public String org_form_UI(OrgForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "org_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(OrgForm form) throws Exception {
		Msg msg = this.orgService.add(form) ;
		this.orgService.tree(null) ;
		return msg ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(OrgForm form) throws Exception {
		Msg msg = this.orgService.delete(form) ;
		this.orgService.tree(null) ;
		return msg ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(OrgForm form) throws Exception {
		Msg msg = this.orgService.update(form) ;
		this.orgService.tree(null) ;
		return msg ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public OrgForm get(OrgForm form) throws Exception {
		return this.orgService.get(form) ;
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<OrgForm> tree(OrgForm form) throws Exception {
		return this.orgService.tree(form.getPid()) ;
	}
	
	@RequestMapping("/combo_tree.do")
	@ResponseBody
	public List<OrgForm> combo_tree(OrgForm form) throws Exception {
		return this.orgService.tree(form.getPid()) ;
	}
	
	@RequestMapping("/combo_sync_tree.do")
	@ResponseBody
	public List<OrgForm> combo_sync_tree(OrgForm form) throws Exception {
		return this.orgService.syncTree(form.getPid()) ;
	}
	
	@RequestMapping("/doNotNeedAuth_sort.do")
	@ResponseBody
	public void doNotNeedAuth_sort(int oldSort, int newSort, String id, String pid) {
		this.orgService.updateResetSort(oldSort, newSort, id, pid) ;
		this.orgService.tree(null) ;
	}
}
