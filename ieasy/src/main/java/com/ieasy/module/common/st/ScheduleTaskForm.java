package com.ieasy.module.common.st;

import java.util.Date;

import com.ieasy.basic.model.PageHelper;

public class ScheduleTaskForm extends PageHelper {
	
	private String id ;
	
	/** 任务代码 */
	private String task_code ;
	
	/** 任务名称 */
	private String task_name ;
	
	/** Job类路径 */
	private String task_job_class ;
	
	/** 作业时间Cron表达式 */
	private String cron_expression ;
	
	/** 作业时间（2014-11-09 12:33:20）由crom表达式翻译过来 */
	private Date task_datetime ;
	
	/** 运行次数 */
	private Integer run_count ;
	
	/** 是否启动 */
	private String task_enable ;
	
	/** 任务备注 */
	private String task_remark ;
	
	private String link1 ;
	private String link2 ;
	private String link3 ;
	private String link4 ;
	private String link5 ;
	private String link6 ;
	private String link7 ;
	private String link8 ;
	private String link9 ;
	
	private String st_id; 
	
	private Date task_datetime_start ;
	
	private Date task_datetime_end ;

	public Date getTask_datetime_start() {
		return task_datetime_start;
	}

	public void setTask_datetime_start(Date task_datetime_start) {
		this.task_datetime_start = task_datetime_start;
	}

	public Date getTask_datetime_end() {
		return task_datetime_end;
	}

	public void setTask_datetime_end(Date task_datetime_end) {
		this.task_datetime_end = task_datetime_end;
	}

	public Integer getRun_count() {
		return run_count;
	}

	public void setRun_count(Integer run_count) {
		this.run_count = run_count;
	}

	public Date getTask_datetime() {
		return task_datetime;
	}

	public void setTask_datetime(Date task_datetime) {
		this.task_datetime = task_datetime;
	}

	public String getSt_id() {
		return st_id;
	}

	public void setSt_id(String st_id) {
		this.st_id = st_id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_code() {
		return task_code;
	}

	public void setTask_code(String task_code) {
		this.task_code = task_code;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		if(null!=task_name && !"".equals(task_name)) {
			task_name = task_name.trim() ;
		}
		this.task_name = task_name;
	}

	public String getTask_job_class() {
		return task_job_class;
	}

	public void setTask_job_class(String task_job_class) {
		if(null!=task_job_class && !"".equals(task_job_class)) {
			task_job_class = task_job_class.trim() ;
		}
		this.task_job_class = task_job_class;
	}

	public String getCron_expression() {
		return cron_expression;
	}

	public void setCron_expression(String cron_expression) {
		if(null!=cron_expression && !"".equals(cron_expression)) {
			cron_expression = cron_expression.trim() ;
		}
		this.cron_expression = cron_expression;
	}

	public String getTask_enable() {
		return task_enable;
	}

	public void setTask_enable(String task_enable) {
		this.task_enable = task_enable;
	}

	public String getTask_remark() {
		return task_remark;
	}

	public void setTask_remark(String task_remark) {
		this.task_remark = task_remark;
	}

	@Override
	public String toString() {
		return "ScheduleTaskForm [id=" + id + ", task_code=" + task_code + ", task_name=" + task_name + ", task_job_class=" + task_job_class + ", cron_expression=" + cron_expression + ", task_enable=" + task_enable + ", task_remark=" + task_remark + "]";
	}
	
	
}
