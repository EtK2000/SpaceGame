package com.etk2000;

public class StringHandling {
	public static boolean isALetter(char c) {
		if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
			return true;
		return false;
	}

	public static String removeLastChar(String str) {
		return str.substring(0, str.length() - 1);
	}
	
	public static String flip(String str) {
		String newStr = new String();
		for (int i = str.length() - 1; i >= 0; i--)
			newStr += str.charAt(i);
		return newStr;
	}
	
	public static boolean isHebrew(String text) {
		for (int i = 0; i < '\u05F4' - '\u0591'; i++) {
			if (text.contains(new String(new char[] { (char) ('\u0591' + i) })))
				return true;
		}
		return false;
	}
}