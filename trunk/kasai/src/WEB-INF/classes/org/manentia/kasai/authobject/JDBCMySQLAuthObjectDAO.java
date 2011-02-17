/*
 * JDBCMySQLUserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:46
 */

package org.manentia.kasai.authobject;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.AuthObject;
import org.manentia.kasai.Constants;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;

import com.manentia.commons.log.Log;
import com.manentia.commons.persistence.DBUtil;

/**
 *
 * @author rzuasti
 */
public class JDBCMySQLAuthObjectDAO extends JDBCANSISQLAuthObjectDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCMySQLAuthObjectDAO() {
    }
    
    public void copyPermissionsFromObject (String sourceObject, String destinationObject) throws DoesntExistsException,DataAccessException{
            
        Connection con = null;
        String sql;
                
        try{
            if ((StringUtils.isNotEmpty(sourceObject)) && (StringUtils.isNotEmpty(destinationObject))){
                if (this.read(sourceObject) == null){
                	Log.write("Source object doesn't exist", Log.WARN, "copyPermissionsFromObject", JDBCMySQLAuthObjectDAO.class);
                    
                    throw new DoesntExistsException(AuthObject.class.getName() + ".objectDoesntExist");
                }

                if (this.read(destinationObject) == null){
                	Log.write("Destination object doesn't exist", Log.WARN, "copyPermissionsFromObject", JDBCMySQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(AuthObject.class.getName() + ".objectDoesntExist");
                }
                
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                
                sql = "REPLACE INTO kasai_objects_users_roles (id_object,id_user,id_role) SELECT '"+destinationObject+"',id_user,id_role FROM kasai_objects_users_roles WHERE id_object='" + sourceObject + "'";
                con.createStatement().executeUpdate(sql);
                
                sql = "REPLACE INTO kasai_objects_groups_roles (id_object,id_group,id_role) SELECT '"+destinationObject+"',id_group,id_role FROM kasai_objects_groups_roles WHERE id_object='" + sourceObject + "'";
                con.createStatement().executeUpdate(sql);
                
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "copyPermissionsFromObject", JDBCMySQLAuthObjectDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }        
    }
}
