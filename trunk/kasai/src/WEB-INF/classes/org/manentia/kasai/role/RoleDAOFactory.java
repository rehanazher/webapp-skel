/*
 * UserDAOFactory.java
 *
 * Created on 28 de marzo de 2005, 13:53
 */

package org.manentia.kasai.role;

import java.util.ResourceBundle;

import org.manentia.kasai.Constants;

/**
 *
 * @author rzuasti
 */
public class RoleDAOFactory {
    
    private static RoleDAOFactory instance;
    
    private RoleDAO dao;
    
    /** Creates a new instance of UserDAOFactory */
    private RoleDAOFactory() {
    	ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
    	
    	if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_MYSQL)){
    		dao = new JDBCMySQLRoleDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_SQLSERVER)){
    		dao = new JDBCSQLServerRoleDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_PGSQL)){
    		dao = new JDBCPGSQLRoleDAO();
    	}         	
    }
 
    public static synchronized RoleDAOFactory getInstance(){
        if (instance == null){
            instance = new RoleDAOFactory();
        }
        
        return instance;
    }
    
    public RoleDAO createDAO(){        
        return dao;
    }
}
