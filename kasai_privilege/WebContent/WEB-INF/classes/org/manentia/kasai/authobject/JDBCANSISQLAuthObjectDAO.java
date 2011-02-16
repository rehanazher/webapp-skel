/*
 * JDBCMySQLUserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:46
 */

package org.manentia.kasai.authobject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.AuthObject;
import org.manentia.kasai.Constants;
import org.manentia.kasai.Group;
import org.manentia.kasai.ObjectGroupRole;
import org.manentia.kasai.ObjectUserRole;
import org.manentia.kasai.Role;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.group.GroupHandler;
import org.manentia.kasai.role.RoleHandler;
import org.manentia.kasai.user.UserHandler;

import com.manentia.commons.log.Log;
import com.manentia.commons.persistence.DBUtil;
import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public class JDBCANSISQLAuthObjectDAO implements AuthObjectDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCANSISQLAuthObjectDAO() {
    }
    
    public void copyPermissionsFromObject (String sourceObject, String destinationObject) throws DoesntExistsException,DataAccessException{
            
        Connection con = null;
        String sql;
                
        try{
            if ((StringUtils.isNotEmpty(sourceObject)) && (StringUtils.isNotEmpty(destinationObject))){
                if (this.read(sourceObject) == null){
                	Log.write("Source object doesn't exist", Log.WARN, "copyPermissionsFromObject", JDBCANSISQLAuthObjectDAO.class);
                    
                    throw new DoesntExistsException(AuthObject.class.getName() + ".objectDoesntExist");
                }

                if (this.read(destinationObject) == null){
                	Log.write("Destination object doesn't exist", Log.WARN, "copyPermissionsFromObject", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(AuthObject.class.getName() + ".objectDoesntExist");
                }
                
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                
                sql = "INSERT INTO kasai_objects_users_roles KOUR1 (id_object,id_user,id_role) SELECT '"
                		+destinationObject+"',KOUR2.id_user,KOUR2.id_role FROM kasai_objects_users_roles KOUR2 " +
                		"WHERE id_object='" + sourceObject + "' AND NOT EXISTS ( " +
                			" SELECT * FROM kasai_objects_users_roles KOUR3 " +
                			" WHERE KOUR3.id_object='"+destinationObject+"' AND KOUR3.id_user=KOUR2.id_user AND KOUR3.id_role=KOUR2.id_role " +
                			" )";
                con.createStatement().executeUpdate(sql);
                
                sql = "REPLACE INTO kasai_objects_groups_roles (id_object,id_group,id_role) SELECT '"
                		+destinationObject+"',id_group,id_role FROM kasai_objects_groups_roles  KOGR2 " +
                		"WHERE id_object='" + sourceObject + "' AND NOT EXISTS ( " +
                			" SELECT * FROM kasai_objects_groups_roles KOGR3 " +
                			" WHERE KOGR3.id_object='"+destinationObject+"' AND KOGR3.id_group=KOGR2.id_group AND KOGR3.id_role=KOGR2.id_role " +
                			" )";
                con.createStatement().executeUpdate(sql);
                
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "copyPermissionsFromObject", JDBCANSISQLAuthObjectDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }        
    }
    
    public void create(String object) throws DataAccessException{
            
        Connection con = null;
        String sql;
        if (StringUtils.isNotEmpty(object)){
            if (this.read(object) == null){
                try{
                    sql = "INSERT INTO kasai_objects (id) values ('" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "')";
                    con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                    con.createStatement().executeUpdate(sql);
                }catch (SQLException sqle){
                	Log.write("SQL Error", sqle, Log.ERROR, "create", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DataAccessException(sqle);
                }finally{
                    try{
                        con.close();
                    }catch(Exception e){}
                }
            }
        }
    }
    
    public void createObjectGroupRole(String object, String group, int role) throws DoesntExistsException,DataAccessException, XMLException{
            
        Connection con = null;
        String sql;
        
        try{
            if ((StringUtils.isNotEmpty(object)) && (StringUtils.isNotEmpty(group))){
                if (GroupHandler.getInstance().read(group) == null){
                	Log.write("Group doesn't exist", Log.WARN, "createObjectGroupRole", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(Group.class.getName() + ".groupDoesntExist");
                }
                if (RoleHandler.getInstance().read(role) == null){
                	Log.write("Role doesn't exist", Log.WARN, "createObjectGroupRole", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(Role.class.getName() + ".roleDoesntExist");
                }
                if (this.read(object) == null){
                	Log.write("Object doesn't exist", Log.WARN, "createObjectGroupRole", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(AuthObject.class.getName() + ".objectDoesntExist");
                }

                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                sql = "SELECT id from kasai_objects_groups_roles WHERE id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' AND id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "' AND id_role="  + role;
                if (!con.createStatement().executeQuery(sql).next()){
                    sql = "INSERT INTO kasai_objects_groups_roles (id_object,id_group,id_role) values ('" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "','" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "'," + role + ")";
                    con.createStatement().executeUpdate(sql);
                }
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "createObjectGroupRole", JDBCANSISQLAuthObjectDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }        
    }
    
    public void createObjectUserRole(String object, String login, int role) throws DoesntExistsException,DataAccessException, XMLException{
            
        Connection con = null;
        String sql;
        
        try{
            if ((StringUtils.isNotEmpty(object)) && (StringUtils.isNotEmpty(login))){
                if (UserHandler.getInstance().read(login,true) == null){
                	Log.write("User doesn't exist", Log.WARN, "createObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(User.class.getName() + ".userDoesntExist");
                }
                if (RoleHandler.getInstance().read(role) == null){
                	Log.write("Role doesn't exist", Log.WARN, "createObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(Role.class.getName() + ".roleDoesntExist");
                }
                if (this.read(object) == null){
                	Log.write("Object doesn't exist", Log.WARN, "createObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
                	
                    throw new DoesntExistsException(AuthObject.class.getName() + ".objectDoesntExist");
                }
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                sql = "SELECT id from kasai_objects_users_roles WHERE id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' AND id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' AND id_role="  + role;
                if (!con.createStatement().executeQuery(sql).next()){
                    sql = "INSERT INTO kasai_objects_users_roles (id_object,id_user,id_role) values ('" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "','" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "'," + role + ")";
                    con.createStatement().executeUpdate(sql);
                }
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "createObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }        
    }   
    
    public void delete(String object) throws DataAccessException{
        Connection con = null;
        String sql;
        try{
            if (StringUtils.isNotEmpty(object)){
                
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                
                sql = "DELETE FROM kasai_objects WHERE id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "'";
                con.createStatement().executeUpdate(sql);
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "delete", JDBCANSISQLAuthObjectDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
        
    }
    
    public void deleteObjectGroupRole(int id) throws DataAccessException{
        Connection con  = null;
        try{
            String sql = "delete from kasai_objects_groups_roles where id=" + id;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "deleteObjectGroupRole", JDBCANSISQLAuthObjectDAO.class);
            
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }
    
    public void deleteObjectUserRole(int id) throws DataAccessException{
        Connection con = null;
        try{
            String sql = "DELETE FROM kasai_objects_users_roles WHERE id=" + id;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "deleteObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
            
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }
    
    public void deleteObjectUserRole(String login, String object, int role) throws DataAccessException{
        Connection con = null;
        try{
            String sql = "DELETE FROM kasai_objects_users_roles WHERE id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' AND id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) +"' AND id_role=" + role;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "deleteObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
            
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }
    
    public void deleteObjectUserRole(String login, String object) throws DataAccessException{
        Connection con = null;
        try{
            String sql = "DELETE FROM kasai_objects_users_roles WHERE id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' AND id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) +"'";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "deleteObjectUserRole", JDBCANSISQLAuthObjectDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }
    
    public Collection listObjectGroupsRoles(String object) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        ObjectGroupRole ogr = null;
        ArrayList ogrs = new ArrayList();
        if(StringUtils.isNotEmpty(object)){
            try{

                sql = "SELECT AOGR.*,AR.name as role_name FROM kasai_objects_groups_roles AOGR,kasai_roles AR " + 
                      "WHERE AOGR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and AR.id=AOGR.id_role";

                sql += " order by AOGR.id_group ";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                rs = con.createStatement().executeQuery(sql);
                while (rs.next()){
                    ogr = new ObjectGroupRole (rs);
                    ogrs.add(ogr);
                }                
            }catch (SQLException sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "listObjectGroupsRoles", JDBCANSISQLAuthObjectDAO.class);
            	
                throw new DataAccessException(sqle);
            }finally{
                try{
                rs.close();
                }catch(Exception e){}
                try{
                    con.close();
                }catch(Exception e){}
            }
        }
        return ogrs;
    }
    
    public Collection listObjectUsersRoles(String object) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        ObjectUserRole our = null;
        ArrayList ours = new ArrayList();
        if(StringUtils.isNotEmpty(object)){
            try{

                sql = "SELECT AOUR.*,AR.name as role_name FROM kasai_objects_users_roles AOUR,kasai_roles AR " + 
                      "WHERE AOUR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and AR.id=AOUR.id_role";

                sql += " order by AOUR.id_user ";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                rs = con.createStatement().executeQuery(sql);
                while (rs.next()){
                    our = new ObjectUserRole (rs);
                    ours.add(our);
                }                
            }catch (SQLException sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "listObjectUsersRoles", JDBCANSISQLAuthObjectDAO.class);
            	
                throw new DataAccessException(sqle);
            }finally{
                try{
                    rs.close();
                }catch(Exception e){}
                try{
                    con.close();
                }catch(Exception e){}
            }
        }
        return ours;
    }
    
    public AuthObject read(String id) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        AuthObject o = null;
        try{
            if (StringUtils.isNotEmpty(id)){                
                sql = "SELECT * FROM kasai_objects WHERE id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(id) + "'";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                rs = con.createStatement().executeQuery(sql);
                if (rs.next()){
                    o = new AuthObject (rs);
                }
            }
            return o;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "read", JDBCANSISQLAuthObjectDAO.class);
            
            throw new DataAccessException(sqle);
        }finally{
            try{
                rs.close();
            }catch(Exception e){}
            try{
                con.close();
            }catch(Exception e){}
        }
        
    }
}
