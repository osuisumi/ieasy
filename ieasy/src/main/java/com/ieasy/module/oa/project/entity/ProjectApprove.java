package com.ieasy.module.oa.project.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ieasy.basic.dao.IdEntity;
import com.ieasy.module.system.entity.PersonEntity;

/**
 * 项目审批人
 * @author Administrator
 *
 */
@Entity @Table(name="ieasy_oa_project_approve")
public class ProjectApprove extends IdEntity {

	private String person_num ;
	
	private String person_name ;
	
	private String proj_num ;
	
	private String proj_name ;
	
	private Integer status = new Integer(0) ;			//审批状态（0：未审批，1：已审批）	
	
	private PersonEntity person ;						//关联人员
	
	private ProjectCenter projectCenter ;				//关联项目
	
	@ManyToOne
	@JoinColumn(name="person_id")
	public PersonEntity getPerson() {
		return person;
	}

	public void setPerson(PersonEntity person) {
		this.person = person;
	}
	
	@ManyToOne
	@JoinColumn(name="project_id")
	public ProjectCenter getProjectCenter() {
		return projectCenter;
	}

	public void setProjectCenter(ProjectCenter projectCenter) {
		this.projectCenter = projectCenter;
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
