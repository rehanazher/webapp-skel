/* 
 * @(#)BasicDao.java    Created on 2007-6-6 by James
 * Copyright (c) 2007 AAUT ltd, corp. All rights reserved.
 * $Header$
 */
package com.aaut.skeleton.commons.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.aaut.skeleton.commons.util.JdbcUtils;
import com.aaut.skeleton.commons.util.Pagination;
import com.aaut.skeleton.commons.util.StringUtils;
import com.aaut.skeleton.commons.util.UUIDGenerator;
import com.aaut.skeleton.commons.util.Validators;

/*
 * Comment: 用于处理数据库操作的DAO基类.
 */
@SuppressWarnings("unchecked")
public abstract class BasicDaoSupport<PO> extends JdbcDaoSupport {

	// public static final String TABLE_NAME_WILDCARD = "$table_name$";
	//
	// private static final Map<String, Map<String, String>> TABLE_NAME_MAP =
	// new
	// HashMap<String, Map<String, String>>();
	//
	// static {
	// TABLE_NAME_MAP.put("hotel", getLocale2TableName("hotel"));
	// TABLE_NAME_MAP.put("air", getLocale2TableName("air"));
	// TABLE_NAME_MAP.put("air_condition",
	// getLocale2TableName("air_condition"));
	// TABLE_NAME_MAP.put("other_service",
	// getLocale2TableName("other_service"));
	// }
	//
	// private static Map<String, String> getLocale2TableName(String tableName)
	// {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("zh_CN", tableName + "_zh_CN");
	// map.put("zh_TW", tableName + "_zh_TW");
	// map.put("en", tableName + "_en");
	// return map;
	// }

	// protected String getRealTableName(String tableName, Locale locale) {
	// String realName = tableName;
	// if (TABLE_NAME_MAP.containsKey(tableName)) {
	// if ("zh".equals(locale.getLanguage())) {
	// if ("TW".equals(locale.getCountry())) {
	// realName = TABLE_NAME_MAP.get(tableName).get("zh_TW");
	// }
	// else {
	// realName = TABLE_NAME_MAP.get(tableName).get("zh_CN");
	// }
	// }
	// else {
	// realName = TABLE_NAME_MAP.get(tableName).get("en");
	// }
	// }
	// return realName;
	// }

	// public static enum International {
	// ZH_CN(Locale.SIMPLIFIED_CHINESE), ZH_TW(Locale.TRADITIONAL_CHINESE), EN(
	// Locale.ENGLISH);
	//
	// private Locale value;
	//
	// International(Locale value) {
	// this.value = value;
	// }
	//
	// public String getNationSuffix() {
	// switch (this) {
	// case ZH_CN:
	// return "_zh_CN";
	// case ZH_TW:
	// return "_zh_TW";
	// case EN:
	// return "_en";
	// default:
	// return "_en";
	// }
	// }
	//
	// public Locale getLocale() {
	// return this.value;
	// }
	//
	// public Locale getValue() {
	// return value;
	// }
	// }

	/**
	 * 数据库操作的超时时间，默认为300000毫秒，如果超时会在日志里严重警告
	 */
	private static final int EXECUTE_OVERTIME = 300000;// 如果执行时间操作5分钟，在日志里严重警告

	/**
	 * 默认SQL批处理操作的记录数量.
	 */
	private static int DEFAULT_BATCH_SIZE = 500;

	/**
	 * 默认数据库IN SQL中参数的允许的最大数量.
	 */
	private static int DEFAULT_MAX_IN_SQL_PARAM_COUNT = 5000;

	private int batchSize = DEFAULT_BATCH_SIZE;
	private int maxInSQLParamCount = DEFAULT_MAX_IN_SQL_PARAM_COUNT;

	private class MapResultExtractor implements ResultSetExtractor {
		private MapRowMapper rowMapper = null;

		public MapResultExtractor(MapRowMapper rowMapper) {
			this.rowMapper = rowMapper;
		}

		public Object extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			Map<Object, Object> results = new HashMap<Object, Object>();
			int index = 0;
			while (rs.next()) {
				results.put(rowMapper.mapRowKey(rs, index++), rowMapper
						.mapRowValue(rs, index++));
			}

			return results;
		}
	}

	//
	// private class JRResultSetExtractor implements ResultSetExtractor {
	//
	// public JRResultSetDataSource extractData(ResultSet resultSet)
	// throws SQLException, DataAccessException {
	// JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(
	// resultSet);
	// return resultSetDataSource;
	// }
	// }

	private class MultiResultSetExtractor<T> implements ResultSetExtractor {

		private MultiRowMapper<T> multiRowMapper = null;

		public MultiResultSetExtractor(MultiRowMapper<T> multiRowMapper) {
			this.multiRowMapper = multiRowMapper;
		}

		public ArrayList<T> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			ArrayList<T> data = new ArrayList<T>();
			int index = 0;
			while (rs.next()) {
				data.add(multiRowMapper.mapRow(rs, index++));
			}
			return data;
		}
	}

	private class MultiTopResultSetExtractor<T> implements ResultSetExtractor {

		private MultiRowMapper<T> multiRowMapper = null;
		private int topLimit;

		public MultiTopResultSetExtractor(MultiRowMapper<T> multiRowMapper,
				int topLimit) {
			this.multiRowMapper = multiRowMapper;
			this.topLimit = topLimit;
		}

		public List<T> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			List<T> data = new ArrayList<T>();
			int index = 0;
			while (rs.next() && index < topLimit) {
				data.add(multiRowMapper.mapRow(rs, index++));
			}
			return data;
		}
	}

	private class SingleResultSetExtractor<T> implements ResultSetExtractor {
		private SingleRowMapper<T> singleRowMapper = null;

		public SingleResultSetExtractor(SingleRowMapper<T> singleRowMapper) {
			this.singleRowMapper = singleRowMapper;
		}

		public T extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			if (rs.next()) {
				return singleRowMapper.mapRow(rs);
			}
			return null;
		}
	}

	private class SpecialBatchPreparedStatementSetter implements
			BatchPreparedStatementSetter {

		private int[] argTypes = null;
		private List listOfArgs = null;

		public SpecialBatchPreparedStatementSetter(List listOfArgs,
				int[] argTypes) {
			this.listOfArgs = listOfArgs;
			this.argTypes = argTypes;
		}

		public int getBatchSize() {
			return listOfArgs.size();
		}

		public void setValues(PreparedStatement ps, int i) throws SQLException {
			Object[] args = (Object[]) listOfArgs.get(i);
			JdbcUtils.setSuitedParamsToStatement(args, argTypes, ps);
		}
	}

	protected static Logger logger = Logger.getLogger(BasicDaoSupport.class);

	private UUIDGenerator uuid = new UUIDGenerator();

	/**
	 * 批量更新数据.
	 * 
	 * @param sql
	 *            sql语句
	 * @param listOfArgs
	 *            参数数组的List集合
	 * @param argTypes
	 *            参数类型数组
	 * @return 更新的记录条数数组
	 */
	protected int batchUpdate(String sql, List listOfArgs, int[] argTypes) {
		if (listOfArgs == null || listOfArgs.isEmpty()) {
			return 0;
		}

		if (logger.isDebugEnabled()) {
			for (int i = 0; i < listOfArgs.size(); i++) {
				Object[] args = (Object[]) listOfArgs.get(i);
				logger.debug(JdbcUtils.getSQL(sql, args));
			}
		}
		long startTime = System.currentTimeMillis();

		try {
			int destPos = 0;
			int size = listOfArgs.size();
			int totalResults = 0;
			int batchExecCount = (size % batchSize == 0) ? size / batchSize
					: size / batchSize + 1;

			if (logger.isDebugEnabled()) {
				logger.debug("Batch executed times: " + batchExecCount);
			}

			for (int i = 0; i < batchExecCount; i++) {
				int from = batchSize * i;
				int to = 0;
				if (i == batchExecCount - 1) {
					to = size;
				} else {
					to = batchSize * (i + 1);
				}

				List batchListOfArgs = listOfArgs.subList(from, to);
				int[] batchResults = getJdbcTemplate().batchUpdate(
						sql,
						new SpecialBatchPreparedStatementSetter(
								batchListOfArgs, argTypes));
//				System.arraycopy(batchResults, 0, totalResults, destPos,
//						batchResults.length);
				for (int result: batchResults){
					totalResults += result;
				}
				destPos += batchResults.length;
			}
			return totalResults;
		} finally {
			// 如果超时只记录第一条sql
			processOvertime(startTime, sql, (Object[]) listOfArgs.get(0));
		}
	}

	/**
	 * 统计记录条数.
	 * 
	 * @param sql
	 *            sql语句(如：SELECT * FROM a),自动替换成(SELECT COUNT(1) FROM a)
	 * @return 记录条数
	 */
	protected int count(String sql) {
		return count(sql, null);
	}

	/**
	 * 统计记录条数
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @return 记录条数
	 */
	protected int count(String sql, Object[] args) {
		String lowCaseSql = sql.toLowerCase();
		int indexOfUnion = lowCaseSql.indexOf(" union ");
		if (indexOfUnion == -1) {
			int countValue = queryForInt(JdbcUtils.getCountSQL(sql), args);
			int indexOfTop = lowCaseSql.indexOf(" top ");
			if (indexOfTop == -1) {
				return countValue;
			} else {
				int indexOfBlank = lowCaseSql.indexOf(" ", indexOfTop
						+ " top ".length());
				String topLimitString = lowCaseSql.substring(indexOfTop
						+ " top ".length(), indexOfBlank);
				int topLimit = Integer.parseInt(topLimitString);
				return countValue > topLimit ? topLimit : countValue;
			}
		}

		// 处理带union的SQL的结果数量统计, 只支持单个union
		// 使用union很可能会影响执行效率, 应该避免!
		String sqlA = sql.substring(0, indexOfUnion);
		String sqlB = sql.substring(indexOfUnion + " union ".length());
		Object[] argsOfA = null;
		Object[] argsOfB = null;
		if (!Validators.isEmpty(args)) {
			final String mark = "?";
			int argCountOfA = StringUtils.countMatches(sqlA, mark);
			int argCountOfB = StringUtils.countMatches(sqlB, mark);
			argsOfA = new Object[argCountOfA];
			argsOfB = new Object[argCountOfB];
			for (int i = 0; i < argCountOfA; i++) {
				argsOfA[i] = args[i];
			}
			for (int i = 0; i < argCountOfB; i++) {
				argsOfB[i] = args[argCountOfA + i];
			}
		}
		int amount = queryForInt(JdbcUtils.getCountSQL(sqlA), argsOfA);
		amount += queryForInt(JdbcUtils.getCountSQL(sqlB), argsOfB);
		return amount;
	}

	/**
	 * 生成32位的uuid字符串.
	 * 
	 * @return 32位的uuid字符串
	 */
	protected String createId() {
		return uuid.generateHex();
	}

	/**
	 * 获取结果对象列表.
	 * 
	 * @param sql
	 *            sql语句(不包含动态参数?)
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, MultiRowMapper<PO> multiRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(sql);
		}
		long startTime = System.currentTimeMillis();

		try {
			return (List<PO>) getJdbcTemplate().query(sql,
					new MultiResultSetExtractor<PO>(multiRowMapper));
		} finally {
			processOvertime(startTime, sql, null);
		}
	}

	/**
	 * 通过分页的方式获取多行结果集.
	 * 
	 * @param sql
	 *            sql语句(不包含动态参数?)
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @param page
	 *            分页对象
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, MultiRowMapper<PO> multiRowMapper,
			Pagination page) {
		return query(sql, null, null, multiRowMapper, page);
	}

	/**
	 * 获取结果对象列表.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组
	 * @param argTypes
	 *            参数类型数组
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, Object[] args, int[] argTypes,
			MultiRowMapper<PO> multiRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (List<PO>) getJdbcTemplate().query(sql, args, argTypes,
					new MultiResultSetExtractor<PO>(multiRowMapper));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 通过分页的方式获取结果对象列表.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(如：new Object[] {id, name})
	 * @param argTypes
	 *            参数类型数组(如：new int[] {Types.VARCHAR, Types.INTEGER})，长度必须与参数数组一致
	 * @param multiRowMapper
	 *            处理多行结果集的接口(注意: 实现中不必调用rs.next())
	 * @param page
	 *            分页对象
	 * @return 结果对象列表
	 */
	protected List<PO> query(final String sql, final Object[] args,
			final int[] argTypes, final MultiRowMapper<PO> multiRowMapper,
			final Pagination page) {
		if (page == null) {
			throw new IllegalArgumentException(
					"the parameter: page cannot be null.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			final boolean isUsedCursor = page.isUseCursor();

			PreparedStatementCreator psCreator = new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					if (isUsedCursor) {
						return con.prepareStatement(sql,
								ResultSet.TYPE_SCROLL_INSENSITIVE,
								// ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
					}

					return con.prepareStatement(sql);
				}

			};

			PreparedStatementCallback psCallback = new PreparedStatementCallback() {

				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					// Set parameters to statement
					if (args == null) {
						; // Do not set parameters
					} else if (argTypes != null) {
						JdbcUtils
								.setSuitedParamsToStatement(args, argTypes, ps);
					} else {
						JdbcUtils.setParamsToStatement(args, ps);
					}

					ResultSet rs = null;

					try {
						rs = ps.executeQuery();

						if (isUsedCursor) { // Use cursor for paging
							rs.last();
							page.setMaxRowCount(rs.getRow());
						} else { // Not use cursor for paging
							page.setMaxRowCount(args == null ? count(sql)
									: count(sql, args));
						}

						page.initialize(); // Initialize the pagination

						// Get records of current page
						List<PO> results = new ArrayList<PO>();
						if (page.getMaxRowCount() > 0) {
							if (isUsedCursor) {
								// If current row is first one, move cursor
								// before
								// it
								if (page.getCurRowNum() == 1) {
									rs.beforeFirst();
								} else {
									rs.absolute(page.getCurRowNum() - 1);
								}
							} else {
								// If current row is not before the current page
								// index,
								// move cursor to next row
								while (rs.getRow() != (page.getCurRowNum() - 1)) {
									rs.next();
								}
							}

							// Add value objects into list
							int cursor = 0;
							while (rs.next() && cursor++ < page.getPageSize()) {
								results.add(multiRowMapper.mapRow(rs, cursor));
							}
						}

						return results;
					} finally {
						org.springframework.jdbc.support.JdbcUtils
								.closeResultSet(rs);
					}
				}
			};

			return (List<PO>) getJdbcTemplate().execute(psCreator, psCallback);
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取单行结果集对应的对象.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组
	 * @param argTypes
	 *            参数类型数组
	 * @param singleRowMapper
	 *            处理单行结果集的接口，把数据库记录转换成对象
	 * @return 结果对象
	 */
	protected PO query(String sql, Object[] args, int[] argTypes,
			SingleRowMapper<PO> singleRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (PO) getJdbcTemplate().query(sql, args, argTypes,
					new SingleResultSetExtractor<PO>(singleRowMapper));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取结果对象列表.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, Object[] args,
			MultiRowMapper<PO> multiRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (List<PO>) getJdbcTemplate().query(sql, args,
					new MultiResultSetExtractor<PO>(multiRowMapper));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 通过分页的方式获取多行结果集.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @param page
	 *            分页对象
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, Object[] args,
			MultiRowMapper<PO> multiRowMapper, Pagination page) {
		return query(sql, args, null, multiRowMapper, page);
	}

	/**
	 * 获取单行结果集对应的对象.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @param singleRowMapper
	 *            处理单行结果集的接口
	 * @return 结果对象
	 */
	protected PO query(String sql, Object[] args,
			SingleRowMapper<PO> singleRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (PO) getJdbcTemplate().query(sql, args,
					new SingleResultSetExtractor<PO>(singleRowMapper));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取单行结果集对应的对象.
	 * 
	 * @param sql
	 *            sql语句(不包含动态参数?)
	 * @param singleRowMapper
	 *            处理单行结果集的接口
	 * @return 结果对象
	 */
	protected PO query(String sql, SingleRowMapper<PO> singleRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(sql);
		}
		long startTime = System.currentTimeMillis();

		try {
			return (PO) getJdbcTemplate().query(sql,
					new SingleResultSetExtractor<PO>(singleRowMapper));
		} finally {
			processOvertime(startTime, sql, null);
		}
	}

	/**
	 * 获取结果对象列表.
	 * 
	 * @param sql
	 *            sql语句
	 * @param arg
	 *            单个参数(不能等于null)
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, String arg,
			MultiRowMapper<PO> multiRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, new Object[] { arg }));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (List<PO>) getJdbcTemplate().query(sql,
					new Object[] { arg },
					new MultiResultSetExtractor<PO>(multiRowMapper));
		} finally {
			processOvertime(startTime, sql, new Object[] { arg });
		}
	}

	/**
	 * 通过分页的方式获取多行结果集.
	 * 
	 * @param sql
	 *            sql语句
	 * @param arg
	 *            单个参数(不能等于null)
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @param page
	 *            分页对象
	 * @return 结果对象列表
	 */
	protected List<PO> query(String sql, String arg,
			MultiRowMapper<PO> multiRowMapper, Pagination page) {
		return query(sql, new Object[] { arg }, null, multiRowMapper, page);
	}

	/**
	 * 获取单行结果集对应的对象.
	 * 
	 * @param sql
	 *            sql语句
	 * @param arg
	 *            单个参数(不能等于null)
	 * @param singleRowMapper
	 *            处理单行结果集的接口
	 * @return 结果对象
	 */
	protected PO query(String sql, String arg,
			SingleRowMapper<PO> singleRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, new Object[] { arg }));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (PO) getJdbcTemplate().query(sql, new Object[] { arg },
					new SingleResultSetExtractor<PO>(singleRowMapper));
		} finally {
			processOvertime(startTime, sql, new Object[] { arg });
		}
	}

	/**
	 * @param prefix
	 *            In查询SQL语句前部分 <br>
	 *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
	 * @param prefixArgs
	 *            In查询SQL语句前部分的参数 <br>
	 *            eg:new Object[] { schoolId }
	 * @param inArgs
	 *            In参数 <br>
	 *            eg:teacherIds
	 * @param mapRowMapper
	 * @return Map
	 */
	// protected Map queryForInSQL(String prefix, Object[] prefixArgs,
	// Object[] inArgs, MapRowMapper mapRowMapper) {
	// return queryForInSQL(prefix, prefixArgs, inArgs, mapRowMapper, null);
	// }
	/**
	 * @param prefix
	 *            In查询SQL语句前部分 <br>
	 *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
	 * @param prefixArgs
	 *            In查询SQL语句前部分的参数 <br>
	 *            eg:new Object[] { schoolId }
	 * @param inArgs
	 *            In参数 <br>
	 *            eg:teacherIds
	 * @param postfix
	 *            sql的最后意部份 <br>
	 *            eg:ORDER BY c_stuid
	 * @param mapRowMapper
	 * @return Map
	 */
	// protected Map queryForInSQL(String prefix, Object[] prefixArgs,
	// Object[] inArgs, MapRowMapper mapRowMapper, String postfix) {
	// Map result = new HashMap();
	// if (inArgs == null || inArgs.length == 0) {
	// return result;
	// }
	//
	// queryForInSQL(prefix, prefixArgs, inArgs, mapRowMapper, postfix, result);
	//
	// return result;
	// }
	/**
	 * @param prefix
	 *            In查询SQL语句前部分 <br>
	 *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
	 * @param prefixArgs
	 *            In查询SQL语句前部分的参数 <br>
	 *            eg:new Object[] { schoolId }
	 * @param inArgs
	 *            In参数 <br>
	 *            eg:teacherIds
	 * @param multiRowMapper
	 * @return List
	 */
	protected List<PO> queryForInSQL(String prefix, Object[] prefixArgs,
			Object[] inArgs, MultiRowMapper<PO> multiRowMapper) {
		return queryForInSQL(prefix, prefixArgs, inArgs, multiRowMapper, null);

	}

	/**
	 * @param prefix
	 *            In查询SQL语句前部分 <br>
	 *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
	 * @param prefixArgs
	 *            In查询SQL语句前部分的参数 <br>
	 *            eg:new Object[] { schoolId }
	 * @param inArgs
	 *            In参数 <br>
	 *            eg:teacherIds
	 * @param postfix
	 *            sql的最后意部份 <br>
	 *            eg:ORDER BY c_stuid
	 * @param multiRowMapper
	 * @return List
	 */
	protected List<PO> queryForInSQL(String prefix, Object[] prefixArgs,
			Object[] inArgs, MultiRowMapper<PO> multiRowMapper, String postfix) {
		ArrayList<PO> result = new ArrayList<PO>();
		if (inArgs == null || inArgs.length == 0) {
			return result;
		}

		queryForInSQL(prefix, prefixArgs, inArgs, multiRowMapper, postfix,
				result);
		return result;
	}

	private void queryForInSQL(String prefix, Object[] prefixArgs,
			Object[] inArgs, Object rowMapper, String postfix, Object result) {
		if (prefixArgs == null) {
			prefixArgs = new Object[0];
		}

		int count = maxInSQLParamCount;
		for (int i = 0, length = inArgs.length; i < length; i += count) {

			if (i + maxInSQLParamCount > length) {
				count = length - i;
			}

			String sql = prefix + JdbcUtils.getInSQL(count);
			if (!Validators.isEmpty(postfix)) {
				sql += postfix;
			}
			Object[] args = new Object[count + prefixArgs.length];
			for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
				args[_i] = prefixArgs[_i];
			}

			System.arraycopy(inArgs, i, args, prefixArgs.length, count);
			if (rowMapper instanceof MapRowMapper) {
				((Map<Object, Object>) result).putAll(queryForMap(sql, args,
						(MapRowMapper) rowMapper));
			} else if (rowMapper instanceof MultiRowMapper) {
				((List<PO>) result).addAll(query(sql, args,
						(MultiRowMapper<PO>) rowMapper));
			}
		}
	}

	/**
	 * 获取单条记录的整型字段，当不存在记录时会抛出异常
	 * 
	 * @param sql
	 *            sql语句
	 * @return 单条记录的整型字段值
	 */
	protected int queryForInt(String sql) {
		return queryForInt(sql, null);
	}

	/**
	 * 获取单条记录的整型字段，当不存在记录时会抛出异常
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @return 单条记录的整型字段值
	 */
	protected int queryForInt(String sql, Object[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return args == null ? getJdbcTemplate().queryForInt(sql)
					: getJdbcTemplate().queryForInt(sql, args);
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取单条记录的长整型字段，当不存在记录时会抛出异常.
	 * 
	 * @param sql
	 *            sql语句
	 * @return 单条记录的长整型字段值
	 */
	protected long queryForLong(String sql) {
		return queryForLong(sql, null);
	}

	/**
	 * 获取单条记录的长整型字段，当不存在记录时会抛出异常.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @return 单条记录的长整型字段值
	 */
	protected long queryForLong(String sql, Object[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return args == null ? getJdbcTemplate().queryForLong(sql)
					: getJdbcTemplate().queryForLong(sql, args);
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取结果集的map
	 * 
	 * @param sql
	 *            sql语句
	 * @param mapRowMapper
	 *            处理多行记录集的接口,指定map的key和value
	 * @return 结果对象的map
	 */
	protected Map<Object, Object> queryForMap(String sql,
			MapRowMapper mapRowMapper) {
		return queryForMap(sql, null, null, mapRowMapper);
	}

	/**
	 * 获取结果集的map
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组
	 * @param argTypes
	 *            参数类型数组
	 * @param mapRowMapper
	 *            处理多行记录集的接口，指定map的key和value
	 * @return 结果对象的map
	 */
	protected Map<Object, Object> queryForMap(String sql, Object[] args,
			int[] argTypes, MapRowMapper mapRowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			if (args == null && argTypes == null) {
				return (Map<Object, Object>) getJdbcTemplate().query(sql,
						new MapResultExtractor(mapRowMapper));
			}

			if (argTypes == null) {
				return (Map<Object, Object>) getJdbcTemplate().query(sql, args,
						new MapResultExtractor(mapRowMapper));
			}

			return (Map<Object, Object>) getJdbcTemplate().query(sql, args,
					argTypes, new MapResultExtractor(mapRowMapper));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 通过分页的方式获取结果集的map
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @param argTypes
	 *            参数类型
	 * @param mapRowMapper
	 *            处理每行结果集的接口, 指定map的key和value <br>
	 *            (注意: 实现中不必调用rs.next())
	 * @param page
	 *            分页对象
	 * @return 结果对象的map
	 */
	protected Map<Object, Object> queryForMap(final String sql,
			final Object[] args, final int[] argTypes,
			final MapRowMapper mapRowMapper, final Pagination page) {
		if (page == null) {
			throw new IllegalArgumentException(
					"the parameter: page cannot be null.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			final boolean isUsedCursor = page.isUseCursor();

			PreparedStatementCreator psCreator = new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					if (isUsedCursor) {
						return con.prepareStatement(sql,
								ResultSet.TYPE_SCROLL_INSENSITIVE,
								// ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
					}

					return con.prepareStatement(sql);
				}

			};

			PreparedStatementCallback psCallback = new PreparedStatementCallback() {

				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					// Set parameters to statement
					if (args == null) {
						; // Do not set parameters
					} else if (argTypes != null) {
						JdbcUtils
								.setSuitedParamsToStatement(args, argTypes, ps);
					} else {
						JdbcUtils.setParamsToStatement(args, ps);
					}

					ResultSet rs = null;

					try {
						rs = ps.executeQuery();

						if (isUsedCursor) { // Use cursor for paging
							rs.last();
							page.setMaxRowCount(rs.getRow());
						} else { // Not use cursor for paging
							page.setMaxRowCount(args == null ? count(sql)
									: count(sql, args));
						}

						page.initialize(); // Initialize the pagination

						// Get records of current page
						Map<Object, Object> results = new HashMap<Object, Object>();
						if (page.getMaxRowCount() > 0) {
							if (isUsedCursor) {
								// If current row is first one, move cursor
								// before
								// it
								if (page.getCurRowNum() == 1) {
									rs.beforeFirst();
								} else {
									rs.absolute(page.getCurRowNum() - 1);
								}
							} else {
								// If current row is not before the current page
								// index,
								// move cursor to next row
								while (rs.getRow() != (page.getCurRowNum() - 1)) {
									rs.next();
								}
							}

							// Add value objects into list
							int cursor = 0;
							while (rs.next() && cursor++ < page.getPageSize()) {
								results.put(mapRowMapper.mapRowKey(rs, cursor),
										mapRowMapper.mapRowValue(rs, cursor));
							}
						}

						return results;
					} finally {
						org.springframework.jdbc.support.JdbcUtils
								.closeResultSet(rs);
					}
				}
			};

			return (Map<Object, Object>) getJdbcTemplate().execute(psCreator,
					psCallback);
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取结果集的map
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @param mapRowMapper
	 *            处理多行记录集的接口,指定map的key和value
	 * @return 结果对象的map
	 */
	protected Map<Object, Object> queryForMap(String sql, Object[] args,
			MapRowMapper mapRowMapper) {
		return queryForMap(sql, args, null, mapRowMapper);
	}

	/**
	 * 获取单条记录的字符型字段，该字段值不允许null，当不存在记录时返回null
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @return 单条记录的字符型字段值
	 */
	protected String queryForNotNullString(String sql, Object[] args) {

		return (String) getJdbcTemplate().query(
				sql,
				args,
				new SingleResultSetExtractor<String>(
						new SingleRowMapper<String>() {
							public String mapRow(ResultSet rs)
									throws SQLException {
								return rs.getString(1);
							}
						}));
	}

	/**
	 * 获取单条记录的字符型字段，当不存在记录时会抛出异常
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @return 单条记录的字符型字段值
	 */
	protected String queryForString(String sql, Object[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (String) getJdbcTemplate().queryForObject(sql, args,
					String.class);
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 获取结果对象列表, 数量不超过topLimit.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组
	 * @param argTypes
	 *            参数类型数组
	 * @param multiRowMapper
	 *            处理多行结果集的接口
	 * @param topLimit
	 *            数量限制
	 * @return 结果对象列表
	 */
	protected List<PO> queryForTop(String sql, Object[] args, int[] argTypes,
			MultiRowMapper<PO> multiRowMapper, int topLimit) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			return (List<PO>) getJdbcTemplate()
					.query(
							sql,
							args,
							argTypes,
							new MultiTopResultSetExtractor<PO>(multiRowMapper,
									topLimit));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 支持JasperReport的结果集获取
	 * 
	 * @param sql
	 *            sql语句
	 * @return JasperReport 数据集
	 */
	// protected JRResultSetDataSource queryForJRResultSet(String sql) {
	// return queryForJRResultSet(sql, null, null);
	// }
	/**
	 * 支持JasperReport的结果集获取
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @return JasperReport 数据集
	 */
	// protected JRResultSetDataSource queryForJRResultSet(String sql,
	// Object[] args) {
	// return queryForJRResultSet(sql, args, null);
	// }
	/**
	 * 支持JasperReport的结果集获取
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数
	 * @param argTypes
	 *            参数类型
	 * @return JasperReport 数据集
	 */
	// protected JRResultSetDataSource queryForJRResultSet(String sql,
	// Object[] args, int[] argTypes) {
	// if (logger.isDebugEnabled()) {
	// logger.debug(JdbcUtils.getSQL(sql, args));
	// }
	// long startTime = System.currentTimeMillis();
	//
	// try {
	// if (args == null && argTypes == null) {
	// return (JRResultSetDataSource) getJdbcTemplate().query(sql,
	// new JRResultSetExtractor());
	// }
	//
	// if (argTypes == null) {
	// return (JRResultSetDataSource) getJdbcTemplate().query(sql,
	// args, new JRResultSetExtractor());
	// }
	//
	// return (JRResultSetDataSource) getJdbcTemplate().query(sql, args,
	// argTypes, new JRResultSetExtractor());
	// }
	// finally {
	// processOvertime(startTime, sql, args);
	// }
	// }
	/**
	 * 更新数据.
	 * 
	 * @param sql
	 *            sql语句
	 * @return 更新的记录条数
	 */
	protected int update(String sql) {
		return update(sql, null, null);
	}

	/**
	 * 更新数据.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组(全部不能等于null)
	 * @return 更新的记录条数
	 */
	protected int update(String sql, Object[] args) {
		return update(sql, args, null);
	}

	/**
	 * 更新数据.
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数数组
	 * @param argTypes
	 *            参数类型数组
	 * @return 更新的记录条数
	 */
	protected int update(String sql, Object[] args, int[] argTypes) {

		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			if (args == null && argTypes == null) {
				return getJdbcTemplate().update(sql);
			}

			if (argTypes == null) {
				return getJdbcTemplate().update(sql, args);
			}

			return getJdbcTemplate().update(sql, args, argTypes);

		} finally {
			processOvertime(startTime, sql, args);
		}
	}

	/**
	 * 更新数据.
	 * 
	 * @param sql
	 *            sql语句
	 * @param arg
	 *            参数
	 * @return 更新的记录条数
	 */
	protected int update(String sql, String arg) {
		return update(sql, new Object[] { arg }, null);
	}

	/**
	 * 含in参数的更新.
	 * 
	 * @param prefix
	 *            In查询SQL语句前部分 <br>
	 *            eg:UPDATE dt_teacher WHERE c_schid = ? AND c_teachid IN
	 * @param prefixArgs
	 *            In查询SQL语句前部分的参数 <br>
	 *            eg:new Object[] { schoolId }
	 * @param inArgs
	 *            In参数 <br>
	 *            eg:teacherIds
	 * @return
	 */
	protected int updateForInSQL(String prefix, Object[] prefixArgs,
			Object[] inArgs) {
		return updateForInSQL(prefix, prefixArgs, inArgs, null);
	}

	/**
	 * 含in参数的更新.
	 * 
	 * @param prefix
	 *            In查询SQL语句前部分 <br>
	 *            eg:UPDATE dt_teacher WHERE c_schid = ? AND c_teachid IN
	 * @param prefixArgs
	 *            In查询SQL语句前部分的参数 <br>
	 *            eg:new Object[] { schoolId }
	 * @param inArgs
	 *            In参数 <br>
	 *            eg:teacherIds
	 * @param postfix
	 *            sql的最后意部份 <br>
	 *            eg:))
	 * @return
	 */
	protected int updateForInSQL(String prefix, Object[] prefixArgs,
			Object[] inArgs, String postfix) {
		int result = 0;

		if (prefixArgs == null) {
			prefixArgs = new Object[0];
		}

		int count = maxInSQLParamCount;
		for (int i = 0, length = inArgs.length; i < length; i += count) {
			if (i + maxInSQLParamCount > length) {
				count = length - i;
			}

			String sql = prefix + JdbcUtils.getInSQL(count);
			if (!Validators.isEmpty(postfix)) {
				sql += postfix;
			}
			Object[] args = new Object[count + prefixArgs.length];
			for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
				args[_i] = prefixArgs[_i];
			}

			System.arraycopy(inArgs, i, args, prefixArgs.length, count);
			result += update(sql, args);
		}
		return result;
	}

	/**
	 * 把处理多行记录集的接口转化为处理单行记录集的接口
	 * 
	 * @param multiRowMapper
	 * @return
	 */
	protected SingleRowMapper beSingle(final MultiRowMapper multiRowMapper) {
		return new SingleRowMapper() {

			public Object mapRow(ResultSet rs) throws SQLException {
				return multiRowMapper.mapRow(rs, 0);
			}

		};
	}

	/**
	 * 把MapRowMapper转化为处理单行记录集的接口
	 * 
	 * @param mapRowMapper
	 * @return
	 */
	protected SingleRowMapper beSingle(final MapRowMapper mapRowMapper) {
		return new SingleRowMapper() {

			public Object mapRow(ResultSet rs) throws SQLException {
				return mapRowMapper.mapRowValue(rs, 0);
			}

		};
	}

	/**
	 * 把MapRowMapper转化为处理多行记录集的接口
	 * 
	 * @param mapRowMapper
	 * @return
	 */
	protected MultiRowMapper beMulti(final MapRowMapper mapRowMapper) {
		return new MultiRowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return mapRowMapper.mapRowValue(rs, rowNum);
			}

		};
	}

	/**
	 * 处理执行超时，日志记录
	 * 
	 * @param startTime
	 * @param endTime
	 * @param sql
	 * @param args
	 */
	private void processOvertime(long startTime, String sql, Object[] args) {
		long spendTime = System.currentTimeMillis() - startTime;
		if (spendTime > EXECUTE_OVERTIME) {
			sql = JdbcUtils.getSQL(sql, args);
			logger.fatal("Overtime " + spendTime + "ms " + sql);
		}
	}

	/**
	 * 设置SQL批处理操作的记录数量.
	 * 
	 * @param batchSize
	 *            批处理记录大小
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * 获取IN SQL中参数允许的最大数量.
	 * 
	 * @return IN SQL中参数允许的最大数量.
	 */
	public int getMaxInSQLParamCount() {
		return maxInSQLParamCount;
	}

	/**
	 * 设置IN SQL中参数允许的最大数量.
	 * 
	 * @param maxInSQLParamCount
	 *            IN SQL中参数允许的最大数量
	 */
	public void setMaxInSQLParamCount(int maxInSQLParamCount) {
		this.maxInSQLParamCount = maxInSQLParamCount;
	}

	/**
	 * 打印一个uuid.
	 * 
	 * @param args
	 *            无用参数数组
	 */
	public static void main(String[] args) {
		// BasicDao basicDAO = new BasicDao();
		// System.out.println(basicDAO.createId());
	}

}
