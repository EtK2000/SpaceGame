package com.etk2000.Levels.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;

import com.etk2000.GameHandling.Localization;
import com.etk2000.GameHandling.SoundHandler;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.Button;

public class GameOverMenu {
	public static String printMenu() {
		Button play = new Button(Localization.curLoc.getString("gameOver_tryAgain"), DisplayHandler.width / 2, DisplayHandler.height / 2 + 55,
				DisplayHandler.width / 2 * 1.5f, 100);
		Button yolo = new Button(Localization.curLoc.getString("gameOver_quit"), DisplayHandler.width / 2, DisplayHandler.height / 2 - 55,
				DisplayHandler.width / 2 * 1.5f, 100);
		Display.setTitle("Game Over");
		Audio backgroundMusic = SoundHandler.playWav("res/sounds/music/GameOverMusic.wav", true);

		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			play.render();
			yolo.render();
			if (play.clicked()) {
				backgroundMusic.stop();
				return "continue";
			}
			if (yolo.clicked()) {
				backgroundMusic.stop();
				return "yolo";
			}
			Display.update();
			Display.sync(60);
		}
		System.exit(0);
		return null;// apparently needed?!
	}
}