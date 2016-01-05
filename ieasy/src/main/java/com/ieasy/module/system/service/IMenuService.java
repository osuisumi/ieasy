package com.ieasy.module.system.service;

import java.util.List;

import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.entity.MenuEntity;
import com.ieasy.module.system.web.form.MenuForm;

public interface IMenuService {
	
	/**
	 * 添加菜单
	 * @param form
	 * @return
	 */
	public Msg add(MenuForm form) ;
	
	/**
	 * 删除菜单
	 * @param form
	 * @return
	 */
	public Msg delete(MenuForm form) ;
	
	/**
	 * 修改菜单
	 * @param form
	 * @return
	 */
	public Msg update(MenuForm form) ;
	
	/**
	 * 获取一个菜单信息
	 * @param form
	 * @return
	 */
	public MenuForm get(MenuForm form) ;
	
	/**
	 * 生成所有菜单树
	 * @param pid 根据父菜单生成菜单树
	 * @return
	 */
	public List<MenuForm> tree(String pid) ;
	
	/**
	 * 下拉列表或下拉树
	 * @param form
	 * @return
	 */
	public List<MenuForm> combo(MenuForm form) ;
	
	
	/**
	 * 生成JSON菜单树
	 * @param form
	 * @param sc
	 * @return
	 */
	public void exportTree() ;
	

	/**
	 * 设置菜单是否隐藏
	 * @param form
	 * @return
	 */
	public Msg isShow(MenuForm form) ;
	
	/**
	 * 更新位置
	 * 如果原位置小于新位置，让所有>原位置，<=新位置的元素全部-1，之后更新对象的位置
	 * 如果原位置大于新位置，让所有<原位置，>=新位置的元素全部加1，之后更新对象位置
	 * @param oldSort 原始的位置
	 * @param newSort 新的位置
	 * @param id 需修改的ID
	 * @param pid 该pid下的sort先加1或减1
	 * @return
	 */
	public void updateResetSort(int oldSort, int newSort, String id, String pid) ;
	
	/**
	 * 获取所有菜单数据
	 * @param form
	 * @return
	 */
	public List<MenuForm> getAllMenuTree(MenuForm form) ;
	
	/**
	 * 初始化菜单数据
	 * @param entity
	 */
	public void init(MenuEntity entity) ;
}
