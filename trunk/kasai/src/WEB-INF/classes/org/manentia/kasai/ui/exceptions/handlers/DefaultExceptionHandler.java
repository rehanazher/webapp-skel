/*
 * NonCriticalExceptionHandler.java
 *
 * 
 */

package org.manentia.kasai.ui.exceptions.handlers;

import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.manentia.kasai.Constants;

import com.manentia.commons.configuration.Configuration;

/**
 *
 * @author
 */
public class DefaultExceptionHandler extends ExceptionHandler {
    
    public ActionForward execute(Exception ex,
                              ExceptionConfig exConfig,
                              ActionMapping mapping,
                              ActionForm formInstance,
                              HttpServletRequest request,
                              HttpServletResponse response) throws ServletException {
                                  
        ActionForward forward = new ActionForward(exConfig.getPath());
      
        String message = ex.getMessage();
      
        try {
            // we hope the module programmer used a key instead of a hard coded text
            ResourceBundle res = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE, request.getLocale());
            message = res.getString(message); // dahhh ?? :)
        }catch (Exception unhandledBastard) {}
      
        if(Configuration.getInstance(Constants.CONFIG_PROPERTY_FILE).getInt("page.debug", 1) == 1){
            message += ": " + ExceptionUtils.getStackTrace(ex);
        } 
        
        request.setAttribute("message", message);
      
        request.setAttribute("errorTrace", message);
      
        return forward;
    }
}