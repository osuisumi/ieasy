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
import com.ieasy.basic.util.UUIDHexGenerator;
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.GlobalEntity;
import com.ieasy.module.system.web.form.GlobalForm;

/**
 * @author 杨浩泉
 *
 */
@Service("globalService") @Transactional
public class GlobalService implements IGlobalService {
	
	@Inject
	private IBaseDao<GlobalEntity> basedaoGlobal ;
	
	@Override
	public Msg add(GlobalForm form) {
		GlobalEntity entity = new GlobalEntity() ;
		BeanUtils.copyNotNullProperties(form, entity) ;
		entity.setCreated(new Date()) ;
		BeanUtils.copyNotNullProperties(form, entity) ;
		this.basedaoGlobal.add(entity) ;
		return new Msg(true, "添加成功！");
	}

	@Override
	public Msg delete(GlobalForm form) {
		this.basedaoGlobal.delete(GlobalEntity.class, form.getId()) ;
		return new Msg(true, "删除成功！");
	}
	
	@Override
	public Msg update(GlobalForm form) {
		try {
			GlobalEntity entity = this.basedaoGlobal.load(GlobalEntity.class, form.getId()) ;
			
			if(null != entity) {	
				//将旧的信息保存为历史
				GlobalEntity history = new GlobalEntity() ;
				BeanUtils.copyNotNullProperties(entity, history) ;
				history.setId(UUIDHexGenerator.generator()) ;
				this.basedaoGlobal.add(history) ;		
				
				//再修改当前记录
				BeanUtils.copyNotNullProperties(form, entity) ;
				entity.setModifyDate(new Date()) ;
				this.basedaoGlobal.update(entity) ;
				
				BeanUtils.copyNotNullProperties(entity, form) ;
				//重新设置服务器上下文的值
				WebContextUtil.setGlobal(form) ;
				
				return new Msg(true, "编辑成功！") ;
			}
			
		} catch (BeansException e) {
			return new Msg(false, "发生错误！") ;
		}
		return new Msg(false, "编辑失败！") ;
	}
	
	@Override
	public GlobalForm get(String id) {
		GlobalEntity entity = this.basedaoGlobal.load(GlobalEntity.class, id) ;
		GlobalForm form = new GlobalForm() ;
		BeanUtils.copyNotNullProperties(entity, form) ;
		return form ;
	}

	@Override
	public DataGrid datagrid(GlobalForm form) {
		
		if(null != SystemContext.getSort() && !"".equals(SystemContext.getSort())) {
			SystemContext.setSort("t.modifyDate") ;SystemContext.setOrder("desc") ;
		}
		try {
			Pager<GlobalEntity> pager = this.find(form) ;
			List<GlobalForm> forms = new ArrayList<GlobalForm>() ;
			for (GlobalEntity entity : pager.getDataRows()) {
				GlobalForm gf = new GlobalForm() ;
				BeanUtils.copyNotNullProperties(entity, gf) ;
				forms.add(gf) ;
			}
			
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(forms);
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e) ;
		}
	}


	private Pager<GlobalEntity> find(GlobalForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String hql = "select t from GlobalEntity t where 1=1 " ;
		hql = addWhere(hql, form, alias) ;
		return this.basedaoGlobal.find(hql, alias) ;
	}

	private String addWhere(String hql, GlobalForm form, Map<String, Object> params) {
		if (form.getCreateName() != null && !"".equals(form.getCreateName())) {
			hql += " and t.createName=:createName";
			params.put("createName", form.getCreateName());
		}
		return hql;
	}

	@Override
	public void init(GlobalEntity entity) {
		this.basedaoGlobal.add(entity) ;
	}
	
}
