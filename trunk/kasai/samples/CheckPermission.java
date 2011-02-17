import org.manentia.kasai.KasaiFacade;

public class CheckPermission {
    
    public static void main(String args[]){
        try {     
            KasaiFacade.getInstance().validateOperative("jdoe", "kasai.user.commit", "/kasai/user/admin");
            
            System.out.println("jdoe can modify the admin user data");                      
        } catch (Exception e){
            System.out.println("jdoe can not modify the admin user data, reason: " + e.getMessage());
        }
    }
}