package org.manentia.kasai.ui.exceptions;

public class SessionExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1449390427807066207L;
	
	public String getType(){
		return "SessionExpiredException";
	}
}
