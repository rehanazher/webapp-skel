package org.manentia.kasai.services;

import gs.scribblin.sysauth.SysAuth;

import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.ServiceException;

/**
 *
 * @author  rzuasti
 */
public class UnixAuthService implements AuthService {
    
    public int checkPassword(String userName, String password) throws ServiceException {
        int result = AUTH_BAD_USERNAME;                

        if (SysAuth.isAllowed(userName, password)){
          result = AUTH_OK;  
        }                       
        
        return result;
    }
    
    public void changePassword(String userName, String oldPassword, String newPassword) throws ServiceException {
    
    }
    
    public void setPassword(String userName, String password)
        throws ServiceException, InvalidPasswordException{
    
        // NOT SUPPORTED YET
    }
    
    public String resetPassword(String userName) throws ServiceException {
        return null;
    }
    
    public native boolean validUser( String strUser, String strPass );
}
