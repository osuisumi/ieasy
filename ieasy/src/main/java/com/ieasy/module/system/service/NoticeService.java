/**
 * 
 */
package com.ieasy.module.system.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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
import com.ieasy.module.common.web.servlet.WebContextUtil;
import com.ieasy.module.system.entity.NoticeEntity;
import com.ieasy.module.system.web.form.NoticeForm;

/**
 * @author 杨浩泉
 *
 */
@Service("noticeService") @Transactional
public class NoticeService implements INoticeService {
	
	@Inject
	private IBaseDao<NoticeEntity> basedaoNotice ;
	
	@Override
	public Msg add(NoticeForm form) {
		NoticeEntity entity = new NoticeEntity() ;
		BeanUtils.copyNotNullProperties(form, entity) ;
		entity.setCreateName(WebContextUtil.getCurrentUser().getUser().getEmp_name()) ;
		entity.setCreated(new Date()) ;
		
		this.basedaoNotice.add(entity) ;
		return new Msg(true, "添加成功！");
	}

	@Override
	public Msg delete(NoticeForm form) {
		try {
			if(null != form.getIds() && !"".equals(form.getIds())) {
				String[] ids = form.getIds().split(",") ;
				for (String id : ids) {
					this.basedaoNotice.delete(NoticeEntity.class, id) ;
				}
				return new Msg(true, "删除成功！") ;
			}
		} catch (Exception e) {
			return new Msg(false, "删除失败！") ;
		}
		return null;
	}
	
	@Override
	public Msg update(NoticeForm form) {
		NoticeEntity entity = this.basedaoNotice.load(NoticeEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(form, entity, new String[]{"created", "createName"}) ;
		entity.setModifyName(WebContextUtil.getCurrentUser().getUser().getEmp_name()) ;
		entity.setModifyDate(new Date()) ;
		
		this.basedaoNotice.update(entity) ;
		
		return new Msg(true, "修改成功！");
	}
	
	@Override
	public NoticeForm get(NoticeForm form) {
		NoticeEntity entity = this.basedaoNotice.load(NoticeEntity.class, form.getId()) ;
		BeanUtils.copyNotNullProperties(entity, form) ;
		return form ;
	}

	@Override
	public DataGrid datagrid(NoticeForm form) {
		
		if(null != form.getSort()) {
			String[] personSorts = new String[]{"created"};
			if(StringUtil.arrayToString(personSorts).contains(form.getSort())) {
				SystemContext.setSort("t."+form.getSort()) ; SystemContext.setOrder(form.getOrder()) ;
			}
		}
		try {
			Pager<NoticeForm> pager = this.find(form) ;
			DataGrid dg = new DataGrid() ;
			dg.setTotal(pager.getTotal());
			dg.setRows(pager.getDataRows());
			return dg;
		} catch (Exception e) {
			throw new ServiceException("加载信息异常", e) ;
		}
	}


	private Pager<NoticeForm> find(NoticeForm form) {
		Map<String, Object> alias = new HashMap<String, Object>();
		String sql = "select " +
					 "t.id, t.title, t.content, t.type, t.approve, " +
					 "t.created, t.createName, t.modifyName, t.modifyDate " +
				 	 "from ieasy_sys_notice t " +
				 	 "where 1=1 ";
		sql = addWhere(sql, form, alias) ;
		return this.basedaoNotice.findSQL(sql, alias, NoticeForm.class, false) ;
	}

	private String addWhere(String hql, NoticeForm form, Map<String, Object> params) {
		if (form.getTitle() != null && !"".equals(form.getTitle())) {
			hql += " and t.title=:title";
			params.put("title", form.getTitle());
		}
		if (form.getApprove() != null && !"".equals(form.getApprove())) {
			hql += " and t.approve=:approve";
			params.put("approve", form.getApprove());
		}
		if (form.getType() != null && !"".equals(form.getType())) {
			hql += " and t.type=:type";
			params.put("type", form.getType());
		}
		if (form.getCreateName() != null && !"".equals(form.getCreateName())) {
			hql += " and t.createName=:createName";
			params.put("createName", form.getCreateName());
		}
		return hql;
	}

	@Override
	public Msg modifyAp(NoticeForm form) {
		Map<String, Object> alias = new HashMap<String, Object>() ;
		alias.put("id", form.getId()) ;
		alias.put("s", form.getApprove()) ;
		this.basedaoNotice.executeSQL("update ieasy_sys_notice t set t.approve=:s where t.id=:id", alias) ;
		return new Msg(true, "修改成功！");
	}
	
}
