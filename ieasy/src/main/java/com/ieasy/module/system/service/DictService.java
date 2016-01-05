/**
 * 
 */
package com.ieasy.module.system.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ieasy.module.system.entity.DictEntity;
import com.ieasy.module.system.web.form.DictForm;
import com.ieasy.module.system.web.form.DictJson;

/**
 * 人力资源（数据字典）
 * @author 杨浩泉
 *
 */
@Service("dictService") @Transactional
public class DictService implements IDictService {

	private static Logger logger = Logger.getLogger(DictService.class) ;
	
	@Inject
	private IBaseDao<DictEntity> basedaoDict ;
	
	@Override
	public Msg add(DictForm form) {
		addResetSort(form.getPid()) ;
		DictEntity entity = new DictEntity() ;
		entity.setSort(1) ;
		entity.setCreated(new Date()) ;
		
		BeanUtils.copyNotNullProperties(form, entity) ;
		
		if (form.getPid() != null && !"".equalsIgnoreCase(form.getPid())) {
			entity.setDict(this.basedaoDict.load(DictEntity.class, form.getPid())) ;
		}
		
		this.basedaoDict.add(entity) ;
		return new Msg(true, "添加成功！");
	}

	@Override
	public Msg delete(DictForm form) {
		DictEntity entity = this.basedaoDict.load(DictEntity.class, form.getId()); ;
		int oldSort = entity.getSort() ;
		String pid = (null!=entity.getDict()?entity.getDict().getId():null) ;
		del(entity) ;
		
		delResetSort(oldSort, pid) ;
		
		return new Msg(true, "删除成功！");
	}
	
	private void del(DictEntity entity) {
		if (entity.getDicts() != null && entity.getDicts().size() > 0) {
			for (DictEntity e : entity.getDicts()) {
				del(e);
			}
		}
		this.basedaoDict.delete(DictEntity.class, entity.getId()) ;
	}

	@Override
	public Msg update(DictForm form) {
		if("LB".equals(form.getType().trim())) {
			this.basedaoDict.updateByHql("update DictEntity t set t.selected='false' where t.dict.id='"+form.getPid()+"'") ;
		}
		
		DictEntity entity = this.basedaoDict.load(DictEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(form, entity) ;
		entity.setModifyDate(new Date()) ;
		
		if(null != entity.getDict() && !form.getPid().equals(entity.getDict().getId())) {
			entity.setSort(getMM(form.getPid())[1]+1) ;
		}
		
		if (null != form.getPid() && !"".equals(form.getPid())) {
			if(!entity.getId().equals(form.getPid())) {
				entity.setDict(this.basedaoDict.load(DictEntity.class, form.getPid()));
			} else {
				return new Msg(false, "操作有误，父模块服务关联自己！");
			}
		}
		this.basedaoDict.update(entity) ;
		
		return new Msg(true, "修改成功！");
	}
	
	@Override
	public DictForm get(DictForm form) {
		DictEntity entity = this.basedaoDict.load(DictEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(entity, form) ;
		if (null != entity.getDict()) {
			form.setPid(entity.getDict().getId());
		}
		return form ;
	}

	@Override
	public List<DictForm> tree(String pid, boolean gridOrCombo) {
		String sql = "select t.* from ieasy_sys_dict t where t.pid is null order by t.sort asc" ;
		if(null != pid && !"".equals(pid.trim()))
			sql = "select t.* from ieasy_sys_dict t where t.id='"+pid+"'" ;
		
		List<DictForm> list = this.basedaoDict.listSQL(sql, DictForm.class, false) ;
		List<DictForm> forms = new ArrayList<DictForm>() ;
		for (DictForm e : list) {
			forms.add(recursive(e, gridOrCombo));
		}
		
		String path = WebContextUtil.getSc().getRealPath("/static_res") +File.separator+ "dict.tree.json" ;
		if(gridOrCombo) {
			path = WebContextUtil.getSc().getRealPath("/static_res") +File.separator+ "dict.combotree.json";
		}
		FileUtils.WriteJSON(path, forms) ;
		return forms;
	}

	public DictForm recursive(DictForm form, boolean gridOrCombo) {
		form.setText(form.getDictName()) ;
		List<DictForm> dicts = this.basedaoDict.listSQL("select t.* from ieasy_sys_dict t where t.pid=? order by t.sort asc", new Object[]{form.getId()}, DictForm.class, false) ;
		if(null != dicts && dicts.size() > 0) {
			
			List<DictForm> chlds = new ArrayList<DictForm>() ;
		
			for(DictForm e : dicts) {
				//导出false为treegrid，true为combotree
				if(gridOrCombo) {
					if(e.getType().equals("LB")) continue ;
				}
				DictForm recursive = this.recursive(e, gridOrCombo) ;
				
				DictForm childform = new DictForm() ;
				BeanUtils.copyNotNullProperties(recursive, childform) ;
				chlds.add(childform) ;
			}
			
			form.setChildren(chlds) ;
		}
		
		return form ;
	}
	
	@Override
	public Map<String, List<DictJson>> generateAttrMaps() {
		String sql = "select t.id, t.dictIndex, t.dictName as text, t.dictCode from ieasy_sys_dict t where t.type=?" ;
		List<DictJson> list = this.basedaoDict.listSQL(sql, new Object[]{"LX"}, DictJson.class, false) ;
		Map<String, List<DictJson>> maps = new HashMap<String, List<DictJson>>() ;
		for (DictJson e : list) {
			List<DictJson> dicts = this.basedaoDict.listSQL("select t.id, t.dictIndex, t.dictName as text, t.dictCode, (CASE t.selected WHEN 'false' THEN '' ELSE 'true' END) as selected from ieasy_sys_dict t where t.pid=? order by t.sort asc", new Object[]{e.getId()}, DictJson.class, false) ;
			if(null != dicts && dicts.size() > 0) {
				List<DictJson> chlds = new ArrayList<DictJson>() ;
				for (DictJson child : dicts) {
					chlds.add(child) ;
				}
				maps.put(e.getDictCode(), chlds) ;
			}
		}
		return maps;
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
					Object[] obj = this.basedaoDict.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_dict t where t.pid is null") ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.dict.id is null", new Object[]{oldSort, newSort}) ;
					} else {
						this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.dict.id is null", new Object[]{oldSort, newSort}) ;
					}
				} else {
					Object[] obj = this.basedaoDict.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_dict t where t.pid=?", new Object[]{pid}) ;
					int min = (int)obj[0] ;
					int max = (int)obj[1] ;
					if(newSort<min) newSort = min;
					if(newSort>=max) newSort = max ;
					
					if(oldSort < newSort) {
						this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort-1 where t.sort>? and t.sort<=? and t.dict.id=?", new Object[]{oldSort, newSort, pid}) ;
					} else {
						this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort+1 where t.sort<? and t.sort>=? and t.dict.id=?", new Object[]{oldSort, newSort, pid}) ;
					}
				}
				this.basedaoDict.updateByHql("update DictEntity t set t.sort=? where t.id=?", new Object[]{newSort, id}) ;
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
			Object[] obj = this.basedaoDict.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_dict t where t.pid is null") ;
			mm[0] = (int)(obj[0] == null?0:obj[0]) ;
			mm[1] = (int)(obj[1] == null?0:obj[1]) ;
		} else {
			Object[] obj = this.basedaoDict.queryObjectSQL("select min(t.sort) min, max(t.sort) max from ieasy_sys_dict t where t.pid=?", new Object[]{pid}) ;
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
			this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort+1 where t.sort>=1 and t.dict.id is null") ;
		} else {
			this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort+1 where t.sort>=1 and t.dict.id=?", new Object[]{pid}) ;
		}
	}
	
	/**
	 * 删除数据，大于原位置的元素全部-1
	 * @param pid
	 */
	private void delResetSort(int oldSort, String pid) {
		if(null == pid || "".equals(pid.trim())) {
			this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort-1 where t.sort>? and t.dict.id is null", new Object[]{oldSort}) ;
		} else {
			this.basedaoDict.updateByHql("update DictEntity t set t.sort=t.sort-1 where t.sort>? and t.dict.id=?", new Object[]{oldSort, pid}) ;
		}
	}

	@Override
	public void init(DictEntity entity) {
		if(this.getBySn(entity.getDictCode()) != null)
			throw new ServiceException("该数据字典的SN["+entity.getDictCode()+"]已存在，无法添加！") ;
		
		if(null != entity.getDict()) {
			addResetSort(entity.getDict().getId()) ;
		}
		entity.setSort(1) ;
		entity.setCreated(new Date()) ;
		entity.setCreateName("系统初始化") ;
		
		this.basedaoDict.add(entity) ;
	}
	private String getBySn(String sn) {
		return (String) this.basedaoDict.queryObject("select t.dictCode from DictEntity t where t.dictCode=?", new Object[]{sn}) ;
	}
}
