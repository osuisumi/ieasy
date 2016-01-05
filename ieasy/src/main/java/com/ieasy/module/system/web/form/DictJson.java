package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.EasyuiTree;

public class DictJson extends EasyuiTree<DictJson> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dictCode ;				//字典编码
	
	private Integer dictIndex ;				//字典索引
	
	private String text ;					//字典名称
	
	private String selected ;				//默认选中

	public Integer getDictIndex() {
		return dictIndex;
	}

	public void setDictIndex(Integer dictIndex) {
		this.dictIndex = dictIndex;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	

}
