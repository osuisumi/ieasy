package com.ieasy.module.oa.project.service;

import java.util.List;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.oa.project.web.form.ProjectDevWorkTimeForm;

public interface IProjectDevWorkTimeService {

	public Msg add(ProjectDevWorkTimeForm form) ;
	
	public Msg delete(ProjectDevWorkTimeForm form) ;
	
	/**
	 * 根据项目ID进行删除
	 * @param projectId
	 * @return
	 */
	public Msg deleteByProjectId(String projectId) ;
	
	/**
	 * 修改开发人员进入项目的开始和结束日期和加班信息
	 * 客户端传入开发人员进项目的开始和结束日期的数据形式，如下
	 * ID,项目中的角色,开始日期,结束日期,平时加班小时,周六日加班小时,节假日加班小时|ID,项目中的角色,开始日期,结束日期,平时加班小时,周六日加班小时,节假日加班小时
	 * @param form
	 * @throws RuntimeException
	 */
	public Msg update(ProjectDevWorkTimeForm form) ;
	
	public ProjectDevWorkTimeForm getProjectDetail(String id) ;
	
	public DataGrid datagrid(ProjectDevWorkTimeForm form) ;
	
	public List<ProjectDevWorkTimeForm> listDev(ProjectDevWorkTimeForm form) ;
	
	/**
	 * 人员退出项目
	 * @param form
	 * @return
	 */
	public Msg exitProject(ProjectDevWorkTimeForm form) ;
	
	/**
	 * 修改已退出的人员的开始和结束时间 
	 * @param form
	 * @return
	 */
	public Msg modifySEDate(ProjectDevWorkTimeForm form) ;
	
	/**
	 * 删除开发人员的数据
	 * @param form
	 * @return
	 */
	public Msg deleteDevPerson(ProjectDevWorkTimeForm form) ;
	
}
