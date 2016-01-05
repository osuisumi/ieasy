package com.ieasy.module.common.dto;

import com.ieasy.basic.util.poi.excel.ExcelResources;

public class UserExcelDto {

	private String num ;					//用户编号
	
	private String name ;					//用户名称
	
	private String sex ;					//性别
	
	private String mobile ;					//手机号码
	
	private String email ;					//邮件地址
	
	private String account ;				//用户账号
	
	private String password ;				//用户密码
	
	private String status ;					//账号状态
	
	private boolean state ;					//用于返回数据导入的状态
	
	private String msg ;					//用于返回数据导入的提示信息

	@ExcelResources(title="用户编号", order=1)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@ExcelResources(title="用户名称", order=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelResources(title="用户账号", order=3)
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}

	@ExcelResources(title="性别", order=4)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ExcelResources(title="手机号码", order=4)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelResources(title="邮件地址", order=5)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@ExcelResources(title="用户密码", order=6)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ExcelResources(title="账号状态", order=7)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "UserExcelDto [num=" + num + ", name=" + name + ", account="
				+ account + ", password=" + password + ", status=" + status
				+ ", state=" + state + ", msg=" + msg + "]";
	}

	public UserExcelDto(String num, String name, String account,
			String password, String status) {
		super();
		this.num = num;
		this.name = name;
		this.account = account;
		this.password = password;
		this.status = status;
	}

	public UserExcelDto() {
		super();
	}
}
