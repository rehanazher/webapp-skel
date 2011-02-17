/*
 * UserHandler.java
 *
 * Created on June 22, 2004, 5:44 PM
 */

package org.manentia.kasai.authobject;

import java.util.Collection;

import org.manentia.kasai.AuthObject;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author  rzuasti
 */
public class AuthObjectHandler {
    private static AuthObjectHandler instance;
    
    private AuthObjectHandler(){}
    
    public static synchronized AuthObjectHandler getInstance(){
        if (instance == null){
            instance = new AuthObjectHandler();
        }
        
        return instance;
    }
    
    public void copyPermissionsFromObject (String sourceObject, String destinationObject) throws DoesntExistsException,DataAccessException {
        AuthObjectDAOFactory.getInstance().createDAO().copyPermissionsFromObject(sourceObject, destinationObject);
    }
    
    public void create(String object) throws DataAccessException{
        AuthObjectDAOFactory.getInstance().createDAO().create(object);
    }
    
    public void createObjectGroupRole(String object, String group, int role) throws DoesntExistsException,DataAccessException, XMLException{
        AuthObjectDAOFactory.getInstance().createDAO().createObjectGroupRole(object, group, role);
    }
    
    public void createObjectUserRole(String object, String login, int role) throws DoesntExistsException,DataAccessException, XMLException{
        AuthObjectDAOFactory.getInstance().createDAO().createObjectUserRole(object, login, role);
    }
    
    public void delete(String object) throws DataAccessException{
        AuthObjectDAOFactory.getInstance().createDAO().delete(object);
    }
    
    public void deleteObjectGroupRole(int id) throws DataAccessException{
        AuthObjectDAOFactory.getInstance().createDAO().deleteObjectGroupRole(id);
    }
    
    public void deleteObjectUserRole(int id) throws DataAccessException{
        AuthObjectDAOFactory.getInstance().createDAO().deleteObjectUserRole(id);
    }
    
    public void deleteObjectUserRole(String login, String object, int role) throws DataAccessException{
        AuthObjectDAOFactory.getInstance().createDAO().deleteObjectUserRole(login, object, role);
    }
    
    public void deleteObjectUserRole(String login, String object) throws DataAccessException{
        AuthObjectDAOFactory.getInstance().createDAO().deleteObjectUserRole(login, object);
    }
    
    public Collection listObjectGroupsRoles(String object) throws DataAccessException {
        return AuthObjectDAOFactory.getInstance().createDAO().listObjectGroupsRoles(object);
    }
    
    public Collection listObjectUsersRoles(String object) throws DataAccessException{
        return AuthObjectDAOFactory.getInstance().createDAO().listObjectUsersRoles(object);
    }
    
    public AuthObject read(String id) throws DataAccessException{
        return AuthObjectDAOFactory.getInstance().createDAO().read(id);
    }
}
