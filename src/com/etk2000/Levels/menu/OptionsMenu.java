package com.etk2000.Levels.menu;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.etk2000.FileHandling.DataFile;
import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.GameHandling.Localization;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.gui.Button;

public class OptionsMenu {

	/** return true if language changed **/
	public static boolean printMenu() {
		Button gamepackSelect = new Button(Localization.curLoc.getString("optionsMenu_selectGamepack"),
				DisplayHandler.width / 2, DisplayHandler.height / 1.9, DisplayHandler.width / 2 * 1.5f, 100);
		Button languageSelect = new Button(Localization.curLoc.getString("optionsMenu_selectLang"),
				DisplayHandler.width / 2, DisplayHandler.height / 3, DisplayHandler.width / 2 * 1.5f, 100);
		Button back = new Button(Localization.curLoc.getString("menu_back"), DisplayHandler.width / 2, 50,
				DisplayHandler.width / 2 * 1.5f, 75);

		Localization startLoc = Localization.curLoc;

		Display.setTitle("Getters / Setters");
		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			gamepackSelect.render();
			languageSelect.render();
			back.render();
			if (gamepackSelect.clicked())
				GamepackMenu.printMenu();
			else if (languageSelect.clicked()) {
				if (LocalizationMenu.printMenu()) {// relocalize all the buttons (if needed)
					gamepackSelect.setText(Localization.curLoc.getString("optionsMenu_selectGamepack"));
					languageSelect.setText(Localization.curLoc.getString("optionsMenu_selectLang"));
					back.setText(Localization.curLoc.getString("menu_back"));
				}
				Display.setTitle("Getters / Setters");
			}
			else if (back.clicked()) {
				saveSettings();
				if (startLoc != Localization.curLoc)
					return true;
				return false;
			}
			Display.update();
			Display.sync(60);
		}
		saveSettings();
		System.exit(0);
		return false;
	}

	private static void saveSettings() {
		DataFile df = new DataFile(GameBase.dataFolder + "/options.txt");
		df.save("lang: "
				+ Localization.curLoc.name()
				+ "\npack: "
				+ GamePackHandler.getCurrentTexturePack().getName()
						.substring(0, GamePackHandler.getCurrentTexturePack().getName().lastIndexOf(".zip")));
	}

	public static void loadSettings() {
		DataFile df = new DataFile(GameBase.dataFolder + "/options.txt");
		if (df.load().size() == 0)
			return;
		ArrayList<String> values = df.load();
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).contains("lang: "))
				Localization.curLoc = Localization
						.valueOf(values.get(i).substring(values.get(i).indexOf("lang: ") + 6));
			else if (values.get(i).contains("pack: "))
				GamePackHandler.loadTexturePack(GamePackHandler.TexturePackFolder + '/'
						+ values.get(i).substring(values.get(i).indexOf("pack: ") + 6) + ".zip");
		}
	}
}