/**
 * Added by James
 * on 2011-3-8
 */
package com.aaut.skeleton.rbac.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.aaut.skeleton.commons.util.Validators;
import com.aaut.skeleton.commons.util.dao.BasicDaoSupport;
import com.aaut.skeleton.commons.util.dao.MultiRowMapper;
import com.aaut.skeleton.commons.util.dao.SingleRowMapper;
import com.aaut.skeleton.rbac.po.RefRoleCatalogOperative;

public class RefRoleCatalogOperativeDaoImpl extends
		BasicDaoSupport<RefRoleCatalogOperative> implements
		RefRoleCatalogOperativeDao {

	private static class RefRoleCatalogOperativeMultiRowMapper implements
			MultiRowMapper<RefRoleCatalogOperative> {
		public RefRoleCatalogOperative mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			RefRoleCatalogOperative ref = new RefRoleCatalogOperative();
			ref.setRoleId(rs.getString("role_id"));
			ref.setCatalogId(rs.getString("catalog_id"));
			ref.setOperativeId(rs.getString("operative_id"));
			return ref;
		}
	}

	private static class RefRoleCatalogOperativeSingleRowMapper implements
			SingleRowMapper<RefRoleCatalogOperative> {
		public RefRoleCatalogOperative mapRow(ResultSet rs) throws SQLException {
			return new RefRoleCatalogOperativeMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_DELETE_BY_CATALOG_ID = "DELETE FROM rbac_roles_catalogs_operatives WHERE catalog_id=?";
	private static final String SQL_DELETE_BY_ROLE_ID = "DELETE FROM rbac_roles_catalogs_operatives WHERE role_id=?";
	private static final String SQL_DELETE_BY_REF_ID = "DELETE FROM rbac_roles_catalogs_operatives WHERE role_id=? AND catalog_id=? AND operative_id=?";

	private static final String SQL_FIND_BY_CATALOG_ID = "SELECT * FROM rbac_roles_catalogs_operatives WHERE catalog_id=?";
	private static final String SQL_FIND_BY_ROLE_ID = "SELECT * FROM rbac_roles_catalogs_operatives WHERE role_id=?";
	private static final String SQL_FIND_BY_REF_ID = "SELECT * FROM rbac_roles_catalogs_operatives WHERE role_id=? AND catalog_id=? AND operative_id=?";

	private static final String SQL_INSERT = "INSERT INTO rbac_roles_catalogs_operatives(role_id, catalog_id, operative_id) VALUES(?,?,?)";

	@Override
	public int deleteByCatalogId(String catalogId) {
		return update(SQL_DELETE_BY_CATALOG_ID, catalogId);
	}

	@Override
	public int deleteByRefId(String roleId, String catalogId, String operativeId) {
		return update(SQL_DELETE_BY_REF_ID, new Object[] { roleId, catalogId,
				operativeId });
	}

	@Override
	public int deleteByRoleId(String roleId) {
		return update(SQL_DELETE_BY_ROLE_ID, roleId);
	}

	@Override
	public List<RefRoleCatalogOperative> findByCatalogId(String catalogId) {
		return query(SQL_FIND_BY_CATALOG_ID, catalogId,
				new RefRoleCatalogOperativeMultiRowMapper());
	}

	@Override
	public RefRoleCatalogOperative findByRefId(String roleId, String catalogId,
			String operativeId) {
		return query(SQL_FIND_BY_REF_ID, new Object[] { roleId, catalogId,
				operativeId }, new RefRoleCatalogOperativeSingleRowMapper());
	}

	@Override
	public List<RefRoleCatalogOperative> findByRoleId(String roleId) {
		return query(SQL_FIND_BY_ROLE_ID, roleId,
				new RefRoleCatalogOperativeMultiRowMapper());
	}

	@Override
	public int insert(RefRoleCatalogOperative... refs) {
		if (Validators.isEmpty(refs)) {
			return 0;
		}

		List<Object[]> argsList = new ArrayList<Object[]>();
		for (RefRoleCatalogOperative ref : refs) {
			argsList.add(new Object[] { ref.getRoleId(), ref.getCatalogId(),
					ref.getOperativeId() });
		}

		int[] argTypes = { Types.CHAR, Types.CHAR, Types.CHAR };

		return batchUpdate(SQL_INSERT, argsList, argTypes);
	}
}
