/*
 * UserHandler.java
 *
 * Created on June 22, 2004, 5:44 PM
 */

package org.manentia.kasai.operative;

import java.util.Collection;

import org.manentia.kasai.exceptions.DataAccessException;

import com.manentia.commons.xml.XMLException;

/**
 *
 * @author  rzuasti
 */
public class OperativeHandler {
    private static OperativeHandler instance;
    
    private OperativeHandler(){}
    
    public static synchronized OperativeHandler getInstance(){
        if (instance == null){
            instance = new OperativeHandler();
        }
        
        return instance;
    }
    
    public Collection listGroupsOperative(String operative, String object) throws DataAccessException, XMLException {
        return OperativeDAOFactory.getInstance().createDAO().listGroupsOperative(operative, object);
    }
    
    public Collection list(String idOperative) throws DataAccessException{
        return OperativeDAOFactory.getInstance().createDAO().list(idOperative);
    }
    
    public Collection listUsersOperative(String operative, String object) throws DataAccessException, XMLException {
        return OperativeDAOFactory.getInstance().createDAO().listUsersOperative(operative, object);
    }
}
