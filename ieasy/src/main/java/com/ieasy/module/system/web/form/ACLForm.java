package com.ieasy.module.system.web.form;

import java.io.Serializable;
import java.util.List;

public class ACLForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id ;									//ACL ID唯一标示
	
	private String principals ;							//接收客户端传来主体的JSON字符串数据
	
	private String resources ;							//接收客户端传来资源的JSON字符串数据

	private String principalType;						//主体类型（USER、ROLE、DEPT、POSITION）
	
	private String principalId;							//主体ID
	
	private String menuId ;								//菜单资源ID
	
	private String menuName ;							//菜单资源名称
	
	private String menuHref ;							//菜单资源的URL
	
	private Integer menuSort ;							//菜单排序号
	
	private String menuIconCls ;						//菜单图标
	
	private String state = "open" ;						//菜单状态

	private String menuPid ;							//菜单父节点
	
	private String operMenuId ;							//菜单操作资源ID
	
	private String operMenuName ;						//菜单操作资源ID
	
	private String operMenuHref ;						//菜单操作资源的URL
	
	private String operIconCls ;						//菜单操作资源的图标
	
	private Integer operSort ;							//菜单操作资源的状态
	
	private String permitsMenuId ;						//对应许可菜单中的ID
	
	private List<ACLForm> menus ;						//许可的菜单资源
	
	private List<ACLForm> opers ;						//许可的操作
	
	
	public String getMenuIconCls() {
		return menuIconCls;
	}

	public String getOperIconCls() {
		return operIconCls;
	}

	public void setOperIconCls(String operIconCls) {
		this.operIconCls = operIconCls;
	}

	public Integer getOperSort() {
		return operSort;
	}

	public void setOperSort(Integer operSort) {
		this.operSort = operSort;
	}

	public void setMenuIconCls(String menuIconCls) {
		this.menuIconCls = menuIconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}

	public List<ACLForm> getMenus() {
		return menus;
	}

	public void setMenus(List<ACLForm> menus) {
		this.menus = menus;
	}

	public String getPrincipals() {
		return principals;
	}

	public String getOperMenuName() {
		return operMenuName;
	}

	public void setOperMenuName(String operMenuName) {
		this.operMenuName = operMenuName;
	}

	public void setPrincipals(String principals) {
		this.principals = principals;
	}

	public String getPermitsMenuId() {
		return permitsMenuId;
	}

	public void setPermitsMenuId(String permitsMenuId) {
		this.permitsMenuId = permitsMenuId;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getPrincipalType() {
		return principalType;
	}

	public void setPrincipalType(String principalType) {
		this.principalType = principalType;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuHref() {
		return menuHref;
	}

	public void setMenuHref(String menuHref) {
		this.menuHref = menuHref;
	}

	public String getMenuPid() {
		return menuPid;
	}

	public void setMenuPid(String menuPid) {
		this.menuPid = menuPid;
	}

	public String getOperMenuId() {
		return operMenuId;
	}

	public void setOperMenuId(String operMenuId) {
		this.operMenuId = operMenuId;
	}

	public String getOperMenuHref() {
		return operMenuHref;
	}

	public void setOperMenuHref(String operMenuHref) {
		this.operMenuHref = operMenuHref;
	}

	public List<ACLForm> getOpers() {
		return opers;
	}

	public void setOpers(List<ACLForm> opers) {
		this.opers = opers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operMenuId == null) ? 0 : operMenuId.hashCode());
		return result;
	}

	/**
	 * 去除operMenuId重复的操作
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ACLForm other = (ACLForm) obj;
		if (operMenuId == null) {
			if (other.operMenuId != null)
				return false;
		} else if (!operMenuId.equals(other.operMenuId))
			return false;
		return true;
	}


}
