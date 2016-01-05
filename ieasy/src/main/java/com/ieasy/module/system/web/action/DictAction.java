package com.ieasy.module.system.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.service.IDictService;
import com.ieasy.module.system.web.form.DictForm;
import com.ieasy.module.system.web.form.DictJson;

@Controller
@RequestMapping("/admin/system/dict")
public class DictAction extends BaseController {

	@Inject
	private IDictService dictService ;
	
	@RequestMapping("/dict_main_UI.do")
	public String dict_main_UI() {
		return Const.SYSTEM + "dict_main_UI" ;
	}
	
	@RequestMapping("/dict_form_UI.do")
	public String dict_form_UI(DictForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "dict_form_UI" ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(DictForm form) throws Exception {
		Msg msg = this.dictService.add(form) ;
		this.dictService.tree(null, false) ;
		this.dictService.tree(null, true) ;
		resetDictContext() ;
		return msg ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(DictForm form) throws Exception {
		Msg msg = this.dictService.delete(form) ;
		this.dictService.tree(null, false) ;
		this.dictService.tree(null, true) ;
		resetDictContext() ;
		return msg ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg update(DictForm form) throws Exception {
		Msg msg = this.dictService.update(form) ;
		this.dictService.tree(null, false) ;
		this.dictService.tree(null, true) ;
		resetDictContext() ;
		return msg ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public DictForm get(DictForm form) throws Exception {
		return this.dictService.get(form) ;
	}
	
	@RequestMapping("/tree.do")
	@ResponseBody
	public List<DictForm> tree(DictForm form) throws Exception {
		return this.dictService.tree(form.getPid(), false) ;
	}
	
	@RequestMapping("/combo_tree.do")
	@ResponseBody
	public List<DictForm> combo_tree(DictForm form) throws Exception {
		return this.dictService.tree(form.getPid(), true) ;
	}
	
	@RequestMapping("/doNotNeedAuth_sort.do")
	@ResponseBody
	public void doNotNeedAuth_sort(int oldSort, int newSort, String id, String pid) {
		this.dictService.updateResetSort(oldSort, newSort, id, pid) ;
		this.dictService.tree(null, false) ;
		this.dictService.tree(null, true) ;
	}
	
	
	@RequestMapping("/doNotNeedSession_dictAttrMaps.do")
	@ResponseBody
	public Map<String, List<DictJson>> doNotNeedAuth_dictAttrMaps(String dictCode) {
		Map<String, List<DictJson>> attrs = new HashMap<String, List<DictJson>>();
		Map<String, List<DictJson>> dicts = WebContextUtil.getDicts() ;
		if(null != dictCode && !"".equals(dictCode.trim())) {
			String[] split = dictCode.split(",") ;
			for (String k : split) {
				attrs.put(k.toUpperCase(), dicts.get(k)) ;
			}
		}
		return attrs ;
	}
	private void resetDictContext() {
		WebContextUtil.setDicts(this.dictService.generateAttrMaps()) ;
	}
	
	
}
