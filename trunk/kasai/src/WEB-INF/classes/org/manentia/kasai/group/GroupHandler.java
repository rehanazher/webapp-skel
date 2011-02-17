/*
 * UserHandler.java
 *
 * Created on June 22, 2004, 5:44 PM
 */

package org.manentia.kasai.group;

import java.util.Collection;
import java.util.List;

import org.manentia.kasai.Group;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.exceptions.InvalidAttributesException;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author  rzuasti
 */
public class GroupHandler {
    private static GroupHandler instance;
    
    private GroupHandler(){}
    
    public static synchronized GroupHandler getInstance(){
        if (instance == null){
            instance = new GroupHandler();
        }
        
        return instance;
    }
    
    public void addUserToGroup(String login, String group) throws DoesntExistsException,DataAccessException, XMLException{
        GroupDAOFactory.getInstance().createDAO().addUserToGroup(login, group);
    }
    
    public boolean checkUserBelongsToGroup(String user, String group) throws DataAccessException{
        return GroupDAOFactory.getInstance().createDAO().checkUserBelongsToGroup(user, group);
    }
    
    public void create(Group group) throws InvalidAttributesException, AlreadyExistsException, DataAccessException, XMLException{
        GroupDAOFactory.getInstance().createDAO().create(group);
    }
    
    public void delete(String group) throws DataAccessException{
        GroupDAOFactory.getInstance().createDAO().delete(group);
    }
    
    public void deleteUserFromGroup(String login, String group) throws DataAccessException{
        GroupDAOFactory.getInstance().createDAO().deleteUserFromGroup(login, group);
    }
    
    public List list(String idGroup, String description, int blocked, int system, String login) throws DataAccessException, XMLException {
        return GroupDAOFactory.getInstance().createDAO().list(idGroup, description, blocked, system, login);
    }
    
    public Collection listUsersNotInGroup(String group) throws DataAccessException, XMLException{
        return GroupDAOFactory.getInstance().createDAO().listUsersNotInGroup(group);
    }
    
    public Group read(String group) throws DataAccessException, XMLException{
        return GroupDAOFactory.getInstance().createDAO().read(group);
    }
    
    public void update(Group group) throws InvalidAttributesException, DataAccessException, XMLException{
        GroupDAOFactory.getInstance().createDAO().update(group);
    }

    public void update(Group group, String[] members) throws InvalidAttributesException, DataAccessException, XMLException{
        GroupDAOFactory.getInstance().createDAO().update(group, members);
    }
}
