/*
 * UserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:45
 */

package org.manentia.kasai.operative;

import java.util.Collection;

import org.manentia.kasai.exceptions.DataAccessException;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public interface OperativeDAO {
    public Collection listGroupsOperative(String operative, String object) throws DataAccessException, XMLException;
    
    public Collection list(String idOperative) throws DataAccessException;
    
    public Collection listUsersOperative(String operative, String object) throws DataAccessException, XMLException;
}
