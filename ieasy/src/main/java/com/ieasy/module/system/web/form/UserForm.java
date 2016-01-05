package com.ieasy.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import com.ieasy.basic.model.PageHelper;

public class UserForm extends PageHelper implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String account ;					//登陆账号
	
	private String password ;					//登陆密码
	
	private Integer status = new Integer(0);	//账号状态（0：允许登录，1：禁止登录）
	
	private String lastAcceccTime ;				//最后访问时间
	
	private String emp_id ;						//人员账号ID
	
	private String num ;						//人员编号
	
	private String name ;						//人员名称
	
	private String empState ;					//人员状态
	
	private String sex ;						//人员性别
	
	private String email ;						//邮箱地址
	
	private String mobile ;						//手机号码
	
	private String org_id ;						//组织机构ID
	
	private String org_name ;					//组织机构名称
	
	private String position_id ;				//岗位ID
	
	private String position_name ;				//岗位名称
	
	private String diffDatetime ;				//相差[天、时、分、秒]
	
	private Date startDate ;					//开始日期
	
	private Date endDate ;						//结束日期

	private String role_ids ;					//角色Ids
	
	private String role_names ;					//角色名称
	
	private String oldPwd ;						//旧密码
	
	private boolean sendMailNotity ;			//添加人员，是否发送邮件通知
	

	public boolean isSendMailNotity() {
		return sendMailNotity;
	}

	public void setSendMailNotity(boolean sendMailNotity) {
		this.sendMailNotity = sendMailNotity;
	}

	public String getEmpState() {
		return empState;
	}

	public void setEmpState(String empState) {
		this.empState = empState;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getPosition_id() {
		return position_id;
	}

	public void setPosition_id(String position_id) {
		this.position_id = position_id;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public String getRole_ids() {
		return role_ids;
	}

	public void setRole_ids(String role_ids) {
		this.role_ids = role_ids;
	}

	public String getRole_names() {
		return role_names;
	}

	public void setRole_names(String role_names) {
		this.role_names = role_names;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getDiffDatetime() {
		return diffDatetime;
	}

	public void setDiffDatetime(String diffDatetime) {
		this.diffDatetime = diffDatetime;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLastAcceccTime() {
		return lastAcceccTime;
	}

	public void setLastAcceccTime(String lastAcceccTime) {
		this.lastAcceccTime = lastAcceccTime;
	}

	@Override
	public String toString() {
		return "UserForm [account=" + account + ", password=" + password + ", status=" + status + ", lastAcceccTime=" + lastAcceccTime + ", emp_id=" + emp_id + ", num=" + num + ", name=" + name + ", sex=" + sex + ", email=" + email + ", mobile=" + mobile + ", org_id=" + org_id + ", org_name=" + org_name + ", diffDatetime=" + diffDatetime + ", startDate=" + startDate + ", endDate=" + endDate + ", role_ids=" + role_ids + ", role_names=" + role_names + "]";
	}

}
