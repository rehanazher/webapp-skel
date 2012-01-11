package jp.co.fcctvweb.services;

import java.util.Calendar;
import java.util.Date;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.daos.UserDao;
import jp.co.fcctvweb.po.User;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public User userLogin(String username, String password, String remoteIp) {
		User user = userDao.findByRemoteIp(remoteIp);
		if (user != null) {
			String userCookieTime = Config.getUserCookieTime();
			Date lastLogin = user.getLastLogin();
			Calendar c = Calendar.getInstance();
			if (lastLogin == null) {
				c.add(Calendar.YEAR, -10);
			} else {
				c.setTime(lastLogin);
			}
			c.add(Calendar.HOUR_OF_DAY, Integer.parseInt(userCookieTime));
			if (Calendar.getInstance().after(c)) {
				user = null;
			}
		}

		if (user == null) {
			user = userDao.findByUsernameAndPassword(username, password);
		}

		// login success
		if (user != null) {
			userDao.updateLastLoginById(new Date(), user.getId());
			userDao.updateRemoteIpById(remoteIp, user.getId());
		}
		return user;
	}

	@Override
	public boolean logout(String username) {
		return userDao.updateLastLoginByUsername(username, null);
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
