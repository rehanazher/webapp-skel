/* 
 * @(#)MultiRowMapper.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package jp.co.fcctvweb.utils.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Comment: undefined
 */
public interface MultiRowMapper<T> {
    /**
     * 把记录集的一行记录映射成一个对象，方法里不需要执行rs.next
     * 
     * @param rs
     *            记录集
     * @param rowNum
     *            这是第几条记录，从0开始
     * @return 对象
     * @throws SQLException
     */
	T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
