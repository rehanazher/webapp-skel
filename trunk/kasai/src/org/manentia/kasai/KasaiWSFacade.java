import com.manentia.commons.CriticalException;
import com.manentia.commons.NonCriticalException;
import java.util.Collection;
import java.util.HashMap;
import org.manentia.kasai.Role;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.*;
import org.manentia.kasai.*;
import org.w3c.dom.Document;


/**
 * WebService access facade for Kasai
 *
 * @author rzuasti
 * 
 * (c) 2005 Manentia Software
 */
public class KasaiWSFacade {


    //~ Constructors --------------------------------------------------------------------------------------------------

    /**
     * Creates a new KasaiWSFacade object.
     */
    public KasaiWSFacade() {
    }

    //~ Methods --------------------------------------------------------------------------------------------------------

    public boolean isUserInGroup(String login, String userId, String groupId)
        throws DataAccessException {
        
        return KasaiFacade.getInstance().isUserInGroup(login, userId, groupId);
    }

    public void addOperativeToRole(String loginUser, String idOperative, int role, String clientIP)
        throws DataAccessException, DoesntExistsException,NotEnoughPermissionException, 
            InvalidAttributesException, CannotAuditException {
        
        KasaiFacade.getInstance().addOperativeToRole(loginUser, idOperative, role, clientIP);
    }

    public void addUserToGroup(String loginUser, String idGroup, String idUserToAdd, String clientIP)
        throws DataAccessException, DoesntExistsException, NotEnoughPermissionException, 
            InvalidAttributesException, CannotAuditException {
        
        KasaiFacade.getInstance().addUserToGroup(loginUser, idGroup, idUserToAdd, clientIP);
    }

    public void blockGroup(String loginUser, String idGroup, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, 
            DoesntExistsException, CannotAuditException {
        
        KasaiFacade.getInstance().blockGroup(loginUser, idGroup, clientIP);
    }

    public void blockUser(String loginUser, String idUserToBlock, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, 
            DoesntExistsException, CannotAuditException {
        
        KasaiFacade.getInstance().blockUser(loginUser, idUserToBlock, clientIP);
    }

    public void changePasswordUser(String login, String oldPassword, String newPassword, String confirmation,
        String clientIP) throws DataAccessException, InvalidAttributesException, ServiceException, ServiceNotAvailableException,
        DoesntExistsException, CannotAuditException, InvalidPasswordException {
        
        KasaiFacade.getInstance().changePasswordUser(login, oldPassword, newPassword, confirmation, clientIP);
    }

    public boolean checkOperative(String login, String operative, String object){
        
        return KasaiFacade.getInstance().checkOperative(login, operative, object);
    }

    public void checkPasswordUser(String login, String password, String clientIP)
        throws DataAccessException, NotFoundException, UserBlockedException, InvalidPasswordException, 
        InvalidAttributesException, ServiceException, ServiceNotAvailableException, CannotAuditException {

        KasaiFacade.getInstance().checkPasswordUser(login, password, clientIP);
    }

    public void copyObjectRoles(String loginUser, String sourceObject, String destinationObject)
        throws DataAccessException, DoesntExistsException {
        
        KasaiFacade.getInstance().copyObjectRoles(loginUser, sourceObject, destinationObject);
    }

    public void createAuditEntry(String userId, int returnCode, String errorDescription, long duration,
        String clientIP, String operation, String objectID, HashMap transactionData)
        throws CannotAuditException {

        KasaiFacade.getInstance().createAuditEntry(userId, returnCode, errorDescription, duration, clientIP, operation, objectID, transactionData);
                
    }

    public void createAuditEntry(String userId, int returnCode, String errorDescription, long duration,
        String clientIP, String operation, String objectID, Document transactionData)
        throws CannotAuditException {

        KasaiFacade.getInstance().createAuditEntry(userId, returnCode, errorDescription, duration, clientIP, operation, objectID, transactionData);
    }

    public void createGroup(String loginUser, String id, String description, boolean blocked, String clientIP)
        throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
        NotEnoughPermissionException, CannotAuditException, DoesntExistsException,
        CriticalException {
        
        KasaiFacade.getInstance().createGroup(loginUser, id, description, blocked, clientIP);
    }

    public void createObject(String loginUser, String objectId)
        throws DataAccessException, CriticalException {
        
        KasaiFacade.getInstance().createObject(loginUser, objectId);
    }

    public void createObjectGroupRole(String loginUser, String objectId, String group, int role,
        String clientIP) throws DataAccessException, DoesntExistsException, NotEnoughPermissionException,
        InvalidAttributesException, CannotAuditException{
        
        KasaiFacade.getInstance().createObjectGroupRole(loginUser, objectId, group, role, clientIP);
    }

    public void createObjectUserRole(String loginUser, String objectId, String user, int role, String clientIP)
        throws DataAccessException, DoesntExistsException, NotEnoughPermissionException,
        InvalidAttributesException, CannotAuditException{
        
        KasaiFacade.getInstance().createObjectUserRole(loginUser, objectId, user, role, clientIP);
    }

    public void createObjectUserRole(String objectId, String user, int role)
        throws DataAccessException, DoesntExistsException {
        
        KasaiFacade.getInstance().createObjectUserRole(objectId, user, role);
    }

    public void createObjectWithRole(String loginUser, String objectId, int roleId)
        throws DataAccessException, DoesntExistsException {
        
        KasaiFacade.getInstance().createObjectWithRole(loginUser, objectId, roleId);
    }

    public int createRole(String loginUser, String name, String description, String[] operatives, String clientIP)
        throws AlreadyExistsException, DoesntExistsException, DataAccessException, 
        InvalidAttributesException, NotEnoughPermissionException, CannotAuditException,
        CriticalException {
        
        return KasaiFacade.getInstance().createRole(loginUser, name, description, operatives, clientIP);
    }

    public void createUser(String loginUser, String idUser, String firstName, String lastName, String email,
        boolean blocked, String description, boolean superUser, String clientIP)
        throws DataAccessException, AlreadyExistsException, InvalidAttributesException,
        DoesntExistsException, NotEnoughPermissionException, CannotAuditException,
        CriticalException {
        
        KasaiFacade.getInstance().createUser(loginUser, idUser, firstName, lastName, email, blocked, description, superUser, clientIP);
    }

    public void deleteGroup(String loginUser, String group, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteGroup(loginUser, group, clientIP);
    }

    public void deleteObject(String objectId) throws DataAccessException {
        
        KasaiFacade.getInstance().deleteObject(objectId);
    }

    public void deleteObjectGroupRole(String loginUser, int id, String clientIP)
        throws DataAccessException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteObjectGroupRole(loginUser, id, clientIP);
    }

    public void deleteObjectUserRole(String loginUser, String user, String idObject, int role,
        String clientIP) throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteObjectUserRole(loginUser, user, idObject, role, clientIP);
    }

    public void deleteObjectUserRole(String loginUser, String user, String idObject, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteObjectUserRole(loginUser, user, idObject, clientIP);
    }

    public void deleteObjectUserRole(String user, String idObject)
        throws DataAccessException {
        
        KasaiFacade.getInstance().deleteObjectUserRole(user, idObject);
    }

    public void deleteObjectUserRole(String loginUser, int id, String clientIP)
        throws DataAccessException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteObjectUserRole(loginUser, id, clientIP);
    }

    public void deleteRole(String loginUser, int role, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteRole(loginUser, role, clientIP);
    }

    public void deleteUser(String loginUser, String idUserToDelete, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().deleteUser(loginUser, idUserToDelete, clientIP);
    }

    public Collection listGroups(String actualUser, String idGroup, String description, int blocked, int system)
        throws DataAccessException, NonCriticalException {

        return KasaiFacade.getInstance().listGroups(actualUser, idGroup, description, blocked, system);
    }

    public Collection listGroupsFromUser(String user) throws DataAccessException {

        return KasaiFacade.getInstance().listGroupsFromUser(user);
    }

    public Collection listGroupsFromUser(String loginUser, String user)
        throws DataAccessException, NotEnoughPermissionException {

        return KasaiFacade.getInstance().listGroupsFromUser(loginUser, user);
    }

    public Collection listGroupsOperativeCollection(String operative, String object)
        throws DataAccessException {

        return KasaiFacade.getInstance().listGroupsOperativeCollection(operative, object);
    }

    public Collection listObjectRoles(String loginUser, String idObject)
        throws DataAccessException {
        
        return KasaiFacade.getInstance().listObjectRoles(loginUser, idObject);
    }

    public Collection listOperatives(String loginUser)
        throws DataAccessException {
        
        return KasaiFacade.getInstance().listOperatives(loginUser);
    }

    public Collection listOperativesFromRole(String loginUser, int role)
        throws DataAccessException {
        
        return KasaiFacade.getInstance().listOperativesFromRole(loginUser, role);
    }

    public Collection listOperativesNotInRole(String loginUser, int role)
        throws DataAccessException {
        
        return KasaiFacade.getInstance().listOperativesNotInRole(loginUser, role);
    }

    public Collection listRoles(String loginUser, String name)
        throws DataAccessException {
        
        return KasaiFacade.getInstance().listRoles(loginUser, name);
    }

    public Collection listUsers(String loginUser, String login, String firstName, String lastName, String email,
        int blocked, String description, String group)
        throws DataAccessException {

        return KasaiFacade.getInstance().listUsers(loginUser, login, firstName, lastName, email, blocked, description, group);
    }

    
    public Collection listUsersFromGroup(String loginUser, String group)
        throws DataAccessException {

        return KasaiFacade.getInstance().listUsersFromGroup(loginUser, group);
    }

    public Collection listUsersFromGroup(String group)
        throws DataAccessException {

        return KasaiFacade.getInstance().listUsersFromGroup(group);
    }

    public Collection listUsersNotInGroup(String loginUser, String group)
        throws DataAccessException {

        return KasaiFacade.getInstance().listUsersNotInGroup(loginUser, group);
    }

    public Collection listUsersOperative(String operative, String object)
        throws DataAccessException {

        return KasaiFacade.getInstance().listUsersOperative(operative, object);
    }

    public void modifyRole(String loginUser, int role, String name, String description, String[] operatives,
        String clientIP) throws DataAccessException, InvalidAttributesException,
        NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().modifyRole(loginUser, role, name, description, operatives, clientIP);
    }

    public Group readGroup(String loginUser, String group, String clientIP)
        throws NotEnoughPermissionException, CannotAuditException, DataAccessException, DoesntExistsException {
        
        return KasaiFacade.getInstance().readGroup(loginUser, group, clientIP);
    }

    public Group readGroup(String group) throws DataAccessException, DoesntExistsException {

        return KasaiFacade.getInstance().readGroup(group);
    }

    public Role readRole(String loginUser, int role) throws DataAccessException {

        return KasaiFacade.getInstance().readRole(loginUser, role);
    }

    public User readUser(String loginUser, String login, String clientIP)
        throws NotEnoughPermissionException, CannotAuditException, DataAccessException,DoesntExistsException {
        
        return KasaiFacade.getInstance().readUser(loginUser, login, clientIP);
    }

    public User readUser(String login) throws DataAccessException, DoesntExistsException {

        return KasaiFacade.getInstance().readUser(login);
    }

    public void remindPasswordUser(String loginUser, String clientIP)
        throws ServiceException, ServiceNotAvailableException, DataAccessException, DoesntExistsException, 
        CannotAuditException {
        
        KasaiFacade.getInstance().remindPasswordUser(loginUser, clientIP);
    }

    public void removeOperativeFromRole(String loginUser, String idOperative, int role, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().removeOperativeFromRole(loginUser, idOperative, role, clientIP);
    }

    public void removeUserFromGroup(String loginUser, String idGroup, String login, String clientIP)
        throws DataAccessException, NotEnoughPermissionException, CannotAuditException {
        
        KasaiFacade.getInstance().removeUserFromGroup(loginUser, idGroup, login, clientIP);
    }

    public void resetPasswordUser(String actualUser, String login, String clientIP)
        throws ServiceException, ServiceNotAvailableException, NotEnoughPermissionException, 
        CannotAuditException, DataAccessException, DoesntExistsException {
        
        KasaiFacade.getInstance().resetPasswordUser(actualUser, login, clientIP);
    }

    public void unblockGroup(String loginUser, String idGroup, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, 
        CannotAuditException, DoesntExistsException {
        
        KasaiFacade.getInstance().unblockGroup(loginUser, idGroup, clientIP);
    }

    public void unblockUser(String loginUser, String login, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException,
        CannotAuditException, DoesntExistsException {
        
        KasaiFacade.getInstance().unblockUser(loginUser, login, clientIP);
    }

    public void updateGroup(String loginUser, String id, String description, boolean blocked, String[] members,
        String clientIP) throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException,
        CannotAuditException {
        
        KasaiFacade.getInstance().updateGroup(loginUser, id, description, blocked, members, clientIP);
    }


    public void updateUser(String loginUser, String login, String firstName, String lastName, String email,
        boolean blocked, String description, boolean superUser, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException, CannotAuditException,
        DoesntExistsException{
        
        KasaiFacade.getInstance().updateUser(loginUser, login, firstName, lastName, email, blocked, description, superUser, clientIP);
    }

    public void updateUser(String loginUser, String login, String firstName, String lastName, String email,
        boolean blocked, String description, String clientIP)
        throws DataAccessException, InvalidAttributesException, NotEnoughPermissionException,
        CannotAuditException {
        
        KasaiFacade.getInstance().updateUser(loginUser, login, firstName, lastName, email, blocked, description, clientIP);
    }
    
    public void validateOperative(String user, String operative, String object)
        throws NotEnoughPermissionException {
        
        KasaiFacade.getInstance().validateOperative(user, operative, object);
    }

    public Role readRole(int role) throws DataAccessException {

        return KasaiFacade.getInstance().readRole(role);
    }
}
