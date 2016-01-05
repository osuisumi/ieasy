package com.ieasy.module.oa.project.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.ChangeCharset;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.module.common.dto.ProjectDevWorkTimeDto;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.oa.project.service.IProjectCenterService;
import com.ieasy.module.oa.project.service.IProjectDevWorkTimeService;
import com.ieasy.module.oa.project.web.form.ProjectDevWorkTimeForm;

/**
 * 项目开发人员的作业时间和加班信息
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/admin/oa/project_dev_worktime")
public class ProjectDevWorkTimeAction extends BaseController {

	@Inject
	private IProjectDevWorkTimeService projectDevWorkTimeService ;
	
	@Inject
	private IProjectCenterService projectService ;
	
	/**
	 * 查询项目开发人员的作业时间信息
	 * @return
	 */
	@RequestMapping("/by_project_dev_datagrid.do")
	public @ResponseBody DataGrid by_project_dev_datagrid(ProjectDevWorkTimeForm form) {
		return this.projectDevWorkTimeService.datagrid(form)  ;
	}
	
	/**
	 * 修改项目开发人员的作业时间和加班信息
	 * @return
	 */
	@RequestMapping("/update.do")
	public @ResponseBody Msg update(ProjectDevWorkTimeForm form) {
		return this.projectDevWorkTimeService.update(form) ;
	}
	
	/**
	 * 开发人员退出项目
	 * @param form
	 * @return
	 */
	@RequestMapping("/by_exit_project.do")
	public @ResponseBody Msg exit_project(ProjectDevWorkTimeForm form) {
		Msg msg = this.projectDevWorkTimeService.exitProject(form) ;
		
		//删除项目定时器
		this.projectService.deleteProjectTime(form.getProj_id()) ;
		//删除开的人员定时器
		this.projectService.deleteProjectDevTime(form.getProj_id()) ;
		//重新设置定时器
		this.projectService.setProjectTimer(form.getProj_id()) ;
		
		//发送邮件，开发人员退出
		if(null != form.getId() && !"".equals(form.getId().trim())) {
			List<String> changeIds = new ArrayList<String>() ;
			changeIds.add(form.getId()) ;
			form.setSendStatus("exit") ;
			this.projectService.sendMailByProjectDevStatus(form.getProj_id(), form.getSendStatus(), changeIds) ;
		}
		
		return msg ;
	}
	
	/**
	 * 开发人员新增或日期变更发送邮件
	 * @param form
	 * @return
	 */
	@RequestMapping("/by_DevSendMail.do")
	public @ResponseBody Msg by_DevSendMail(ProjectDevWorkTimeForm form) {
		//发送邮件，开发人员退出
		if(null != form.getChangeIds() && !"".equals(form.getChangeIds().trim())) {
			List<String> changeIds = new ArrayList<String>() ;
			String[] split = StringUtils.split(form.getChangeIds(), ",") ;
			for (String id : split) {
				changeIds.add(id) ;
			}
			this.projectService.sendMailByProjectDevStatus(form.getProj_id(), form.getSendStatus(), changeIds) ;
			return new Msg(true, "邮件发送成功") ;
		}
		
		return new Msg(false, "邮件发送失败，已退出人员不能发送邮件，或邮件地址为空！") ;
	}
	
	/**
	 * 修改已退出人员的开始和结束时间
	 * @param form
	 * @return
	 */
	@RequestMapping("/by_modify_sed.do")
	public @ResponseBody Msg by_modify_sed(ProjectDevWorkTimeForm form) {
		Msg msg = this.projectDevWorkTimeService.modifySEDate(form) ;
		return msg ;
	}
	
	/**
	 * 删除开发人员工作起止时间数据
	 * @param form
	 * @return
	 */
	@RequestMapping("/by_delete_dev_person.do")
	public @ResponseBody Msg by_delete_dev_person(ProjectDevWorkTimeForm form) {
		Msg msg = this.projectDevWorkTimeService.deleteDevPerson(form) ;
		
		//删除项目定时器
		this.projectService.deleteProjectTime(form.getProj_id()) ;
		//删除开的人员定时器
		this.projectService.deleteProjectDevTime(form.getProj_id()) ;
		//重新设置定时器
		this.projectService.setProjectTimer(form.getProj_id()) ;
		
		return msg ;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/by_export_dev_list.do")
	public void export(HttpServletResponse resp, ProjectDevWorkTimeForm form){
		this.logService.add("导出项目开发人员列表","项目名称：" + form.getProj_name()) ;
		try {
			List<ProjectDevWorkTimeDto> objs = new ArrayList<ProjectDevWorkTimeDto>() ;
			
			form.setPage(0) ;
			form.setRows(10000000) ;
			DataGrid d = this.projectDevWorkTimeService.datagrid(form) ;
			List<ProjectDevWorkTimeForm> rows = (List<ProjectDevWorkTimeForm>) d.getRows() ;
			for (ProjectDevWorkTimeForm f : rows) {
				ProjectDevWorkTimeDto dto = new ProjectDevWorkTimeDto() ;
				BeanUtils.copyNotNullProperties(f, dto, new String[]{"work_startDate","work_endDate","work_status", "dbmDate", "lbmDate"}) ;
				dto.setWork_startDate(DateUtils.formatYYYYMMDD(f.getWork_startDate())) ;
				dto.setWork_endDate(DateUtils.formatYYYYMMDD(f.getWork_endDate())) ;
				dto.setDbmDate((null == f.getDbmDate()? "-":DateUtils.formatYYYYMMDD(f.getDbmDate()))) ;
				dto.setLbmDate((null == f.getLbmDate()? "-":DateUtils.formatYYYYMMDD(f.getLbmDate()))) ;
				if(f.getWork_status() == 1) {
					dto.setWork_status("进行中") ;
				} else {
					dto.setWork_status("已退出") ;
				}
				
				objs.add(dto) ;
			}
			List<Map<String, Object>> footer = (List<Map<String, Object>>) d.getFooter() ;
			Map<String, Object> map = footer.get(0) ;
			
			
			/**下载*******************************************************/
			resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ;
			resp.setHeader("Content-disposition", "attachment;filename="+ChangeCharset.toISO_8859_1("项目开发人员列表")+".xlsx");
			
			Map<String, String> datas = new HashMap<String, String>() ;
			datas.put("title", rows.get(0).getProj_name() + "-开发人员") ;
			datas.put("totalNum", rows.size()+"") ;
			datas.put("totalCyc", map.get("devCyc").toString()) ;				//天数
			datas.put("total_ext_Cyc", map.get("ext_devCyc").toString()) ;		//已消耗天数
			datas.put("total_ry", map.get("ry").toString()) ;					//人月
			datas.put("total_ext_ry", map.get("ext_ry").toString()) ;			//已消耗人月
			datas.put("n", map.get("normalHour").toString()) ;
			datas.put("w", map.get("weekendHour").toString()) ;
			datas.put("h", map.get("holidaysHour").toString()) ;
			datas.put("total_hours", map.get("totalHour").toString()) ;			//加班小时
			datas.put("total_jbl", map.get("jbl").toString()) ;					//加班率
			ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas, "resources/excel/template/project_dev_list.xlsx", resp.getOutputStream(), objs, ProjectDevWorkTimeDto.class, true) ;
			
		} catch (Exception e) {
			e.printStackTrace() ;
		}
	}
}
