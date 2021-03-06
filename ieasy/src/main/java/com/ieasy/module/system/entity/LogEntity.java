package com.ieasy.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ieasy.basic.dao.IdEntity;

/**
 * 用户操作日志
 */
@Entity
@Table(name = "ieasy_sys_log")
public class LogEntity extends IdEntity {

	private String userId ;						//用户ID
	
	private String userAccount ;				//用户账号
	
	private String name ;						//人员名称
	
	private String title ;						//操作标题
	
	private String ip ;							//用户登录IP
	
	private String detail ;						//操作明细
	
	private String remark ;						//备注
	
	private Date operDateTime ;					//操作时间

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getOperDateTime() {
		return operDateTime;
	}

	public void setOperDateTime(Date operDateTime) {
		this.operDateTime = operDateTime;
	}


}
