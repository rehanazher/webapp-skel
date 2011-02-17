/*
 * UserHandler.java
 *
 * Created on June 22, 2004, 5:44 PM
 */

package org.manentia.kasai.role;

import java.util.Collection;
import java.util.List;

import org.manentia.kasai.Role;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.exceptions.InvalidAttributesException;

/**
 *
 * @author  rzuasti
 */
public class RoleHandler {
    private static RoleHandler instance;
    
    private RoleHandler(){}
    
    public static synchronized RoleHandler getInstance(){
        if (instance == null){
            instance = new RoleHandler();
        }
        
        return instance;
    }
    
    public void addOperativeToRole(
        final String idOperative,
        final int    role
    ) throws DoesntExistsException, DataAccessException {
    
        RoleDAOFactory.getInstance().createDAO().addOperativeToRole(idOperative, role);
    }
    
    public int create(String name, String description, String[] operatives) throws InvalidAttributesException, AlreadyExistsException, DoesntExistsException,DataAccessException{
        return RoleDAOFactory.getInstance().createDAO().create(name, description, operatives);
    }
    
    public void deleteOperativeFromRole(String idOperative, int role) throws DataAccessException {
        RoleDAOFactory.getInstance().createDAO().deleteOperativeFromRole(idOperative, role);
    }
    
    public void delete(int id) throws DataAccessException {
        RoleDAOFactory.getInstance().createDAO().delete(id);
    }
    
    public Collection listOperativesFromRole(int role, String operative) throws DataAccessException {
        return RoleDAOFactory.getInstance().createDAO().listOperativesFromRole(role, operative);
    }
    
    public Collection listOperativesNotInRole(int role) throws DataAccessException {
        return RoleDAOFactory.getInstance().createDAO().listOperativesNotInRole(role);
    }
    
    public List list(String name, boolean exactly) throws DataAccessException {
        return RoleDAOFactory.getInstance().createDAO().list(name, exactly);
    }
    
    public Role read(int role) throws DataAccessException {
        return RoleDAOFactory.getInstance().createDAO().read(role);
    }
    
    public void update(int id, String name, String description) throws InvalidAttributesException,DataAccessException {
        RoleDAOFactory.getInstance().createDAO().update(id, name, description);
    }
    
    public void update(int id, String name, String description, String[] operatives) throws InvalidAttributesException,DataAccessException {
        RoleDAOFactory.getInstance().createDAO().update(id, name, description, operatives);
    }
}
