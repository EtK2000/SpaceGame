package com.etk2000.GameHandling;

import org.lwjgl.Sys;

public class GameDelta {
	private static long lastFrame;

	public static void Init() {
		lastFrame = getTime();
	}

	public static long getDelta() {
		long currentTime = getTime();
		long delta = currentTime - lastFrame;
		lastFrame = getTime();
		return delta;
	}

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}