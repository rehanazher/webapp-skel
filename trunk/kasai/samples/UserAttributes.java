import org.manentia.kasai.KasaiFacade;
import org.manentia.kasai.User;

public class UserAttributes {
    
    public static void main(String args[]){
        try {           
        	try {
        		KasaiFacade.getInstance().deleteUser("admin", "jlopez", "127.0.0.1");
        	} catch (Exception e){}
        	
            KasaiFacade.getInstance().createUser("admin", "jlopez", "Jennifer", "Lopez", 
                "jlopez@uwishuknew.com", false, "This is Jenn's user", false, null);
            
            User jennifer = KasaiFacade.getInstance().readUser("admin", "jlopez", "127.0.0.1");
            jennifer.setAttribute("husband", "uncertain");
            jennifer.setAttribute("acting", "so so");
            
            KasaiFacade.getInstance().updateUser("admin", jennifer, "127.0.0.1");
            
            jennifer = KasaiFacade.getInstance().readUser("admin", "jlopez", "127.0.0.1");
            System.out.println("Husband: " + jennifer.getAttribute("husband"));
            System.out.println(jennifer.getAttributesXML());
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}