/*
 * UserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:45
 */

package org.manentia.kasai.group;

import java.util.Collection;
import java.util.List;

import org.manentia.kasai.Group;
import org.manentia.kasai.exceptions.*;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public interface GroupDAO {
       public void addUserToGroup(String login, String group) throws DoesntExistsException,DataAccessException, XMLException;
    
    public boolean checkUserBelongsToGroup(String user, String group) throws DataAccessException;
    
    public void create(Group group) throws InvalidAttributesException, AlreadyExistsException, DataAccessException, XMLException;
    
    public void delete(String group) throws DataAccessException;
    
    public void deleteUserFromGroup(String login, String group) throws DataAccessException;
    
    public List list(String idGroup, String description, int blocked, int system, String login) throws DataAccessException, XMLException;
    
    public Collection listUsersNotInGroup(String group) throws DataAccessException, XMLException;
    
    public Group read(String group) throws DataAccessException, XMLException;
    
    public void update(Group group) throws InvalidAttributesException, DataAccessException, XMLException;

    public void update(Group group, String[] members
    ) throws InvalidAttributesException, DataAccessException, XMLException;
}
