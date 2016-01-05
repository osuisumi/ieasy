package com.ieasy.module.common.web.servlet;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		//记录启动时间
		AppConst.START_DATE = new Date();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		//清空结果
		AppConst.START_DATE = null;
		AppConst.MAX_ONLINE_COUNT_DATE = null;
	}
	

}
