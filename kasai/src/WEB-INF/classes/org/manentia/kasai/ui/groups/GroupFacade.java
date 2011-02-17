package org.manentia.kasai.ui.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.Group;
import org.manentia.kasai.KasaiFacade;
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

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import com.manentia.commons.CriticalException;
import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLException;
 
public class GroupFacade {
	private String lastSortOrder = null;
	
	public void createNewGroup(String id, String description, boolean blocked, String[] members) throws SessionExpiredException, DataAccessException, AlreadyExistsException, InvalidAttributesException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, CriticalException, InvalidPasswordException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
				
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().createGroup(userView.getUser(), id, description, blocked, request.getRemoteAddr());
		
		for (String member : members){
			Log.write("Usuario: " + member, Log.DEBUG, "TEST", GroupFacade.class);
			KasaiFacade.getInstance().addUserToGroup(userView.getUser(), id, member, request.getRemoteAddr());
		}
	}
	
	public void modifyGroup(String id, String description, boolean blocked, String[] members) throws SessionExpiredException, DataAccessException, AlreadyExistsException, InvalidAttributesException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, CriticalException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().updateGroup(userView.getUser(), id, description, blocked, members, request.getRemoteAddr());		
	}
	
	public void block(String id) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().blockGroup(userView.getUser(), id, request.getRemoteAddr());
	}
		
	public void deleteGroup(String id) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, ServiceException, ServiceNotAvailableException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().deleteGroup(userView.getUser(), id, request.getRemoteAddr());
	}
	
	public void unblock(String id) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().unblockGroup(userView.getUser(), id, request.getRemoteAddr());
	}
	
	public Map refresh(int page, String id, String description, int blocked, int system) throws SessionExpiredException, DataAccessException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		Collection groups = KasaiFacade.getInstance().listGroups(userView.getUser(), id, description, blocked, system);
		        
        request.getSession().setAttribute("groups", groups);
        
        return goToPageGroups(page);
	}
	 
	public Group readGroup(String id) throws NotEnoughPermissionException, CannotAuditException, DataAccessException, DoesntExistsException, SessionExpiredException, XMLException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		Group result = KasaiFacade.getInstance().readGroup(userView.getUser(), id, request.getRemoteAddr());
		result.setMembers(KasaiFacade.getInstance().listGroupMembers(result.getId()));
		
		return result;
	}
	
	public Map goToPageGroups(int page){
		ArrayList result = null;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		Map map = new HashMap();
		
		Collection groups = (List) request.getSession().getAttribute("groups");
        int rowsPerPage = ((Integer) request.getSession().getAttribute("rowsPerPage")).intValue();
        
        result = new ArrayList(rowsPerPage);
        int index = 0;
        for (Iterator iter=groups.iterator(); iter.hasNext() && index<rowsPerPage*page;){
        	Group group = (Group) iter.next();
        	if (index>=rowsPerPage*(page-1) && index<rowsPerPage*page){
        		result.add(group);
        	}
        	index++;
        }
        
        map.put("list", result);
        map.put("totalSize", new Integer(groups.size()));
        
        return map;
	}
	
	public Map sortGroups(String sortOrder) {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		if (StringUtils.isEmpty(sortOrder)){
			sortOrder = "id";
		}
		
		boolean reverse = sortOrder.equalsIgnoreCase(lastSortOrder);
		Comparator comparator = new BeanComparator(sortOrder);
	
		if (reverse){
			comparator = new ReverseComparator(comparator);
		}
		
		List groups = (List) request.getSession().getAttribute("groups");
		Collections.sort(groups, comparator);
		
		lastSortOrder = reverse ? "__CHANGE__" : sortOrder;
		
		return goToPageGroups(1);
	}
	
	
}
