package com.ieasy.module.oa.project.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.PageHelper;

public class ProjectApproveForm extends PageHelper implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String person_id ;
	
	private String person_num ;
	
	private String person_name ;
	
	private String proj_id ;
	
	private String proj_num ;
	
	private String proj_name ;
	
	private Integer status = new Integer(0) ;			//审批状态（0：未审批，1：已审批）	

	public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}

	public String getPerson_num() {
		return person_num;
	}

	public void setPerson_num(String person_num) {
		this.person_num = person_num;
	}

	public String getPerson_name() {
		return person_name;
	}

	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}

	public String getProj_id() {
		return proj_id;
	}

	public void setProj_id(String proj_id) {
		this.proj_id = proj_id;
	}

	public String getProj_num() {
		return proj_num;
	}

	public void setProj_num(String proj_num) {
		this.proj_num = proj_num;
	}

	public String getProj_name() {
		return proj_name;
	}

	public void setProj_name(String proj_name) {
		this.proj_name = proj_name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
