package jp.co.fcctvweb.services;

import jp.co.fcctvweb.po.User;

public interface UserService {

	/**
	 * 
	 * @param username
	 * @param password
	 * @param remoteIp
	 * @return user for success, null for failure
	 */
	User userLogin(String username, String password, String remoteIp);

	boolean logout(String username);
}
