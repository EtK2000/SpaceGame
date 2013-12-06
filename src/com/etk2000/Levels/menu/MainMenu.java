package com.etk2000.Levels.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;

import com.etk2000.GameHandling.Localization;
import com.etk2000.GameHandling.SoundHandler;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.Button;

public class MainMenu {
	public static String printMenu() {
		Button play = new Button(Localization.curLoc.getString("mainMenu_play"), DisplayHandler.width / 2,
				DisplayHandler.height / 2 + 165, DisplayHandler.width / 2 * 1.5f, 100);
		Button multiPlayer = new Button(Localization.curLoc.getString("mainMenu_multiPlayer"),
				DisplayHandler.width / 2, DisplayHandler.height / 2 + 55, DisplayHandler.width / 2 * 1.5f, 100);
		Button options = new Button(Localization.curLoc.getString("mainMenu_options"), DisplayHandler.width / 2,
				DisplayHandler.height / 2 - 55, DisplayHandler.width / 2 * 1.5f, 100);
		Button quit = new Button(Localization.curLoc.getString("mainMenu_quit"), DisplayHandler.width / 2,
				DisplayHandler.height / 2 - 165, DisplayHandler.width / 2 * 1.5f, 100);
		Display.setTitle("Main Menu");
		Audio backgroundMusic = SoundHandler.playWav("res/sounds/music/MainMenuMusic.wav", true);

		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			play.render();
			multiPlayer.render();
			options.render();
			quit.render();
			if (play.clicked()) {
				backgroundMusic.stop();
				return "play";
			}
			else if (multiPlayer.clicked()) {
				SelectServer.printMenu();
			}
			else if (options.clicked()) {
				if (OptionsMenu.printMenu()) {
					play.setText(Localization.curLoc.getString("mainMenu_play"));
					multiPlayer.setText(Localization.curLoc.getString("mainMenu_multiPlayer"));
					options.setText(Localization.curLoc.getString("mainMenu_options"));
					quit.setText(Localization.curLoc.getString("mainMenu_quit"));
				}
				Display.setTitle("Main Menu");
			}
			else if (quit.clicked())
				break;
			Display.update();
			Display.sync(60);
		}
		System.exit(0);
		return null;
	}
}