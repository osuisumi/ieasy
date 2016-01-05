package com.ieasy.module.oa.project.web.form;

import java.io.Serializable;
import java.util.Date;

import com.ieasy.basic.model.PageHelper;

public class ProjectDevWorkTimeForm extends PageHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	/*********************************************稼动率计算基本信息*************************************************************/
	private String person_id ;												//人员ID
	
	private String person_num ;												//人员编号
	
	private String person_name ;											//人员名称
	
	private String proj_id ;												//项目ID
	
	private String proj_num ;												//项目编号
	
	private String proj_name ;												//项目名称
	
	private String proj_role ;												//在项目中担任的角色
	
	private Date work_startDate ;											//进入项目工作的开始时间
	
	private Date work_endDate ;												//进入项目工作的结束时间
	
	private Integer work_status	;											//作业状态（1：进行中，0：已结束）
	
	
	
	/*********************************************开发人员加班信息*************************************************************/
	private float normalHour ;												//平时加班小时
	
	private float weekendHour ;												//周六日加班小时
	
	private float holidaysHour ;											//节假日加班小时
	
	
	/*********************************************编外属性*************************************************************/
	private String dbmType ;												//到部门类型
	
	private Date dbmDate ;													//到部门日期
	
	private String lbmType ;												//离部门类型
	
	private Date lbmDate ;													//离部门日期	
	
	private String positionRecord ;											//变更记录
	
	private String develops_worktime ;										//【编外属性】开发人员在项目中的作业开始和结束日期
	
	private Date work_begin_startDate ;										//【编外属性】根据项目开始时间进行范围查询(客户传入的开始时间)
	
	private Date work_begin_endDate ;										//【编外属性】根据项目开始时间进行范围查询(客户传入的结束时间)
	
	private Date work_finish_startDate ;									//【编外属性】根据项目结束时间进行范围查询(客户传入的开始时间)
	
	private Date work_finish_endDate ;										//【编外属性】根据项目结束时间进行范围查询(客户传入的结束时间)

	private int devCyc ;													//开发周期，工作天数，排除周六日
	
	private int ext_devCyc ;												//已消耗的天数（开发周期）个人
	
	private float proj_quot ;												//项目系数
	
	private float totalHour ;												//累计加班小时
	
	private String dept_name ;												//部门名称
	
	private float ry ;														//人月
	
	private float ext_ry ;													//已消耗人月
	
	private String jbl ;													//加班率
	
	private String sendStatus ;												//邮件发送的类型（exit人员退出）
	
	private String changeIds ;												//变更的人员Ids
	
	private String empStatus ;												//人员状态（在职，离职，退休）
	
	private String positionName ;											//公司岗位名称
	
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getChangeIds() {
		return changeIds;
	}

	public void setChangeIds(String changeIds) {
		this.changeIds = changeIds;
	}

	public String getDbmType() {
		return dbmType;
	}

	public void setDbmType(String dbmType) {
		this.dbmType = dbmType;
	}

	public Date getDbmDate() {
		return dbmDate;
	}

	public void setDbmDate(Date dbmDate) {
		this.dbmDate = dbmDate;
	}

	public String getLbmType() {
		return lbmType;
	}

	public void setLbmType(String lbmType) {
		this.lbmType = lbmType;
	}

	public Date getLbmDate() {
		return lbmDate;
	}

	public void setLbmDate(Date lbmDate) {
		this.lbmDate = lbmDate;
	}

	public String getPositionRecord() {
		return positionRecord;
	}

	public void setPositionRecord(String positionRecord) {
		this.positionRecord = positionRecord;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getExt_devCyc() {
		return ext_devCyc;
	}

	public void setExt_devCyc(int ext_devCyc) {
		this.ext_devCyc = ext_devCyc;
	}


	public String getJbl() {
		return jbl;
	}

	public void setJbl(String jbl) {
		this.jbl = jbl;
	}

	public float getExt_ry() {
		return ext_ry;
	}

	public void setExt_ry(float ext_ry) {
		this.ext_ry = ext_ry;
	}


	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public Integer getWork_status() {
		return work_status;
	}

	public void setWork_status(Integer work_status) {
		this.work_status = work_status;
	}

	public float getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(float totalHour) {
		this.totalHour = totalHour;
	}

	public float getProj_quot() {
		return proj_quot;
	}

	public void setProj_quot(float proj_quot) {
		this.proj_quot = proj_quot;
	}

	public int getDevCyc() {
		return devCyc;
	}

	public void setDevCyc(int devCyc) {
		this.devCyc = devCyc;
	}

	public Date getWork_begin_startDate() {
		return work_begin_startDate;
	}

	public void setWork_begin_startDate(Date work_begin_startDate) {
		this.work_begin_startDate = work_begin_startDate;
	}

	public Date getWork_begin_endDate() {
		return work_begin_endDate;
	}

	public void setWork_begin_endDate(Date work_begin_endDate) {
		this.work_begin_endDate = work_begin_endDate;
	}

	public Date getWork_finish_startDate() {
		return work_finish_startDate;
	}

	public void setWork_finish_startDate(Date work_finish_startDate) {
		this.work_finish_startDate = work_finish_startDate;
	}

	public Date getWork_finish_endDate() {
		return work_finish_endDate;
	}

	public void setWork_finish_endDate(Date work_finish_endDate) {
		this.work_finish_endDate = work_finish_endDate;
	}


	public String getDevelops_worktime() {
		return develops_worktime;
	}

	public void setDevelops_worktime(String develops_worktime) {
		this.develops_worktime = develops_worktime;
	}

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

	public String getProj_role() {
		return proj_role;
	}

	public void setProj_role(String proj_role) {
		this.proj_role = proj_role;
	}

	public Date getWork_startDate() {
		return work_startDate;
	}

	public void setWork_startDate(Date work_startDate) {
		this.work_startDate = work_startDate;
	}

	public Date getWork_endDate() {
		return work_endDate;
	}

	public void setWork_endDate(Date work_endDate) {
		this.work_endDate = work_endDate;
	}

	public float getNormalHour() {
		return normalHour;
	}

	public void setNormalHour(float normalHour) {
		this.normalHour = normalHour;
	}

	public float getWeekendHour() {
		return weekendHour;
	}

	public void setWeekendHour(float weekendHour) {
		this.weekendHour = weekendHour;
	}

	public float getHolidaysHour() {
		return holidaysHour;
	}

	public void setHolidaysHour(float holidaysHour) {
		this.holidaysHour = holidaysHour;
	}

}
