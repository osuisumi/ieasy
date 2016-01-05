package com.ieasy.module.system.entity;

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 角色实体
 * @author t-work
 *
 */
@Entity @Table(name="ieasy_sys_role")
public class RoleEntity extends ExtFieldEntity {

	private String name ;				//角色名称
	
	private String sn ;					//角色序列号
	
	private Integer defaultRole ;		//默认分配角色，1：默认角色
	
	private Clob remark ;				//角色备注
	
	private Set<UserEntity> users = new HashSet<UserEntity>(0);
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ieasy_sys_user_roles", joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}
	public String getName() {
		return name;
	}

	public Integer getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(Integer defaultRole) {
		this.defaultRole = defaultRole;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Clob getRemark() {
		return remark;
	}

	public void setRemark(Clob remark) {
		this.remark = remark;
	}

}
