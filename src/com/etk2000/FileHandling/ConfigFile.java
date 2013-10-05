package com.etk2000.FileHandling;

import com.etk2000.StringHandling;
import com.etk2000.SpaceGame.GameBase;

public class ConfigFile extends DataFile {
	private String path = null;
	private String data = "";

	public ConfigFile() {
		super(GameBase.dataFolder + "/temp/" + new java.util.Date());
		path = GameBase.dataFolder + "/temp/" + new java.util.Date();
	}

	public ConfigFile(String path) {
		super(path);
	}

	public void addLine(String line) {
		data += line + '\n';
	}

	public void save() {
		if (path.contains(":"))
			System.out.println("Saving Data In A Temporary Location! (" + path + ')');
		super.save(data);
	}

	public static String getData(String line) {
		if (!line.contains("="))
			return line;
		String data = line.substring(line.indexOf('=') + 1);// DON'T EDIT THE ARIGINAL
		while (data.length() > 0 && data.charAt(0) == ' ' && data.charAt(0) == '\n')
			data = data.substring(1);
		return StringHandling.removeLastChar(data);
	}

	public static String getTitle(String line) {
		String title = new String();
		String temp = new String();
		for (int i = 0; i < line.length() && line.charAt(i) != '='; i++) {
			temp += line.charAt(i);
			if(line.charAt(i) != ' ')
				title = temp;
		}
		return title;
	}
}