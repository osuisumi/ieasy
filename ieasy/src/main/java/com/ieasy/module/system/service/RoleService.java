/**
 * 
 */
package com.ieasy.module.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.model.Pager;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.ClobUtil;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.module.common.service.BaseService;
import com.ieasy.module.system.entity.MenuEntity;
import com.ieasy.module.system.entity.RoleEntity;
import com.ieasy.module.system.web.form.RoleForm;

/**
 * @author 杨浩泉
 *
 */
@Service("roleService") @Transactional
public class RoleService extends BaseService implements IRoleService {
	
	private Logger logger = Logger.getLogger(RoleService.class) ;

	@Inject
	private IBaseDao<RoleEntity> basedaoRole ;
	
	@Inject
	private IBaseDao<MenuEntity> basedaoMenu ;
	
	@Inject
	private IDBUtilsHelper dbutil ;
	
	@Override
	public Msg add(RoleForm form) {
		try {
			if(this.getBySn(form.getSn()) != null)
				return new Msg(false, "该的角色的SN["+form.getSn()+"]已存在，无法添加！") ;
			
			//修改默认角色
			if(form.getDefaultRole() == 1) {
				this.dbutil.getQr().update("update ieasy_sys_role set defaultRole=0 where defaultRole=1") ;
			}
			
			RoleEntity entity = new RoleEntity() ;
			BeanUtils.copyNotNullProperties(form, entity, new String[]{"remark"}) ;
			entity.setRemark(ClobUtil.getClob(form.getRemark())) ;
			entity.setCreated(new Date()) ;
			entity.setCreateName(this.getCurrentUser().getEmp_name()) ;
			
			this.basedaoRole.add(entity) ;
			
			return new Msg(true, "添加成功！");
		} catch (Throwable e) {
			logger.error("添加角色信息失败===>异常信息：", e) ;
			return new Msg(false, "添加失败！") ;
		}
	}

	@Override
	public Msg delete(RoleForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoRole.delete(RoleEntity.class, id) ;
				}
				return new Msg(true, "删除成功！") ;
			}
		} catch (Exception e) {
			logger.error("根据ID["+form.getIds()+"]删除角色信息失败===>异常信息：", e) ;
			return new Msg(false, "删除失败！") ;
		}
		return null;
	}

	@Override
	public Msg update(RoleForm form) {
		try {
			//修改默认角色
			if(form.getDefaultRole() == 1) {
				this.dbutil.getQr().update("update ieasy_sys_role set defaultRole=0 where defaultRole=1") ;
			}
			
			RoleEntity entity = this.basedaoRole.load(RoleEntity.class, form.getId()) ;
			BeanUtils.copyNotNullProperties(form, entity, new String[]{"remark"}) ;
			entity.setModifyDate(new Date()) ;
			entity.setRemark(ClobUtil.getClob(form.getRemark())) ;
			entity.setModifyDate(new Date()) ;
			entity.setModifyName(this.getCurrentUser().getEmp_name()) ;
			
			this.basedaoRole.update(entity) ;
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			logger.error("修改角色信息失败===>异常信息：", e) ;
			return new Msg(false, "修改角色信息失败！") ;
		}
	}
	
	@Override
	public RoleForm get(RoleForm form) {
		try {
			RoleEntity entity = this.basedaoRole.load(RoleEntity.class, form.getId()) ;
			BeanUtils.copyNotNullProperties(entity, form, new String[]{"remark"}) ;
			form.setRemark(ClobUtil.getString(entity.getRemark())) ;
			
			return form ;
		} catch (Exception e) {
			logger.error("加载角色信息失败===>异常信息：", e) ;
		}
		return null ;
	}

	@Override
	public DataGrid datagrid(RoleForm form) {
		try {
			List<RoleForm> forms = new ArrayList<RoleForm>() ;
			Pager<RoleForm> pager = this.find(form) ;
			if (null != pager && !pager.getDataRows().isEmpty()) {
				for(RoleForm pf : pager.getDataRows()) {
					forms.add(pf) ;
				}
			}
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			logger.error("加载角色列表信息失败===>异常信息：", e) ;
		}
		return null;
	}
	
	private Pager<RoleForm> find(RoleForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select t.* from ieasy_sys_role t where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		
		return this.basedaoRole.findSQL(sql, alias, RoleForm.class, false) ;
	}
	
	private String addWhere(String sql, RoleForm form, Map<String, Object> params) {
		if (null != form) {
			if (form.getSn() != null && !"".equals(form.getSn().trim())) {
				sql += " and t.sn like :sn";
				params.put("sn", "%%" + form.getSn() + "%%");
			}
			if (form.getName() != null && !"".equals(form.getName().trim())) {
				sql += " and t.name like :name";
				params.put("name", "%%" + form.getName() + "%%");
			}
		}
		return sql;
	}
	
	@Override
	public RoleForm getRoleMenus(RoleForm form) {
		String sql = "select t.menu_id from ieasy_sys_role_menus t where t.role_id=?" ;
		
		List<Object[]> menuIds = this.basedaoRole.listSQL(sql, form.getId()) ;
		if(null != menuIds && menuIds.size() > 0) {
			form.setMenu_ids(StringUtils.join(menuIds.toArray(), ",")) ;
		}
		return form ;
	}
	
	/**
	 * 检索数据是否存在
	 * int equalsName = this.equlasVal("name='"+form.getName()+"'") ;
	 * if(equalsName == 1) return new Msg(false, "该角色已存在！") ;
	 * @param param
	 * @return
	 */
	/*private int equlasVal(String param) {
		String sql = "select t.* from ieasy_sys_role t where " + param;
		return this.basedaoRole.countSQL(sql, false).intValue() ;
	}*/

	private String getBySn(String sn) {
		return (String) this.basedaoRole.queryObject("select t.sn from RoleEntity t where t.sn=?", new Object[]{sn}) ;
	}
	
	@Override
	public void init(RoleEntity entity) {
		if(this.getBySn(entity.getSn()) != null)
			throw new ServiceException("该的角色的SN["+entity.getName()+"-"+entity.getSn()+"]已存在，无法添加！") ;
		
		entity.setCreated(new Date()) ;
		entity.setCreateName("系统初始化") ;
		
		this.basedaoRole.add(entity) ;
	}

}
