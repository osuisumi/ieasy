package com.ieasy.module.system.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ieasy_sys_global")
public class GlobalEntity {
	
	/**
	 * 0：是、1：否
	 */

	private String id ;										//唯一编号
	
	//系统安全设置
	private Integer pass_length1 = new Integer(8) ;			//密码长度-开始
	
	private Integer pass_length2 = new Integer(20) ;		//密码长度-结束
	
	private Integer retry_ban = new Integer(0) ;			//登录错误次数限制
	
	private Integer retry_times = new Integer(3) ;			//登录错误重试3次后 10 分钟内禁止再次登录
	
	private Integer ban_time = new Integer(10) ;			//分钟内禁止再次登录
	
	private Integer retrieve_mail_pwd = new Integer(0) ;	//是否开启密码找回
	
	private Integer only_user_login = new Integer(0) ;		//是否允许多人用同一帐号同时登录
	
	
	
	//OA服务设置
	private Integer mail_host = new Integer(0) ;			//邮件服务IP地址
	
	private Integer mail_port = new Integer(0) ;			//邮件服务端口
	
	private Integer mail_recv = new Integer(0) ;			//邮件收取间隔
	
	private Integer mail_pop_port = new Integer(0) ;		//POP3端口
	
	private Integer daemon_timer = new Integer(0) ;			//监控服务监控其他服务运行状态的间隔时间
	
	private Integer default_attach_path = new Integer(0) ;	//默认附件路径
	
	
	
	
	//IP访问规则
	private String start_ip ;								//起始IP
	
	private String end_ip ;									//结束IP
	
	private String ip_rule_remark ;							//IP访问规则备注
	
	private String forbidden_ip ;							//禁止登录IP
	
	private Date startTime ;								//禁止登录时段（开始时间）
	
	private Date endTime ;									//禁止登录时段（结束时间）
	
	private Date startDate ;								//禁止登录时段（开始日期）
	
	private Date endDate ;									//禁止登录时段（结束日期）
	
	private Date startTimeSite ;							//禁止访问本站时段（开始时间）
	
	private Date endTimeSite ;								//禁止访问本站时段（结束时间）
	
	private Date startDateSite ;							//禁止访问本站时段（开始日期）
	
	private Date endDateSite ;								//禁止访问本站时段（结束日期）
	
	private Date created ;									//创建日期
	
	private String createName ;								//创建者

	private Date modifyDate ;								//修改日期
	
	private String modifyName ;								//修改者名称
	
	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Temporal(TemporalType.TIME)
	public Date getStartTimeSite() {
		return startTimeSite;
	}

	public void setStartTimeSite(Date startTimeSite) {
		this.startTimeSite = startTimeSite;
	}

	@Temporal(TemporalType.TIME)
	public Date getEndTimeSite() {
		return endTimeSite;
	}

	public void setEndTimeSite(Date endTimeSite) {
		this.endTimeSite = endTimeSite;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDateSite() {
		return startDateSite;
	}

	public void setStartDateSite(Date startDateSite) {
		this.startDateSite = startDateSite;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDateSite() {
		return endDateSite;
	}

	public void setEndDateSite(Date endDateSite) {
		this.endDateSite = endDateSite;
	}

	@Temporal(TemporalType.TIME)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIME)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPass_length1() {
		return pass_length1;
	}

	public void setPass_length1(Integer pass_length1) {
		this.pass_length1 = pass_length1;
	}

	public Integer getPass_length2() {
		return pass_length2;
	}

	public void setPass_length2(Integer pass_length2) {
		this.pass_length2 = pass_length2;
	}

	public Integer getRetry_ban() {
		return retry_ban;
	}

	public void setRetry_ban(Integer retry_ban) {
		this.retry_ban = retry_ban;
	}

	public Integer getRetry_times() {
		return retry_times;
	}

	public void setRetry_times(Integer retry_times) {
		this.retry_times = retry_times;
	}

	public Integer getBan_time() {
		return ban_time;
	}

	public void setBan_time(Integer ban_time) {
		this.ban_time = ban_time;
	}

	public Integer getRetrieve_mail_pwd() {
		return retrieve_mail_pwd;
	}

	public void setRetrieve_mail_pwd(Integer retrieve_mail_pwd) {
		this.retrieve_mail_pwd = retrieve_mail_pwd;
	}

	public Integer getOnly_user_login() {
		return only_user_login;
	}

	public void setOnly_user_login(Integer only_user_login) {
		this.only_user_login = only_user_login;
	}

	public Integer getMail_host() {
		return mail_host;
	}

	public void setMail_host(Integer mail_host) {
		this.mail_host = mail_host;
	}

	public Integer getMail_port() {
		return mail_port;
	}

	public void setMail_port(Integer mail_port) {
		this.mail_port = mail_port;
	}

	public Integer getMail_recv() {
		return mail_recv;
	}

	public void setMail_recv(Integer mail_recv) {
		this.mail_recv = mail_recv;
	}

	public Integer getMail_pop_port() {
		return mail_pop_port;
	}

	public void setMail_pop_port(Integer mail_pop_port) {
		this.mail_pop_port = mail_pop_port;
	}

	public Integer getDaemon_timer() {
		return daemon_timer;
	}

	public void setDaemon_timer(Integer daemon_timer) {
		this.daemon_timer = daemon_timer;
	}

	public Integer getDefault_attach_path() {
		return default_attach_path;
	}

	public void setDefault_attach_path(Integer default_attach_path) {
		this.default_attach_path = default_attach_path;
	}

	public String getStart_ip() {
		return start_ip;
	}

	public void setStart_ip(String start_ip) {
		this.start_ip = start_ip;
	}

	public String getEnd_ip() {
		return end_ip;
	}

	public void setEnd_ip(String end_ip) {
		this.end_ip = end_ip;
	}

	public String getIp_rule_remark() {
		return ip_rule_remark;
	}

	public void setIp_rule_remark(String ip_rule_remark) {
		this.ip_rule_remark = ip_rule_remark;
	}

	public String getForbidden_ip() {
		return forbidden_ip;
	}

	public void setForbidden_ip(String forbidden_ip) {
		this.forbidden_ip = forbidden_ip;
	}

	
}
