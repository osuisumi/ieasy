package com.ieasy.module.system.web.form;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;

import com.alibaba.fastjson.annotation.JSONField;
import com.ieasy.basic.model.PageHelper;

public class PersonForm extends PageHelper implements Serializable {
	

	private static final long serialVersionUID = 1L;

	/**  基本信息   */
	private String name ;											//姓名
	
	private String num ;											//人员编号
	
	private String sex ;											//性别
	
	private Date birth ;											//出生日期
	
	private String zjlx ;											//证件类型
	
	private String zjhm ;											//证件号码
	
	private String salNum ;											//工资卡号
	
	private String ssNum ;											//社保卡号
	
	private String place ;											//籍贯
	
	private String stirps ;											//种族
	
	private String country ;										//国籍
	
	private String marriage ;										//婚姻状况
	
	private String political  ;										//政治面貌
	
	private String health  ;										//健康状况
	
	private String highest  ;										//最高学位
	
	private String profession  ;									//专业
	
	private String posTitle  ;										//职称
	
	private Date bysj;												//毕业时间
	
	private String school  ;										//毕业院校
	
	private String cjgzrq  ;										//参加工资日期
	
	private String kxgl  ;											//空闲工龄
	
	private String height  ;										//身高
	
	private String weight  ;										//体重
	
	private String hkxz  ;											//户口性质
	
	private String hkdz  ;											//户口地址
	
	
	/**************************************职位信息*****************************************************/
	private String empState ;										//人员状态（1：在职，2：离职，3：退休）
	
	private Date dimissionDate ;									//离职日期
	
	private String empType ;										//人员类型
	
	private String empLevel ;										//职称等级
	
	private Date enterDate ;										//入职时间
	
	private Date changeDate ;										//转正日期
	
	private String positionRecord ;									//职位变更记录
	
	/**************************************联系方式*****************************************************/
	private String mobile ;											//手机号码
	
	private String phone ;											//住宅电话
	
	private String email ;											//邮箱地址
	
	private String address ;										//联系地址
	
	private String zipCode ;										//邮编
	
	
	/**************************************其他属性*****************************************************/
	private String ryjb ;											//日语级别
	
	private String remark ;											//备注
	
	private String txPicPath ;										//人员头像路径
	
	private Integer archivesStatus = new Integer(0) ;				//档案状态(0：未建档，1：已建档)
	
	
	
	/**************************************稼动率或项目相关属性*****************************************************/
	private String dbmType ;										//到部门类型(1:在职,2:转入,3:新增,4:试用,5:停薪留职返回,6:返聘)
	
	private String lbmType ;										//离部门日期(1:转出-到开发部,2:转出-到非开发部,3:离职,4:停薪留职)
	
	private Date dbmDate ;											//到部门日期
	
	private Date lbmDate ;											//离部门类型
	
	private Integer byProjectWorkStatus ;							//0在项目中，1为待机状态，-1离职
	
	private Integer logout ;										//是否注销，-1注销，0为正常，（将关键信息修改为，如工号：__1039__等等...）
	
	
	/**************************************编外属性*****************************************************/
	private Date startDate ;										//开始日期
	
	private Date endDate ;											//结束日期
	
	private Date rz_startDate ;										//开始日期
	
	private Date rz_endDate ;										//结束日期
	
	private Date lz_startDate ;										//开始日期
	
	private Date lz_endDate ;										//结束日期
	
	private String org_id ;											//组织机构ID
	
	private String org_name ;										//组织机构名称

	private String position_id ;									//职位ID
	
	private String position_name ;									//职位名称
	
	private int createAccount ;										//是否创建账号（0：不创建，1：创建）
	
	private int isSendMailNotity ;									//添加人员，是否发送邮件通知
	
	private String account ;										//人员的登陆账号
	
	private String password ;										//人员的登陆密码
	
	private String user_id ;										//用户账号ID
	
	private String user_account ;									//登陆账号
	
	private String inIds ;											//IN查询

	
	public String getPositionRecord() {
		return positionRecord;
	}

	public String getRyjb() {
		return ryjb;
	}

	public void setRyjb(String ryjb) {
		this.ryjb = ryjb;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPositionRecord(String positionRecord) {
		this.positionRecord = positionRecord;
	}

	public Integer getLogout() {
		return logout;
	}

	public void setLogout(Integer logout) {
		this.logout = logout;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsSendMailNotity() {
		return isSendMailNotity;
	}

	public void setIsSendMailNotity(int isSendMailNotity) {
		this.isSendMailNotity = isSendMailNotity;
	}

	public Date getRz_startDate() {
		return rz_startDate;
	}

	public void setRz_startDate(Date rz_startDate) {
		this.rz_startDate = rz_startDate;
	}
	
	public Date getRz_endDate() {
		return rz_endDate;
	}

	public Integer getByProjectWorkStatus() {
		return byProjectWorkStatus;
	}

	public void setByProjectWorkStatus(Integer byProjectWorkStatus) {
		this.byProjectWorkStatus = byProjectWorkStatus;
	}

	public void setRz_endDate(Date rz_endDate) {
		this.rz_endDate = rz_endDate;
	}

	public Date getLz_startDate() {
		return lz_startDate;
	}


	public void setLz_startDate(Date lz_startDate) {
		this.lz_startDate = lz_startDate;
	}

	public Date getLz_endDate() {
		return lz_endDate;
	}

	public void setLz_endDate(Date lz_endDate) {
		this.lz_endDate = lz_endDate;
	}

	public String getDbmType() {
		return dbmType;
	}

	public void setDbmType(String dbmType) {
		this.dbmType = dbmType;
	}

	public String getLbmType() {
		return lbmType;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getDimissionDate() {
		return dimissionDate;
	}

	public void setDimissionDate(Date dimissionDate) {
		this.dimissionDate = dimissionDate;
	}

	public void setLbmType(String lbmType) {
		this.lbmType = lbmType;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getDbmDate() {
		return dbmDate;
	}

	public void setDbmDate(Date dbmDate) {
		this.dbmDate = dbmDate;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getLbmDate() {
		return lbmDate;
	}

	public void setLbmDate(Date lbmDate) {
		this.lbmDate = lbmDate;
	}

	public String getInIds() {
		return inIds;
	}

	public void setInIds(String inIds) {
		this.inIds = inIds;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public int getCreateAccount() {
		return createAccount;
	}

	public void setCreateAccount(int createAccount) {
		this.createAccount = createAccount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Integer getArchivesStatus() {
		return archivesStatus;
	}

	public void setArchivesStatus(Integer archivesStatus) {
		this.archivesStatus = archivesStatus;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}


	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmpState() {
		return empState;
	}

	public void setEmpState(String empState) {
		this.empState = empState;
	}

	public String getEmpLevel() {
		return empLevel;
	}

	public void setEmpLevel(String empLevel) {
		this.empLevel = empLevel;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTxPicPath() {
		return txPicPath;
	}

	public void setTxPicPath(String txPicPath) {
		this.txPicPath = txPicPath;
	}

	public String getPosition_id() {
		return position_id;
	}

	public void setPosition_id(String position_id) {
		this.position_id = position_id;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public String getSalNum() {
		return salNum;
	}

	public void setSalNum(String salNum) {
		this.salNum = salNum;
	}

	public String getSsNum() {
		return ssNum;
	}

	public void setSsNum(String ssNum) {
		this.ssNum = ssNum;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getStirps() {
		return stirps;
	}

	public void setStirps(String stirps) {
		this.stirps = stirps;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getHighest() {
		return highest;
	}

	public void setHighest(String highest) {
		this.highest = highest;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPosTitle() {
		return posTitle;
	}

	public void setPosTitle(String posTitle) {
		this.posTitle = posTitle;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCjgzrq() {
		return cjgzrq;
	}

	public void setCjgzrq(String cjgzrq) {
		this.cjgzrq = cjgzrq;
	}

	public String getKxgl() {
		return kxgl;
	}

	public void setKxgl(String kxgl) {
		this.kxgl = kxgl;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHkxz() {
		return hkxz;
	}

	public void setHkxz(String hkxz) {
		this.hkxz = hkxz;
	}

	public String getHkdz() {
		return hkdz;
	}

	public void setHkdz(String hkdz) {
		this.hkdz = hkdz;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getBysj() {
		return bysj;
	}

	public void setBysj(Date bysj) {
		this.bysj = bysj;
	}

	@Lob @Column(length=16777216)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}
