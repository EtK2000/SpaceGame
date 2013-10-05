package com.etk2000.FileHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataFile {
	protected final File file;

	public DataFile(String path) {
		file = new File(path);
		if (!file.exists()) {// doesn't exist
			new File(file.getParent()).mkdirs();

			try {
				file.createNewFile();
			}
			catch (IOException e) {
			}
		}
	}

	public ArrayList<String> load() {
		try {
			ArrayList<String> text = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null)// read a line
				text.add(line);
			br.close();
			return text;// if it skips the while, this should be null!
		}
		catch (IOException e) {
		}
		return null;
	}

	public void save(String text) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String[] splitText = text.split("\n");
			for (int i = 0; i < splitText.length; i++) {
				bw.write(splitText[i]);
				bw.newLine();
			}
			bw.flush();// just to be safe
			bw.close();
		}
		catch (IOException e) {
		}
	}
}