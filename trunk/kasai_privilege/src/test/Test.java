/**
 * Added by James
 * on 2011-1-4
 */
package test;

import org.jasypt.digest.StandardStringDigester;

public class Test {
	public static void main(String[] args) {
		
		String password = "abcd";
		
		StandardStringDigester digester = new StandardStringDigester();
		digester.setAlgorithm("SHA-256");
		digester.setIterations(100000);
		digester.setSaltSizeBytes(16);
		digester.initialize();

		String enPwd = digester.digest(password);

		System.out.println("EN PASSWORD: " + enPwd);
		
		boolean matched = digester.matches("abcd", enPwd);
		
		System.out.println(matched);
	}
}
