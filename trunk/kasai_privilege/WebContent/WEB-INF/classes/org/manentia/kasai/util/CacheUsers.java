package org.manentia.kasai.util;

import org.apache.oro.util.CacheFIFO;
import org.manentia.kasai.User;

/**
 *
 * @author  fpena
 */
public class CacheUsers {
    
    private static CacheFIFO cache;
    
    static{
        cache = new CacheFIFO(100);
    }
    
    public static void addUser (String login, User u){
        cache.addElement(login, u);
    }
    
    public static User getUser (String login){
        try{
            return (User)cache.getElement(login);
        }catch (Exception e){
            return null;
        }
    }
}
