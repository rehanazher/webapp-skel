import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
    	
    	System.out.println(passwordEncryptor.encryptPassword("changeme"));
    	
    	StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();
    	
    	System.out.println(strongPasswordEncryptor.encryptPassword("changeme"));
	}

}
