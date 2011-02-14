/* 
 * @(#)Validators.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package com.aaut.skeleton.commons.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/*
 * Comment: undefined
 */
@SuppressWarnings("unchecked")
public final class Validators {

	public static class ScriptRegexp {
		public static final String PHONE = "((\\(\\d+\\))*|(\\d+[- ]?)*)\\d+";

		public static final String NUMERICE_NOT_NEGATIVE = "\\d+([.]\\d{2})?";

		public static final String NUMERICE = "[-]?\\d+(.\\d{2})?";

		public static final String MAIL = "(\\w|[.-]){1,}[@][\\w\\-]{1,}([.]([\\w\\-]{1,})){1,3}";

		public static final String FINANCE_NUMERIC = "[-]?\\d+(,\\d{3})*(.\\d{2})?";

		public static final String NUMBER = "\\d*";

		public static final String PASSENGER_ALLOW = "[0-6]?";
	}

	public static boolean checkByRegex(String str, String regex) {
		if (Validators.isEmpty(str)) {
			return false;
		}
		return Pattern.matches(regex, str);
	}

	/**
	 * 判断字符串是否只包含字母和数字.
	 * 
	 * @param str
	 *            字符串
	 * @return 如果字符串只包含字母和数字, 则返回 <code>ture</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isAlphanumeric(String str) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		String regex = "[a-zA-Z0-9]+";
		return Pattern.matches(regex, str);
	}

	/**
	 * 判断字符串是否是固定长度范围内的只包含字母和数字.
	 * 
	 * @param str
	 *            字符串
	 * @return 如果字符串只包含字母和数字, 则返回 <code>ture</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isAlphanumeric(String str, int min, int max) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		String regex = "[a-zA-Z0-9]+";
		if (Pattern.matches(regex, str) && isString(str, min, max)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否只包含字母.
	 * 
	 * @param str
	 *            字符串
	 * @return 如果字符串只包含字母, 则返回 <code>ture</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isAlpha(String str) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		String regex = "[a-zA-Z]+";
		return Pattern.matches(regex, str);
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 *                                    StringUtils.isBlank(null)                = true
	 *                                    StringUtils.isBlank(&quot;&quot;)        = true
	 *                                    StringUtils.isBlank(&quot; &quot;)       = true
	 *                                    StringUtils.isBlank(&quot;bob&quot;)     = false
	 *                                    StringUtils.isBlank(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为中国移动手机号码.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinaMobile(String str) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		// Regex for checking ChinaMobile
		String regex = "1(3|5)[4-9]\\d{8}";
		return Pattern.matches(regex, str);
	}

	/**
	 * 判断是否为小灵通手机(Personal Access Phone System, PAS).
	 * 
	 * @param str
	 *            字符串
	 * @return 如果是小灵通号码, 返回 <code>true</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isChinaPAS(String str) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		// Deal with 013 & 015
		if (str.startsWith("013") || str.startsWith("015")) {
			return false;
		}

		// Regex for checking PAS
		String regex = "0\\d{9,11}";
		return Pattern.matches(regex, str);
	}

	/**
	 * 是否为中国联通手机号码.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinaUnicom(String str) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		// Regex for checking ChinaMunicm
		String regex = "1(3|5)[0-3]\\d{8}";
		return Pattern.matches(regex, str);
	}

	/**
	 * 是否是合法的日期字符串
	 * 
	 * @param str
	 *            日期字符串
	 * @return 是true，否则false
	 */
	public static boolean isDate(String str) {
		if (isEmpty(str) || str.length() > 10) {
			return false;
		}

		String[] items = str.split("-");

		if (items.length != 3) {
			return false;
		}

		if (!isNumber(items[0], 1900, 9999) || !isNumber(items[1], 1, 12)) {
			return false;
		}

		int year = Integer.parseInt(items[0]);
		int month = Integer.parseInt(items[1]);

		return isNumber(items[2], 1, DateUtils
				.getMaxDayOfMonth(year, month - 1));
	}

	/**
	 * 是否是合法的日期时间字符串
	 * 
	 * @param str
	 *            日期时间字符串
	 * @return 是true，否则false
	 */
	public static boolean isDateTime(String str) {
		if (isEmpty(str) || str.length() > 20) {
			return false;
		}

		String[] items = str.split(" ");

		if (items.length != 2) {
			return false;
		}

		return isDate(items[0]) && isTime(items[1]);
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
			date = DateUtils.string2Date(start);
		}

		if (Validators.isEmpty(end) || !Validators.isDate(end)) {
			anotherDate = new Date();
		} else {
			anotherDate = DateUtils.string2Date(end);
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
	 * 是否是合法的电子邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		return !isEmpty(str) && str.indexOf("@") > 0;
	}

	/**
	 * 当数组为<code>null</code>, 或者长度为0, 或者长度为1且元素的值为<code>null</code>时返回
	 * <code>true</code>.
	 * 
	 * @param args
	 * @return
	 */
	public static boolean isEmpty(Object[] args) {
		return args == null || args.length == 0
				|| (args.length == 1 && args[0] == null);
	}

	public static boolean isEmpty(Object arg) {
		return arg == null;
	}

	/**
	 * 当数组为<code>null</code>, 或者长度为0, 或者长度为1且元素的值为<code>null</code>时返回
	 * <code>true</code>.
	 * 
	 * @param args
	 * @return
	 */
	public static boolean isEmpty(List args) {
		return args == null || args.size() == 0
				|| (args.size() == 1 && args.get(0) == null);
	}

	/**
	 * 字符串是否为Empty，null和空格都算是Empty
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 是否为手机号码, 包括移动, 联通, 小灵通等手机号码.
	 * 
	 * @param str
	 *            字符串
	 * @return 若是合法的手机号码返回 <code>true</code>, 否则返回 <code>false</code>.
	 */
	public static boolean isMobile(String str) {
		if (Validators.isEmpty(str)) {
			return false;
		}

		// Regex for Mobile
		String regex = "(13\\d{9})|(0\\d{9,11})|(15\\d{9})";
		return Pattern.matches(regex, str) && !str.startsWith("013")
				&& !str.startsWith("015");
	}

	/**
	 * 是否为数字的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) > '9' || str.charAt(i) < '0') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否是固定范围内的数字的字符串
	 * 
	 * @param str
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isNumber(String str, int min, int max) {
		if (null == str) {
			return false;
		} else {
			str = str.replaceAll(",", "");
		}
		if (!isNumeric(str)) {
			return false;
		}

		int number = 0;
		try {
			number = (int) Double.parseDouble(str) * 100;
			min = min * 100;
			max = max * 100;
		} catch (Exception e) {
			// throw new RuntimeException(e);
			return false;
		}
		return number >= min && number <= max;
	}

	/**
	 * 判断字符是否为整数或浮点数. <br>
	 * 
	 * @param str
	 *            字符
	 * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}

		String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d*)"; // 整数或浮点数
		return Pattern.matches(regex, str);
	}

	/**
	 * 判断字符是否为符合精度要求的整数或浮点数. <br>
	 * 
	 * @param str
	 *            字符
	 * @param fractionNum
	 *            小数部分的最多允许的位数
	 * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public static boolean isNumeric(String str, int fractionNum) {
		if (null == str) {
			return false;
		} else {
			str = str.replaceAll(",", "");
		}
		if (isEmpty(str)) {
			return false;
		}

		// 整数或浮点数
		String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d{0," + fractionNum + "})";
		return Pattern.matches(regex, str);
	}

	/**
	 * <p>
	 * Validating for phone number.
	 * </p>
	 * e.g. <li>78674585 --> valid</li> <li>6872-4585 --> valid</li> <li>
	 * (6872)4585 --> valid</li> <li>0086-10-6872-4585 --> valid</li> <li>
	 * 0086-(10)-6872-4585 --> invalid</li> <li>0086(10)68724585 --> invalid</li>
	 * 
	 * @param str
	 *            string to be validated
	 * @return If the str is valid phone number return <code>true</code>,
	 *         otherwise return <code>false</code>.
	 */
	public static boolean isPhoneNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		// Regex for checking phone number
		String regex = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";
		return Pattern.matches(regex, str);
	}

	/**
	 * 判断是否是合法的邮编
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPostcode(String str) {
		if (isEmpty(str)) {
			return false;
		}

		if (str.length() != 6 || !Validators.isNumber(str)) {
			return false;
		}

		return true;
	}

	/**
	 * 判断是否是固定长度范围内的字符串
	 * 
	 * @param str
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean isString(String str, int minLength, int maxLength) {
		if (str == null) {
			return false;
		}

		if (minLength < 0) {
			return str.length() <= maxLength;
		} else if (maxLength < 0) {
			return str.length() >= minLength;
		} else {
			return str.length() >= minLength && str.length() <= maxLength;
		}
	}

	/**
	 * 判断是否是合法的时间字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTime(String str) {
		if (isEmpty(str) || str.length() > 8) {
			return false;
		}

		String[] items = str.split(":");

		if (items.length != 2 && items.length != 3) {
			return false;
		}

		for (int i = 0; i < items.length; i++) {
			if (items[i].length() != 2 && items[i].length() != 1) {
				return false;
			}
		}

		return !(!isNumber(items[0], 0, 23) || !isNumber(items[1], 0, 59) || (items.length == 3 && !isNumber(
				items[2], 0, 59)));
	}

	/**
	 * 判断某个整数是否在有效范围内：
	 * 
	 * 当且仅当value在in的范围内，则返回true;否则返回false.
	 * 
	 * @param in
	 *            有效值
	 * @param value
	 *            需判定的值
	 * @return flag 布尔型
	 */
	public static boolean valueOfEnum(int value, int[] in) {
		boolean flag = false;

		if (null != in && in.length > 0) {
			for (Integer each : in) {
				if (value == each) {
					flag = true;
				}
			}
		}
		return flag;
	}

	public static boolean isFlightTimeMode(String args) {
		String timeReg = "(([0-1][0-9])|(2[0-3]))[0-5][0-9]"; // 12:30 表示为 1230，
		// 18：06表示为 1806
		return Pattern.matches(timeReg, args);
	}

	public static boolean startWith(String args, String prefix) {
		if (args == null) {
			return false;
		}
		return args.startsWith(prefix);
	}

	public static boolean endWith(String args, String suffix) {
		if (args == null) {
			return false;
		}
		return args.endsWith(suffix);
	}

	private Validators() {
	}

	public static void main(String[] arg) {
		boolean flag = false;
		int[] in = new int[] { 0, 1 };
		int value = -1;
		if (null != in && in.length > 0) {
			for (Integer each : in) {
				if (value == each) {
					flag = true;
				}
			}
		}
		System.out.println(flag);
	}
}
