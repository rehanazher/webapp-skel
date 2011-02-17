package org.manentia.kasai.ui;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionServlet;

public class KasaiServlet extends ActionServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3715312659572095237L;

	public KasaiServlet(){
		super();
	}
	
	public void init() throws javax.servlet.ServletException {
        super.init();        
    }
	
	protected void process(HttpServletRequest request,
            HttpServletResponse response
	        ) throws IOException, ServletException {
	        
	    request.setCharacterEncoding("ISO-8859-1");
	    super.process(request, response);
	}

}
