package com.ieasy.basic.util.poi.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用来在对象的get方法上加入的annotation,通过该annotation说明某个属性所对应的标题
 * @author ibm-work
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelResources {
	
	String title() ;					//标题的名称
	int order() default 9999 ; 			//标题在Excel的排序
	
}
