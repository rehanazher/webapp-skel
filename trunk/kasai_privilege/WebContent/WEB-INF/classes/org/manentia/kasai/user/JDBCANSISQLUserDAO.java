/*
 * JDBCMySQLUserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:46
 */

package org.manentia.kasai.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.Constants;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.util.CacheUsers;
import org.xml.sax.SAXException;

import com.manentia.commons.log.Log;
import com.manentia.commons.persistence.DBUtil;
import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public class JDBCANSISQLUserDAO implements UserDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCANSISQLUserDAO() {
    }

    public boolean checkOperative(String login, String operative, String object) {
        Connection con = null;
        String sql;
        ResultSet rs = null;
        boolean result = false;
        try{
            org.manentia.kasai.User u = this.read(login,true);
            if (u == null){
                return false;
            }
            if (u.getBlocked()){
                return false;
            }
            if(u.getSuperUser()){
                return true;
            }
            
            sql = "select distinct(ARO.id_operative) as operative from kasai_roles_operatives ARO, " + 
                  "kasai_users_groups AUG,kasai_objects_groups_roles AOGR,kasai_groups AG where " + 
                  "AOGR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and AOGR.id_group=AUG.id_group " + 
                  "and AUG.id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' and ARO.id_role=AOGR.id_role and AG.id=AUG.id_group and AG.blocked=0";
            
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next() && (!result)){
                result = (operative.startsWith(rs.getString("operative")));
            }
            
            if (!result){
                sql = "select distinct(ARO.id_operative) as operative from kasai_roles_operatives ARO, " + 
                      "kasai_objects_users_roles AOUR where AOUR.id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' and " + 
                      "AOUR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and ARO.id_role=AOUR.id_role";
                rs = con.createStatement().executeQuery(sql);
                while (rs.next() && (!result)){
                    result = (operative.startsWith(rs.getString("operative")));
                }
            }
            
            return result;
        }catch (DataAccessException dae){
            return false;
        } catch (SQLException sqlE){
        	Log.write("SQL Error", sqlE, Log.ERROR, "checkOperative", JDBCANSISQLUserDAO.class);
        	
            return false;
        } catch (XMLException e) {
        	return false;
		}finally{
            try{
                rs.close();
            }catch(Exception e){}
            try{
                con.close();
            }catch(Exception e){}
        }
    }

    public void create(User user) 
    	throws org.manentia.kasai.exceptions.InvalidAttributesException, 
    		org.manentia.kasai.exceptions.AlreadyExistsException, DataAccessException, XMLException {
    	
        Connection con = null;
        String sql;
        try{
            user.validate();
            
            if (this.read(user.getLogin(),true) != null){
            	Log.write("Login already exist", Log.WARN, "create", JDBCANSISQLUserDAO.class);
            	
                throw new AlreadyExistsException(this.getClass().getName() + ".userAlreadyExist");
            }
            sql = "INSERT INTO kasai_users (id, first_name, last_name, email,blocked,description,data,super_user) VALUES (?,?,?,?,?,?,?,?)";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, user.getLogin());
            stm.setString(2, user.getFirstName());
            stm.setString(3, user.getLastName());
            stm.setString(4, user.getEmail());
            if (user.getBlocked()){
                stm.setInt(5, 1);
            }else{
                stm.setInt(5, 0);
            }
            stm.setString(6, user.getDescription());
            stm.setString(7, user.getAttributesXML());
            if (user.getSuperUser()){
                stm.setInt(8, 1);
            }else{
                stm.setInt(8, 0);
            }
            stm.executeUpdate();
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "create", JDBCANSISQLUserDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (ParserConfigurationException e) {
        	Log.write("Error saving attributes XML", e, Log.ERROR, "create", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("Error saving attributes XML", e, Log.ERROR, "create", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		}finally{
            try{
                con.close();
            }catch(Exception e){}
        }        
    }

    public void delete(String login) throws DataAccessException {
        Connection con = null;
        String sql;
        try{
            if (StringUtils.isNotEmpty(login)){
                sql = "DELETE FROM kasai_users WHERE id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "'";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                con.createStatement().executeUpdate(sql);
                CacheUsers.addUser(login,null);
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "delete", JDBCANSISQLUserDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }

    public java.util.List list(String login, String firstName, String lastName, String email, int blocked, String description, String group) throws DataAccessException, XMLException {
        Connection con = null;
        String sql;
        ResultSet rs = null;
        User u = null;
        ArrayList users = new ArrayList();
        try{
            sql = "SELECT AU.* FROM kasai_users AU";
            if (StringUtils.isNotEmpty(group)){
                sql += ", kasai_users_groups AUG WHERE AUG.id_user=AU.id AND AUG.id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) +"'";
            }
            else{
                sql += " WHERE AU.id <> ''";
            }
            if (StringUtils.isNotEmpty(login)){
                sql += " AND AU.id LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) +"%'";
            }
            if (StringUtils.isNotEmpty(firstName)){
                sql += " AND AU.first_name LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(firstName) +"%'";
            }
            if (StringUtils.isNotEmpty(lastName)){
                sql += " AND AU.last_name LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(lastName) +"%'";
            }
            if (StringUtils.isNotEmpty(email)){
                sql += " AND AU.email LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(email) +"%'";
            }
            if (blocked != -1){
                sql += " AND AU.blocked = " + blocked;
            }
            if (StringUtils.isNotEmpty(description)){
                sql += " AND AU.description LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(description) +"%'";
            }
            sql += " order by AU.last_name, AU.first_name, AU.id ";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                u = new User (rs);
                users.add(u);
            }
            return users;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "list", JDBCANSISQLUserDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
        	Log.write("Error attributes XML document", e, Log.ERROR, "list", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("Error attributes XML document", e, Log.ERROR, "list", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("Error attributes XML document", e, Log.ERROR, "list", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("Error attributes XML document", e, Log.ERROR, "list", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		}finally{
            try{
                rs.close();
            }catch(Exception e){}
            try{
                con.close();
            }catch(Exception e){}
        }
    }

    public org.manentia.kasai.User read(String login, boolean cache) throws DataAccessException, XMLException {
        Connection con = null;
        String sql;
        ResultSet rs = null;
        User u = null;
        try{
            if (StringUtils.isNotEmpty(login)){
                if (cache){
                    u = CacheUsers.getUser(login);
                }
                if (u == null){
                    sql = "SELECT * FROM kasai_users WHERE id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "'";
                    con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                    rs = con.createStatement().executeQuery(sql);
                    if (rs.next() && (rs.getString ("id").equals (login))){
                        u = new User (rs);
                    }
                }
            }
            return u;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "read", JDBCANSISQLUserDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
    		Log.write("Error attributes XML document", e, Log.ERROR, "read", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("Error attributes XML document", e, Log.ERROR, "read", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("Error attributes XML document", e, Log.ERROR, "read", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("Error attributes XML document", e, Log.ERROR, "read", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		}finally{
            try{
                rs.close();
            }catch(Exception e){}
            try{
                con.close();
            }catch(Exception e){}
        }
    }

    public void update(User user) throws org.manentia.kasai.exceptions.InvalidAttributesException, DataAccessException, XMLException {
        Connection con = null;
        String sql;
        try{
            user.validate();
            
            sql = "UPDATE kasai_users set first_name=?, last_name=?, email=?, blocked=?, description=?, data=?, super_user=? where id=?";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setString(1, user.getFirstName());
            stm.setString(2, user.getLastName());
            stm.setString(3, user.getEmail());
            if (user.getBlocked()){
                stm.setInt(4, 1);
            }else{
                stm.setInt(4, 0);
            }
            stm.setString(5, user.getDescription());
            stm.setString(6, user.getAttributesXML());
            if (user.getSuperUser()){
                stm.setInt(7, 1);
            }else{
                stm.setInt(7, 0);
            }
            stm.setString(8,user.getLogin());
            stm.executeUpdate();
            user = this.read(user.getLogin(),false);
            CacheUsers.addUser(user.getLogin(), user);
                
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "update", JDBCANSISQLUserDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (ParserConfigurationException e) {
        	Log.write("Error saving attributes XML", e, Log.ERROR, "update", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("Error saving attributes XML", e, Log.ERROR, "update", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		} catch (DataAccessException e) {
			Log.write("Error saving attributes XML", e, Log.ERROR, "update", JDBCANSISQLUserDAO.class);
        	
            throw new XMLException(e);
		}finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }

	public String[] listUsernames() throws DataAccessException {
		Connection con = null;
        String sql;
        ResultSet rs = null;
        ArrayList<String> usernames = new ArrayList<String>();
        try{
            sql = "SELECT AU.id FROM kasai_users AU";
            sql += " order by AU.id ";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                usernames.add(rs.getString(1));
            }
            return usernames.toArray(new String[0]);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "list", JDBCANSISQLUserDAO.class);
        	
            throw new DataAccessException(sqle);
        } finally{
            try{
                rs.close();
            }catch(Exception e){}
            try{
                con.close();
            }catch(Exception e){}
        }
	}

	public String[] listUsernames(String groupId) throws DataAccessException {
		Connection con = null;
        String sql;
        ResultSet rs = null;
        ArrayList<String> usernames = new ArrayList<String>();
        
        if (StringUtils.isEmpty(groupId)){
        	return listUsernames();
        } else {
	        try{
	            sql = "SELECT UG.id_user FROM kasai_users_groups UG";
	            sql += " WHERE UG.id_group='" + StringEscapeUtils.escapeSql(groupId) + "'";
	            sql += " order by UG.id_user ";
	            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
	            rs = con.createStatement().executeQuery(sql);
	            while (rs.next()){
	                usernames.add(rs.getString(1));
	            }
	            return usernames.toArray(new String[0]);
	        }catch (SQLException sqle){
	        	Log.write("SQL Error", sqle, Log.ERROR, "listUsernames", JDBCANSISQLUserDAO.class);
	        	
	            throw new DataAccessException(sqle);
	        } finally{
	            try{
	                rs.close();
	            }catch(Exception e){}
	            try{
	                con.close();
	            }catch(Exception e){}
	        }
        }
	}
    
}
