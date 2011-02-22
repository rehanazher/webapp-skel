/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.aaut.skeleton.commons.util.dao.BasicDaoSupport;
import com.aaut.skeleton.commons.util.dao.MultiRowMapper;
import com.aaut.skeleton.commons.util.dao.SingleRowMapper;
import com.aaut.skeleton.rbac.po.Action;

public class ActionDaoImpl extends BasicDaoSupport<Action> implements
		ActionDao<Action> {

	private static class ActionMultiRowMapper implements MultiRowMapper<Action> {
		public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
			Action action = new Action();
			action.setId(rs.getString("id"));
			action.setName(rs.getString("name"));
			action.setAction(rs.getString("action"));
			action.setNamespace(rs.getString("namespace"));
			return action;
		}
	}

	private static class ActionSingleRowMapper implements
			SingleRowMapper<Action> {
		public Action mapRow(ResultSet rs) throws SQLException {
			return new ActionMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_INSERT_ACTION = "INSERT INTO rbac_actions(id,name,action,"
			+ "namespace) " + "VALUES(?,?,?,?)";

	private static final String SQL_DELETE_ACTION = "DELETE FROM rbac_actions WHERE id=?";

	private static final String SQL_UPDATE_ACTION = "UPDATE rbac_actions SET name=?,action=?,"
			+ "namespace=? WHERE id=?";

	private static final String SQL_FIND_ACTION_BY_ID = "SELECT * FROM rbac_actions WHERE id=?";

	public String insert(Action action) {
		action.setId(createId());
		if (update(SQL_INSERT_ACTION, new Object[] { action.getId(),
				action.getName(), action.getAction(), action.getNamespace() },
				new int[] { Types.CHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR }) > 0) {
			return action.getId();
		} else {
			return null;
		}
	}

	public String delete(String actionId) {
		if (update(SQL_DELETE_ACTION, actionId) > 0) {
			return actionId;
		} else {
			return null;
		}
	}

	public int update(Action action) {
		return update(SQL_UPDATE_ACTION, new Object[] { action.getName(),
				action.getAction(), action.getNamespace(), action.getId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.CHAR });
	}

	public Action findById(String actionId) {
		return (Action) query(SQL_FIND_ACTION_BY_ID, actionId,
				new ActionSingleRowMapper());
	}

}
