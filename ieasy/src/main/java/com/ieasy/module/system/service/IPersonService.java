package com.ieasy.module.system.service;

import java.util.List;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.common.dto.EmpExcelDto;
import com.ieasy.module.system.web.form.PersonForm;

public interface IPersonService {
	
	/**
	 * 添加员工
	 * @param form
	 * @return
	 */
	public Msg add(PersonForm form) ;
	
	
	/**
	 * 删除员工
	 * @param form
	 * @return
	 */
	public Msg delete(PersonForm form) ;
	
	/**
	 * 修改员工
	 * @param form
	 * @return
	 */
	public Msg update(PersonForm form) ;
	
	/**
	 * 获取一个员工对象
	 * @param form
	 * @return
	 */
	public PersonForm get(PersonForm form) ;
	
	/**
	 * 员工查询
	 * @param form
	 * @return
	 */
	public DataGrid datagrid(PersonForm form) ;
	
	/**
	 * 批量调换组织机构
	 * @param form
	 * @return
	 */
	public Msg batchChangeDept(PersonForm form) ;
	
	/**
	 * 批量设置岗位
	 * @param form
	 * @return
	 */
	public Msg batchSetPos(PersonForm form) ;
	
	/**
	 * 导出员工账号基本信息到Excel
	 * @return
	 */
	public List<EmpExcelDto> exportEmpInfo(PersonForm form) ;
	
	/**
	 * 解析Excle文件并导入到数据库中
	 * @param datafile
	 * @return
	 */
	//public Msg parseDataInsert(String datafile) ;


	/**
	 * 批量离职
	 * @param form
	 * @return
	 */
	public Msg batchDimission(PersonForm form) ;


	/**
	 * 批量注销，logout为-1
	 * 修改关键信息为：
	 * 编号(__1039__)
	 * 邮件(__none__)
	 * @param form
	 * @return
	 */
	public Msg batchOrgLogout(PersonForm form);
	
	/**
	 * 修改人员在项目中的工作状态，待机或者在项目中
	 * @param form
	 * @return
	 */
	public Msg modifyWorkStatus(PersonForm form) ;
	
}
