package org.manentia.kasai.services;

import org.manentia.kasai.exceptions.ServiceNotAvailableException;

import com.manentia.commons.log.Log;

/**
 *
 * @author  rzuasti
 */
public class AuthServiceFactory {
    
    public static AuthService getAuthService(String serviceClass) throws ServiceNotAvailableException {
        AuthService service = null;
        
        try {
            service = (AuthService) Class.forName(serviceClass).newInstance();
        }
        catch (ClassNotFoundException e){
        	Log.write("Class not found", e, Log.ERROR, "getAuthService", AuthServiceFactory.class);
        	
            throw new ServiceNotAvailableException(AuthServiceFactory.class.getName() + ".classNotFound", e);
        }
        catch (InstantiationException e){
        	Log.write("Error initializing service, it doesn't have a correct constructor", e, Log.ERROR, "getAuthService", AuthServiceFactory.class);
        	
            throw new ServiceNotAvailableException(AuthServiceFactory.class.getName() + ".instantiationError", e);            
        }
        catch (IllegalAccessException e){
        	Log.write("Error initializing service, it doesn't have a public constructor", e, Log.ERROR, "getAuthService", AuthServiceFactory.class);
         
            throw new ServiceNotAvailableException(AuthServiceFactory.class.getName() + ".instantiationError", e);
        }
     
        return service;
    }
    
}
