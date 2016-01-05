package com.ieasy.module.system.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.INoticeService;
import com.ieasy.module.system.web.form.NoticeForm;

@Controller
@RequestMapping("/admin/system/notice")
public class NoticeAction extends BaseController {

	@Autowired
	private INoticeService noticeService ;
	
	@RequestMapping("/notice_main_UI.do")
	public String notice_main_UI(){
		return  Const.SYSTEM + "notice_main_UI" ;
	}
	
	@RequestMapping("/info_main_UI.do")
	public String info_main_UI(){
		return  Const.SYSTEM + "info_main_UI" ;
	}
	
	@RequestMapping("/info_main_type_UI.do")
	public String info_main_type_UI(String type, Model mode){
		mode.addAttribute("type", type) ;
		return  Const.SYSTEM + "info_main_type_UI" ;
	}
	
	@RequestMapping("/notice_form_UI.do")
	public String notice_form_UI(NoticeForm form, Model mode){
		if(null != form.getId() && !"".equals(form.getId())) {
			mode.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "notice_form_UI" ;
	}
	
	@RequestMapping("/info_form_UI.do")
	public String info_form_UI(NoticeForm form, Model mode){
		if(null != form.getId() && !"".equals(form.getId())) {
			mode.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "info_form_UI" ;
	}
	
	@RequestMapping("/info_open_UI.do")
	public String info_open_UI(NoticeForm form, Model mode){
		if(null != form.getId() && !"".equals(form.getId())) {
			mode.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "info_open_UI" ;
	}
	
	@RequestMapping("/get.do")
	@ResponseBody
	public NoticeForm get(NoticeForm form){
		return this.noticeService.get(form) ;
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Msg add(NoticeForm form){
		return this.noticeService.add(form) ;
	}
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Msg edit(NoticeForm form){
		return this.noticeService.update(form) ;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Msg delete(NoticeForm form){
		return this.noticeService.delete(form) ;
	}
	
	@RequestMapping("/datagrid.do")
	@ResponseBody
	public DataGrid datagrid(NoticeForm form){
		return this.noticeService.datagrid(form) ;
	}
	
	@RequestMapping("/datagrid_info.do")
	@ResponseBody
	public DataGrid datagrid_info(NoticeForm form){
		return this.noticeService.datagrid(form) ;
	}
	
	@RequestMapping("/view_datagrid.do")
	@ResponseBody
	public DataGrid view_datagrid(NoticeForm form){
		return this.noticeService.datagrid(form) ;
	}
	
	/**
	 * 修改人员的工作状态，是否待机或者在项目中
	 * @param form
	 * @param mode
	 * @return
	 */
	@RequestMapping("/doNotNeedAuth_approve.do")
	@ResponseBody public Msg doNotNeedAuth_approve(NoticeForm form){
		return this.noticeService.modifyAp(form) ;
	}
}
