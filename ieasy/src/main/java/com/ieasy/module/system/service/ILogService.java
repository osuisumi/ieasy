package com.ieasy.module.system.service;

import java.util.List;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.web.form.LogForm;
import com.ieasy.module.system.web.form.LoginLogForm;

public interface ILogService {
	
	/**
	 * 添加用户操作日志
	 * @param title
	 * @param detail
	 * @param remark
	 * @param ip
	 */
	public void add(String title, String detail, String remark) ;
	public void add(String title, String detail) ;
	public void add(String title) ;
	
	/**
	 * 删除用户操作日志
	 * @param form
	 * @return
	 */
	public Msg delete(LogForm form) ;
	
	/**
	 * 获取一个用户操作日志对象
	 * @param form
	 * @return
	 */
	public LogForm get(LogForm form) ;
	
	/**
	 * 用户操作日志列表查询
	 * @param form
	 * @return
	 */
	public DataGrid datagrid(LogForm form) ;
	
	
	/****************************************************************************/
	/**
	 * 添加用户登录日志
	 * @param form
	 * @return
	 */
	public Msg addLL(LoginLogForm form) ;
	
	/**
	 * 删除用户登录日志
	 * @param form
	 * @return
	 */
	public Msg deleteLL(LoginLogForm form) ;
	
	/**
	 * 获取一个用户登录日志对象
	 * @param form
	 * @return
	 */
	public LoginLogForm getLL(LoginLogForm form) ;
	
	/**
	 * 用户登录日志列表查询
	 * @param form
	 * @return
	 */
	public DataGrid datagridLL(LoginLogForm form) ;
	
	/**
	 * 用户登录时段报表
	 * @return
	 */
	public List<Long> loginTimeChartLL() ;
	
}
