package com.etk2000.Levels.menu;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.etk2000.FileHandling.DataFile;
import com.etk2000.GameHandling.Localization;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.gui.Button;

public class SaveMenu {
	public static int saveNum = 1;

	public static void printMenu() {
		Button save = new Button(Localization.curLoc.getString("menu_save"), DisplayHandler.width / 2, 150,
				DisplayHandler.width / 2 * 1.5f, 75);
		Button continueNoSave = new Button(Localization.curLoc.getString("menu_continueNoSave"),
				DisplayHandler.width / 2, 50, DisplayHandler.width / 2 * 1.5f, 75);

		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			save.render();
			continueNoSave.render();

			if (save.clicked()) {
				savePlayerData();
				return;
			}
			else if (continueNoSave.clicked())
				return;

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}

	public static void loadPlayerData() {
		DataFile saveFile = new DataFile(GameBase.dataFolder + "/saves/" + saveNum + ".sav");
		if (saveFile.load().size() == 0)
			return;
		ArrayList<String> values = saveFile.load();
		for (int i = 0; i < values.size(); i++) {
			try {
				if (values.get(i).contains("health: "))// TODO: tweak
					GameBase.player.setHealth(Float.parseFloat(values.get(i)
							.substring(values.get(i).indexOf("health: ") + 7).trim()));
				else if (values.get(i).contains("level: "))
					GameBase.currentLevel = Integer.parseInt(values.get(i)
							.substring(values.get(i).indexOf("level: ") + 7).trim());
				else if (values.get(i).contains("score: "))
					GameBase.currentScore = Integer.parseInt(values.get(i)
							.substring(values.get(i).indexOf("score: ") + 7).trim());
				System.out.println('"' +  values.get(i).trim() + '"');
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void savePlayerData() {
		DataFile saveFile = new DataFile(GameBase.dataFolder + "/saves/" + saveNum + ".sav");
		saveFile.save("level: " + (GameBase.currentLevel + 1) + "\nhealth: " + GameBase.player.getHealth() + "\nscore: "
				+ GameBase.currentScore);
	}
}