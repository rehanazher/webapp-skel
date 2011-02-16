package org.manentia.kasai;

import com.manentia.commons.log.Log;

public class AdminFacade {
	private static AdminFacade instance = null;
	
	private AdminFacade() {
        Log.write("Enter", Log.INFO, "init", AdminFacade.class);
        Log.write("Exit", Log.INFO, "init", AdminFacade.class);
    }
	
	public static synchronized AdminFacade getInstance(){
		if (instance == null){
			instance = new AdminFacade();
		}
		return instance;
	}
	
	
}
