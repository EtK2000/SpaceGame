package com.etk2000.GameHandling;

import java.io.IOException;
import java.util.ArrayList;

import com.etk2000.FileHandling.ConfigFile;
import com.etk2000.FileHandling.FileFunctions;

public enum Localization {
	custom, // from a Mod or GamePack
	en_uk, en_us, he, pirate;

	public static Localization curLoc = en_us;

	private ArrayList<String> localizedStrings;
	private ArrayList<String> stringIndex;

	private boolean isLoaded = false;

	public String displayName() {
		return getLocalizedString("displayName");
	}

	public void unloadLocalization(Localization loc) {
		loc.localizedStrings.clear();
		loc.stringIndex.clear();
		loc.isLoaded = false;
		loc = null;
	}

	private String getLocalizedString(String str) {
		String loc = this.name();
		boolean loaded = Localization.valueOf(loc).isLoaded;

		if (!loaded) {
			localizedStrings = new ArrayList<String>();
			stringIndex = new ArrayList<String>();
			String path = "localization/" + loc + ".txt";
			System.out.println(this.name() + ": " + path);
			if (loc.equals("custom")) {
				path = GamePackHandler.getLocalization();
			}

			try {
				ArrayList<String> lines = FileFunctions.readFileFromJarAsString(path);
				for (int i = 0; i < lines.size(); i++) {
					String index = ConfigFile.getTitle(lines.get(i));
					if (index != "" && !stringIndex.contains(index)) {
						stringIndex.add(index);
						localizedStrings.add(ConfigFile.getData(lines.get(i)));
					}
				}
				isLoaded = true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return localizedStrings.get(stringIndex.indexOf(str));
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e) {
			return str;
		}
	}

	public String getString(String str) {
		return getLocalizedString(str);
	}
}