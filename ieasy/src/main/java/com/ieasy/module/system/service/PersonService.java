package com.ieasy.module.system.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.ConfigUtil;
import com.ieasy.basic.util.RandomUtils;
import com.ieasy.basic.util.StringUtil;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.module.common.dto.EmpExcelDto;
import com.ieasy.module.common.service.BaseService;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.OrgEntity;
import com.ieasy.module.system.entity.PersonEntity;
import com.ieasy.module.system.entity.PositionEntity;
import com.ieasy.module.system.entity.RoleEntity;
import com.ieasy.module.system.entity.UserEntity;
import com.ieasy.module.system.util.mail.SendMail;
import com.ieasy.module.system.web.form.PersonForm;

/**
 * 该类实现了人员的档案建立
 * 该类不具备添加人员的方法，人员的添加，由UserService类来实现，通过UserService类来添加账号，
 * 并创建人员对象，添加基本的人员信息（人员编号，姓名，性别，邮箱地址，联系电话）并标示该人员还未建立档案，需由EmpService来实现档案的建立
 * 档案的建立是修改人员的信息来完成
 * 如果档案未已建立状态，则管理下无法删除已建档的人员，只能删除账号信息
 * 与人员是一对一的关系，
 * @author ibm-work
 *
 */
@Service @Transactional
public class PersonService extends BaseService implements IPersonService {
	
	private Logger logger = Logger.getLogger(PersonService.class) ;
	
	@Inject
	private IBaseDao<OrgEntity> basedaoOrg ;

	@Inject
	private IBaseDao<PositionEntity> basedaoPos ;
	
	@Inject
	private IBaseDao<PersonEntity> basedaoPerson ;
	
	@Inject
	private IBaseDao<UserEntity> basedaoUser ;
	
	@Inject
	private IBaseDao<RoleEntity> basedaoRole ;
	
	@Inject
	private IDBUtilsHelper dbutil ;
	
	@Inject
	private SendMail sendMail ;

	@Override
	public Msg add(PersonForm form) {
		try {
			int equalsNum = this.equlasVal("e.num='"+form.getNum()+"'") ;
			if(equalsNum == 1) return new Msg(false, "该编号已存在！") ;
			int equalsMail = this.equlasVal("e.email='"+form.getEmail()+"'") ;
			if(equalsMail == 1) return new Msg(false, "该邮件地址已存在！") ;
			
			
			PersonEntity entity = new PersonEntity() ;
			BeanUtils.copyNotNullProperties(form, entity) ;
			entity.setCreated(new Date()) ;
			entity.setCreateName(this.getCurrentUser().getEmp_name()) ;
			entity.setArchivesStatus(1) ;
			
			if(null != form.getOrg_id() && !"".equals(form.getOrg_id().trim())) {
				entity.setOrg(this.basedaoOrg.load(OrgEntity.class, form.getOrg_id())) ;
			}
			
			if(null != form.getPosition_id() && !"".equals(form.getPosition_id().trim())) {
				entity.setPositions(this.basedaoPos.load(PositionEntity.class, form.getPosition_id())) ;
			}
			
			//如果人员头像路径不为空，则将已上传到临时目录下的头像图片拷贝到相应的目录中
			if(null != form.getTxPicPath()) {
				String tempTxpath = WebContextUtil.getRealPath(form.getTxPicPath()) ;
				String toPath = WebContextUtil.getRealPath(WebContextUtil.getUploadDir()+File.separator+ConfigUtil.get("emp_tx_path"))+File.separator+FilenameUtils.getName(form.getTxPicPath()) ;
				entity.setTxPicPath(WebContextUtil.getUploadDir()+File.separator+ConfigUtil.get("emp_tx_path")+File.separator+FilenameUtils.getName(form.getTxPicPath())) ;
				try {
					FileUtils.copyFile(new File(tempTxpath), new File(toPath)) ;
					com.ieasy.basic.util.file.FileUtils.deleteFile(tempTxpath) ;
				} catch (FileNotFoundException e) {
					logger.error("文件不存在，拷贝失败！请检查文件路径：" + tempTxpath) ;
				}
			}
			PersonEntity emp = this.basedaoPerson.add(entity) ;
			
			if(form.getCreateAccount() == 1) {
				Set<RoleEntity> roles = new HashSet<RoleEntity>() ;
				RoleEntity defaultRole = (RoleEntity) this.basedaoRole.queryObject("select t from RoleEntity t where t.defaultRole=?", new Object[]{1}) ;
				roles.add(defaultRole) ;
				
				UserEntity u = new UserEntity() ;
				u.setAccount(form.getNum()) ;
				u.setPassword(RandomUtils.generateNumber(3)) ;
				u.setCreated(new Date()) ;
				u.setEmp(emp) ;
				u.setRoles(roles) ;
				this.basedaoUser.add(u) ;
				
				if(form.getIsSendMailNotity() == 1) {
					form.setAccount(u.getAccount()) ;
					form.setPassword(u.getPassword()) ;
					this.sendMail.sendUserRegInfo(form) ;
				}
			}
			
			return new Msg(true, "添加成功！");
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("添加人员信息失败===>异常信息：", e) ;
			return new Msg(false, "添加人员信息失败！") ;
		}
	}
	
	@Override
	public Msg delete(PersonForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					if(!id.equals(WebContextUtil.getCurrentUser().getUser().getEmp_id())) {
						PersonEntity entity = this.basedaoPerson.load(PersonEntity.class, id) ;
						this.basedaoUser.executeSQL("update ieasy_sys_user set emp_id=? where emp_id=?", new Object[]{null, id}) ;
						
						//删除人员头像
						if(null != entity.getTxPicPath() && !"".equals(entity.getTxPicPath().trim()))
							com.ieasy.basic.util.file.FileUtils.deleteFile(WebContextUtil.getRealPath(entity.getTxPicPath())) ;
						
						this.basedaoPerson.delete(entity) ;
					}
				}
				return new Msg(true, "删除成功！") ;
			}
		} catch (Exception e) {
			logger.error("根据ID["+form.getIds()+"]删除信息失败===>异常信息：", e) ;
			return new Msg(false, "删除失败！") ;
		}
		return null;
	}

	@Override
	public Msg update(PersonForm form) {
		try {
			if(null == form.getNum() || "".equals(form.getNum())) return new Msg(false, "编号不能为空！") ;
			
			PersonEntity entity = this.basedaoPerson.load(PersonEntity.class, form.getId()) ;
			
			if(!form.getNum().equals(entity.getNum())) {
				int equalsNum = this.equlasVal("e.num='"+form.getNum()+"'") ;
				if(equalsNum == 1) return new Msg(false, "该编号已存在！") ;
			}
			
			if(null != form.getOrg_id() && !"".equals(form.getOrg_id().trim())) {
				entity.setOrg(this.basedaoOrg.load(OrgEntity.class, form.getOrg_id())) ;
			} else {
				entity.setOrg(null) ;
			}
			
			
			if(null != form.getPosition_id() && !"".equals(form.getPosition_id().trim())) {
				PositionEntity pos = entity.getPositions() ;
				if(null != pos) {
					//[{old:"", new:""},{old:"", new:""}]，保存不加[]，取出加入[]
					//岗位变更记录
					if(!form.getPosition_id().equals(pos.getId())) {
						PositionEntity pe = this.basedaoPos.load(PositionEntity.class, form.getPosition_id()) ;
						String positionRecord = entity.getPositionRecord() ;
						if(null != positionRecord && positionRecord.length() > 0) {
							form.setPositionRecord(positionRecord+",{\"name\": \""+pe.getName()+"\", \"date\": \""+DateUtils.formatYYYYMMDD(new Date())+"\"}") ;
						} else {
							form.setPositionRecord("" +
									"{\"name\": \""+pos.getName()+"\", \"date\": \""+DateUtils.formatYYYYMMDD(entity.getEnterDate())+"\"}," +
									"{\"name\": \""+pe.getName()+"\", \"date\": \""+DateUtils.formatYYYYMMDD(new Date())+"\"}") ;
						}
					} else {
						form.setPositionRecord(entity.getPositionRecord()) ;
					}
				}
				PositionEntity pe = new PositionEntity() ;
				pe.setId(form.getPosition_id()) ;
				entity.setPositions(pe) ;
			} else {
				entity.setPositions(null) ;
			}
			
			if((null == form.getTxPicPath() || "".equals(form.getTxPicPath())) && (null != entity.getTxPicPath() && !"".equals(entity.getTxPicPath()))) {
				com.ieasy.basic.util.file.FileUtils.deleteFile(WebContextUtil.getRealPath(entity.getTxPicPath())) ;
			}
			//如果两个头像的名称不相等,则说明已修改头像
			if(null != form.getTxPicPath() && !form.getTxPicPath().equals(entity.getTxPicPath())) {
				if(null != entity.getTxPicPath() && !"".equals(entity.getTxPicPath().trim()))
					com.ieasy.basic.util.file.FileUtils.deleteFile(WebContextUtil.getRealPath(entity.getTxPicPath())) ;
				
				String tempTxpath = WebContextUtil.getRealPath(form.getTxPicPath()) ;
				String toPath = WebContextUtil.getRealPath(WebContextUtil.getUploadDir()+File.separator+ConfigUtil.get("emp_tx_path"))+File.separator+FilenameUtils.getName(form.getTxPicPath()) ;
				FileUtils.copyFile(new File(tempTxpath), new File(toPath)) ;
				com.ieasy.basic.util.file.FileUtils.deleteFile(tempTxpath) ;
				
				entity.setTxPicPath(WebContextUtil.getUploadDir()+File.separator+ConfigUtil.get("emp_tx_path")+File.separator+FilenameUtils.getName(form.getTxPicPath())) ;
			}
			
			BeanUtils.copyNotNullProperties(form, entity, new String[]{"remark", "txPicPath", "created", "createName"}) ;
			
			if(null == form.getTxPicPath() || "".equals(form.getTxPicPath())) entity.setTxPicPath(null) ;
			entity.setModifyDate(new Date()) ;
			entity.setModifyName(this.getCurrentUser().getEmp_name()) ;
			entity.setArchivesStatus(1) ;
			
			this.basedaoPerson.update(entity) ;
			
			if(form.getCreateAccount() == 1) {
				BigInteger countEMP = this.basedaoUser.countSQL("select count(t.empid) from ieasy_sys_user t where t.emp_id=?", new Object[]{entity.getId()}, false) ;
				if(countEMP.intValue() <= 0) {
					UserEntity u = new UserEntity() ;
					u.setAccount(form.getNum()) ;
					u.setCreated(new Date()) ;
					u.setEmp(entity) ;
					this.basedaoUser.add(u) ;
				}
			}
			return new Msg(true, "修改成功！");
		} catch (FileNotFoundException e) {
			logger.error("文件不存在，请检查！", e) ;
		} catch (Exception e) {
			logger.error("修改人员信息失败===>异常信息：", e) ;
		}
		return new Msg(false, "修改人员信息失败！") ;
	}
	
	@Override
	public PersonForm get(PersonForm form) {
		try {
			String sql = "select " +
						 "e.id, e.name, e.num, e.sex, e.birth, e.zjlx, e.zjhm, "+
						 "e.salNum, e.ssNum, e.place, e.stirps, e.country, e.marriage, " +
						 "e.political, e.health, e.highest, e.profession, e.posTitle, e.school, " +
						 "e.cjgzrq, e.kxgl, e.height, e.weight, e.hkxz, e.hkdz, e.bysj, e.ryjb, " +
						 "e.empState, e.empType, e.empLevel, e.enterDate, e.dimissionDate, e.changeDate, " +
						 "e.mobile, e.phone, e.email, e.address, e.zipCode, " +
						 "e.archivesStatus, e.txPicPath, e.remark, " +
						 "e.created, e.modifyDate, e.modifyName, e.createName, " +
						 "e.dbmType, e.dbmDate, e.lbmType, e.lbmDate, e.byProjectWorkStatus, e.logout, " +
						 "e.positionRecord, " + 
						 "o.id as org_id, o.name as org_name, " +
						 "position.id as position_id " +
					 	 "from ieasy_sys_person e " +
					 	 "left join ieasy_sys_org o ON(e.org_id=o.id) " + 
					 	 "left join ieasy_sys_position position ON(e.position_id=position.id) " + 
					 	 "where e.id='"+form.getId()+"'";
			
			return (PersonForm)this.basedaoPerson.queryObjectSQL(sql, PersonForm.class, false) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("加载人员信息失败===>异常信息：", e) ;
			throw new ServiceException("加载信息异常：", e) ;
		}
	}

	@Override
	public DataGrid datagrid(PersonForm form) {
		
		if(null != form.getSort()) {
			String[] personSorts = new String[]{"num", "name", "sex", "archivesStatus", "empState"};
			if(StringUtil.arrayToString(personSorts).contains(form.getSort())) {
				SystemContext.setSort("e."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
			}
		}
		try {
			List<PersonForm> forms = new ArrayList<PersonForm>() ;
			Pager<PersonForm> pager = this.find(form) ;
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for(PersonForm pf : pager.getDataRows()) {
					forms.add(pf) ;
				}
			}
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("加载人员列表信息失败===>异常信息：", e) ;
			throw new ServiceException("加载人员列表信息异常：", e) ;
		}
	}
	
	
	private Pager<PersonForm> find(PersonForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "e.id, e.name, e.num, e.sex, e.birth, e.zjlx, e.zjhm, "+
					 "e.salNum, e.ssNum, e.place, e.stirps, e.country, e.marriage, " +
					 "e.political, e.health, e.highest, e.profession, e.posTitle, e.school, " +
				 	 "e.cjgzrq, e.kxgl, e.height, e.weight, e.hkxz, e.hkdz, e.bysj, e.ryjb, " +
				 	 "e.empState, e.empType, e.empLevel, e.enterDate, e.dimissionDate, e.changeDate, " +
				 	 "e.mobile, e.phone, e.email, e.address, e.zipCode, " +
				 	 "e.archivesStatus, e.txPicPath, e.remark, " +
				 	 "e.dbmType, e.dbmDate, e.lbmType, e.lbmDate, e.byProjectWorkStatus, e.logout, " +
				 	 "e.created, e.modifyDate, e.modifyName, e.createName, " +
				 	 "e.positionRecord, " + 
					 "o.id as org_id, o.name as org_name, " +
					 "position.id as position_id, position.name as position_name " +
				 	 "from ieasy_sys_person e " +
				 	 "left join ieasy_sys_org o ON(e.org_id=o.id) " +
				 	 "left join ieasy_sys_position position ON(e.position_id=position.id) " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		
		return this.basedaoPerson.findSQL(sql, alias, PersonForm.class, false) ;
	}
	
	private String addWhere(String sql, PersonForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getId() != null && !"".equals(form.getId().trim())) {
				sql += " and e.id=:id";
				params.put("id", form.getId().trim());
			}
			if (form.getLogout() != null) {
				sql += " and e.logout=:logout";
				params.put("logout", form.getLogout());
			}
			if (form.getNum() != null && !"".equals(form.getNum().trim())) {
				sql += " and e.num like :num";
				params.put("num", "%%" + form.getNum().trim() + "%%");
			}
			if (form.getName() != null && !"".equals(form.getName().trim())) {
				sql += " and e.name like :name";
				params.put("name", "%%" + form.getName().trim() + "%%");
			}
			if (form.getSex() != null && !"".equals(form.getSex())) {
				sql += " and e.sex= :sex ";
				params.put("sex", form.getSex().trim());
			}
			if (form.getEmpState() != null && !"".equals(form.getEmpState())) {
				sql += " and e.empState= :empState ";
				params.put("empState", form.getEmpState().trim());
			}
			if (form.getDbmType() != null && !"".equals(form.getDbmType())) {
				sql += " and e.dbmType= :dbmType ";
				params.put("dbmType", form.getDbmType().trim());
			}
			if (form.getLbmType() != null && !"".equals(form.getLbmType())) {
				sql += " and e.lbmType= :lbmType ";
				params.put("lbmType", form.getLbmType().trim());
			}
			if (null != form.getByProjectWorkStatus()) {
				sql += " and e.byProjectWorkStatus= :byProjectWorkStatus ";
				params.put("byProjectWorkStatus", form.getByProjectWorkStatus());
			}
			if (form.getEmail() != null && !"".equals(form.getEmail().trim())) {
				sql += " and e.email like :email ";
				params.put("email", "%%" + form.getEmail().trim() + "%%");
			}
			if (form.getStartDate() != null && !"".equals(form.getStartDate())) {
				sql += " and date_format(e.created,'%Y-%m-%d')>= date_format(:startDate,'%Y-%m-%d') ";
				params.put("startDate", form.getStartDate());
			}
			if (form.getEndDate() != null && !"".equals(form.getEndDate())) {
				sql += " and date_format(e.created,'%Y-%m-%d')<= date_format(:endDate,'%Y-%m-%d') ";
				params.put("endDate", form.getEndDate());
			}
			if (form.getRz_startDate() != null && !"".equals(form.getRz_startDate())) {
				sql += " and date_format(e.enterDate,'%Y-%m-%d')>= date_format(:rz_startDate,'%Y-%m-%d') ";
				params.put("rz_startDate", form.getRz_startDate());
				SystemContext.setSort("e.enterDate") ; SystemContext.setOrder("asc") ;
			}
			if (form.getRz_endDate() != null && !"".equals(form.getRz_endDate())) {
				sql += " and date_format(e.enterDate,'%Y-%m-%d')<= date_format(:rz_endDate,'%Y-%m-%d') ";
				params.put("rz_endDate", form.getRz_endDate());
			}
			if (form.getLz_startDate() != null && !"".equals(form.getLz_startDate())) {
				sql += " and date_format(e.dimissionDate,'%Y-%m-%d')>= date_format(:lz_startDate,'%Y-%m-%d') ";
				params.put("lz_startDate", form.getLz_startDate());
				SystemContext.setSort("e.dimissionDate") ; SystemContext.setOrder("asc") ;
			}
			if (form.getLz_endDate() != null && !"".equals(form.getLz_endDate())) {
				sql += " and date_format(e.dimissionDate,'%Y-%m-%d')<= date_format(:lz_endDate,'%Y-%m-%d') ";
				params.put("lz_endDate", form.getLz_endDate());
			}
			if (form.getOrg_id() != null && !"".equals(form.getOrg_id())) {
				try {
					Map<String, Object> map = this.dbutil.getQr().query("select queryOrgChildren('"+form.getOrg_id()+"') as child", new MapHandler()) ;
					String child = map.get("child").toString().replace("$,", "") ;
					sql += " and o.id IN(:inIds)" ;
					params.put("inIds", child.trim().split(",")) ;
				} catch (SQLException e) {
					logger.error("调用机构树状递归函数发生错误", e) ;
				}
			}
			if(form.getInIds() != null && !"".equals(form.getInIds().trim())) {
				String[] inIds = form.getInIds().trim().split(",") ;
				sql += " and e.id IN(:inIds)" ;
				params.put("inIds", inIds) ;
			}
		}
		return sql;
	}
	
	@Override
	public Msg batchChangeDept(PersonForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				if(ids.length>0) {
					Map<String, Object> alias = new HashMap<String, Object>() ;
					alias.put("org_id", (null==form.getOrg_id()||"".equals(form.getOrg_id())?null:form.getOrg_id())) ;
					alias.put("ids", ids) ;
					String sql = "update ieasy_sys_person t SET t.org_id=:org_id WHERE t.id IN(:ids)" ;
					this.basedaoPerson.executeSQL(sql, alias) ;
					return new Msg(true, "批量设置部门成功！") ;
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace() ;
			return new Msg(false, "批量设置部门失败！") ;
		} catch (Exception e) {
			e.printStackTrace();
			return new Msg(false, "批量设置部门失败！") ;
		}
		return new Msg(false, "批量设置部门失败！"); 
	}
	
	@Override
	public Msg batchSetPos(PersonForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				if(ids.length>0) {
					Map<String, Object> alias = new HashMap<String, Object>() ;
					alias.put("position_id", (null==form.getPosition_id()||"".equals(form.getPosition_id())?null:form.getPosition_id())) ;
					alias.put("ids", ids) ;
					
					String sql = "update ieasy_sys_person t SET t.position_id=:position_id WHERE t.id IN(:ids)" ;
					this.basedaoPerson.executeSQL(sql, alias) ;
					return new Msg(true, "批量设置岗位成功！") ;
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace() ;
			return new Msg(false, "批量设置岗位失败！") ;
		} catch (Exception e) {
			e.printStackTrace();
			return new Msg(false, "批量设置岗位失败！") ;
		}
		return new Msg(false, "批量设置岗位失败！"); 
	}
	
	@Override
	public Msg batchDimission(PersonForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				if(ids.length>0) {
					Map<String, Object> alias = new HashMap<String, Object>() ;
					alias.put("ids", ids) ;
					alias.put("empState", "离职") ;
					alias.put("dimissionDate", form.getDimissionDate()) ;
					alias.put("lbmType", "离职") ;
					alias.put("lbmDate", form.getDimissionDate()) ;
					alias.put("modifyDate", new Date()) ;
					alias.put("modifyName", this.getCurrentUser().getEmp_name()) ;
					
					String sql = "update ieasy_sys_person t SET " +
							"t.empState=:empState, t.dimissionDate=:dimissionDate, t.lbmType=:lbmType, t.lbmDate=:lbmDate," +
							"t.modifyName=:modifyName, t.modifyDate=:modifyDate " +
							"WHERE t.id IN(:ids)" ;
					this.basedaoPerson.executeSQL(sql, alias) ;
					
					return new Msg(true, "批量设置离职成功！") ;
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace() ;
			return new Msg(false, "批量设置离职失败！") ;
		} catch (Exception e) {
			e.printStackTrace();
			return new Msg(false, "批量设置离职失败！") ;
		}
		return new Msg(false, "批量设置离职失败！"); 
	}

	@Override
	public Msg batchOrgLogout(PersonForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					PersonEntity entity = this.basedaoPerson.load(PersonEntity.class, id) ;
					entity.setNum("__"+entity.getNum().replace("_", "")+"__") ;
					entity.setEmail("__none__"+entity.getEmail().substring(entity.getEmail().indexOf("@"))) ;
					entity.setLogout(-1) ;
				}
				return new Msg(true, "批量注销成功！") ;
			}
		} catch (ServiceException e) {
			e.printStackTrace() ;
			return new Msg(false, "批量注销失败！") ;
		} catch (Exception e) {
			e.printStackTrace();
			return new Msg(false, "批量注销失败！") ;
		}
		return new Msg(false, "批量注销失败！"); 
	}
	
	
	@Override
	public List<EmpExcelDto> exportEmpInfo(PersonForm form) {
		/*
		String sql = "select " +
					 "e.name, e.num, e.sex, " +
					 "date_format(e.birth,'%Y-%m-%d') birth, " +
					 "e.zjlx, e.zjhm, "+
					 "e.salNum, e.ssNum, e.place, e.stirps, e.country, e.marriage, " +
					 "e.political, e.health, e.highest, e.profession, e.posTitle, e.school, " +
				 	 "e.cjgzrq, e.kxgl, e.height, e.weight, e.hkxz, e.hkdz, " +
				 	 "e.empState, e.empType, e.empLevel, " +
				 	 "date_format(e.enterDate,'%Y-%m-%d') enterDate, " +
				 	 "date_format(e.changeDate,'%Y-%m-%d') changeDate, " +
				 	 "e.mobile, e.phone, e.email, e.address, e.zipCode " +
				 	 "from ieasy_sys_person e " ;
		return this.basedaoPerson.listSQL(sql, EmpExcelDto.class, false) ;
		*/
		//到部门类型(1:在职,2:转入,3:新增,4:试用,5:停薪留职返回,6:返聘)
		//离部门日期(1:转出-到开发部,2:转出-到非开发部,3:离职,4:停薪留职)
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "e.num, e.name, e.sex, " +
					 "e.email, " +
					 "date_format(e.birth,'%Y-%m-%d') birth, "+
				 	 "e.empState, " +
				 	 "date_format(e.enterDate,'%Y-%m-%d') enterDate , " +
				 	 "date_format(e.dimissionDate,'%Y-%m-%d') dimissionDate, " +
				 	 "date_format(e.changeDate,'%Y-%m-%d') changeDate, " +
				 	 "e.dbmType, " +
				 	 "date_format(e.dbmDate,'%Y-%m-%d') dbmDate, " +
				 	 "e.lbmType, " +
				 	 "date_format(e.lbmDate,'%Y-%m-%d') lbmDate, " +
				 	 "(CASE e.byProjectWorkStatus WHEN 0 THEN '在项目中' WHEN 1 THEN '待机' WHEN -1 THEN '注销' ELSE '注销' END) as byProjectWorkStatus , " +
					 "o.name as dept_name, " +
					 "position.name as position_name " +
				 	 "from ieasy_sys_person e " +
				 	 "left join ieasy_sys_org o ON(e.org_id=o.id) " +
				 	 "left join ieasy_sys_position position ON(e.position_id=position.id) " +
				 	 "where 1=1 and e.num<>'ROOT' ";
		sql = addWhere(sql, form, alias) ;
		
		return this.basedaoPerson.listSQL(sql, alias, EmpExcelDto.class, false) ;
		
	}

	/*@SuppressWarnings("unchecked")
	private List<String> getEmpNum() {
		String sql = "select t.num from ieasy_sys_person t" ;
		return this.basedaoPerson.getCurrentSession().createSQLQuery(sql).list() ;
	}*/
	

	/*
	@Override
	public Msg parseDataInsert(String datafile) {
		Msg msg = new Msg(true, "数据导入完成！") ;
		
		List<Object> list = ExcelUtil.getInstance().readExce2ObjsByPath(datafile, EmpExcelDto.class, 0, 1) ;
		if(null == list || list.size() < 0) return new Msg(false, "Excle文件中没有数据，或格式不正确！请检查") ;
		
		DruidPooledConnection conn = null ;
		PreparedStatement ps1 = null ;
		PreparedStatement ps2 = null ;
		try {
			
			String sql = "insert into ieasy_sys_person(" +
					"id, name, num, sex, birth, zjlx, zjhm, salNum, ssNum, place, archivesStatus, stirps, country, " +
					"marriage, political, health, highest, profession, posTitle, school, cjgzrq, " +
					"kxgl, height, weight, hkxz, hkdz, empState, empType, empLevel, enterDate, changeDate, " +
					"mobile, phone, email, address, zipCode,created)" +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			
			String[] propertyName = {
					"id","name","num","sex","birth","zjlx","zjhm","salNum","ssNum","place","archivesStatus",
					"stirps","country","marriage","political","health","highest","profession","posTitle","school","cjgzrq",
					"kxgl","height","weight","hkxz","hkdz","empState","empType","empLevel","enterDate","changeDate",
					"mobile","phone","email","address","zipCode"
			};
			
			String sqlUser = "insert into ieasy_sys_user(id, account, password, status, emp_id) values(?,?,?,?,?)" ;
			String[] userPropertyName ={"id", "account", "password", "status", "emp_id"};
			
			QueryRunner qr = this.basedaoPerson.getDbHelper().getQr();
			conn = DBConnectPool.getInstance().getConnection() ;
			ps1 = conn.prepareStatement(sql);
			ps2 = conn.prepareStatement(sqlUser);
			
			List<EmpExcelDto> importInfo = new ArrayList<EmpExcelDto>() ;
			for (Object o : list) {
				EmpExcelDto eDto = (EmpExcelDto) o ;
				
				if(getEmpNum().contains(eDto.getNum())) { //编号相同则不导入数据
					eDto.setState(false) ;
					eDto.setMsg("该编号已存在") ;
					importInfo.add(eDto) ;
					continue ;
				}
				
				if(null == eDto.getNum() || "".equals(eDto.getNum().trim()) || "NULL".equals(eDto.getNum())
						|| null == eDto.getName() || "".equals(eDto.getName().trim()) || "NULL".equals(eDto.getName())) {
					eDto.setState(false) ;
					eDto.setMsg("有空值，请检查") ;
					importInfo.add(eDto) ;
					continue ;
				}
				
				
				//使用SQL来进行批量插入人员数据
				eDto.setId(UUIDHexGenerator.generator().toString()) ;
				eDto.setArchivesStatus(1) ;
				qr.fillStatementWithBean(ps1, eDto, propertyName) ;
				ps1.addBatch() ;
				
				
				UserForm user = new UserForm() ;
				user.setAccount(PinyinUtil.getPinYin(eDto.getName())) ;
				user.setPassword("123456") ;
				user.setStatus(0) ;
				//使用SQL来进行批量插入用户账号数据
				user.setId(UUIDHexGenerator.generator().toString()) ;
				user.setEmp_id(eDto.getId()) ;
				qr.fillStatementWithBean(ps2, user, userPropertyName) ;
				ps2.addBatch() ;
				
				
				eDto.setState(true) ;
				eDto.setMsg("导入成功") ;
				importInfo.add(eDto) ;
			}
			ps1.executeBatch() ;
			ps2.executeBatch() ;
			msg.setObj(importInfo) ;
		} catch (SQLException e) {
			logger.error("导入人员信息失败，原因：", e) ;
			msg.setStatus(false);msg.setMsg("导入失败，可能人员编号有重复！请检查") ;
		} finally {
			try {
				if(null != ps1) ps1.clearBatch();ps1.close() ;
				if(null != ps2) ps2.clearBatch();ps2.close() ;
				if(null != conn) conn.close() ;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return msg ;
	} 
	*/

	private int equlasVal(String param) {
		String sql = "select e.* from ieasy_sys_person e where " + param;
		return this.basedaoPerson.countSQL(sql, false).intValue() ;
	}

	@Override
	public Msg modifyWorkStatus(PersonForm form) {
		int status = 0 ;
		if(form.getByProjectWorkStatus() == 0) {
			status = 1 ;
		}
		Map<String, Object> alias = new HashMap<String, Object>() ;
		alias.put("id", form.getId()) ;
		alias.put("s", status) ;
		this.basedaoPerson.executeSQL("update ieasy_sys_person t set byProjectWorkStatus=:s where t.id=:id", alias) ;
		return new Msg(true, "修改成功！");
	}

}
