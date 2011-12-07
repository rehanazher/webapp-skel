/* 
 * @(#)MapRowMapper.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package jp.co.fcctvweb.utils.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Comment: undefined
 */
public interface MapRowMapper {
    /**
     * 产生要放入map中的可以标识这条记录的某个key, <br>
     * 例如可以以这条记录中的某个字段的值作为key等等.
     * 
     * @param rs
     *            结果集
     * @param rowNum
     *            当前记录行号
     * @return 放入map的关键字
     */
    public abstract Object mapRowKey(ResultSet rs, int rowNum)
            throws SQLException;

    /**
     * 产生要放入map中的以 <code>mapKey()</code> 方法的返回值为key的某个value, <br>
     * 例如可以以这条记录中的某个字段的值作为value, 或者一个值对象等等.
     * 
     * @param rs
     *            结果集
     * @param rowNum
     *            当前记录行号
     * @return 放入map的值
     */
    public abstract Object mapRowValue(ResultSet rs, int rowNum)
            throws SQLException;
}
