/*
 * UserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:45
 */

package org.manentia.kasai.role;

import java.util.Collection;
import java.util.List;

import org.manentia.kasai.Role;
import org.manentia.kasai.exceptions.*;

/**
 *
 * @author rzuasti
 */
public interface RoleDAO {
    
   public void addOperativeToRole(
        final String idOperative,
        final int    role
    ) throws DoesntExistsException, DataAccessException;
    
    public int create(String name, String description, String[] operatives) throws InvalidAttributesException, AlreadyExistsException, DoesntExistsException,DataAccessException;
    
    public void deleteOperativeFromRole(String idOperative, int role) throws DataAccessException;
    
    public void delete(int id) throws DataAccessException;
    
    public Collection listOperativesFromRole(int role, String operative) throws DataAccessException;
    
    public Collection listOperativesNotInRole(int role) throws DataAccessException;
    
    public List list(String name, boolean exactly) throws DataAccessException;
    
    public Role read(int role) throws DataAccessException;
    
    public void update(int id, String name, String description) throws InvalidAttributesException,DataAccessException;
    
    public void update(int id, String name, String description, String[] operatives) throws InvalidAttributesException,DataAccessException;
}
