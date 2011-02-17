package org.manentia.kasai.ui.users.actions;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.manentia.kasai.KasaiFacade;
import org.manentia.kasai.ui.UserView;
import org.manentia.kasai.ui.actions.BaseAction;

public class List extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
		UserView userView = validateSession(request);
		
        Collection users = KasaiFacade.getInstance().listUsers(userView.getUser(), null, null, null, null, -1, null, null);
        Collection groups = KasaiFacade.getInstance().listGroups(userView.getUser(), null, null, -1, -1);
        
        request.getSession().setAttribute("users", users);
        request.getSession().setAttribute("groups", groups);
        
        
        return mapping.findForward(SUCCESS);
    }
}
