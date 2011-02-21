/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.aaut.skeleton.commons.util.dao.BasicDaoSupport;
import com.aaut.skeleton.commons.util.dao.MultiRowMapper;
import com.aaut.skeleton.commons.util.dao.SingleRowMapper;
import com.aaut.skeleton.rbac.po.User;

public class UserDaoImpl extends BasicDaoSupport<User> implements UserDao<User> {

	private static class UserMultiRowMapper implements MultiRowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();

			return user;
		}
	}

	private static class UserSingleRowMapper implements SingleRowMapper<User> {

		@Override
		public User mapRow(ResultSet rs) throws SQLException {
			return new UserMultiRowMapper().mapRow(rs, 1);
		}
	}
	

	@Override
	public String add(User ele) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(User ele) {
		// TODO Auto-generated method stub
		return 0;
	}

}
