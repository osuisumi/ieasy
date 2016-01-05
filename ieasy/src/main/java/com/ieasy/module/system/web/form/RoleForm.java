package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.PageHelper;

public class RoleForm extends PageHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name ;				//组名称
	
	private String sn ;					//组序列号
	
	private String remark ;				//组备注
	
	private String user_ids ;			//用户Ids
	
	private String user_names ;			//用户名称
	
	private String menu_ids ;			//资源Ids
	
	private String menu_names ;			//资源名称
	
	private int defaultRole ;		//默认分配角色
	

	public int getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(int defaultRole) {
		this.defaultRole = defaultRole;
	}

	public String getMenu_ids() {
		return menu_ids;
	}

	public void setMenu_ids(String menu_ids) {
		this.menu_ids = menu_ids;
	}

	public String getMenu_names() {
		return menu_names;
	}

	public void setMenu_names(String menu_names) {
		this.menu_names = menu_names;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUser_ids() {
		return user_ids;
	}

	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}

	public String getUser_names() {
		return user_names;
	}

	public void setUser_names(String user_names) {
		this.user_names = user_names;
	}
}