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
import com.ieasy.basic.util.PinyinUtil;
import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.PositionEntity;
import com.ieasy.module.system.web.form.PositionForm;

/**
 * @author 杨浩泉
 *
 */
@Service("positionService") @Transactional
public class PositionService implements IPositionService {
	
	private static Logger logger = Logger.getLogger(PositionService.class) ;

	@Inject
	private IBaseDao<PositionEntity> basedaoPos ;
	
	@Override
	public Msg add(PositionForm form) {
		addResetSort(form.getPid()) ;
		PositionEntity entity = new PositionEntity() ;
		entity.setSort(1) ;
		entity.setCreated(new Date()) ;
		
		BeanUtils.copyNotNullProperties(form, entity) ;
		
		if (form.getPid() != null && !"".equalsIgnoreCase(form.getPid())) {
			entity.setPosition(this.basedaoPos.load(PositionEntity.class, form.getPid())) ;
		}
		
		this.basedaoPos.add(entity) ;
		return new Msg(true, "添加成功！");
	}

	@Override
	public Msg delete(PositionForm form) {
		PositionEntity entity = this.basedaoPos.load(PositionEntity.class, form.getId()); ;
		int oldSort = entity.getSort() ;
		String pid = (null!=entity.getPosition()?entity.getPosition().getId():null) ;
		del(entity) ;
		
		delResetSort(oldSort, pid) ;
		
		
		
		return new Msg(true, "删除成功！");
	}
	
	private void del(PositionEntity entity) {
		if (entity.getPositions() != null && entity.getPositions().size() > 0) {
			for (PositionEntity e : entity.getPositions()) {
				del(e);
			}
		}
		this.basedaoPos.delete(PositionEntity.class, entity.getId()) ;
	}

	@Override
	public Msg update(PositionForm form) {
		PositionEntity entity = this.basedaoPos.load(PositionEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(form, entity) ;
		entity.setModifyDate(new Date()) ;
		
		if(null != entity.getPosition() && !form.getPid().equals(entity.getPosition().getId())) {
			entity.setSort(getMM(form.getPid())[1]+1) ;
		}
		if (null != form.getPid() && !"".equals(form.getPid())) {
			if(!entity.getId().equals(form.getPid())) {
				entity.setPosition(this.basedaoPos.load(PositionEntity.class, form.getPid()));
			} else {
				return new Msg(false, "操作有误，父模块服务关联自己！");
			}
		}
		this.basedaoPos.update(entity) ;
		
		return new Msg(true, "修改成功！");
	}
	
	@Override
	public PositionForm get(PositionForm form) {
		PositionEntity entity = this.basedaoPos.load(PositionEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(entity, form) ;
		if (null != entity.getPosition()) {
			form.setPid(entity.getPosition().getId());
		}
		return form ;
	}

	@Override
	public List<PositionForm> tree(String pid) {
		String sql = "select t.* from ieasy_sys_position t where t.pid is null order by t.sort asc" ;
		if(null != pid && !"".equals(pid.trim()))
			sql = "select t.* from ieasy_sys_position t where t.id='"+pid+"'" ;
		
		List<PositionForm> list = this.basedaoPos.listSQL(sql, PositionForm.class, false) ;
		List<PositionForm> forms = new ArrayList<PositionForm>() ;
		for (PositionForm e : list) {
			forms.add(recursive(e));
		}
		FileUtils.WriteJSON(WebContextUtil.getSc().getRealPath("/static_res") +File.separator+ "position.tree.json", forms) ;
		return forms;
	}

	public PositionForm recursive(PositionForm form) {
		form.setText(form.getName()) ;
		List<PositionForm> orgs = this.basedaoPos.listSQL("select t.* from ieasy_sys_position t where t.pid='"+form.getId()+"' order by t.sort asc", PositionForm.class, false) ;
		if(null != orgs && orgs.size() > 0) {
			
			List<PositionForm> chlds = new ArrayList<PositionForm>() ;
		
			for(PositionForm e : orgs) {
				PositionForm recursive = this.recursive(e) ;
				
				PositionForm childform = new PositionForm() ;
				BeanUtils.copyNotNullProperties(recursive, childform) ;
				chlds.add(childform) ;
			}
			
			form.setChildren(chlds) ;
		}
		
		return form ;
	}
	
	private String getBySn(String sn) {
		return (String) this.basedaoPos.queryObject("select t.sn from PositionEntity t where t.sn=?", new Object[]{sn}) ;
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
					Object[] obj = this.basedaoPos.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_position t where t.pid is null") ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.position.id is null", new Object[]{oldSort, newSort}) ;
					} else {
						this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.position.id is null", new Object[]{oldSort, newSort}) ;
					}
				} else {
					Object[] obj = this.basedaoPos.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_position t where t.pid=?", new Object[]{pid}) ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.position.id=?", new Object[]{oldSort, newSort, pid}) ;
					} else {
						this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.position.id=?", new Object[]{oldSort, newSort, pid}) ;
					}
				}
				this.basedaoPos.updateByHql("update PositionEntity t set t.sort=? where t.id=?", new Object[]{newSort, id}) ;
				
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
			Object[] obj = this.basedaoPos.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_position t where t.pid is null") ;
			mm[0] = (int)(obj[0] == null?0:obj[0]) ;
			mm[1] = (int)(obj[1] == null?0:obj[1]) ;
		} else {
			Object[] obj = this.basedaoPos.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_position t where t.pid=?", new Object[]{pid}) ;
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
			this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort+1 where t.sort>=1 and t.position.id is null") ;
		} else {
			this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort+1 where t.sort>=1 and t.position.id=?", new Object[]{pid}) ;
		}
	}
	
	/**
	 * 删除数据，大于原位置的元素全部-1
	 * @param pid
	 */
	private void delResetSort(int oldSort, String pid) {
		if(null == pid || "".equals(pid.trim())) {
			this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort-1 where t.sort>? and t.position.id is null", new Object[]{oldSort}) ;
		} else {
			this.basedaoPos.updateByHql("update PositionEntity t set t.sort=t.sort-1 where t.sort>? and t.position.id=?", new Object[]{oldSort, pid}) ;
		}
	}

	@Override
	public void init(PositionEntity entity) {
		if(this.getBySn(entity.getSn()) != null)
			throw new ServiceException("该机岗位的SN["+entity.getName()+"-"+entity.getSn()+"]已存在，无法添加！") ;
		
		if(null != entity.getPosition()) {
			addResetSort(entity.getPosition().getId()) ;
		}
		entity.setSn(PinyinUtil.getPinYinHeadChar(entity.getName()).toUpperCase()) ;
		entity.setSort(1) ;
		entity.setCreated(new Date()) ;
		entity.setCreateName("系统初始化") ;
		this.basedaoPos.add(entity) ;
	}
	
}
