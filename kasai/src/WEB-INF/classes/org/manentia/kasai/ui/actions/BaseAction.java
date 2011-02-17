package org.manentia.kasai.ui.actions;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.manentia.kasai.ui.UserView;
import org.manentia.kasai.ui.exceptions.SessionExpiredException;

public class BaseAction extends Action {
	protected static final String SUCCESS = "success";
    protected static final String FAILURE = "failure";
    
    private boolean isLoggedIn(HttpServletRequest request) {
        boolean valida = false;
        UserView userView;
        if (request.getSession().getAttribute("userView") != null) {
            userView = (UserView)request.getSession().getAttribute("userView");
            if (userView.getUser()!= null) {
                valida = true;
            }
        }
        return valida;
    }
                                                                                                                                                                                                           
    public UserView validateSession(HttpServletRequest request) throws SessionExpiredException {
        if (!isLoggedIn(request)) {
            throw new SessionExpiredException();
        }
        
        return (UserView) request.getSession().getAttribute("userView");
    }
    
    public Date doDate (int day, int month, int year, boolean lastHour){
        Date dateResult = null;
        if ((day != -1) && (month != -1) && (year != -1)) {
            if (lastHour){
                dateResult = new GregorianCalendar(year, month - 1, day, 23, 59, 59).getTime();
            }else{
                dateResult = new GregorianCalendar(year, month - 1, day).getTime();
            }
        }
        return dateResult;
    }
    
    public int doInt (String value){
        int result = -1;
        try{
            result = Integer.parseInt (value);
        }catch (Exception e){}
        return result;
    }
    
    public int doInt (String value, int defaultValue){
        int result = defaultValue;
        try{
            result = Integer.parseInt (value);
        }catch (Exception e){}
        return result;
    }
    
    public Integer doInteger (String value){
        Integer result = null;
        try{
            result = Integer.valueOf (value);
        }catch (Exception e){}
        return result;
    }
}
