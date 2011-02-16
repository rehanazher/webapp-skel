package org.manentia.kasai.services;

import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.ServiceException;

/**
 *
 * @author  rzuasti
 */
public interface AuthService { 
    
    public static final int AUTH_OK = 0;
    public static final int AUTH_BAD_USERNAME = 1;
    public static final int AUTH_BAD_PASSWORD = 2;
    
    public int checkPassword(String userName, String password) throws ServiceException;
    
    public void changePassword(String userName, String oldPassword, String newPassword)
        throws ServiceException, InvalidPasswordException;
    
    public String resetPassword(String userName)
        throws ServiceException;
    
    public void setPassword(String userName, String password)
        throws ServiceException, InvalidPasswordException;
}
