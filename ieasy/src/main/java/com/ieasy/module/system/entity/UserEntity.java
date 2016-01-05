package com.ieasy.module.system.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 用户实体
 * @author t-work
 *
 */
@Entity @Table(name="ieasy_sys_user")
public class UserEntity extends ExtFieldEntity {

	private String account ;					//登陆账号
	
	private String password = "";				//登陆密码
	
	private Integer status = new Integer(0);	//账号状态（0：允许登录，1：禁止登录）
	
	private String lastAcceccTime ;				//最后访问时间

	private PersonEntity emp ;
	
	private Set<RoleEntity> roles = new HashSet<RoleEntity>(0) ;					//权限角色
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ieasy_sys_user_roles", joinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="emp_id", unique=true)
	public PersonEntity getEmp() {
		return emp;
	}

	public void setEmp(PersonEntity emp) {
		this.emp = emp;
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
	
}
