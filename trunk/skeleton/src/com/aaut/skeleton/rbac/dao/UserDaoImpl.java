/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.aaut.skeleton.commons.util.dao.BasicDaoSupport;
import com.aaut.skeleton.commons.util.dao.MultiRowMapper;
import com.aaut.skeleton.commons.util.dao.SingleRowMapper;
import com.aaut.skeleton.rbac.po.User;

public class UserDaoImpl extends BasicDaoSupport<User> implements UserDao<User> {

	private static class UserMultiRowMapper implements MultiRowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setLastName(rs.getString("last_name"));
			user.setFirstName(rs.getString("first_name"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setBlocked(rs.getInt("blocked"));
			user.setDescription(rs.getString("description"));
			user.setSuperUser(rs.getInt("super_user"));
			user.setCreationTime(rs.getTimestamp("creation_time"));
			return user;
		}
	}

	private static class UserSingleRowMapper implements SingleRowMapper<User> {
		public User mapRow(ResultSet rs) throws SQLException {
			return new UserMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_INSERT_USER = "INSERT INTO rbac_users(id,name,last_name,"
			+ "first_name,email,password,blocked,description,super_user,creation_time) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?)";

	private static final String SQL_DELETE_USER = "DELETE FROM rbac_users WHERE id=?";

	private static final String SQL_UPDATE_USER = "UPDATE rbac_users SET name=?,last_name=?,"
			+ "first_name=?,email=?,password=?,blocked=?,description=?,super_user=?,creation_time=? WHERE id=?";

	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM rbac_users WHERE id=?";

	public String insert(User user) {
		user.setId(createId());
		if (update(SQL_INSERT_USER, new Object[] { user.getId(),
				user.getName(), user.getLastName(), user.getFirstName(),
				user.getEmail(), user.getPassword(), user.getBlocked(),
				user.getDescription(), user.getSuperUser(),
				user.getCreationTime() }, new int[] { Types.CHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.INTEGER,
				Types.TIMESTAMP }) > 0) {
			return user.getId();
		} else {
			return null;
		}
	}

	public String delete(String userId) {
		if (update(SQL_DELETE_USER, userId) > 0) {
			return userId;
		} else {
			return null;
		}
	}

	public int update(User user) {
		return update(SQL_UPDATE_USER, new Object[] { user.getName(),
				user.getLastName(), user.getFirstName(), user.getEmail(),
				user.getPassword(), user.getBlocked(), user.getDescription(),
				user.getSuperUser(), user.getCreationTime(), user.getId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
						Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP,
						Types.CHAR });
	}

	public User findById(String userId) {
		return (User) query(SQL_FIND_USER_BY_ID, userId,
				new UserSingleRowMapper());
	}
}
