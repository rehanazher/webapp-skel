/**
 * Added by James
 * on 2010-11-4
 */
package jp.co.fcctvweb.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
	public static String md5(String src) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		byte[] strTemp = src.getBytes();
		// 使用MD5创建MessageDigest对象
		MessageDigest mdTemp;
		try {
			mdTemp = MessageDigest.getInstance("MD5");

			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// System.out.println((int)b);
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}

			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			// no possible reach here
			return null;
		}
	}

	// public static String sha1(String src){
	// return null;
	// }
}
