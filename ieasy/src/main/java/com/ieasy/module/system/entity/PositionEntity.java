package com.ieasy.module.system.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 岗位实体
 * @author t-work
 *
 */
@Entity @Table(name="ieasy_sys_position")
public class PositionEntity extends ExtFieldEntity {

	private String name ;			//岗位名称
	
	private String sn ;				//岗位序号
	
	private Integer sort ;			//岗位排序号
	
	private Set<PositionEntity> positions = new HashSet<PositionEntity>(0);

	private PositionEntity position ;
	
	private Set<PersonEntity> persons = new HashSet<PersonEntity>(0);
	
	@OneToMany(mappedBy="positions", fetch = FetchType.LAZY)
	public Set<PersonEntity> getPersons() {
		return persons;
	}

	public void setPersons(Set<PersonEntity> persons) {
		this.persons = persons;
	}
	
	@OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
	public Set<PositionEntity> getPositions() {
		return positions;
	}

	public void setPositions(Set<PositionEntity> positions) {
		this.positions = positions;
	}

	@ManyToOne
	@JoinColumn(name = "pid")
	public PositionEntity getPosition() {
		return position;
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

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}
	
}
