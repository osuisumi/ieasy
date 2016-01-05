package com.ieasy.basic.util.poi.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 该类实现了基于模板的导出
 * 如果要导出序号，需要在excel中定义一个表示为sernums
 * 如果要替换信息，需要传入一个Map，这个map中存储着要替换信息的值，在excel中通过#来开图
 * 要从哪一行哪一列开始替换需要定义一个标示为datas
 * 如果要设定相应的样式，可以再改行使用styles完成设定，此时所有此行都使用该样式
 * 如果使用defaultStyles作为标示，表示默认样式，如果没有defaultStyles使用datas行作为默认样式
 * @author ibm-work
 *
 */
public class ExcelTemplate {
	
	private static Logger logger = Logger.getLogger(ExcelTemplate.class) ;

	private final static String DATA_LINE = "datas" ;						//数据行标示
	private final static String DEFAULT_STYLE = "defaultStyle" ;			//默认样式标示
	private final static String STYLE = "styles" ;							//行样式标示
	private final static String SER_NUM = "sernums" ;						//插入序号标示
	
	private static ExcelTemplate et = new ExcelTemplate() ;
	
	private Workbook wb ;
	private Sheet sheet ;
	private Row curRow ;								//当前的行对象
	
	private int initRowIndex ;							//数据的初始化行数
	private int initCollIndex ;							//数据的初始化列数
	private int curRowIndex ;							//当前行数
	private int curCollIndex ;							//当前列数
	
	private int lastRowIndex ;							//最后一行的数据
	
	private CellStyle defaultStyle ;					//默认样式
	private float rowHeight ;							//默认行高
	
	private Map<Integer, CellStyle> styles ;			//存储某一行所对应的样式
	
	private int serColIndex ;							//序号的列
	
	private ExcelTemplate() {}
	
	public static ExcelTemplate getInstance() {
		return et ;
	}
	
	/**
	 * 从ClassPath路径下读取相应的模板文件
	 * @param path
	 * @return
	 */
	public ExcelTemplate readTemplateByClassPath(String path) {
		try {
			try {
				//处理2007版本以上的excel
				wb = new XSSFWorkbook(ExcelTemplate.class.getClassLoader().getResourceAsStream(path));
			} catch (Exception ex) {
				//处理2007版本以下的excel
				wb = new HSSFWorkbook(ExcelTemplate.class.getClassLoader().getResourceAsStream(path));
			}
			initTemplate() ;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("读取的模板不存在！请检查["+path+"]") ;
		}
		return this ;
	}
	
	
	/**
	 * 根据绝对路径读取相应的模板文件
	 * @param path
	 * @return
	 */
	public ExcelTemplate readTemplateByPath(String path) {
		try {
			try {
				//处理2007版本以上的excel
				wb = new XSSFWorkbook(new FileInputStream(path));
			} catch (Exception ex) {
				//处理2007版本以下的excel
				wb = new HSSFWorkbook(new FileInputStream(path));
			}
			initTemplate() ;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("读取的模板不存在！请检查["+path+"]") ;
		}
		return this ;
	}
	
	/**
	 * 2、初始化模板
	 */
	private void initTemplate() {
		sheet = wb.getSheetAt(0) ;
		initConfigData() ;
		lastRowIndex = sheet.getLastRowNum() ;
		curRow = sheet.createRow(curRowIndex) ;
	}

	/**
	 * 3、初始化数据信息
	 */
	private void initConfigData() {
		boolean findData = false ;
		boolean findSer = false ;
		for(Row row : sheet) {
			if(findData) break ;
			for(Cell c : row) {
				if(c.getCellType() != Cell.CELL_TYPE_STRING) continue ;
				String str = c.getStringCellValue().trim() ;
				
				if(str.equals(SER_NUM)) {
					serColIndex = c.getColumnIndex() ;
					findSer = true ;
				}
				
				if(str.equals(DATA_LINE)) {
					initCollIndex = c.getColumnIndex() ;
					initRowIndex = row.getRowNum() ;
					curCollIndex = initCollIndex ;
					curRowIndex = initRowIndex ;
					findData = true ;
					
					defaultStyle = c.getCellStyle() ;
					rowHeight = row.getHeightInPoints() ;
					
					initStyles() ;
					
					break ;
				}
			}
		}
		if(!findSer) {
			initSer() ;
		}
	}
	
	/**
	 * 4、初始化样式
	 */
	private void initStyles() {
		styles = new HashMap<Integer, CellStyle>() ;
		for(Row row : sheet) {
			for(Cell c : row) {
				if(c.getCellType() != Cell.CELL_TYPE_STRING) continue ;
				String str = c.getStringCellValue().trim() ;
				if(str.equals(DEFAULT_STYLE)) {
					defaultStyle = c.getCellStyle() ;
				}
				if(str.equals(STYLE)) {
					styles.put(c.getColumnIndex(), c.getCellStyle()) ;
				}
			}
		}
	}
	
	/**
	 * 初始化序号
	 */
	private void initSer() {
		for(Row row : sheet) {
			for(Cell c : row) {
				if(c.getCellType() != Cell.CELL_TYPE_STRING) continue ;
				String str = c.getStringCellValue().trim() ;
				if(str.equals(SER_NUM)) {
					serColIndex = c.getColumnIndex() ;
				}
			}
		}
	}
	
	/**
	 * 插入序号
	 */
	public void insertSer() {
		int index = 1 ;	
		Row row = null ;
		Cell c = null ;
		for(int i=initRowIndex; i<curRowIndex; i++) {
			row = sheet.getRow(i) ;
			c = row.createCell(serColIndex) ;
			setCellStyle(c) ;
			c.setCellValue(index++) ;
		}
	}
	
	/**
	 * 创建行
	 */
	public void createNewRow() {
		if(lastRowIndex >= curRowIndex && curRowIndex != initRowIndex) {
			sheet.shiftRows(curRowIndex, lastRowIndex, 1, true, true) ;
			lastRowIndex ++ ;
		}
		curRow = sheet.createRow(curRowIndex) ;
		curRow.setHeightInPoints(rowHeight) ;
		curRowIndex++ ;
		curCollIndex = initCollIndex ;
	}
	
	/**
	 * 创建列
	 * @param value
	 */
	public void createCell(Object value) {
		Cell c = curRow.createCell(curCollIndex) ;
		setCellStyle(c) ;
		setValue(c, value);
		curCollIndex++ ;
	}
	
	private void setValue(Cell c, Object value) {
		if(value instanceof String) {
			c.setCellValue((String)value) ;
		} else if(value instanceof Integer) {
			c.setCellValue((int)value) ;
		} else if(value instanceof Double) {
			c.setCellValue((double)value) ;
		} else if(value instanceof Boolean) {
			c.setCellValue((boolean)value) ;
		} else if(value instanceof Date) {
			c.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format((Date)value)) ;
		} else if(value == null) {
			c.setCellValue("NULL") ; return ;
		} else {
			throw new RuntimeException("无法处理该单元格的数据，或类型不正确！请检查") ;
		}
	}
	
	/**
	 * 设置样式
	 * @param c
	 */
	private void setCellStyle(Cell c) {
		if(styles.containsKey(curCollIndex)) {
			c.setCellStyle(styles.get(curCollIndex)) ;
		} else {
			c.setCellStyle(defaultStyle) ;
		}
	}
	
	/**
	 * 根据map替换相应的常量，通过map中的值来替换#开头的值
	 * @param datas
	 */
	public void replaceFinalData(Map<String, String> datas) {
		if(null == datas) return ;
		for(Row row : sheet) {
			for(Cell c : row) {
				if(c.getCellType() != Cell.CELL_TYPE_STRING) continue ;
				String str = c.getStringCellValue().trim() ;
				if(str.startsWith("#")) {
					if(datas.containsKey(str.substring(1))) {
						c.setCellValue(datas.get(str.substring(1))) ;
					}
				}
			}
		}
	}
	
	/**
	 * 基于Properites的替换，依然也是替换#开始的
	 * @param datas
	 */
	public void replaceFinalData(Properties prop) {
		if(null == prop) return ;
		for(Row row : sheet) {
			for(Cell c : row) {
				if(c.getCellType() != Cell.CELL_TYPE_STRING) continue ;
				String str = c.getStringCellValue().trim() ;
				if(str.startsWith("#")) {
					if(prop.containsKey(str.substring(1))) {
						c.setCellValue(prop.getProperty(str.substring(1))) ;
					}
				}
			}
		}
	}
	
	
	/**
	 * 讲文件写出到相应的路径下
	 * @param path
	 */
	public void writeToFile(String path) {
		FileOutputStream fos = null ;
		try {
			fos = new FileOutputStream(path) ;
			wb.write(fos) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("写入的文件不存在") ;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("写入数据失败：" + e.getMessage()) ;
		} finally {
			try {
				if(fos != null) fos.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将文件写到某个输出流中
	 * @param os
	 */
	public void writeToStream(OutputStream os) {
		try {
			wb.write(os) ;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("写入流失败：" + e.getMessage()) ;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
