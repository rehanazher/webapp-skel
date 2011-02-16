package org.manentia.kasai.ui.actions;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.manentia.kasai.Constants;

public class LoadParams extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        ResourceBundle config = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
        
        Integer rowsPerPage = new Integer(config.getString("lists.rowsPerPage"));
        
        request.getSession().setAttribute("rowsPerPage", rowsPerPage);

        return mapping.findForward(SUCCESS);
    }
}
