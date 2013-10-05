package com.etk2000.FileHandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileFunctions {
	public static ArrayList<String> readFileAsString(String path) throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return String2ArrayList(fileData.toString());
	}

	public static ArrayList<String> readFileFromJarAsString(String path) throws IOException {
		InputStream is = FileFunctions.class.getResourceAsStream('/' + path);// "/localization/en_us.txt"

		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		is.close();
		return String2ArrayList(fileData.toString());
	}

	public static ArrayList<String> String2ArrayList(String str) {// TODO: remove \n from lines
		ArrayList<String> al = new ArrayList<String>();
		String temp = new String();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				if (temp.length() > 0) {
					al.add(temp);
					temp = new String();
				}
				continue;
			}
			temp += str.charAt(i);
		}
		return al;
	}
}