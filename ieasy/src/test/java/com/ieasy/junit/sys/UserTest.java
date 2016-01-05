package com.ieasy.junit.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.ieasy.basic.util.PinyinUtil;
import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.common.dto.UserExcelDto;
import com.ieasy.module.system.service.IUserService;
import com.ieasy.module.system.web.form.LoginUser;
import com.ieasy.module.system.web.form.UserForm;

public class UserTest extends BasicJunitTest {
	
	@Inject
	private IUserService userService ;
	
	
	@Test
	public void testUserDatagrid() {
		this.userService.datagrid(null) ;
	}
	
	
	@Test
	public void testImport() {
		ArrayList<ArrayList<Object>> list = ExcelUtil.getInstance().getSheetRowsByClasspath("resources/excel/员工信息记录.xls", "Sheet2", 1, null, null, null, false) ;
		for (ArrayList<Object> a : list) {
			UserForm u = new UserForm() ;
			u.setNum(a.get(0).toString()) ;
			u.setName(a.get(1).toString()) ;
			u.setSex(a.get(3).toString()) ;
			u.setMobile(a.get(4).toString()) ;
			u.setEmail(a.get(5).toString()) ;
			
			u.setAccount(PinyinUtil.getPinYin(a.get(1).toString())) ;
			u.setPassword("123") ;
			
			this.userService.add(u) ;
		}
	}
	@Test
	public void testExport() {
		List<UserExcelDto> objs = this.userService.exportBasicUserInfo() ;
		Map<String, String> datas = new HashMap<String, String>() ;
		datas.put("title", "用户账号信息") ;
		datas.put("total", String.valueOf(objs.size())) ;
		datas.put("date", DateUtils.formatYYYYMMDD(new Date())) ;
		ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas, "resources/excel/template/user.xlsx", "c:/abc.xlsx", objs, UserExcelDto.class, true) ;
	}
	
	
	@Test
	public void testLoginCheck() {
		LoginUser form = new LoginUser() ;
		form.setAccount("BH-895918") ;
		form.setPassword("123") ;
		this.userService.loginCheck(form) ;
	}
	
	@Test
	public void test04() {
		this.userService.getAuth("402881e54841095001484109cc51012f") ;
	}
	
	

}
