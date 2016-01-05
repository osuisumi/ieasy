package com.ieasy.module.common.init;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.util.MD5Util;
import com.ieasy.module.system.entity.MenuEntity;
import com.ieasy.module.system.entity.PermitsMenuEntity;
import com.ieasy.module.system.entity.PermitsOperEntity;
import com.ieasy.module.system.entity.PersonEntity;
import com.ieasy.module.system.entity.RoleEntity;
import com.ieasy.module.system.entity.UserEntity;
import com.ieasy.module.system.web.form.MenuForm;

@Service @Transactional
public class InitAdminService implements IInitAdminService {
	
	@Inject
	private IBaseDao<MenuEntity> basedaoMenu ;
	
	@Inject
	private IBaseDao<RoleEntity> basedaoRole ;
	
	@Inject
	private IBaseDao<PersonEntity> basedaoPerson ;
	
	@Inject
	private IBaseDao<UserEntity> basedaoUser ;
	
	@Inject
	private IBaseDao<PermitsMenuEntity> basedaoPermitsMenu;

	@Inject
	private IBaseDao<PermitsOperEntity> basedaoPermitsOper;
	
	@Override
	public void addInitAdmin() {
		// 删除所有的权限
		this.basedaoPermitsOper.executeSQL("delete from ieasy_sys_permits_oper");
		this.basedaoPermitsMenu.executeSQL("delete from ieasy_sys_permits_menu");

					
		//获取ROOT角色
		RoleEntity role = (RoleEntity) this.basedaoRole.queryObjectSQL("select t.* from ieasy_sys_role t where t.sn=?", "ADMIN", RoleEntity.class, true) ;
		if(null == role) 
			throw new ServiceException("找不到超级管理角色[ROOT]，无法完成角色授权和创建超级管理员账号！") ;
		
		//为ROOT角色授权
		getAllMenuTree(role.getId(), "ROLE") ;
		
		PersonEntity p = new PersonEntity() ;
		p.setNum("ROOT") ;
		p.setName("超级管理员") ;
		p.setSex("男") ;
		p.setArchivesStatus(1) ;
		p.setEmpState("在职") ;
		p.setEmail("yanghaoquan@whizen.com") ;
		p.setMobile("13143536661") ;
		p.setCreateName("系统初始化") ;
		p.setCreated(new Date()) ;
		this.basedaoPerson.add(p) ;
		
		Set<RoleEntity> roles = new HashSet<RoleEntity>() ;
		roles.add(role) ;
		
		UserEntity u = new UserEntity() ;
		u.setAccount("admin") ;
		u.setPassword(MD5Util.md5("")) ;
		u.setStatus(0) ;
		u.setCreateName("系统初始化") ;
		u.setCreated(new Date()) ;
		u.setRoles(roles) ;
		u.setEmp(p) ;
		
		
		this.basedaoUser.add(u) ;
	}
	
	public List<MenuForm> getAllMenuTree(String principalId, String principalType) {
		String sql = "select t.* from ieasy_sys_menu t where t.pid is null order by t.sort asc" ;
		List<MenuForm> list = this.basedaoMenu.listSQL(sql, MenuForm.class, false) ;
		
		List<MenuForm> forms = new ArrayList<MenuForm>() ;
		for (MenuForm e : list) {
			forms.add(allMenuNodes(principalId, principalType, e));
		}
		return forms;
	}
	
	/**
	 * @param principalId
	 * @param principalType
	 * @param form
	 * @return
	 */
	private MenuForm allMenuNodes(String principalId, String principalType, MenuForm form) {
		PermitsMenuEntity pmEntity = new PermitsMenuEntity();
		pmEntity.setMenuId(form.getId()) ;
		pmEntity.setMenuHref(form.getHref()) ;
		pmEntity.setMenuName(form.getName()) ;
		pmEntity.setMenuPid(form.getPid()) ;
		pmEntity.setMenuSort(form.getSort()) ;
		pmEntity.setState(form.getState()) ;
		pmEntity.setMenuIconCls(form.getIconCls()) ;
		pmEntity.setPrincipalId(principalId) ;
		pmEntity.setPrincipalType(principalType) ;
		this.basedaoPermitsMenu.add(pmEntity) ;
		
		List<MenuForm> menus = this.basedaoMenu.listSQL("select t.* from ieasy_sys_menu t where t.pid='"+form.getId()+"' order by t.sort asc", MenuForm.class, false) ;
		if(null != menus && menus.size() > 0) {
			
			List<MenuForm> chlds = new ArrayList<MenuForm>() ;
			for(MenuForm e : menus) {
				if("O".equals(e.getType())) {
					PermitsOperEntity poEntity = new PermitsOperEntity();
					poEntity.setPrincipalId(principalId) ;
					poEntity.setPrincipalType(principalType) ;
					poEntity.setOperMenuHref(e.getHref()) ;
					poEntity.setOperMenuId(e.getId()) ;
					poEntity.setOperMenuName(e.getName()) ;
					poEntity.setOperIconCls(e.getIconCls()) ;
					poEntity.setOperSort(e.getSort()) ;
					poEntity.setState(e.getState()) ;
					poEntity.setPermitsMenu(pmEntity) ;
					this.basedaoPermitsOper.add(poEntity) ;
					
					continue ;
				}
				MenuForm recursive = this.allMenuNodes(principalId, principalType, e) ;
				chlds.add(recursive) ;
			}
			
			form.setChildren(chlds) ;
		}
		return form ;
	}
	
}
