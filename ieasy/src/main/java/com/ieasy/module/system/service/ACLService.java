package com.ieasy.module.system.service;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.module.system.entity.PermitsMenuEntity;
import com.ieasy.module.system.entity.PermitsOperEntity;
import com.ieasy.module.system.web.form.ACLForm;

@Service
@Transactional
public class ACLService implements IACLService {

	@Inject
	private IBaseDao<PermitsMenuEntity> basedaoPermitsMenu;

	@Inject
	private IBaseDao<PermitsOperEntity> basedaoPermitsOper;

	@Override
	public Msg grantPermits(ACLForm form) {

		// 获取主体
		List<ACLForm> principals = JSON.parseArray(form.getPrincipals(), ACLForm.class);
		for (ACLForm p : principals) {
			// 删除原有的权限
			this.basedaoPermitsOper.executeSQL("delete from ieasy_sys_permits_oper where principalId='" + p.getPrincipalId() + "' and principalType='" + p.getPrincipalType() + "'");
			this.basedaoPermitsMenu.executeSQL("delete from ieasy_sys_permits_menu where principalId='" + p.getPrincipalId() + "' and principalType='" + p.getPrincipalType() + "'");

			// 获取许可菜单资源
			List<ACLForm> resources = JSON.parseArray(form.getResources(), ACLForm.class);
			for (ACLForm r : resources) {
				r.setPrincipalId(p.getPrincipalId());
				r.setPrincipalType(p.getPrincipalType());
				PermitsMenuEntity pmEntity = new PermitsMenuEntity();
				BeanUtils.copyNotNullProperties(r, pmEntity);

				// 保存许可菜单资源
				this.basedaoPermitsMenu.add(pmEntity);

				// 获取许可的操作资源
				List<ACLForm> opers = r.getOpers();
				for (ACLForm o : opers) {
					PermitsOperEntity poEntity = new PermitsOperEntity();
					BeanUtils.copyNotNullProperties(o, poEntity);
					poEntity.setPrincipalId(p.getPrincipalId());
					poEntity.setPrincipalType(p.getPrincipalType());
					poEntity.setPermitsMenu(pmEntity);

					// 保存许可的操作资源
					this.basedaoPermitsOper.add(poEntity);
				}
			}
		}

		return new Msg(true, "授权成功！");
	}

	@Override
	public ACLForm getPermits(ACLForm form) {
		ACLForm aclPermits = new ACLForm() ;
		
		String sqlMenus = "select t.menuId from ieasy_sys_permits_menu t where t.principalId=? and t.principalType=?" ;
		List<ACLForm> menuList = this.basedaoPermitsMenu.listSQL(sqlMenus, new Object[]{form.getPrincipalId(), form.getPrincipalType()}, ACLForm.class, false) ;
		
		String sqlOpers = "select t.operMenuId from ieasy_sys_permits_oper t where t.principalId=? and t.principalType=?" ;
		List<ACLForm> operList = this.basedaoPermitsOper.listSQL(sqlOpers, new Object[]{form.getPrincipalId(), form.getPrincipalType()}, ACLForm.class, false) ;
		
		aclPermits.setMenus(menuList) ;
		aclPermits.setOpers(operList) ;
		
		return aclPermits;
	}

}
