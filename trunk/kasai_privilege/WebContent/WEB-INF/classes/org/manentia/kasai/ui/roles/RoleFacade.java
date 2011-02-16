package org.manentia.kasai.ui.roles;

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
import org.manentia.kasai.Role;
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
import org.manentia.kasai.ui.groups.GroupFacade;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import com.manentia.commons.CriticalException;
import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLException;
 
public class RoleFacade {
	private String lastSortOrder = null;
	
	public void createNewRole(String name, String description, String[] operatives) throws SessionExpiredException, DataAccessException, AlreadyExistsException, InvalidAttributesException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, CriticalException, InvalidPasswordException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
				
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().createRole(userView.getUser(), name, description, operatives, request.getRemoteAddr());		
	}
	
	public void modifyRole(int idRole, String name, String description, String[] operatives) throws SessionExpiredException, DataAccessException, AlreadyExistsException, InvalidAttributesException, DoesntExistsException, NotEnoughPermissionException, CannotAuditException, CriticalException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().modifyRole(userView.getUser(), idRole, name, description, operatives, request.getRemoteAddr());		
	}
	
	public void deleteRole(int idRole) throws SessionExpiredException, DataAccessException, InvalidAttributesException, NotEnoughPermissionException, DoesntExistsException, CannotAuditException, ServiceException, ServiceNotAvailableException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		KasaiFacade.getInstance().deleteRole(userView.getUser(), idRole, request.getRemoteAddr());
	}
	
	public Map refresh(int page, String name) throws SessionExpiredException, DataAccessException, XMLException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		Collection roles = KasaiFacade.getInstance().listRoles(userView.getUser(), name);
		        
        request.getSession().setAttribute("roles", roles);
        
        return goToPageRoles(page);
	}
	 
	public Role readRole(int idRole) throws NotEnoughPermissionException, CannotAuditException, DataAccessException, DoesntExistsException, SessionExpiredException, XMLException {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		
		Role result = KasaiFacade.getInstance().readRole(userView.getUser(), idRole);
		result.setOperatives(KasaiFacade.getInstance().listOperativesFromRole(userView.getUser(), idRole));
		
		return result;
	}
	
	public Map goToPageRoles(int page){
		ArrayList<Role> result = null;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Collection roles = (List) request.getSession().getAttribute("roles");
        int rowsPerPage = ((Integer) request.getSession().getAttribute("rowsPerPage")).intValue();
        
        result = new ArrayList<Role>(rowsPerPage);
        int index = 0;
        for (Iterator iter=roles.iterator(); iter.hasNext() && index<rowsPerPage*page;){
        	Role role = (Role) iter.next();
        	if (index>=rowsPerPage*(page-1) && index<rowsPerPage*page){
        		result.add(role);
        	}
        	index++;
        }
        
        map.put("list", result);
        map.put("totalSize", new Integer(roles.size()));
        
        return map;
	}
	
	public Map sortRoles(String sortOrder) {
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
		
		List roles = (List) request.getSession().getAttribute("roles");
		Collections.sort(roles, comparator);
		
		lastSortOrder = reverse ? "__CHANGE__" : sortOrder;
		
		return goToPageRoles(1);
	}
	
	
}
