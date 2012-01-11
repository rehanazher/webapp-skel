package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import jp.co.fcctvweb.po.User;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.utils.dao.SingleRowMapper;

public class UserDaoImpl extends BasicDao<User> implements UserDao {
	private static class UserMultiRowMapper implements MultiRowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setEncryped(rs.getString("encryped"));
			user.setRemoteIp(rs.getString("remote_ip"));
			user.setLastLogin(rs.getTimestamp("last_login"));
			return user;
		}
	}

	private static class UserSingleRowMapper implements SingleRowMapper<User> {
		public User mapRow(ResultSet rs) throws SQLException {
			return new UserMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_FIND_ALL = "SELECT * FROM user_tbl";
	private static final String SQL_INSERT_USER = "INSERT INTO user_tbl(id,username,password,"
			+ "encryped,remote_ip) " + "VALUES(?,?,?,?,?)";
	private static final String SQL_DELETE_USER = "DELETE FROM user_tbl WHERE id=?";
	private static final String SQL_UPDATE_USER = "UPDATE user_tbl SET username=?,password=?,"
			+ "encryped=?,remote_ip=? WHERE id=?";
	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM user_tbl WHERE id=?";
	private static final String SQL_FIND_BY_REMOTE_ID = "SELECT * FROM user_tbl WHERE remote_ip=?";
	private static final String SQL_FIND_BY_USERNAME_AND_PWD = "SELECT * FROM user_tbl WHERE username=? AND password=?";
	private static final String SQL_UPDATE_LAST_LOGIN_BY_ID = "UPDATE user_tbl SET last_login=? WHERE id=?";
	private static final String SQL_UPDATE_REMOTE_IP_BY_ID = "UPDATE user_tbl SET remote_ip=? WHERE id=?";
	private static final String SQL_UPDATE_LAST_LOGIN_BY_USERNAME = "UPDATE user_tbl SET last_login=? WHERE username=?";

	public List<User> findAll() {
		return query(SQL_FIND_ALL, new UserMultiRowMapper());
	}

	public String insert(User user) {
		user.setId(createId());
		if (update(
				SQL_INSERT_USER,
				new Object[] { user.getId(), user.getUsername(),
						user.getPassword(), user.getEncryped(),
						user.getRemoteIp() }, new int[] { Types.CHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR }) > 0) {
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

	public String update(User user) {
		if (update(
				SQL_UPDATE_USER,
				new Object[] { user.getUsername(), user.getPassword(),
						user.getEncryped(), user.getRemoteIp(), user.getId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.CHAR }) > 0) {
			return user.getId();
		} else {
			return null;
		}
	}

	public User findById(String userId) {
		return query(SQL_FIND_USER_BY_ID, userId, new UserSingleRowMapper());
	}

	public User findByRemoteIp(String remoteIp) {
		return query(SQL_FIND_BY_REMOTE_ID, remoteIp, new UserSingleRowMapper());
	}

	public User findByUsernameAndPassword(String username, String password) {
		return query(SQL_FIND_BY_USERNAME_AND_PWD, new Object[] { username,
				password }, new UserSingleRowMapper());
	}

	public boolean updateLastLoginById(Date loginTime, String userId) {
		return update(SQL_UPDATE_LAST_LOGIN_BY_ID, new Object[] { loginTime,
				userId }, new int[] { Types.TIMESTAMP, Types.CHAR }) > 0;
	}

	@Override
	public boolean updateRemoteIpById(String ip, String userId) {
		return update(SQL_UPDATE_REMOTE_IP_BY_ID, new Object[] { ip, userId },
				new int[] { Types.VARCHAR, Types.CHAR }) > 0;
	}

	@Override
	public boolean updateLastLoginByUsername(String username, Date loginTime) {
		return update(SQL_UPDATE_LAST_LOGIN_BY_USERNAME, new Object[] {
				loginTime, username }, new int[] { Types.TIMESTAMP,
				Types.VARCHAR }) > 0;
	}
}
