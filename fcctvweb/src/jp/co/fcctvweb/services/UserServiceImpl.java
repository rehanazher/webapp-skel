package jp.co.fcctvweb.services;

import jp.co.fcctvweb.daos.UserDao;
import jp.co.fcctvweb.po.User;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public User userLogin(String username, String password, String remoteIp) {
		User user = userDao.findByRemoteIp(remoteIp);
		if (user != null){
			
		}
		
		return userDao.findByUsernameAndPassword(username, password);
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
