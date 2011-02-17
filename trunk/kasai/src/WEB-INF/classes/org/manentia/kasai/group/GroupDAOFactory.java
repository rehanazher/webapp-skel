/*
 * UserDAOFactory.java
 *
 * Created on 28 de marzo de 2005, 13:53
 */

package org.manentia.kasai.group;

import java.util.ResourceBundle;

import org.manentia.kasai.Constants;

/**
 *
 * @author rzuasti
 */
public class GroupDAOFactory {
    
    private static GroupDAOFactory instance;
    
    private GroupDAO dao;
    
    /** Creates a new instance of UserDAOFactory */
    private GroupDAOFactory() {
    	ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
    	
    	if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_MYSQL)){
    		dao = new JDBCMySQLGroupDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_SQLSERVER)){
    		dao = new JDBCSQLServerGroupDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_PGSQL)){
    		dao = new JDBCPGSQLGroupDAO();
    	}	
    }
 
    public static synchronized GroupDAOFactory getInstance(){
        if (instance == null){
            instance = new GroupDAOFactory();
        }
        
        return instance;
    }
    
    public GroupDAO createDAO(){        
        return dao;
    }
}
