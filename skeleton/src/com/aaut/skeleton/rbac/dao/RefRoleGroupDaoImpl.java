/**
 * Added by James
 * on 2011-2-22
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
import com.aaut.skeleton.rbac.po.RefRoleGroup;

public class RefRoleGroupDaoImpl extends BasicDaoSupport<RefRoleGroup>
		implements RefRoleGroupDao {

	private static class RefRoleGroupMultiRowMapper implements
			MultiRowMapper<RefRoleGroup> {
		public RefRoleGroup mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			RefRoleGroup refRoleGroup = new RefRoleGroup();
			refRoleGroup.setRoleId(rs.getString("role_id"));
			refRoleGroup.setGroupId(rs.getString("group_id"));
			return refRoleGroup;
		}
	}

	private static class RefRoleGroupSingleRowMapper implements
			SingleRowMapper<RefRoleGroup> {
		public RefRoleGroup mapRow(ResultSet rs) throws SQLException {
			return new RefRoleGroupMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_DELETE_BY_GROUP_ID = "DELETE FROM rbac_groups_roles WHERE group_id=?";
	private static final String SQL_DELETE_BY_ROLE_ID = "DELETE FROM rbac_groups_roles WHERE role_id=?";
	private static final String SQL_DELETE_BY_ROLE_GROUP_ID = "DELETE FROM rbac_groups_roles WHERE role_id=? AND group_id=?";

	private static final String SQL_SELECT_BY_GROUP_ID = "SELECT * FROM rbac_groups_roles WHERE group_id=?";
	private static final String SQL_SELECT_BY_ROLE_ID = "SELECT * FROM rbac_groups_roles WHERE role_id=?";
	private static final String SQL_SELECT_BY_ROLE_GROUP_ID = "SELECT * FROM rbac_groups_roles WHERE role_id=? AND group_id=?";

	private static final String SQL_INSERT = "INSERT INTO rbac_groups_roles(role_id,group_id) VALUES (?,?)";

	@Override
	public int deleteByGroupId(String groupId) {
		return update(SQL_DELETE_BY_GROUP_ID, groupId);
	}

	@Override
	public int deleteByRefId(String roleId, String groupId) {
		return update(SQL_DELETE_BY_ROLE_GROUP_ID, new Object[] { roleId,
				groupId });
	}

	@Override
	public int deleteByRoleId(String roleId) {
		return update(SQL_DELETE_BY_ROLE_ID, roleId);
	}

	@Override
	public List<RefRoleGroup> findByGroupId(String groupId) {
		return query(SQL_SELECT_BY_GROUP_ID, groupId,
				new RefRoleGroupMultiRowMapper());
	}

	@Override
	public RefRoleGroup findByRefId(String roleId, String groupId) {
		return query(SQL_SELECT_BY_ROLE_GROUP_ID, new Object[] { roleId,
				groupId }, new RefRoleGroupSingleRowMapper());
	}

	@Override
	public List<RefRoleGroup> findByRoleId(String roleId) {
		return query(SQL_SELECT_BY_ROLE_ID, roleId,
				new RefRoleGroupMultiRowMapper());
	}

	@Override
	public int insert(RefRoleGroup... refs) {
		if (Validators.isEmpty(refs)) {
			return 0;
		}

		List<Object[]> argsList = new ArrayList<Object[]>();
		for (RefRoleGroup ug : refs) {
			argsList.add(new Object[] { ug.getRoleId(), ug.getGroupId() });
		}

		int[] argTypes = { Types.CHAR, Types.CHAR };

		return batchUpdate(SQL_INSERT, argsList, argTypes);
	}

}
