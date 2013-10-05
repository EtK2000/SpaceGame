package com.etk2000.Levels;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.etk2000.Entity.Entity2D;
import com.etk2000.Entity.BadGuy.BasicBadGuy;
import com.etk2000.Entity.BadGuy.Boss;
import com.etk2000.Entity.BadGuy.ShootingBadGuy;
import com.etk2000.GameHandling.GameDelta;
import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.Levels.menu.GameOverMenu;
import com.etk2000.Levels.menu.SaveMenu;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.client.multiplayer.LanGame;

public class LevelHandler {
	private static ArrayList<Level> levels = new ArrayList<>();
	private static boolean initialized = false;

	private static LanGame lan = null;

	public static void Init() {
		if (!initialized) {
			Level level1 = new Level(400, 400);
			{
				level1.registerEntity(new BasicBadGuy(25, 25, 150, 50));
				level1.registerEntity(new BasicBadGuy(25, 25, 200, 50));
				level1.registerEntity(new BasicBadGuy(25, 25, 250, 50));
				level1.registerEntity(new BasicBadGuy(25, 25, 150, 100));
				level1.registerEntity(new BasicBadGuy(25, 25, 200, 100));
				level1.registerEntity(new BasicBadGuy(25, 25, 250, 100));
			}
			Level level2 = new Level(600, 400);
			{
				Entity2D bg1 = new ShootingBadGuy(50, 50, 150, 50);
				Entity2D bg2 = new ShootingBadGuy(50, 50, 200, 50);
				Entity2D bg3 = new ShootingBadGuy(50, 50, 250, 50);
				Entity2D bg4 = new ShootingBadGuy(50, 50, 150, 100);
				Entity2D bg5 = new ShootingBadGuy(50, 50, 200, 100);
				Entity2D bg6 = new ShootingBadGuy(50, 50, 250, 100);
				level2.registerEntity(bg1);
				level2.registerEntity(bg2);
				level2.registerEntity(bg3);
				level2.registerEntity(bg4);
				level2.registerEntity(bg5);
				level2.registerEntity(bg6);
			}
			Level level3 = new Level(600, 400);
			{
				Entity2D bg1 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg2 = new ShootingBadGuy(25, 25, 200, 50);
				Entity2D bg3 = new ShootingBadGuy(25, 25, 250, 50);
				Entity2D bg4 = new ShootingBadGuy(25, 25, 150, 100);
				Entity2D bg5 = new ShootingBadGuy(25, 25, 200, 100);
				Entity2D bg6 = new ShootingBadGuy(25, 25, 250, 100);
				level3.registerEntity(bg1);
				level3.registerEntity(bg2);
				level3.registerEntity(bg3);
				level3.registerEntity(bg4);
				level3.registerEntity(bg5);
				level3.registerEntity(bg6);
			}
			Level level4 = new Level(200, 400);
			{
				Entity2D bg1 = new Boss(25, 25, 55, 50, 20, 5, GamePackHandler.BGBasic);
				Entity2D bg2 = new Boss(25, 25, 80, 25, 20, 5, GamePackHandler.BGBasic);
				Entity2D bg3 = new Boss(25, 25, 105, 50, 20, 5, GamePackHandler.BGBasic);
				level4.registerEntity(bg1);
				level4.registerEntity(bg2);
				level4.registerEntity(bg3);
				level4.setAsBossLevel();
			}
			Level level5 = new Level(800, 600);
			{
				Entity2D bg1 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg2 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg3 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg4 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg5 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg6 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg7 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg8 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg9 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg10 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg11 = new ShootingBadGuy(25, 25, 150, 50);
				Entity2D bg12 = new ShootingBadGuy(25, 25, 150, 50);
				level5.registerEntity(bg1);
				level5.registerEntity(bg2);
				level5.registerEntity(bg3);
				level5.registerEntity(bg4);
				level5.registerEntity(bg5);
				level5.registerEntity(bg6);
				level5.registerEntity(bg7);
				level5.registerEntity(bg8);
				level5.registerEntity(bg9);
				level5.registerEntity(bg10);
				level5.registerEntity(bg11);
				level5.registerEntity(bg12);
			}
			registerLevel(level1);
			registerLevel(level2);
			registerLevel(level3);
			registerLevel(level4);
			registerLevel(level5);
		}
	}

	public static int numberOfLevels() {
		return levels.size();
	}

	public static Level loadLevel(int num) {
		num--;
		if (num < 0 || num >= levels.size())
			return null;
		return levels.get(num);
	}

	public static void registerLevel(Level lvl) {
		levels.add(lvl);
		System.out.println("added " + (lvl.isBossLevel() ? "BOSS L" : "level ") + levels.size() + ", size: "
				+ lvl.getWidth() + "x" + lvl.getHeight() + ", entities: " + lvl.numberOfEntities());
	}

	public static void renderAndUpdate() {
		Level lvl = getCurrentLevel();

		if (!GameBase.player.alive) {
			Mouse.setGrabbed(false);//show the cursor
			switch (GameOverMenu.printMenu()) {
				case "continue":
					GameBase.player.heal(20);
					lvl.reset();
					GameBase.currentScore = 0;
					break;
				case "yolo":
				default:
					System.exit(0);
			}
			Mouse.setGrabbed(true);//hide the cursor
		}

		// move all Entities
		GameBase.player.check4input();

		if (lan == null && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_O)) {
			lan = new LanGame();
			lan.Init();
		}

		// update all Entities
		int delta = (int) GameDelta.getDelta();
		GameBase.player.update(delta);
		if (lvl.updateLevel(delta)) {
			if (GameBase.currentLevel < LevelHandler.numberOfLevels()) {
				if (lvl.isBossLevel()) {
					Mouse.setGrabbed(false);
					SaveMenu.printMenu();
					Mouse.setGrabbed(true);
				}
				GameBase.currentLevel++;
				System.out.println("level up! new level: " + GameBase.currentLevel);
			}
			else {
				Mouse.setGrabbed(false);
				System.out.println("going 3D!");//beat all the 2D levels
				GameBase.is2D = false;
			}
		}

		// draw all Entities
		lvl.renderLevel();
		GameBase.player.draw();

		GameBase.renderPlayerHealthAndScore();
	}

	public static Level getCurrentLevel() {
		return loadLevel(GameBase.currentLevel);
	}
}