package org.manentia.kasai.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.manentia.kasai.Constants;
import org.manentia.kasai.KasaiFacade;
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
import org.manentia.kasai.ui.actions.BaseAction;
import org.manentia.kasai.ui.exceptions.SessionExpiredException;
import org.manentia.kasai.ui.roles.RoleFacade;

import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLException;


public class BaseFacade {
	public void login(String username, String password) throws DataAccessException, NotFoundException, UserBlockedException, InvalidPasswordException, ServiceException, ServiceNotAvailableException, CannotAuditException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		KasaiFacade.getInstance().checkPasswordUser(username, password, request.getRemoteAddr());
		
		UserView userView = new UserView();
        userView.setUser(username);
        
        request.getSession().setAttribute("userView", userView);
	}
	
	public void changePassword(String oldPassword, String newPassword, String confirmation) throws SessionExpiredException, DataAccessException, InvalidAttributesException, ServiceException, ServiceNotAvailableException, DoesntExistsException, CannotAuditException, InvalidPasswordException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().changePasswordUser(userView.getUser(), oldPassword, newPassword, confirmation, request.getRemoteAddr());
	}
	
	public Collection listObjectRoles(String objectId) throws SessionExpiredException, DataAccessException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		return KasaiFacade.getInstance().listObjectRoles(userView.getUser(),objectId);
	}
	
	
	public Map decodeMessage(String data){
		HashMap map = new HashMap();
		
	
		String message = data;
		String level = "error";
		
		try {
			WebContext ctx = WebContextFactory.get();
			HttpServletRequest request = ctx.getHttpServletRequest();
			
            // we hope the module programmer used a key instead of a hard coded text
            ResourceBundle res = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE, request.getLocale());
            
            level = res.getString(message + ".level");
            message = res.getString(message); // dahhh ?? :)
        }catch (Exception unhandledBastard) {}
        
        
        map.put("message", message);
        map.put("level", level);
                
        return map;
	}
	
	public Map listUsersGroupsRoles() throws DataAccessException, SessionExpiredException, XMLException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		Map result = new HashMap();
		
		List users = KasaiFacade.getInstance().listUsers(userView.getUser(), null, null, null, null, -1, null, null);
		List groups = KasaiFacade.getInstance().listGroups(userView.getUser(), null, null, -1, -1);
		List roles = KasaiFacade.getInstance().listRoles(userView.getUser(), null);
		
		result.put("users", users);
		result.put("groups", groups);
		result.put("roles", roles);
		
		return result;
	}
	
	public void deleteObjectUserRole(int id) throws SessionExpiredException, DataAccessException, CannotAuditException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().deleteObjectUserRole(userView.getUser(), id, request.getRemoteAddr());
	}
	
	public void deleteObjectGroupRole(int id) throws SessionExpiredException, DataAccessException, CannotAuditException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().deleteObjectGroupRole(userView.getUser(), id, request.getRemoteAddr());
	}
	
	public void createObjectUserRole(String objectId, String user, int roleId) throws SessionExpiredException, DataAccessException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, XMLException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().createObjectUserRole(userView.getUser(), objectId, user, roleId, request.getRemoteAddr());
	}
	
	public void createObjectGroupRole(String objectId, String group, int roleId) throws SessionExpiredException, DataAccessException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, XMLException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().createObjectGroupRole(userView.getUser(), objectId, group, roleId, request.getRemoteAddr());
	}
	
	public String[] listUsernames() throws DataAccessException{
		return KasaiFacade.getInstance().listUsernames();
	}
	
	public Collection listOperatives() throws DataAccessException, SessionExpiredException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		UserView userView = new BaseAction().validateSession(request);
		
		Collection result = KasaiFacade.getInstance().listOperatives(userView.getUser());
		
		Log.write("Operativas: " + result.size(), Log.DEBUG, "TEST", RoleFacade.class);
		
		return result;
	}
}
