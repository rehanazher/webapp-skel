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
import com.aaut.skeleton.rbac.po.Group;

public class GroupDaoImpl extends BasicDaoSupport<Group> implements
		GroupDao<Group> {

	private static class GroupMultiRowMapper implements MultiRowMapper<Group> {
		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
			Group group = new Group();
			group.setId(rs.getString("id"));
			group.setName(rs.getString("name"));
			group.setDescription(rs.getString("description"));
			group.setBlocked(rs.getInt("blocked"));
			group.setSystem(rs.getInt("system"));
			return group;
		}
	}

	private static class GroupSingleRowMapper implements SingleRowMapper<Group> {
		public Group mapRow(ResultSet rs) throws SQLException {
			return new GroupMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_INSERT_GROUP = "INSERT INTO rbac_groups(id,name,description,"
			+ "blocked,system) " + "VALUES(?,?,?,?,?)";

	private static final String SQL_DELETE_GROUP = "DELETE FROM rbac_groups WHERE id=?";

	private static final String SQL_UPDATE_GROUP = "UPDATE rbac_groups SET name=?,description=?,"
			+ "blocked=?,system=? WHERE id=?";

	private static final String SQL_FIND_GROUP_BY_ID = "SELECT * FROM rbac_groups WHERE id=?";

	public String insert(Group group) {
		group.setId(createId());
		if (update(SQL_INSERT_GROUP, new Object[] { group.getId(),
				group.getName(), group.getDescription(), group.getBlocked(),
				group.getSystem() }, new int[] { Types.CHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.INTEGER }) > 0) {
			return group.getId();
		} else {
			return null;
		}
	}

	public String delete(String groupId) {
		if (update(SQL_DELETE_GROUP, groupId) > 0) {
			return groupId;
		} else {
			return null;
		}
	}

	public int update(Group group) {
		return update(SQL_UPDATE_GROUP, new Object[] { group.getName(),
				group.getDescription(), group.getBlocked(), group.getSystem(),
				group.getId() }, new int[] { Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.INTEGER, Types.CHAR });
	}

	public Group findById(String groupId) {
		return (Group) query(SQL_FIND_GROUP_BY_ID, groupId,
				new GroupSingleRowMapper());
	}

}
