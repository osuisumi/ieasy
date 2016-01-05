package com.ieasy.basic.model;

import java.util.ArrayList;
import java.util.List;

public class DataGrid {

	/** 总记录数 */
	private Long total;
	
	/** 每行记录 */
	private List<?> rows;
	
	private List<?> footer;
	
	public List<?> data ;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		if(rows == null) {
			rows = new ArrayList<>();
		}
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public List<?> getFooter() {
		return footer;
	}

	public void setFooter(List<?> footer) {
		this.footer = footer;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	
	
}
