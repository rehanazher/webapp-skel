/*
 * JDBCMySQLUserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:46
 */

package org.manentia.kasai.role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.Constants;
import org.manentia.kasai.Operative;
import org.manentia.kasai.Role;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.exceptions.InvalidAttributesException;
import org.manentia.kasai.operative.OperativeHandler;

import com.manentia.commons.log.Log;
import com.manentia.commons.persistence.DBUtil;

/**
 *
 * @author rzuasti
 */
public class JDBCSQLServerRoleDAO extends JDBCANSISQLRoleDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCSQLServerRoleDAO() {
    }
    
    public int create(String name, String description, String[] operatives) throws InvalidAttributesException, AlreadyExistsException, DoesntExistsException,DataAccessException{
        
    	Log.write("Creating role '"+ name +"'", Log.DEBUG, "create", JDBCSQLServerRoleDAO.class);
    	
        Connection con = null;
        String sql;
        Role r = null;
        ResultSet rs = null;
        int idRole = -1;
        try{
            r = new Role();
            r.setDescription(description);
            r.setName(name);
            r.validate();
            
            if (this.list(name, true).size() > 0){
            	Log.write("Role name already exist", Log.WARN, "create", JDBCSQLServerRoleDAO.class);
            	
                throw new AlreadyExistsException(this.getClass().getName() + ".roleAlreadyExist");
            }
            sql = "INSERT INTO kasai_roles (name, description) VALUES (?, ?)";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, name);
            stm.setString(2, description);
            stm.executeUpdate();            
            stm.close();
            
            Log.write("Role '"+ name +"' created, about to insert operatives", Log.DEBUG, "create", JDBCSQLServerRoleDAO.class);
            
            stm = con.prepareStatement("SELECT @@IDENTITY");
            rs = stm.executeQuery();
            
            if (rs.next()){
                idRole = rs.getInt(1);
                
                Log.write("Role '"+ name +"' created with id=" + idRole, Log.DEBUG, "create", JDBCSQLServerRoleDAO.class);
            } else {
            	Log.write("Cannot retrieve generated role ID", Log.ERROR, "create", JDBCSQLServerRoleDAO.class);
            	
                throw new DataAccessException("Cannot retrieve generated role ID");
            }
            
            String idOperative = null;
            if (operatives != null){
                for (int i=0; i<operatives.length; i++) {
                    idOperative = operatives[i];
                    
                    if (StringUtils.isEmpty(idOperative) || (OperativeHandler.getInstance().list(idOperative).size() == 0)) {
                    	Log.write("Operative doesn't exist", Log.WARN, "create", JDBCSQLServerRoleDAO.class);
                    	
                        throw new DoesntExistsException(Operative.class.getName() + ".operativeDoesntExist");
                    }

                    sql = "INSERT INTO kasai_roles_operatives (id_role, id_operative) VALUES (" + idRole + ",'" + org.apache.commons.lang.StringEscapeUtils.escapeSql(idOperative) + "')";
                    con.createStatement().executeUpdate(sql);
                }
            }
            con.commit();
        }catch (DataAccessException sqlE){
            try{
                con.rollback();
            }catch (SQLException e){}
            throw sqlE;
        }catch (DoesntExistsException deE){
            try{
                con.rollback();
            }catch (SQLException e){}
            throw deE;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "create", JDBCSQLServerRoleDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                
                con.setAutoCommit(true);
                con.close();
            }catch(Exception e){}
        }        
        
        return idRole;
    }
}
