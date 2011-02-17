package org.manentia.kasai.ui.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.KasaiFacade;
import org.manentia.kasai.User;
import org.manentia.kasai.exceptions.AlreadyExistsException;
import org.manentia.kasai.exceptions.CannotAuditException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.exceptions.DoesntExistsException;
import org.manentia.kasai.exceptions.InvalidAttributesException;
import org.manentia.kasai.exceptions.InvalidPasswordException;
import org.manentia.kasai.exceptions.NotEnoughPermissionException;
import org.manentia.kasai.exceptions.ServiceException;
import org.manentia.kasai.exceptions.ServiceNotAvailableException;
import org.manentia.kasai.ui.UserView;
import org.manentia.kasai.ui.actions.BaseAction;
import org.manentia.kasai.ui.exceptions.SessionExpiredException;

import com.manentia.commons.CriticalException;
import com.manentia.commons.xml.XMLException;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

public class UserFacade {
	private String lastSortOrder = null;
	
	public void createNewUser(String login, String firstName, String lastName, String email, boolean blocked, String description, boolean superUser) throws SessionExpiredException, DataAccessException, AlreadyExistsException, InvalidAttributesException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, CriticalException, InvalidPasswordException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().createUser(userView.getUser(), login, firstName, lastName, email, blocked, description, superUser, request.getRemoteAddr());
	}
	
	public void modifyUser(String login, String firstName, String lastName, String email, boolean blocked, String description, boolean superUser) throws SessionExpiredException, DataAccessException, AlreadyExistsException, InvalidAttributesException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, CriticalException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().updateUser(userView.getUser(), login, firstName, lastName, email, blocked, description, superUser, request.getRemoteAddr());
	}
	
	public void block(String login) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().blockUser(userView.getUser(), login, request.getRemoteAddr());
	}
	
	public void newPassword(String login) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, ServiceException, ServiceNotAvailableException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().resetPasswordUser(userView.getUser(), login, request.getRemoteAddr());
	}
	
	public void deleteUser(String login) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, ServiceException, ServiceNotAvailableException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().deleteUser(userView.getUser(), login, request.getRemoteAddr());
	}
	
	public void unblock(String login) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().unblockUser(userView.getUser(), login, request.getRemoteAddr());
	}
	
	public Map refresh(int page, String login, String firstName, String lastName, String email, String description, String group) throws SessionExpiredException, DataAccessException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		Collection users = KasaiFacade.getInstance().listUsers(userView.getUser(), login, firstName, 
				lastName, email, -1, description, group);
		        
        request.getSession().setAttribute("users", users);
        
        return goToPageUsers(page);
	}
	 
	public User readUser(String login) throws NotEnoughPermissionException, CannotAuditException, DataAccessException, DoesntExistsException, SessionExpiredException, XMLException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		User result = KasaiFacade.getInstance().readUser(userView.getUser(), login, request.getRemoteAddr());
		
		return result;
	}
	
	public Map goToPageUsers(int page){
		ArrayList result = null;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		Map map = new HashMap();
		
		Collection users = (List) request.getSession().getAttribute("users");
        int rowsPerPage = ((Integer) request.getSession().getAttribute("rowsPerPage")).intValue();
        
        result = new ArrayList(rowsPerPage);
        int index = 0;
        for (Iterator iter=users.iterator(); iter.hasNext() && index<rowsPerPage*page;){
        	User user = (User) iter.next();
        	if (index>=rowsPerPage*(page-1) && index<rowsPerPage*page){
        		result.add(user);
        	}
        	index++;
        }
        
        map.put("list", result);
        map.put("totalSize", new Integer(users.size()));
        
        return map;
	}
	
	public Map sortUsers(String sortOrder) {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		if (StringUtils.isEmpty(sortOrder)){
			sortOrder = "login";
		}
		
		boolean reverse = sortOrder.equalsIgnoreCase(lastSortOrder);
		Comparator comparator = new BeanComparator(sortOrder);
	
		if (reverse){
			comparator = new ReverseComparator(comparator);
		}
		
		List users = (List) request.getSession().getAttribute("users");
		Collections.sort(users, comparator);
		
		lastSortOrder = reverse ? "__CHANGE__" : sortOrder;
		
		return goToPageUsers(1);
	}
	
	
}
