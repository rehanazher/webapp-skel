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
import com.aaut.skeleton.rbac.po.RefUserGroup;

public class RefUserGroupDaoImpl extends BasicDaoSupport<RefUserGroup>
		implements RefUserGroupDao {

	private static class RefUserGroupMultiRowMapper implements
			MultiRowMapper<RefUserGroup> {
		public RefUserGroup mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			RefUserGroup refUserGroup = new RefUserGroup();
			refUserGroup.setUserId(rs.getString("user_id"));
			refUserGroup.setGroupId(rs.getString("group_id"));
			return refUserGroup;
		}
	}

	private static class RefUserGroupSingleRowMapper implements
			SingleRowMapper<RefUserGroup> {
		public RefUserGroup mapRow(ResultSet rs) throws SQLException {
			return new RefUserGroupMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_DELETE_BY_GROUP_ID = "DELETE FROM rbac_users_groups WHERE group_id=?";
	private static final String SQL_DELETE_BY_USER_ID = "DELETE FROM rbac_users_groups WHERE user_id=?";
	private static final String SQL_DELETE_BY_USER_GROUP_ID = "DELETE FROM rbac_users_groups WHERE user_id=? AND group_id=?";

	private static final String SQL_SELECT_BY_GROUP_ID = "SELECT * FROM rbac_users_groups WHERE group_id=?";
	private static final String SQL_SELECT_BY_USER_ID = "SELECT * FROM rbac_users_groups WHERE user_id=?";
	private static final String SQL_SELECT_BY_USER_GROUP_ID = "SELECT * FROM rbac_users_groups WHERE user_id=? AND group_id=?";

	private static final String SQL_INSERT = "INSERT INTO rbac_users_groups(user_id,group_id) VALUES (?,?)";

	@Override
	public int deleteByGroupId(String groupId) {
		return update(SQL_DELETE_BY_GROUP_ID, groupId);
	}

	@Override
	public int deleteByRefId(String userId, String groupId) {
		return update(SQL_DELETE_BY_USER_GROUP_ID, new Object[] { userId,
				groupId });
	}

	@Override
	public int deleteByUserId(String userId) {
		return update(SQL_DELETE_BY_USER_ID, userId);
	}

	@Override
	public List<RefUserGroup> findByGroupId(String groupId) {
		return query(SQL_SELECT_BY_GROUP_ID, groupId,
				new RefUserGroupMultiRowMapper());
	}

	@Override
	public RefUserGroup findByRefId(String userId, String groupId) {
		return query(SQL_SELECT_BY_USER_GROUP_ID, new Object[] { userId,
				groupId }, new RefUserGroupSingleRowMapper());
	}

	@Override
	public List<RefUserGroup> findByUserId(String userId) {
		return query(SQL_SELECT_BY_USER_ID, userId,
				new RefUserGroupMultiRowMapper());
	}

	@Override
	public int insert(RefUserGroup... refs) {
		if (Validators.isEmpty(refs)){
			return 0;
		}
		
		List<Object[]> argsList = new ArrayList<Object[]>();
		for (RefUserGroup ug: refs){
			argsList.add(new Object[]{ug.getUserId(), ug.getGroupId()});
		}

        int[] argTypes = { Types.CHAR, Types.CHAR};

        return batchUpdate(SQL_INSERT, argsList, argTypes);
	}

}
