package org.manentia.kasai.user.passwordvalidators;

import org.manentia.kasai.exceptions.ServiceNotAvailableException;

import com.manentia.commons.log.Log;

/**
 *
 * @author  rzuasti
 */
public class PasswordValidatorFactory {
    
    public static PasswordValidator getPasswordValidator(String validatorClass) throws ServiceNotAvailableException {
    	PasswordValidator validator = null;
        
        try {
        	validator = (PasswordValidator) Class.forName(validatorClass).newInstance();
        }
        catch (ClassNotFoundException e){
        	Log.write("Class not found", e, Log.ERROR, "getPasswordValidator", PasswordValidatorFactory.class);
        	
            throw new ServiceNotAvailableException(PasswordValidatorFactory.class.getName() + ".classNotFound", e);
        }
        catch (InstantiationException e){
        	Log.write("Error initializing validator, it doesn't have a correct constructor", e, Log.ERROR, "getPasswordValidator", PasswordValidatorFactory.class);
        	
            throw new ServiceNotAvailableException(PasswordValidatorFactory.class.getName() + ".instantiationError", e);            
        }
        catch (IllegalAccessException e){
        	Log.write("Error initializing validator, it doesn't have a public constructor", e, Log.ERROR, "getPasswordValidator", PasswordValidatorFactory.class);
         
            throw new ServiceNotAvailableException(PasswordValidatorFactory.class.getName() + ".instantiationError", e);
        }
     
        return validator;
    }
    
}
