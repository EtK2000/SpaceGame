package com.etk2000.Levels;

import java.util.ArrayList;

public class LevelHandler3D {
	private static boolean didInit = false;
	private static ArrayList<Level3D> levels = new ArrayList<>();
	
	public static void renderAndUpdate() {
		if(!didInit)
			Init();
		
	}
	
	public static void Init() {
		
	}
}