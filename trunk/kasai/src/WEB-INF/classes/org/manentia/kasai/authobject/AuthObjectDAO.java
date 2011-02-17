/*
 * UserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:45
 */

package org.manentia.kasai.authobject;

import java.util.Collection;

import org.manentia.kasai.AuthObject;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public interface AuthObjectDAO {
    
    public void copyPermissionsFromObject (String sourceObject, String destinationObject) throws DoesntExistsException,DataAccessException;
    
    public void create(String object) throws DataAccessException;
    
    public void createObjectGroupRole(String object, String group, int role) throws DoesntExistsException,DataAccessException, XMLException;
    
    public void createObjectUserRole(String object, String login, int role) throws DoesntExistsException,DataAccessException, XMLException;
    
    public void delete(String object) throws DataAccessException;
    
    public void deleteObjectGroupRole(int id) throws DataAccessException;
    
    public void deleteObjectUserRole(int id) throws DataAccessException;
    
    public void deleteObjectUserRole(String login, String object, int role) throws DataAccessException;
    
    public void deleteObjectUserRole(String login, String object) throws DataAccessException;
    
    public Collection listObjectGroupsRoles(String object) throws DataAccessException;
    
    public Collection listObjectUsersRoles(String object) throws DataAccessException;
    
    public AuthObject read(String id) throws DataAccessException;
}
