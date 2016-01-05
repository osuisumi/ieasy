package com.ieasy.module.oa.project.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ieasy.basic.dao.ExtFieldEntity;
import com.ieasy.module.system.entity.PersonEntity;

/**
 * 开发人员的项目作业起始时间和加班信息
 * @author Administrator
 *
 */
@Entity @Table(name="ieasy_oa_project_dev_worktime")
public class ProjectDevWorkTime extends ExtFieldEntity {

	/*********************************************稼动率计算基本信息*************************************************************/
	private String person_num ;												//人员编号
	
	private String person_name ;											//人员名称
	
	private String proj_num ;												//项目编号
	
	private String proj_name ;												//项目名称
	
	private String proj_role ;												//在项目中担任的角色
	
	private Date work_startDate ;											//进入项目工作的开始时间
	
	private Date work_endDate ;												//进入项目工作的结束时间
	
	private Integer work_status	;											//作业状态（1：进行中，0：已结束）
	
	
	/*********************************************开发人员加班信息*************************************************************/
	private Float normalHour ;												//平时加班小时
	
	private Float weekendHour ;												//周六日加班小时
	
	private Float holidaysHour ;											//节假日加班小时
	
	
	
	/*********************************************开发人员加班信息*************************************************************/
	private PersonEntity person ;											//与人员多对一关联
	
	private ProjectCenter projectCenter ;									//与项目多对一关联

	
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

	public Integer getWork_status() {
		return work_status;
	}

	public void setWork_status(Integer work_status) {
		this.work_status = work_status;
	}

	public String getProj_role() {
		return proj_role;
	}

	public void setProj_role(String proj_role) {
		this.proj_role = proj_role;
	}

	@Temporal(TemporalType.DATE)
	public Date getWork_startDate() {
		return work_startDate;
	}

	public void setWork_startDate(Date work_startDate) {
		this.work_startDate = work_startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getWork_endDate() {
		return work_endDate;
	}

	public void setWork_endDate(Date work_endDate) {
		this.work_endDate = work_endDate;
	}

	public Float getNormalHour() {
		return normalHour;
	}

	public void setNormalHour(Float normalHour) {
		this.normalHour = normalHour;
	}

	public Float getWeekendHour() {
		return weekendHour;
	}

	public void setWeekendHour(Float weekendHour) {
		this.weekendHour = weekendHour;
	}

	public Float getHolidaysHour() {
		return holidaysHour;
	}

	public void setHolidaysHour(Float holidaysHour) {
		this.holidaysHour = holidaysHour;
	}
	
}
