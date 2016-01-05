package com.ieasy.module.common.st;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ieasy.basic.dao.ExtFieldEntity;

@Entity @Table(name="ieasy_sys_st")
public class ScheduleTaskEntity extends ExtFieldEntity {
	
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
	private int run_count ;
	
	/** 是否启动 */
	private String task_enable ;
	
	/** 任务备注 */
	private String task_remark ;

	
	public int getRun_count() {
		return run_count;
	}

	public void setRun_count(int run_count) {
		this.run_count = run_count;
	}

	public Date getTask_datetime() {
		return task_datetime;
	}

	public void setTask_datetime(Date task_datetime) {
		this.task_datetime = task_datetime;
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
		this.task_name = task_name;
	}

	public String getTask_job_class() {
		return task_job_class;
	}

	public void setTask_job_class(String task_job_class) {
		this.task_job_class = task_job_class;
	}

	public String getCron_expression() {
		return cron_expression;
	}

	public void setCron_expression(String cron_expression) {
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
}
