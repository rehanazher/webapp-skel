/**
 * Added by James
 * on 2011-2-15
 */
package com.aaut.skeleton.rbac.dao;

import java.util.List;

import com.aaut.skeleton.rbac.exception.AlreadyExistsException;
import com.aaut.skeleton.rbac.exception.DataAccessException;
import com.aaut.skeleton.rbac.exception.InvalidAttributesException;
import com.aaut.skeleton.rbac.po.User;

public interface UserDao {
	public interface UserDAO {

		public boolean checkOperative(String login, String operative,
				String object);

		public void create(User user) throws InvalidAttributesException,
				AlreadyExistsException, DataAccessException;

		public void delete(String login) throws DataAccessException;

		public List<User> list(String login, String firstName, String lastName,
				String email, int blocked, String description, String group)
				throws DataAccessException;

		public String[] listUsernames() throws DataAccessException;

		public String[] listUsernames(String groupId)
				throws DataAccessException;

		public User read(String login, boolean cache)
				throws DataAccessException;

		public void update(User user) throws InvalidAttributesException,
				DataAccessException;

	}
}
