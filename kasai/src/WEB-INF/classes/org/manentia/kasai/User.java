package org.manentia.kasai;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.exceptions.InvalidAttributesException;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.ServiceException;
import org.manentia.kasai.exceptions.ServiceNotAvailableException;
import org.manentia.kasai.services.AuthService;
import org.manentia.kasai.services.AuthServiceFactory;
import org.manentia.kasai.user.passwordvalidators.PasswordValidator;
import org.manentia.kasai.user.passwordvalidators.PasswordValidatorFactory;
import org.manentia.kasai.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLException;


/**
 *
 * @author  fpena
 *
 */
public class User implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2559503917687585951L;
	
	public static final int AUTH_OK = AuthService.AUTH_OK;
    public static final int AUTH_BAD_USERNAME = AuthService.AUTH_BAD_USERNAME;
    public static final int AUTH_BAD_PASSWORD = AuthService.AUTH_BAD_PASSWORD;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_BLOCKED = 2;
    
    private String login;
   
    private String firstName;

    private String lastName;

    private String email;

    private boolean blocked;

    private String description;

    private Map attributes;

    private String password;

    private Collection groups;

    private Collection objectsUsersRoles;
    
    private boolean superUser;
    
   
    public User() {
        groups = new ArrayList();
        objectsUsersRoles = new ArrayList();
        superUser = false;
        attributes = new HashMap();
    }
    
    public User (ResultSet rs) throws SQLException, SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
        login = rs.getString("id");
        firstName = StringUtils.defaultString(rs.getString ("first_name"));
        lastName = StringUtils.defaultString(rs.getString("last_name"));
        email = StringUtils.defaultString(rs.getString("email"));
        blocked = (rs.getInt("blocked")!=0);
        description = StringUtils.defaultString(rs.getString("description"));
        attributes = MiscUtils.parseXMLMap(rs.getString("data"));
        superUser = (rs.getInt("super_user")!=0);
        groups = new ArrayList();
        objectsUsersRoles = new ArrayList();
    }
    
    
    public int checkPassword(String password)
        throws ServiceNotAvailableException, ServiceException {
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);

        AuthService authService = AuthServiceFactory.getAuthService(res.getString(
                    "auth.service"));

        return authService.checkPassword(this.login, StringUtils.defaultString(password));
    }

    public void changePassword(String oldPassword, String newPassword, String confirmation)
        throws ServiceNotAvailableException, ServiceException, InvalidAttributesException, InvalidPasswordException {
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);

        AuthService authService = AuthServiceFactory.getAuthService(res.getString(
                    "auth.service"));
        if ((newPassword != null) && (newPassword.equals (confirmation))){
        	
        	executePasswordValidators(newPassword);
            authService.changePassword(this.login, oldPassword, newPassword);
            this.setPassword(newPassword);
        }
        else{
        	Log.write("Password and confirmation password don't match", Log.INFO, "changePassword", User.class);
        	
            throw new InvalidPasswordException(User.class.getName() + ".changePassword.passwordMisMatch");
        }
    }
    
    private void executePasswordValidators(String password) throws ServiceNotAvailableException, InvalidPasswordException{
    	ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
    	
    	StringTokenizer validatorsList = new StringTokenizer(res.getString("passwords.validators"), ",");
    	String validatorClassName = null;
    	PasswordValidator validator = null;
    	
    	while (validatorsList.hasMoreTokens()){
    		validatorClassName = validatorsList.nextToken();
    		
    		validator = PasswordValidatorFactory.getPasswordValidator(validatorClassName);
    		
    		if (!validator.validate(password)){
    			throw new InvalidPasswordException(validatorClassName + ".message");
    		}
    	}
    }
    
    public void overridePassword(String newPassword)
        throws ServiceNotAvailableException, ServiceException, InvalidAttributesException, InvalidPasswordException {
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);

        AuthService authService = AuthServiceFactory.getAuthService(res.getString(
                    "auth.service"));
        
        authService.setPassword(this.login, StringUtils.defaultString(newPassword));
        this.setPassword(newPassword);
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        String result = "";
        if (StringUtils.isEmpty(this.lastName)) {
            if (StringUtils.isNotEmpty(this.firstName)) {
                result = this.firstName + " ";
            }
        } else {
            if (StringUtils.isEmpty(this.firstName)) {
                result = this.lastName + " ";
            } else {
                result = this.lastName + ", " + this.firstName + " ";
            }
        }
        return result;
    }
    
    public String getFullNameWithLogin(){
    	return getFullName() + "(" + getLogin() + ")";
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getBlocked() {
        return this.blocked;
    }
    
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescriptionPrefix() {
        String result = StringUtils.defaultString(description);
        
        if (result.length() > 60){
            result = result.substring(0, 57) + "...";
        }
        
        return result;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAttribute(String key, String value){
        this.attributes.put(key, value);
    }

    public String getAttribute(String key) throws com.manentia.commons.xml.XMLException {
        return (String) this.attributes.get(key);
    }
    
    public String getAttributesXML() throws ParserConfigurationException, FactoryConfigurationError{
    	return MiscUtils.serializeMapToXML(attributes);
    }

    public Collection getGroups() {
        return groups;
    }

    public void setGroups(Collection groups) {
        this.groups = groups;
    }

    public Collection getObjectsUsersRoles(){
        return objectsUsersRoles;
    }
    
    public void setObjectsUsersRoles(Collection objectUserRole){
        this.objectsUsersRoles = objectUserRole;
    }

    public void addObjectUserRole(ObjectUserRole objectUserRole) {
        if (objectUserRole != null) {
            if(!objectsUsersRoles.contains(objectUserRole)){
                this.objectsUsersRoles.add(objectUserRole);
            }
        }
    }

    public void removeObjectUserRole (ObjectUserRole objectUserRole){
        if (objectUserRole != null){
            this.objectsUsersRoles.remove(objectUserRole);
        }
    }

    public void addGroup(Group group) {
        if (group != null) {
            if(!groups.contains(group)){
                this.groups.add(group);
            }
        }
    }

    public void removeGroup (Group group){
        if (group != null){
            this.groups.remove(group);
        }
    }

    public String resetPassword()
        throws ServiceNotAvailableException, ServiceException {
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
        ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);

        AuthService authService = AuthServiceFactory.getAuthService(res.getString(
                    "auth.service"));
        
        this.setPassword(authService.resetPassword(this.login));
        
        String msg= StringUtils.replace(messages.getString("user.resetPassword.mail.body"), "<NEW_PASSWORD>", this.getPassword());
        String subject = messages.getString("user.resetPassword.mail.subject");
        
        try{
            org.manentia.kasai.util.MailUtil.send (subject, msg, this.getEmail());
        }catch (Exception e){
        	Log.write("Password was modified successfully (user: " + this.getLogin() + ") but email could not be sent", 
        			e, Log.ERROR, "resetPassword", User.class);        	
        }
        return this.getPassword();
    }

    public void validate() throws InvalidAttributesException{
    	Log.write("Enter", Log.INFO, "validate", User.class);
        
        if ((this.getLogin() == null) || (this.getLogin().length()==0)){
        	Log.write("Login was not specified", Log.WARN, "validate", User.class);
             
            throw new InvalidAttributesException(User.class.getName() + ".emptyLogin");
        }
        else if ((this.getEmail() == null) || (this.getEmail().length() < 5)){
        	Log.write("Email was not specified", Log.WARN, "validate", User.class);
             
            throw new InvalidAttributesException(User.class.getName() + ".emptyEmail");
        }
        
        Log.write("Exit", Log.INFO, "validate", User.class);
    }

    public boolean equals (java.lang.Object obj){
        boolean result = false;
        
        try{
            if (obj instanceof User){
                if (((User)obj).getLogin().equals (this.login)){
                    result = true;
                }
            }
        }
        catch (Exception e){
            result = false;
        }
        return result;
    }

    public boolean getSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }
    
    public String getObjectId() {
        return "/kasai/user/" + this.getLogin();
    }
}
