package com.aaut.skeleton.rbac.service;

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
import org.apache.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import com.aaut.skeleton.SkelConstants;
import com.aaut.skeleton.rbac.exception.InvalidPasswordException;
import com.aaut.skeleton.rbac.exception.ServiceException;

/**
 *
 * @author  James
 */
public class RDBMSAuthService implements AuthService {    
	
	private static Logger logger = Logger.getLogger(RDBMSAuthService.class);

	public static final String ENCRYPTOR_STRENGTH_CLEARTEXT = "cleartext";
	public static final String ENCRYPTOR_STRENGTH_BASIC = "basic";
	public static final String ENCRYPTOR_STRENGTH_STRONG = "strong";
	
    private static Connection getConnection()
        throws SQLException {
        Connection con = null;

        logger.info("Enter");
        
        try {
            con = DriverManager.getConnection("jdbc:apache:commons:dbcp:lyptusAuth");
        } catch (Exception e) {
            initPool();
            con = DriverManager.getConnection("jdbc:apache:commons:dbcp:lyptusAuth");
        }
                
        logger.info("Exit");

        return con;
    }

    private static void initPool() {
        try {                        
            ResourceBundle res = ResourceBundle.getBundle(
                    SkelConstants.CONFIG_PROPERTY_FILE);

            Class.forName(res.getString("kasai.rdbms.driver"))
                 .newInstance();
                 
            GenericObjectPool              connPool    = new GenericObjectPool(null);
            DriverManagerConnectionFactory connFactory = new DriverManagerConnectionFactory(res.getString(
                        "kasai.rdbms.url"),
                    res.getString("kasai.rdbms.user"),
                    res.getString("kasai.rdbms.password"));

            @SuppressWarnings("unused")
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory,
                    connPool, null,
                    null, false,
                    true);
            PoolingDriver driver = new PoolingDriver();

            driver.registerPool("lyptusAuth", connPool);
        } catch (Exception e) {
        	logger.error("Something really bad happened while initializing db connection pool");
        }
    }    
    
    public int checkPassword(String userName, String password) throws ServiceException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        int result = AUTH_BAD_USERNAME;
        
        try {
            ResourceBundle res = ResourceBundle.getBundle(
                    SkelConstants.CONFIG_PROPERTY_FILE);
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
        	logger.error("Can't retrieve password from database");
            throw new ServiceException(e);
        } catch (Exception e){
        	logger.error("Unknow error");
        	
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
                    SkelConstants.CONFIG_PROPERTY_FILE);
        	
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
        	logger.error("SQL Error");
            
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
                    SkelConstants.CONFIG_PROPERTY_FILE);
            
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
        	
        	logger.error("SQL Error");
        	
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
                    SkelConstants.CONFIG_PROPERTY_FILE);
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
        	logger.error("SQL Error");
        	
            throw new ServiceException(e);
        }
        finally {
            try {stmt.close();}catch(Exception e){}
            try {con.close();}catch(Exception e){}
        }
        
        return password;
    }
    
    public static void main(String[] args) {
    	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    	System.out.println(passwordEncryptor.encryptPassword("admin"));
	}
}
