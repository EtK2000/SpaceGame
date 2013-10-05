package com.etk2000.Levels.menu;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.etk2000.FileHandling.DataFile;
import com.etk2000.GameHandling.Localization;
import com.etk2000.Levels.MPLanGame;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.gui.Button;
import com.etk2000.gui.ScrollArea;

public class SelectServer {
	public static String serverIP = "0.0.0.0";

	public static void printMenu() {
		Button back = new Button(Localization.curLoc.getString("menu_back"), DisplayHandler.width / 2, 50,
				DisplayHandler.width / 2 * 1.5f, 75);
		ScrollArea selectionArea = new ScrollArea(10, DisplayHandler.height / 2 - 50, DisplayHandler.width - 20,
				DisplayHandler.height - 150);

		DataFile servers = new DataFile(GameBase.dataFolder + "/servers.txt");
		ArrayList<String> savedServers = servers.load();
		for (int i = 0; i < savedServers.size(); i++) {
			selectionArea.addButton(savedServers.get(i));// TODO: auto-find, ping
		}

		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();

			back.render();
			selectionArea.render();

			if (back.clicked())
				return;

			int buttonClicked = selectionArea.update();
			if (buttonClicked >= 0) {
				if (Keyboard.isKeyDown(Keyboard.KEY_DELETE))
					selectionArea.removeButton(buttonClicked);
				else {
					serverIP = selectionArea.getButton(buttonClicked).getText();
					new MPLanGame().start();
				}
			}

			Display.update();
			Display.sync(60);
		}

		System.exit(0);
	}
}