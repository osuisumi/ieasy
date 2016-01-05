package com.ieasy.module.oa.project.web.action;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.oa.project.service.IProjectJdlService;
import com.ieasy.module.oa.project.web.form.ProjectJdlForm;
import com.ieasy.module.system.service.IPersonService;
import com.ieasy.module.system.web.form.PersonForm;

@Controller 
@RequestMapping("/admin/oa/jdl")
public class ProjectJdlAction extends BaseController {

	@Inject
	private IProjectJdlService jdlService ;
	
	@Inject
	private IPersonService personService ;
	
	/**
	 * 稼动率中心主页
	 * @return
	 */
	@RequestMapping("/jdl_center_main_UI.do")
	public String jdl_center_main_UI() {
		return Const.PROJECT + "jdl_center_main_UI" ;
	}
	
	/**
	 * 稼动率中心查询
	 * @param form
	 * @return
	 */
	@RequestMapping("/jdl_center.do")
	public @ResponseBody DataGrid jdl_center(ProjectJdlForm form) {
		return this.jdlService.dagagrid(form) ;
	}
	
	/**以下为我的稼动率*************************************************************************/
	
	/**
	 * 我的稼动率主页
	 * @param form
	 * @return
	 */
	@RequestMapping("/my_jdl_UI.do")
	public String my_jdl_UI(ProjectJdlForm form) {
		return Const.PROJECT + "my_jdl_UI" ;
	}
	@RequestMapping("/by_my_jdl.do")
	public @ResponseBody DataGrid by_my_jdl(ProjectJdlForm form) {
		return this.jdlService.dagagrid(form) ;
	}
	
	
	/**汇总及图表*************************************************************************/
	/**
	 * 多部门统计汇总页面
	 * @param form
	 * @return
	 */
	@RequestMapping("/jdl_dept_report_UI.do")
	public String jdl_dept_report_UI() {
		return Const.PROJECT + "report/jdl_dept_report_UI" ;
	}
	
	/**
	 * 多部门统计汇总
	 * @return
	 */
	@RequestMapping("/get_jdl_dept_report.do")
	public @ResponseBody Map<String, Object> get_jdl_dept_report(ProjectJdlForm form) {
		return this.jdlService.get_jdl_dept_report(form) ;
	}
	
	/**
	 * 多部门汇总统计，筛选不计算稼动率的人员页面
	 * @param dept_id
	 * @param model
	 * @return
	 */
	@RequestMapping("/by_jdl_person_dialog.do")
	public String person_by_dept_datagrid_UI(String dept_id, Model model) {
		model.addAttribute("dept_id", dept_id) ;
		return Const.PROJECT + "report/by_jdl_person_dialog" ;
	}
	
	/**
	 * 不计算稼动率人员筛选,根据部门ID查询人员（多部门稼动率汇总统计页面用到）
	 * @param form
	 * @return
	 */
	@RequestMapping("/get_person_by_dept_datagrid.do")
	public @ResponseBody DataGrid get_person_by_dept_datagrid(PersonForm form) {
		return this.personService.datagrid(form) ;
	}
	
	@RequestMapping("/by_product_chart_UI")
	public String by_product_chart(ProjectJdlForm form, Model model) {
		model.addAttribute("product_dept", form) ;
		return Const.PROJECT + "report/by_product_chart_UI" ;
	}
	
	@RequestMapping("/by_dept_chart_UI")
	public String by_dept_chart_UI(ProjectJdlForm form, Model model) {
		return Const.PROJECT + "report/by_dept_chart_UI" ;
	}
	
}
