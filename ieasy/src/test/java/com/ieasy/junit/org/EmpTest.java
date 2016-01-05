package com.ieasy.junit.org;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.ieasy.basic.util.date.DateUtils;
import com.ieasy.basic.util.poi.excel.ExcelUtil;
import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.common.dto.EmpExcelDto;
import com.ieasy.module.system.service.IPersonService;

public class EmpTest extends BasicJunitTest {
	
	@Inject
	private IPersonService empService ;
	
	@Test
	public void testUserDatagrid() {
		this.empService.datagrid(null) ;
	}
	
	
	@Test
	public void testExport() {
		List<EmpExcelDto> exportEmpInfo = this.empService.exportEmpInfo(null) ;
		
		System.out.println(exportEmpInfo);
		
		
		Map<String, String> datas = new HashMap<String, String>() ;
		datas.put("title", "用户账号信息") ;
		datas.put("total", String.valueOf(exportEmpInfo.size())) ;
		datas.put("date", DateUtils.formatYYYYMMDD(new Date())) ;
		ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas, "resources/excel/template/emp.xlsx", "c:/abc.xlsx", exportEmpInfo, EmpExcelDto.class, true) ;
		
	}

}
