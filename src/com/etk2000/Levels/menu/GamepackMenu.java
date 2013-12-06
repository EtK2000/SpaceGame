package com.etk2000.Levels.menu;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.GameHandling.Localization;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.Button;
import com.etk2000.gui.ScrollArea;

public class GamepackMenu {
	public static void printMenu() {
		Button back = new Button(Localization.curLoc.getString("menu_back"), DisplayHandler.width / 2, 50,
				DisplayHandler.width / 2 * 1.5f, 75);
		ScrollArea selectionArea = new ScrollArea(10, DisplayHandler.height / 2 - 50, DisplayHandler.width - 20,
				DisplayHandler.height - 150);

		// add all the buttons
		ArrayList<String> allPacks = GamePackHandler.listAllTexturePacks();
		for (int i = 0; i < allPacks.size(); i++)
			selectionArea.addButton(allPacks.get(i));

		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			back.render();
			selectionArea.render();
			if (back.clicked())
				return;

			int buttonClicked = selectionArea.update();
			if (buttonClicked >= 0)
				GamePackHandler.loadTexturePack(GamePackHandler.TexturePackFolder + '/' + allPacks.get(buttonClicked)
						+ ".zip");

			Display.update();
			Display.sync(60);
		}
		System.exit(0);
	}
}