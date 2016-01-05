package com.ieasy.module.system.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.IpUtil;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.basic.util.springmvc.ContextHolderUtils;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.LogEntity;
import com.ieasy.module.system.entity.LoginLogEntity;
import com.ieasy.module.system.web.form.LogForm;
import com.ieasy.module.system.web.form.LoginLogForm;
import com.ieasy.module.system.web.form.LoginSession;
import com.ieasy.module.system.web.form.LoginUser;

/**
 * 包含了两张表的操作：日志管理表，用户登录日志管理表
 * @author Administrator
 *
 */
@Service
@Transactional
public class LogServiceImpl implements ILogService {

	@Autowired
	private IBaseDao<LogEntity> basedaoLog;
	
	@Autowired
	private IBaseDao<LoginLogEntity> basedaoLoginLog;
	
	@Autowired
	private IDBUtilsHelper dbUtil ;
	
	@Override
	public void add(String title, String detail, String remark) {
		LoginSession currentUser = WebContextUtil.getCurrentUser() ;
		LoginUser user = currentUser.getUser();
		
		LogEntity entity = new LogEntity() ;
		entity.setUserId(user.getUser_id()) ;
		entity.setUserAccount(user.getAccount()) ;
		entity.setName(user.getEmp_name()) ;
		entity.setOperDateTime(new Date()) ;
		entity.setTitle(title) ;
		entity.setDetail(detail) ;
		entity.setRemark(remark) ;
		entity.setIp(IpUtil.getIpAddr(ContextHolderUtils.getRequest())) ;
		
		this.basedaoLog.add(entity) ;
	}
	@Override
	public void add(String title, String detail) {
		this.add(title, detail, null) ;
	}
	@Override
	public void add(String title) {
		this.add(title, null, null) ;
	}

	@Override
	public Msg delete(LogForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoLog.delete(LogEntity.class, id) ;
				}
				return new Msg(true, "删除成功！") ;
			} else {
				return new Msg(false, "删除失败！") ; 
			}
		} catch (BeansException e) {
			return new Msg(false, "删除失败！") ;
		}
	}
	
	@Override
	public LogForm get(LogForm form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select t from OperLogEntity t where 1=1";
		hql = addWhere(hql, form, params);
		
		LogEntity entity = (LogEntity) this.basedaoLog.queryObject(hql, params) ;
		if(null != entity) {
			LogForm pform = new LogForm();
			BeanUtils.copyProperties(entity, pform);
			
			return pform;
		} else {
			return null ;
		}
	}
	

	@Override
	public DataGrid datagrid(LogForm form) {
		if(null == form.getSort()) {
			SystemContext.setSort("t.created") ;
		} else {
			SystemContext.setSort("t."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
		}
		try {
			Pager<LogForm> pager = this.find(form) ;
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载列表信息异常" + e) ;
		}
	}


	private Pager<LogForm> find(LogForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "t.id, t.userId, t.userAccount, t.name, t.title, t.ip, " +
					 "t.detail, t.remark, t.operDateTime " +
				 	 "from ieasy_sys_log t " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		return this.basedaoLog.findSQL(sql, alias, LogForm.class, false) ;
	}

	private String addWhere(String hql, LogForm form, Map<String, Object> params) {
		if (form.getName() != null && !"".equals(form.getName())) {
			hql += " and t.name=:name";
			params.put("name", form.getName());
		}
		return hql;
	}

	
	
	/*********************************************************************************/
	
	@Override
	public Msg addLL(LoginLogForm form) {
		try {
			LoginLogEntity entity = new LoginLogEntity() ;
			
			BeanUtils.copyNotNullProperties(form, entity) ;
			
			this.basedaoLoginLog.add(entity) ;
		} catch (BeansException e) {
			return new Msg(false, "发生错误！") ;
		}
		return new Msg(false, "编辑失败！") ;
	}

	@Override
	public Msg deleteLL(LoginLogForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoLoginLog.delete(LoginLogEntity.class, id) ;
				}
				return new Msg(true, "删除成功！") ;
			} else {
				return new Msg(false, "删除失败！") ; 
			}
		} catch (BeansException e) {
			return new Msg(false, "删除失败！") ;
		}
	}
	
	@Override
	public LoginLogForm getLL(LoginLogForm form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select t from LoginLogEntity t where 1=1";
		hql = addWhere(hql, form, params);
		
		LoginLogEntity entity = (LoginLogEntity) this.basedaoLoginLog.queryObject(hql, params) ;
		if(null != entity) {
			LoginLogForm pform = new LoginLogForm();
			BeanUtils.copyProperties(entity, pform);
			
			return pform;
		} else {
			return null ;
		}
	}
	
	@Override
	public List<Long> loginTimeChartLL() {
		List<Long> l = new ArrayList<Long>();
		try {
			int k = 0;
			for (int i = 0; i < 12; i++) {
				String sql = "select count(*) as c from ieasy_sys_log_login t where HOUR(t.loginTime)>="+k+" and HOUR(t.loginTime)<"+(k+2) ;
				Map<String, Object> query = this.dbUtil.getQr().query(sql, new MapHandler()) ;
				l.add(Long.parseLong(query.get("c").toString())) ;
				k = k + 2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l ;
	}

	@Override
	public DataGrid datagridLL(LoginLogForm form) {
		
		if(null != SystemContext.getSort() && !"".equals(SystemContext.getSort())) {
			SystemContext.setSort("t.loginTime") ;SystemContext.setOrder("desc") ;
		}
		try {
			Pager<LoginLogForm> pager = this.find(form) ;
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			e.printStackTrace() ;
			throw new ServiceException("加载人员列表信息异常：" + e.getMessage()) ;
		}
	}


	private Pager<LoginLogForm> find(LoginLogForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "t.id, t.name, t.loginAccount, t.loginTime, t.ip, " +
					 "t.browserType, t.browserVersion, t.platformType, t.detail " +
				 	 "from ieasy_sys_log_login t " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		return this.basedaoLoginLog.findSQL(sql, alias, LoginLogForm.class, false) ;
	}

	private String addWhere(String hql, LoginLogForm form, Map<String, Object> params) {
		if (form.getLoginAccount() != null && !"".equals(form.getLoginAccount())) {
			hql += " and t.loginAccount=:loginAccount";
			params.put("loginAccount", form.getLoginAccount());
		}
		if (form.getName() != null && !"".equals(form.getName())) {
			hql += " and t.name=:name";
			params.put("name", form.getName());
		}
		return hql;
	}
}
