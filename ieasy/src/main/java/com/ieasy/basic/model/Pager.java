package com.ieasy.basic.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象模型
 * @author 杨浩泉
 *
 */
public class Pager<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page;					// 分页的起始页
	
	private int rows;					// 每页显示记录数
	
	private long total ;				//总记录数
	
	private List<T> dataRows ;			//分页的数据

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<T> dataRows) {
		this.dataRows = dataRows;
	}
	
}
