/*
 * UserDAOFactory.java
 *
 * Created on 28 de marzo de 2005, 13:53
 */

package org.manentia.kasai.authobject;

import java.util.ResourceBundle;

import org.manentia.kasai.Constants;

/**
 *
 * @author rzuasti
 */
public class AuthObjectDAOFactory {
    
    private static AuthObjectDAOFactory instance;
    
    private AuthObjectDAO dao;
    
    /** Creates a new instance of UserDAOFactory */
    private AuthObjectDAOFactory() {
    	ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
    	
    	if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_MYSQL)){
    		dao = new JDBCMySQLAuthObjectDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_SQLSERVER)){
    		dao = new JDBCSQLServerAuthObjectDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_PGSQL)){
    		dao = new JDBCPGSQLAuthObjectDAO();
    	}
    }
 
    public static synchronized AuthObjectDAOFactory getInstance(){
        if (instance == null){
            instance = new AuthObjectDAOFactory();
        }
        
        return instance;
    }
    
    public AuthObjectDAO createDAO(){        
        return dao;
    }
}
