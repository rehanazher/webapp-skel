package org.manentia.kasai.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.manentia.kasai.Constants;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.ServiceException;

import com.manentia.commons.log.Log;
import com.manentia.commons.security.EncryptionUtil;

/**
 *
 * @author  rzuasti
 */
public class RDBMSAuthService implements AuthService {    

	public static final String ENCRYPTOR_STRENGTH_CLEARTEXT = "cleartext";
	public static final String ENCRYPTOR_STRENGTH_BASIC = "basic";
	public static final String ENCRYPTOR_STRENGTH_STRONG = "strong";
	
    private static Connection getConnection()
        throws SQLException {
        Connection con = null;

        Log.write("Enter", Log.INFO, "getConnection", RDBMSAuthService.class);
        
        try {
            con = DriverManager.getConnection("jdbc:apache:commons:dbcp:lyptusAuth");
        } catch (Exception e) {
            initPool();
            con = DriverManager.getConnection("jdbc:apache:commons:dbcp:lyptusAuth");
        }
                

        Log.write("Exit", Log.INFO, "getConnection", RDBMSAuthService.class);

        return con;
    }

    private static void initPool() {
        try {                        
            ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);

            Class.forName(res.getString("kasai.rdbms.driver"))
                 .newInstance();
                 
            GenericObjectPool              connPool    = new GenericObjectPool(null);
            DriverManagerConnectionFactory connFactory = new DriverManagerConnectionFactory(res.getString(
                        "kasai.rdbms.url"),
                    res.getString("kasai.rdbms.user"),
                    res.getString("kasai.rdbms.password"));

            PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory,
                    connPool, null,
                    null, false,
                    true);
            PoolingDriver driver = new PoolingDriver();

            driver.registerPool("lyptusAuth", connPool);
        } catch (Exception e) {
        	Log.write("Something really bad happened while initializing db connection pool", e, Log.ERROR, "initPool", RDBMSAuthService.class);
        }
    }    
    
    public int checkPassword(String userName, String password) throws ServiceException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int result = AUTH_BAD_USERNAME;
        
        try {
            ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);
            con = getConnection();
            
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT " +
                res.getString("kasai.rdbms.passwordField") + " FROM " + res.getString("kasai.rdbms.table") + 
                " WHERE " + res.getString("kasai.rdbms.usernameField") + "='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(userName) + "'");
            
            if (rs.next()){
                // Just to make sure no nulls are handled
                String storedPassword = StringUtils.defaultString(rs.getString(1));
                password = StringUtils.defaultString(password);
                
                result = AUTH_BAD_PASSWORD;
                
                if (StringUtils.isEmpty(storedPassword) && StringUtils.isEmpty(password)){
                	result = AUTH_OK;
                } else {
	                if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_BASIC)){
	                	BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
	                	
	                	if (passwordEncryptor.checkPassword(password, storedPassword)) {
	                		result = AUTH_OK;
	            		}
	                } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_STRONG)){
	                	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	                	
	                	if (passwordEncryptor.checkPassword(password, storedPassword)) {
	                		result = AUTH_OK;
	            		}
	                } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_CLEARTEXT)){	                	
	                	if (storedPassword.equals(password)) {
	                		result = AUTH_OK;
	            		}
	                } 
                }
            }
            
        } catch (SQLException e){
        	Log.write("Can't retrieve password from database", e, Log.ERROR, "checkPassword", RDBMSAuthService.class);
            throw new ServiceException(e);
        } catch (Exception e){
        	Log.write("Unknow error", e, Log.ERROR, "checkPassword", RDBMSAuthService.class);
        	
            throw new ServiceException(e);
        } 
        finally {
            try {rs.close();}catch(Exception e){}
            try {stmt.close();}catch(Exception e){}
            try {con.close();}catch(Exception e){}
        }
        
        return result;
    }
    
    public void changePassword(String userName, String oldPassword, String newPassword) throws ServiceException, InvalidPasswordException {
    
        Connection con = null;
        Statement stmt = null;   
        String sql = null;
        
        try {
        	ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);
        	
            oldPassword = StringUtils.defaultString(oldPassword);
            newPassword = StringUtils.defaultString(newPassword);
            String encryptedPassword = null;
            
            if (checkPassword(userName, oldPassword)!=AUTH_OK){
                throw new InvalidPasswordException("Invalid password");
            }
            
            if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_BASIC)){
            	BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            	
            	encryptedPassword = passwordEncryptor.encryptPassword(newPassword);
            } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_STRONG)){
            	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            	
            	encryptedPassword = passwordEncryptor.encryptPassword(newPassword);
            } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_CLEARTEXT)){	                	
            	encryptedPassword = newPassword;
            } 
            
            
            con = getConnection();
            
            stmt = con.createStatement();
            
            sql = "UPDATE " + res.getString("kasai.rdbms.table") + 
                " SET " + res.getString("kasai.rdbms.passwordField") + "='" + 
                encryptedPassword  +
                "' WHERE " + res.getString("kasai.rdbms.usernameField") + "='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(userName) + "'";
            
            stmt.executeUpdate(sql);                        
            
        } catch (SQLException e){
        	Log.write("SQL Error", e, Log.ERROR, "changePassword", RDBMSAuthService.class);
            
            throw new ServiceException(e);
        }
        finally {
            try {stmt.close();}catch(Exception e){}
            try {con.close();}catch(Exception e){}
        }
        
    }
    
    public void setPassword(String userName, String password)
        throws ServiceException, InvalidPasswordException {
    
        Connection con = null;
        Statement stmt = null;
        String sql = null;
        
        try {           
            // We dont like null passwords
            password = StringUtils.defaultString(password);
            
            ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);
            
            String encryptedPassword = null;
            
            if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_BASIC)){
            	BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            	
            	encryptedPassword = passwordEncryptor.encryptPassword(password);
            } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_STRONG)){
            	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            	
            	encryptedPassword = passwordEncryptor.encryptPassword(password);
            } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_CLEARTEXT)){	                	
            	encryptedPassword = password;
            } 
            
            con = getConnection();
            
            stmt = con.createStatement();
        
            sql = "UPDATE " + res.getString("kasai.rdbms.table") + 
                " SET " + res.getString("kasai.rdbms.passwordField") + "='" + 
                encryptedPassword  +
                "' WHERE " + res.getString("kasai.rdbms.usernameField") + "='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(userName) + "'";
            
            stmt.executeUpdate(sql);                        
            
        } catch (SQLException e){
        	Log.write("SQL Error", e, Log.ERROR, "setPassword", RDBMSAuthService.class);
        	
            throw new ServiceException(e);
        }
        finally {
            try {stmt.close();}catch(Exception e){}
            try {con.close();}catch(Exception e){}
        }
        
    }
    
    public String resetPassword(String userName) throws ServiceException {
            
        Connection con = null;
        Statement stmt = null;   
        String password = null;
        String sql = null;
        
        try {            
            ResourceBundle res = ResourceBundle.getBundle(
                    Constants.CONFIG_PROPERTY_FILE);
            con = getConnection();
            
            password = RandomStringUtils.randomAlphanumeric(Integer.parseInt(res.getString("kasai.rdbms.randomPassword.length")));
            String encryptedPassword = null;
            
            if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_BASIC)){
            	BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            	
            	encryptedPassword = passwordEncryptor.encryptPassword(password);
            } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_STRONG)){
            	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            	
            	encryptedPassword = passwordEncryptor.encryptPassword(password);
            } else if (res.getString("kasai.rdbms.encryptorStrength").equals(RDBMSAuthService.ENCRYPTOR_STRENGTH_CLEARTEXT)){	                	
            	encryptedPassword = password;
            } 
            
            stmt = con.createStatement();
            
            sql = "UPDATE " + res.getString("kasai.rdbms.table") + 
                " SET " + res.getString("kasai.rdbms.passwordField") + "='" + 
                encryptedPassword  +
                "' WHERE " + res.getString("kasai.rdbms.usernameField") + "='" + org.apache.commons.lang.StringEscapeUtils.escapeSql(userName) + "'";
            
            stmt.executeUpdate(sql);                        
            
        } catch (SQLException e){
        	Log.write("SQL Error", e, Log.ERROR, "resetPassword", RDBMSAuthService.class);
        	
            throw new ServiceException(e);
        }
        finally {
            try {stmt.close();}catch(Exception e){}
            try {con.close();}catch(Exception e){}
        }
        
        return password;
    }        
}
