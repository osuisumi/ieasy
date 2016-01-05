package com.ieasy.module.system.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ieasy.basic.dao.IdEntity;
/**
 * 许可的操作，对应许可菜单
 * @author t-work
 *
 */
@Entity @Table(name="ieasy_sys_permits_oper")
public class PermitsOperEntity  extends IdEntity {

	private String principalType;							//主体类型（USER、ROLE、DEPT、POSITION）
	
	private String principalId;								//主体ID
	
	private String operMenuId ;								//菜单操作资源ID
	
	private String operMenuName ;							//菜单操作资源名称
	
	private String operMenuHref ;							//菜单操作资源的URL
	
	private Integer operSort = new Integer(1) ;				//菜单操作资源的排序号
	
	private String operIconCls ;							//菜单操作资源的图标
	
	private String state = "open" ;							//菜单操作资源的状态

	private PermitsMenuEntity permitsMenu ;
	
	@ManyToOne
	@JoinColumn(name="permitsMenuId")
	public PermitsMenuEntity getPermitsMenu() {
		return permitsMenu;
	}

	public void setPermitsMenu(PermitsMenuEntity permitsMenu) {
		this.permitsMenu = permitsMenu;
	}

	public String getPrincipalType() {
		return principalType;
	}

	public Integer getOperSort() {
		return operSort;
	}

	public void setOperSort(Integer operSort) {
		this.operSort = operSort;
	}

	public String getOperIconCls() {
		return operIconCls;
	}

	public void setOperIconCls(String operIconCls) {
		this.operIconCls = operIconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPrincipalType(String principalType) {
		this.principalType = principalType;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public String getOperMenuName() {
		return operMenuName;
	}

	public void setOperMenuName(String operMenuName) {
		this.operMenuName = operMenuName;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
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

	@Override
	public String toString() {
		return "PermitsOperEntity [principalType=" + principalType + ", principalId=" + principalId + ", operMenuId=" + operMenuId + ", operMenuHref=" + operMenuHref + ", permitsMenu=" + permitsMenu + "]";
	}

}
