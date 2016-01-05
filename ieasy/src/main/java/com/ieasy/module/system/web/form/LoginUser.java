package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.util.StringUtil;

public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String account ;
	
	private String password ;
	
	private int status ;
	
	private String user_id ;
	
	private String emp_id ;
	
	private String emp_num ;
	
	private String emp_name ;
	
	private String emp_tx ;
	
	private String org_id ;
	
	private String org_name ;
	
	private String emp_email ;
	
	private String validCode ;
	
	private String ip ;
	
	
	public String getEmp_num() {
		return emp_num;
	}

	public void setEmp_num(String emp_num) {
		this.emp_num = emp_num;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmp_email() {
		return emp_email;
	}

	public void setEmp_email(String emp_email) {
		this.emp_email = emp_email;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmp_tx() {
		return emp_tx;
	}

	public void setEmp_tx(String emp_tx) {
		if(null != emp_tx && !"".equals(emp_tx.trim())) {
			emp_tx = StringUtil.urlPath(emp_tx) ;
		}
		this.emp_tx = emp_tx;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
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

	@Override
	public String toString() {
		return "LoginForm [account=" + account + ", password=" + password
				+ ", user_id=" + user_id + ", emp_id=" + emp_id + ", emp_name="
				+ emp_name + ", org_id=" + org_id + ", org_name=" + org_name
				+ "]";
	}
	
	
}
