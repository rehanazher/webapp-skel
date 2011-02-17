import org.manentia.kasai.Group;
import org.manentia.kasai.KasaiFacade;
import org.manentia.kasai.User;

public class GroupAttributes {
    
    public static void main(String args[]){
        try {           
        	try {
        		KasaiFacade.getInstance().deleteGroup("admin", "partyppl", "127.0.0.1");
        	} catch (Exception e){}
        	
            KasaiFacade.getInstance().createGroup("admin", "partyppl", "The party people", false, "127.0.0.1");
            
            Group partyppl = KasaiFacade.getInstance().readGroup("admin", "partyppl", "127.0.0.1");
            partyppl.setAttribute("drunk", "john");
            partyppl.setAttribute("designated driver", "jenny");
            
            KasaiFacade.getInstance().updateGroup("admin", partyppl, null, "127.0.0.1");
            
            partyppl = KasaiFacade.getInstance().readGroup("admin", "partyppl", "127.0.0.1");
            System.out.println("Designated driver: " + partyppl.getAttribute("designated driver"));
            System.out.println(partyppl.getAttributesXML());
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}