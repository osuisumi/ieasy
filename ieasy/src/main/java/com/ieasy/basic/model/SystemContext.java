package com.ieasy.basic.model;

/**
 * 用来传递列表对象的ThreadLocal数据
 * @author 杨浩泉
 *
 */
public class SystemContext {

	private static ThreadLocal<Integer> page = new ThreadLocal<Integer>() ;				//分页的起始页
	
	private static ThreadLocal<Integer> rows = new ThreadLocal<Integer>() ;				//分页的大小
	
	private static ThreadLocal<String> sort = new ThreadLocal<String>() ;				//列表的排序字段
	
	private static ThreadLocal<String> order = new ThreadLocal<String>() ;				//列表的排序方式
	
	public static Integer getPage() {
		return page.get();
	}

	public static void setPage(Integer _page) {
		page.set(_page) ;
	}

	public static Integer getRows() {
		return rows.get();
	}

	public static void setRows(Integer _rows) {
		rows.set(_rows) ;
	}

	public static String getSort() {
		return sort.get();
	}

	public static void setSort(String _sort) {
		SystemContext.sort.set(_sort) ;
	}

	public static String getOrder() {
		return order.get();
	}

	public static void setOrder(String _order) {
		SystemContext.order.set(_order) ;
	}
	
	public static void removeSort() {
		sort.remove() ; 
	}
	
	public static void removeOrder() {
		order.remove() ; 
	}
	
}
