package com.ieasy.module.system.service;

import java.util.List;

import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.entity.PositionEntity;
import com.ieasy.module.system.web.form.PositionForm;

public interface IPositionService {
	
	/**
	 * 添加岗位
	 * @param form
	 * @return
	 */
	public Msg add(PositionForm form) ;
	
	/**
	 * 删除岗位
	 * @param form
	 * @return
	 */
	public Msg delete(PositionForm form) ;
	
	/**
	 * 修改岗位
	 * @param form
	 * @return
	 */
	public Msg update(PositionForm form) ;
	
	/**
	 * 获取一个岗位对象
	 * @param form
	 * @return
	 */
	public PositionForm get(PositionForm form) ;
	
	/**
	 * 生成所有岗位树
	 * @param pid 根据父岗位生成岗位树
	 * @return
	 */
	public List<PositionForm> tree(String pid) ;
	
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
	 * 初始化岗位数据
	 * @param entity
	 */
	public void init(PositionEntity entity) ;
}
