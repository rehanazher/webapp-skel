/**
 * Added by James
 * on 2010-6-2
 */
package jp.co.fcctvweb.utils;

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
