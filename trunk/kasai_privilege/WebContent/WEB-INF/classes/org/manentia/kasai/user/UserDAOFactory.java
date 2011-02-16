/*
 * UserDAOFactory.java
 *
 * Created on 28 de marzo de 2005, 13:53
 */

package org.manentia.kasai.user;

import java.util.ResourceBundle;

import org.manentia.kasai.Constants;

/**
 *
 * @author rzuasti
 */
public class UserDAOFactory {
    
    private static UserDAOFactory instance;
    
    private UserDAO dao;
    
    /** Creates a new instance of UserDAOFactory */
    private UserDAOFactory() {
    	ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
    	
    	if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_MYSQL)){
    		dao = new JDBCMySQLUserDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_SQLSERVER)){
    		dao = new JDBCSQLServerUserDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_PGSQL)){
    		dao = new JDBCPGSQLUserDAO();
    	}   	
    }
 
    public static synchronized UserDAOFactory getInstance(){
        if (instance == null){
            instance = new UserDAOFactory();
        }
        
        return instance;
    }
    
    public UserDAO createDAO(){        
        return dao;
    }
}
