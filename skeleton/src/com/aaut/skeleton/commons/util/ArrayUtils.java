/* 
 * @(#)ArrayUtils.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package com.aaut.skeleton.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*
 * Comment: undefined
 */
@SuppressWarnings("unchecked")
public final class ArrayUtils {

	public static void main(String[] args) {
		System.out.println(toString(new String[] { "HKG", "SHA", "HKG" }, "-"));
	}

	/**
	 * 显示字符串数组的内容，用,分隔
	 * 
	 * @param args
	 *            字符串数组
	 * @return 字符串数组的内容
	 */
	public static String toString(String[] args) {
		return toString(args, ",");
	}

	/**
	 * 显示字符串数组的内容
	 * 
	 * @param args
	 *            字符串数组
	 * @param separator
	 *            分隔符
	 * @return 字符串数组的内容
	 */
	public static String toString(String[] args, String separator) {
		if (Validators.isEmpty(args)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) {
				buffer.append(separator);
			}
			buffer.append(args[i]);
		}
		return buffer.toString();
	}

	public static String toString(List list) {
		return toString(list, ",");
	}

	public static String toString(List list, String separator) {
		if (Validators.isEmpty(list)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				buffer.append(separator);
			}
			buffer.append(list.get(i));
		}
		return buffer.toString();
	}

	/**
	 * 取得字符串数组的第一个元素
	 * 
	 * @param stringArray
	 *            字符串数组
	 * @return 字符串数组的第一个元素
	 */
	public static String getFirst(String[] stringArray) {
		if (stringArray == null || stringArray.length == 0) {
			return null;
		}
		return stringArray[0];
	}

	/**
	 * 取得数组的第一个元素
	 * 
	 * @param array
	 *            数组
	 * @return 数组的第一个元素
	 */
	public static Object getFirst(Object[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		return array[0];
	}

	/**
	 * 把List转换成数组
	 * 
	 * @param list
	 *            字符串List
	 * @return 字符串数组
	 */
	public static String[] toArray(List list) {
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 把Set转换成字符串数组
	 * 
	 * @param set
	 *            字符串Set
	 * @return 字符串数组
	 */
	public static String[] toArray(Set set) {
		return (String[]) set.toArray(new String[set.size()]);
	}

	/**
	 * 判断字符串数组是否包含指定的字符串
	 * 
	 * @param array
	 *            字符串数组
	 * @param str
	 *            指定的字符串
	 * @return 包含true，否则false
	 */
	public static boolean contains(String[] array, String str) {

		if (Validators.isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串数组是否有不为Empty的值
	 * 
	 * @param args
	 *            字符串数组
	 * @return 有true，否则false
	 */
	public static boolean hasValue(String[] args) {
		if (Validators.isEmpty(args)) {
			return false;
		}
		for (int i = 0, length = args.length; i < length; i++) {
			if (!Validators.isEmpty(args[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 联合两个数组
	 * 
	 * @param first
	 *            第一个数组
	 * @param last
	 *            另一个数组
	 * @return 内容合并后的数组
	 */
	public static Object[] combine(Object[] first, Object[] last) {
		if (first.length == 0 && last.length == 0) {
			return null;
		}
		Object[] result = new Object[first.length + last.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(last, 0, result, first.length, last.length);
		return result;
	}

	/**
	 * 把数组转换成List
	 * 
	 * @param array
	 *            数组
	 * @return List
	 */
	public static List toList(Object[] array) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	/**
	 * 把数组转换成Set
	 * 
	 * @param array
	 * @return
	 */
	public static Set toSet(Object[] array) {
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return set;
	}

	/**
	 * 清除字符串数组中的null
	 * 
	 * @param array
	 *            字符串数组
	 * @return 清除null后的字符串数组
	 */
	public static Object[] clearNull(Object[] array) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				list.add(array[i]);
			}
		}
		return toArray(list);
	}

	// /**
	// * 对List进行分页
	// *
	// * @param list
	// * List
	// * @param page
	// * 分页对象
	// * @return 当前页的List
	// */
	// public static List subList(List list, Pagination page) {
	// if (list == null || list.isEmpty()) {
	// return new ArrayList();
	// }
	//
	// page.setMaxRowCount(list.size());
	// page.initialize();
	// int startIndex = page.getCurRowNum() - 1;
	//
	// if (startIndex >= list.size()) {
	// return new ArrayList();
	// }
	//
	// int endIndex = startIndex + page.getPageSize();
	// endIndex = endIndex >= list.size() ? list.size() : endIndex;
	//
	// return list.subList(startIndex, endIndex);
	// }
	
}
