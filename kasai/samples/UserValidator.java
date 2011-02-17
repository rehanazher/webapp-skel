import org.manentia.kasai.KasaiFacade;

public class UserValidator {
    
    public static void main(String args[]){
        try {
            String login = args[0];
            String password = args[1];            
            
            KasaiFacade.getInstance().checkPasswordUser(login, password, null);
            
            
            System.out.println("User successfully validated");
            
        } catch (Exception e){
            System.out.println("User could not be validated, reason: " + e.getMessage());
        }
    }
    
}
