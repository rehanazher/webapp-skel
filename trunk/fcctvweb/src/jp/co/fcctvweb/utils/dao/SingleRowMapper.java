/* 
 * @(#)SingleRowMapper.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package jp.co.fcctvweb.utils.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Comment: undefined
 */
public interface SingleRowMapper<T> {
    /**
     * 把记录集的这行记录映射成一个对象，方法里不需要执行rs.next
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    T mapRow(ResultSet rs) throws SQLException;
}
