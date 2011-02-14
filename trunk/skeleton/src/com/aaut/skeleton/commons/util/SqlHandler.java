/* 
 * @(#)SqlHandler.java    Created on 2007-8-9 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package com.aaut.skeleton.commons.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/*
 * Comment: undefined
 */
@SuppressWarnings("unchecked")
public final class SqlHandler {
    private StringBuffer sql = null;
    private List args = null;
    private List argTypes = null;
    private boolean hasOrderBy = false;
    private boolean hasWhere = true;
    private boolean isFirst = true;

    /**
     * 构造方法
     * 
     * @param baseSQL
     *            带有WHERE关键字的原始sql
     */
    public SqlHandler(String baseSQL) {
        this(baseSQL, true);
    }

    /**
     * 构造方法
     * 
     * @param baseSQL
     *            原始sql
     * @param hasWhere
     *            原始sql是否带有WHERE关键字
     */
    public SqlHandler(String baseSQL, boolean hasWhere) {
        args = new ArrayList();
        argTypes = new ArrayList();
        sql = new StringBuffer();
        sql.append(baseSQL);
        this.hasWhere = hasWhere;
    }

    /**
     * 增加查询条件
     * 
     * @param operator
     *            操作，比如：AND、OR
     * @param expression
     *            表达式，比如：id=1
     * @param precondition
     *            先决条件，当为true时才会增加查询条件，比如user != null
     */
    public void addExpression(String operator, String expression,
            boolean precondition) {
        if (precondition) {
            if (!isFirst) {
                sql.append(" " + operator + " ");
            }
            else {
                if (!hasWhere) {
                    sql.append(" WHERE ");
                }
                isFirst = false;
            }
            sql.append(expression);
        }
    }

    /**
     * 增加查询条件
     * 
     * @param operator
     *            操作，比如：AND、OR
     * @param expression
     *            表达式，比如：id=?
     * @param arg
     *            表达式中的参数的值
     * @param precondition
     *            先决条件，当为true时才会增加查询条件，比如id != null
     */
    public void addExpression(String operator, String expression, Object arg,
            boolean precondition) {
        if (precondition) {
            if (!isFirst) {
                sql.append(" " + operator + " ");
            }
            else {
                if (!hasWhere) {
                    sql.append(" WHERE ");
                }
                isFirst = false;
            }
            sql.append(expression);
            args.add(arg);
        }
    }

    /**
     * 增加查询条件
     * 
     * @param operator
     *            操作，比如：AND、OR
     * @param expression
     *            表达式，比如：id=?
     * @param arg
     *            表达式中的参数的值
     * @param argType
     *            表达式中的参数的类型
     * @param precondition
     *            先决条件，当为true时才会增加查询条件，比如id != null
     */
    public void addExpression(String operator, String expression, Object arg,
            int argType, boolean precondition) {
        if (precondition) {
            if (!isFirst) {
                sql.append(" " + operator + " ");
            }
            else {
                if (!hasWhere) {
                    sql.append(" WHERE ");
                }
                isFirst = false;
            }
            sql.append(expression);
            args.add(arg);
            argTypes.add(new Integer(argType));
        }
    }

    /**
     * 增加AND查询条件
     * 
     * @param expression
     *            表达式
     * @param precondition
     *            先决条件
     */
    public void and(String expression, boolean precondition) {
        addExpression("AND", expression, precondition);
    }

    /**
     * 增加AND查询条件
     * 
     * @param expression
     *            表达式
     * @param arg
     *            参数的值
     * @param precondition
     *            先决条件
     */
    public void and(String expression, Object arg, boolean precondition) {
        addExpression("AND", expression, arg, precondition);
    }

    /**
     * 增加AND查询条件
     * 
     * @param expression
     *            表达式
     * @param arg
     *            参数的值
     * @param argType
     *            参数的类型
     * @param precondition
     *            先决条件
     */
    public void and(String expression, Object arg, int argType,
            boolean precondition) {
        addExpression("AND", expression, arg, argType, precondition);
    }

    /**
     * 增加AND……IN……查询条件，比如AND id IN(?,?,?);
     * 
     * @param param
     *            参数的名称，比如id
     * @param args
     *            参数的值数组，比如new String[] {"1", "2", "3"}
     * @param argType
     *            参数的类型
     * @param precondition
     *            先决条件
     */
    public void andIn(String param, Object[] args, int argType,
            boolean precondition) {
        if (precondition && args.length > 0) {
            if (!isFirst) {
                sql.append(" AND ");
            }
            else {
                if (!hasWhere) {
                    sql.append(" WHERE ");
                }
                isFirst = false;
            }

            sql.append(param);
            sql.append(" IN");
            sql.append(JdbcUtils.getInSQL(args.length));
            for (int i = 0; i < args.length; i++) {
                this.args.add(args[i]);
                argTypes.add(new Integer(argType));
            }
        }
    }

    /**
     * 取得所有参数的值数组
     * 
     * @return 所有参数的值数组
     */
    public Object[] getArgs() {
        return args.toArray();
    }

    /**
     * 取得所有参数的类型数组
     * 
     * @return 所有参数的类型数组
     */
    public int[] getArgTypes() {
        Integer[] objectTypes = (Integer[]) argTypes
                .toArray(new Integer[argTypes.size()]);
        int[] intTypes = new int[objectTypes.length];
        for (int i = 0; i < objectTypes.length; i++) {
            intTypes[i] = objectTypes[i].intValue();
        }
        return intTypes;
    }

    /**
     * 取得最后生成查询sql
     * 
     * @return 查询sql
     */
    public String getSQL() {
        return sql.toString();
    }

    /**
     * 增加OR查询条件
     * 
     * @param expression
     *            表达式
     * @param precondition
     *            先决条件
     */
    public void or(String expression, boolean precondition) {
        addExpression("OR", expression, precondition);
    }

    /**
     * 增加OR查询条件
     * 
     * @param expression
     *            表达式
     * @param arg
     *            参数的值
     * @param precondition
     *            先决条件
     */
    public void or(String expression, Object arg, boolean precondition) {
        addExpression("OR", expression, arg, precondition);
    }

    /**
     * 增加OR查询条件
     * 
     * @param expression
     *            表达式
     * @param arg
     *            参数的值
     * @param argType
     *            参数的类型
     * @param precondition
     *            先决条件
     */
    public void or(String expression, Object arg, int argType,
            boolean precondition) {
        addExpression("OR", expression, arg, argType, precondition);
    }

    /**
     * 升序排序
     * 
     * @param columnName
     *            列名
     */
    public void orderBy(String columnName) {
        orderBy(columnName, false);
    }

    /**
     * 排序
     * 
     * @param columnName
     *            列名
     * @param isDesc
     *            是否降序
     */
    public void orderBy(String columnName, boolean isDesc) {
        if (!hasOrderBy) {
            sql.append(" ORDER BY ");
        }
        else {
            sql.append(", ");
        }

        sql.append(columnName);
        if (isDesc) {
            sql.append(" DESC");
        }

        hasOrderBy = true;
    }

    /**
     * 降序排序
     * 
     * @param columnName
     *            列名
     */
    public void orderByDesc(String columnName) {
        orderBy(columnName, true);
    }

    public static void main(String[] args) {
        SqlHandler handler = new SqlHandler("SELECT * FROM s_usercount", false);
        handler.and("c_schid LIKE ?", "110001%", true);
        handler.and("c_username LIKE ?", "大牛", true);
        handler
                .andIn("id", new String[] { "1", "2", "3" }, Types.VARCHAR,
                        true);
        handler.or("c_password = ?", "大牛", true);
        handler.orderBy("c_schid");
        handler.orderByDesc("c_username");
        System.out.println(handler.getSQL());
        Object[] params = handler.getArgs();
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i]);
        }
    }
}
