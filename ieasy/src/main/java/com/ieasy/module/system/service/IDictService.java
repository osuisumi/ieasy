package com.ieasy.module.system.service;

import java.util.List;
import java.util.Map;

import com.ieasy.basic.model.Msg;
import com.ieasy.module.system.entity.DictEntity;
import com.ieasy.module.system.web.form.DictForm;
import com.ieasy.module.system.web.form.DictJson;

public interface IDictService {
	
	/**
	 * 添加字典
	 * @param form
	 * @return
	 */
	public Msg add(DictForm form) ;
	
	/**
	 * 删除字典
	 * @param form
	 * @return
	 */
	public Msg delete(DictForm form) ;
	
	/**
	 * 修改字典
	 * @param form
	 * @return
	 */
	public Msg update(DictForm form) ;
	
	/**
	 * 获取一个字典对象
	 * @param form
	 * @return
	 */
	public DictForm get(DictForm form) ;
	
	/**
	 * 生成所有字典树
	 * @param pid 根据父机构生成字典树
	 * @return
	 */
	public List<DictForm> tree(String pid, boolean gridOrCombo) ;
	
	
	/**
	 * 获取所有的字典类型和列表，保存到全局上下文
	 * @return
	 */
	public Map<String, List<DictJson>> generateAttrMaps() ;
	
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
	
	public void init(DictEntity entity) ;

}
