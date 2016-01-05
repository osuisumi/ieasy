package com.ieasy.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ieasy.basic.dao.IdEntity;

/**
 * 用户登录日志
 */
@Entity
@Table(name = "ieasy_sys_log_login")
public class LoginLogEntity extends IdEntity {

	private String loginAccount ;			//登陆账号
	
	private String name ;					//姓名
	
	private Date loginTime ;				//登陆时间
	
	private String ip ;						//IP地址
	
	private String browserType ;			//浏览器类型
	
	private String browserVersion ;			//浏览器版本

	private String platformType ;			//平台类型
	
	private String detail ;					//详细信息

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
