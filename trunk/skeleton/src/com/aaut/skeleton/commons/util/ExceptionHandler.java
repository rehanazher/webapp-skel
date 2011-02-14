/**
 * Added by James
 * on 2010-6-2
 */
package com.aaut.skeleton.commons.util;

public class ExceptionHandler {
	Exception e;
	
	public ExceptionHandler(Exception e){
		this.e = e;
	}
	
	public void log(){
		// TODO 
		e.printStackTrace();
	}
}
