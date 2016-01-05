package com.ieasy.basic.dao;

import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ExtFieldEntity extends IdEntity {
	
	private String createName ;				//创建者名称
	
	private Date created ;					//创建日期
	
	private String modifyName ;				//修改者名称
	
	private Date modifyDate ;				//修改日期
	
	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	
}
