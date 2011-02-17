/*
 * UserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:45
 */

package org.manentia.kasai.user;

import java.util.List;

import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.InvalidAttributesException;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public interface UserDAO {
    
    public boolean checkOperative(String login, String operative, String object);
    
    public void create(User user) throws InvalidAttributesException, AlreadyExistsException, DataAccessException, XMLException;
        
    public void delete(String login) throws DataAccessException;
    
    public List list(String login, String firstName, String lastName,
        String email, int blocked, String description, String group) throws DataAccessException, XMLException;
    
    public String[] listUsernames() throws DataAccessException;
    
    public String[] listUsernames(String groupId) throws DataAccessException;
    
    public User read(String login, boolean cache) throws DataAccessException, XMLException;
    
    public void update(User user) throws InvalidAttributesException,DataAccessException,XMLException;
    
}
