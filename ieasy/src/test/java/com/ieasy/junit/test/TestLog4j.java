package com.ieasy.junit.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ieasy.basic.util.LogUtil;

public class TestLog4j {
	
	private static Logger logger = Logger.getLogger(TestLog4j.class) ;

	@Test
	public void testLog4jPath() {
		//如果要设置路径，必须在Logger.getLogger之前设置，否则无效
		System.setProperty("LOG_DIR", "c:\\");
		logger.debug("debug") ;
		logger.info("info") ;
		logger.warn("warn") ;
		logger.error("error") ;
		
		LogUtil.info("1111111111111111") ;
	}
	
}
