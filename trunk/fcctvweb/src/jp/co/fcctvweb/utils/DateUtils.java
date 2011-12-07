/* 
 * @(#)DateUtils.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package jp.co.fcctvweb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/*
 * Comment: undefined
 */
public final class DateUtils {

	public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			DEFAULT_PATTERN);

	private static final int[] DAY_OF_MONTH = new int[] { 31, 28, 31, 30, 31,
			30, 31, 31, 30, 31, 30, 31 };

	/**
	 * 取得指定天数后的时间
	 * 
	 * @param date
	 *            基准时间
	 * @param dayAmount
	 *            指定天数，允许为负数
	 * @return 指定天数后的时间
	 */
	public static Date addDay(Date date, int dayAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dayAmount);
		return calendar.getTime();
	}

	/**
	 * 取得指定小时数后的时间
	 * 
	 * @param date
	 *            基准时间
	 * @param hourAmount
	 *            指定小时数，允许为负数
	 * @return 指定小时数后的时间
	 */
	public static Date addHour(Date date, int hourAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hourAmount);
		return calendar.getTime();
	}

	/**
	 * 取得指定分钟数后的时间
	 * 
	 * @param date
	 *            基准时间
	 * @param minuteAmount
	 *            指定分钟数，允许为负数
	 * @return 指定分钟数后的时间
	 */
	public static Date addMinute(Date date, int minuteAmount) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minuteAmount);
		return calendar.getTime();
	}

	/**
	 * 比较两日期对象的大小,只比较到Day
	 * 
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareDay(Date date, Date anotherDate) {
		if (null == date && null == anotherDate) {
			return 0;
		}
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date);
		cal2.setTime(anotherDate);
		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);

		int month1 = cal1.get(Calendar.MONTH) + 1;
		int month2 = cal2.get(Calendar.MONTH) + 1;

		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);
		if (year1 == year2 && month1 == month2 && day1 == day2) {
			return 0;
		} else {
			if (year1 > year2) {
				return 1;
			} else if (month1 > month2) {
				return 1;
			} else if (day1 > day2) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 比较两日期字符串所表示的日期对象的大小,只比较到Day
	 * 
	 * @param start
	 *            字符串所表示的日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param end
	 *            字符串所表示的日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果字符串所表示的日期对象1大于字符串所表示的日期对象2, 则返回大于0的数; 反之返回小于0的数;
	 *         如果两字符串所表示的日期对象相等, 则返回0.
	 */
	public static int compareStringDate(String start, String end) {
		Date date = null;
		Date anotherDate = null;
		if ((Validators.isEmpty(start) || !Validators.isDate(start))
				&& (Validators.isEmpty(end) || !Validators.isDate(end))) {
			return 0;
		}
		if (Validators.isEmpty(start) || !Validators.isDate(start)) {
			date = new Date();
		} else {
			date = string2Date(start);
		}

		if (Validators.isEmpty(end) || !Validators.isDate(end)) {
			anotherDate = new Date();
		} else {
			anotherDate = string2Date(end);
		}

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date);
		cal2.setTime(anotherDate);
		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);

		int month1 = cal1.get(Calendar.MONTH) + 1;
		int month2 = cal2.get(Calendar.MONTH) + 1;

		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);
		if (year1 == year2 && month1 == month2 && day1 == day2) {
			return 0;
		} else {
			if (year1 > year2) {
				return 1;
			} else if (month1 > month2) {
				return 1;
			} else if (day1 > day2) {
				return 1;
			} else {
				return -1;
			}
		}

	}

	/**
	 * 比较两日期对象中的小时和分钟部分的大小.
	 * 
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareHourAndMinute(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hourOfDay1 = cal.get(Calendar.HOUR_OF_DAY);
		int minute1 = cal.get(Calendar.MINUTE);

		cal.setTime(anotherDate);
		int hourOfDay2 = cal.get(Calendar.HOUR_OF_DAY);
		int minute2 = cal.get(Calendar.MINUTE);

		if (hourOfDay1 > hourOfDay2) {
			return 1;
		} else if (hourOfDay1 == hourOfDay2) {
			// 小时相等就比较分钟
			return minute1 > minute2 ? 1 : (minute1 == minute2 ? 0 : -1);
		} else {
			return -1;
		}
	}

	/**
	 * 比较两日期对象的大小, 忽略秒, 只精确到分钟.
	 * 
	 * @param date
	 *            日期对象1, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @param anotherDate
	 *            日期对象2, 如果为 <code>null</code> 会以当前时间的日期对象代替
	 * @return 如果日期对象1大于日期对象2, 则返回大于0的数; 反之返回小于0的数; 如果两日期对象相等, 则返回0.
	 */
	public static int compareIgnoreSecond(Date date, Date anotherDate) {
		if (date == null) {
			date = new Date();
		}

		if (anotherDate == null) {
			anotherDate = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();

		cal.setTime(anotherDate);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		anotherDate = cal.getTime();

		return date.compareTo(anotherDate);
	}

	/**
	 * 取得当前时间的字符串表示，格式为01/10/2006 20:56:30.756
	 * 
	 * @return 当前时间的字符串表示
	 */
	public static String currentDate2String() {
		return date2String(new Date());
	}

	/**
	 * 取得当前时间的字符串表示，格式为01/10/2006
	 * 
	 * @return 当前时间的字符串表示
	 */
	public static String currentDate2StringByDay() {
		return date2StringByDay(new Date());
	}

	/**
	 * 取得今天的最后一个时刻
	 * 
	 * @return 今天的最后一个时刻
	 */
	public static Date currentEndDate() {
		return getEndDate(new Date());
	}

	/**
	 * 取得今天的第一个时刻
	 * 
	 * @return 今天的第一个时刻
	 */
	public static Date currentStartDate() {
		return getStartDate(new Date());
	}

	/**
	 * 把时间转换成字符串，格式为yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2String(Date date) {
		return date2String(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式
	 * @return 时间字符串
	 */
	public static String date2String(Date date, String pattern) {
		if (date == null) {
			return null;
		}

		formatter.applyPattern(pattern);
		String d = formatter.format(date);
		formatter.applyPattern(DEFAULT_PATTERN);
		return d;
	}

	/**
	 * 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式
	 * @param locale
	 *            locale
	 * @return 时间字符串
	 */
	public static String date2String(Date date, String pattern, Locale locale) {
		if (date == null) {
			return null;
		}
		return (new SimpleDateFormat(pattern, locale)).format(date);
	}

	/**
	 * 把时间转换成字符串，格式为01/10/2006
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringByDay(Date date) {
		return date2String(date, "yyyy-MM-dd");
	}

	/**
	 * 把时间转换成字符串，格式为01/10/2006 20:56
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringByMinute(Date date) {
		return date2String(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 把时间转换成字符串，格式为01/10/2006 20:56:30
	 * 
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String date2StringBySecond(Date date) {
		return date2String(date, "MM/dd/yyyy HH:mm:ss");
	}

	/**
	 * 根据某星期几的英文名称来获取该星期几的中文数. <br>
	 * e.g. <li>monday -> 一</li> <li>sunday -> 日</li>
	 * 
	 * @param englishWeekName
	 *            星期的英文名称
	 * @return 星期的中文数
	 */
	public static String getChineseWeekNumber(String englishWeekName) {
		if ("monday".equalsIgnoreCase(englishWeekName)) {
			return "一";
		}

		if ("tuesday".equalsIgnoreCase(englishWeekName)) {
			return "二";
		}

		if ("wednesday".equalsIgnoreCase(englishWeekName)) {
			return "三";
		}

		if ("thursday".equalsIgnoreCase(englishWeekName)) {
			return "四";
		}

		if ("friday".equalsIgnoreCase(englishWeekName)) {
			return "五";
		}

		if ("saturday".equalsIgnoreCase(englishWeekName)) {
			return "六";
		}

		if ("sunday".equalsIgnoreCase(englishWeekName)) {
			return "日";
		}

		return null;
	}

	/**
	 * 根据指定的年, 月, 日等参数获取日期对象.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date) {
		return getDate(year, month, date, 0, 0);
	}

	/**
	 * 根据指定的年, 月, 日, 时, 分等参数获取日期对象.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hourOfDay
	 *            时(24小时制)
	 * @param minute
	 *            分
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay,
			int minute) {
		return getDate(year, month, date, hourOfDay, minute, 0);
	}

	/**
	 * 根据指定的年, 月, 日, 时, 分, 秒等参数获取日期对象.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param date
	 *            日
	 * @param hourOfDay
	 *            时(24小时制)
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @return 对应的日期对象
	 */
	public static Date getDate(int year, int month, int date, int hourOfDay,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date, hourOfDay, minute, second);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 取得某个日期是星期几，星期日是1，依此类推
	 * 
	 * @param date
	 *            日期
	 * @return 星期几
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取某天的结束时间, e.g. 2005-10-01 23:59:59.999
	 * 
	 * @param date
	 *            日期对象
	 * @return 该天的结束时间
	 */
	public static Date getEndDate(Date date) {

		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTime();
	}

	/**
	 * 取得一个月最多的天数
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份，0表示1月，依此类推
	 * @return 最多的天数
	 */
	public static int getMaxDayOfMonth(int year, int month) {
		if (month == 1 && isLeapYear(year)) {
			return 29;
		}
		return DAY_OF_MONTH[month];
	}

	/**
	 * 得到指定日期的下一天
	 * 
	 * @param date
	 *            日期对象
	 * @return 同一时间的下一天的日期对象
	 */
	public static Date getNextDay(Date date) {
		return addDay(date, 1);
	}

	/**
	 * 获取某天的起始时间, e.g. 2005-10-01 00:00:00.000
	 * 
	 * @param date
	 *            日期对象
	 * @return 该天的起始时间
	 */
	public static Date getStartDate(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 根据日期对象来获取日期中的时间(HH:mm:ss).
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字符串, 格式为: HH:mm:ss
	 */
	public static String getTime(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 根据日期对象来获取日期中的时间(HH:mm).
	 * 
	 * @param date
	 *            日期对象
	 * @return 时间字符串, 格式为: HH:mm
	 */
	public static String getTimeIgnoreSecond(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}

	/**
	 * 判断是否是闰年
	 * 
	 * @param year
	 *            年份
	 * @return 是true，否则false
	 */
	public static boolean isLeapYear(int year) {
		Calendar calendar = Calendar.getInstance();
		return ((GregorianCalendar) calendar).isLeapYear(year);
	}

	/**
	 * 把字符串转换成日期，格式为01/10/2006
	 * 
	 * @param str
	 *            字符串
	 * @return 日期
	 */
	public static Date string2Date(String str) {
		return string2Date(str, "yyyy-MM-dd");
	}

	/**
	 * 按照指定的格式把字符串转换成时间，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param str
	 *            字符串
	 * @param pattern
	 *            格式
	 * @return 时间
	 */
	public static Date string2Date(String str, String pattern) {
		if (Validators.isEmpty(str)) {
			return null;
		}

		formatter.applyPattern(pattern);
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			formatter.applyPattern(DEFAULT_PATTERN);
		}
		return date;
	}

	/**
	 * 把字符串转换成日期，格式为01/10/2006 20:56:30
	 * 
	 * @param str
	 *            字符串
	 * @return 日期
	 */
	public static Date string2DateTime(String str) {
		return string2Date(str, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 取得一年中的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static String getWeekDescription(int dayOfWeek) {
		return getWeekDescription(dayOfWeek, 0);
	}

	public static String getWeekDescription(int dayOfWeek, int cutOf) {
		switch (dayOfWeek) {
		case 1:
			return "Sunday"
					.substring(0, cutOf == 0 ? "Sunday".length() : cutOf);
		case 2:
			return "Monday"
					.substring(0, cutOf == 0 ? "Monday".length() : cutOf);
		case 3:
			return "Tuesday".substring(0, cutOf == 0 ? "Tuesday".length()
					: cutOf);
		case 4:
			return "Wednesday".substring(0, cutOf == 0 ? "Wednesday".length()
					: cutOf);
		case 5:
			return "Thursday".substring(0, cutOf == 0 ? "Thursday".length()
					: cutOf);
		case 6:
			return "Friday"
					.substring(0, cutOf == 0 ? "Friday".length() : cutOf);
		case 7:
			return "Saturday".substring(0, cutOf == 0 ? "Saturday".length()
					: cutOf);
		default:
			return "";
		}
	}

	public static String getMonthDescription(int month) {
		return getMonthDescription(month, 0);
	}

	public static String getMonthDescription(int month, int cutOf) {
		switch (month) {
		case 0:
			return "January".substring(
					0,
					cutOf == 0 || cutOf > "January".length() ? "January"
							.length() : cutOf);
		case 1:
			return "February".substring(
					0,
					cutOf == 0 || cutOf > "February".length() ? "February"
							.length() : cutOf);
		case 2:
			return "March".substring(0,
					cutOf == 0 || cutOf > "March".length() ? "March".length()
							: cutOf);
		case 3:
			return "April".substring(0,
					cutOf == 0 || cutOf > "April".length() ? "April".length()
							: cutOf);
		case 4:
			return "May".substring(0,
					cutOf == 0 || cutOf > "May".length() ? "May".length()
							: cutOf);
		case 5:
			return "June".substring(0,
					cutOf == 0 || cutOf > "June".length() ? "June".length()
							: cutOf);
		case 6:
			return "July".substring(0,
					cutOf == 0 || cutOf > "July".length() ? "July".length()
							: cutOf);
		case 7:
			return "August".substring(0,
					cutOf == 0 || cutOf > "August".length() ? "August".length()
							: cutOf);
		case 8:
			return "September".substring(
					0,
					cutOf == 0 || cutOf > "September".length() ? "September"
							.length() : cutOf);
		case 9:
			return "October".substring(
					0,
					cutOf == 0 || cutOf > "October".length() ? "October"
							.length() : cutOf);
		case 10:
			return "November".substring(
					0,
					cutOf == 0 || cutOf > "November".length() ? "November"
							.length() : cutOf);
		case 11:
			return "December".substring(
					0,
					cutOf == 0 || cutOf > "December".length() ? "December"
							.length() : cutOf);
		default:
			return "";
		}
	}

	public static String translateDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String description = getWeekDescription(cal.get(Calendar.DAY_OF_WEEK),
				3)
				+ " "
				+ cal.get(Calendar.DAY_OF_MONTH)
				+ "th "
				+ DateUtils.getMonthDescription(cal.get(Calendar.MONTH), 3);
		return description;
	}

	/**
	 * 获得两个日期之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return 两日期之间相差的天数
	 * @author Kiter.qian 2008-04-14
	 * 
	 */
	public static int getInstanceDays(String date1Str, String date2Str) {
		Date date1 = null;
		Date date2 = null;
		if (!Validators.isEmpty(date1Str) && !Validators.isEmpty(date1Str)) {
			date1 = DateUtils.string2Date(date1Str);
			date2 = DateUtils.string2Date(date2Str);
		}
		if (date1 == null || date2 == null)
			return 0;
		return (int) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
	}

	public static void main(String[] arg) {

	}
}
