package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.EasyuiTree;

public class PositionForm extends EasyuiTree<PositionForm> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name ;			//岗位名称
	
	private String sn ;				//岗位序号
	
	private Integer sort ;			//岗位排序号
	
	private String person_id ;		//人员ID

	public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String person_id) {
		this.person_id = person_id;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
