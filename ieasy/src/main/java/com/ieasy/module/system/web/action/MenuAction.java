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
import com.ieasy.module.system.service.IMenuService;
import com.ieasy.module.system.web.form.MenuForm;

@Controller
@RequestMapping("/admin/system/menu")
public class MenuAction extends BaseController {

	@Inject
	private IMenuService menuService ;
	
	@RequestMapping("/menu_main_UI.do")
	public String menu_main_UI() {
		return Const.SYSTEM + "menu_main_UI" ;
	}
	
	@RequestMapping("/menu_form_UI.do")
	public String menu_form_UI(MenuForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "menu_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(MenuForm form) throws Exception {
		Msg msg = this.menuService.add(form) ;
		this.menuService.exportTree() ;
		return msg ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(MenuForm form) throws Exception {
		Msg msg = this.menuService.delete(form) ;
		this.menuService.exportTree() ;
		return msg ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(MenuForm form) throws Exception {
		Msg msg = this.menuService.update(form) ;
		this.menuService.exportTree() ;
		return msg ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public MenuForm get(MenuForm form) throws Exception {
		return this.menuService.get(form) ;
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<MenuForm> tree(MenuForm form) throws Exception {
		return this.menuService.tree(form.getPid()) ;
	}
	
	@RequestMapping("/doNotNeedAuth_tree.do")
	@ResponseBody
	public List<MenuForm> doNotNeedAuth_tree(MenuForm form) throws Exception {
		return this.menuService.tree(form.getPid()) ;
	}
	
	@RequestMapping("/combo_tree.do")
	@ResponseBody
	public List<MenuForm> combo_tree(MenuForm form) throws Exception {
		return this.menuService.combo(form) ;
	}
	
	@RequestMapping("/isShow_hide.do")
	@ResponseBody
	public Msg isShow_hide(MenuForm form) {
		Msg msg = this.menuService.isShow(form) ;
		this.menuService.exportTree() ;
		return msg ;
	}
	
	@RequestMapping("/doNotNeedAuth_sort.do")
	@ResponseBody
	public void doNotNeedAuth_sort(int oldSort, int newSort, String id, String pid) {
		this.menuService.exportTree() ;
		this.menuService.updateResetSort(oldSort, newSort, id, pid) ;
	}
}
