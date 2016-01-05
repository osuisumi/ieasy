package com.ieasy.module.system.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

@Entity @Table(name="ieasy_sys_org")
public class OrgEntity extends ExtFieldEntity {

	private String name ;							//组织机构名称
	
	private Integer sort = new Integer(1);			//组织机构的排序号
	
	private String type ;							//组织机构的类型（机构：O、部门：D）
	
	private String sn ;								//组织机构序列号
	
	private String iconCls ;						//EasyUI tree的图标
	
	private String state ;							//EasyUI tree的展开或隐藏(open、closed)
	
	private Boolean sumJdl ;						//是否计算稼动率

	private Set<OrgEntity> orgs = new HashSet<OrgEntity>() ;
	
	private OrgEntity org ;
	
	private Set<PersonEntity> persons = new HashSet<PersonEntity>(0) ;
	
	@OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
	public Set<PersonEntity> getPersons() {
		return persons;
	}

	public void setPersons(Set<PersonEntity> persons) {
		this.persons = persons;
	}

	@OneToMany(mappedBy = "org", fetch = FetchType.LAZY)
	@OrderBy("sort asc")
	public Set<OrgEntity> getOrgs() {
		return orgs;
	}

	public void setOrgs(Set<OrgEntity> orgs) {
		this.orgs = orgs;
	}

	@ManyToOne
	@JoinColumn(name = "pid")
	public OrgEntity getOrg() {
		return org;
	}

	public void setOrg(OrgEntity org) {
		this.org = org;
	}

	public Boolean getSumJdl() {
		return sumJdl;
	}

	public void setSumJdl(Boolean sumJdl) {
		this.sumJdl = sumJdl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
