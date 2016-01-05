package com.ieasy.basic.util.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ieasy.basic.exception.ServiceException;
import com.ieasy.module.common.dto.EmpExcelDto;

/**
 * 该类实现了将一组短信转为为Excle表格，并且可以从Excle表格中读取一组List到对象中
 * 该类使用了BeanUtils框架中的反射完成
 * 使用该类的前提，在相应的实体对象上通过ExcleResources来完成相应的注解
 * @author ibm-work
 *
 */
public class ExcelUtil {

	private static ExcelUtil eu = new ExcelUtil() ;
	
	private ExcelUtil() {}
	
	public static ExcelUtil getInstance() {
		return eu ;
	}
	
	
	
	
	/**
	 * 将对象转化为Excle并且导出，该方法基于模板的导出，导出到流
	 * @param datas				模板中的替换的常量数据
	 * @param template			模板路径
	 * @param os				输出流
	 * @param objs				对象列表
	 * @param clz				对象的类型
	 * @param isClassPath		模板是否在classpath路径下
	 */
	public void exportObj2ExcelByTemplate(Map<String,String> datas, String template, OutputStream os, List<?> objs, Class<?> clz, boolean isClassPath) {
		try {
			ExcelTemplate et = handlerObj2Excel(datas, template, objs, clz, isClassPath) ;
			et.writeToStream(os) ;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将对象转化为Excle并且导出，该方法基于模板的导出，导出到一个具体的路径
	 * @param datas				模板中的替换的常量数据
	 * @param template			模板路径
	 * @param outPath			输出路径
	 * @param objs				对象列表
	 * @param clz				对象的类型
	 * @param isClassPath		模板是否在classpath路径下
	 */
	public void exportObj2ExcelByTemplate(Map<String,String> datas, String template, String outPath, List<?> objs, Class<?> clz, boolean isClassPath) {
		try {
			ExcelTemplate et = handlerObj2Excel(datas, template, objs, clz, isClassPath) ;
			et.writeToFile(outPath) ;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 填充模板数据
	 * @param datas
	 * @param template
	 * @param objs
	 * @param clz
	 * @param isClassPath
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private ExcelTemplate handlerObj2Excel(Map<String,String> datas, String template, List<?> objs, Class<?> clz, boolean isClassPath) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ExcelTemplate et = ExcelTemplate.getInstance() ;
		if(isClassPath) {
			et.readTemplateByClassPath(template) ;
		} else {
			et.readTemplateByPath(template) ;
		}
		List<ExcelHeader> headers = getHeaderList(clz) ;
		Collections.sort(headers) ;
		//输出标题
		et.createNewRow() ;
		for (ExcelHeader eh : headers) {
			et.createCell(eh.getTitle()) ;
		}
		//输出值
		for(Object obj : objs) {
			et.createNewRow() ;
			for (ExcelHeader eh : headers) {
				et.createCell(BeanUtils.getProperty(obj, getMethodName(eh))) ;
			}
		}
		et.replaceFinalData(datas) ;
		return et;
	}
	
	
	/**
	 * 导出对象到Excel，不是基于模板，直接新建一个Excle完成导出，基于路径的导出
	 * @param outPath 		导出路径
	 * @param objs			对象列表
	 * @param clz			对象类型
	 * @param isXssf		是否是2007版本
	 */
	public void exportObj2Excel(String outPath, List<?> objs, Class<?> clz, boolean isXssf) {
		Workbook wb = handlerObj2Excel(objs, clz, isXssf) ;
		FileOutputStream fos = null ;
		try {
			fos = new FileOutputStream(outPath) ;
			wb.write(fos) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fos) fos.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 导出对象到Excel，不是基于模板，直接新建一个Excle完成导出，基于流
	 * @param os 			输出流
	 * @param objs			对象列表
	 * @param clz			对象类型
	 * @param isXssf		是否是2007版本
	 */
	public void exportObj2Excel(OutputStream os, List<?> objs, Class<?> clz, boolean isXssf) {
		try {
			Workbook wb = handlerObj2Excel(objs, clz, isXssf) ;
			wb.write(os) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private Workbook handlerObj2Excel(List<?> objs, Class<?> clz, boolean isXssf) {
		Workbook wb = null ;
		try {
			if(isXssf) {
				wb = new XSSFWorkbook();
			} else {
				wb = new HSSFWorkbook();
			}
			Sheet sheet = wb.createSheet() ;
			Row r = sheet.createRow(0) ;
			List<ExcelHeader> headers = getHeaderList(clz) ;
			Collections.sort(headers) ;
			//写标题
			for(int i=0; i<headers.size(); i++) {
				r.createCell(i).setCellValue(headers.get(i).getTitle()) ;
			}
			
			//写数据
			Object obj = null ;
			for(int i=0; i<objs.size(); i++) {
				r = sheet.createRow(i+1) ;
				obj = objs.get(i) ;
				for(int j=0; j<headers.size(); j++) {
					r.createCell(j).setCellValue(BeanUtils.getProperty(obj, getMethodName(headers.get(j)))) ;
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return wb ;
	}
	
	/**
	 * 从类路径读取相应的Excel文件到对象列表
	 * @param path			类路径下的path
	 * @param clz			对象类型
	 * @param readLine		开始行，注意是标题所在行
	 * @param tailLine		底部有多少行，在读入对象时，会减去这些行
	 * @return
	 */
	public List<Object> readExce2ObjsByClasspath(String path, Class<?> clz, int readLine, int tailLine) {
		Workbook wb = null ;
		try {
			wb = WorkbookFactory.create(ExcelUtil.class.getClassLoader().getResourceAsStream(path)) ;
			return handlerExcel2Objs(wb, clz, readLine, tailLine) ;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从文件路径读取相应的Excel文件到对象列表
	 * @param path			类路径下的path
	 * @param clz			对象类型
	 * @param readLine		开始行，注意是标题所在行
	 * @param tailLine		底部有多少行，在读入对象时，会减去这些行
	 * @return
	 */
	public List<Object> readExce2ObjsByPath(String path, Class<?> clz, int readLine, int tailLine) {
		Workbook wb = null ;
		try {
			wb = WorkbookFactory.create(new File(path)) ;
			return handlerExcel2Objs(wb, clz, readLine, tailLine) ;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从类路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * @param path		路径
	 * @param clz		类型
	 * @return			返回对象列表
	 */
	public List<Object> readExce2ObjsByClasspath(String path, Class<?> clz) {
		return this.readExce2ObjsByClasspath(path, clz, 0, 0) ;
	}
	
	/**
	 * 从文件路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * @param path		路径
	 * @param clz		类型
	 * @return			返回对象列表
	 */
	public List<Object> readExce2ObjsByPath(String path, Class<?> clz) {
		return this.readExce2ObjsByPath(path, clz, 0, 0) ;
	}
	
	public List<Object> handlerExcel2Objs(Workbook wb, Class<?> clz, int readLine, int tailLine) {
		Sheet sheet = wb.getSheetAt(0);
		List<Object> objs = null;
		try {
			Row row = sheet.getRow(readLine);
			objs = new ArrayList<Object>();
			Map<Integer, String> maps = getHeaderMap(row, clz);
			
			if(maps == null || maps.size() < 0) throw new RuntimeException("要读取的Excel的格式不正确，检查是否设定了合适的行") ;

			for (int i = readLine + 1; i <= sheet.getLastRowNum() - tailLine; i++) {
				row = sheet.getRow(i);
				Object obj = clz.newInstance();
				for (Cell c : row) {
					int ci = c.getColumnIndex();
					
					String mn = maps.get(ci).substring(3);
					mn = mn.substring(0, 1).toLowerCase() + mn.substring(1) ;
					BeanUtils.copyProperty(obj, mn, getCellValue(c));
				}
				objs.add(obj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return objs;
	}
	
	private String getMethodName(ExcelHeader eh) {
		String mn = eh.getMethodName().substring(3) ;
		mn = mn.substring(0,1).toLowerCase() + mn.substring(1) ;
		return mn ;
	}
	
	
	
	/**
	 * 只获取方法上的注解
	 * @param clz
	 * @return
	 */
	/*private List<ExcelHeader> getHeaderList(Class<?> clz) {
		List<ExcelHeader> headers = new ArrayList<ExcelHeader>() ;
		Method[] methods = clz.getMethods() ;
		for (Method m : methods) {
			String mn = m.getName() ;
			if(mn.startsWith("get")) {
				if(m.isAnnotationPresent(ExcelResources.class)) {
					ExcelResources er = m.getAnnotation(ExcelResources.class) ;
					headers.add(new ExcelHeader(er.title(), er.order(), mn)) ;
				}
			}
		}
		return headers;
	}*/
	
	/**
	 * 获取属性和方法上的注解，去除重复
	 * @param clz
	 * @return
	 */
	private static List<ExcelHeader> getHeaderList(Class<?> clz) {
		Map<String, ExcelHeader> map = new HashMap<String, ExcelHeader>();
		List<ExcelHeader> headers = new ArrayList<ExcelHeader>() ;
		Field[] declaredFields = clz.getDeclaredFields() ;
		for (Field f : declaredFields) {
			String name = f.getName() ;
			String mn = "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length()) ;
			if(f.isAnnotationPresent(ExcelResources.class)) {
				ExcelResources er = f.getAnnotation(ExcelResources.class) ;
				map.put(mn, new ExcelHeader(er.title(), er.order(), mn)) ;
			}
		}
		
		Method[] methods = clz.getMethods() ;
		for (Method m : methods) {
			String mn = m.getName() ;
			if(mn.startsWith("get")) {
				if(m.isAnnotationPresent(ExcelResources.class)) {
					ExcelResources er = m.getAnnotation(ExcelResources.class) ;
					map.put(mn, new ExcelHeader(er.title(), er.order(), mn)) ;
				}
			}
		}
		headers.addAll(map.values()) ;
		return headers;
	}
	
	private Map<Integer, String> getHeaderMap(Row titleRow, Class<?> clz) {
		List<ExcelHeader> headers = getHeaderList(clz) ;
		Map<Integer, String> maps = new HashMap<Integer, String>() ;
		for(Cell c : titleRow) {
			String title = c.getStringCellValue() ;
			for(ExcelHeader eh : headers) {
				if(eh.getTitle().equals(title.trim())) {
					maps.put(c.getColumnIndex(), eh.getMethodName().replace("get", "set")) ;
					break ;
				}
			}
		}
		return maps;
	}
	
	
	/**
	 * 指定相应的开始行，结束行，开始列，结束列来读取Excle，基于类路径
	 * @param path				类路径
	 * @param sheetName			sheet页名称
	 * @param startRowIndex		指定读取的开始行的索引，没有指定则从0开始
	 * @param endRowIndex		指定读取的结束行的索引，没有指定则读取所有行
	 * @param callIndex			指定读取列的索引，没有指定则读取所有列  new Integer[]{1,2,3}
	 * @param isXssh			是否2007版本
	 * @return					返回集合
	 */
	public ArrayList<ArrayList<Object>> getSheetRowsByClasspath(String path, Object sheetName, int startRowIndex, Integer endRowIndex, Integer cellNum, Integer[] callIndex, boolean isXssh) {
		try {
			Workbook wb = null;
			if(isXssh) {
				wb = new XSSFWorkbook(ExcelUtil.class.getClassLoader().getResourceAsStream(path));
			} else {
				wb = new HSSFWorkbook(ExcelUtil.class.getClassLoader().getResourceAsStream(path));
			}
			return handlerSheetRow(wb, sheetName, startRowIndex, endRowIndex, cellNum, callIndex) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 指定相应的开始行，结束行，开始列，结束列来读取Excle，基于文件路径
	 * @param path				类路径
	 * @param sheetName			sheet页名称
	 * @param startRowIndex		指定读取的开始行的索引，没有指定则从0开始
	 * @param endRowIndex		指定读取的结束行的索引，没有指定则读取所有行
	 * @param callIndex			指定读取列的索引，没有指定则读取所有列  new Integer[]{1,2,3}
	 * @param isXssh			是否2007版本
	 * @return					返回集合
	 */
	public ArrayList<ArrayList<Object>> getSheetRowsByPath(String path, Object sheetName, int startRowIndex, Integer endRowIndex, Integer cellNum, Integer[] callIndex, boolean isXssh) {
		try {
			Workbook wb = null;
			if(isXssh) {
				wb = new XSSFWorkbook(new FileInputStream(path));
			} else {
				wb = new HSSFWorkbook(new FileInputStream(path));
			}
			return handlerSheetRow(wb, sheetName, startRowIndex, endRowIndex, cellNum, callIndex) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<ArrayList<Object>> handlerSheetRow(Workbook wb, Object sheetName, int startRowIndex, Integer endRowIndex, Integer cellNum, Integer[] callIndex) {
		ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
		Sheet sheet = null ;
		if(sheetName instanceof String) {
			sheet = wb.getSheet(sheetName.toString()) ;
		} else if(sheetName instanceof Integer) {
			sheet = wb.getSheetAt((Integer) sheetName) ;
		} else {
			sheet = wb.getSheetAt(0) ;
		}
		if(null == sheet) {
			throw new ServiceException("没有找到指定的Sheet页！") ;
		}
		
		int lastRowNum = (endRowIndex==null||endRowIndex<0?sheet.getLastRowNum():endRowIndex) ;
		for(int i=startRowIndex; i<=lastRowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				throw new ServiceException("没有找到指定的行数!") ;
			}
			if(cellNum == null || cellNum < 0) {cellNum = (int) row.getLastCellNum();}
			
			ArrayList<Object> cellList = new ArrayList<Object>();
			if(null != callIndex && callIndex.length > 0) {//指定读取的列		
				for(int j=0; j<=callIndex.length; j++) {
					cellList.add(getCellValue(row.getCell(callIndex[j])));
				}
			} else {
				//读取所有列	
				for(int c=0; c<=cellNum; c++) {
					Cell cell = row.getCell(c) ;
					cellList.add(getCellValue(cell));
				}
			}
			
			rowList.add(cellList) ;
		}
		return rowList;
	}
	
	
	private Object getCellValue(Cell cell) {
		if (cell == null) return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			String str = cell.getRichStringCellValue().getString();
			return str == null ? "" : str.trim();
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) 
				return cell.getDateCellValue();
			else
				return getRightStr(cell.getNumericCellValue()+"");
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_FORMULA:
			if (DateUtil.isCellDateFormatted(cell)) 
				return cell.getDateCellValue();
			else
				return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			return "";
		default:
			System.err.println("数据类型发生错误: " + cell.getCellType());
			return "";
		}
	}
	
	private String getRightStr(String sNum) {
		DecimalFormat decimalFormat = new DecimalFormat("#.###########");
		String resultStr = decimalFormat.format(new Double(sNum));
		if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
			resultStr = resultStr.substring(0, resultStr.indexOf("."));
		}
		return resultStr;
	}
	
	
	public static void main(String[] args) {
		List<ExcelHeader> headerField = getHeaderList(EmpExcelDto.class) ;
		for (ExcelHeader e : headerField) {
			System.out.println(e);
		}
	}
	
}
