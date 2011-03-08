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
import com.aaut.skeleton.rbac.po.RefCatalogOperativeAction;

public class RefCatalogOperativeActionDaoImpl extends
		BasicDaoSupport<RefCatalogOperativeAction> implements
		RefCatalogOperativeActionDao {

	private static class RefCatalogOperativeActionMultiRowMapper implements
			MultiRowMapper<RefCatalogOperativeAction> {
		public RefCatalogOperativeAction mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			RefCatalogOperativeAction refCatalogOperativeAction = new RefCatalogOperativeAction();
			refCatalogOperativeAction.setCatalogId(rs.getString("catalog_id"));
			refCatalogOperativeAction.setActionId(rs.getString("action_id"));
			refCatalogOperativeAction.setOperativeId(rs
					.getString("operative_id"));
			return refCatalogOperativeAction;
		}
	}

	private static class RefCatalogOperativeActionSingleRowMapper implements
			SingleRowMapper<RefCatalogOperativeAction> {
		public RefCatalogOperativeAction mapRow(ResultSet rs)
				throws SQLException {
			return new RefCatalogOperativeActionMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_DELETE_BY_ACTION_ID = "DELETE FROM rbac_catalog_operative_action WHERE action_id=?";
	private static final String SQL_DELETE_BY_CATALOG_ID = "DELETE FROM rbac_catalog_operative_action WHERE catalog_id=?";
	private static final String SQL_DELETE_BY_CATALOG_ID_OPERATIVE_ID = "DELETE FROM rbac_catalog_operative_action WHERE catalog_id=? AND operative_id=?";
	private static final String SQL_DELETE_BY_REF_ID = "DELETE FROM rbac_catalog_operative_action WHERE catalog_id=? AND operative_id=? AND action_id=?";

	private static final String SQL_FIND_BY_ACTION_ID = "SELECT * FROM rbac_catalog_operative_action WHERE action_id=?";
	private static final String SQL_FIND_BY_CATALOG_ID = "SELECT * FROM rbac_catalog_operative_action WHERE catalog_id=?";
	private static final String SQL_FIND_BY_CATALOG_ID_OPERATIVE_ID = "SELECT * FROM rbac_catalog_operative_action WHERE catalog_id=? AND operative_id=?";
	private static final String SQL_FIND_BY_REF_ID = "SELECT * FROM rbac_catalog_operative_action WHERE catalog_id=? AND operative_id=? AND action_id=?";

	private static final String SQL_INSERT = "INSERT INTO rbac_catalog_operative_action(catalog_id, operative_id, action_id) VALUES(?,?,?)";

	@Override
	public int deleteByActionId(String actionId) {
		return update(SQL_DELETE_BY_ACTION_ID, actionId);
	}

	@Override
	public int deleteByCatalogId(String catalogId) {
		return update(SQL_DELETE_BY_CATALOG_ID, catalogId);
	}

	@Override
	public int deleteByCatalogIdOperativeId(String catalogId, String operativeId) {
		return update(SQL_DELETE_BY_CATALOG_ID_OPERATIVE_ID, new Object[] {
				catalogId, operativeId });
	}

	@Override
	public int deleteByRefId(String catalogId, String operativeId,
			String actionId) {
		return update(SQL_DELETE_BY_REF_ID, new Object[] { catalogId,
				operativeId, actionId });
	}

	@Override
	public List<RefCatalogOperativeAction> findByActionId(String actionId) {
		return query(SQL_FIND_BY_ACTION_ID, actionId,
				new RefCatalogOperativeActionMultiRowMapper());
	}

	@Override
	public List<RefCatalogOperativeAction> findByCatalogId(String catalogId) {
		return query(SQL_FIND_BY_CATALOG_ID, catalogId,
				new RefCatalogOperativeActionMultiRowMapper());
	}

	@Override
	public List<RefCatalogOperativeAction> findByCatalogIdOperativeId(
			String catalogId, String operativeId) {
		return query(SQL_FIND_BY_CATALOG_ID_OPERATIVE_ID, new Object[] {
				catalogId, operativeId },
				new RefCatalogOperativeActionMultiRowMapper());
	}

	@Override
	public RefCatalogOperativeAction findByRefId(String catalogId,
			String operativeId, String actionId) {
		return query(SQL_FIND_BY_REF_ID, new Object[] { catalogId, operativeId,
				actionId }, new RefCatalogOperativeActionSingleRowMapper());
	}

	@Override
	public int insert(RefCatalogOperativeAction... refs) {
		if (Validators.isEmpty(refs)) {
			return 0;
		}

		List<Object[]> argsList = new ArrayList<Object[]>();
		for (RefCatalogOperativeAction ref : refs) {
			argsList.add(new Object[] { ref.getCatalogId(),
					ref.getOperativeId(), ref.getActionId() });
		}

		int[] argTypes = { Types.CHAR, Types.CHAR, Types.CHAR };

		return batchUpdate(SQL_INSERT, argsList, argTypes);
	}

}
