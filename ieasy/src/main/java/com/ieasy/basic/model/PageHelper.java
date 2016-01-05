package com.ieasy.basic.model;

import java.util.Date;

/**
 * 分页数据模型
 * 用于接收客户端的参数
 * @author 杨浩泉
 *
 */
public class PageHelper {

	private String id ;				//唯一标示
	
	private String ids ;			//ID集合（删除）
	
	private int page;				// 当前页
	
	private int rows;				// 每页显示记录数
	
	private String sort;			// 排序字段名
	
	private String order;			// 按什么排序(asc,desc)

	private String createName ;				//创建者名称
	
	private Date created ;					//创建日期
	
	private String modifyName ;				//修改者名称
	
	private Date modifyDate ;				//修改日期
	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if(page < 0) SystemContext.setPage(1) ;
		else SystemContext.setPage(page) ;
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		if(rows < 0) SystemContext.setRows(15) ;
		else SystemContext.setRows(rows) ;
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	
	
}
