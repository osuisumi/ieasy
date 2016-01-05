/**
 * 
 */
package com.ieasy.module.system.service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.ClobUtil;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;
import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.MenuEntity;
import com.ieasy.module.system.entity.PermitsMenuEntity;
import com.ieasy.module.system.entity.PermitsOperEntity;
import com.ieasy.module.system.web.form.MenuForm;

/**
 * @author 杨浩泉
 *
 */
@Service("menuService") @Transactional
public class MenuService implements IMenuService {
	
	private static Logger LOG = Logger.getLogger(MenuService.class) ;

	@Inject
	private IBaseDao<MenuEntity> basedaoMenu ;
	
	@Inject
	private IBaseDao<PermitsMenuEntity> basedaoPermitsMenu;

	@Inject
	private IBaseDao<PermitsOperEntity> basedaoPermitsOper;
	
	@Inject
	private IDBUtilsHelper dbUtils;
	
	/* (non-Javadoc)
	 * @see com.ieasy.module.Menu.service.IMenuService#add(com.ieasy.module.Menu.web.form.MenuForm)
	 */
	@Override
	public Msg add(MenuForm form) {
		addResetSort(form.getPid()) ;
		
		MenuEntity entity = new MenuEntity() ;
		BeanUtils.copyNotNullProperties(form, entity, new String[]{"remark"}) ;
		entity.setRemark(ClobUtil.getClob(form.getRemark())) ;
		entity.setCreated(new Date()) ;
		
		if(null != form.getPid() && !"".equals(form.getPid().trim())) {
			entity.setMenu(this.basedaoMenu.load(MenuEntity.class, form.getPid())) ;
		}
		
		this.basedaoMenu.add(entity) ;
		return new Msg(true, "添加成功！");
	}

	/* (non-Javadoc)
	 * @see com.ieasy.module.Menu.service.IMenuService#delete(com.ieasy.module.Menu.web.form.MenuForm)
	 */
	@Override
	public Msg delete(MenuForm form) {
		MenuEntity entity = this.basedaoMenu.load(MenuEntity.class, form.getId()) ;
		int oldSort = entity.getSort() ;
		String pid = (null!=entity.getMenu()?entity.getMenu().getId():null) ;
		del(entity) ;
		
		delResetSort(oldSort, pid) ;
		return new Msg(true, "删除成功！");
	}
	
	private void del(MenuEntity entity) {
		if (entity.getMenus() != null && entity.getMenus().size() > 0) {
			for (MenuEntity e : entity.getMenus()) {
				del(e);
			}
		}
		
		//同步删除权限控制列表与之对应的菜单和操作资源
		if(entity.getType().equals("T") || entity.getType().equals("M")) {
			this.basedaoPermitsMenu.executeSQL("delete from ieasy_sys_permits_menu where menuId='" + entity.getId() + "'");
		}
		if(entity.getType().equals("O")) {
			this.basedaoPermitsOper.executeSQL("delete from ieasy_sys_permits_oper where operMenuId='" + entity.getId() + "'");
		}
					
		this.basedaoMenu.delete(MenuEntity.class, entity.getId()) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.module.Menu.service.IMenuService#update(com.ieasy.module.Menu.web.form.MenuForm)
	 */
	@Override
	public Msg update(MenuForm form) {
		MenuEntity entity = this.basedaoMenu.load(MenuEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(form, entity, new String[]{"remark"}) ;
		entity.setRemark(ClobUtil.getClob(form.getRemark())) ;
		entity.setModifyDate(new Date()) ;
		
		
		if(null != entity.getMenu() && !form.getPid().equals(entity.getMenu().getId())) {
			entity.setSort(getMM(form.getPid())[1]+1) ;
		}
		
		if (null != form.getPid() && !"".equals(form.getPid())) {
			if(!entity.getId().equals(form.getPid())) {
				entity.setMenu(this.basedaoMenu.load(MenuEntity.class, form.getPid()));
			} else {
				return new Msg(false, "操作有误，父模块服务关联自己！");
			}
		}
		this.basedaoMenu.update(entity) ;
		
		//同步修改权限控制列表与之对应的菜单和操作资源
		if(form.getType().equals("T") || form.getType().equals("M")) {
			try {
				String sql = "update ieasy_sys_permits_menu set " +
						"menuHref=?, menuName=?, menuIconCls=?, menuPid=?, " +
						"menuSort=?, state=? where menuId=? " ;
				this.dbUtils.getQr().update(sql, new Object[]{
						form.getHref(), form.getName(), form.getIconCls(), form.getPid(), entity.getSort(), form.getState(), form.getId()
				});
			} catch (SQLException e) {
				LOG.error("修改菜单并级联修改权限操作发生异常", e) ;
			}
		} else {
			try {
				String sql = "update ieasy_sys_permits_oper set " +
						"operMenuHref=?, operMenuName=?, operSort=? where operMenuId=? " ;
				this.dbUtils.getQr().update(sql, new Object[]{
						form.getHref(), form.getName(), entity.getSort(), form.getId()
				});
			} catch (SQLException e) {
				LOG.error("修改菜单并级联修改权限操作发生异常", e) ;
			}
		}
		
		return new Msg(true, "修改成功！");
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.module.Menu.service.IMenuService#get(com.ieasy.module.Menu.web.form.MenuForm)
	 */
	@Override
	public MenuForm get(MenuForm form) {
		MenuEntity entity = this.basedaoMenu.load(MenuEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(entity, form, new String[]{"remark"}) ;
		form.setRemark(ClobUtil.getString(entity.getRemark())) ;
		if (null != entity.getMenu()) {
			form.setPid(entity.getMenu().getId());
		}
		return form ;
	}
	

	/* (non-Javadoc)
	 * @see com.ieasy.module.Menu.service.IMenuService#treeAll(com.ieasy.module.Menu.web.form.MenuForm)
	 */
	@Override
	public List<MenuForm> tree(String pid) {
		String sql = "select t.* from ieasy_sys_menu t where t.pid is null order by t.sort asc" ;
		if(null != pid && !"".equals(pid.trim()))
			sql = "select t.* from ieasy_sys_menu t where t.id='"+pid+"'" ;
		
		List<MenuForm> list = this.basedaoMenu.listSQL(sql, MenuForm.class, false) ;
		List<MenuForm> forms = new ArrayList<MenuForm>() ;
		for (MenuForm e : list) {
			forms.add(recursive(e));
		}
		return forms;
	}

	private MenuForm recursive(MenuForm form) {
		List<MenuForm> menus = this.basedaoMenu.listSQL("select t.* from ieasy_sys_menu t where t.pid='"+form.getId()+"' order by t.sort asc", MenuForm.class, false) ;
		if(null != menus && menus.size() > 0) {
			List<MenuForm> chlds = new ArrayList<MenuForm>() ;
			for(MenuForm e : menus) {
				MenuForm recursive = this.recursive(e) ;
				chlds.add(recursive) ;
			}
			form.setChildren(chlds) ;
		}
		return form ;
	}

	@Override
	public List<MenuForm> combo(MenuForm form) {
		String sql = "select t.* from ieasy_sys_menu t where t.pid is null order by t.sort asc" ;
		if(null != form.getPid() && !"".equals(form.getPid().trim()))
			sql = "select t.* from ieasy_sys_menu t where t.id='"+form.getPid()+"'" ;
		
		List<MenuForm> list = this.basedaoMenu.listSQL(sql, MenuForm.class, false) ;
		List<MenuForm> forms = new ArrayList<MenuForm>() ;
		for (MenuForm e : list) {
			forms.add(recursiveCombo(e));
		}
		return forms;
	}
	

	private MenuForm recursiveCombo(MenuForm form) {
		form.setText(form.getName()) ;
		List<MenuForm> menus = this.basedaoMenu.listSQL("select t.* from ieasy_sys_menu t where t.pid='"+form.getId()+"' order by t.sort asc", MenuForm.class, false) ;
		if(null != menus && menus.size() > 0) {
			List<MenuForm> chlds = new ArrayList<MenuForm>() ;
			for(MenuForm e : menus) {
				MenuForm recursive = this.recursiveCombo(e) ;
				chlds.add(recursive) ;
			}
			form.setChildren(chlds) ;
		}
		return form ;
	}
	

	
	@Override
	public void exportTree() {
		FileUtils.WriteJSON(WebContextUtil.getSc().getRealPath("/static_res") +File.separator+ "menu.tree.json", tree(null)) ;
	}
	
	@Override
	public Msg isShow(MenuForm form) {
		try {
			this.basedaoMenu.updateByHql("update MenuEntity t set t.isShow=? where t.id=?", new Object[]{form.getIsShow(), form.getId()}) ;
			return new Msg(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace() ;
			return new Msg(false, "修改失败！");
		}
	}
	
	
	/**
	 * 更新位置
	 * @param pid
	 */
	public void updateResetSort(int oldSort, int newSort, String id, String pid) {
		try {
			if(oldSort == newSort) {return;}
			if(oldSort != newSort) {
				if(null == pid || "".equals(pid.trim())) {
					Object[] obj = this.basedaoMenu.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_menu t where t.pid is null") ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.menu.id is null", new Object[]{oldSort, newSort}) ;
					} else {
						this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.menu.id is null", new Object[]{oldSort, newSort}) ;
					}
				} else {
					Object[] obj = this.basedaoMenu.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_menu t where t.pid=?", new Object[]{pid}) ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.menu.id=?", new Object[]{oldSort, newSort, pid}) ;
					} else {
						this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.menu.id=?", new Object[]{oldSort, newSort, pid}) ;
					}
				}
				this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=? where t.id=?", new Object[]{newSort, id}) ;
			}
		} catch (Exception e) {
			LOG.error("修改排序序号失败：原因：", e) ;
			e.printStackTrace() ;
		}
	}
	
	/**
	 * 获取排序号最小值，最大值
	 * @param pid
	 * @return
	 */
	private Integer[] getMM(String pid) { 
		Integer[] mm = new Integer[2] ;
		if(null == pid || "".equals(pid.trim())) {
			Object[] obj = this.basedaoMenu.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_menu t where t.pid is null") ;
			mm[0] = (int)(obj[0] == null?0:obj[0]) ;
			mm[1] = (int)(obj[1] == null?0:obj[1]) ;
		} else {
			Object[] obj = this.basedaoMenu.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_menu t where t.pid=?", new Object[]{pid}) ;
			mm[0] = (int)(obj[0] == null?0:obj[0]) ;
			mm[1] = (int)(obj[1] == null?0:obj[1]) ;
		}
		return mm;
	}
	
	/**
	 * 添加数据，先将全部元素+1
	 * @param pid
	 */
	private void addResetSort(String pid) {
		if(null == pid || "".equals(pid.trim())) {
			this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort+1 where t.sort>=1 and t.menu.id is null") ;
		} else {
			this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort+1 where t.sort>=1 and t.menu.id=?", new Object[]{pid}) ;
		}
	}
	
	/**
	 * 删除数据，大于原位置的元素全部-1
	 * @param pid
	 */
	private void delResetSort(int oldSort, String pid) {
		if(null == pid || "".equals(pid.trim())) {
			this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort-1 where t.sort>? and t.menu.id is null", new Object[]{oldSort}) ;
		} else {
			this.basedaoMenu.updateByHql("update MenuEntity t set t.sort=t.sort-1 where t.sort>? and t.menu.id=?", new Object[]{oldSort, pid}) ;
		}
	}

	@Override
	public List<MenuForm> getAllMenuTree(MenuForm form) {
		String sql = "select t.* from ieasy_sys_menu t where t.pid is null order by t.sort asc" ;
		List<MenuForm> list = this.basedaoMenu.listSQL(sql, MenuForm.class, false) ;
		
		List<MenuForm> forms = new ArrayList<MenuForm>() ;
		for (MenuForm e : list) {
			forms.add(allMenuNodes(e));
		}
		return forms;
	}
	
	private MenuForm allMenuNodes(MenuForm form) {
		//form.setState("open") ;
		List<MenuForm> menus = this.basedaoMenu.listSQL("select t.* from ieasy_sys_menu t where t.pid='"+form.getId()+"' order by t.sort asc", MenuForm.class, false) ;
		if(null != menus && menus.size() > 0) {
			
			List<MenuForm> chlds = new ArrayList<MenuForm>() ;
			List<MenuForm> opers = new ArrayList<MenuForm>() ;
			for(MenuForm e : menus) {
				if("O".equals(e.getType())) {
					opers.add(e) ;
					form.setOpers(opers) ;
					continue ;
				}
				MenuForm recursive = this.allMenuNodes(e) ;
				chlds.add(recursive) ;
			}
			
			form.setChildren(chlds) ;
		}
		return form ;
	}

	@Override
	public void init(MenuEntity entity) {
		if(null != entity.getMenu()) {
			addResetSort(entity.getMenu().getId()) ;
		}
		entity.setCreated(new Date()) ;
		
		this.basedaoMenu.add(entity) ;
	}
}
