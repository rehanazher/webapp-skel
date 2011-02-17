/*
 * JDBCMySQLUserDAO.java
 *
 * Created on 28 de marzo de 2005, 13:46
 */

package org.manentia.kasai.operative;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.Constants;
import org.manentia.kasai.Group;
import org.manentia.kasai.Operative;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.DataAccessException;
import org.xml.sax.SAXException;

import com.manentia.commons.log.Log;
import com.manentia.commons.persistence.DBUtil;
import com.manentia.commons.xml.XMLException;

/**
 *
 * @author rzuasti
 */
public class JDBCANSISQLOperativeDAO implements OperativeDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCANSISQLOperativeDAO() {
    }
    
    public Collection listGroupsOperative(String operative, String object) throws DataAccessException, XMLException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Group g = null;
        ArrayList groups = new ArrayList();
        try{
            
            int index = 0;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            while (index!=-1){
            
                sql = "select distinct AG.* from kasai_roles_operatives ARO, " + 
                      "kasai_users_groups AUG,kasai_objects_groups_roles AOGR,kasai_groups AG where " + 
                      "ARO.id_operative='"+ org.apache.commons.lang.StringEscapeUtils.escapeSql(operative) + "' and AOGR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and AOGR.id_group=AUG.id_group " + 
                      "and ARO.id_role=AOGR.id_role and AG.id=AUG.id_group " +
                      "and AG.blocked=0";
                
                rs = con.createStatement().executeQuery(sql);
                while (rs.next()){
                    g = new Group (rs);
                    if (!groups.contains(g))
                        groups.add(g);
                }
                
                index = operative.lastIndexOf('.');
                if (index>=0){
                    operative = operative.substring(0,index);
                }
                
            }
            
            return groups;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "listGroupsOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
        	Log.write("XML Error reading group", e, Log.ERROR, "listGroupsOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("XML Error reading group", e, Log.ERROR, "listGroupsOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("XML Error reading group", e, Log.ERROR, "listGroupsOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML Error reading group", e, Log.ERROR, "listGroupsOperative", JDBCANSISQLOperativeDAO.class);
        	
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
    
    public Collection list(String idOperative) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Operative o = null;
        ArrayList operatives = new ArrayList();
        try{
            sql = "SELECT * FROM kasai_operatives ";
            if (StringUtils.isNotEmpty(idOperative)){
                sql += " where id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(idOperative) +"'";
            }
            sql += " order by sequence ";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                o = new Operative (rs);
                operatives.add(o);
            }
            return operatives;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "list", JDBCANSISQLOperativeDAO.class);
        	
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
    
    public Collection listUsersOperative(String operative, String object) throws DataAccessException, XMLException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        User u = null;
        ArrayList users = new ArrayList();
        try{

            sql = "SELECT AU.* FROM kasai_users AU WHERE AU.super_user=1 AND AU.blocked=0";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                u = new User (rs);
                users.add(u);
            }
            
            int index = 0;
            while (index!=-1){
            
                sql = "select distinct AU.* from kasai_roles_operatives ARO, " + 
                      "kasai_users_groups AUG,kasai_users AU,kasai_objects_groups_roles AOGR,kasai_groups AG where " + 
                      "ARO.id_operative='"+ org.apache.commons.lang.StringEscapeUtils.escapeSql(operative) + "' and AOGR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and AOGR.id_group=AUG.id_group " + 
                      "and ARO.id_role=AOGR.id_role and AG.id=AUG.id_group " +
                      "and AU.id=AUG.id_user" + " and AG.blocked=0" + " and AU.blocked=0";

                rs = con.createStatement().executeQuery(sql);
                while (rs.next()){
                    u = new User (rs);
                    if (!users.contains(u))
                        users.add(u);
                }

                sql = "select distinct AU.* from kasai_roles_operatives ARO,kasai_users AU,kasai_objects_users_roles AOUR " +
                      "where AOUR.id_user=AU.id and " + 
                      "ARO.id_operative='"+ org.apache.commons.lang.StringEscapeUtils.escapeSql(operative) + "' and AOUR.id_object='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(object) + "' and ARO.id_role=AOUR.id_role" + " and AU.blocked=0";
                
                rs = con.createStatement().executeQuery(sql);
                while (rs.next()){
                    u = new User (rs);
                    if (!users.contains(u))
                        users.add(u);
                }                
                
                index = operative.lastIndexOf('.');
                if (index>=0){
                    operative = operative.substring(0,index);
                }
                
            }
            return users;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "listUsersOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new DataAccessException(sqle);
        } catch (SAXException e) {
        	Log.write("XML error reading user", e, Log.ERROR, "listUsersOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new XMLException(e);
		} catch (IOException e) {
			Log.write("XML error reading user", e, Log.ERROR, "listUsersOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new XMLException(e);
		} catch (ParserConfigurationException e) {
			Log.write("XML error reading user", e, Log.ERROR, "listUsersOperative", JDBCANSISQLOperativeDAO.class);
        	
            throw new XMLException(e);
		} catch (FactoryConfigurationError e) {
			Log.write("XML error reading user", e, Log.ERROR, "listUsersOperative", JDBCANSISQLOperativeDAO.class);
        	
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
}
