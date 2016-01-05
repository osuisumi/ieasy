package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.EasyuiTree;

public class OrgForm extends EasyuiTree<OrgForm> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name ;							//组织机构名称
	
	private Integer sort ;							//组织机构的排序号
	
	private String type ;							//组织机构的类型（机构：O、部门：D）
	
	private String sn ;								//组织机构序列号
	
	private String iconCls ;						//EasyUI tree的图标
	
	private String state ;							//EasyUI tree的展开或隐藏(open、closed)

	private boolean sumJdl ;						//是否计算稼动率
	
	public boolean isSumJdl() {
		return sumJdl;
	}

	public void setSumJdl(boolean sumJdl) {
		this.sumJdl = sumJdl;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
}
