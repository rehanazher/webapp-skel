/*
 * UserDAOFactory.java
 *
 * Created on 28 de marzo de 2005, 13:53
 */

package org.manentia.kasai.operative;

import java.util.ResourceBundle;

import org.manentia.kasai.Constants;

/**
 *
 * @author rzuasti
 */
public class OperativeDAOFactory {
    
    private static OperativeDAOFactory instance;
    
    private OperativeDAO dao;
    
    /** Creates a new instance of UserDAOFactory */
    private OperativeDAOFactory() {
    	ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
    	
    	if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_MYSQL)){
    		dao = new JDBCMySQLOperativeDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_SQLSERVER)){
    		dao = new JDBCSQLServerOperativeDAO();
    	} else if (res.getString("db.kasai.engine").equalsIgnoreCase(Constants.DATABASE_PGSQL)){
    		dao = new JDBCPGSQLOperativeDAO();
    	}       	
    }
 
    public static synchronized OperativeDAOFactory getInstance(){
        if (instance == null){
            instance = new OperativeDAOFactory();
        }
        
        return instance;
    }
    
    public OperativeDAO createDAO(){        
        return dao;
    }
}
