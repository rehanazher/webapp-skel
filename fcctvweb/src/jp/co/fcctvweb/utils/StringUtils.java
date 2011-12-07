package jp.co.fcctvweb.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

public final class StringUtils {

	public static final String SEPARATOR_MULTI = ";";
	public static final String SEPARATOR_SINGLE = "#";
	public static final String SQL_REPLACE = "_";

	/**
	 * ��str�ұ߼����㹻���addStr�ַ�
	 * 
	 * @param str
	 * @param addStr
	 * @param length
	 * @return
	 */
	public static String addStringRight(String str, String addStr, int length) {
		while (str.length() < length) {
			str = str + addStr;
		}
		return str;
	}

	/**
	 * ���ַ�strƴ�ӷָ��regex���ַ�sub
	 * 
	 * @param str
	 * @param sub
	 * @param regex
	 * @return
	 */
	public static String addSubString(String str, String sub, String regex) {
		return Validators.isEmpty(str) ? sub : str + regex + sub;
	}

	/**
	 * ���ַ�str�ұ߲���0ֱ�����ȵ���length
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String addZeroRight(String str, int length) {
		return addStringRight(str, "0", length);
	}

	/**
	 * �����ַ�str���ַ�sub�ĸ���
	 * 
	 * @param str
	 * @param sub
	 * @return
	 */
	public static int charCount(String str, char sub) {
		int charCount = 0;
		int fromIndex = 0;

		while ((fromIndex = str.indexOf(sub, fromIndex)) != -1) {
			fromIndex++;
			charCount++;
		}
		return charCount;
	}

	/**
	 * �����ַ�str�ұ߳��ֶ��ٴ�sub
	 * 
	 * @param str
	 * @param sub
	 * @return
	 */
	public static int charCountRight(String str, String sub) {
		if (str == null) {
			return 0;
		}

		int charCount = 0;
		String subStr = str;
		int currentLength = subStr.length() - sub.length();
		while (currentLength >= 0
				&& subStr.substring(currentLength).equals(sub)) {
			charCount++;
			subStr = subStr.substring(0, currentLength);
			currentLength = subStr.length() - sub.length();
		}
		return charCount;
	}

	/**
	 * <p>
	 * Counts how many times the substring appears in the larger String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String input returns <code>0</code>.
	 * </p>
	 * 
	 * <pre>
	 *                                              StringUtils.countMatches(null, *)                           = 0
	 *                                              StringUtils.countMatches(&quot;&quot;, *)                   = 0
	 *                                              StringUtils.countMatches(&quot;abba&quot;, null)            = 0
	 *                                              StringUtils.countMatches(&quot;abba&quot;, &quot;&quot;)    = 0
	 *                                              StringUtils.countMatches(&quot;abba&quot;, &quot;a&quot;)   = 2
	 *                                              StringUtils.countMatches(&quot;abba&quot;, &quot;ab&quot;)  = 1
	 *                                              StringUtils.countMatches(&quot;abba&quot;, &quot;xxx&quot;) = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param sub
	 *            the substring to count, may be null
	 * @return the number of occurrences, 0 if either String is
	 *         <code>null</code>
	 */
	public static int countMatches(String str, String sub) {
		if (Validators.isEmpty(str) || Validators.isEmpty(sub)) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	/**
	 * ��ȡ�̶����ȵ��ַ�ʣ�ಿ����ʵ���Ȳ��ᳬ��len������������suffix���棬ʹ��cutOut��ʽ�����
	 * �
	 * 
	 * @param str
	 * @param len
	 * @param suffix
	 * @return
	 * @deprecated
	 */
	public static String cutOff(String str, int len, String suffix) {
		if (Validators.isEmpty(str)) {
			return str;
		}

		int byteIndex = 0;
		int charIndex = 0;

		while (charIndex < str.length() && byteIndex < len) {
			if (str.charAt(charIndex) >= 256) {
				byteIndex += 2;
			} else {
				byteIndex++;
			}

			charIndex++;
		}
		return byteIndex < len ? str.substring(0, charIndex) : str.substring(0,
				charIndex)
				+ suffix;
	}

	/**
	 * ��ȡ�̶����ȵ��ַ�����������suffix���棬�����ַ���ʵ���Ȳ��ᳬ��maxLength
	 * 
	 * @param str
	 * @param maxLength
	 * @param suffix
	 * @return
	 */
	public static String cutOut(String str, int maxLength, String suffix) {
		if (Validators.isEmpty(str)) {
			return str;
		}

		int byteIndex = 0;
		int charIndex = 0;

		while (charIndex < str.length() && byteIndex <= maxLength) {
			char c = str.charAt(charIndex);
			if (c >= 256) {
				byteIndex += 2;
			} else {
				byteIndex++;
			}
			charIndex++;
		}

		if (byteIndex <= maxLength) {
			return str;
		}

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(str.substring(0, charIndex));
		stringBuffer.append(suffix);

		while (getRealLength(stringBuffer.toString()) > maxLength) {
			stringBuffer.deleteCharAt(--charIndex);
		}

		return stringBuffer.toString();
	}

	/**
	 * ���ַ�strʹ��URLDecoder����ָ�����뷽ʽ���룬�Ѿ���URLUtils�е�decode����
	 * 
	 * @deprecated
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String decode(String str, String encoding) {
		if (Validators.isEmpty(str)) {
			return str;
		}
		try {
			str = URLDecoder.decode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(StringUtils.class).error(e);
		}

		return str;
	}

	/**
	 * ���ַ�strʹ��URLEncoder����ָ�����뷽ʽ���룬�Ѿ���URLUtils�е�encode����
	 * 
	 * @deprecated
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String encode(String str, String encoding) {
		if (Validators.isEmpty(str)) {
			return str;
		}
		try {
			str = URLEncoder.encode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(StringUtils.class).error(e);
		}

		return str;
	}

	/**
	 * ���ַ�str��߲���0ֱ�����ȵ���length
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String enoughZero(String str, int len) {
		while (str.length() < len) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * ��4��ʾ�쳣��Ϣ��html������
	 * 
	 * @param value
	 * @return
	 */
	public static String exceptionFilter(String value) {
		return Validators.isEmpty(value) ? "" : value.replaceAll("\n", "<br>")
				.replaceAll("\t", "&nbsp; &nbsp; ");
	}

	/**
	 * @param text
	 *            ��Ҫ����ʽ�����ַ� <br>
	 *            eg:����һ:{0},�����:{1},������:{2}
	 * 
	 * @param args
	 *            ������ַ��еĲ���,Щ�����滻{X} <br>
	 *            eg:new Object[] { "0001", "0005049", new Integer(1) }
	 * @return ��ʽ������ַ� <br>
	 *         eg: ����������������Ϊ:����һ:0001,�����:0005049,������:1
	 */
	public static String format(String text, Object[] args) {
		if (Validators.isEmpty(text) || args == null || args.length == 0) {
			return text;
		}
		for (int i = 0, length = args.length; i < length; i++) {
			text = replace(text, "{" + i + "}", args[i].toString());
		}
		return text;
	}

	/**
	 * ��ʽ�����������ֳ��ַ�, ����}λС��λ.
	 * 
	 * @param number
	 *            ��������
	 * @return ��ʽ��֮����ַ�
	 */
	public static String formatDecimal(double number) {
		NumberFormat format = NumberFormat.getInstance();

		format.setMaximumIntegerDigits(Integer.MAX_VALUE);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);

		return format.format(number);
	}

	/**
	 * ��ʽ�������������.
	 * 
	 * @param number
	 *            �������
	 * @param minFractionDigits
	 *            ��С����С��λ
	 * @param maxFractionDigits
	 *            �����С��λ
	 * @return ��������ݸ�ʽ������ַ�
	 */
	public static String formatDecimal(double number, int minFractionDigits,
			int maxFractionDigits) {
		NumberFormat format = DecimalFormat.getInstance();
		format.setMinimumFractionDigits(minFractionDigits);
		format.setMaximumFractionDigits(minFractionDigits);

		return format.format(number);
	}

	/**
	 * ȡ�ó��ȵ���length��������ִ����Ѿ���RandomUtils.getRandom����
	 * 
	 * @deprecated
	 * @param length
	 * @return
	 */
	public static String getRandom(int length) {
		return (Double.toString(Math.random())).substring(2, (2 + length));
	}

	/**
	 * ȡ���ַ����ʵ���ȣ�һ���ֳ���Ϊ2
	 * 
	 * @param str
	 * @return
	 */
	public static int getRealLength(String str) {
		if (str == null) {
			return 0;
		}

		char separator = 256;
		int realLength = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= separator) {
				realLength += 2;
			} else {
				realLength++;
			}
		}
		return realLength;
	}

	/**
	 * html�ı�����, ���valueΪ <code>null</code> ��Ϊ�մ�, �򷵻�"&nbsp;". ���ڿո��ַ�,
	 * ת��Ϊ"&nbsp". (��������ʾ��ͨ�ı�)
	 * 
	 * @param value
	 *            Ҫ���˵��ı�
	 * @return ���˺��html�ı�
	 */
	public static String htmlFilter(String value) {
		if (Validators.isEmpty(value)) {
			return "&nbsp;";
		}

		return value.replaceAll("&", "&amp;").replaceAll(" ", "&nbsp;")
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(
						"\"", "&quot;").replaceAll("\n", "<br>");
	}

	/**
	 * html�ı�����, ���valueΪ <code>null</code> ��Ϊ�մ�, �򷵻ؿմ�. ���ڿո��ַ�,
	 * ��ת��Ϊ"&nbsp;". (��������ʾ�ı����е�ֵ)
	 * 
	 * @param value
	 *            Ҫ���˵��ı�
	 * @return ���˺��html�ı�
	 */
	public static String htmlFilterToEmpty(String value) {
		if (Validators.isEmpty(value)) {
			return "";
		}

		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
	}

	/**
	 * ����ֵΪ <code>null</code> ���ַ�
	 * 
	 * @param str
	 *            �ַ�
	 * @return ����ַ�Ϊ <code>null</code>, �򷵻ؿ��ַ�.
	 */
	public static String ignoreNull(String str) {
		return str == null ? "" : str;
	}

	/**
	 * ȡ���û�id�е�ѧУid���֣�������Ŀ��صģ��Ѿ���ֹʹ��
	 * 
	 * @deprecated
	 * @param userId
	 * @return
	 */
	public static String insideId(String userId) {
		if (Validators.isEmpty(userId)) {
			return userId;
		}
		return userId.trim().length() > 6 ? userId.trim().substring(6) : userId;
	}

	/**
	 * ֻ���("\""�Ȳ������ı�����ʾ���ַ�
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean isNotAllowed4TextBox(String arg) {
		if (Validators.isEmpty(arg)) {
			return false;
		}

		return arg.indexOf("\"") >= 0;
	}

	/**
	 * ����html��"'"�ַ�(ת��Ϊ"\'")�Լ����������ַ�, ��Ҫ����t�ӵ�ַ�������ַ����.
	 * 
	 * @param str
	 *            Ҫ���˵��ַ�
	 * @return ���˺���ַ�
	 */
	public static String linkFilter(String str) {
		if (Validators.isEmpty(str)) {
			return str;
		}

		return htmlFilter(htmlFilter(str.replaceAll("'", "\\\\'")));
	}

	/**
	 * <p>
	 * Replaces all occurrences of a String within another String.
	 * </p>
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, for the
	 * first <code>max</code> values of the search String.
	 */
	public static String replace(String text, String repl, String with, int max) {
		if (text == null || Validators.isEmpty(repl) || with == null
				|| max == 0) {
			return text;
		}

		StringBuffer buf = new StringBuffer(text.length());
		int start = 0, end = 0;
		while ((end = text.indexOf(repl, start)) != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();

			if (--max == 0) {
				break;
			}
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * ����ַ����Ŀո�
	 * 
	 * @param str
	 * @return
	 */
	public static String ltrim(String str) {
		return ltrim(str, " ");
	}

	/**
	 * ����ַ�����ָ���ַ�
	 * 
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String ltrim(String str, String remove) {
		if (str == null || str.length() == 0 || remove == null
				|| remove.length() == 0) {
			return str;
		}

		while (str.startsWith(remove)) {
			str = str.substring(remove.length());
		}
		return str;
	}

	/**
	 * ����ַ��Ҳ�Ŀո�
	 * 
	 * @param str
	 * @return
	 */
	public static String rtrim(String str) {
		return rtrim(str, " ");
	}

	/**
	 * ����ַ��Ҳ��ָ���ַ�
	 * 
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String rtrim(String str, String remove) {
		if (str == null || str.length() == 0 || remove == null
				|| remove.length() == 0) {
			return str;
		}

		while (str.endsWith(remove) && (str.length() - remove.length()) >= 0) {
			str = str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * ���ַ��չ���ָ����strΪ��id=123&name=test����ruleΪ��id=#&name=#�����ָ��Ϊ[
	 * "123", "test"];
	 * 
	 * @param str
	 * @param rule
	 * @return
	 */
	public static String[] split(String str, String rule) {
		if (rule.indexOf(SEPARATOR_SINGLE) == -1
				|| rule.indexOf(SEPARATOR_SINGLE + SEPARATOR_SINGLE) != -1) {
			throw new IllegalArgumentException("Could not parse rule");
		}

		String[] rules = rule.split(SEPARATOR_SINGLE);
		// System.out.println(rules.length);

		if (str == null || str.length() < rules[0].length()) {
			return new String[0];
		}

		boolean endsWithSeparator = rule.endsWith(SEPARATOR_SINGLE);

		String[] strs = new String[endsWithSeparator ? rules.length
				: rules.length - 1];
		if (rules[0].length() > 0 && !str.startsWith(rules[0])) {
			return new String[0];
		}

		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < strs.length; i++) {
			startIndex = str.indexOf(rules[i], endIndex) + rules[i].length();
			if (i + 1 == strs.length && endsWithSeparator) {
				endIndex = str.length();
			} else {
				endIndex = str.indexOf(rules[i + 1], startIndex);
			}

			// System.out.println(startIndex + "," + endIndex);

			if (startIndex == -1 || endIndex == -1) {
				return new String[0];
			}
			strs[i] = str.substring(startIndex, endIndex);
		}

		return strs;
	}

	/**
	 * �滻sql like���ֶ��е�ͨ����([]%_
	 * 
	 * @param str
	 * @return
	 */
	public static String sqlWildcardFilter(String str) {
		if (Validators.isEmpty(str)) {
			return str;
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '[') {
				stringBuffer.append("[[]");
			} else if (c == ']') {
				stringBuffer.append("[]]");
			} else if (c == '%') {
				stringBuffer.append("[%]");
			} else if (c == '_') {
				stringBuffer.append("[_]");
			} else {
				stringBuffer.append(c);
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * ���ַ���ָ�����ַ���б���
	 * 
	 * @param str
	 * @param charSetName
	 * @return
	 */
	public static String toCharSet(String str, String charSetName) {
		try {
			return new String(str.getBytes(), charSetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * ��һ���ֽ�����ת��Ϊ16���Ʊ����ַ�
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHexString(byte[] bytes) {
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			hexString
					.append(enoughZero(Integer.toHexString(bytes[i] & 0xff), 2));
		}
		return hexString.toString();
	}

	/**
	 * ��16���Ʊ����ַ�ת��Ϊ����
	 * 
	 * @param hexString
	 * @return
	 */
	public static int hexString2Int(String hexString) {
		return Integer.valueOf(hexString, 16).intValue();
	}

	/**
	 * ����ַ�}�ߵĿո�null������
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? str : str.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String
	 * returning an empty String ("") if the String is empty ("") after the trim
	 * or if it is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if <code>null</code> input
	 */
	public static String trimToEmpty(String str) {
		return (str == null ? "" : str.trim());
	}

	/**
	 * ����ַ��еĻس��ͻ��з�
	 * 
	 * @param str
	 * @return
	 */
	public static String ignoreEnter(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		return str.replaceAll("\r|\n", "");
	}

	/**
	 * ����»��ߣ����»��ߺ�����ĸת���ɴ�д��ĸ
	 * 
	 * @param str
	 * @return
	 */
	public static String underline2Uppercase(String str) {
		if (Validators.isEmpty(str)) {
			return str;
		}

		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '_' && i < charArray.length - 1) {
				charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);
			}
		}

		return new String(charArray).replaceAll("_", "");
	}

	private StringUtils() {
	}

	public static void main(String[] args) {
		System.out.println(sqlWildcardFilter("[[����test]]"));
		System.out.println(split("id=123&name=test", "id=#&name=#")[0]);
		System.out.println(cutOff("��įɳ��", 8, "��"));
		System.out.println(cutOut("��įɳ��", 8, "��"));
	}

}
