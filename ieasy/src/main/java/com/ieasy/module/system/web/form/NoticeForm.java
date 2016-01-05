package com.ieasy.module.system.web.form;

import java.io.Serializable;

import com.ieasy.basic.model.PageHelper;

public class NoticeForm extends PageHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title ;									//标题
	
	private String content ;								//内容
	
	private Integer approve ;								//审批，-1不通过，1通过

	private String type ;									//类型
	
	private String inTypes ;								//查询包含的类型

	public String getInTypes() {
		return inTypes;
	}

	public void setInTypes(String inTypes) {
		this.inTypes = inTypes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getApprove() {
		return approve;
	}

	public void setApprove(Integer approve) {
		this.approve = approve;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
