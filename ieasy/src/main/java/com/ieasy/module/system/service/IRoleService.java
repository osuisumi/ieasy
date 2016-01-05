package com.ieasy.module.system.service;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.entity.RoleEntity;
import com.ieasy.module.system.web.form.RoleForm;

public interface IRoleService {
	
	/**
	 * 添加角色
	 * @param form
	 * @return
	 */
	public Msg add(RoleForm form) ;
	
	/**
	 * 删除角色
	 * @param form
	 * @return
	 */
	public Msg delete(RoleForm form) ;
	
	/**
	 * 修改角色
	 * @param form
	 * @return
	 */
	public Msg update(RoleForm form) ;
	
	/**
	 * 获取一个角色对象
	 * @param form
	 * @return
	 */
	public RoleForm get(RoleForm form) ;
	
	/**
	 * 角色列表查询
	 * @param form
	 * @return
	 */
	public DataGrid datagrid(RoleForm form) ;
	
	/**
	 * 获取角色的资源
	 * @param form
	 * @return
	 */
	public RoleForm getRoleMenus(RoleForm form) ;

	/**
	 * 角色数据初始化
	 * @param entity
	 */
	public void init(RoleEntity entity) ;
	
}
