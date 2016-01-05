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
import com.ieasy.module.system.service.IPositionService;
import com.ieasy.module.system.web.form.PositionForm;

@Controller
@RequestMapping("/admin/system/position")
public class PositionAction extends BaseController {

	@Inject
	private IPositionService posService ;
	
	@RequestMapping("/position_main_UI.do")
	public String position_main_UI() {
		return Const.SYSTEM + "position_main_UI" ;
	}
	
	@RequestMapping("/position_form_UI.do")
	public String position_form_UI(PositionForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "position_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(PositionForm form) throws Exception {
		Msg msg = this.posService.add(form) ;
		this.posService.tree(null) ;
		return msg ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(PositionForm form) throws Exception {
		Msg msg = this.posService.delete(form) ;
		this.posService.tree(null) ;
		return msg ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(PositionForm form) throws Exception {
		Msg msg = this.posService.update(form) ;
		this.posService.tree(null) ;
		return msg ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public PositionForm get(PositionForm form) throws Exception {
		return this.posService.get(form) ;
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<PositionForm> tree(PositionForm form) throws Exception {
		return this.posService.tree(form.getPid()) ;
	}
	
	@RequestMapping("/combo_tree.do")
	@ResponseBody
	public List<PositionForm> combo_tree(PositionForm form) throws Exception {
		return this.posService.tree(form.getPid()) ;
	}

	@RequestMapping("/doNotNeedAuth_sort.do")
	@ResponseBody
	public void doNotNeedAuth_sort(int oldSort, int newSort, String id, String pid) {
		this.posService.updateResetSort(oldSort, newSort, id, pid) ;
		this.posService.tree(null) ;
	}
}
