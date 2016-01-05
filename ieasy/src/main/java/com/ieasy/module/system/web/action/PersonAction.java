package com.ieasy.module.system.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

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
import com.ieasy.module.common.dto.EmpExcelDto;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.system.service.IPersonService;
import com.ieasy.module.system.web.form.PersonForm;

@Controller
@RequestMapping("/admin/system/person")
public class PersonAction extends BaseController {

	@Inject
	private IPersonService personService ;
	
	@RequestMapping("/person_main_UI.do")
	public String person_main_UI() {
		return Const.SYSTEM + "person_main_UI" ;
	}
	
	@RequestMapping("/add.do")
	public @ResponseBody Msg add(PersonForm form) throws Exception {
		return this.personService.add(form) ;
	}
	
	@RequestMapping("/person_form_UI.do")
	public String person_form_UI(PersonForm form, Model model) throws Exception {
		if(null != form.getId() && !"".equals(form.getId())) {
			model.addAttribute("id", form.getId()) ;
		}
		return Const.SYSTEM + "person_form_UI" ;
	}
	
	@RequestMapping("/delete.do")
	public @ResponseBody Msg delete(PersonForm form) throws Exception {
		return this.personService.delete(form) ;
	}
	
	@RequestMapping("/update.do")
	public @ResponseBody Msg update(PersonForm form) throws Exception {
		return this.personService.update(form) ;
	}
	
	@RequestMapping("/get.do")
	public @ResponseBody PersonForm get(PersonForm form) throws Exception {
		return this.personService.get(form) ;
	}
	
	@RequestMapping("/datagrid.do")
	public @ResponseBody DataGrid datagrid(PersonForm form) {
		DataGrid datagrid= null;
		try {
			datagrid = this.personService.datagrid(form);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return datagrid ;
	}
	
	@RequestMapping("/combo_datagrid.do")
	public @ResponseBody DataGrid combo_datagrid(PersonForm form) {
		DataGrid datagrid= null;
		try {
			datagrid = this.personService.datagrid(form);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return datagrid ;
	}
	
	
	/**
	 * 修改人员的工作状态，是否待机或者在项目中
	 * @param form
	 * @param mode
	 * @return
	 */
	@RequestMapping("/doNotNeedAuth_modifyWorkStatus.do")
	@ResponseBody public Msg doNotNeedAuth_modifyWorkStatus(PersonForm form){
		return this.personService.modifyWorkStatus(form) ;
	}
	
	/**
	 * 批量离职页面
	 * @param form
	 * @param mode
	 * @return
	 */
	@RequestMapping("/person_batch_dimission.do")
	public String person_batch_dimission(PersonForm form, Model mode){
		return Const.SYSTEM + "person_batch_dimission" ;
	}
	@RequestMapping("/doNotNeedAuth_batch_dimission.do")
	public @ResponseBody Msg doNotNeedAuth_batch_dimission(PersonForm form){
		return this.personService.batchDimission(form) ;
	}
	
	@RequestMapping("/batchOrgLogout.do")
	public @ResponseBody Msg batchOrgLogout(PersonForm form){
		return this.personService.batchOrgLogout(form) ;
	}

	
	@RequestMapping("/person_batch_org.do")
	public String person_batch_org_UI(PersonForm form, Model mode){
		return Const.SYSTEM + "person_batch_org" ;
	}
	
	@RequestMapping("/doNotNeedAuth_batchChangeOrg.do")
	synchronized public @ResponseBody Msg batchChangeOrg(PersonForm form){
		return this.personService.batchChangeDept(form) ;
	}

	@RequestMapping("/person_batch_position.do")
	public String person_batch_position_UI(PersonForm form, Model mode){
		return Const.SYSTEM + "person_batch_position" ;
	}
	
	@RequestMapping("/doNotNeedAuth_batchPosition.do")
	synchronized public @ResponseBody Msg batchPosition(PersonForm form){
		return this.personService.batchSetPos(form) ;
	}
	
	@RequestMapping("/person_import_excel_data.do")
	public String person_import_excel_data_UI(){
		return Const.SYSTEM + "person_import_excel_data" ;
	}
	
	/*@RequestMapping("/doNotNeedAuth_parsePersonExcelData.do")
	public @ResponseBody Msg parsePersonExcelData(String datafile){
		return this.personService.parseDataInsert(datafile) ;
	}*/
	
	@RequestMapping("/exportPersonExcelData.do")
	public void exportPersonExcelData(HttpServletResponse resp, PersonForm form){
		try {
			resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ;
			resp.setHeader("Content-disposition", "attachment;filename="+ChangeCharset.toISO_8859_1("员工信息")+".xlsx");
			
			List<EmpExcelDto> objs = this.personService.exportEmpInfo(form) ;
			
			Map<String, String> datas = new HashMap<String, String>() ;
			datas.put("title", "员工信息") ;
			datas.put("total", String.valueOf(objs.size())) ;
			datas.put("date", DateUtils.formatYYYYMMDD(new Date())) ;
			ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas, "resources/excel/template/emp.xlsx", resp.getOutputStream(), objs, EmpExcelDto.class, true) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/person_touxiang_UI.do")
	public String person_touxiang_UI() {
		return Const.SYSTEM + "person_touxiang_UI" ;
	}
	
}
