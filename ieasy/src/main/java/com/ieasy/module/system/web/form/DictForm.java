package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.EasyuiTree;

public class DictForm extends EasyuiTree<DictForm> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dictName ;					//字典名称
	
	private Integer dictIndex ;					//索引编号
	
	private String dictCode ;					//字典编码
	
	private Integer sort ;						//排序序号
	
	private String type ;						//字典类型（SZX：分类，LX：类型，LB：列表）
	
	private String selected ;					//默认选中(0：不选中，1：选中)
	
	private String remark ;						//备注

	public String getDictName() {
		return dictName;
	}


	public Integer getDictIndex() {
		return dictIndex;
	}


	public void setDictIndex(Integer dictIndex) {
		this.dictIndex = dictIndex;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
