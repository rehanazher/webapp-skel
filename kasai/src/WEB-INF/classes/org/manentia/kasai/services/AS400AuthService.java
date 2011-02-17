package org.manentia.kasai.services;

import java.io.IOException;
import java.util.ResourceBundle;

import org.manentia.kasai.Constants;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.ServiceException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.manentia.commons.log.Log;
/*
 *
 */




/**
 *
 * @author  JK Adams
 */
public class AS400AuthService implements AuthService {
    
    public int checkPassword(String userName, String password) throws ServiceException {
        
        int result = AUTH_BAD_USERNAME;
        
        ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);
        try {
            String ip = res.getString("kasai.as400.IPAddress");
            AS400 system = new AS400(ip, userName, password);
            if (system.validateSignon()){
                result = AUTH_OK;
            }
        }
        catch (AS400SecurityException e){
        	Log.write("Error contacting the AS/400 server", e, Log.ERROR, "checkPassword", AS400AuthService.class);
        	
            throw new ServiceException(AS400AuthService.class.getName() + ".securityException", e);
        }
        catch (IOException e){
        	Log.write("Error contacting the AS/400 server", e, Log.ERROR, "checkPassword", AS400AuthService.class);
        	
            throw new ServiceException(AS400AuthService.class.getName() + ".IOException", e);
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
