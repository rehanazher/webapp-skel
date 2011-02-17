/*
 * JDBCMySQLUserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:46
 */

package org.manentia.kasai.group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.Constants;
import org.manentia.kasai.Group;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.exceptions.InvalidAttributesException;
import org.manentia.kasai.user.UserHandler;
import org.xml.sax.SAXException;

import com.manentia.commons.log.Log;
import com.manentia.commons.persistence.DBUtil;
import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public class JDBCANSISQLGroupDAO implements GroupDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCANSISQLGroupDAO() {
    }
   
    public void addUserToGroup(String login, String group) throws DoesntExistsException,DataAccessException, XMLException{
        Connection con = null;
        if (UserHandler.getInstance().read(login,true) == null){
        	Log.write("User doesn't exist", Log.WARN, "addUserToGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new DoesntExistsException(User.class.getName() + ".userDoesntExist");
        }
        if (this.read(group) == null){
        	Log.write("Group doesn't exist", Log.WARN, "addUserToGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new DoesntExistsException(Group.class.getName() + ".groupDoesntExist");
        }
        if (UserHandler.getInstance().list(login,null,null,null,-1,null,group).size()==0){
            try{
                String sql = "insert into kasai_users_groups (id_user,id_group) values ('" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "','" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "')";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                con.createStatement().executeUpdate(sql);
            }catch (SQLException sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "addUserToGroup", JDBCANSISQLGroupDAO.class);
            	
                throw new DataAccessException(sqle);
            }finally{
                try{
                    con.close();
                }catch(Exception e){}
            }
        }
    }
    
    public boolean checkUserBelongsToGroup(String user, String group) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        try{
            sql = "SELECT AU.* FROM kasai_users AU";
            sql += ", kasai_users_groups AUG WHERE AUG.id_user=AU.id AND AUG.id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) +"'";
            sql += " AND AU.id = '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(user) +"'";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            if (rs.next()){
                return true;
            }
            else{
                return false;
            }
                
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "checkUserBelongsToGroup", JDBCANSISQLGroupDAO.class);
        	
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
    
    public void create(Group group) throws InvalidAttributesException, AlreadyExistsException, DataAccessException, XMLException{
            
        Connection con = null;
        String sql;
        try{
            
            group.validate();
            
            if (this.read(group.getId()) != null){
            	Log.write("Group name already exist", Log.WARN, "create", JDBCANSISQLGroupDAO.class);
            	
                throw new AlreadyExistsException(this.getClass().getName() + ".groupAlreadyExist");
            }
            sql = "INSERT INTO kasai_groups (id, blocked, description, data) VALUES (?,?,?,?)";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1,group.getId());
            if (group.getBlocked()){
                stm.setInt(2, 1);
            }else{
                stm.setInt(2, 0);
            }
            stm.setString(3, group.getDescription());
            stm.setString(4, group.getAttributesXML());
            stm.executeUpdate();
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "create", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (ParserConfigurationException e) {
        	Log.write("XML error saving group", e, Log.ERROR, "create", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML error saving group", e, Log.ERROR, "create", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		}finally{
            try{
                
                con.close();
            }catch(Exception e){}
        }        
    }
    
    public void delete(String group) throws DataAccessException{
        Connection con = null;
        String sql;
        try{
            if (StringUtils.isNotEmpty(group)){
                sql = "DELETE FROM kasai_groups WHERE id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "'";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                con.createStatement().executeUpdate(sql);
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "delete", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
        
    }
    
    public void deleteUserFromGroup(String login, String group) throws DataAccessException{
        Connection con = null;
        if (StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(group)){
            try{
                String sql = "delete from kasai_users_groups where id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' and id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "'";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                con.createStatement().executeUpdate(sql);
            }catch (SQLException sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "deleteUserFromGroup", JDBCANSISQLGroupDAO.class);
            	
                throw new DataAccessException(sqle);
            }finally {
                try{
                    con.close();
                }catch(Exception e){}
            }
        }
    }
    
    public List list(String idGroup, String description, int blocked, int system, String login) throws DataAccessException, XMLException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Group g = null;
        ArrayList groups = new ArrayList();
        try{
            sql = "SELECT AG.* FROM kasai_groups AG ";
            if (StringUtils.isNotEmpty(login)){
                sql += ", kasai_users_groups AUG WHERE AUG.id_group=AG.id AND AUG.id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) +"'";
            }
            else{
                sql += " WHERE AG.id <> ''";
            }
            if (StringUtils.isNotEmpty(idGroup)){
                sql += " AND AG.id LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(idGroup) +"%'";
            }
            if (blocked != -1){
                sql += " AND AG.blocked = " + blocked;
            }
            if (system != -1){
                sql += " AND AG.system = " + system;
            }
            if (StringUtils.isNotEmpty(description)){
                sql += " AND AG.description LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(description) +"%'";
            }
            sql += " order by AG.id ";
            
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                g = new Group (rs);
                groups.add(g);
            }
            return groups;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "list", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
        	Log.write("XML error reading group attributes", e, Log.ERROR, "list", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("XML error reading group attributes", e, Log.ERROR, "list", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("XML error reading group attributes", e, Log.ERROR, "list", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML error reading group attributes", e, Log.ERROR, "list", JDBCANSISQLGroupDAO.class);
        	
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
    
    public Collection listUsersNotInGroup(String group) throws DataAccessException, XMLException {

        Connection con   = null;
        String     sql;
        ResultSet  rs    = null;
        User       u     = null;
        ArrayList  members = new ArrayList();
        ArrayList  users = new ArrayList();

        try {
            sql = "SELECT AU.* FROM kasai_users AU,kasai_users_groups AUG WHERE AUG.id_user=AU.id AND AUG.id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "'";
            sql += " order by AU.last_name, AU.first_name, AU.id ";
            con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs      = con.createStatement().executeQuery(sql);

            while (rs.next()) {

                u = new User(rs);
                members.add(u);
            }

            sql = "SELECT * FROM kasai_users";
            sql += " order by last_name, first_name, id ";
            con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs      = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                u = new User(rs);
                if (!members.contains(u)) {
                    users.add(u);
                }
            }
            
            return users;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "listUsersNotInGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
        	Log.write("XML error reading users", e, Log.ERROR, "listUsersNotInGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("XML error reading users", e, Log.ERROR, "listUsersNotInGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("XML error reading users", e, Log.ERROR, "listUsersNotInGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML error reading users", e, Log.ERROR, "listUsersNotInGroup", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} finally {

            try {

                rs.close();
            } catch (Exception e) {}

            try {

                con.close();
            } catch (Exception e) {}
        }
    }
    
    public Group read(String group) throws DataAccessException, XMLException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Group g = null;
        try{
            if (StringUtils.isNotEmpty(group)){                
                sql = "SELECT * FROM kasai_groups WHERE id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group) + "'";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                rs = con.createStatement().executeQuery(sql);
                if (rs.next()){
                    g = new Group (rs);
                }
            }
            return g;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "read", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
        	Log.write("XML error reading group attributes", e, Log.ERROR, "read", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("XML error reading group attributes", e, Log.ERROR, "read", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("XML error reading group attributes", e, Log.ERROR, "read", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML error reading group attributes", e, Log.ERROR, "read", JDBCANSISQLGroupDAO.class);
        	
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
    
    public void update(Group group) throws InvalidAttributesException, DataAccessException, XMLException {

        Connection con = null;
        String     sql;
        try {

            group.validate();

            sql     = "UPDATE kasai_groups set blocked=?, description=?, data=? where id=?";
            con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);

            PreparedStatement stm = con.prepareStatement(sql);

            if (group.getBlocked()) {

                stm.setInt(1, 1);
            } else {

                stm.setInt(1, 0);
            }

            stm.setString(2, group.getDescription());
            stm.setString(3, group.getAttributesXML());
            stm.setString(4, group.getId());
            stm.executeUpdate();
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "update", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (ParserConfigurationException e) {
        	Log.write("XML Error saving group", e, Log.ERROR, "update", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML Error saving group", e, Log.ERROR, "update", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} finally {

            try {

                con.close();
            } catch (Exception e) {}
        }
    }

    public void update(Group group, String[] members
    ) throws InvalidAttributesException, DataAccessException, XMLException {

        Connection con = null;
        ResultSet  rs  = null;
        String     sql;

        try {

            group.validate();

            sql     = "UPDATE kasai_groups set blocked=?, description=?, data=? where id=?";
            con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);

            PreparedStatement stm = con.prepareStatement(sql);

            if (group.getBlocked()) {

                stm.setInt(1, 1);
            } else {

                stm.setInt(1, 0);
            }

            stm.setString(2, group.getDescription());
            stm.setString(3, group.getAttributesXML());
            stm.setString(4, group.getId());
            stm.executeUpdate();

            if (members != null) {

                String login = null;

                ArrayList newMembers = new ArrayList(Arrays.asList(members));

                ArrayList currentMembers = new ArrayList();
                sql     = "SELECT id_user FROM kasai_users_groups WHERE id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group.getId()) + "'";
                rs      = con.createStatement().executeQuery(sql);

                while (rs.next()) {

                    currentMembers.add(StringUtils.defaultString(rs.getString("id_user")));
                }

                for (int i = 0; i < members.length; i++) {

                    login = members[i];

                    if (!currentMembers.contains(login)) {

                        sql     = "INSERT INTO kasai_users_groups (id_user,id_group) VALUES ('" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "','" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group.getId()) +
                            "')";
                        con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                        con.createStatement().executeUpdate(sql);
                    }
                }

                for (int i = 0; i < currentMembers.size(); i++) {

                    login = (String) currentMembers.get(i);

                    if (!newMembers.contains(login)) {

                        sql     = "DELETE FROM kasai_users_groups WHERE id_user='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(login) + "' AND id_group='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(group.getId()) +
                            "'";
                        con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                        con.createStatement().executeUpdate(sql);
                    }
                }
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "update", JDBCANSISQLGroupDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (ParserConfigurationException e) {
        	Log.write("XML Error saving group", e, Log.ERROR, "update", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML Error saving group", e, Log.ERROR, "update", JDBCANSISQLGroupDAO.class);
        	
            throw new XMLException(e);
		} finally {

            try {

                con.close();
            } catch (Exception e) {}
        }
    }
}
