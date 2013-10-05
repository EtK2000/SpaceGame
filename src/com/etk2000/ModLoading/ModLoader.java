package com.etk2000.ModLoading;

import java.io.File;
import java.util.ArrayList;

import com.etk2000.SpaceGame.GameBase;

public class ModLoader {
	/** This Is A Basic ModLoader, It Could Be Overridden **/
	private static final File modsFolder = new File(GameBase.dataFolder + "/mods");
	public static ArrayList<Mod> mods = new ArrayList<>();
	
	public static void Init() throws Exception {
		if (!modsFolder.exists())
			modsFolder.mkdirs();
		if (!modsFolder.isDirectory())
			throw new UnsupportedOperationException('"' + modsFolder.getAbsolutePath() + "\" must be a directory!");
		
		loadMods();
		InitMods();
	}
	
	public static void loadMods() {
		mods.add(new ModHealthThread());
		//for()
	}
	
	public static void InitMods() throws Exception {//So It Can Stop The Main Thread
		for(int i = 0; i < mods.size(); i++)
			mods.get(i).Init();
	}
}