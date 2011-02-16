package com.aaut.skeleton.rbac.service;

import com.aaut.skeleton.rbac.exception.InvalidPasswordException;
import com.aaut.skeleton.rbac.exception.ServiceException;

/**
 * 
 * @author James
 */
public interface AuthService {

	public static final int AUTH_OK = 0;
	public static final int AUTH_BAD_USERNAME = 1;
	public static final int AUTH_BAD_PASSWORD = 2;

	public int checkPassword(String userName, String password)
			throws ServiceException;

	public void changePassword(String userName, String oldPassword,
			String newPassword) throws ServiceException,
			InvalidPasswordException;

	public String resetPassword(String userName) throws ServiceException;

	public void setPassword(String userName, String password)
			throws ServiceException, InvalidPasswordException;
}
