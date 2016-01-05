package com.ieasy.module.oa.project.service;

import java.util.List;
import java.util.Map;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.oa.project.web.form.ProjectApproveForm;
import com.ieasy.module.oa.project.web.form.ProjectCenterForm;

public interface IProjectCenterService {

	public Msg createProject(ProjectCenterForm form) ;
	
	public Msg depeteProject(ProjectCenterForm form) ;
	
	public Msg updateProject(ProjectCenterForm form) ;
	
	public ProjectCenterForm getProjectDetail(String project_id) ;
	
	public ProjectCenterForm getProjectDetail(ProjectCenterForm form) ;
	
	public List<Map<String, Object>> getProjectStatusCount(ProjectCenterForm form) ;
	public List<Map<String, Object>> getProjectIdTypeCount(ProjectCenterForm form) ;
	public List<Map<String, Object>> getProjectShouZhuCount(ProjectCenterForm form) ;
	public List<Map<String, Object>> getProjectCWHSZTCount(ProjectCenterForm form) ;
	
	/**
	 * 以项目状态为基本查询条件，进行列表查询
	 * @param form
	 * @return
	 */
	public DataGrid findProjectListByStatus(ProjectCenterForm form) ;
	
	/**
	 * 设置项目的状态
	 * @param status
	 * @return
	 */
	public void setProjectStatus(int status, String id) ;
	
	/**
	 * 转换状态，如项目已结束，讲项目转换为进行中
	 * @param projectId
	 * @param status
	 * @return
	 */
	public Msg changeProjectStatus(String projectId, Integer status) ;
	
	/**
	 * 审批
	 * @param personId
	 * @param projectId
	 */
	public Msg approve(String personId, String projectId) ;

	/**
	 * 项目挂起
	 * @param projectId
	 * @return
	 */
	public Msg sleeping(String projectId);

	/**
	 * 项目激活
	 * @param projectId
	 * @return
	 */
	public Msg activated(String projectId);

	/**
	 * 项目办结
	 * @param projectId
	 * @return
	 */
	public Msg finish(String projectId);

	/**
	 * 我的项目审批列表
	 * @param form
	 * @return
	 */
	public DataGrid get_myApprove_datagrid(ProjectApproveForm form);
	
	
	/**定时器操作*******************************************************************************/
	public void setProjectTimer(String projectId) ;
	
	public void deleteProjectTime(String projectId) ;
	
	public void setProjectDevTimer(String projectId) ;
	
	public void deleteProjectDevTime(String projectId) ;
	
	public void refProjectTimer(ProjectCenterForm form) ;
	
	
	/**
	 * 定时调用，项目即将到期提醒，项目结束通知
	 * @param taskCode
	 */
	public void sendEmailProjectInfoForTimer(String taskCode) ;
	
	/**
	 * 定时调用,开发人员即将到期提醒,开发人员到期退出提醒
	 * @param taskCode
	 */
	public void sendEmailProjectDevInfoForTimer(String taskCode) ;
	
	
	/**
	 * 发送邮件，项目状态变更
	 * @param project_id
	 */
	public void sendMailByProjectStatus(String project_id, List<Map<String, String>> devIdsList) ;
	
	/**
	 * 发送邮件，项目起止时间变更
	 * @param project_id
	 */
	public void sendMailByProjectDate(String project_id) ;
	
	/**
	 * 项目开发人员变更，发送邮件
	 * 人员日期变更（发送邮件）
	 * 人员退出（发送邮件）
	 * 人员新增（发送邮件）
	 * @param project_id
	 * @param sendStatus 发送的类型（exit人员退出，change（新增，日期变更））
	 */
	public void sendMailByProjectDevStatus(String project_id, String sendStatus, List<String> changeIds) ;
	
	/**
	 * 发送邮件，事务通知（审批）
	 * @param project_id
	 */
	public void sendMailByApproveAffair(String[] person_ids, String project_id) ;

	/**
	 * 项目审批不通过，邮件通知创建者修改项目内容
	 * @param form
	 * @return
	 */
	public Msg noApproveSendMail(ProjectCenterForm form);

	/**
	 * 项目审批不通过，项目创建者修改后，邮件通知审判者
	 * @param form
	 * @return
	 */
	public Msg sendMailApprove(ProjectCenterForm form);
}
