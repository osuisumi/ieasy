package com.ieasy.module.common.dto;

import com.ieasy.basic.util.poi.excel.ExcelResources;

public class EmpExcelDto {

	
	@ExcelResources(title="编号", order=1)
	private String num ;											//员工编号
	
	@ExcelResources(title="姓名", order=2)
	private String name ;											//姓名
	
	@ExcelResources(title="性别", order=3)
	private String sex ;											//性别

	@ExcelResources(title="出生日期", order=4)
	private String birth ;											//出生日期
	
	@ExcelResources(title="邮件地址", order=5)
	private String email ;											//邮箱地址
	
	@ExcelResources(title="部门名称", order=6)
	private String dept_name ;										//部门
	
	@ExcelResources(title="岗位", order=7)
	private String position_name ;									//岗位
	
	/**************************************职位信息*****************************************************/
	
	@ExcelResources(title="状态", order=8)
	private String empState ;										//人员状态（1：在职，2：离职，3：退休）
	
	@ExcelResources(title="离职日期", order=9)
	private String dimissionDate ;									//离职日期
	
	@ExcelResources(title="入职时间", order=10)
	private String enterDate ;										//入职时间
	
	@ExcelResources(title="转正日期", order=11)
	private String changeDate ;										//转正日期
	
	/**************************************稼动率或项目相关属性*****************************************************/
	@ExcelResources(title="到部门类型", order=12)
	private String dbmType ;										//到部门类型(1:在职,2:转入,3:新增,4:试用,5:停薪留职返回,6:返聘)
	
	@ExcelResources(title="到部门日期", order=13)
	private String dbmDate ;										//到部门日期
	
	@ExcelResources(title="离部门类型", order=14)
	private String lbmType ;										//离部门日期(1:转出-到开发部,2:转出-到非开发部,3:离职,4:停薪留职)
	
	@ExcelResources(title="离部门日期", order=15)
	private String lbmDate ;										//离部门类型
	
	@ExcelResources(title="项目工作状态", order=16)
	private String byProjectWorkStatus ;							//0在项目中，1为待机状态，-1离职

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public String getEmpState() {
		return empState;
	}

	public void setEmpState(String empState) {
		this.empState = empState;
	}

	public String getDimissionDate() {
		return dimissionDate;
	}

	public void setDimissionDate(String dimissionDate) {
		this.dimissionDate = dimissionDate;
	}

	public String getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(String enterDate) {
		this.enterDate = enterDate;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

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

	public String getByProjectWorkStatus() {
		return byProjectWorkStatus;
	}

	public void setByProjectWorkStatus(String byProjectWorkStatus) {
		this.byProjectWorkStatus = byProjectWorkStatus;
	}

}
