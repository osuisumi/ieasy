package com.ieasy.module.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 系统公告
 * @author Administrator
 *
 */
@Entity
@Table(name = "ieasy_sys_notice")
public class NoticeEntity extends ExtFieldEntity {

	private String title ;								//标题
	
	private String content ;							//内容
	
	private Integer approve = new Integer(-1) ;			//审批，-1不通过，1通过

	private String type ;								//类型
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Lob @Column(length=16777216)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
