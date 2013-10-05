package com.etk2000.Levels.menu;

import org.lwjgl.opengl.Display;

import com.etk2000.GameHandling.Localization;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.Button;
import com.etk2000.gui.ScrollArea;

public class LocalizationMenu {
	/** return true if language changed **/
	public static boolean printMenu() {
		Button back = new Button(Localization.curLoc.getString("menu_back"), DisplayHandler.width / 2, 50,
				DisplayHandler.width / 2 * 1.5f, 75);
		ScrollArea selectionArea = new ScrollArea(10, DisplayHandler.height / 2 - 50, DisplayHandler.width - 20,
				DisplayHandler.height - 150);

		Localization startLoc = Localization.curLoc;

		// add all the buttons
		for (int i = 0; i < Localization.values().length; i++) {
			if (Localization.values()[i] != Localization.custom)
				selectionArea.addButton(Localization.values()[i].displayName());
		}

		String lastLoc = Localization.curLoc.name();
		Display.setTitle("Select Your Language, Current Language: " + lastLoc);

		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			back.render();
			selectionArea.render();
			if (back.clicked()) {
				return startLoc != Localization.curLoc;
			}

			int langButtonClicked = selectionArea.update();
			if (langButtonClicked >= 0) {
				Localization.curLoc = Localization.values()[(Localization.valueOf("custom").ordinal() <= langButtonClicked ? ++langButtonClicked
						: langButtonClicked)];
			}

			Display.update();
			Display.sync(60);
			if (!lastLoc.equals(Localization.curLoc.name())) {
				back.setText(Localization.curLoc.getString("menu_back"));
				lastLoc = Localization.curLoc.name();
				Display.setTitle("Sucessfully Changed Language To " + lastLoc);
			}
		}
		System.exit(0);
		return false;
	}
}