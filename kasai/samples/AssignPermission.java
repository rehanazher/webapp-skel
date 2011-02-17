import org.manentia.kasai.KasaiFacade;

public class AssignPermission {
    
    public static void main(String args[]){
        try {            
            KasaiFacade.getInstance().createUser("admin", "jdoe", "John", "Doe", 
                "jdoe@welovekasai.com", false, "This is John Doe's user", false, null);
            
            KasaiFacade.getInstance().createObject("admin", "/proposals/79");
            
            KasaiFacade.getInstance().createObjectUserRole("admin", "/proposals/79", "jdoe", 1, null);                        
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}