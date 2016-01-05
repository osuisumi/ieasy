package com.ieasy.module.common.st;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 关联操作
 * 定时作业执行时可能需要对某个业务操作，需要查询某个业务的ID等
 * @author Administrator
 *
 */
@Entity @Table(name="ieasy_sys_st_link")
public class ScheduleLinkOperEntity extends ExtFieldEntity {

	private String link1 ;
	private String link2 ;
	private String link3 ;
	private String link4 ;
	private String link5 ;
	private String link6 ;
	private String link7 ;
	private String link8 ;
	private String link9 ;
	
	private ScheduleTaskEntity schedule ;
	
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="st_id", unique=true)
	public ScheduleTaskEntity getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleTaskEntity schedule) {
		this.schedule = schedule;
	}
	public String getLink1() {
		return link1;
	}
	public void setLink1(String link1) {
		this.link1 = link1;
	}
	public String getLink2() {
		return link2;
	}
	public void setLink2(String link2) {
		this.link2 = link2;
	}
	public String getLink3() {
		return link3;
	}
	public void setLink3(String link3) {
		this.link3 = link3;
	}
	public String getLink4() {
		return link4;
	}
	public void setLink4(String link4) {
		this.link4 = link4;
	}
	public String getLink5() {
		return link5;
	}
	public void setLink5(String link5) {
		this.link5 = link5;
	}
	public String getLink6() {
		return link6;
	}
	public void setLink6(String link6) {
		this.link6 = link6;
	}
	public String getLink7() {
		return link7;
	}
	public void setLink7(String link7) {
		this.link7 = link7;
	}
	public String getLink8() {
		return link8;
	}
	public void setLink8(String link8) {
		this.link8 = link8;
	}
	public String getLink9() {
		return link9;
	}
	public void setLink9(String link9) {
		this.link9 = link9;
	}
}
