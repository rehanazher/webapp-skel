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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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
public class JDBCANSISQLRoleDAO implements RoleDAO {
    
    /** Creates a new instance of JDBCMySQLUserDAO */
    public JDBCANSISQLRoleDAO() {
    }
    
    public void addOperativeToRole(
        final String idOperative,
        final int    role
    ) throws DoesntExistsException, DataAccessException {

        Connection con = null;
        
        if (this.read(role) == null) {
        	Log.write("Role doesn't exist", Log.WARN, "addOperativeToRole", JDBCANSISQLRoleDAO.class);
        	
            throw new DoesntExistsException(Role.class.getName() + ".roleDoesntExist");
        }
        if (StringUtils.isEmpty(idOperative) || (OperativeHandler.getInstance().list(idOperative).size() == 0)) {
        	Log.write("Operative doesn't exist", Log.WARN, "addOperativeToRole", JDBCANSISQLRoleDAO.class);
            throw new DoesntExistsException(Operative.class.getName() + ".operativeDoesntExist");
        }

        if (this.listOperativesFromRole(role, idOperative).size() == 0) {

            try {
                
                String sql = "insert into kasai_roles_operatives (id_role,id_operative) values (" + role + ",'" +
                    org.apache.commons.lang.StringEscapeUtils.escapeSql(idOperative) + "')";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                con.createStatement().executeUpdate(sql);
            }catch (SQLException sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "addOperativeToRole", JDBCANSISQLRoleDAO.class);
            	
                throw new DataAccessException(sqle);
            } finally {

                try {
                    
                    con.close();
                } catch (Exception e) {

                }
            }
        }
    }
    
    public int create(String name, String description, String[] operatives) throws InvalidAttributesException, AlreadyExistsException, DoesntExistsException,DataAccessException{
            
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
            	Log.write("Role name already exist", Log.WARN, "create", JDBCANSISQLRoleDAO.class);
            	
                throw new AlreadyExistsException(this.getClass().getName() + ".roleAlreadyExist");
            }
            sql = "INSERT INTO kasai_roles (name, description) VALUES ('"+StringEscapeUtils.escapeSql(name)+"', '"+StringEscapeUtils.escapeSql(description)+"')";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.setAutoCommit(false);
            Statement stm = con.createStatement();
            stm.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            
            rs = stm.getGeneratedKeys();
            if (rs.next()){
                idRole = rs.getInt(1);
                
                Log.write("Role '"+ name +"' created with id=" + idRole, Log.DEBUG, "create", JDBCANSISQLRoleDAO.class);
            }
            
            rs.close();
            
            String idOperative = null;
            if (operatives != null){
                for (int i=0; i<operatives.length; i++) {
                    idOperative = operatives[i];
                    
                    if (StringUtils.isEmpty(idOperative) || (OperativeHandler.getInstance().list(idOperative).size() == 0)) {
                    	Log.write("Operative doesn't exist", Log.WARN, "create", JDBCANSISQLRoleDAO.class);
                    	
                        throw new DoesntExistsException(Operative.class.getName() + ".operativeDoesntExist");
                    }

                    sql = "INSERT INTO kasai_roles_operatives (id_role, id_operative) VALUES (" + idRole + ",'" + org.apache.commons.lang.StringEscapeUtils.escapeSql(idOperative) + "')";
                    stm.executeUpdate(sql);
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
        	Log.write("SQL Error", sqle, Log.ERROR, "create", JDBCANSISQLRoleDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                
                con.setAutoCommit(true);
                con.close();
            }catch(Exception e){}
        }        
        
        return idRole;
    }
    
    public void deleteOperativeFromRole(String idOperative, int role) throws DataAccessException{
        Connection con = null;
        if (StringUtils.isNotEmpty(idOperative)){
            try{
                String sql = "delete from kasai_roles_operatives where id_role=" + role + " and id_operative='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(idOperative) + "'";
                con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                con.createStatement().executeUpdate(sql);
            }catch (SQLException sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "deleteOperativeFromRole", JDBCANSISQLRoleDAO.class);
            	
                throw new DataAccessException(sqle);
            }finally{
                try{
                    con.close();
                }catch(Exception e){}
            }
        }
    }
    
    public void delete(int id) throws DataAccessException{
        Connection con = null;
        String sql;
        
        try{
            sql = "DELETE FROM kasai_roles WHERE id=" + id;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "delete", JDBCANSISQLRoleDAO.class);
        	
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
        
    }
    
    public Collection listOperativesFromRole(int role, String operative) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Operative o = null;
        ArrayList operatives = new ArrayList();
        try{
            sql = "SELECT AU.* FROM kasai_operatives AU,kasai_roles_operatives ARO " + 
                  "WHERE AU.id=ARO.id_operative AND ARO.id_role=" + role;
            if (StringUtils.isNotEmpty(operative)){
                sql += " AND AU.id='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(operative) +"' ";
            }
            sql += " order by AU.sequence ";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                o = new Operative (rs);
                operatives.add(o);
            }
            return operatives;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "listOperativesFromRole", JDBCANSISQLRoleDAO.class);
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
    
    public Collection listOperativesNotInRole(int role) throws DataAccessException {

        Connection con   = null;
        String     sql;
        ResultSet  rs    = null;
        Operative  o     = null;
        ArrayList  currentOperatives = new ArrayList();
        ArrayList  operatives = new ArrayList();

        try {
            sql = "SELECT AU.* FROM kasai_operatives AU,kasai_roles_operatives ARO " + 
                  "WHERE AU.id=ARO.id_operative AND ARO.id_role=" + role + " ORDER BY  AU.sequence ";
            con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs      = con.createStatement().executeQuery(sql);

            while (rs.next()) {

                o = new Operative(rs);
                currentOperatives.add(o);
            }
            
            sql = "SELECT * FROM kasai_operatives ORDER BY sequence ";
            con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs      = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                o = new Operative(rs);
                if (!currentOperatives.contains(o)) {
                    operatives.add(o);
                }
            }
            
            return operatives;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "listOperativesNotInRole", JDBCANSISQLRoleDAO.class);
        	
            throw new DataAccessException(sqle);
        } finally {

            try {

                rs.close();
            } catch (Exception e) {}

            try {

                con.close();
            } catch (Exception e) {}
        }
    }
    
    public List list(String name, boolean exactly) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Role r = null;
        ArrayList roles = new ArrayList();
        try{
            sql = "SELECT * FROM kasai_roles WHERE id <> -1";
            if (StringUtils.isNotEmpty(name)){
                if (!exactly){
                    sql += " AND name LIKE '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(name) +"%'";
                }else{
                    sql += " AND name LIKE '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(name) +"'";
                }
            }
            
            sql += " order by name ";
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            while (rs.next()){
                r = new Role (rs);
                roles.add(r);
            }
            return roles;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "list", JDBCANSISQLRoleDAO.class);
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
    
    public Role read(int role) throws DataAccessException{
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Role r = null;
        try{

            sql = "SELECT * FROM kasai_roles WHERE id=" + role;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            rs = con.createStatement().executeQuery(sql);
            if (rs.next()){
                r = new Role (rs);
            }
            return r;
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "read", JDBCANSISQLRoleDAO.class);
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
    
    public void update(int id, String name, String description) throws InvalidAttributesException,DataAccessException{
            
        Connection con = null;
        String sql;
        Role r = null;
        try{
            r = new Role();
            r.setDescription(description);
            r.setName(name);
            r.validate();
            
            sql = "UPDATE kasai_roles set name='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(name) + "', description='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(description) + "' where id=" + id;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "update", JDBCANSISQLRoleDAO.class);
            throw new DataAccessException(sqle);
        }finally{
            try{
                con.close();
            }catch(Exception e){}
        }
    }
    
    public void update(int id, String name, String description, String[] operatives) throws InvalidAttributesException,DataAccessException{
            
        Connection con = null;
        String sql;
        ResultSet rs = null;
        Role r = null;
        try{
            r = new Role();
            r.setDescription(description);
            r.setName(name);
            r.validate();
            
            sql = "UPDATE kasai_roles SET name='" + name + "', description='" + description + "' WHERE id=" + id;
            con = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
            con.createStatement().executeUpdate(sql);
            
            if (operatives != null) {

                String idOperative = null;

                ArrayList newOperatives = new ArrayList(Arrays.asList(operatives));

                ArrayList currentOperatives = new ArrayList();
                sql     = "SELECT id_operative FROM kasai_roles_operatives WHERE id_role=" + id;
                rs      = con.createStatement().executeQuery(sql);

                while (rs.next()) {

                    currentOperatives.add(StringUtils.defaultString(rs.getString("id_operative")));
                }

                for (int i = 0; i < operatives.length; i++) {

                    idOperative = operatives[i];

                    if (!currentOperatives.contains(idOperative)) {

                        sql     = "INSERT INTO kasai_roles_operatives (id_role, id_operative) VALUES (" + id + ",'" + idOperative +
                            "')";
                        con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                        con.createStatement().executeUpdate(sql);
                    }
                }

                for (int i = 0; i < currentOperatives.size(); i++) {

                    idOperative = (String) currentOperatives.get(i);

                    if (!newOperatives.contains(idOperative)) {

                        sql     = "DELETE FROM kasai_roles_operatives WHERE id_operative='" + idOperative + "' AND id_role=" + id;
                        con     = DBUtil.getConnection(Constants.DATABASE_SOURCE, Constants.CONFIG_PROPERTY_FILE);
                        con.createStatement().executeUpdate(sql);
                    }
                }
            }
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "update", JDBCANSISQLRoleDAO.class);
        	
            throw new DataAccessException(sqle);
        } finally {
            try {
                con.close();
            } catch(Exception e) {}
        }
    }
}
