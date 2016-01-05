package com.ieasy.module.system.service;

import java.util.List;

import com.ieasy.basic.model.DataGrid;
import com.ieasy.basic.model.Msg;
import com.ieasy.module.common.dto.UserExcelDto;
import com.ieasy.module.system.web.form.AuthForm;
import com.ieasy.module.system.web.form.LoginUser;
import com.ieasy.module.system.web.form.UserForm;

public interface IUserService {
	
	/**
	 * 添加用户
	 * @param form
	 * @return
	 */
	public Msg add(UserForm form) ;
	
	/**
	 * 删除用户
	 * @param form
	 * @return
	 */
	public Msg delete(UserForm form) ;
	
	/**
	 * 修改用户
	 * @param form
	 * @return
	 */
	public Msg update(UserForm form) ;
	
	/**
	 * 获取一个用户对象
	 * @param form
	 * @return
	 */
	public UserForm get(UserForm form) ;
	
	/**
	 * 用户查询
	 * @param form
	 * @return
	 */
	public DataGrid datagrid(UserForm form) ;
	
	/**
	 * 用户批量添加角色
	 * @param form
	 * @return
	 */
	public Msg batchUserRole(UserForm form) ;
	
	/**
	 * 清空密码
	 * @param form
	 * @return
	 */
	public Msg batchClearPwd(UserForm form) ;
	
	/**
	 * 重设密码
	 * @param form
	 * @return
	 */
	public Msg batchResetPwd(UserForm form) ;
	
	/**
	 * 锁定账号
	 * @param form
	 * @return
	 */
	public Msg batchLockAccount(UserForm form) ;
	
	/**
	 * 导出用户账号基本信息到Excel
	 * @return
	 */
	public List<UserExcelDto> exportBasicUserInfo() ;
	
	/**
	 * 解析Excle文件并导入到数据库中
	 * @param datafile
	 * @return
	 */
	public Msg parseDataInsert(String datafile) ;
	
	/**
	 * 创建用户账号
	 * @param form
	 * @return
	 */
	public Msg createAccount(UserForm form) ;
	
	/**
	 * 用户登陆验证
	 * @param form
	 * @return
	 */
	public LoginUser loginCheck(LoginUser form) ;
	
	/**
	 * 获取权限
	 * @param form
	 * @return
	 */
	public AuthForm getAuth(String userId) ;
	
}
