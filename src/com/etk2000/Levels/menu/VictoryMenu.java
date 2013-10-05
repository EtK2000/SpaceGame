package com.etk2000.Levels.menu;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;

import com.etk2000.GameHandling.Localization;
import com.etk2000.GameHandling.SoundHandler;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.Button;
import com.etk2000.gui.GUIText;

public class VictoryMenu {
	public static String printMenu() {
		Button returnToMenu = new Button(Localization.curLoc.getString("victory_returnToMenu"),
				DisplayHandler.width / 2, DisplayHandler.height / 2 + 100, DisplayHandler.width / 2, 100);
		Button quit = new Button(Localization.curLoc.getString("victory_quit"), DisplayHandler.width / 2,
				DisplayHandler.height / 2 - 100, DisplayHandler.width / 2, 100);
		GUIText victory = new GUIText("Victory", DisplayHandler.width / 2, DisplayHandler.height / 2 + 200);
		Audio backgroundMusic = SoundHandler.playWav("com/etk2000/SpaceGame/Music/VictoryMusic.wav", true);

		while (true) {
			DisplayHandler.clearDisplay();

			glColor3f(new Random().nextFloat() / new Random().nextFloat(),
					new Random().nextFloat() / new Random().nextFloat(),
					new Random().nextFloat() / new Random().nextFloat());
			victory.DrawString();
			glColor3f(1, 1, 1);

			returnToMenu.render();
			quit.render();
			if (returnToMenu.clicked()) {
				backgroundMusic.stop();
				return MainMenu.printMenu();
			}
			else if (quit.clicked())
				System.exit(0);
			
			Display.update();
			Display.sync(60);
		}
	}
}