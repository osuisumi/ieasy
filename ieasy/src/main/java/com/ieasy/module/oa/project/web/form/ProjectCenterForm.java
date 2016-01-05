package com.ieasy.module.oa.project.web.form;

import java.io.Serializable;
import java.util.Date;

import com.ieasy.basic.model.PageHelper;

public class ProjectCenterForm extends PageHelper implements Serializable {

	
	private static final long serialVersionUID = 1L;

	/*********************************************立项及预算*************************************************************/
	private String proj_num ;												//项目编号
	
	private String proj_name ;												//项目名称
	
	private Date proj_start_time ;											//项目计划开始时间
	
	private Date proj_end_time ;											//项目计划结束时间 
	
	private String proj_owner_dept_id ;										//项目所属部门ID
	
	private String proj_owner_dept_name ;									//项目所属部门名称
	
	private String proj_partake_dept_ids ;									//参与部门IDS
	
	private String proj_partake_dept_names ;								//参与部门名称
	
	private String proj_level ;												//项目级别
	
	private String proj_type ;												//项目类型
	
	private String proj_approve_person_ids ;								//项目审批者IDS
	
	private String proj_approve_person_names ;								//项目审批者名称
	
	private String proj_description ;										//项目描述
	
	private String proj_attach_path ;										//项目附件路径
	
	private String proj_bak ;												//项目备注
	
	
	private double proj_budget_amount1 ;									//人员工资
	private double proj_budget_amount2 ;									//差旅费
	private double proj_budget_amount3 ;									//设备消耗
	private double proj_budget_amount4 ;									//项目维护
	private double proj_budget_amount5 ;									//资源采购
	private double proj_budget_amount6 ;									//活动支出
	private double proj_budget_amount7 ;									//差旅费
	private double proj_budget_amount8 ;									//加班费
	private double proj_budget_amount9 ;									//奖金
	
	
	/*********************************************干系人部分*************************************************************/
	private String proj_creator_id ;										//项目创建者ID
	
	private String proj_creator_name ;										//项目创建者名称
	
	private String proj_owner_id ;											//项目负责人ID（拥有者）
	
	private String proj_owner_name ;										//项目负责人名称（拥有者）
	
	private String proj_viewer_member_ids ;									//项目查看人IDS
	
	private String proj_viewer_member_names ;								//项目查看人名称
	
	private String proj_manager_ids ;										//项目经理IDS
	
	private String proj_manager_names ;										//项目经理名称
	
	private String proj_develop_engine_ids ;								//项目开发工程师IDS
	
	private String proj_develop_engine_names ;								//项目开发工程师名称
	
	private String proj_testing_engine_ids ;								//项目测试工程师IDS
	
	private String proj_testing_engine_names ;								//项目测试工程师名称
	
	private String proj_sqa_member_ids ;									//项目品质保证人员IDS
	
	private String proj_sqa_member_names ;									//项目品质保证人员名称
	
	private String proj_operation_ids ;										//指定项目可操作人IDS
	
	private String proj_operation_names ;									//指定项目可操作人名称
	
	
	/*********************************************项目状态部分************************************************************/
	private Integer proj_status ;											//项目状态（0：立项中，1:审批中，2：办理中，3：挂起中，4：已办结）
	
	private String distinguish ;											//类型区分（项目，提案）
	
	/*********************************************稼动率部分*************************************************************/
	private String proj_gjjb ;												//跟进级别
	private String proj_htpjzt ;											//合同评审状态
	private String proj_shouzhu ;											//合同受注状况 
	private String proj_jxzt ;												//结项状态
	private String proj_cwjszt ;											//财务结算状态
	
	private Float proj_quot = new Float(0) ;								//项目系数
	private String proj_zyfw ;												//作业范围
	private String proj_gm ;												//项目规模
	private String proj_buglv ;												//顾客返回BUG率目标
	private String proj_bjzry ;												//报价总人月
	private String proj_yjtrzry ;											//预计投入总人月数
	private String proj_bjscx ;												//报价生产性
	private String proj_clrl ;												//初始粗利润率
	private String proj_cclrl ;												//当前粗利润率
	private String proj_ydscx ;												//预定生产性
	private String proj_cwydwcjssj ;										//财务预定完成结算时间
	private String proj_xmjsnf ;											//项目结算年份
	private String proj_cwwcjssj ;											//财务完成结算时间
	
	
	
	/*********************************************编外属性部分*************************************************************/
	private double proj_const_money ;										//项目总预算资金
	
	private String develops_worktime ;										//【编外属性】开发人员在项目中的作业开始和结束日期
	
	private Date proj_begin_startDate ;										//【编外属性】根据项目开始时间进行范围查询(客户传入的开始时间)
	
	private Date proj_begin_endDate ;										//【编外属性】根据项目开始时间进行范围查询(客户传入的结束时间)
	
	private Date proj_finish_startDate ;									//【编外属性】根据项目结束时间进行范围查询(客户传入的开始时间)
	
	private Date proj_finish_endDate ;										//【编外属性】根据项目结束时间进行范围查询(客户传入的结束时间)
	
	private boolean isSubmitApprove = false ;								//是否显示提交审批按钮
	
	private String approve_id ;												//审批ID
	
	private int approve_status ;											//审批状态
	
	private int cyc ;														//工期，工作天数，排除周六日
	
	private String my_dev_project_personId ;								//条件查询，查询我参与开发的项目，传入用户ID
	
	private Integer dev_work_status ;										//刷新项目定时器用到,开发人员的作业状态
	
	
	public String getProj_operation_ids() {
		return proj_operation_ids;
	}

	public void setProj_operation_ids(String proj_operation_ids) {
		this.proj_operation_ids = proj_operation_ids;
	}

	public String getProj_operation_names() {
		return proj_operation_names;
	}

	public void setProj_operation_names(String proj_operation_names) {
		this.proj_operation_names = proj_operation_names;
	}

	public Integer getDev_work_status() {
		return dev_work_status;
	}

	public void setDev_work_status(Integer dev_work_status) {
		this.dev_work_status = dev_work_status;
	}

	public String getMy_dev_project_personId() {
		return my_dev_project_personId;
	}

	public void setMy_dev_project_personId(String my_dev_project_personId) {
		this.my_dev_project_personId = my_dev_project_personId;
	}

	public String getProj_bak() {
		return proj_bak;
	}

	public void setProj_bak(String proj_bak) {
		this.proj_bak = proj_bak;
	}

	public String getDistinguish() {
		return distinguish;
	}

	public void setDistinguish(String distinguish) {
		this.distinguish = distinguish;
	}

	public int getCyc() {
		return cyc;
	}

	public void setCyc(int cyc) {
		this.cyc = cyc;
	}

	public String getProj_type() {
		return proj_type;
	}

	public void setProj_type(String proj_type) {
		this.proj_type = proj_type;
	}

	public String getApprove_id() {
		return approve_id;
	}

	public void setApprove_id(String approve_id) {
		this.approve_id = approve_id;
	}

	public int getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(int approve_status) {
		this.approve_status = approve_status;
	}

	public boolean isSubmitApprove() {
		return isSubmitApprove;
	}

	public void setSubmitApprove(boolean isSubmitApprove) {
		this.isSubmitApprove = isSubmitApprove;
	}

	public Date getProj_begin_startDate() {
		return proj_begin_startDate;
	}

	public void setProj_begin_startDate(Date proj_begin_startDate) {
		this.proj_begin_startDate = proj_begin_startDate;
	}

	public Date getProj_begin_endDate() {
		return proj_begin_endDate;
	}

	public void setProj_begin_endDate(Date proj_begin_endDate) {
		this.proj_begin_endDate = proj_begin_endDate;
	}

	public Date getProj_finish_startDate() {
		return proj_finish_startDate;
	}

	public void setProj_finish_startDate(Date proj_finish_startDate) {
		this.proj_finish_startDate = proj_finish_startDate;
	}

	public Date getProj_finish_endDate() {
		return proj_finish_endDate;
	}

	public void setProj_finish_endDate(Date proj_finish_endDate) {
		this.proj_finish_endDate = proj_finish_endDate;
	}


	public String getProj_zyfw() {
		return proj_zyfw;
	}

	public void setProj_zyfw(String proj_zyfw) {
		this.proj_zyfw = proj_zyfw;
	}


	public void setProj_budget_amount3(double proj_budget_amount3) {
		this.proj_budget_amount3 = proj_budget_amount3;
	}

	public double getProj_budget_amount4() {
		return proj_budget_amount4;
	}

	public void setProj_budget_amount4(double proj_budget_amount4) {
		this.proj_budget_amount4 = proj_budget_amount4;
	}

	public double getProj_budget_amount5() {
		return proj_budget_amount5;
	}

	public void setProj_budget_amount5(double proj_budget_amount5) {
		this.proj_budget_amount5 = proj_budget_amount5;
	}

	public double getProj_budget_amount6() {
		return proj_budget_amount6;
	}

	public void setProj_budget_amount6(double proj_budget_amount6) {
		this.proj_budget_amount6 = proj_budget_amount6;
	}

	public double getProj_budget_amount7() {
		return proj_budget_amount7;
	}

	public void setProj_budget_amount7(double proj_budget_amount7) {
		this.proj_budget_amount7 = proj_budget_amount7;
	}

	public double getProj_budget_amount8() {
		return proj_budget_amount8;
	}

	public void setProj_budget_amount8(double proj_budget_amount8) {
		this.proj_budget_amount8 = proj_budget_amount8;
	}

	public double getProj_budget_amount9() {
		return proj_budget_amount9;
	}

	public void setProj_budget_amount9(double proj_budget_amount9) {
		this.proj_budget_amount9 = proj_budget_amount9;
	}

	public String getProj_creator_id() {
		return proj_creator_id;
	}

	public void setProj_creator_id(String proj_creator_id) {
		this.proj_creator_id = proj_creator_id;
	}

	public String getProj_creator_name() {
		return proj_creator_name;
	}

	public void setProj_creator_name(String proj_creator_name) {
		this.proj_creator_name = proj_creator_name;
	}

	public String getProj_owner_id() {
		return proj_owner_id;
	}

	public void setProj_owner_id(String proj_owner_id) {
		this.proj_owner_id = proj_owner_id;
	}

	public String getProj_owner_name() {
		return proj_owner_name;
	}

	public void setProj_owner_name(String proj_owner_name) {
		this.proj_owner_name = proj_owner_name;
	}

	public String getProj_viewer_member_ids() {
		return proj_viewer_member_ids;
	}

	public void setProj_viewer_member_ids(String proj_viewer_member_ids) {
		this.proj_viewer_member_ids = proj_viewer_member_ids;
	}

	public String getProj_viewer_member_names() {
		return proj_viewer_member_names;
	}

	public void setProj_viewer_member_names(String proj_viewer_member_names) {
		this.proj_viewer_member_names = proj_viewer_member_names;
	}


	public String getProj_manager_ids() {
		return proj_manager_ids;
	}

	public void setProj_manager_ids(String proj_manager_ids) {
		this.proj_manager_ids = proj_manager_ids;
	}

	public String getProj_manager_names() {
		return proj_manager_names;
	}

	public void setProj_manager_names(String proj_manager_names) {
		this.proj_manager_names = proj_manager_names;
	}

	public String getProj_develop_engine_ids() {
		return proj_develop_engine_ids;
	}

	public void setProj_develop_engine_ids(String proj_develop_engine_ids) {
		this.proj_develop_engine_ids = proj_develop_engine_ids;
	}

	public String getProj_develop_engine_names() {
		return proj_develop_engine_names;
	}

	public void setProj_develop_engine_names(String proj_develop_engine_names) {
		this.proj_develop_engine_names = proj_develop_engine_names;
	}

	public String getProj_testing_engine_ids() {
		return proj_testing_engine_ids;
	}

	public void setProj_testing_engine_ids(String proj_testing_engine_ids) {
		this.proj_testing_engine_ids = proj_testing_engine_ids;
	}

	public String getProj_testing_engine_names() {
		return proj_testing_engine_names;
	}

	public void setProj_testing_engine_names(String proj_testing_engine_names) {
		this.proj_testing_engine_names = proj_testing_engine_names;
	}

	public String getProj_sqa_member_ids() {
		return proj_sqa_member_ids;
	}

	public String getProj_num() {
		return proj_num;
	}

	public void setProj_num(String proj_num) {
		this.proj_num = proj_num;
	}

	public String getProj_name() {
		return proj_name;
	}

	public void setProj_name(String proj_name) {
		this.proj_name = proj_name;
	}

	public Date getProj_start_time() {
		return proj_start_time;
	}

	public void setProj_start_time(Date proj_start_time) {
		this.proj_start_time = proj_start_time;
	}

	public Date getProj_end_time() {
		return proj_end_time;
	}

	public void setProj_end_time(Date proj_end_time) {
		this.proj_end_time = proj_end_time;
	}

	public String getProj_owner_dept_id() {
		return proj_owner_dept_id;
	}

	public void setProj_owner_dept_id(String proj_owner_dept_id) {
		this.proj_owner_dept_id = proj_owner_dept_id;
	}

	public String getProj_owner_dept_name() {
		return proj_owner_dept_name;
	}

	public void setProj_owner_dept_name(String proj_owner_dept_name) {
		this.proj_owner_dept_name = proj_owner_dept_name;
	}

	public String getProj_partake_dept_ids() {
		return proj_partake_dept_ids;
	}

	public void setProj_partake_dept_ids(String proj_partake_dept_ids) {
		this.proj_partake_dept_ids = proj_partake_dept_ids;
	}

	public String getProj_partake_dept_names() {
		return proj_partake_dept_names;
	}

	public void setProj_partake_dept_names(String proj_partake_dept_names) {
		this.proj_partake_dept_names = proj_partake_dept_names;
	}

	public String getProj_level() {
		return proj_level;
	}

	public void setProj_level(String proj_level) {
		this.proj_level = proj_level;
	}

	public String getProj_approve_person_ids() {
		return proj_approve_person_ids;
	}

	public void setProj_approve_person_ids(String proj_approve_person_ids) {
		this.proj_approve_person_ids = proj_approve_person_ids;
	}

	public String getProj_approve_person_names() {
		return proj_approve_person_names;
	}

	public void setProj_approve_person_names(String proj_approve_person_names) {
		this.proj_approve_person_names = proj_approve_person_names;
	}

	public String getProj_description() {
		return proj_description;
	}

	public void setProj_description(String proj_description) {
		this.proj_description = proj_description;
	}

	public String getProj_attach_path() {
		return proj_attach_path;
	}

	public void setProj_attach_path(String proj_attach_path) {
		this.proj_attach_path = proj_attach_path;
	}

	public double getProj_budget_amount1() {
		return proj_budget_amount1;
	}

	public void setProj_budget_amount1(double proj_budget_amount1) {
		this.proj_budget_amount1 = proj_budget_amount1;
	}

	public double getProj_budget_amount2() {
		return proj_budget_amount2;
	}

	public void setProj_budget_amount2(double proj_budget_amount2) {
		this.proj_budget_amount2 = proj_budget_amount2;
	}

	public Float getProj_quot() {
		return proj_quot;
	}

	public void setProj_quot(Float proj_quot) {
		this.proj_quot = proj_quot;
	}

	public String getProj_shouzhu() {
		return proj_shouzhu;
	}

	public void setProj_shouzhu(String proj_shouzhu) {
		this.proj_shouzhu = proj_shouzhu;
	}

	public String getProj_gm() {
		return proj_gm;
	}

	public void setProj_gm(String proj_gm) {
		this.proj_gm = proj_gm;
	}

	public String getProj_buglv() {
		return proj_buglv;
	}

	public void setProj_buglv(String proj_buglv) {
		this.proj_buglv = proj_buglv;
	}

	public String getProj_bjzry() {
		return proj_bjzry;
	}

	public void setProj_bjzry(String proj_bjzry) {
		this.proj_bjzry = proj_bjzry;
	}

	public String getProj_yjtrzry() {
		return proj_yjtrzry;
	}

	public void setProj_yjtrzry(String proj_yjtrzry) {
		this.proj_yjtrzry = proj_yjtrzry;
	}

	public String getProj_bjscx() {
		return proj_bjscx;
	}

	public void setProj_bjscx(String proj_bjscx) {
		this.proj_bjscx = proj_bjscx;
	}

	public String getProj_clrl() {
		return proj_clrl;
	}

	public void setProj_clrl(String proj_clrl) {
		this.proj_clrl = proj_clrl;
	}

	public String getProj_cclrl() {
		return proj_cclrl;
	}

	public void setProj_cclrl(String proj_cclrl) {
		this.proj_cclrl = proj_cclrl;
	}

	public String getProj_ydscx() {
		return proj_ydscx;
	}

	public void setProj_ydscx(String proj_ydscx) {
		this.proj_ydscx = proj_ydscx;
	}

	public String getProj_htpjzt() {
		return proj_htpjzt;
	}

	public void setProj_htpjzt(String proj_htpjzt) {
		this.proj_htpjzt = proj_htpjzt;
	}

	public String getProj_cwjszt() {
		return proj_cwjszt;
	}

	public void setProj_cwjszt(String proj_cwjszt) {
		this.proj_cwjszt = proj_cwjszt;
	}

	public String getProj_cwydwcjssj() {
		return proj_cwydwcjssj;
	}

	public void setProj_cwydwcjssj(String proj_cwydwcjssj) {
		this.proj_cwydwcjssj = proj_cwydwcjssj;
	}

	public String getProj_gjjb() {
		return proj_gjjb;
	}

	public void setProj_gjjb(String proj_gjjb) {
		this.proj_gjjb = proj_gjjb;
	}

	public String getProj_xmjsnf() {
		return proj_xmjsnf;
	}

	public void setProj_xmjsnf(String proj_xmjsnf) {
		this.proj_xmjsnf = proj_xmjsnf;
	}

	public String getProj_cwwcjssj() {
		return proj_cwwcjssj;
	}

	public void setProj_cwwcjssj(String proj_cwwcjssj) {
		this.proj_cwwcjssj = proj_cwwcjssj;
	}

	public String getProj_jxzt() {
		return proj_jxzt;
	}

	public void setProj_jxzt(String proj_jxzt) {
		this.proj_jxzt = proj_jxzt;
	}

	public double getProj_const_money() {
		return proj_const_money;
	}

	public void setProj_const_money(double proj_const_money) {
		this.proj_const_money = proj_const_money;
	}

	public String getDevelops_worktime() {
		return develops_worktime;
	}

	public void setDevelops_worktime(String develops_worktime) {
		this.develops_worktime = develops_worktime;
	}

	public double getProj_budget_amount3() {
		return proj_budget_amount3;
	}

	public void setProj_sqa_member_ids(String proj_sqa_member_ids) {
		this.proj_sqa_member_ids = proj_sqa_member_ids;
	}

	public String getProj_sqa_member_names() {
		return proj_sqa_member_names;
	}

	public void setProj_sqa_member_names(String proj_sqa_member_names) {
		this.proj_sqa_member_names = proj_sqa_member_names;
	}

	public Integer getProj_status() {
		return proj_status;
	}

	public void setProj_status(Integer proj_status) {
		this.proj_status = proj_status;
	}

}
