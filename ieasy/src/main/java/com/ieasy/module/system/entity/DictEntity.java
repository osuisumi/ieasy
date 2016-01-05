package com.ieasy.module.system.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 数据字典
 * @author ibm-work
 *
 */
@Entity @Table(name="ieasy_sys_dict")
public class DictEntity extends ExtFieldEntity {

	private String dictName ;					//字典名称
	
	private Integer dictIndex ;						//索引编号
	
	private String dictCode ;					//字典编码
	
	private Integer sort ;						//排序序号
	
	private String type ;						//字典类型（SZX：分类，LX：类型，LB：列表）
	
	private String selected ;					//默认选中(0：不选中，1：选中)
	
	private String remark ;						//备注
	
	private String state = "open" ;
	
	
	private Set<DictEntity> dicts= new HashSet<DictEntity>() ;
	
	private DictEntity dict ;

	public Integer getDictIndex() {
		return dictIndex;
	}

	public void setDictIndex(Integer dictIndex) {
		this.dictIndex = dictIndex;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}
	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(mappedBy="dict")
	public Set<DictEntity> getDicts() {
		return dicts;
	}

	public void setDicts(Set<DictEntity> dicts) {
		this.dicts = dicts;
	}

	@ManyToOne
	@JoinColumn(name="pid")
	public DictEntity getDict() {
		return dict;
	}

	public void setDict(DictEntity dict) {
		this.dict = dict;
	}
	
	
}
