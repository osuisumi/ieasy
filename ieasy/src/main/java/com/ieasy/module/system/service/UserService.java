/**
 * 
 */
package com.ieasy.module.system.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.StringUtil;
import com.ieasy.basic.util.cons.Const;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.module.common.dto.UserExcelDto;
import com.ieasy.module.common.service.BaseService;
import com.ieasy.module.common.web.servlet.AppConst;
import com.ieasy.module.system.entity.OrgEntity;
import com.ieasy.module.system.entity.PermitsMenuEntity;
import com.ieasy.module.system.entity.PermitsOperEntity;
import com.ieasy.module.system.entity.PersonEntity;
import com.ieasy.module.system.entity.RoleEntity;
import com.ieasy.module.system.entity.UserEntity;
import com.ieasy.module.system.util.mail.SendMail;
import com.ieasy.module.system.web.form.ACLForm;
import com.ieasy.module.system.web.form.AuthForm;
import com.ieasy.module.system.web.form.LoginSession;
import com.ieasy.module.system.web.form.LoginUser;
import com.ieasy.module.system.web.form.PersonForm;
import com.ieasy.module.system.web.form.RoleForm;
import com.ieasy.module.system.web.form.UserForm;

/**
 * 删除用户操作，只删除用户账号的数据，用户对于的员工信息见不会删除，
 * 员工删除操作需员工管理页面下进行操作
 * @author ibm-work
 *
 */
@Service @Transactional
public class UserService extends BaseService implements IUserService {
	
	private Logger logger = Logger.getLogger(UserService.class) ;

	@Inject
	private IBaseDao<UserEntity> basedaoUser ;
	
	@Inject
	private IBaseDao<OrgEntity> basedaoOrg ;
	
	@Inject
	private IBaseDao<PersonEntity> basedaoPerson ;
	
	@Inject
	private IBaseDao<RoleEntity> basedaoRole ;
	
	@Inject
	private IBaseDao<PermitsMenuEntity> basedaoPermitsMenu;

	@Inject
	private IBaseDao<PermitsOperEntity> basedaoPermitsOper;
	
	@Inject
	private IDBUtilsHelper dbutil ;
	
	@Inject
	private SendMail sendMail ;
	
	@Override
	public Msg add(UserForm form) {
		try {
			if(null == form.getAccount() || "".equals(form.getAccount())) return new Msg(false, "账号不能为空！") ;
			int equalsAccount = this.equlasVal("u.account='"+form.getAccount()+"'") ;
			if(equalsAccount == 1) return new Msg(false, "该账号已存在！") ;
			int equalsMail = this.equlasValByPerson("p.email='"+form.getEmail()+"'") ;
			if(equalsMail == 1) return new Msg(false, "该邮件地址已存在！") ;
			
			PersonEntity empEntity = new PersonEntity() ;
			BeanUtils.copyNotNullProperties(form, empEntity) ;
			empEntity.setCreated(new Date()) ;
			empEntity.setCreateName(this.getCurrentUser().getEmp_name()) ;
			
			UserEntity entity = new UserEntity() ;
			BeanUtils.copyNotNullProperties(form, entity) ;
			entity.setCreated(new Date()) ;
			entity.setCreateName(this.getCurrentUser().getEmp_name()) ;
			entity.setEmp(empEntity) ;
			
			Set<RoleEntity> roles = new HashSet<RoleEntity>() ;
			//获取默认角色
			RoleEntity defaultRole = (RoleEntity) this.basedaoRole.queryObject("select t from RoleEntity t where t.defaultRole=?", new Object[]{1}) ;
			roles.add(defaultRole) ;
			
			if(null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
				String[] split = form.getRole_ids().split(",") ;
				for (String role_id : split) {
					roles.add(this.basedaoRole.load(RoleEntity.class, role_id)) ;
				}
			}
			entity.setRoles(roles) ;
			
			
			this.basedaoPerson.add(empEntity) ;
			this.basedaoUser.add(entity) ;
			
			
			if(form.isSendMailNotity()) {
				PersonForm pf = new PersonForm() ;
				pf.setName(form.getName()) ;
				pf.setEmail(form.getEmail()) ;
				pf.setAccount(form.getAccount()) ;
				String pwd = (form.getPassword() == null || "".equals(form.getPassword().trim())?"":form.getPassword()) ;
				pf.setPassword(pwd) ;
				this.sendMail.sendUserRegInfo(pf) ;
			}
			
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			e.printStackTrace() ;
			logger.error("添加用户信息失败===>异常信息：", e) ;
			return new Msg(false, "添加失败！") ;
		}
	}
	
	@Override
	public Msg createAccount(UserForm form) {
		try {
			int equalsAccount = this.equlasVal("u.account='"+form.getAccount()+"'") ;
			if(equalsAccount == 1) return new Msg(false, "该账号已存在！") ;
			
			PersonEntity emp = this.basedaoPerson.load(PersonEntity.class, form.getEmp_id());

			UserEntity entity = new UserEntity();
			BeanUtils.copyNotNullProperties(form, entity);
			entity.setCreated(new Date()) ;
			entity.setCreateName(this.getCurrentUser().getEmp_name()) ;
			entity.setEmp(emp);

			if(null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
				Set<RoleEntity> roles = new HashSet<RoleEntity>() ;
				String[] split = form.getRole_ids().split(",") ;
				for (String role_id : split) {
					roles.add(this.basedaoRole.load(RoleEntity.class, role_id)) ;
				}
				entity.setRoles(roles) ;
			}

			this.basedaoUser.add(entity);
			return new Msg(true, "账户创建成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("账户创建失败===>异常信息：", e);
			return new Msg(false, "账户创建失败！");
		}
	}

	@Override
	public Msg delete(UserForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoUser.delete(UserEntity.class, id) ;
				}
			}
			return new Msg(true, "删除成功！") ;
		} catch (Exception e) {
			logger.error("根据ID["+form.getIds()+"]删除用户信息失败===>异常信息：", e) ;
			return new Msg(false, "删除失败！") ;
		}
	}

	@Override
	public Msg update(UserForm form) {
		try {
			UserEntity entity = this.basedaoUser.load(UserEntity.class, form.getId()) ;
			BeanUtils.copyNotNullProperties(form, entity, new String[]{"password", "created", "createName"}) ;
			entity.setModifyDate(new Date()) ;
			entity.setModifyName(this.getCurrentUser().getEmp_name()) ;
			
			PersonEntity emp =entity.getEmp() ;
			
			//判断该账号是否关联了人员信息，如果未关联，则不进行设置用户关联人员信息
			if(null != emp) {
				emp.setSex(form.getSex()) ;
				emp.setEmail(form.getEmail()) ;
				emp.setMobile(form.getMobile()) ;
				emp.setModifyDate(new Date()) ;
				entity.setEmp(emp) ;
			} else {
				//如果账号未关联人员信息，而编辑账号信息时如果输入了姓名字段，则建立人员信息并关联到账号
				if(form.getName() != null && !"".equals(form.getName().trim())) {
					PersonEntity person = new PersonEntity() ;
					BeanUtils.copyNotNullProperties(form, person, new String[]{"id"}) ;
					this.basedaoPerson.add(person) ;
					entity.setEmp(person) ;
				}
			}
			
			if(null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
				Set<RoleEntity> roles = new HashSet<RoleEntity>() ;
				String[] split = form.getRole_ids().split(",") ;
				for (String role_id : split) {
					roles.add(this.basedaoRole.load(RoleEntity.class, role_id)) ;
				}
				entity.setRoles(roles) ;
			} else {
				entity.setRoles(null) ;
			}
			

			this.basedaoUser.update(entity) ;
			
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			logger.error("修改人员信息失败===>异常信息：", e) ;
			return new Msg(false, "修改人员信息失败！") ;
		}
	}
	
	@Override
	public UserForm get(UserForm form) {
		try {
			String sql = "select " +
					 	 "u.id, u.account, u.password, u.status, " +
					 	 "e.id emp_id, e.num, e.name, e.sex, e.mobile, e.email, " +
					 	 "o.id org_id " +
					 	 "from ieasy_sys_user u " +
					 	 "left join ieasy_sys_person e ON(u.emp_id=e.id) " + 
					 	 "left join ieasy_sys_org o ON(e.org_id=o.id) " + 
					 	 "where u.id='"+form.getId()+"'";
			
			form = (UserForm)this.basedaoUser.queryObjectSQL(sql, UserForm.class, false) ;
			
			String role_sql = "SELECT ur.role_id FROM ieasy_sys_user_roles ur WHERE ur.user_id=?" ;
			List<Object[]> roleIds = this.basedaoUser.listSQL(role_sql, form.getId()) ;
			if(null != roleIds && roleIds.size() > 0)
				form.setRole_ids(org.apache.commons.lang3.StringUtils.join(roleIds.toArray(), ",")) ;
				
			return form ;
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("加载人员信息失败===>异常信息：", e) ;
			throw new ServiceException("加载用户信息异常：", e) ;
		}
	}

	@Override
	public DataGrid datagrid(UserForm form) {
		if(null != form.getSort()) {
			String[] userSorts = new String[]{"account", "status", "created"};
			String[] personSorts = new String[]{"num", "name", "sex"};
			if(StringUtil.arrayToString(userSorts).contains(form.getSort())) {
				SystemContext.setSort("u."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
			} else if(StringUtil.arrayToString(personSorts).contains(form.getSort())) {
				SystemContext.setSort("e."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
			}
		}
		
		try {
			List<UserForm> forms = new ArrayList<UserForm>() ;
			Pager<UserForm> pager = this.find(form) ;
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for(UserForm pf : pager.getDataRows()) {

					if(null != pf.getId()) {
						if(null == pf.getPassword() || "".equals(pf.getPassword().trim()) || "d41d8cd98f00b204e9800998ecf8427e".equals(pf.getPassword().trim())) {
							pf.setPassword("空") ;
						} else {
							pf.setPassword("******") ;
						}
						
						if(null == pf.getLastAcceccTime() || "".equals(pf.getLastAcceccTime())) {
							pf.setLastAcceccTime("") ;
							pf.setDiffDatetime("未曾登录") ;
						} else {
							//计算离线时间
							Set<String> keySet = AppConst.SESSION_MAP.keySet() ;
							if(null != keySet && !keySet.isEmpty()) {
								for(String id : keySet){
									HttpSession sess = AppConst.SESSION_MAP.get(id);
									LoginSession userSession = (LoginSession)sess.getAttribute(Const.USER_SESSION);
									if(null != userSession) {
										if(pf.getId().equalsIgnoreCase(userSession.getUser().getUser_id())) {
											pf.setDiffDatetime("<font color='red'>在线</font>") ;
										} else {
											pf.setDiffDatetime(DateUtils.dateDiffStr(pf.getLastAcceccTime(), DateUtils.getSysDateTimeStr(), DateUtils.FORMAT_DATETIME)) ;
										}
									}
								}
							} else {
								pf.setDiffDatetime(DateUtils.dateDiffStr(pf.getLastAcceccTime(), DateUtils.getSysDateTimeStr(), DateUtils.FORMAT_DATETIME)) ;
							}
						}
						
						//获取角色
						List<RoleForm> roles = this.basedaoRole.listSQL("select r.name from ieasy_sys_user_roles t LEFT JOIN ieasy_sys_role r on(r.id=t.role_id) WHERE t.user_id=?", new Object[]{pf.getId()}, RoleForm.class, false) ;
						if(null != roles) {
							StringBuffer s = new StringBuffer() ;
							for (RoleForm r : roles) {
								s.append(r.getName()+",");
							}
							pf.setRole_names((s.length()>0?s.deleteCharAt(s.length()-1).toString():"")) ;
						}
					} else {
						pf.setAccount("未创建") ;
						pf.setPassword("未创建") ;
						pf.setStatus(2) ;
					}
					
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
	
	
	private Pager<UserForm> find(UserForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "e.id emp_id, e.num, e.name, e.sex, e.mobile, e.email, e.empState, " +
					 "u.id, u.account, u.password, u.status, u.created, u.modifyDate, u.modifyName, u.createName, u.lastAcceccTime, " +
					 "o.id as org_id, o.name as org_name " +
				 	 "from ieasy_sys_user u " +
				 	 "right join ieasy_sys_person e ON(e.id=u.emp_id) " +
				 	 "left join ieasy_sys_org o ON(e.org_id=o.id) " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		return this.basedaoUser.findSQL(sql, alias, UserForm.class, false) ;
	}
	
	private String addWhere(String sql, UserForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getId() != null && !"".equals(form.getId().trim())) {
				sql += " and p.id=:id";
				params.put("id", form.getId());
			}
			if (form.getNum() != null && !"".equals(form.getNum().trim())) {
				sql += " and e.num like :num";
				params.put("num", "%%" + form.getNum() + "%%");
			}
			if (form.getName() != null && !"".equals(form.getName().trim())) {
				sql += " and e.name like :name";
				params.put("name", "%%" + form.getName() + "%%");
			}
			if (form.getSex() != null && !"".equals(form.getSex())) {
				sql += " and e.sex= :sex ";
				params.put("sex", form.getSex());
			}
			if (form.getEmail() != null && !"".equals(form.getEmail().trim())) {
				sql += " and e.email like :email ";
				params.put("email", "%%" + form.getEmail() + "%%");
			}
			if (form.getStartDate() != null && !"".equals(form.getStartDate())) {
				sql += " and date_format(e.created,'%Y-%m-%d')>= date_format(:startDate,'%Y-%m-%d') ";
				params.put("startDate", form.getStartDate());
			}
			if (form.getEndDate() != null && !"".equals(form.getEndDate())) {
				sql += " and date_format(e.created,'%Y-%m-%d')<= date_format(:endDate,'%Y-%m-%d') ";
				params.put("endDate", form.getEndDate());
			}
			if (form.getAccount() != null && !"".equals(form.getAccount().trim())) {
				sql += " and u.account like :account ";
				params.put("account", "%%" + form.getAccount() + "%%");
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
		}
		return sql;
	}
	
	@Override
	public Msg batchClearPwd(UserForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					UserEntity user = this.basedaoUser.load(UserEntity.class, id);
					user.setPassword("") ;
					user.setCreated(new Date()) ;
					this.basedaoUser.update(user) ;
				}
				return new Msg(true, "清除密码成功！") ;
			} else {
				return new Msg(false, "清除密码失败！") ;
			}
		} catch (ServiceException e) {
			return new Msg(false, "清除密码失败！") ;
		}
	}

	@Override
	public Msg batchResetPwd(UserForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					UserEntity user = this.basedaoUser.load(UserEntity.class, id);
					user.setPassword((null==form.getPassword()||"".equals(form.getPassword().trim())?"":form.getPassword())) ;
					user.setCreated(new Date()) ;
					this.basedaoUser.update(user) ;
				}
				return new Msg(true, "重设密码成功！") ;
			} else {
				return new Msg(false, "重设密码失败！") ;
			}
		} catch (ServiceException e) {
			return new Msg(false, "重设密码失败！") ;
		}
	}

	@Override
	public Msg batchLockAccount(UserForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					UserEntity user = this.basedaoUser.load(UserEntity.class, id);
					user.setStatus(form.getStatus()) ;
					user.setCreated(new Date()) ;
					this.basedaoUser.update(user) ;
				}
			}
		} catch (ServiceException e) {
			return new Msg(false, "程序发送错误！") ;
		}
		
		if(form.getStatus() == 0) {
			return new Msg(true, "账号已解锁！") ;
		} else if(form.getStatus() == 1) {
			return new Msg(true, "账号已解锁！") ;
		} else {
			return new Msg(true, "出现错误！") ;
		}
	}
	
	@Override
	public List<UserExcelDto> exportBasicUserInfo() {
		String sql = "select u.account, u.password, e.num, e.name, e.sex, e.email, e.mobile, " +
					 "(CASE u.status WHEN 0 THEN '正常' ELSE '禁止登陆' END) as status " +
					 "from ieasy_sys_user u LEFT JOIN ieasy_sys_person e ON(u.emp_id=e.id)" ;
		return this.basedaoUser.listSQL(sql, UserExcelDto.class, false) ;
	}

	@SuppressWarnings("unchecked")
	private List<String> getEmpNum() {
		String sql = "select t.num from ieasy_sys_person t" ;
		return this.basedaoUser.getCurrentSession().createSQLQuery(sql).list() ;
	}

	@Override
	public Msg parseDataInsert(String datafile) {
		Msg msg = new Msg(true, "数据导入完成！") ;
		List<Object> list = ExcelUtil.getInstance().readExce2ObjsByPath(datafile, UserExcelDto.class, 1, 1) ;
		if(null == list || list.size() < 0) return new Msg(false, "Excle文件中没有数据，或格式不正确！请检查") ;
		
		List<UserExcelDto> importInfo = new ArrayList<UserExcelDto>() ;
		for (Object o : list) {
			UserExcelDto e = (UserExcelDto) o ;
			//编号相同则不导入数据
			if(getEmpNum().contains(e.getNum())) {
				e.setState(false) ;
				e.setMsg("该编号已存在") ;
				importInfo.add(e) ;
				continue ;
			}
			
			if(null == e.getNum() || "".equals(e.getNum().trim()) || "NULL".equals(e.getNum())
					|| null == e.getName() || "".equals(e.getName().trim()) || "NULL".equals(e.getName())
					|| null == e.getAccount() || "".equals(e.getAccount().trim()) || "NULL".equals(e.getAccount())
					|| null == e.getEmail() || "".equals(e.getAccount().trim()) || "NULL".equals(e.getEmail())) {
				e.setState(false) ;
				e.setMsg("有空值，请检查") ;
				importInfo.add(e) ;
				continue ;
			}
 			
			UserForm u = new UserForm() ;
			BeanUtils.copyNotNullProperties(e, u, new String[]{"status"}) ;
			u.setStatus(("正常".equals(e.getStatus().trim())?0:1)) ;
			this.add(u) ;
			
			e.setState(true) ;
			e.setMsg("导入成功") ;
			importInfo.add(e) ;
		}
		msg.setObj(importInfo) ;
		
		return msg ;
	}
	
	private int equlasVal(String param) {
		String sql = "select u.* from ieasy_sys_user u where " + param;
		return this.basedaoUser.countSQL(sql, false).intValue() ;
	}
	
	private int equlasValByPerson(String param) {
		String sql = "select p.* from ieasy_sys_person p where " + param;
		return this.basedaoPerson.countSQL(sql, false).intValue() ;
	}

	@Override
	public Msg batchUserRole(UserForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				
				Set<RoleEntity> roles = new HashSet<RoleEntity>() ;
				if(null != form.getRole_ids() && !"".equals(form.getRole_ids())) {
					String[] ids = form.getRole_ids().split(",") ;
					for (String id : ids) {
						roles.add(this.basedaoRole.load(RoleEntity.class, id)) ;
					}
				}
				
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					UserEntity user = this.basedaoUser.load(UserEntity.class, id);
					user.setRoles(roles) ;
 					user.setModifyDate(new Date()) ;
					this.basedaoUser.update(user) ;
				}
				return new Msg(true, "加入角色成功！") ;
			} else {
				return new Msg(false, "加入角色失败！") ;
			}
		} catch (BeansException e) {
			return new Msg(false, "批量加入角色失败！") ;
		}
	}


	@Override
	public LoginUser loginCheck(LoginUser form) {
		String sql = "select " +
					 "u.id user_id, u.account, u.password, u.status, " +
					 "e.id emp_id, e.num emp_num, e.name emp_name, e.txPicPath emp_tx, e.email emp_email, " +
					 "o.id org_id, o.name org_name " +
					 "from ieasy_sys_user u " +
					 "inner join ieasy_sys_person e ON(e.id=u.emp_id) " +
					 "left join ieasy_sys_org o ON(o.id=e.org_id)" +
					 "where " +
					 "u.account=? and u.password=?" ;
		
		String pwd = (null==form.getPassword() || "".equals(form.getPassword().trim())?"":form.getPassword()) ;
		LoginUser lu = (LoginUser) this.basedaoUser.queryObjectSQL(sql, new Object[]{form.getAccount().trim(), pwd}, LoginUser.class, false);
		
		if(null != lu) {
			this.basedaoUser.updateByHql("update UserEntity set lastAcceccTime=? where id=?", new Object[]{DateUtils.formatYYYYMMDD_HHMMSS(new Date()), lu.getUser_id()}) ;
		}
		
		return lu ;
	}

	/**
	 * 获取登陆用户的权限
	 * 如果同时拥有用户授权，角色授权，部门授权，岗位授权，则进行权限累加，并去除重复的权限
	 */
	@Override
	public AuthForm getAuth(String userId) {
		AuthForm auth = new AuthForm() ;
		List<Object> tree = new ArrayList<Object>() ;
		List<ACLForm> opers = new ArrayList<ACLForm>() ;
		List<String> authUrl = new ArrayList<String>() ;
		
		//获取用户和人员信息
		String sql = "select " +
					 "u.id, u.account, u.emp_id, " +
					 "e.org_id, e.position_id " +
				 	 "from ieasy_sys_user u " +
				 	 "left join ieasy_sys_person e ON(e.id=u.emp_id) " +
				 	 "where u.id=? ";
		UserForm user = (UserForm)this.basedaoUser.queryObjectSQL(sql, userId, UserForm.class, false) ;
		
		//获取用户的角色ID
		List<Object[]> roleIds = this.basedaoRole.listSQL("select r.user_id, r.role_id from ieasy_sys_user_roles r where r.user_id=?", userId) ;
		
		
		/*********************************用户权限**************************************/
		//获取用户权限
		List<ACLForm> aclUserMenus = getAclMenus(userId, Const.PRINCIPAL_USER) ;
		List<ACLForm> aclUserOpers = getAclOpers(userId, Const.PRINCIPAL_USER) ;
		//菜单
		for (ACLForm a : aclUserMenus) {
			Map<String, Object> dataRecord = new HashMap<String, Object>() ;
			dataRecord.put("id", a.getMenuId());
			dataRecord.put("text", a.getMenuName());
			dataRecord.put("href", a.getMenuHref());
			dataRecord.put("iconCls", a.getMenuIconCls());
			dataRecord.put("state", a.getState());
			dataRecord.put("pid", a.getMenuPid());
			dataRecord.put("weight", a.getMenuSort());
			if(!tree.contains(dataRecord)) {
				tree.add(dataRecord) ;
			}
			//操作
			for (ACLForm aclForm : aclUserOpers) {
				opers.add(aclForm) ;
				authUrl.add(aclForm.getOperMenuHref()) ;
			}
		}
		
		
		/*********************************角色权限**************************************/
		//获取用户角色权限
		for (Object[] o : roleIds) {
			List<ACLForm> aclRoleMenus = getAclMenus((String)o[1], Const.PRINCIPAL_ROLE) ;
			List<ACLForm> aclRoleOpers = getAclOpers((String)o[1], Const.PRINCIPAL_ROLE) ;
			//菜单
			for (ACLForm a : aclRoleMenus) {
				Map<String, Object> dataRecord = new HashMap<String, Object>() ;
				dataRecord.put("id", a.getMenuId());
				dataRecord.put("text", a.getMenuName());
				dataRecord.put("href", a.getMenuHref());
				dataRecord.put("iconCls", a.getMenuIconCls());
				dataRecord.put("state", a.getState());
				dataRecord.put("pid", a.getMenuPid());
				dataRecord.put("weight", a.getMenuSort());
				if(!tree.contains(dataRecord)) {
					tree.add(dataRecord) ;
				}
			}
			//操作
			for (ACLForm aclForm : aclRoleOpers) {
				opers.add(aclForm) ;
				authUrl.add(aclForm.getOperMenuHref()) ;
			}
			
		}
		
		/**********************************部门权限*************************************/
		//获取部门权限
		List<ACLForm> aclOrgMenus = getAclMenus(user.getOrg_id(), Const.PRINCIPAL_DEPT) ;
		List<ACLForm> aclOrgOpers = getAclOpers(user.getOrg_id(), Const.PRINCIPAL_DEPT) ;
		//菜单
		for (ACLForm a : aclOrgMenus) {
			Map<String, Object> dataRecord = new HashMap<String, Object>() ;
			dataRecord.put("id", a.getMenuId());
			dataRecord.put("text", a.getMenuName());
			dataRecord.put("href", a.getMenuHref());
			dataRecord.put("iconCls", a.getMenuIconCls());
			dataRecord.put("state", a.getState());
			dataRecord.put("pid", a.getMenuPid());
			dataRecord.put("weight", a.getMenuSort());
			if(!tree.contains(dataRecord)) {
				tree.add(dataRecord) ;
			}
		}
		for (ACLForm aclForm : aclOrgOpers) {
			opers.add(aclForm) ;
			authUrl.add(aclForm.getOperMenuHref()) ;
		}
		/***********************************岗位权限************************************/
		//获取岗位权限
		List<ACLForm> aclPositionMenus = getAclMenus(user.getPosition_id(), Const.PRINCIPAL_POSITION) ;
		List<ACLForm> aclPositionOpers = getAclOpers(user.getPosition_id(), Const.PRINCIPAL_POSITION) ;
		//菜单
		for (ACLForm a : aclPositionMenus) {
			Map<String, Object> dataRecord = new HashMap<String, Object>() ;
			dataRecord.put("id", a.getMenuId());
			dataRecord.put("text", a.getMenuName());
			dataRecord.put("href", a.getMenuHref());
			dataRecord.put("iconCls", a.getMenuIconCls());
			dataRecord.put("state", a.getState());
			dataRecord.put("pid", a.getMenuPid());
			dataRecord.put("weight", a.getMenuSort());
			if(!tree.contains(dataRecord)) {
				tree.add(dataRecord) ;
			}
		}
		//操作
		for (ACLForm aclForm : aclPositionOpers) {
			opers.add(aclForm) ;
			authUrl.add(aclForm.getOperMenuHref()) ;
		}

		/***********************************************************************/
		auth.setAuthTree(tree) ;
		auth.setAuthOpers(opers) ;
		auth.setAuthUrl(authUrl) ;
		
		return auth;
	}
	
	private List<ACLForm> getAclMenus(String principalId, String principalType) {
		String sql = "select t.* from ieasy_sys_permits_menu t where t.principalId=? and t.principalType=?" ;
		return this.basedaoPermitsMenu.listSQL(sql, new Object[]{principalId, principalType}, ACLForm.class, false) ;
	}
	
	private List<ACLForm> getAclOpers(String principalId, String principalType) {
		String sql = "select t.* from ieasy_sys_permits_oper t where t.principalId=? and t.principalType=?" ;
		return this.basedaoPermitsMenu.listSQL(sql, new Object[]{principalId, principalType}, ACLForm.class, false) ;
	}
	
	
	
	
	
	
	
	
	
}
