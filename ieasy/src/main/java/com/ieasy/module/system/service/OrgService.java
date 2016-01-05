/**
 * 
 */
package com.ieasy.module.system.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieasy.basic.dao.IBaseDao;
import com.ieasy.basic.exception.ServiceException;
import com.ieasy.basic.model.Msg;
import com.ieasy.basic.util.BeanUtils;
import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.OrgEntity;
import com.ieasy.module.system.web.form.OrgForm;

/**
 * @author 杨浩泉
 *
 */
@Service("orgService") @Transactional
public class OrgService implements IOrgService {

	private static Logger logger = Logger.getLogger(OrgService.class) ;
	
	@Inject
	private IBaseDao<OrgEntity> basedaoOrg ;
	
	@Override
	public Msg add(OrgForm form) {
		addResetSort(form.getPid()) ;
		
		if(this.getBySn(form.getSn()) != null)
			throw new ServiceException("该机构或部门的SN["+form.getSn()+"]已存在，无法添加！") ;
		
		OrgEntity entity = new OrgEntity() ;
		entity.setSort(1) ;
		entity.setCreated(new Date()) ;
		
		BeanUtils.copyNotNullProperties(form, entity) ;
		
		if (form.getPid() != null && !"".equalsIgnoreCase(form.getPid())) {
			entity.setOrg(this.basedaoOrg.load(OrgEntity.class, form.getPid())) ;
		}
		
		this.basedaoOrg.add(entity) ;
		return new Msg(true, "添加成功！");
	}
	
	@Override
	public Msg delete(OrgForm form) {
		OrgEntity entity = this.basedaoOrg.load(OrgEntity.class, form.getId()); ;
		int oldSort = entity.getSort() ;
		String pid = (null!=entity.getOrg()?entity.getOrg().getId():null) ;
		del(entity) ;
		
		delResetSort(oldSort, pid) ;
		return new Msg(true, "删除成功！");
	}
	
	private void del(OrgEntity entity) {
		if (entity.getOrgs() != null && entity.getOrgs().size() > 0) {
			for (OrgEntity e : entity.getOrgs()) {
				del(e);
			}
		}
		this.basedaoOrg.delete(OrgEntity.class, entity.getId()) ;
	}

	@Override
	public Msg update(OrgForm form) {
		OrgEntity entity = this.basedaoOrg.load(OrgEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(form, entity) ;
		entity.setModifyDate(new Date()) ;
		
		if(null != entity.getOrg() && !form.getPid().equals(entity.getOrg().getId())) {
			entity.setSort(getMM(form.getPid())[1]+1) ;
		}
		if (null != form.getPid() && !"".equals(form.getPid())) {
			if(!entity.getId().equals(form.getPid())) {
				entity.setOrg(this.basedaoOrg.load(OrgEntity.class, form.getPid()));
			} else {
				return new Msg(false, "操作有误，父模块服务关联自己！");
			}
		}
		this.basedaoOrg.update(entity) ;
		
		return new Msg(true, "修改成功！");
	}
	
	@Override
	public OrgForm get(OrgForm form) {
		OrgEntity entity = this.basedaoOrg.load(OrgEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(entity, form) ;
		if (null != entity.getOrg()) {
			form.setPid(entity.getOrg().getId());
		}
		return form ;
	}

	@Override
	public List<OrgForm> tree(String pid) {
		String sql = "select t.* from ieasy_sys_org t where t.pid is null order by t.sort asc" ;
		if(null != pid && !"".equals(pid.trim()))
			sql = "select t.* from ieasy_sys_org t where t.id='"+pid+"'" ;
		
		List<OrgForm> list = this.basedaoOrg.listSQL(sql, OrgForm.class, false) ;
		List<OrgForm> forms = new ArrayList<OrgForm>() ;
		for (OrgForm e : list) {
			forms.add(recursive(e));
		}
		FileUtils.WriteJSON(WebContextUtil.getSc().getRealPath("/static_res") +File.separator+ "org.tree.json", forms) ;
		return forms;
	}
	
	@Override
	public List<OrgForm> syncTree(String pid) {
		String sql = "select t.* from ieasy_sys_org t where t.pid is null order by t.sort asc" ;
		if(null != pid && !"".equals(pid.trim()))
			sql = "select t.* from ieasy_sys_org t where t.id='"+pid+"'" ;
		
		List<OrgForm> list = this.basedaoOrg.listSQL(sql, OrgForm.class, false) ;
		List<OrgForm> forms = new ArrayList<OrgForm>() ;
		for (OrgForm e : list) {
			forms.add(recursive(e));
		}
		return forms;
	}

	public OrgForm recursive(OrgForm form) {
		form.setText(form.getName()) ;
		List<OrgForm> orgs = this.basedaoOrg.listSQL("select t.* from ieasy_sys_org t where t.pid='"+form.getId()+"' order by t.sort asc", OrgForm.class, false) ;
		if(null != orgs && orgs.size() > 0) {
			
			List<OrgForm> chlds = new ArrayList<OrgForm>() ;
		
			for(OrgForm e : orgs) {
				OrgForm recursive = this.recursive(e) ;
				
				OrgForm childform = new OrgForm() ;
				BeanUtils.copyNotNullProperties(recursive, childform) ;
				chlds.add(childform) ;
			}
			
			form.setChildren(chlds) ;
		}
		
		return form ;
	}
	
	private String getBySn(String sn) {
		return (String) this.basedaoOrg.queryObject("select t.sn from OrgEntity t where t.sn=?", new Object[]{sn}) ;
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
					Object[] obj = this.basedaoOrg.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_org t where t.pid is null") ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.org.id is null", new Object[]{oldSort, newSort}) ;
					} else {
						this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.org.id is null", new Object[]{oldSort, newSort}) ;
					}
				} else {
					Object[] obj = this.basedaoOrg.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_org t where t.pid=?", new Object[]{pid}) ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.org.id=?", new Object[]{oldSort, newSort, pid}) ;
					} else {
						this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.org.id=?", new Object[]{oldSort, newSort, pid}) ;
					}
				}
				this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=? where t.id=?", new Object[]{newSort, id}) ;
			}
		} catch (Exception e) {
			logger.error("修改排序序号失败：原因：", e) ;
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
			Object[] obj = this.basedaoOrg.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_org t where t.pid is null") ;
			mm[0] = (int)(obj[0] == null?0:obj[0]) ;
			mm[1] = (int)(obj[1] == null?0:obj[1]) ;
		} else {
			Object[] obj = this.basedaoOrg.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_org t where t.pid=?", new Object[]{pid}) ;
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
			this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort+1 where t.sort>=1 and t.org.id is null") ;
		} else {
			this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort+1 where t.sort>=1 and t.org.id=?", new Object[]{pid}) ;
		}
	}
	
	/**
	 * 删除数据，大于原位置的元素全部-1
	 * @param pid
	 */
	private void delResetSort(int oldSort, String pid) {
		if(null == pid || "".equals(pid.trim())) {
			this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort-1 where t.sort>? and t.org.id is null", new Object[]{oldSort}) ;
		} else {
			this.basedaoOrg.updateByHql("update OrgEntity t set t.sort=t.sort-1 where t.sort>? and t.org.id=?", new Object[]{oldSort, pid}) ;
		}
	}
	
	@Override
	public void init(OrgEntity entity) {
		if(this.getBySn(entity.getSn()) != null)
			throw new ServiceException("该机构或部门的SN["+entity.getName()+"-"+entity.getSn()+"]已存在，无法添加！") ;
		
		if(null != entity.getOrg()) {
			addResetSort(entity.getOrg().getId()) ;
		}
		entity.setSumJdl(false) ;
		entity.setSort(1) ;
		entity.setCreated(new Date()) ;
		entity.setCreateName("系统初始化") ;
		
		this.basedaoOrg.add(entity) ;
	}

}
