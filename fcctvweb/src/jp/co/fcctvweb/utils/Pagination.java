/* 
 * @(#)Pagination.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package jp.co.fcctvweb.utils;

import org.apache.log4j.Logger;

/*
 * Comment: undefined
 */
public class Pagination {
    private static Logger logger = Logger.getLogger(Pagination.class);

    private String id = "";// 分页信息的标识，会作为参数的后缀，在多个pagination共存的时候有用

    private int pageIndex; // 当前是第几页
    private int maxPageIndex; // 一共有多少页
    private int maxRowCount; // 一共有多少行
    private int pageSize; // 每页有多少行
    private int curRowNum; // 当前起始记录序号
    private boolean isUseCursor = false;
    private String sort = null;
    private boolean desc = false;

    /**
     * 构造方法
     * 
     * @param pageSize
     *            每页多少行
     * @param isUseCursor
     *            是否使用游标
     */
    public Pagination(int pageSize, boolean isUseCursor) {
        this(0, pageSize, isUseCursor);
    }

    /**
     * 构造方法
     * 
     * @param pageIndex
     *            当前是第几页
     * @param pageSize
     *            每页多少行
     * @param isUseCursor
     *            是否使用游标
     */
    public Pagination(int pageIndex, int pageSize, boolean isUseCursor) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.isUseCursor = isUseCursor;
    }

    /**
     * 初始化，计算出一共有多少页和当前起始记录序号
     */
    public void initialize() {
        // 得到总共页数
        if (maxRowCount % pageSize == 0) {
            maxPageIndex = maxRowCount / pageSize;
        }
        else {
            maxPageIndex = maxRowCount / pageSize + 1;
        }
        // 校验当前页参数
        if (pageIndex > maxPageIndex) {
            pageIndex = maxPageIndex;
        }
        else if (pageIndex < 1) {
            pageIndex = 1;
        }
        // 得到当前起始记录序号
        if (maxRowCount == 0) {
            curRowNum = 0;
            pageIndex = 0;
        }
        else {
            curRowNum = (pageIndex - 1) * pageSize + 1;
        }

        if (logger.isInfoEnabled()) {
            logger.info("page: " + pageIndex + "/" + maxPageIndex + " row: "
                    + curRowNum + "/" + maxRowCount + " per: " + pageSize
                    + " cursor: " + isUseCursor);
        }
    }

    /**
     * 取得分页对象的id
     * 
     * @return 分页对象的id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置分页对象的id
     * 
     * @param id
     *            分页对象的id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 设置每页多少行
     * 
     * @param pageSize
     *            每页多少行
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置当前是第几页
     * 
     * @param pageIndex
     *            当前是第几页
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 判断是否降序排列
     * 
     * @return 是true，否则false
     */
    public boolean isDesc() {
        return desc;
    }

    /**
     * 设置是否降序排列
     * 
     * @param desc
     *            是否降序排列
     */
    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    /**
     * 取得排序字段
     * 
     * @return 排序字段
     */
    public String getSort() {
        return sort;
    }

    /**
     * 设置排序字段
     * 
     * @param sort
     *            排序字段
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 取得当前是第几页
     * 
     * @return 当前是第几页
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * 取得当前起始记录序号
     * 
     * @return 当前起始记录序号
     */
    public int getCurRowNum() {
        return curRowNum;
    }

    /**
     * 设置是否使用游标
     * 
     * @param isUseCursor
     *            是否使用游标
     */
    public void setUseCursor(boolean isUseCursor) {
        this.isUseCursor = isUseCursor;
    }

    /**
     * 判断是否使用游标
     * 
     * @return 是true，否则false
     */
    public boolean isUseCursor() {
        return isUseCursor;
    }

    /**
     * 取得一共有多少页
     * 
     * @return 一共有多少页
     */
    public int getMaxPageIndex() {
        return maxPageIndex;
    }

    /**
     * 取得一共有多少行
     * 
     * @return 一共有多少行
     */
    public int getMaxRowCount() {
        return maxRowCount;
    }

    /**
     * 取得每页有多少行
     * 
     * @return 每页有多少行
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置一共有多少行
     * 
     * @param maxRowCount
     *            一共有多少行
     */
    public void setMaxRowCount(int maxRowCount) {
        this.maxRowCount = maxRowCount;
    }
}
