package com.ieasy.module.common.dto;

import com.ieasy.basic.util.poi.excel.ExcelResources;

public class ProjectDevWorkTimeDto {

	/*********************************************稼动率计算基本信息*************************************************************/
	
	@ExcelResources(title="编号", order=1)
	private String person_num ;												//人员编号
	
	@ExcelResources(title="名称", order=2)
	private String person_name ;											//人员名称
	
	@ExcelResources(title="部门名称", order=3)
	private String dept_name ;												//部门名称
	
	@ExcelResources(title="公司岗位", order=4)
	private String positionName ;											//公司岗位
	
	@ExcelResources(title="担任角色", order=5)
	private String proj_role ;												//在项目中担任的角色
	
	@ExcelResources(title="系数", order=6)									//系数
	private float proj_quot ;
	
	@ExcelResources(title="开始日期", order=7)
	private String work_startDate ;											//进入项目工作的开始时间
	
	@ExcelResources(title="结束日期", order=8)
	private String work_endDate ;											//进入项目工作的结束时间
	
	@ExcelResources(title="天数", order=9)
	private int devCyc ;													//开发周期，工作天数，排除周六日
	
	@ExcelResources(title="人月", order=10)
	private float ry ;														//人月
	
	@ExcelResources(title="已消耗人月", order=11)
	private float ext_ry ;													//已消耗人月
	
	@ExcelResources(title="已消耗天数", order=12)
	private int ext_devCyc ;												//已消耗的天数（开发周期）个人
	
	@ExcelResources(title="平时加班", order=13)
	private float normalHour ;												//平时加班小时
	
	@ExcelResources(title="周六日加班", order=14)
	private float weekendHour ;												//周六日加班小时
	
	@ExcelResources(title="节假日加班", order=15)
	private float holidaysHour ;											//节假日加班小时
	
	@ExcelResources(title="累计加班小时", order=16)
	private float totalHour ;												//累计加班小时
	
	@ExcelResources(title="加班率", order=17)
	private String jbl ;													//加班率
	
	@ExcelResources(title="状态", order=18)
	private String work_status	;											//作业状态（1：进行中，0：已结束）
	
	@ExcelResources(title="员工状态", order=19)
	private String empStatus	;											//员工状态
	
	@ExcelResources(title="到部门类型", order=20)
	private String dbmType ;												//到部门类型
	
	@ExcelResources(title="到部门日期", order=21)
	private String dbmDate ;												//到部门日期
	
	@ExcelResources(title="离部门类型", order=22)
	private String lbmType ;												//离部门类型
	
	@ExcelResources(title="离部门日期", order=23)
	private String lbmDate ;												//离部门日期	
	
	@ExcelResources(title="岗位变更记录", order=24)
	private String positionRecord ;											//岗位变更记录
	
	public String getDbmType() {
		return dbmType;
	}

	public void setDbmType(String dbmType) {
		this.dbmType = dbmType;
	}

	public String getDbmDate() {
		return dbmDate;
	}

	public void setDbmDate(String dbmDate) {
		this.dbmDate = dbmDate;
	}

	public String getLbmType() {
		return lbmType;
	}

	public void setLbmType(String lbmType) {
		this.lbmType = lbmType;
	}

	public String getLbmDate() {
		return lbmDate;
	}

	public void setLbmDate(String lbmDate) {
		this.lbmDate = lbmDate;
	}

	public String getPositionRecord() {
		return positionRecord;
	}

	public void setPositionRecord(String positionRecord) {
		this.positionRecord = positionRecord;
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

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getProj_role() {
		return proj_role;
	}

	public void setProj_role(String proj_role) {
		this.proj_role = proj_role;
	}

	public float getProj_quot() {
		return proj_quot;
	}

	public void setProj_quot(float proj_quot) {
		this.proj_quot = proj_quot;
	}

	public String getWork_status() {
		return work_status;
	}

	public void setWork_status(String work_status) {
		this.work_status = work_status;
	}

	public String getWork_startDate() {
		return work_startDate;
	}

	public void setWork_startDate(String work_startDate) {
		this.work_startDate = work_startDate;
	}

	public String getWork_endDate() {
		return work_endDate;
	}

	public void setWork_endDate(String work_endDate) {
		this.work_endDate = work_endDate;
	}

	public int getDevCyc() {
		return devCyc;
	}

	public void setDevCyc(int devCyc) {
		this.devCyc = devCyc;
	}

	public int getExt_devCyc() {
		return ext_devCyc;
	}

	public void setExt_devCyc(int ext_devCyc) {
		this.ext_devCyc = ext_devCyc;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getExt_ry() {
		return ext_ry;
	}

	public void setExt_ry(float ext_ry) {
		this.ext_ry = ext_ry;
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

	public float getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(float totalHour) {
		this.totalHour = totalHour;
	}

	public String getJbl() {
		return jbl;
	}

	public void setJbl(String jbl) {
		this.jbl = jbl;
	}

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

}
