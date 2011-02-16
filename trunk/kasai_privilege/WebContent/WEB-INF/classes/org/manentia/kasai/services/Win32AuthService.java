package org.manentia.kasai.services;

import java.net.UnknownHostException;
import java.util.ResourceBundle;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

import org.manentia.kasai.Constants;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.ServiceException;

import com.manentia.commons.log.Log;
/*
 *
 */




/**
 *
 * @author  JK Adams
 */
public class Win32AuthService implements AuthService {
    public int checkPassword(String userName, String password) throws ServiceException {
        
        int result = AUTH_BAD_USERNAME;
        
        ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);
        try {
            NtlmPasswordAuthentication npa = new NtlmPasswordAuthentication(res.getString("kasai.win32.domain"), userName, password);
            SmbSession.logon(UniAddress.getByName(res.getString("kasai.win32.domainController")), npa);
            result = AUTH_OK;
        }
        catch (UnknownHostException uhe){
        	Log.write("The domain controller could not be reached", uhe, Log.ERROR, "checkPassword", Win32AuthService.class);
        	
            throw new ServiceException(Win32AuthService.class.getName() + ".unknownhost", uhe);
        }
        catch (SmbException se){
        	Log.write("Invalid username or password in the domain", se, Log.INFO, "checkPassword", Win32AuthService.class);        	
        }
        return result;
    }
    
    public void changePassword(String userName, String oldPassword, String newPassword) throws ServiceException {
        // NOT SUPPORTED YET
    }
    
    public void setPassword(String userName, String password)
        throws ServiceException, InvalidPasswordException{
    
        // NOT SUPPORTED YET
    }
    
    public String resetPassword(String userName) throws ServiceException {
        return null;
    }        

}
