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
import com.aaut.skeleton.rbac.po.Role;

public class RoleDaoImpl extends BasicDaoSupport<Role> implements RoleDao<Role> {
	
	private static class RoleMultiRowMapper implements MultiRowMapper<Role> {
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setId(rs.getString("id"));
			role.setName(rs.getString("name"));
			role.setDescription(rs.getString("description"));
			return role;
		}
	}

	private static class RoleSingleRowMapper implements SingleRowMapper<Role> {
		public Role mapRow(ResultSet rs) throws SQLException {
			return new RoleMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_INSERT_ROLE = "INSERT INTO rbac_roles(id,name,description) "
			+ "VALUES(?,?,?)";

	private static final String SQL_DELETE_ROLE = "DELETE FROM rbac_roles WHERE id=?";

	private static final String SQL_UPDATE_ROLE = "UPDATE rbac_roles SET name=?,description=? WHERE id=?";

	private static final String SQL_FIND_ROLE_BY_ID = "SELECT * FROM rbac_roles WHERE id=?";

	public String insert(Role role) {
		role.setId(createId());
		if (update(SQL_INSERT_ROLE, new Object[] { role.getId(),
				role.getName(), role.getDescription() }, new int[] {
				Types.CHAR, Types.VARCHAR, Types.VARCHAR }) > 0) {
			return role.getId();
		} else {
			return null;
		}
	}

	public String delete(String roleId) {
		if (update(SQL_DELETE_ROLE, roleId) > 0) {
			return roleId;
		} else {
			return null;
		}
	}

	public int update(Role role) {
		return update(SQL_UPDATE_ROLE, new Object[] { role.getName(),
				role.getDescription(), role.getId() }, new int[] {
				Types.VARCHAR, Types.VARCHAR, Types.CHAR });
	}

	public Role findById(String roleId) {
		return (Role) query(SQL_FIND_ROLE_BY_ID, roleId,
				new RoleSingleRowMapper());
	}

}
