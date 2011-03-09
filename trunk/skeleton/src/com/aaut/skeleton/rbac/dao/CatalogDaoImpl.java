/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.aaut.skeleton.commons.util.dao.BasicDaoSupport;
import com.aaut.skeleton.commons.util.dao.MultiRowMapper;
import com.aaut.skeleton.commons.util.dao.SingleRowMapper;
import com.aaut.skeleton.rbac.po.Catalog;

public class CatalogDaoImpl extends BasicDaoSupport<Catalog> implements
		CatalogDao<Catalog> {

	private static class CatalogMultiRowMapper implements
			MultiRowMapper<Catalog> {
		public Catalog mapRow(ResultSet rs, int rowNum) throws SQLException {
			Catalog catalog = new Catalog();
			catalog.setId(rs.getString("id"));
			catalog.setName(rs.getString("name"));
			catalog.setWeight(rs.getInt("weight"));
			catalog.setParentId(rs.getString("parent_id"));
			catalog.setDisabled(rs.getInt("disabled"));
			return catalog;
		}
	}

	private static class CatalogSingleRowMapper implements
			SingleRowMapper<Catalog> {
		public Catalog mapRow(ResultSet rs) throws SQLException {
			return new CatalogMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_INSERT_CATALOG = "INSERT INTO rbac_catalogs(id,name,weight,"
			+ "parent_id,disabled) " + "VALUES(?,?,?,?,?)";

	private static final String SQL_DELETE_CATALOG = "DELETE FROM rbac_catalogs WHERE id=?";

	private static final String SQL_UPDATE_CATALOG = "UPDATE rbac_catalogs SET name=?,weight=?,"
			+ "parent_id=?,disabled=? WHERE id=?";

	private static final String SQL_FIND_CATALOG_BY_ID = "SELECT * FROM rbac_catalogs WHERE id=?";

	private static final String SQL_FIND_ALL = "SELECT * FROM rbac_catalogs";

	public String insert(Catalog catalog) {
		catalog.setId(createId());
		if (update(SQL_INSERT_CATALOG, new Object[] { catalog.getId(),
				catalog.getName(), catalog.getWeight(), catalog.getParentId(),
				catalog.getDisabled() }, new int[] { Types.CHAR, Types.VARCHAR,
				Types.INTEGER, Types.CHAR, Types.DECIMAL }) > 0) {
			return catalog.getId();
		} else {
			return null;
		}
	}

	public String delete(String catalogId) {
		if (update(SQL_DELETE_CATALOG, catalogId) > 0) {
			return catalogId;
		} else {
			return null;
		}
	}

	public int update(Catalog catalog) {
		return update(SQL_UPDATE_CATALOG, new Object[] { catalog.getName(),
				catalog.getWeight(), catalog.getParentId(),
				catalog.getDisabled(), catalog.getId() }, new int[] {
				Types.VARCHAR, Types.INTEGER, Types.CHAR, Types.DECIMAL,
				Types.CHAR });
	}

	public Catalog findById(String catalogId) {
		return query(SQL_FIND_CATALOG_BY_ID, catalogId,
				new CatalogSingleRowMapper());
	}

	@Override
	public List<Catalog> findAll() {
		return query(SQL_FIND_ALL, new CatalogMultiRowMapper());
	}
}
