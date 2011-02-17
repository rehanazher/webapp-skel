package org.manentia.kasai;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.audit.AuditHandler;
import org.manentia.kasai.authobject.AuthObjectHandler;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.CannotAuditException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.exceptions.InvalidAttributesException;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.NotEnoughPermissionException;
import org.manentia.kasai.exceptions.NotFoundException;
import org.manentia.kasai.exceptions.ServiceException;
import org.manentia.kasai.exceptions.ServiceNotAvailableException;
import org.manentia.kasai.exceptions.UserBlockedException;
import org.manentia.kasai.group.GroupHandler;
import org.manentia.kasai.operative.OperativeHandler;
import org.manentia.kasai.role.RoleHandler;
import org.manentia.kasai.user.UserHandler;
import org.w3c.dom.Document;

import com.manentia.commons.CriticalException;
import com.manentia.commons.NonCriticalException;
import com.manentia.commons.audit.AuditBean;
import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLBean;
import com.manentia.commons.xml.XMLException;


/**
 * Class to provide authority and authorization functionalities
 *
 * @author fpena
 * 
 * (c) 2004 Koala Developers S.R.L.
 */
public class KasaiFacade {

    //~ Static variables / initialization ---------------------------------------------------------------------------

    /** Permission to read a group */
    static final String READ_GROUP = "kasai.group.read";

    /** Permission to delete a user from a group */
    static final String DELETE_USER_GROUP = "kasai.group.user.delete";

    /** Permission to add users to a group */
    static final String ADD_USER_TO_GROUP = "kasai.group.user.add";

    /** Permission to delete a group */
    static final String DELETE_GROUP = "kasai.group.delete";

    /** Permission to modify a group */
    static final String COMMIT_GROUP = "kasai.group.commit";

    /** Permission to block a group */
    static final String BLOCK_GROUP = "kasai.group.block";

    /** Permission to unblock a group */
    static final String UNBLOCK_GROUP = "kasai.group.unblock";

    /** Permission to read a user */
    static final String READ_USER = "kasai.user.read";

    /** Permission to delete a user */
    static final String DELETE_USER = "kasai.user.delete";

    /** Permission to modify a user */
    static final String COMMIT_USER = "kasai.user.commit";

    /** Permission to reset a user password*/
    static final String RESET_PASSWORD_USER = "kasai.user.resetpassword";

    /** Permission to block a user */
    static final String BLOCK_USER = "kasai.user.block";

    /** Permission to unblock a user */
    static final String UNBLOCK_USER = "kasai.user.unblock";

    /** Permission to modify a role */
    static final String COMMIT_ROLE = "kasai.role.commit";

    /** Permission to read a role */
    static final String READ_ROLE = "kasai.role.read";

    /** Permission to delete a role */
    static final String DELETE_ROLE = "kasai.role.delete";

    /** Permission to modify permissions of a object */
    static final String MODIFY_ACCESS = "kasai.object.modifyaccess";
    
    static final String LIST_AUDIT_ENTRIES = "kasai.audit.list";
    
    /** Singleton instance */
    private static KasaiFacade instance = null;

    //~ Constructors --------------------------------------------------------------------------------------------------

    /**
     * Returns an instance of KasaiFacade
     *
     * @return Instance of KasaiFacade
     *
     */
    public static synchronized KasaiFacade getInstance(){
        if (instance == null) {
            instance = new KasaiFacade();
        }

        return instance;
    }

    //~ Methods --------------------------------------------------------------------------------------------------------

    /**
     * Creates a new KasaiFacade object.
     */
    private KasaiFacade() {
    	Log.write("Enter", Log.INFO, "init", KasaiFacade.class);
    	Log.write("Exit", Log.INFO, "init", KasaiFacade.class);
    }

    /**
     * Add one operative to a specific role.
     *
     * @param loginUser User who is making the request
     * @param idOperative Operative identifier
     * @param role Role identifier
     * @param clientIP IP Address of whom is making the request Client IP address
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The operative or role does not exist
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing operation
     */
    public void addOperativeToRole(String loginUser, String idOperative, int role, String clientIP)
        throws DataAccessException, DoesntExistsException,NotEnoughPermissionException, 
            CannotAuditException {
        
    	Log.write("Enter (loginUser=" + StringUtils.defaultString(loginUser, "<null>") + ",idOperative=" +
            StringUtils.defaultString(idOperative, "<null>") + ",role=" + role, Log.INFO, "addOperativeToRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_ROLE, "/kasai/role/" + role);
            RoleHandler.getInstance().addOperativeToRole(idOperative, role);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;
            throw e;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 2;
            throw deE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;
            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idOperative", idOperative);
            transactionData.put("role", String.valueOf(role));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".addOperativeToRole", "/kasai/role/" + role, transactionData);
        }

        Log.write("Exit", Log.INFO, "addOperativeToRole", KasaiFacade.class);
    }

    /**
     * Add a user to a specific group
     *
     * @param loginUser User who is making the request
     * @param idGroup Destination group identifier
     * @param idUserToAdd User identifier
     * @param clientIP IP Address of whom is making the request IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The user or group does not exist
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Errors auditing operation
     * @throws XMLException 
     */
    public void addUserToGroup(String loginUser, String idGroup, String idUserToAdd, String clientIP)
        throws DataAccessException, DoesntExistsException, NotEnoughPermissionException, 
            CannotAuditException, XMLException {
        
    	Log.write("Enter(loginUser=" + StringUtils.defaultString(loginUser, "<null>") + ",idGroup=" +
            StringUtils.defaultString(idGroup, "<null>") + ",idUserToAdd=" +
            StringUtils.defaultString(idUserToAdd, "<null>") + ",clientIP=" +
            StringUtils.defaultString(clientIP, "<null>"), Log.INFO, "addUserToGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, ADD_USER_TO_GROUP, "/kasai/group/" + idGroup);

            GroupHandler.getInstance().addUserToGroup(idUserToAdd, idGroup);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlErrror";
            returnCode = 1;
            throw e;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 2;
            throw deE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;
            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idGroup", idGroup);
            transactionData.put("idUserToAdd", idUserToAdd);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".addUserToGroup", "/kasai/group/" + idGroup, transactionData);
        }
        
        Log.write("Exit", Log.INFO, "addUserToGroup", KasaiFacade.class);
        
    }

    /**
     * Method to block just the group, not the users. Users lost permissions assigned to them through the group.
     *
     * @param loginUser User who is making the request
     * @param idGroup Group identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException Group existing attributes are not correct
     * @throws NotEnoughPermissionException The user cannot execute this operative
     * @throws DoesntExistsException The group does not exist
     * @throws CannotAuditException Error auditing transaction
     * @throws XMLException 
     */
    public void blockGroup(String loginUser, String idGroup, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, 
            DoesntExistsException, CannotAuditException, XMLException {
        
    	Log.write("Enter", Log.INFO, "blockGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.BLOCK_GROUP, "/kasai/group/" + idGroup);

            Group group = GroupHandler.getInstance().read(idGroup);

            if (group == null) {
            	Log.write("Group doesn't exist (" + idGroup + ")", Log.WARN, "blockGroup", KasaiFacade.class);
            	
                throw new DoesntExistsException(KasaiFacade.class.getName() + "groupDoesntExist");
            }
            
            group.setBlocked(true);

            GroupHandler.getInstance().update(group);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;
            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;
            throw iaE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;
            throw nep;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 4;
            throw deE;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idGroup", idGroup);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".blockGroup", "/kasai/group/" + idGroup, transactionData);
        }

        Log.write("Exit", Log.INFO, "blockGroup", KasaiFacade.class);
    }

    /**
     * Method to block a specific user. After this, user is disabled to use the application.
     *
     * @param loginUser User who is making the request
     * @param idUserToBlock User identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException Existing user information is invalid
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws DoesntExistsException The selected user does not exist
     * @throws CannotAuditException Error auditing transaction
     * @throws XMLException 
     */
    public void blockUser(String loginUser, String idUserToBlock, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, 
            DoesntExistsException, CannotAuditException, XMLException {
        
    	Log.write("Enter", Log.INFO, "blockUser", KasaiFacade.class);
    	
        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.BLOCK_USER, "/kasai/user/" + idUserToBlock);

            User user = UserHandler.getInstance().read(idUserToBlock, true);

            if (user == null) {
            	Log.write("User doesn't exist (" + idUserToBlock + ")", Log.WARN, "blockUser", KasaiFacade.class);
            	
                throw new DoesntExistsException(KasaiFacade.class.getName() + "userDoesntExist");
            }
            
            user.setBlocked(true);

            UserHandler.getInstance().update(user);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;
            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;
            throw iaE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;
            throw nep;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 4;
            throw deE;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idUserToBlock", idUserToBlock);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".blockUser", "/kasai/user/" + idUserToBlock, transactionData);
        }

        Log.write("Exit", Log.INFO, "blockUser", KasaiFacade.class);        
    }

    /**
     * Method to change the user's password
     *
     * @param login User who is making the request
     * @param oldPassword Actual password
     * @param newPassword New password
     * @param confirmation Confirmation password
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException Existing user info or new password are not valid
     * @throws ServiceException Error changing password at the authentication service level
     * @throws ServiceNotAvailableException The authentication service is not available
     * @throws DoesntExistsException The user does not exists
     * @throws CannotAuditException Error auditing operation
     * @throws InvalidPasswordException The old password is not valid
     * @throws XMLException 
     */
    public void changePasswordUser(String login, String oldPassword, String newPassword, String confirmation,
        String clientIP) throws DataAccessException, InvalidAttributesException, ServiceException, ServiceNotAvailableException,
        DoesntExistsException, CannotAuditException, InvalidPasswordException, XMLException {
        
    	Log.write("Enter", Log.INFO, "changePasswordUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {

            User user = UserHandler.getInstance().read(login, true);

            if (user == null) {
            	Log.write("User doesn't exist (" + login + ")", Log.WARN, "changePasswordUser", KasaiFacade.class);
            	
                throw new DoesntExistsException(KasaiFacade.class.getName() + "userDoesntExist");
            }

            user.changePassword(oldPassword, newPassword, confirmation);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;
            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;
            throw iaE;
        } catch (ServiceException e) {
            raisedError = e.getMessage();
            returnCode = 3;
            throw e;
        } catch (ServiceNotAvailableException e) {
            raisedError = e.getMessage();
            returnCode = 4;

            throw e;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 5;
            throw deE;
        } catch (InvalidPasswordException ipe) {
            raisedError = ipe.getMessage();
            returnCode = 6;
            throw ipe;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            createAuditEntry(login, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".changePasswordUser", "/kasai/user/" + login, transactionData);
        }

        Log.write("Exit", Log.INFO, "changePasswordUser", KasaiFacade.class);        
    }

    /**
     * Method to verify if a user can do a specific operative over the object identified by object parameter
     *
     * @param login User who is making the request
     * @param operative Operative to run over the object
     * @param object Id of the object
     *
     * @return True if the user can do the operative over this object, false in other case
     *
     */
    public boolean checkOperative(String login, String operative, String object){
        
        return UserHandler.getInstance().checkOperative(login, operative, object);
    }

    /**
     * Method to verify user's password
     *
     * @param login User who is making the request
     * @param password User password
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotFoundException The user does not exist
     * @throws UserBlockedException The user is blocked
     * @throws InvalidPasswordException The password is not valid
     * @throws ServiceException The authentication service raised an error while validating the password
     * @throws ServiceNotAvailableException The authentication service is not available
     * @throws CannotAuditException Error auditing operation
     * @throws XMLException 
     */
    public void checkPasswordUser(String login, String password, String clientIP)
        throws DataAccessException, NotFoundException, UserBlockedException, InvalidPasswordException, 
        ServiceException, ServiceNotAvailableException, CannotAuditException, XMLException {

        Log.write("Enter(login=" + StringUtils.defaultString(login, "<null>") + ", password=" +
            ((password == null) ? "<null>" : "******") + ", clientIP=" + StringUtils.defaultString(clientIP, "<null>") +
            ")", Log.INFO, "checkPasswordUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            UserHandler.getInstance().checkPassword(login, password);
            
            Log.write("Exit", Log.INFO, "checkPasswordUser", KasaiFacade.class);             
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;
            throw e;
        } catch (NotFoundException nfe) {
            raisedError = nfe.getMessage();
            returnCode = 2;
            throw nfe;
        } catch (UserBlockedException ube) {
            raisedError = ube.getMessage();
            returnCode = 3;
            throw ube;
        } catch (InvalidPasswordException ipe) {
            raisedError = ipe.getMessage();
            returnCode = 5;
            throw ipe;
        } catch (ServiceException se) {
            raisedError = se.getMessage();
            returnCode = 7;
            throw se;
        } catch (ServiceNotAvailableException snae) {
        	Log.write("Error verifing user password", snae, Log.ERROR, "checkPasswordUser", KasaiFacade.class); 
        	
            raisedError = snae.getMessage();
            returnCode = 8;
            throw snae;
        } finally {
        	
        	login = StringUtils.isEmpty(login) ?  "guest" : login;
        	
            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("login", login);

            createAuditEntry(login, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".checkPasswordUser", "/kasai/user/" + login, transactionData);
        }
    }

    /**
     * Copy all permissions from one object to another.
     *
     * @param loginUser User who is making the request
     * @param sourceObject 
     * @param destinationObject
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException One (or both) of the objects does not exist
     */
    public void copyObjectRoles(String loginUser, String sourceObject, String destinationObject)
        throws DataAccessException, DoesntExistsException {
    	
    	Log.write("Enter", Log.INFO, "copyObjectRoles", KasaiFacade.class); 

        try {
            AuthObjectHandler.getInstance().copyPermissionsFromObject(sourceObject, destinationObject);
        } catch (DataAccessException e) {
            throw e;
        } catch (DoesntExistsException deE) {
            throw deE;
        }

        Log.write("Exit", Log.INFO, "copyObjectRoles", KasaiFacade.class);
    }

    /**
     * Audit an operation
     *
     * @param userId User who is making the request
     * @param returnCode Return code
     * @param errorDescription Error description
     * @param duration Duration in milliseconds
     * @param clientIP IP Address of whom is making the request
     * @param operation Operation executed
     * @param objectID Object involved in the operation
     * @param transactionData Extra data
     *
     * @throws CannotAuditException A severe error ocurred while writing the audit entry
     */
    public void createAuditEntry(String userId, int returnCode, String errorDescription, long duration,
        String clientIP, String operation, String objectID, Document transactionData)
        throws CannotAuditException {

        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
        boolean auditIsEnabled = res.getString("kasai.audit.enabled").equalsIgnoreCase("yes");

        if (auditIsEnabled) {
        	Log.write("Enter (userId=" + StringUtils.defaultString(userId, "<null>") + ", returnCode=" + returnCode +
                ", errorDescription=" + StringUtils.defaultString(errorDescription, "<null>") + ", duration=" +
                duration + ", clientIP=" + StringUtils.defaultString(clientIP, "<null>") + ", operation=" +
                StringUtils.defaultString(operation, "<null>") + ", objectID=" +
                StringUtils.defaultString(objectID, "<null>") + ", transactionData=" +
                ((transactionData == null) ? "<null>" : "<data>"), Log.INFO, "createAuditEntry", KasaiFacade.class);
        	
            AuditHandler.createEntry(userId, returnCode, errorDescription, duration, clientIP, operation, objectID,
                transactionData);

            Log.write("Exit", Log.INFO, "createAuditEntry", KasaiFacade.class);
        }
    }

    /**
     * Audits an operation
     *
     * @param userId User who is making the request
     * @param returnCode Return code
     * @param errorDescription Error description
     * @param duration Duration in milliseconds
     * @param clientIP IP Address of whom is making the request
     * @param operation Operation executed
     * @param objectID Object involved in the operation
     * @param transactionData Extra data
     *
     * @throws CannotAuditException A severe error ocurred while writing the audit entry
     */
    public void createAuditEntry(String userId, int returnCode, String errorDescription, long duration,
        String clientIP, String operation, String objectID, HashMap<String, String> transactionData)
        throws CannotAuditException {
    	
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
        boolean auditIsEnabled = res.getString("kasai.audit.enabled").equalsIgnoreCase("yes");

        if (auditIsEnabled) {
        	Log.write("Enter", Log.INFO, "createAuditEntry", KasaiFacade.class);

            Document transactionDoc = null;

            try {

                XMLBean auditXML = new XMLBean("TransactionData");
                String key;
                String value;

                for (Iterator<String> iter = transactionData.keySet().iterator(); iter.hasNext();) {
                    key = (String) iter.next();
                    value = (String) transactionData.get(key);
                    auditXML.setString(key, value);
                }

                transactionDoc = auditXML.getXML();
            } catch (XMLException xmlE) {
            	Log.write("XML error", xmlE, Log.ERROR, "createAuditEntry", KasaiFacade.class);
            	
                throw new CannotAuditException(KasaiFacade.class.getName() + ".xmlError", xmlE);
            }

            createAuditEntry(userId, returnCode, errorDescription, duration, clientIP, operation, objectID,
                transactionDoc);
            
            Log.write("Exit", Log.INFO, "createAuditEntry", KasaiFacade.class);
        }
        
        
    }

    public void createGroup(String loginUser, Group group, String clientIP) throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
    	NotEnoughPermissionException, CannotAuditException, CriticalException{
        
    	Log.write("Enter", Log.INFO, "createGroup", KasaiFacade.class);
    	
        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_GROUP, "/kasai/group/");

            GroupHandler.getInstance().create(group);

            this.createObject(loginUser, "/kasai/group/" + group.getId());
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (AlreadyExistsException aeE) {
            raisedError = aeE.getMessage();
            returnCode = 2;

            throw aeE;
        } catch (InvalidAttributesException e) {
            raisedError = e.getMessage();
            returnCode = 3;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 4;

            throw nep;
        } catch (CriticalException ce) {
            raisedError = ce.getMessage();
            returnCode = 5;

            throw ce;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("id", group.getId());
            transactionData.put("description", group.getDescription());
            transactionData.put("blocked", String.valueOf(group.getBlocked()));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".createGroup", "/kasai/group/" + group.getId(), transactionData);
        }

        Log.write("Exit", Log.INFO, "createGroup", KasaiFacade.class);
    }

        
    /**
     * Method to create a group
     *
     * @param loginUser User who is making the request
     * @param id Group Group Identifier
     * @param description Group description
     * @param blocked Specify if the group must be blocked or not
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws AlreadyExistsException A group with the given ID already exists
     * @throws InvalidAttributesException The attributes are not valid for a group
     * @throws NotEnoughPermissionException The user does not have enough permission to execute this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws CriticalException Severe error creating group
     */
    public void createGroup(String loginUser, String id, String description, boolean blocked, String clientIP)
        throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
        NotEnoughPermissionException, CannotAuditException, CriticalException {
    	
    	Group group = new Group();
    	group.setId(id);
    	group.setDescription(description);
    	group.setBlocked(blocked);
    	
    	createGroup(loginUser, group, clientIP);
    }
    
    /**
     * Method to register a new object in kasai. It Method assign to the loginUser the role specified in the properties file
     * with the property kasai.default.role
     *
     * @param loginUser User who is making the request
     * @param objectId Object identifier
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws CriticalException The object could be created but not its default permissions
     */
    public void createObject(String loginUser, String objectId)
        throws DataAccessException, CriticalException {
        
    	Log.write("Enter", Log.INFO, "createObject", KasaiFacade.class);
    	
        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);

        try {
            AuthObjectHandler.getInstance().create(objectId);
            AuthObjectHandler.getInstance().createObjectUserRole(objectId, loginUser, Integer.parseInt(res.getString("kasai.default.role")));
        } catch (DataAccessException e) {
            throw e;
        } catch (DoesntExistsException deE) {
            throw new CriticalException(deE);
        }

        Log.write("Exit", Log.INFO, "createObject", KasaiFacade.class);
    }

    /**
     * Method to assign permissions. This assign the role specified to the group over the object identified by objectId
     *
     * @param loginUser User who is making the request
     * @param objectId Object identifier
     * @param group Group identifier
     * @param role Role identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The object, group or role doesnt exists
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws XMLException 
     */
    public void createObjectGroupRole(String loginUser, String objectId, String group, int role,
        String clientIP) throws DataAccessException, DoesntExistsException, NotEnoughPermissionException,
        CannotAuditException, XMLException{
    	
    	Log.write("Enter (" + loginUser + "," + objectId + "," + group + "," + role + ")", Log.INFO, "createObjectGroupRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, MODIFY_ACCESS, objectId);

            AuthObjectHandler.getInstance().createObjectGroupRole(objectId, group, role);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 2;

            throw deE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("group", group);
            transactionData.put("role", String.valueOf(role));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".createObjectGroupRole", objectId, transactionData);
        }

        Log.write("Exit", Log.INFO, "createObjectGroupRole", KasaiFacade.class);
    }

    /**
     * Method to assign permissions. This assign the role specified to the user over the object identified by objectId
     *
     * @param objectId User who is making the request
     * @param user User identifier
     * @param role Role identifier
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The object, user or role doesnt exist
     * @throws XMLException 
     */
    public void createObjectUserRole(String objectId, String user, int role)
        throws DataAccessException, DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "createObjectUserRole", KasaiFacade.class);

        try {
            AuthObjectHandler.getInstance().createObjectUserRole(objectId, user, role);
        } catch (DataAccessException e) {
            throw e;
        } catch (DoesntExistsException deE) {
            throw deE;
        }

        Log.write("Exit", Log.INFO, "createObjectUserRole", KasaiFacade.class);
    }

    /**
     * Method to assign permissions. This assign the role specified to the user over the object identified by objectId
     *
     * @param loginUser User who is making the request
     * @param objectId Object identifier
     * @param user User identifier
     * @param role Role identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The user, object or role doesnt exist
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws XMLException 
     */
    public void createObjectUserRole(String loginUser, String objectId, String user, int role, String clientIP)
        throws DataAccessException, DoesntExistsException, NotEnoughPermissionException,
        CannotAuditException, XMLException{
        
    	Log.write("Enter", Log.INFO, "createObjectUserRole", KasaiFacade.class);
    	
        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, MODIFY_ACCESS, objectId);

            AuthObjectHandler.getInstance().createObjectUserRole(objectId, user, role);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;
            throw e;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 2;
            throw deE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;
            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("user", user);
            transactionData.put("role", String.valueOf(role));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".createObjectUserRole", objectId, transactionData);
        }

        Log.write("Exit", Log.INFO, "createObjectUserRole", KasaiFacade.class);
    }

    /**
     * Register an object in Kasai, and assign to the user the specified role (roleId) over an object (objectId)
     *
     * @param loginUser User who is making the request
     * @param objectId Object Identifier
     * @param roleId Role identifier
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The user, object or role doesnt exist
     * @throws XMLException 
     */
    public void createObjectWithRole(String loginUser, String objectId, int roleId)
        throws DataAccessException, DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "createObjectWithRole", KasaiFacade.class);

        try {
            AuthObjectHandler.getInstance().create(objectId);
            if (roleId != -1) {
                AuthObjectHandler.getInstance().createObjectUserRole(objectId, loginUser, roleId);
            }
        } catch (DataAccessException e) {
            throw e;
        } catch (DoesntExistsException deE) {
            throw deE;
        }

        Log.write("Exit", Log.INFO, "createObjectWithRole", KasaiFacade.class);
    }

    /**
     * Creates a role
     *
     * @param loginUser User who is making the request
     * @param name Name of the role to be created
     * @param description Description of the role to be created
     * @param operatives Operatives of the role to be created
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws CriticalException Severe errors like SQL error, IO Error, etc
     * @throws AlreadyExistsException A role with the given name already exists
     * @throws DoesntExistsException One of the given operatives does not exist
     * @throws InvalidAttributesException The attributes are not valid for a role
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public int createRole(String loginUser, String name, String description, String[] operatives, String clientIP)
        throws AlreadyExistsException, DoesntExistsException, DataAccessException, 
        InvalidAttributesException, NotEnoughPermissionException, CannotAuditException,
        CriticalException{
        
    	Log.write("Enter (name=" + name + ")", Log.INFO, "createRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;
        int roleId = -1;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_ROLE, "/kasai/role/");

            roleId = RoleHandler.getInstance().create(name, description, operatives);

            this.createObject(loginUser, "/kasai/role/" + roleId);
            
        } catch (AlreadyExistsException aeE) {
            raisedError = aeE.getMessage();
            returnCode = 1;

            throw aeE;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 2;

            throw deE;
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 3;

            throw e;
        } catch (InvalidAttributesException e) {
            raisedError = e.getMessage();
            returnCode = 4;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 5;

            throw nep;
        } catch (CriticalException ce) {
            raisedError = ce.getMessage();
            returnCode = 6;

            throw ce;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("name", name);
            transactionData.put("description", description);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".createRole", "/kasai/role/" + roleId, transactionData);
        }

        Log.write("Exit", Log.INFO, "createRole", KasaiFacade.class);
        
        return roleId;
    }

    /**
     * Creates a user
     *
     * @param loginUser User who is making the request
     * @param idUser Identifier of the user to be created
     * @param firstName First Name of the user to be created
     * @param lastName Last Name of the user to be created
     * @param email Email of the user to be created
     * @param blocked Specifies if the user must be blocked or not
     * @param description Description of the user to be created
     * @param superUser Specifies if the user must be superUser or not
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws CriticalException Severe errors like SQL error, IO Error, etc
     * @throws AlreadyExistsException A user with the given id already exists
     * @throws InvalidAttributesException The attributes are not valid for a user
     * @throws DoesntExistsException The user executing the transaction does not exist
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws InvalidPasswordException 
     */
    public void createUser(String loginUser, String idUser, String firstName, String lastName, String email,
        boolean blocked, String description, boolean superUser, String clientIP)
        throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
        DoesntExistsException, NotEnoughPermissionException, CannotAuditException,
        CriticalException, InvalidPasswordException {
    	
    	User user = new User();
    	user.setLogin(idUser);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	user.setBlocked(blocked);
    	user.setDescription(description);
    	user.setSuperUser(superUser);
    	
    	createUser(loginUser, user, null, clientIP);
    }

    /**
     * Creates a user
     *
     * @param loginUser User who is making the request
     * @param idUser Identifier of the user to be created
     * @param firstName First Name of the user to be created
     * @param lastName Last Name of the user to be created
     * @param email Email of the user to be created
     * @param blocked Specifies if the user must be blocked or not
     * @param description Description of the user to be created
     * @param superUser Specifies if the user must be superUser or not
     * @param password Password to assign to the user
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws CriticalException Severe errors like SQL error, IO Error, etc
     * @throws AlreadyExistsException A user with the given id already exists
     * @throws InvalidAttributesException The attributes are not valid for a user
     * @throws DoesntExistsException The user executing the transaction does not exist
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void createUser(String loginUser, String idUser, String firstName, String lastName, String email,
        boolean blocked, String description, boolean superUser, String password, String clientIP)
        throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
        DoesntExistsException, NotEnoughPermissionException, CannotAuditException,
        CriticalException, InvalidPasswordException {
    	
    	User user = new User();
    	user.setLogin(idUser);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	user.setBlocked(blocked);
    	user.setDescription(description);
    	user.setSuperUser(superUser);
    	
    	createUser(loginUser, user, password, clientIP);
    }
    
    public void createUser(String loginUser, User user, String password, String clientIP) throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
    	DoesntExistsException, NotEnoughPermissionException, CannotAuditException,
    	CriticalException, InvalidPasswordException {
        
    	Log.write("Enter", Log.INFO, "createUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_USER, "/kasai/user/");

            boolean sU = this.readUser(loginUser).getSuperUser();

            if (!sU) {
        		user.setSuperUser(false);
            }
            
            if (password == null){
            	UserHandler.getInstance().create(user);
            } else {
            	UserHandler.getInstance().create(user, password);
            }

            ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
            String group = res.getString("kasai.group.all");

            GroupHandler.getInstance().addUserToGroup(user.getLogin(), group);
            this.createObject(loginUser, "/kasai/user/" + user.getLogin());
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (AlreadyExistsException aeE) {
            raisedError = aeE.getMessage();
            returnCode = 2;

            throw aeE;
        } catch (InvalidAttributesException e) {
            raisedError = e.getMessage();
            returnCode = 3;

            throw e;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 4;

            throw deE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 5;

            throw nep;
        }  catch (CriticalException ce) {
            raisedError = ce.getMessage();
            returnCode = 6;

            throw ce;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idUser", user.getLogin());
            transactionData.put("firstName", user.getFirstName());
            transactionData.put("lastName", user.getLastName());
            transactionData.put("email", user.getEmail());
            transactionData.put("blocked", String.valueOf(user.getBlocked()));
            transactionData.put("description", user.getDescription());
            transactionData.put("superUser", String.valueOf(user.getSuperUser()));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".createUser", "/kasai/user/" + user.getLogin(), transactionData);
        }

        Log.write("Exit", Log.INFO, "createUser", KasaiFacade.class);
    }
    
    /**
     * Deletes a group
     *
     * @param loginUser User who is making the request
     * @param group Group identifier to be deleted
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteGroup(String loginUser, String group, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, DELETE_GROUP, "/kasai/group/" + group);

            GroupHandler.getInstance().delete(group);

            deleteObject("/kasai/group/" + group);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("group", group);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteGroup", "/kasai/group/" + group, transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteGroup", KasaiFacade.class);
    }

    /**
     * Unregister an object from Kasai
     *
     * @param objectId Object identifier to be deleted
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public void deleteObject(String objectId) throws DataAccessException {
    	Log.write("Enter", Log.INFO, "deleteObject", KasaiFacade.class);
    	
        try {
            AuthObjectHandler.getInstance().delete(objectId);
        } catch (DataAccessException e) {
            throw e;
        }

        Log.write("Exit", Log.INFO, "deleteObject", KasaiFacade.class);
    }

    /**
     * Remove an assigned role to the group over an object
     *
     * @param loginUser User who is making the request
     * @param id Identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteObjectGroupRole(String loginUser, int id, String clientIP)
        throws DataAccessException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteObjectGroupRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            AuthObjectHandler.getInstance().deleteObjectGroupRole(id);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("id", String.valueOf(id));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteObjectGroupRole", "", transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteObjectGroupRole", KasaiFacade.class);
    }

    /**
     * Remove all assigned roles to the user over an object
     *
     * @param loginUser User who is making the request
     * @param id Relation identifier to be deleted
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteObjectUserRole(String loginUser, int id, String clientIP)
        throws DataAccessException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);
        
        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            AuthObjectHandler.getInstance().deleteObjectUserRole(id);
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("id", String.valueOf(id));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteObjectUserRole", "", transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);
    }

    /**
     * Remove all assigned roles to the user over an object
     *
     * @param user User who is making the request
     * @param idObject Object identifier
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public void deleteObjectUserRole(String user, String idObject)
        throws DataAccessException {
        
    	Log.write("Enter", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);

        AuthObjectHandler.getInstance().deleteObjectUserRole(user, idObject);

        Log.write("Exit", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);
    }

    /**
     * Remove an assigned role to the user over an object
     *
     * @param loginUser User who is making the request
     * @param user User identifier
     * @param idObject Object identifier
     * @param role Role identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteObjectUserRole(String loginUser, String user, String idObject, int role,
        String clientIP) throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);
    	
        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, MODIFY_ACCESS, idObject);

            AuthObjectHandler.getInstance().deleteObjectUserRole(user, idObject, role);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("user", user);
            transactionData.put("role", String.valueOf(role));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteObjectUserRole", idObject, transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);
    }

    /**
     * Remove all assigned roles to the user over an object
     *
     * @param loginUser User who is making the request
     * @param user User identifier
     * @param idObject Object identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteObjectUserRole(String loginUser, String user, String idObject, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, MODIFY_ACCESS, idObject);

            AuthObjectHandler.getInstance().deleteObjectUserRole(user, idObject);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("user", user);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteObjectUserRole", idObject, transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteObjectUserRole", KasaiFacade.class);
    }

    /**
     * Delete a role
     *
     * @param loginUser User who is making the request
     * @param role Role identifier to be deleted
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteRole(String loginUser, int role, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, DELETE_ROLE, "/kasai/role/" + role);

            this.deleteObject("/kasai/role/" + role);
            RoleHandler.getInstance().delete(role);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("role", String.valueOf(role));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteObjectUserRole", "/kasai/role/" + role, transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteRole", KasaiFacade.class);
    }

    /**
     * Delete a user
     *
     * @param loginUser User who is making the request
     * @param idUserToDelete User identifier to be deleted
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void deleteUser(String loginUser, String idUserToDelete, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "deleteUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, DELETE_USER, "/kasai/user/" + idUserToDelete);

            UserHandler.getInstance().delete(idUserToDelete);

            this.deleteObject("/kasai/user/" + idUserToDelete);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idUserToDelete", idUserToDelete);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".deleteUser", "/kasai/user/" + idUserToDelete, transactionData);
        }

        Log.write("Exit", Log.INFO, "deleteUser", KasaiFacade.class);
    }

    /**
     * Method to verify if a specific user is part of a specific group
     *
     * @param login User who is making the request 
     * @param userId Specific user
     * @param groupId Specific group
     *
     * @return true if the userId is part of a groupId
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc Severe error (SQL errors, IO errors, etc)
     * @throws XMLException 
     */
    public boolean isUserInGroup(String login, String userId, String groupId)
        throws DataAccessException, XMLException {
        
    	Log.write("Enter", Log.INFO, "isUserInGroup", KasaiFacade.class);

        boolean result = false;
        Collection<User> users = null;

        if ((StringUtils.isNotEmpty(userId)) && (StringUtils.isNotEmpty(groupId))) {
            users = UserHandler.getInstance().list(userId, null, null, null, -1, null, groupId);
        }

        result = (users != null) && (users.size() > 0);

        Log.write("Exit", Log.INFO, "isUserInGroup", KasaiFacade.class);

        return result;
    }

    public Collection<AuditBean> listAuditEntries(String loginUser,
    									java.util.Date dateFrom,
                                        java.util.Date dateTo,
                                        java.lang.String user,
                                        java.lang.String operation, String clientIP) 
    		throws DataAccessException, NonCriticalException, CannotAuditException{
 
    	Log.write("Enter", Log.INFO, "listAuditEntries", KasaiFacade.class);
    	
    	long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;
        Collection<AuditBean> result = null;
        
    	try {
	    	this.validateOperative(loginUser, KasaiFacade.LIST_AUDIT_ENTRIES, "/kasai");
	    	
	    	result = AuditHandler.listEntries(dateFrom, dateTo, user, operation);	
	    } catch (DataAccessException e) {
	        raisedError = KasaiFacade.class.getName() + ".sqlError";
	        returnCode = 1;
	
	        throw e;
	    } catch (InvalidAttributesException iaE) {
	        raisedError = iaE.getMessage();
	        returnCode = 2;
	
	        throw iaE;
	    } catch (NotEnoughPermissionException nep) {
	        raisedError = nep.getMessage();
	        returnCode = 3;
	
	        throw nep;
	    } catch (DoesntExistsException dee) {
	        raisedError = dee.getMessage();
	        returnCode = 4;
	
	        throw dee;
	    } catch (NonCriticalException e) {
	    	raisedError = e.getMessage();
	        returnCode = 5;
	
	        throw e;
		} finally {
	
	        HashMap<String, String> transactionData = new HashMap<String, String>();
	        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        
	        if (dateFrom!=null){
	        	transactionData.put("dateFrom", format.format(dateFrom));
	        }
	        if (dateTo!=null){
	        	transactionData.put("dateTo", format.format(dateTo));
	        }
	        transactionData.put("user", user);
	        transactionData.put("operation", operation);
	
	        createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
	            KasaiFacade.class.getName() + ".listAuditEntries", "/kasai", transactionData);
	    }
    	
    	Log.write("Exit", Log.INFO, "listAuditEntries", KasaiFacade.class);
    	
    	return result;
    }

    public String[] listGroupMembers(String groupId) throws DataAccessException{
    	String[] members = null;

        Log.write("Enter", Log.INFO, "listGroupMembers", KasaiFacade.class);

        members = UserHandler.getInstance().listUsernames(groupId);

        Log.write("Exit", Log.INFO, "listGroupMembers", KasaiFacade.class);

        return members;
    }

    /**
     * List groups
     *
     * @param actualUser User making the request
     * @param idGroup It filters the list by group identifier
     * @param description It filters the list by group description
     * @param blocked It filters the list by blocked attribute
     * @param system It filters the list by system attribute
     *
     * @return Groups that satisfy given filters.
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public List<Group> listGroups(String actualUser, String idGroup, String description, int blocked, int system)
        throws DataAccessException, XMLException {

    	List<Group> groups = null;

        Log.write("Enter", Log.INFO, "listGroups", KasaiFacade.class);

        groups = GroupHandler.getInstance().list(idGroup, description, blocked, system, null);

        Log.write("Exit", Log.INFO, "listGroups", KasaiFacade.class);

        return groups;
    }

    /**
     * List all the groups a user is member of
     *
     * @param user user identifier
     *
     * @return A collection containing the groups
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public Collection<Group> listGroupsFromUser(String user) throws DataAccessException, XMLException {

        Collection<Group> groups = new ArrayList();

        Log.write("Enter", Log.INFO, "listGroupsFromUser", KasaiFacade.class);

        groups = GroupHandler.getInstance().list(null, null, -1, -1, user);

        Log.write("Exit", Log.INFO, "listGroupsFromUser", KasaiFacade.class);

        return groups;
    }

    /**
     * List all the groups a user is member of
     *
     * @param loginUser User who is making the request
     * @param user User identifier
     *
     * @return A collection containing groups
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws XMLException 
     */
    public Collection<Group> listGroupsFromUser(String loginUser, String user)
        throws DataAccessException, NotEnoughPermissionException, XMLException {

        Collection<Group> groups = new ArrayList();

        Log.write("Enter", Log.INFO, "listGroupsFromUser", KasaiFacade.class);

        if (!user.equals(loginUser)) {
            this.validateOperative(loginUser, KasaiFacade.READ_USER, "/kasai/user/" + user);
        }

        groups = GroupHandler.getInstance().list(null, null, -1, -1, user);

        Log.write("Exit", Log.INFO, "listGroupsFromUser", KasaiFacade.class);

        return groups;
    }

    /**
     * List groups that have a given operative assigned over a specific object.
     *
     * @param operative Operative
     * @param object Object identifier
     *
     * @return A collection of groups
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public Collection<Group> listGroupsOperativeCollection(String operative, String object)
        throws DataAccessException, XMLException {

        Collection<Group> groups = null;

        Log.write("Enter", Log.INFO, "listGroupsOperativeCollection", KasaiFacade.class);

        groups = OperativeHandler.getInstance().listGroupsOperative(operative, object);

        Log.write("Exit", Log.INFO, "listGroupsOperativeCollection", KasaiFacade.class);

        return groups;
    }

    /**
     * List assigned permissions over a given object
     *
     * @param loginUser User who is making the request
     * @param idObject Object identifier
     *
     * @return 
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public Collection listObjectRoles(String loginUser, String idObject)
        throws DataAccessException {
        
    	Log.write("Enter", Log.INFO, "listObjectRoles", KasaiFacade.class);

        Collection list = new ArrayList();

        list.addAll(AuthObjectHandler.getInstance().listObjectGroupsRoles(idObject));
        list.addAll(AuthObjectHandler.getInstance().listObjectUsersRoles(idObject));
        
        Log.write("Exit", Log.INFO, "listObjectRoles", KasaiFacade.class);

        return list;
    }

    /**
     * List all operatives
     *
     * @param loginUser User who is making the request
     *
     * @return Operatives
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public Collection<Operative> listOperatives(String loginUser)
        throws DataAccessException {
        
    	Log.write("Enter", Log.INFO, "listOperatives", KasaiFacade.class);

        Collection<Operative> operatives = null;

        operatives = OperativeHandler.getInstance().list(null);

        Log.write("Exit", Log.INFO, "listOperatives", KasaiFacade.class);

        return operatives;
    }

    /**
     * List all operatives for a given role
     *
     * @param loginUser User who is making the request
     * @param role Role Identifier
     *
     * @return 
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public Collection<Operative> listOperativesFromRole(String loginUser, int role)
        throws DataAccessException {
        
    	Log.write("Enter", Log.INFO, "listOperativesFromRole", KasaiFacade.class);

        Collection<Operative> list = new ArrayList<Operative>();

        list = RoleHandler.getInstance().listOperativesFromRole(role, "");

        Log.write("Exit", Log.INFO, "listOperativesFromRole", KasaiFacade.class);

        return list;
    }

    /**
     * List operatives that are not part of the role
     *
     * @param loginUser User who is making the request
     * @param role Role identifier
     *
     * @return
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public Collection<Operative> listOperativesNotInRole(String loginUser, int role)
        throws DataAccessException {
        
    	Log.write("Enter", Log.INFO, "listOperativesNotInRole", KasaiFacade.class);

        Collection<Operative> list = new ArrayList<Operative>();

        list = RoleHandler.getInstance().listOperativesNotInRole(role);

        Log.write("Exit", Log.INFO, "listOperativesNotInRole", KasaiFacade.class);

        return list;
    }
    
    /**
     * List all roles
     *
     * @param loginUser User who is making the request
     * @param name It filters the list by the role name
     *
     * @return List of roles
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public List<Role> listRoles(String loginUser, String name)
        throws DataAccessException {
        
    	Log.write("Enter", Log.INFO, "listRoles", KasaiFacade.class);

    	List<Role> roles = null;

        roles = RoleHandler.getInstance().list(name, false);

        Log.write("Exit", Log.INFO, "listRoles", KasaiFacade.class);

        return roles;
    }

    
    public String[] listUsernames() throws DataAccessException{
    	Log.write("Enter", Log.INFO, "listUsernames", KasaiFacade.class);

        String[] users = UserHandler.getInstance().listUsernames();

        Log.write("Exit", Log.INFO, "listUsernames", KasaiFacade.class);

        return users;
    }

    /**
     * List users
     *
     * @param loginUser User who is making the request
     * @param login It filters the list by the user login
     * @param firstName It filters the list by the user first name
     * @param lastName It filters the list by the user last name
     * @param email It filters the list by the user email
     * @param blocked It filters the list by the block attribute
     * @param description It filters the list by the user description
     * @param group It filters the list by a specific group that the user has to belong to
     *
     * @return List of users
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public List<User> listUsers(String loginUser, String login, String firstName, String lastName, String email,
        int blocked, String description, String group)
        throws DataAccessException, XMLException {

        List<User> users = null;

        Log.write("Enter", Log.INFO, "listUsers", KasaiFacade.class);

        users = UserHandler.getInstance().list(login, firstName, lastName, email, blocked, description, group);

        Log.write("Exit", Log.INFO, "listUsers", KasaiFacade.class);

        return users;
    }
    
    /**
     * List group's users without checking permission
     *
     * @param group Group Identifier
     *
     * @return List of users
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public Collection<User> listUsersFromGroup(String group)
        throws DataAccessException, XMLException {

        Collection<User> users = null;

        Log.write("Enter", Log.INFO, "listUsersFromGroup", KasaiFacade.class);

        users = UserHandler.getInstance().list(null, null, null, null, -1, null, group);

        Log.write("Exit", Log.INFO, "listUsersFromGroup", KasaiFacade.class);

        return users;
    }

    /**
     * List group's users checking permission
     *
     * @param loginUser User who is making the request
     * @param group Group identifier
     *
     * @return List of users
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public Collection<User> listUsersFromGroup(String loginUser, String group)
        throws DataAccessException, XMLException {

        Collection<User> users = null;

        Log.write("Enter", Log.INFO, "listUsersFromGroup", KasaiFacade.class);

        users = UserHandler.getInstance().list(null, null, null, null, -1, null, group);

        Log.write("Exit", Log.INFO, "listUsersFromGroup", KasaiFacade.class);

        return users;
    }

    /**
     * List users that aren't part of a group
     *
     * @param loginUser User who is making the request
     * @param group Group identifier
     *
     * @return List of users
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public Collection<User> listUsersNotInGroup(String loginUser, String group)
        throws DataAccessException, XMLException {

        Collection<User> aux = null;

        Log.write("Enter", Log.INFO, "listUsersNotInGroup", KasaiFacade.class);

        ArrayList<User> users = new ArrayList<User>();

        aux = GroupHandler.getInstance().listUsersNotInGroup(group);

        for (Iterator<User> iter = aux.iterator(); iter.hasNext();) {

            User u = (User) iter.next();

            try {
                this.validateOperative(loginUser, KasaiFacade.READ_USER, "/kasai/user/" + u.getLogin());
                users.add(u);
            } catch (Exception e) {}
        }

        Log.write("Exit", Log.INFO, "listUsersNotInGroup", KasaiFacade.class);

        return users;
    }

    /**
     * List users that have a given operative assigned over a specific object.
     *
     * @param operative Operative identifier
     * @param object Object identifier
     *
     * @return A collection of users
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws XMLException 
     */
    public Collection<User> listUsersOperative(String operative, String object)
        throws DataAccessException, XMLException {

        Collection<User> users = null;

        Log.write("Enter", Log.INFO, "listUsersOperative", KasaiFacade.class);
        
        users = OperativeHandler.getInstance().listUsersOperative(operative, object);

        Log.write("Exit", Log.INFO, "listUsersOperative", KasaiFacade.class);

        return users;
    }

    /**
     * Modify a role
     *
     * @param loginUser User who is making the request
     * @param role Role identifier
     * @param name New name of the role to be modified
     * @param description New description of the role to be modified
     * @param operatives New operatives of the role to be modified
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException The new attributes are not valid for a role
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void modifyRole(String loginUser, int role, String name, String description, String[] operatives,
        String clientIP) throws DataAccessException, InvalidAttributesException,
        NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "modifyRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_ROLE, "/kasai/role/" + role);

            RoleHandler.getInstance().update(role, name, description, operatives);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (InvalidAttributesException e) {
            raisedError = e.getMessage();
            returnCode = 2;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("role", String.valueOf(role));
            transactionData.put("name", name);
            transactionData.put("description", description);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".modifyRole", "/kasai/role/" + role, transactionData);
        }

        Log.write("Exit", Log.INFO, "modifyRole", KasaiFacade.class);
    }

    /**
     * Read a group without checking permission
     *
     * @param group Group identifier
     *
     * @return Group identified by group parameter
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The group doesnt exist
     * @throws XMLException 
     */
    public Group readGroup(String group) throws DataAccessException, DoesntExistsException, XMLException {

        Group g = null;

        Log.write("Enter", Log.INFO, "readGroup", KasaiFacade.class);

        g = GroupHandler.getInstance().read(group);

        if (g == null) {
        	Log.write("Group doesn't exist", Log.WARN, "readGroup", KasaiFacade.class);
            
            throw new DoesntExistsException(KasaiFacade.class.getName() + ".groupDoesntExist");
        }

        Log.write("Exit", Log.INFO, "readGroup", KasaiFacade.class);

        return g;
    }

    /**
     * Read a group checking permission and auditing the transaction
     *
     * @param loginUser User who is making the request
     * @param group Group identifier to be readed
     * @param clientIP IP Address of whom is making the request
     *
     * @return Group indentified by group parameter or null if it doesn't exist
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws DoesntExistsException The group does not exist
     * @throws XMLException 
     */
    public Group readGroup(String loginUser, String group, String clientIP)
        throws NotEnoughPermissionException, CannotAuditException, DataAccessException, DoesntExistsException, XMLException {
    	
    	Log.write("Enter", Log.INFO, "readGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        Group g = null;

        try {
            this.validateOperative(loginUser, KasaiFacade.READ_GROUP, "/kasai/group/" + group);

            g = this.readGroup(group);
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } catch (DoesntExistsException deE) {
            raisedError = deE.getMessage();
            returnCode = 4;

            throw deE;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".readGroup", "/kasai/group/" + group, transactionData);
        }

        Log.write("Exit", Log.INFO, "readGroup", KasaiFacade.class);

        return g;
    }

    /**
     * Reads a role without checking permission
     * 
     * @param role Role identifier
     *
     * @return An Object Role object representing the object role with the given role id
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public Role readRole(int role) throws DataAccessException {

        Role r = null;

        Log.write("Enter", Log.INFO, "readRole", KasaiFacade.class);
        
        r = RoleHandler.getInstance().read(role);
        
        Log.write("Exit", Log.INFO, "readRole", KasaiFacade.class);

        return r;
    }

    /**
     * Read a role
     *
     * @param loginUser User who is making the request
     * @param role Role identifier
     *
     * @return Role identified by role parameter or null if the role doesnt' exist
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     */
    public Role readRole(String loginUser, int role) throws DataAccessException {

        Role r;

        Log.write("Enter", Log.INFO, "readRole", KasaiFacade.class);

        r = readRole(role);

        Log.write("Exit", Log.INFO, "readRole", KasaiFacade.class);

        return r;
    }

    /**
     * Read a user without checking permission
     *
     * @param login User who is making the request
     *
     * @return User identified by login parameter or null if the user doesn't exist
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws DoesntExistsException The user doesnt exist
     * @throws XMLException 
     */
    public User readUser(String login) throws DataAccessException, DoesntExistsException, XMLException {

        User user = null;

        Log.write("Enter", Log.INFO, "readUser", KasaiFacade.class);
        
        user = UserHandler.getInstance().read(login, true);

        if (user == null) {
        	Log.write("User doesn't exist", Log.WARN, "readUser", KasaiFacade.class);
        	
            throw new DoesntExistsException(KasaiFacade.class.getName() + ".userDoesntExist");
        }

        Log.write("Exit", Log.INFO, "readUser", KasaiFacade.class);

        return user;
    }

    /**
     * Read a user checking permission and auditing the transaction.
     *
     * @param loginUser User who is making the request
     * @param login User login
     * @param clientIP IP Address of whom is making the request
     *
     * @return User identified by login parameter or null if the user doesn't exist
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws DoesntExistsException The user doesnt exist
     * @throws XMLException 
     */
    public User readUser(String loginUser, String login, String clientIP)
        throws NotEnoughPermissionException, CannotAuditException, DataAccessException,DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "readUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        User user = null;

        if (!loginUser.equals(login)) {
            try {
                this.validateOperative(loginUser, KasaiFacade.READ_USER, "/kasai/user/" + login);

                user = this.readUser(login);
            } catch (NotEnoughPermissionException nep) {
                raisedError = nep.getMessage();
                returnCode = 3;

                throw nep;
            } catch (DoesntExistsException dee) {
                raisedError = dee.getMessage();
                returnCode = 4;

                throw dee;
            } finally {

                HashMap<String, String> transactionData = new HashMap<String, String>();

                createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime),
                    clientIP, KasaiFacade.class.getName() + ".readUser", "/kasai/user/" + login, transactionData);
            }
        } else {
            user = this.readUser(login);
        }

        Log.write("Exit", Log.INFO, "readUser", KasaiFacade.class);

        return user;
    }

    /**
     * Reset the user password and send it to his email.
     *
     * @param loginUser User who is making the request
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws ServiceException Severe error returned from the authentication service
     * @throws ServiceNotAvailableException The configured authentication service is not available
     * @throws DoesntExistsException The user doesnt exist
     * @throws CannotAuditException Error auditing transaction
     * @throws XMLException 
     */
    public void remindPasswordUser(String loginUser, String clientIP)
        throws ServiceException, ServiceNotAvailableException, DataAccessException, DoesntExistsException, 
        CannotAuditException, XMLException {
        
    	Log.write("Enter", Log.INFO, "remindPasswordUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            User user = this.readUser(loginUser);
            
            user.resetPassword();
        } catch (ServiceException e) {
            raisedError = e.getMessage();
            returnCode = 1;

            throw e;
        } catch (DoesntExistsException e) {
            raisedError = e.getMessage();
            returnCode = 3;

            throw e;
        } catch (ServiceNotAvailableException e) {
            raisedError = e.getMessage();
            returnCode = 2;

            throw e;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".remindPasswordUser", "/kasai/user/" + loginUser, transactionData);
        }
        
        Log.write("Exit", Log.INFO, "remindPasswordUser", KasaiFacade.class);
    }

    /**
     * Remove an operative from a role
     *
     * @param loginUser User who is making the request
     * @param idOperative Operative identifier
     * @param role Role identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void removeOperativeFromRole(String loginUser, String idOperative, int role, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "removeOperativeToRole", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_ROLE, "/kasai/role/" + role);

            RoleHandler.getInstance().deleteOperativeFromRole(idOperative, role);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idOperative", idOperative);
            transactionData.put("role", String.valueOf(role));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".removeOperativeFromRole", "/kasai/role/" + role, transactionData);
        }

        Log.write("Exit", Log.INFO, "removeOperativeToRole", KasaiFacade.class);
    }

    /**
     * Remove a user from a group
     *
     * @param loginUser User who is making the request
     * @param idGroup Group identifier
     * @param login User login to be removed
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     */
    public void removeUserFromGroup(String loginUser, String idGroup, String login, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
    	Log.write("Enter", Log.INFO, "removeUserFromGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.DELETE_USER_GROUP, "/kasai/group/" + idGroup);

            GroupHandler.getInstance().deleteUserFromGroup(login, idGroup);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 2;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idGroup", idGroup);
            transactionData.put("login", login);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".removeUserFromGroup", "/kasai/group/" + idGroup, transactionData);
        }

        Log.write("Exit", Log.INFO, "removeUserFromGroup", KasaiFacade.class);
    }

    /**
     * Reset the user account password
     *
     * @param actualUser User who is making the request
     * @param login is the login
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws ServiceException Severe error returned from the authentication service
     * @throws ServiceNotAvailableException The configured authentication service is not available
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws DoesntExistsException The user doesnt exist
     * @throws XMLException 
     */
    public void resetPasswordUser(String actualUser, String login, String clientIP)
        throws ServiceException, ServiceNotAvailableException, NotEnoughPermissionException, 
        CannotAuditException, DataAccessException, DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "resetPasswordUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(actualUser, RESET_PASSWORD_USER, "/kasai/user/" + login);

            User user = this.readUser(login);

            user.resetPassword();
        } catch (ServiceException e) {
            raisedError = e.getMessage();
            returnCode = 1;

            throw e;
        } catch (ServiceNotAvailableException e) {
            raisedError = e.getMessage();
            returnCode = 2;

            throw e;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } catch (DataAccessException dae) {
            raisedError = dae.getMessage();
            returnCode = 4;

            throw dae;
        } catch (DoesntExistsException dee) {
            raisedError = dee.getMessage();
            returnCode = 5;

            throw dee;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("login", login);

            createAuditEntry(actualUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".resetPasswordUser", "/kasai/user/" + login, transactionData);
        }
        
        Log.write("Exit", Log.INFO, "resetPasswordUser", KasaiFacade.class);
    }

    /**
     * Unblock a group
     *
     * @param loginUser User who is making the request
     * @param idGroup group identifier
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException The existing group attributes are not valid
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws DoesntExistsException The group does not exist
     * @throws XMLException 
     */
    public void unblockGroup(String loginUser, String idGroup, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, 
        CannotAuditException, DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "blockGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.UNBLOCK_GROUP, "/kasai/group/" + idGroup);

            Group group = this.readGroup(idGroup);
            group.setBlocked(false);

            GroupHandler.getInstance().update(group);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;

            throw iaE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        }  catch (DoesntExistsException e) {
            raisedError = e.getMessage(0);
            returnCode = 4;

            throw e;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("idGroup", idGroup);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".unblockGroup", "/kasai/group/" + idGroup, transactionData);
        }

        Log.write("Exit", Log.INFO, "blockGroup", KasaiFacade.class);
    }
    
    /**
     * Unblock a user
     *
     * @param loginUser User who is making the request
     * @param login user identifier to unblock
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException The existing user attributes are not valid
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws DoesntExistsException The user does not exist
     * @throws XMLException 
     */
    public void unblockUser(String loginUser, String login, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException,
        CannotAuditException, DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "unblockUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.UNBLOCK_USER, "/kasai/user/" + login);

            User user = this.readUser(login);
            user.setBlocked(false);

            UserHandler.getInstance().update(user);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;

            throw iaE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } catch (DoesntExistsException dee) {
            raisedError = dee.getMessage();
            returnCode = 4;

            throw dee;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("login", login);

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".unblockUser", "/kasai/user/" + login, transactionData);
        }

        Log.write("Exit", Log.INFO, "unblockUser", KasaiFacade.class);
    }


    public void updateGroup(String loginUser, Group group, String[] members, String clientIP) throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException,
    CannotAuditException, DoesntExistsException, XMLException {
        
    	Log.write("Enter", Log.INFO, "updateGroup", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_GROUP, "/kasai/group/" + group.getId());

            GroupHandler.getInstance().update(group, members);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;

            throw iaE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("id", group.getId());
            transactionData.put("description", group.getDescription());
            transactionData.put("blocked", String.valueOf(group.getBlocked()));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".updateGroup", "/kasai/group/" + group.getId(), transactionData);
        }

        Log.write("Exit", Log.INFO, "updateGroup", KasaiFacade.class);
    }
    
    /**
     * Modify a group
     *
     * @param loginUser User who is making the request
     * @param id group identifier
     * @param description description group
     * @param blocked Specifies if the group must be blocked or not
     * @param members Members of the group
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException The new group attributes are not valid
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws XMLException 
     * @throws DoesntExistsException 
     */
    public void updateGroup(String loginUser, String id, String description, boolean blocked, String[] members,
        String clientIP) throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException,
        CannotAuditException, DoesntExistsException, XMLException {
    	
    	Group group = this.readGroup(id);
    	
    	group.setDescription(description);
    	group.setBlocked(blocked);
    	
    	updateGroup(loginUser, group, members, clientIP);
    }

    /**
     * Update User Information
     *
     * @param loginUser User who is making the request
     * @param login identifier of the user
     * @param firstName First Name of the user
     * @param lastName Last Name of the user
     * @param email email of the user
     * @param blocked Specify if the user must be blocked or not
     * @param description User description
     * @param superUser Specify if the user is super user.
     * @param clientIP IP Address of whom is making the request
     *
     * @throws DataAccessException Severe errors like SQL error, IO Error, etc
     * @throws InvalidAttributesException The new user attributes are not valid
     * @throws NotEnoughPermissionException The user cannot perform this operation
     * @throws CannotAuditException Error auditing transaction
     * @throws DoesntExistsException The user does not exist
     * @throws XMLException 
     */
    public void updateUser(String loginUser, String login, String firstName, String lastName, String email,
        boolean blocked, String description, boolean superUser, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, CannotAuditException,
        DoesntExistsException, XMLException{
    	
    	User user = this.readUser(login);
    	user.setLogin(login);
    	user.setFirstName(firstName);
    	user.setLastName(lastName);
    	user.setEmail(email);
    	user.setBlocked(blocked);
    	user.setDescription(description);
    	user.setSuperUser(superUser);
    	
    	updateUser(loginUser, user, clientIP);
    }

    public void updateUser(String loginUser, User user, String clientIP) throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, CannotAuditException,
    	DoesntExistsException, XMLException{
        
    	Log.write("Enter", Log.INFO, "updateUser", KasaiFacade.class);

        long startTime = System.currentTimeMillis();
        String raisedError = null;
        int returnCode = 0;

        try {
            this.validateOperative(loginUser, KasaiFacade.COMMIT_USER, "/kasai/user/" + user.getLogin());

            boolean sU = this.readUser(loginUser).getSuperUser();
            
            if (!sU){
            	if (user.getSuperUser()){
            		user.setSuperUser(this.readUser(user.getLogin()).getSuperUser());
            	}
            }

            UserHandler.getInstance().update(user);
        } catch (DataAccessException e) {
            raisedError = KasaiFacade.class.getName() + ".sqlError";
            returnCode = 1;

            throw e;
        } catch (InvalidAttributesException iaE) {
            raisedError = iaE.getMessage();
            returnCode = 2;

            throw iaE;
        } catch (NotEnoughPermissionException nep) {
            raisedError = nep.getMessage();
            returnCode = 3;

            throw nep;
        } catch (DoesntExistsException dee) {
            raisedError = dee.getMessage();
            returnCode = 4;

            throw dee;
        } finally {

            HashMap<String, String> transactionData = new HashMap<String, String>();

            transactionData.put("login", user.getLogin());
            transactionData.put("firstName", user.getFirstName());
            transactionData.put("lastName", user.getLastName());
            transactionData.put("email", user.getEmail());
            transactionData.put("blocked", String.valueOf(user.getBlocked()));
            transactionData.put("description", user.getDescription());
            transactionData.put("superUser", String.valueOf(user.getSuperUser()));

            createAuditEntry(loginUser, returnCode, raisedError, (System.currentTimeMillis() - startTime), clientIP,
                KasaiFacade.class.getName() + ".updateUser", "/kasai/user/" + user.getLogin(), transactionData);
        }

        Log.write("Exit", Log.INFO, "updateUser", KasaiFacade.class);
    }
    
    /**
     * Verify if the user can execute the operative over the obejct
     *
     * @param user User who is making the request
     * @param operative Operative identifier
     * @param object Object identifier
     *
     * @throws NotEnoughPermissionException The user cannot execute the operation 
     */
    public void validateOperative(String user, String operative, String object)
        throws NotEnoughPermissionException {
        
    	Log.write("Enter (user=" + user + ", operative=" + operative + ", object=" + object + ")", 
    			Log.INFO, "validateOperative", KasaiFacade.class);

        if (!UserHandler.getInstance().checkOperative(user, operative, object)) {
        	Log.write("Request was denied(User=" + user + ", operative=" + operative + ", object=" + object + ")", 
        			Log.WARN, "validateOperative", KasaiFacade.class);
        	
            throw new NotEnoughPermissionException(KasaiFacade.class.getName() + ".deny");
        }

        Log.write("Exit", Log.INFO, "validateOperative", KasaiFacade.class);
    }
}
