package com.ieasy.module.common.web.servlet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class AppConst {

	public static Map<String, Object> GLOBAL_MAP = new HashMap<String, Object>();// 全局参数
	public static Map<String, HttpSession> SESSION_MAP = new HashMap<String, HttpSession>();// 用来索引所有session
	public static int CURRENT_LOGIN_COUNT = 0;					// 当前登录用户总数
	public static int TOTAL_HISTORY_COUNT = 0;					// 历史访客总数
	public static int MAX_ONLINE_COUNT = 0;						// 最大在线访客数量
	public static Date START_DATE = new Date();					// 服务器启动时间
	public static Date MAX_ONLINE_COUNT_DATE = new Date();		// 达到最大访客量的日期

}
