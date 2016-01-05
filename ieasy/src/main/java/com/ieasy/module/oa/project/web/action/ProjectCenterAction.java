package com.ieasy.module.oa.project.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.module.common.web.action.BaseController;
import com.ieasy.module.oa.project.service.IProjectCenterService;
import com.ieasy.module.oa.project.service.IProjectDevWorkTimeService;
import com.ieasy.module.oa.project.web.form.ProjectApproveForm;
import com.ieasy.module.oa.project.web.form.ProjectCenterForm;
import com.ieasy.module.oa.project.web.form.ProjectDevWorkTimeForm;
import com.ieasy.module.system.service.IPersonService;
import com.ieasy.module.system.web.form.PersonForm;

@Controller
@RequestMapping("/admin/oa/project")
public class ProjectCenterAction extends BaseController {
	
	@Inject
	private IProjectCenterService projectService ;
	
	@Inject
	private IPersonService personService ;
	
	@Inject
	private IProjectDevWorkTimeService projectDevWorkService ;
	
	/**
	 * 项目中心
	 * @return
	 */
	@RequestMapping("/project_center_main_UI.do")
	public String project_center_main_UI() {
		return Const.PROJECT + "project_center_main_UI" ;
	}
	
	/**
	 * 新建项目页面
	 * @return
	 */
	@RequestMapping("/project_add_page.do")
	public String project_add_page() {
		return Const.PROJECT + "project_add_page" ;
	}
	
	/**
	 * 打开新窗口显示项目相关详细信息
	 * @return
	 */
	@RequestMapping("/open_project_UI.do")
	public String project_detail_UI(String projectId, String status, Model mode) {
		mode.addAttribute("projectId", projectId) ;
		mode.addAttribute("status", status) ;
		return Const.PROJECT + "open_project_UI" ;
	}
	
	/**
	 * 创建项目
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/add.do")
	public @ResponseBody Msg add(ProjectCenterForm form) {
		this.logService.add("新建项目","项目名称：" + form.getProj_name()) ;
		
		Msg msg = this.projectService.createProject(form) ;
		
		//审批人只有自己
		if(form.getProj_approve_person_ids().split(",").length == 1) {
			//设置定时器
			this.projectService.setProjectTimer(form.getId()) ;
		}
		
		Map<String, Object> map = (Map<String, Object>) msg.getObj() ;
		ProjectCenterForm p = (ProjectCenterForm) map.get("project") ;
		
		if(map.get("approve_status").toString().equals("2")) {
			//发送邮件
			this.projectService.sendMailByProjectStatus(p.getId(), null) ;
		}
		
		//如果审批人为多个，则发送邮件通知审批人
		String api = form.getProj_approve_person_ids(); // 获取审批人ID
		if (null != api && !"".equals(api.trim())) {
			String[] ids = api.split(",") ;
			if(ids.length > 1) {
				this.projectService.sendMailByApproveAffair(ids, p.getId()) ;
			}
		}
			
		return msg ;
	}

	/**
	 * 获取项目各个状态的数量
	 * @return
	 */
	@RequestMapping("/getProjectStatusCount.do")
	public @ResponseBody List<Map<String, Object>> getProjectStatusCount(ProjectCenterForm form) {
		return this.projectService.getProjectStatusCount(form) ;
	}
	
	/**
	 * 以项目状态为基本查询条件，进行列表查询
	 * @return
	 */
	@RequestMapping("/findProjectByStatusList.do")
	public @ResponseBody DataGrid findByProjectStatusList(ProjectCenterForm form) {
		return this.projectService.findProjectListByStatus(form) ;
	}
	
	/**
	 * 获取项目详细信息
	 * @return
	 */
	@RequestMapping("/getProjectDetail.do")
	public @ResponseBody ProjectCenterForm getProjectDetail(ProjectCenterForm form) {
		ProjectCenterForm projectDetail = this.projectService.getProjectDetail(form) ;
		if(null != projectDetail) {
			return projectDetail;
		} else {
			//返回HTTP的状态
			this.getResponse().setStatus(205) ;
		}
		return projectDetail;
		
	}
	
	/**
	 * 项目详细信息浏览
	 * @return
	 */
	@RequestMapping("/project_detail_view.do")
	public String project_detail_view(Model mode) {
		return Const.PROJECT + "project_detail_view" ;
	}
	
	/**
	 * 项目信息修改页面
	 * @return
	 */
	@RequestMapping("/by_Project_edit_page.do")
	public String by_Project_edit_page(Model mode) {
		return Const.PROJECT + "project_detail_edit_page" ;
	}
	
	/**
	 * 修改项目信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/by_UpdateProject.do")
	public @ResponseBody Msg by_UpdateProject(ProjectCenterForm form) {
		Msg msg = this.projectService.updateProject(form) ;
		if(form.getProj_status() == 2) {
			//设置定时作业
			this.projectService.setProjectTimer(form.getId()) ;
		}
		
		if(msg.isStatus()) {
			if (form.getProj_status() == 2) {
				Map<String, Object> map = (Map<String, Object>) msg.getObj() ;
				ProjectCenterForm p = (ProjectCenterForm) map.get("oldProject") ;
				String oldEndDate = DateUtils.formatYYYYMMDD(p.getProj_end_time()) ;
				String newEndDate = DateUtils.formatYYYYMMDD(form.getProj_end_time()) ;
				//如果项目的结束日期不相等，则发送邮件
				if(!newEndDate.equals(oldEndDate)) {
					//发送邮件
					this.projectService.sendMailByProjectDate(form.getId()) ;
				}
			}
		}
		
		
		return msg ;
	}
	
	/**
	 * 销毁项目
	 * @return
	 */
	@RequestMapping("/by_Project_delete.do")
	public @ResponseBody Msg by_Project_delete(ProjectCenterForm form) {
		return this.projectService.depeteProject(form) ;
	}
	
	/**
	 * 提交审批项目
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/by_Project_Approve.do")
	public @ResponseBody Msg by_Project_Approve(String personId, String projectId) {
		Msg msg = this.projectService.approve(personId, projectId) ;
		
		Map<String, Object> map = (Map<String, Object>) msg.getObj() ;
		if("2".equals(map.get("status").toString())) {
			//发送邮件
			this.projectService.sendMailByProjectStatus(projectId, null) ;
		}
		
		return msg ;
	}
	
	/**
	 * 项目激活
	 * @return
	 */
	@RequestMapping("/by_Project_Activated.do")
	public @ResponseBody Msg by_Project_Activated(String projectId) {
		Msg msg = this.projectService.activated(projectId) ;
		
		//发送邮件
		this.projectService.sendMailByProjectStatus(projectId, null) ;
		
		return msg ;
	}
	
	/**
	 * 项目挂起
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/by_Project_Sleep.do")
	public @ResponseBody Msg by_Project_Sleep(String projectId) {
		Msg msg = this.projectService.sleeping(projectId) ;
		
		//发送邮件
		List<Map<String, String>> devIdsList = (List<Map<String, String>>) msg.getObj() ;
		this.projectService.sendMailByProjectStatus(projectId, (devIdsList.size()>0?devIdsList:null)) ;
		
		return msg ;
	}
	
	/**
	 * 项目状态转换，将已结束状态转换为进行中
	 * @return
	 */
	@RequestMapping("/by_Project_changeStatus.do")
	public @ResponseBody Msg by_Project_changeStatus(String projectId) {
		return this.projectService.changeProjectStatus(projectId, 2) ;
	}
	
	
	/**
	 * 项目结束
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/by_Project_Finish.do")
	public @ResponseBody Msg by_Project_Finish(String projectId) {
		Msg msg = this.projectService.finish(projectId) ;
		
		//发送邮件
		List<Map<String, String>> devIdsList = (List<Map<String, String>>) msg.getObj() ;
		this.projectService.sendMailByProjectStatus(projectId, (devIdsList.size()>0?devIdsList:null)) ;
		
		return msg ;
	}
	
	
	/***************************************图表Report 开始***********************************************/
	/**
	 * UI-项目统计图表
	 * @param mode
	 * @return
	 */
	@RequestMapping("/project_drawing_report_UI.do")
	public String project_drawing_report_UI(Model mode) {
		return Const.PROJECT + "project_drawing_report_UI" ;
	}
	/**
	 * 项目状态统计图表
	 * @return
	 */
	@RequestMapping("/get_project_status_count_report.do")
	public @ResponseBody List<Object[]> get_project_status_count_report(ProjectCenterForm form) {
		Object[] data0 = new Object[2] ;
		Object[] data1 = new Object[2] ;
		Object[] data2 = new Object[2] ;
		Object[] data3 = new Object[2] ;
		Object[] data4 = new Object[2] ;
		
		data0[0] = "立项中" ; data0[1] = 0 ;
		data1[0] = "审批中" ; data1[1] = 0 ;
		data2[0] = "进行中" ; data2[1] = 0 ;
		data3[0] = "挂起中" ; data3[1] = 0 ;
		data4[0] = "已结束" ; data4[1] = 0 ;
		
		List<Map<String, Object>> p = this.projectService.getProjectStatusCount(form) ;
		for (Map<String, Object> map : p) {
			String status = map.get("proj_status").toString() ;
			if("0".equals(status)) {
				data0[1] = Integer.parseInt(map.get("status_count").toString()) ; ;
			} else if("1".equals(status)) {
				data1[1] = Integer.parseInt(map.get("status_count").toString()) ; ;
			} else if("2".equals(status)) {
				data2[1] = Integer.parseInt(map.get("status_count").toString()) ;
			} else if("3".equals(status)) {
				data3[1] = Integer.parseInt(map.get("status_count").toString()) ; ;
			} else if("4".equals(status)) {
				data4[1] = Integer.parseInt(map.get("status_count").toString()) ; ;
			}
		}
		
		List<Object[]> project_report = new ArrayList<Object[]>() ;
		project_report.add(data0) ;
		project_report.add(data1) ;
		project_report.add(data2) ;
		project_report.add(data3) ;
		project_report.add(data4) ;
		return project_report ;
	}
	
	/**
	 * 项目ID类型区分
	 * @return
	 */
	@RequestMapping("/get_project_id_type_count_report.do")
	public @ResponseBody List<Object[]> get_project_id_type_count_report(ProjectCenterForm form) {
		Object[] data0 = new Object[2] ;
		Object[] data1 = new Object[2] ;
		Object[] data2 = new Object[2] ;
		Object[] data3 = new Object[2] ;
		
		data0[0] = "提案" ; data0[1] = 0 ;
		data1[0] = "合同项目" ; data1[1] = 0 ;
		data2[0] = "自主研发" ; data2[1] = 0 ;
		data3[0] = "公司使用" ; data3[1] = 0 ;
		
		List<Map<String, Object>> p = this.projectService.getProjectIdTypeCount(form) ;
		
		for (Map<String, Object> map : p) {
			String proj_id_type = map.get("distinguish").toString() ;
			String count = map.get("count").toString() ;
			if("提案".equals(proj_id_type)) {
				data0[1] = Integer.parseInt(count) ; 
			} else if("合同项目".equals(proj_id_type)) {
				data1[1] = Integer.parseInt(count) ; 
			} else if("自主研发".equals(proj_id_type)) {
				data2[1] = Integer.parseInt(count) ;
			} else if("公司使用".equals(proj_id_type)) {
				data3[1] = Integer.parseInt(count) ; 
			}
		}
		
		List<Object[]> project_report = new ArrayList<Object[]>() ;
		project_report.add(data0) ;
		project_report.add(data1) ;
		project_report.add(data2) ;
		project_report.add(data3) ;
		return project_report ;
	}
	
	/**
	 * 项目受注状态
	 * @return
	 */
	@RequestMapping("/get_project_shouzhu_count_report.do")
	public @ResponseBody List<Object[]> get_project_shouzhu_count_report(ProjectCenterForm form) {
		Object[] data0 = new Object[2] ;
		Object[] data1 = new Object[2] ;
		
		data0[0] = "未受注" ; data0[1] = 0 ;
		data1[0] = "已受注" ; data1[1] = 0 ;
		
		List<Map<String, Object>> p = this.projectService.getProjectShouZhuCount(form) ;
		for (Map<String, Object> map : p) {
			if(null != map.get("proj_shouzhu")) {
				String proj_shouzhu = map.get("proj_shouzhu").toString() ;
				String count = map.get("count").toString() ;
				if("未受注".equals(proj_shouzhu)) {
					data0[1] = Integer.parseInt(count) ; 
				} else if("已受注".equals(proj_shouzhu)) {
					data1[1] = Integer.parseInt(count) ; 
				} 
			}
		}
		
		List<Object[]> project_report = new ArrayList<Object[]>() ;
		project_report.add(data0) ;
		project_report.add(data1) ;
		return project_report ;
	}
	
	/**
	 * 项目财务结算状态
	 * @return
	 */
	@RequestMapping("/get_project_cwjszt_count_report.do")
	public @ResponseBody List<Object[]> get_project_cwjszt_count_report(ProjectCenterForm form) {
		Object[] data0 = new Object[2] ;
		Object[] data1 = new Object[2] ;
		
		data0[0] = "未结算" ; data0[1] = 0 ;
		data1[0] = "已结算" ; data1[1] = 0 ;
		
		List<Map<String, Object>> p = this.projectService.getProjectCWHSZTCount(form) ;
		for (Map<String, Object> map : p) {
			if(null != map.get("proj_cwjszt")) {
				String proj_cwjszt = map.get("proj_cwjszt").toString() ;
				String count = map.get("count").toString() ;
				if("未结算".equals(proj_cwjszt)) {
					data0[1] = Integer.parseInt(count) ; 
				} else if("已结算".equals(proj_cwjszt)) {
					data1[1] = Integer.parseInt(count) ; 
				} 
			}
		}
		
		List<Object[]> project_report = new ArrayList<Object[]>() ;
		project_report.add(data0) ;
		project_report.add(data1) ;
		return project_report ;
	}
	
	
	
	/***************************************我的项目 开始***********************************************/
	/**
	 * 我的项目
	 * @return
	 */
	@RequestMapping("/my_project_UI.do")
	public String my_project_UI() {
		return Const.PROJECT + "my_project_UI" ;
	}
	
	/**
	 * 以项目状态为基本查询条件，进行列表查询
	 * @return
	 */
	@RequestMapping("/get_myProject_list.do")
	public @ResponseBody DataGrid get_myProject_list(ProjectCenterForm form) {
		return this.projectService.findProjectListByStatus(form) ;
	}
	
	/**
	 * 我的项目审批页面
	 * @return
	 */
	@RequestMapping("/my_approve_UI.do")
	public String my_approve_UI() {
		return Const.PROJECT + "my_approve_UI" ;
	}
	
	/**
	 * 我的项目审批列表
	 * @return
	 */
	@RequestMapping("/get_myApprove_datagrid.do")
	public @ResponseBody DataGrid get_myApprove_datagrid(ProjectApproveForm form) {
		return this.projectService.get_myApprove_datagrid(form) ;
	}
	
	/**
	 * 项目审批不通过，进行邮件发送创建者修改
	 * @return
	 */
	@RequestMapping("/doNotNeedAuth_noApproveSendMail.do")
	public @ResponseBody Msg doNotNeedAuth_noApproveSendMail(ProjectCenterForm form) {
		return this.projectService.noApproveSendMail(form) ;
	}
	
	
	/**
	 * 项目审批不通过，项目创建者修改后，邮件通知审批人
	 * @return
	 */
	@RequestMapping("/doNotNeedAuth_sendApproveMail.do")
	public @ResponseBody Msg doNotNeedAuth_sendApproveMail(ProjectCenterForm form) {
		return this.projectService.sendMailApprove(form) ;
	}
	
	
	
	/***************************************人员查询 开始***********************************************/
	/**
	 * UI-人员查询
	 * @return
	 */
	@RequestMapping("/by_project_view_person.do")
	public String by_project_view_person() {
		return Const.PROJECT + "by_project_view_person" ;
	}
	
	@RequestMapping("/view_person_datagrid.do")
	public @ResponseBody DataGrid view_person_datagrid(PersonForm form) {
		DataGrid datagrid= null;
		try {
			datagrid = this.personService.datagrid(form);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return datagrid ;
	}
	
	/**
	 * 人员项目开发明细
	 * @return
	 */
	@RequestMapping("/get_person_project_UI.do")
	public String get_person_project_UI(String person_id, Model model) {
		model.addAttribute("person_id", person_id) ;
		return Const.PROJECT + "get_person_project_UI" ;
	}
	
	@RequestMapping("/get_person_project_info.do")
	public @ResponseBody DataGrid get_person_project_info(ProjectDevWorkTimeForm form) {
		DataGrid datagrid= null;
		try {
			datagrid = this.projectDevWorkService.datagrid(form);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return datagrid ;
	}
	
	/**
	 * 刷新项目定时器
	 */
	@RequestMapping("/refProjectTimer.do")
	@ResponseBody
	public void refProjectTimer(ProjectCenterForm form) {
		this.projectService.refProjectTimer(form) ;
	}
	
}
