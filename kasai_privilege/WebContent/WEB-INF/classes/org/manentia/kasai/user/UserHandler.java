/*
 * UserHandler.java
 *
 * Created on June 22, 2004, 5:44 PM
 */

package org.manentia.kasai.user;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.KasaiFacade;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.InvalidAttributesException;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.NotFoundException;
import org.manentia.kasai.exceptions.ServiceException;
import org.manentia.kasai.exceptions.ServiceNotAvailableException;
import org.manentia.kasai.exceptions.UserBlockedException;

import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLException;

/**
 *
 * @author  rzuasti
 */
public class UserHandler {
    private static UserHandler instance;
    
    private UserHandler(){}
    
    public static synchronized UserHandler getInstance(){
        if (instance == null){
            instance = new UserHandler();
        }
        
        return instance;
    }
    
    public void checkPassword(String login, String password) throws DataAccessException, NotFoundException, 
            UserBlockedException, ServiceNotAvailableException, ServiceException, InvalidPasswordException, XMLException {
                
        User user = null;
        int result = User.AUTH_BAD_USERNAME;
        
        Log.write("Enter (login=" + StringUtils.defaultString(login, "<null>") + 
                ", password=" + ((password==null) ? "<null>" : "******" ) + ")", 
                Log.INFO, "checkPassword", UserHandler.class);
                
            user = this.read(login, true);
            
            if (user == null) {
            	Log.write("User doesn't exist", Log.WARN, "checkPassword", UserHandler.class);
            	
                throw new NotFoundException(KasaiFacade.class.getName() + ".userPasswordInvalid");
            }

            if (user.getBlocked()) {
            	Log.write("User " + login + " is blocked", Log.WARN, "checkPassword", UserHandler.class);
            	
                throw new UserBlockedException(KasaiFacade.class.getName() + ".userLocked");
            }

            result = user.checkPassword(password);


            if (result != User.AUTH_OK) {
            	Log.write("Wrong password", Log.WARN, "checkPassword", UserHandler.class);
            	
                throw new InvalidPasswordException(KasaiFacade.class.getName() + ".userPasswordInvalid");
            }
            
            Log.write("Exit", Log.INFO, "checkPassword", UserHandler.class);
    }
    
    public boolean checkOperative(String login, String operative, String object) {
        return UserDAOFactory.getInstance().createDAO().checkOperative(login, operative, object);
    }
    
    public void create(User user, String password) throws InvalidAttributesException, AlreadyExistsException, DataAccessException, ServiceNotAvailableException, ServiceException, InvalidPasswordException, XMLException {
    
        UserDAOFactory.getInstance().createDAO().create(user);
        
        user.overridePassword(password);
    }
    
    public void create(User user) throws InvalidAttributesException, AlreadyExistsException, DataAccessException, ServiceNotAvailableException, ServiceException, XMLException {
    
        UserDAOFactory.getInstance().createDAO().create(user);
        
        // Assign the user a new password
        user.resetPassword();
    }
    
    public void delete(String login) throws DataAccessException {
        UserDAOFactory.getInstance().createDAO().delete(login);
    }
    
    public List list(String login, String firstName, String lastName,
        String email, int blocked, String description, String group) throws DataAccessException, XMLException {
    
        return UserDAOFactory.getInstance().createDAO().list(login, firstName, lastName, email, blocked, description, group);
    }
    
    public String[] listUsernames() throws DataAccessException{
    	return UserDAOFactory.getInstance().createDAO().listUsernames();
    }
    
    public String[] listUsernames(String groupId) throws DataAccessException{
    	return UserDAOFactory.getInstance().createDAO().listUsernames(groupId);
    }
    
    public User read(String login, boolean cache) throws DataAccessException, XMLException {
        return UserDAOFactory.getInstance().createDAO().read(login, cache);
    }
    
    public void update(User user) throws InvalidAttributesException,DataAccessException, XMLException {
    
        UserDAOFactory.getInstance().createDAO().update(user);
    }
    
}
