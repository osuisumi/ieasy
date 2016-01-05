package com.ieasy.module.oa.project.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ieasy.basic.dao.ExtFieldEntity;

/**
 * 项目中心
 * @author Administrator
 *
 */
@Entity @Table(name="ieasy_oa_project_center")
public class ProjectCenter extends ExtFieldEntity {

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
	
	private String proj_description ;										//项目描述
	
	private String proj_attach_path ;										//项目附件路径
	
	private String proj_bak ;												//项目备注
	
	
	private Double proj_budget_amount1 ;									//人员工资
	private Double proj_budget_amount2 ;									//差旅费
	private Double proj_budget_amount3 ;									//设备消耗
	private Double proj_budget_amount4 ;									//项目维护
	private Double proj_budget_amount5 ;									//资源采购
	private Double proj_budget_amount6 ;									//活动支出
	private Double proj_budget_amount7 ;									//差旅费
	private Double proj_budget_amount8 ;									//加班费
	private Double proj_budget_amount9 ;									//奖金
	
	
	
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
	
	
	/*********************************************项目状态部分***********************************************************/
	private Integer proj_status ;											//项目状态（0：立项中，1:审批中，2：办理中，3：挂起中，4：已办结）
	
	private String distinguish ;											//类型区分（项目，提案）
	
	/*********************************************稼动率部门*************************************************************/
	private Float proj_quot = new Float(0) ;								//项目系数
	private String proj_shouzhu ;											//合同受注状况 
	private String proj_zyfw ;												//作业范围
	private String proj_gm ;												//项目规模
	private String proj_buglv ;												//顾客返回BUG率目标
	private String proj_bjzry ;												//报价总人月
	private String proj_yjtrzry ;											//预计投入总人月数
	private String proj_bjscx ;												//报价生产性
	private String proj_clrl ;												//初始粗利润率
	private String proj_cclrl ;												//当前粗利润率
	private String proj_ydscx ;												//预定生产性
	private String proj_htpjzt ;											//合同评审状态
	private String proj_cwjszt ;											//财务结算状态
	private String proj_cwydwcjssj ;										//财务预定完成结算时间
	private String proj_gjjb ;												//跟进级别
	private String proj_xmjsnf ;											//项目结算年份
	private String proj_cwwcjssj ;											//财务完成结算时间
	private String proj_jxzt ;												//结项状态

	public ProjectCenter() {}
	@Lob @Column(length=2000)
	public String getProj_operation_ids() {
		return proj_operation_ids;
	}

	public void setProj_operation_ids(String proj_operation_ids) {
		this.proj_operation_ids = proj_operation_ids;
	}

	@Lob @Column(length=2000)
	public String getProj_operation_names() {
		return proj_operation_names;
	}

	public void setProj_operation_names(String proj_operation_names) {
		this.proj_operation_names = proj_operation_names;
	}

	@Lob @Column(length=2000)
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

	public ProjectCenter(String id) {
		this.id = id ;
	}
	
	public String getProj_type() {
		return proj_type;
	}

	public void setProj_type(String proj_type) {
		this.proj_type = proj_type;
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

	public String getProj_num() {
		return proj_num;
	}

	public Float getProj_quot() {
		return proj_quot;
	}

	public void setProj_quot(Float proj_quot) {
		this.proj_quot = proj_quot;
	}


	public String getProj_zyfw() {
		return proj_zyfw;
	}

	public void setProj_zyfw(String proj_zyfw) {
		this.proj_zyfw = proj_zyfw;
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

	@Temporal(TemporalType.DATE)
	public Date getProj_start_time() {
		return proj_start_time;
	}

	public void setProj_start_time(Date proj_start_time) {
		this.proj_start_time = proj_start_time;
	}

	@Temporal(TemporalType.DATE)
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

	@Lob @Column(length=16777216)
	public String getProj_partake_dept_ids() {
		return proj_partake_dept_ids;
	}

	public void setProj_partake_dept_ids(String proj_partake_dept_ids) {
		this.proj_partake_dept_ids = proj_partake_dept_ids;
	}

	@Lob @Column(length=16777216)
	public String getProj_level() {
		return proj_level;
	}

	public String getProj_partake_dept_names() {
		return proj_partake_dept_names;
	}

	public void setProj_partake_dept_names(String proj_partake_dept_names) {
		this.proj_partake_dept_names = proj_partake_dept_names;
	}

	public void setProj_level(String proj_level) {
		this.proj_level = proj_level;
	}

	public String getProj_attach_path() {
		return proj_attach_path;
	}

	public void setProj_attach_path(String proj_attach_path) {
		this.proj_attach_path = proj_attach_path;
	}


	@Lob @Column(length=16777216)
	public String getProj_description() {
		return proj_description;
	}

	public void setProj_description(String proj_description) {
		this.proj_description = proj_description;
	}

	public Double getProj_budget_amount1() {
		return proj_budget_amount1;
	}

	public void setProj_budget_amount1(Double proj_budget_amount1) {
		this.proj_budget_amount1 = proj_budget_amount1;
	}

	public Double getProj_budget_amount2() {
		return proj_budget_amount2;
	}

	public void setProj_budget_amount2(Double proj_budget_amount2) {
		this.proj_budget_amount2 = proj_budget_amount2;
	}

	public Double getProj_budget_amount3() {
		return proj_budget_amount3;
	}

	public void setProj_budget_amount3(Double proj_budget_amount3) {
		this.proj_budget_amount3 = proj_budget_amount3;
	}

	public Double getProj_budget_amount4() {
		return proj_budget_amount4;
	}

	public void setProj_budget_amount4(Double proj_budget_amount4) {
		this.proj_budget_amount4 = proj_budget_amount4;
	}

	public Double getProj_budget_amount5() {
		return proj_budget_amount5;
	}

	public void setProj_budget_amount5(Double proj_budget_amount5) {
		this.proj_budget_amount5 = proj_budget_amount5;
	}

	public Double getProj_budget_amount6() {
		return proj_budget_amount6;
	}

	public void setProj_budget_amount6(Double proj_budget_amount6) {
		this.proj_budget_amount6 = proj_budget_amount6;
	}

	public Double getProj_budget_amount7() {
		return proj_budget_amount7;
	}

	public void setProj_budget_amount7(Double proj_budget_amount7) {
		this.proj_budget_amount7 = proj_budget_amount7;
	}

	public Double getProj_budget_amount8() {
		return proj_budget_amount8;
	}

	public void setProj_budget_amount8(Double proj_budget_amount8) {
		this.proj_budget_amount8 = proj_budget_amount8;
	}

	public Double getProj_budget_amount9() {
		return proj_budget_amount9;
	}

	public void setProj_budget_amount9(Double proj_budget_amount9) {
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

	@Lob @Column(length=16777216)
	public String getProj_viewer_member_ids() {
		return proj_viewer_member_ids;
	}
	
	public void setProj_viewer_member_ids(String proj_viewer_member_ids) {
		this.proj_viewer_member_ids = proj_viewer_member_ids;
	}

	@Lob @Column(length=16777216)
	public String getProj_viewer_member_names() {
		return proj_viewer_member_names;
	}

	public void setProj_viewer_member_names(String proj_viewer_member_names) {
		this.proj_viewer_member_names = proj_viewer_member_names;
	}

	@Lob @Column(length=16777216)
	public String getProj_manager_ids() {
		return proj_manager_ids;
	}

	public void setProj_manager_ids(String proj_manager_ids) {
		this.proj_manager_ids = proj_manager_ids;
	}

	@Lob @Column(length=16777216)
	public String getProj_manager_names() {
		return proj_manager_names;
	}

	public void setProj_manager_names(String proj_manager_names) {
		this.proj_manager_names = proj_manager_names;
	}

	@Lob @Column(length=16777216)
	public String getProj_develop_engine_ids() {
		return proj_develop_engine_ids;
	}

	public void setProj_develop_engine_ids(String proj_develop_engine_ids) {
		this.proj_develop_engine_ids = proj_develop_engine_ids;
	}

	@Lob @Column(length=16777216)
	public String getProj_develop_engine_names() {
		return proj_develop_engine_names;
	}

	public void setProj_develop_engine_names(String proj_develop_engine_names) {
		this.proj_develop_engine_names = proj_develop_engine_names;
	}

	@Lob @Column(length=16777216)
	public String getProj_testing_engine_ids() {
		return proj_testing_engine_ids;
	}

	public void setProj_testing_engine_ids(String proj_testing_engine_ids) {
		this.proj_testing_engine_ids = proj_testing_engine_ids;
	}

	@Lob @Column(length=16777216)
	public String getProj_testing_engine_names() {
		return proj_testing_engine_names;
	}

	public void setProj_testing_engine_names(String proj_testing_engine_names) {
		this.proj_testing_engine_names = proj_testing_engine_names;
	}

	@Lob @Column(length=16777216)
	public String getProj_sqa_member_ids() {
		return proj_sqa_member_ids;
	}

	public void setProj_sqa_member_ids(String proj_sqa_member_ids) {
		this.proj_sqa_member_ids = proj_sqa_member_ids;
	}

	@Lob @Column(length=16777216)
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
