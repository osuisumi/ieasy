package com.ieasy.junit.poi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ieasy.basic.util.poi.excel.ExcelTemplate;
import com.ieasy.basic.util.poi.excel.ExcelUtil;

public class TestPoi {

	@Test
	public void testWrite01() {
		ExcelTemplate et = ExcelTemplate.getInstance().readTemplateByClassPath("com/ieasy/junit/poi/template/default.xlsx") ;
		et.createNewRow() ;
		et.createCell("111") ;
		et.createCell(13) ;
		et.createCell(133.4593) ;
		et.createCell(new Date()) ;
		et.createNewRow() ;
		et.createCell(false) ;
		et.createCell(true) ;
		et.createCell("a2") ;
		et.createCell("a3") ;
		et.createNewRow() ;
		et.createCell("333") ;
		et.createCell("a1") ;
		et.createCell("a2") ;
		et.createCell("a3") ;
		et.createNewRow() ;
		et.createCell("444") ;
		et.createCell("a1") ;
		et.createCell("a2") ;
		et.createCell("a3") ;
		et.createNewRow() ;
		et.createCell("555") ;
		et.createCell("a1") ;
		et.createCell("a2") ;
		et.createCell("a3") ;
		
		Map<String, String> datas = new HashMap<String, String>() ;
		datas.put("title", "测试用户信息") ;
		datas.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date())) ;
		datas.put("dept", "研发部") ;
		et.replaceFinalData(datas) ;
		et.insertSer() ;
		
		et.writeToFile("c:/default.xlsx") ;
		
	}
	
	@Test
	public void testObj2XlsTemplate01() {
		List<User> list = new ArrayList<User>() ;
		list.add(new User(1, "张三", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(2, "努尔哈赤", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(3, "康熙", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(4, "乾隆", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(5, "成吉思汗", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		
		Map<String, String> datas = new HashMap<String, String>() ;
		datas.put("title", "用户信息") ;
		datas.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date())) ;
		ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas,"com/ieasy/junit/poi/template/user01.xlsx", "c:/user01_t_xlsx.xlsx", list, User.class, true) ;
	}
	
	@Test
	public void testObj2XlsTemplate02() {
		List<User> list = new ArrayList<User>() ;
		list.add(new User(1, "张三", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(2, "努尔哈赤", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(3, "康熙", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(4, "乾隆", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(5, "成吉思汗", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		
		Map<String, String> datas = new HashMap<String, String>() ;
		datas.put("title", "用户信息") ;
		datas.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date())) ;
		ExcelUtil.getInstance().exportObj2ExcelByTemplate(datas,"com/ieasy/junit/poi/template/user02.xls", "c:/user02_t_xls.xls", list, User.class, true) ;
	}
	
	@Test
	public void testObj2Xls01() {
		List<User> list = new ArrayList<User>() ;
		list.add(new User(1, "张三", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(2, "努尔哈赤", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(3, "康熙", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(4, "乾隆", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(5, "成吉思汗", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		
		ExcelUtil.getInstance().exportObj2Excel("c:/user01_xlsx.xlsx", list, User.class, true) ;
	}
	
	@Test
	public void testObj2Xls02() {
		List<User> list = new ArrayList<User>() ;
		list.add(new User(1, "张三", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(2, "努尔哈赤", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(3, "康熙", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(4, "乾隆", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		list.add(new User(5, "成吉思汗", "bbb", 27, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) ;
		
		ExcelUtil.getInstance().exportObj2Excel("c:/user01_xls.xls", list, User.class, false) ;
	}
	
	@Test
	public void testReadExcel2Objs01() {
		List<Object> list = ExcelUtil.getInstance().readExce2ObjsByPath("c:/user01_xlsx.xlsx", User.class) ;
		for (Object o : list) {
			User u = (User)o ;
			System.out.println(u);
		}
	}
	
	@Test
	public void testReadExcel2Objs02() {
		List<Object> list = ExcelUtil.getInstance().readExce2ObjsByPath("c:/user01_t_xlsx.xlsx", User.class, 1, 1) ;
		for (Object o : list) {
			User u = (User)o ;
			System.out.println(u);
		}
	}
	
	@Test
	public void testReadExcel2Objs03() {
		List<Object> list = ExcelUtil.getInstance().readExce2ObjsByClasspath("resources/excel/11.xls", User.class, 1, 1) ;
		for (Object o : list) {
			User u = (User)o ;
			System.out.println(u);
		}
	}
	
	@Test
	public void testReadSheetRows01() {
		ArrayList<ArrayList<Object>> list = ExcelUtil.getInstance().getSheetRowsByClasspath("resources/excel/员工信息记录.xls", "Sheet2", 1, null, null, null, false) ;
		for (ArrayList<Object> arrayList : list) {
			System.out.println(arrayList);
		}
		
	}
	
	@Test
	public void testReadSheetRows02() {
		String path = System.getProperty("user.dir")+"/src/main/resources/resources/excel/员工信息记录.xls" ;
		ArrayList<ArrayList<Object>> list = ExcelUtil.getInstance().getSheetRowsByPath(path, "Sheet2", 1, 4, null, new Integer[]{0, 1}, false) ;
		for (ArrayList<Object> arrayList : list) {
			System.out.println(arrayList);
		}
		
	}
	
}
