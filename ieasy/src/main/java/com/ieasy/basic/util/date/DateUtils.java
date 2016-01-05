package com.ieasy.basic.util.date;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateUtils {

	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_TIME = "HH:mm:ss";
	public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_DATE_ZH = "yyyy年MM月dd日";
	public final static String FORMAT_DATETIME_ZH = "yyyy年MM月dd日 HH时mm分ss秒";

	public final static String TYPE_DATE = "date";
	public final static String TYPE_TIME = "time";
	public final static String TYPE_DATETIME = "datetime";

	/**
	 * 日期排序类型-升序
	 */
	public final static int DATE_ORDER_ASC = 0;

	/**
	 * 日期排序类型-降序
	 */
	public final static int DATE_ORDER_DESC = 1;

	public static String formatYYYYMMDD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		return sdf.format(date);
	}

	public static String formatYYYYMMDD_HHMMSS(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
		return sdf.format(date);
	}

	public static Date formatYYYYMMDD(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date formatYYYYMMDD_HHMMSS(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期类型转字符串类型
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String dateToString(Date date, String dateFormat) {
		if (date == null)
			return "";
		try {
			return new SimpleDateFormat(dateFormat).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 用字符串获得日期
	 * 
	 * @throws ParseException
	 * @dateValue 日期字符串
	 * @dateType 格式化的类型,date和datetime
	 */
	public static Date getDate(String dateValue, String dateType) throws ParseException {
		if (dateValue == null)
			return null;
		if (dateType.equals(TYPE_TIME)) {
			SimpleDateFormat sfdate = new SimpleDateFormat(FORMAT_TIME);
			return sfdate.parse(dateValue);
		} else if (dateType.equals(TYPE_DATE)) {
			SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATE);
			return sftime.parse(dateValue);
		} else if (dateType.equals(TYPE_DATETIME)) {
			SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATETIME);
			return sftime.parse(dateValue);
		}
		return null;
	}

	/**
	 * (将日期加上某些天或减去天数)返回字符串
	 * 
	 * @param date
	 *            待处理日期
	 * @param to
	 *            加减的天数
	 * @return 日期
	 */
	public static Date dateAdd(String date, int to) {
		java.util.Date d = null;
		try {
			d = java.sql.Date.valueOf(date);
		} catch (Exception e) {
			e.printStackTrace();
			d = new java.util.Date();
		}
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(d);
		strDate.add(Calendar.DATE, to); // 日期减 如果不够减会将月变动
		return strDate.getTime();
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期对象
	 * @param splitChar
	 *            分隔字符
	 * @return
	 */
	public static String formatDate(Date date, String splitChar, String dateType) {
		if (dateType.equals(TYPE_DATE)) {
			java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat("yyyy" + splitChar + "MM" + splitChar + "dd");
			return sfdate.format(date);
		} else if (dateType.equals(TYPE_TIME)) {
			java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat("HH" + splitChar + "mm" + splitChar + "ss");
			return sfdate.format(date);
		} else {
			java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat("yyyy" + splitChar + "MM" + splitChar + "dd");
			return sfdate.format(date);
		}
	}

	/**
	 * @dateValue 日期对象，可以是java.util.Date和java.sql.Date
	 * @dateType 格式化的类型,date和datetime
	 */
	public static String format(Object dateValue, String dateType) {
		if (dateValue == null)
			return "";
		if (dateValue instanceof java.sql.Date) {
			return dateValue.toString();
		} else if (dateValue instanceof java.util.Date) {
			if (dateType.equals(TYPE_TIME)) {
				java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat(FORMAT_TIME);
				return sfdate.format(dateValue);
			} else if (dateType.equals(TYPE_DATE)) {
				java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat(FORMAT_DATE);
				return sfdate.format(dateValue);
			} else if (dateType.equals(TYPE_DATETIME)) {
				java.text.SimpleDateFormat sftime = new java.text.SimpleDateFormat(FORMAT_DATETIME);
				return sftime.format(dateValue);
			} else {
				return "非法日期格式[" + dateType + "]";
			}
		} else {
			return "非日期类型";
		}
	}

	/**
	 * 转换日期对象为中文化日期
	 * 
	 * @dateValue 日期对象，可以是java.util.Date和java.sql.Date
	 * @dateType 格式化的类型,date和datetime
	 */
	public static String formatZh(Date dateValue, String dateType) {
		if (dateValue == null)
			return "";
		if (dateValue instanceof java.sql.Date) {
			return dateValue.toString();
		} else if (dateValue instanceof java.util.Date) {
			if (dateType.equals(TYPE_DATE)) {
				java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat(FORMAT_DATE_ZH);
				return sfdate.format(dateValue);
			} else if (dateType.equals(TYPE_DATETIME)) {
				java.text.SimpleDateFormat sftime = new java.text.SimpleDateFormat(FORMAT_DATETIME_ZH);
				return sftime.format(dateValue);
			} else {
				return "非法日期格式[" + dateType + "]";
			}
		} else {
			return "非日期类型";
		}
	}

	/**
	 * 转化成年月日期
	 * 
	 * @param sDate
	 *            字符型日期：2009-02-02
	 * @param DelimeterChar
	 *            分割符号比如 / -
	 * @return 年月日期 :2009年02月02日
	 */
	public static String chDateChange(String sDate, String DelimeterChar) {
		String tmpArr[] = sDate.split(DelimeterChar);
		tmpArr[0] = tmpArr[0] + "年";
		tmpArr[1] = tmpArr[1] + "月";
		tmpArr[2] = tmpArr[2] + "日";
		return tmpArr[0] + tmpArr[1] + tmpArr[2];
	}

	/**
	 * 得到系统日期
	 * 
	 * @return YYYY-MM-DD
	 */
	public static String getSysDateStr() {
		java.sql.Timestamp timeNow = new java.sql.Timestamp(System.currentTimeMillis());
		return timeNow.toString().substring(0, 10);
	}

	/**
	 * 取得系统时间【年月日时分秒】 方法描述 : 创建者：杨浩泉 项目名称： easy 类名： DateUtils.java 版本： v1.0 创建时间： 2014-5-28 上午9:25:39
	 * 
	 * @return String
	 */
	public static String getSysDateTimeStr() {
		return getSysDateStr() + " " + getSystemTime();
	}

	/**
	 * 获取当前系统时间，24小时制
	 * 
	 * @return 当前系统时间
	 */
	public static Time getSystemTime() {
		Calendar c1 = Calendar.getInstance();
		int hour = c1.get(Calendar.HOUR_OF_DAY);
		int minute = c1.get(Calendar.MINUTE);
		int second = c1.get(Calendar.SECOND);
		Time systemTime = Time.valueOf(hour + ":" + minute + ":" + second);
		return systemTime;
	}

	/**
	 * 取得系统时间【年月日时分秒】 方法描述 : 创建者：杨浩泉 项目名称： easy 类名： DateUtils.java 版本： v1.0 创建时间： 2014-5-28 上午9:26:08
	 * 
	 * @return Date
	 */
	public static Date getSysDateTime() {
		try {
			return getDate(getSysDateStr() + " " + getSystemTime(), TYPE_DATETIME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到某天是周几
	 * 
	 * @param strDay
	 * @return 周几
	 */
	public static int getWeekDay(String strDay) {
		Date day = DateUtils.dateAdd(strDay, -1);
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(day);
		int meStrDate = strDate.get(Calendar.DAY_OF_WEEK);
		return meStrDate;
	}

	/**
	 * 得到某天是周几
	 * 
	 * @param strDay
	 * @return 周几
	 */
	public static int getWeekDay(Date date) {
		Date day = DateUtils.dateAdd(format(date, "date"), -1);
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(day);
		int meStrDate = strDate.get(Calendar.DAY_OF_WEEK);
		return meStrDate;
	}

	/**
	 * 取得两个日期段的日期间隔
	 * 
	 * @author color
	 * @param t1
	 *            时间1
	 * @param t2
	 *            时间2
	 * @return t2 与t1的间隔天数
	 * @throws ParseException
	 *             如果输入的日期格式不是0000-00-00 格式抛出异常
	 */
	public static int getBetweenDays(String t1, String t2) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			int betweenDays = 0;
			Date d1 = format.parse(t1);
			Date d2 = format.parse(t2);
			betweenDays = getBetweenDays(d1, d2);
			return betweenDays;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 取得两个日期段的日期间隔
	 * 
	 * @author color
	 * @param t1
	 *            时间1
	 * @param t2
	 *            时间2
	 * @param swapDate
	 *            当日期1小于日期2时是否交换两个日期值
	 * @return t2 与t1的间隔天数
	 * @throws ParseException
	 *             如果输入的日期格式不是0000-00-00 格式抛出异常
	 */
	public static int getBetweenDays(String t1, String t2, boolean swapDate) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			int betweenDays = 0;
			Date d1 = format.parse(t1);
			Date d2 = format.parse(t2);
			betweenDays = getBetweenDays(d1, d2, swapDate);
			return betweenDays;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 取得两个日期段的日期间隔
	 * 
	 * @param d1
	 *            日期1
	 * @param d2
	 *            日期2
	 * @param swapDate
	 *            当日期1小于日期2时是否交换两个日期值
	 * @return t2 与t1的间隔天数
	 */
	public static int getBetweenDays(Date d1, Date d2, boolean swapDate) {
		if (d1 == null || d2 == null) {
			return -1;
		}
		int betweenDays;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		if (swapDate) {
			// 保证第二个时间一定大于第一个时间
			if (c1.after(c2)) {
				c2.setTime(d1);
				c1.setTime(d2);
			}
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	/**
	 * 取得两个日期段的日期间隔
	 * 
	 * @param d1
	 *            日期1
	 * @param d2
	 *            日期2
	 * @return t2 与t1的间隔天数
	 */
	public static int getBetweenDays(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return -1;
		}
		int betweenDays;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		// 保证第二个时间一定大于第一个时间
		if (c1.after(c2)) {
			c2.setTime(d1);
			c1.setTime(d2);
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	/**
	 * 判断指定日期是否在一个日期范围内
	 * 
	 * @param fromDate
	 *            范围开始日期
	 * @param toDate
	 *            范围结束日期
	 * @param testDate
	 *            测试日期
	 * @return 在范围内true,否则false
	 */
	public static boolean betweenDays(java.sql.Date fromDate, java.sql.Date toDate, java.sql.Date testDate) {
		if (fromDate == null || toDate == null || testDate == null) {
			return false;
		}

		// 1、 交换开始和结束日期
		if (fromDate.getTime() > toDate.getTime()) {
			java.sql.Date tempDate = fromDate;
			fromDate = toDate;
			toDate = tempDate;
		}

		// 2、缩小范围
		long testDateTime = testDate.getTime();
		if ((testDateTime > fromDate.getTime() && testDateTime > toDate.getTime()) || testDateTime < fromDate.getTime() && testDateTime < toDate.getTime()) {
			return false;
		}

		return true;
	}

	/**
	 * 判断两个日期是否为同一天
	 * 
	 * @param d1
	 *            日期一
	 * @param d2
	 *            日期二
	 * @return 同一天true，不是同一天false
	 */
	public static boolean isSameDate(Date d1, Date d2) {
		boolean result = false;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
			result = true;
		}
		return result;
	}

	/**
	 * 是否为周末
	 * 
	 * @param strDate
	 * @return true|false
	 */
	public static boolean isWeekend(String strDate) {
		int weekDay = getWeekDay(strDate);
		if (weekDay == 6 || weekDay == 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为周末
	 * 
	 * @param strDate
	 * @return true|false
	 */
	public static boolean isWeekend(Date date) {
		int weekDay = getWeekDay(format(date, "date"));
		if (weekDay == 6 || weekDay == 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 日期排序
	 * 
	 * @param dates
	 *            日期列表
	 * @param orderType
	 *            排序类型 <br/>
	 *            {@link #DATE_ORDER_ASC}<br/>
	 *            {@link #DATE_ORDER_DESC}
	 * @return 排序结果
	 */
	public static List<? extends java.util.Date> orderDate(List<? extends java.util.Date> dates, int orderType) {
		DateComparator comp = new DateComparator(orderType);
		Collections.sort(dates, comp);
		return dates;
	}

	/**
	 * 日期分组<br/>
	 * 能够对指定日期列表按照连续性分组<br/>
	 * 例如：[2010-01-15, 2010-01-16, 2010-01-17, 2010-01-20, 2010-01-21, 2010-01-25]<br/>
	 * 分组结果为：<br/>
	 * <ul>
	 * <li>[2010-01-15, 2010-01-16, 2010-01-17]</li>
	 * <li>[2010-01-20, 2010-01-21]</li>
	 * <li>[2010-01-25]</li>
	 * </ul>
	 * 
	 * @param dates
	 *            日期对象
	 * @return 连续性分组结果
	 */
	public static List<List<? extends java.util.Date>> groupDates(List<? extends java.util.Date> dates) {
		List<List<? extends java.util.Date>> result = new ArrayList<List<? extends java.util.Date>>();

		// 按照升序排序
		orderDate(dates, DateUtils.DATE_ORDER_ASC);

		// 临时结果
		List<Date> tempDates = null;

		// 上一组最后一个日期
		Date lastDate = null;

		// 当前读取日期
		Date cdate = null;
		for (int i = 0; i < dates.size(); i++) {
			cdate = dates.get(i);

			// 第一次增加
			if (tempDates == null) {
				tempDates = new ArrayList<Date>();
				tempDates.add(cdate);
				result.add(tempDates);
			} else {
				/**
				 * 差距为1是继续在原有的列表中添加，大于1就是用新的列表
				 */
				lastDate = tempDates.get(tempDates.size() - 1);
				int days = getBetweenDays(lastDate, cdate);
				if (days == 1) {
					tempDates.add(cdate);
				} else {
					tempDates = new ArrayList<Date>();
					tempDates.add(cdate);
					result.add(tempDates);
				}
			}

		}

		return result;
	}

	/**
	 * 将出生日期与当前日期相减，获得年龄
	 * 
	 * @param birthdayDate
	 * @return
	 */
	public static int getAge(Date birthdayDate) {
		String formatCurrent = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		int firstCu = formatCurrent.indexOf("-");
		int lastCu = formatCurrent.lastIndexOf("-");
		String currentYearStr = formatCurrent.substring(0, firstCu);
		String currentMonthStr = formatCurrent.substring(firstCu + 1, lastCu);
		String currentDayStr = formatCurrent.substring(lastCu + 1);
		int currentYear = Integer.valueOf(currentYearStr);
		int currentMonth = Integer.valueOf(currentMonthStr);
		int currentDay = Integer.valueOf(currentDayStr);

		String formatBirthday = new SimpleDateFormat("yyyy-MM-dd").format(birthdayDate);

		int first = formatBirthday.indexOf("-");
		int last = formatBirthday.lastIndexOf("-");
		String birthYearStr = formatBirthday.substring(0, first);
		String birthMonthStr = formatBirthday.substring(first + 1, last);
		String birthDayStr = formatBirthday.substring(last + 1);

		int birthYear = Integer.valueOf(birthYearStr);
		int birthMonth = Integer.valueOf(birthMonthStr);
		int birthDay = Integer.valueOf(birthDayStr);

		if (currentMonth > birthMonth) {
			return currentYear - birthYear;
		} else if (currentMonth == birthMonth) {
			if (currentDay >= birthDay) {
				return currentYear - birthYear;
			} else {
				return currentYear - birthYear - 1;
			}
		} else {
			return currentYear - birthYear - 1;
		}
	}

	/**
	 * 获取当前年份
	 */
	public static Integer getCurrentYear() {
		Calendar ca = Calendar.getInstance();
		return ca.get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前月份
	 */
	public static Integer getCurrentMonth() {
		Calendar ca = Calendar.getInstance();
		return ca.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日
	 */
	public static Integer getCurrentDay() {
		Calendar ca = Calendar.getInstance();
		return ca.get(Calendar.DATE);
	}
	
	/**
	 * 根据指定日期获取年份
	 */
	public static Integer getYear(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date) ;
		return ca.get(Calendar.YEAR);
	}
	
	/**
	 * 根据指定日期获取月份
	 */
	public static Integer getMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date) ;
		return ca.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 根据指定日期获取日
	 */
	public static Integer getDay(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date) ;
		return ca.get(Calendar.DATE);
	}

	/**
	 * 计算年龄精确到年月日
	 * 
	 * @param birthday
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Map<String, Object> getBabyAge(String birthday) {
		Map<String, Object> map = new HashMap<String, Object>();
		String age = "";
		int day = 0;
		int y = 0;
		int m = 0;
		int d = 0;

		if (birthday != null && birthday.length() == 10) {
			String[] time = birthday.split("-");
			y = Integer.parseInt(time[0]);
			m = Integer.parseInt(time[1]);
			d = Integer.parseInt(time[2]);

			Calendar selectDate = Calendar.getInstance();
			Calendar currentDate = Calendar.getInstance();
			selectDate.set(Calendar.YEAR, y);
			selectDate.set(Calendar.MONTH, m - 1);
			selectDate.set(Calendar.DAY_OF_MONTH, d);
			// 上一个月
			int lastMonth = (currentDate.get(Calendar.MONTH) + 1) - 1;
			int years = currentDate.get(Calendar.YEAR) - selectDate.get(Calendar.YEAR);
			int months = currentDate.get(Calendar.MONTH) - selectDate.get(Calendar.MONTH);
			int days = currentDate.get(Calendar.DAY_OF_MONTH) - selectDate.get(Calendar.DAY_OF_MONTH);
			if (days < 0) {
				months = months - 1;
				switch (lastMonth) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					day = 31;
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					day = 30;
					break;
				default:
					if (Calendar.YEAR % 4 == 0 && Calendar.YEAR % 100 != 0 || Calendar.YEAR % 400 == 0) {
						day = 28;
					} else {
						day = 29;
					}
					break;
				}
				days = days + day;
			}
			if (months < 0) {
				years = years - 1;
				months = months + 12;
			}
			if (years < 0) {
				years = 0;
			}
			if (years == 0) {
				if (months == 0) {
					if (days == 0) {
						age = "今天是宝宝的出生日期";
					} else {
						age = days + "天";
					}
				} else {
					if (days == 0) {
						age = months + "个月";
					} else {
						age = months + "个月又" + days + "天";
					}
				}
			} else {
				if (months == 0) {
					if (days == 0) {
						age = years + "岁";
					} else {
						age = years + "岁又" + days + "天";
					}
				} else {
					if (days == 0) {
						age = years + "岁" + months + "个月";
					} else {
						age = years + "岁" + months + "个月又" + days + "天";
					}

				}
			}

			map.put("age", age);
			map.put("years", years);
			map.put("months", months);
			map.put("days", days);
			System.out.println(age);
		}
		return map;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期相差的【年-月-日 时:分:秒】 方法描述 : 创建者：杨浩泉 项目名称： easy 类名： DateUtil.java 版本： v1.0 创建时间： 2014-5-28 上午8:35:59
	 * 
	 * @param startTime
	 *            2014-05-14 22:22:22
	 * @param endTime
	 *            2014-06-15 23:32:32
	 * @param format
	 *            yyyy-MM-dd HH:mm:ss
	 * @return Map<String,Object> 天，时，分，秒
	 */
	public static Map<String, Long> dateDiff(String startTime, String endTime, String format) {
		Map<String, Long> map = new HashMap<String, Long>();

		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60; // 一天的毫秒数
		long nh = 1000 * 60 * 60; // 一小时的毫秒数
		long nm = 1000 * 60; // 一分钟的毫秒数
		long ns = 1000; // 一秒钟的毫秒数
		long diff;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;

		// 获得两个时间的毫秒时间差异
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			day = diff / nd; // 计算差多少天
			hour = diff % nd / nh + day * 24; // 计算差多少小时
			min = diff % nd % nh / nm + day * 24 * 60; // 计算差多少分钟
			sec = diff % nd % nh % nm / ns; // 计算差多少秒

			// System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时" + (min - day * 24 * 60) + "分钟" + sec + "秒。");

			map.put("day", day);
			map.put("hour", (hour - day * 24));
			map.put("min", (min - day * 24 * 60));
			map.put("sec", sec);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String dateDiffStr(String startTime, String endTime, String format) {
		String diffStr = "";
		Map<String, Long> map = dateDiff(startTime, endTime, format);

		if (null != map && map.size() > 0) {
			if (map.get("day") > 0) {
				diffStr += map.get("day") + "天 ";
			}
			if (map.get("hour") > 0) {
				diffStr += map.get("hour") + "时 ";
			}
			if (map.get("min") > 0) {
				diffStr += map.get("min") + "分 ";
			}
			if (map.get("sec") > 0) {
				diffStr += map.get("sec") + "秒 ";
			}
		}
		if ("".trim().equals(diffStr)) {
			diffStr = "0秒";
		}

		return diffStr;
	}

	/**
	 * 根据指定日期获取相处的天数，包含今天
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getDutyDays(String strStartDate, String strEndDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;

		try {
			startDate = df.parse(strStartDate);
			endDate = df.parse(strEndDate);
		} catch (ParseException e) {
			System.out.println("非法的日期格式,无法进行转换");
			e.printStackTrace();
		}
		int result = 0;
		while (startDate.compareTo(endDate) <= 0) {
			if (startDate.getDay() != 6 && startDate.getDay() != 0)
				result++;
			startDate.setDate(startDate.getDate() + 1);
		}

		return result;
	}
	
	/**
	 * 获取年份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfYear(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatYYYYMMDD(date));
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	/**
	 * 获取年份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year) ;
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return cal.getTime();
	}


	/**
	 * 获取年份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfYear(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatYYYYMMDD(date));
		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}
	
	/**
	 * 获取年份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year) ;
		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}

	/**
	 * 月份的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatYYYYMMDD(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	/**
	 * 月份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatYYYYMMDD(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}

	/**
	 * 月份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}
	
	/**
	 * 比较两个日期大小和时间
	 * @param fristDate
	 * @param enddate
	 * @return 返回-1、0、1（小于、相等、大于）
	 */
	public static int compare_date(String fristDate, String enddate) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = df.parse(fristDate);
			Date d2 = df.parse(enddate);
			int ss = d1.compareTo(d2);
			return ss;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 比较两个日期大小和时间
	 * @param fristDate
	 * @param enddate
	 * @return 返回-1、0、1（小于、相等、大于）
	 */
	public static int compare_date(Date fristDate, Date enddate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = formatYYYYMMDD(df.format(fristDate)) ;
		Date d2 = formatYYYYMMDD(df.format(enddate)) ;
		int ss = d1.compareTo(d2);
		return ss;
	}
	
	/**
	 * 比较两个日期大小和时间
	 * 
	 * @param fristDate
	 * @param enddate
	 * @return 返回-1、0、1（小于、相等、大于）
	 */
	public static int compare_datetime(String fristDate, String enddate) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d1 = df.parse(fristDate);
			Date d2 = df.parse(enddate);
			int ss = d1.compareTo(d2);
			return ss;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 比较两个日期大小和时间
	 * 
	 * @param fristDate
	 * @param enddate
	 * @return 返回-1、0、1（小于、相等、大于）
	 */
	public static int compare_datetime(Date fristDate, Date enddate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d1 = formatYYYYMMDD_HHMMSS(df.format(fristDate)) ;
		Date d2 = formatYYYYMMDD_HHMMSS(df.format(enddate)) ;
		int ss = d1.compareTo(d2);
		return ss;
	}
	
	/**
	 * 根据指定日期计算所在周的周一和周日
	 * 
	 * @param time
	 * @return
	 */
	public static String[] convertWeekByDate(String date) {
		String[] array = new String[2];

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatYYYYMMDD(date));
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天

		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		array[0] = sdf.format(cal.getTime());

		cal.add(Calendar.DATE, 6);
		array[1] = sdf.format(cal.getTime());

		return array;
	}

	/**
	 * 判定日期是否在指定范围
	 * 
	 * @param datetime
	 * @param startDatetime
	 * @param endDatetime
	 * @return
	 */
	public static boolean datetimeScope(String datetime, String startDatetime, String endDatetime, String dateType) {
		try {
			Calendar curtime = Calendar.getInstance();
			curtime.setTime(getDate(datetime, dateType));

			Calendar sTime = Calendar.getInstance();
			sTime.setTime(getDate(datetime, dateType));

			Calendar eTime = Calendar.getInstance();
			eTime.setTime(getDate(datetime, dateType));

			if (curtime.getTimeInMillis() >= sTime.getTimeInMillis() && curtime.getTimeInMillis() <= eTime.getTimeInMillis()) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 解析日期文本与format刚好相反
	 * @param text String
	 * @param pattern String 
	 * @return Date
	 */
	public static Date parse(String text, String pattern){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		ParsePosition pos = new ParsePosition(0);
		Date date = simpleDateFormat.parse(text, pos);
		if(pos.getIndex() < text.length()){
			throw new RuntimeException("错误索引信息 : " + pos.getErrorIndex());
		}
		return date;
	}
	public static Date parse(String text, String pattern, TimeZone timeZone, Locale locale){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
		simpleDateFormat.setTimeZone(timeZone);
		ParsePosition pos = new ParsePosition(0);
		Date date = simpleDateFormat.parse(text, pos);
		if(pos.getIndex() < text.length()){
			throw new RuntimeException("错误索引信息 : " + pos.getErrorIndex());
		}
		return date;
	}
	
	/**
	 * 将日期转换为quartz表达式Cron
	 * @param datetime
	 * @return
	 * @throws ParseException
	 */
	public static String convertDateToCron(String datetime) {
		try {
			Date dt = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").parse(datetime) ;
			Calendar sc = Calendar.getInstance() ;
			sc.setTime(dt) ;
			return sc.get(Calendar.MINUTE)+" "+sc.get(Calendar.SECOND)+" "+sc.get(Calendar.HOUR_OF_DAY)+" "+sc.get(Calendar.DAY_OF_MONTH)+" "+(sc.get(Calendar.MONTH)+1)+" ? " ;
		} catch (ParseException e) {
			throw new RuntimeException("传入的日期格式不正确！") ;
		}
	}
	
	/**
	 * 获取指定月份工作天数（排除周六日）
	 * @param month
	 * @return
	 */
	public static int getMonthWorkDay(String date) {
		return getWorkingDay(getFirstDayOfMonth(date), getLastDayOfMonth(date));
	}
	
	/**
	 * 获取指定月份工作天数（排除周六日）
	 * @param month
	 * @return
	 */
	public static int getMonthWorkDay(int month) {
		try {
			Calendar fristDay = Calendar.getInstance();
			fristDay.setTime(new SimpleDateFormat("yyyy").parse(new SimpleDateFormat("yyyy").format(new Date())));
			fristDay.add(Calendar.MONTH, month-1 );
			fristDay.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			
			Calendar lastDay = Calendar.getInstance();
			lastDay.setTime(new SimpleDateFormat("yyyy").parse(new SimpleDateFormat("yyyy").format(new Date())));
			lastDay.add(Calendar.MONTH, month-1 );
			lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			int workDay = DateCal.getWorkingDay(fristDay, lastDay);
			
			return workDay;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0 ;
	}
	
	/**
	 * 获取当前年的工作天数，排除周六日
	 * @return
	 */
	public static int getCurrentYearWorkDay() {
		int allWrokDay = 0 ;
		try {
			for (int month = 1; month <= 12; month++) {
				Calendar fristDay = Calendar.getInstance();
				fristDay.setTime(new SimpleDateFormat("yyyy").parse(new SimpleDateFormat("yyyy").format(new Date())));
				fristDay.add(Calendar.MONTH, month-1 );
				fristDay.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				
				Calendar lastDay = Calendar.getInstance();
				lastDay.setTime(new SimpleDateFormat("yyyy").parse(new SimpleDateFormat("yyyy").format(new Date())));
				lastDay.add(Calendar.MONTH, month-1 );
				lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				int diff = DateCal.getWorkingDay(fristDay, lastDay);
				allWrokDay += diff ;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return allWrokDay ;
	}

	/**
	 * 根据两个日期计算每月的天数，并且除去周六日，不包含今天
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getWorkDay(String startDate, String endDate) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar endGC = new GregorianCalendar();
		long times, days1 = 0l;
		try {
			times = sd.parse(endDate).getTime() - sd.parse(startDate).getTime();
			long days = times / (1000 * 24 * 60 * 60);
			
			days1 = (days / 7) * 5;
			
			long days2 = days % 7;
			endGC.setTime(sd.parse(endDate));
			int weekDay = endGC.get(Calendar.DAY_OF_WEEK);
			if (weekDay == 1) {
				days1 += days2 > 2 ? days2 - 2 : 0;
			} else if (weekDay == 7) {
				days1 += days2 > 1 ? days2 - 1 : 0;
			} else if (weekDay - 1 < days2) {
				days1 += days2 - 2;
			} else if (weekDay - 1 > days2) {
				days1 += days2;
			} else if (weekDay - 1 == days2) {
				days1 += weekDay - 1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days1;
	}
	
	/******************************************************************************************************************/
	public static int getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 计算2个日期之间的相隔天数
	 * 工作日
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getWorkingDay(java.util.Calendar d1, java.util.Calendar d2) {
		int result = -1;
		if (d1.after(d2)) { //交换日期，D1和D2是结束的开始
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}

		int charge_start_date = 0;// 开始日期的日期偏移量
		int charge_end_date = 0;// 结束日期的日期偏移量
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			//不包含今天
			//charge_start_date = stmp - 1;
			//包含今天
			charge_start_date = stmp;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			charge_end_date = etmp - 1;
		}
		result = (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7) * 5 + charge_start_date - charge_end_date;
		return result;
	}
	public static int getWorkingDay(Date ds, Date de) {
		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		cal_start.setTime(ds);
		cal_end.setTime(de);
		return getWorkingDay(cal_start, cal_end);
	}
	public static int getWorkingDay(String strDateStart, String strDateEnd) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date_start = sdf.parse(strDateStart);
			Date date_end = sdf.parse(strDateEnd);

			Calendar cal_start = Calendar.getInstance();
			Calendar cal_end = Calendar.getInstance();
			cal_start.setTime(date_start);
			cal_end.setTime(date_end);
			return getWorkingDay(cal_start, cal_end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getChineseWeek(Calendar date) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/******************************************************************************************************************/

	/**
	 * 时间间隔计算
	 * 
	 */
	public static String getDaysBeforeNow(Date date) {
		long sysTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		long ymdhms = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
		String strYear = "年前";
		String strMonth = "月前";
		String strDay = "天前";
		String strHour = "小时前";
		String strMinute = "分钟前";
		try {
			if (ymdhms == 0) {
				return "";
			}
			long between = (sysTime / 10000000000L) - (ymdhms / 10000000000L);
			if (between > 0) {
				return between + strYear;
			}
			between = (sysTime / 100000000L) - (ymdhms / 100000000L);
			if (between > 0) {
				return between + strMonth;
			}
			between = (sysTime / 1000000L) - (ymdhms / 1000000L);
			if (between > 0) {
				return between + strDay;
			}
			between = (sysTime / 10000) - (ymdhms / 10000);
			if (between > 0) {
				return between + strHour;
			}
			between = (sysTime / 100) - (ymdhms / 100);
			if (between > 0) {
				return between + strMinute;
			}
			return "1" + strMinute;
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) throws ParseException {
		String sd1 = "2014-09-08";
		String ed2 = "2014-09-10";

		String sdt1 = "2014-09-12 10:05:10";

		// System.out.println("根据指定日期获取相处的天数，包含今天：" + getDutyDays("2014-09-08", "2014-09-18"));
		System.out.println("两个日期之间的工作天数（包含今天）-->" + getWorkingDay(sd1, ed2));

		System.out.println("两个日期之间的工作天数（不包含今天）-->" + getWorkDay(sd1, ed2));

		System.out.println("根据指定日期获得年的第一天-->" + formatYYYYMMDD(getFirstDayOfYear(sd1)));
		System.out.println("根据指定日期获得月的第一天-->" + formatYYYYMMDD(getFirstDayOfMonth(sd1)));

		System.out.println("根据指定日期获得年的最后一天-->" + getLastDayOfYear(sd1));
		System.out.println("根据指定日期获得月的最后一天-->" + getLastDayOfMonth(sd1));

		System.out.println("指定日期（加天数）得到的日期：" + formatYYYYMMDD(dateAdd(sd1, 3)));
		System.out.println("指定日期（减天数）得到的日期：" + formatYYYYMMDD(dateAdd(sd1, -3)));

		System.out.println("将日期转换为quartz的表达式-->" + convertDateToCron(sdt1));
		System.out.println("获得指定月的工作天数-->" + getMonthWorkDay(9));
		System.out.println("根据日期获得该月的工作天数-->" + getMonthWorkDay("2014-09-22"));
		System.out.println("当前年的工作天数-->" + getCurrentYearWorkDay());

		System.out.println(getDaysBeforeNow(formatYYYYMMDD(sdt1)));
		
		System.out.println(getCurrentYear() + "==" + getCurrentMonth() + "==" + getCurrentDay()); 
		
		System.out.println(getWorkingDay("2014-10-09", "2014-11-08"));
		System.out.println(getBetweenDays("2014-10-09", "2014-11-08"));
		
		System.out.println(getYear(formatYYYYMMDD("2012-12-09")));
		System.out.println(getMonth(formatYYYYMMDD("2012-12-09")));
		System.out.println(getDay(formatYYYYMMDD("2012-10-09")));
		
		System.out.println(formatYYYYMMDD(getFirstDayOfYear(2014)));
		
	}

}

/**
 * <p>
 * <b>Title：</b>日期大小比较
 * </p>
 * <p>
 * <b>Description：</b>实现比较接口，按照排序类型[升序,降序]排列日期集合
 * </p>
 * 
 * @author 闫洪磊
 */
class DateComparator implements Comparator<Date> {

	int orderType;

	public DateComparator(int orderType) {
		this.orderType = orderType;
	}

	public int compare(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			if (orderType == DateUtils.DATE_ORDER_ASC) {
				return 1;
			} else {
				return -1;
			}
		} else {
			if (d1.getTime() == d2.getTime()) {
				return 0;
			} else {
				if (orderType == DateUtils.DATE_ORDER_DESC) {
					return 1;
				} else {
					return -1;
				}
			}
		}
	}

}
