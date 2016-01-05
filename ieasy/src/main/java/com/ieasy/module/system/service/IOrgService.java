package com.ieasy.module.system.service;

import java.util.List;

import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.entity.OrgEntity;
import com.ieasy.module.system.web.form.OrgForm;

public interface IOrgService {
	
	/**
	 * 添加组织机构
	 * @param form
	 * @return
	 */
	public Msg add(OrgForm form) ;
	
	/**
	 * 删除组织机构
	 * @param form
	 * @return
	 */
	public Msg delete(OrgForm form) ;
	
	/**
	 * 修改组织机构
	 * @param form
	 * @return
	 */
	public Msg update(OrgForm form) ;
	
	/**
	 * 获取一个组织机构对象
	 * @param form
	 * @return
	 */
	public OrgForm get(OrgForm form) ;
	
	/**
	 * 生成所有组织机构树
	 * @param pid 根据父机构生成组织机构树
	 * @return
	 */
	public List<OrgForm> tree(String pid) ;
	
	public List<OrgForm> syncTree(String pid) ;
	
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
	 * 初始化机构部门数据
	 * @param entity
	 */
	public void init(OrgEntity entity) ;
	
}
