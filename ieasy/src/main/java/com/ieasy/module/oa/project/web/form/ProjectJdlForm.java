package com.ieasy.module.oa.project.web.form;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.ieasy.basic.model.PageHelper;

public class ProjectJdlForm extends PageHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	/**显示的属性*************************************************************************************/
	private String person_id ;							//人员ID
	
	private String person_num ;							//人员编号
	
	private String person_name ;						//人员名称
	
	private String emp_status ;							//人员状态（在职，离职，退休）
	
	private Date emp_enter_date ;						//入职时间
	
	private Date emp_dimission_date ;					//入职时间
	
	private String org_id ;								//组织机构ID
	
	private String org_name ;							//组织机构名称
	
	private int total_work_days ;						//统计人员总工作天数（不包括周六日）
	
	private int total_quot_work_days ; 					//统计人员乘与系数后的天数
	
	private String dbmType ;							//到部门类型
	
	private Date dbmDate ;								//到部门日期
	
	private String lbmType ;							//离部门类型
	
	private Date lbmDate ;								//离部门日期
	
	/** 1~12月的稼动率 */
	private Float m1 = new Float(0) ;
	private Float m2 = new Float(0) ;
	private Float m3 = new Float(0) ;
	private Float m4 = new Float(0) ;
	private Float m5 = new Float(0) ;
	private Float m6 = new Float(0) ;
	private Float m7 = new Float(0) ;
	private Float m8 = new Float(0) ;
	private Float m9 = new Float(0) ;
	private Float m10 = new Float(0) ;
	private Float m11 = new Float(0) ;
	private Float m12 = new Float(0) ;
	
	private String annualJdl ;							//个人年度稼动率
	
	/** 1~12月的标准工作天数（排除周六日），标准工作天数为可变，根据到部门或离部门类型变动 */
	private int bzts1 ;
	private int bzts2 ;
	private int bzts3 ;
	private int bzts4 ;
	private int bzts5 ;
	private int bzts6 ;
	private int bzts7 ;
	private int bzts8 ;
	private int bzts9 ;
	private int bzts10 ;
	private int bzts11 ;
	private int bzts12 ;
	
	private int totalBzts ;								//累加1~12月的标准天数
	
	/**编外条件属性*************************************************************************************/
	private int searchYear = -1;						//根据年份查询
	
	private String dept_ids ;							//部门IDS
	
	private boolean exclude_manager ;					//计算稼动率是，是否去除本部长
	
	private boolean product_dept_jdl ;					//只计算生产部门稼动率，根据组织机构中表示为计算稼动率的

	private String exclude_person_ids ;					//不计算稼动率的人员ID
	
	public String getExclude_person_ids() {
		return exclude_person_ids;
	}

	public void setExclude_person_ids(String exclude_person_ids) {
		this.exclude_person_ids = exclude_person_ids;
	}

	public boolean isProduct_dept_jdl() {
		return product_dept_jdl;
	}

	public void setProduct_dept_jdl(boolean product_dept_jdl) {
		this.product_dept_jdl = product_dept_jdl;
	}

	public boolean isExclude_manager() {
		return exclude_manager;
	}

	public void setExclude_manager(boolean exclude_manager) {
		this.exclude_manager = exclude_manager;
	}

	public String getDept_ids() {
		return dept_ids;
	}

	public void setDept_ids(String dept_ids) {
		this.dept_ids = dept_ids;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getEmp_enter_date() {
		return emp_enter_date;
	}

	public void setEmp_enter_date(Date emp_enter_date) {
		this.emp_enter_date = emp_enter_date;
	}
	@JSONField(format="yyyy-MM-dd")
	public Date getEmp_dimission_date() {
		return emp_dimission_date;
	}

	public void setEmp_dimission_date(Date emp_dimission_date) {
		this.emp_dimission_date = emp_dimission_date;
	}
	
	public String getAnnualJdl() {
		return annualJdl;
	}

	public void setAnnualJdl(String annualJdl) {
		this.annualJdl = annualJdl;
	}

	public int getBzts1() {
		return bzts1;
	}

	public void setBzts1(int bzts1) {
		this.bzts1 = bzts1;
	}

	public int getBzts2() {
		return bzts2;
	}

	public void setBzts2(int bzts2) {
		this.bzts2 = bzts2;
	}

	public int getBzts3() {
		return bzts3;
	}

	public void setBzts3(int bzts3) {
		this.bzts3 = bzts3;
	}

	public int getBzts4() {
		return bzts4;
	}

	public void setBzts4(int bzts4) {
		this.bzts4 = bzts4;
	}

	public int getBzts5() {
		return bzts5;
	}

	public void setBzts5(int bzts5) {
		this.bzts5 = bzts5;
	}

	public int getBzts6() {
		return bzts6;
	}

	public void setBzts6(int bzts6) {
		this.bzts6 = bzts6;
	}

	public int getBzts7() {
		return bzts7;
	}

	public void setBzts7(int bzts7) {
		this.bzts7 = bzts7;
	}

	public int getBzts8() {
		return bzts8;
	}

	public void setBzts8(int bzts8) {
		this.bzts8 = bzts8;
	}

	public int getBzts9() {
		return bzts9;
	}

	public void setBzts9(int bzts9) {
		this.bzts9 = bzts9;
	}

	public int getBzts10() {
		return bzts10;
	}

	public void setBzts10(int bzts10) {
		this.bzts10 = bzts10;
	}

	public int getBzts11() {
		return bzts11;
	}

	public void setBzts11(int bzts11) {
		this.bzts11 = bzts11;
	}

	public int getBzts12() {
		return bzts12;
	}

	public void setBzts12(int bzts12) {
		this.bzts12 = bzts12;
	}

	public int getTotalBzts() {
		return totalBzts;
	}

	public void setTotalBzts(int totalBzts) {
		this.totalBzts = totalBzts;
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

	public String getEmp_status() {
		return emp_status;
	}

	public void setEmp_status(String emp_status) {
		this.emp_status = emp_status;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public int getTotal_work_days() {
		return total_work_days;
	}

	public void setTotal_work_days(int total_work_days) {
		this.total_work_days = total_work_days;
	}

	public int getTotal_quot_work_days() {
		return total_quot_work_days;
	}

	public void setTotal_quot_work_days(int total_quot_work_days) {
		this.total_quot_work_days = total_quot_work_days;
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

	public Float getM1() {
		return m1;
	}

	public void setM1(Float m1) {
		this.m1 = m1;
	}

	public Float getM2() {
		return m2;
	}

	public void setM2(Float m2) {
		this.m2 = m2;
	}

	public Float getM3() {
		return m3;
	}

	public void setM3(Float m3) {
		this.m3 = m3;
	}

	public Float getM4() {
		return m4;
	}

	public void setM4(Float m4) {
		this.m4 = m4;
	}

	public Float getM5() {
		return m5;
	}

	public void setM5(Float m5) {
		this.m5 = m5;
	}

	public Float getM6() {
		return m6;
	}

	public void setM6(Float m6) {
		this.m6 = m6;
	}

	public Float getM7() {
		return m7;
	}

	public void setM7(Float m7) {
		this.m7 = m7;
	}

	public Float getM8() {
		return m8;
	}

	public void setM8(Float m8) {
		this.m8 = m8;
	}

	public Float getM9() {
		return m9;
	}

	public void setM9(Float m9) {
		this.m9 = m9;
	}

	public Float getM10() {
		return m10;
	}

	public void setM10(Float m10) {
		this.m10 = m10;
	}

	public Float getM11() {
		return m11;
	}

	public void setM11(Float m11) {
		this.m11 = m11;
	}

	public Float getM12() {
		return m12;
	}

	public void setM12(Float m12) {
		this.m12 = m12;
	}

	public int getSearchYear() {
		return searchYear;
	}

	public void setSearchYear(int searchYear) {
		this.searchYear = searchYear;
	}


}
