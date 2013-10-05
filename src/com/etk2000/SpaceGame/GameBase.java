package com.etk2000.SpaceGame;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.ImageIOImageData;

import com.etk2000.GameHandling.CrashReport;
import com.etk2000.GameHandling.GameDelta;
import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.GameHandling.GameType;
import com.etk2000.Entity.Ship_Player;
import com.etk2000.Levels.LevelHandler;
import com.etk2000.Levels.LevelHandler3D;
import com.etk2000.Levels.menu.MainMenu;
import com.etk2000.Levels.menu.OptionsMenu;
import com.etk2000.Levels.menu.SaveMenu;
import com.etk2000.Levels.menu.VictoryMenu;
import com.etk2000.ModLoading.ModLoader;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.SideThreads.SideThread;
import com.etk2000.gui.GUIText;
import com.etk2000.osClasses.OS;

public class GameBase {
	public static final String version = "0.1";
	public static int currentLevel = 1, currentScore = 0;
	public static Ship_Player player;// TODO: make final
	public static final String dataFolder = OS.appData() + "/.EtK2000/SpaceGame";
	public static String path = new File("").getAbsolutePath() + '\\' + System.getProperty("java.class.path");
	public static boolean isMidLevel = false, hasPlayerWon = false, is2D = true;
	public static boolean isIDE = path.contains(";");
	public static final Logger logger = Logger.getLogger("SpaceGame");
	public static UnicodeFont font;

	private static final DisplayHandler dh = new DisplayHandler();

	public static String username = "";

	private static ArrayList<Runnable> addedRunnables = new ArrayList<>();

	/** Load The Junk **/
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		OptionsMenu.loadSettings();
		if (isIDE)
			path = System.getProperty("user.dir");

		/** Load All Arguments **/
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--IDE"))
				isIDE = true;
			else if (args[i].equalsIgnoreCase("--username")) {
				if (i + 1 < args.length)
					username = args[++i];
				System.out.println((!username.equals("") ? "set username to " + username
						: "username argument corrupted!"));
			}
		}

		/** Create The Display **/
		try {
			Display.setDisplayMode(new DisplayMode(DisplayHandler.width, DisplayHandler.height));
			// Display.setIcon(new ByteBuffer[] { MyTools.getIcon(dataFolder +
			// "/res/images/icon.png"),
			// MyTools.getIcon(dataFolder + "/res/images/icon.png") });
			Display.setIcon(new ByteBuffer[] {
					new ImageIOImageData().imageToByteBuffer(
							ImageIO.read(new File(dataFolder + "/res/images/iconx16.png")), false, false, null),
					new ImageIOImageData().imageToByteBuffer(
							ImageIO.read(new File(dataFolder + "/res/images/iconx32.png")), false, false, null),
					new ImageIOImageData().imageToByteBuffer(
							ImageIO.read(new File(dataFolder + "/res/images/iconx64.png")), false, false, null),
					new ImageIOImageData().imageToByteBuffer(
							ImageIO.read(new File(dataFolder + "/res/images/iconx128.png")), false, false, null) });
			Display.setResizable(true);
			Display.setVSyncEnabled(true);
			Display.create();
		}
		catch (LWJGLException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		/** The actual game **/
		try {// try that contains everything
				// OpenGL Init
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
			glEnable(GL_TEXTURE_2D);

			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			/** Setup The Global Font **/
			font = new UnicodeFont(GamePackHandler.fontLoc, 18, false, false);
			font.addAsciiGlyphs();
			font.addGlyphs('\u0591', '\u05F4');// add Hebrew chars
			font.getEffects().add(new ColorEffect());
			font.loadGlyphs();

			GamePackHandler.Init();
			GamePackHandler.listAllTexturePacks();// sets up the .EtK2000/SpaceGame/TexturePack/
			GamePackHandler.loadTexturePack(new File(dataFolder + "/texturepacks/default.zip"));
			GamePackHandler.downloadDefaultSounds();

			player = new Ship_Player(25, 25, DisplayHandler.width / 2, DisplayHandler.height / 2);
			SaveMenu.loadPlayerData();

			// Start The Display Update Thread
			dh.Init();
			Runtime.getRuntime().addShutdownHook(new Thread(SideThread.killAll()));

			LevelHandler.Init();// initialize all predefined levels
			GameDelta.Init();// initialize game delta

			// LevelCreator.open();

			ModLoader.Init();

			switch (MainMenu.printMenu()) {
				case "play":
					GameType.curType = GameType.Adventure;
					isMidLevel = true;
					hasPlayerWon = false;
					break;
				default:
					System.exit(0);
			}

			Mouse.setGrabbed(true);// hide the cursor
			while (!Display.isCloseRequested()) {
				// OpenGL Render Code
				DisplayHandler.clearDisplay();

				Display.setTitle("Level " + GameBase.currentLevel);

				if (!hasPlayerWon) {
					if (is2D)
						LevelHandler.renderAndUpdate();
					else {
						LevelHandler3D.renderAndUpdate();
					}
				}
				else {
					GameType.curType = null;
					isMidLevel = false;
					switch (VictoryMenu.printMenu()) {
						case "play":
							currentLevel = 1;
							GameType.curType = GameType.Adventure;
							isMidLevel = true;
							hasPlayerWon = false;
							player.fullHeal();
							for (int i = 1; i <= LevelHandler.numberOfLevels(); i++)
								LevelHandler.loadLevel(i).reset();
							break;
						default:
							System.exit(0);
					}
				}

				/** Render All The Runnables **/
				for (int i = 0; i < addedRunnables.size(); i++)
					addedRunnables.get(i).run();

				Display.update();
				Display.sync(60);
			}
		}
		catch (Throwable throwable) {// catch any error from the game
			CrashReport cr = new CrashReport(throwable.getMessage(), throwable);
			cr.render();
		}

		Runtime.getRuntime().exit(0);
		// Shutdown Code
	}

	public static void freeMemory() {
		SideThread.killAll().run();
		dh.Init(dh);
	}

	public static void addToRenderThread(Runnable runnable) {
		if (!addedRunnables.contains(runnable))
			addedRunnables.add(runnable);
		else
			logger.severe("Render Thread Already Contains A Runnable With This Name, Please Change Your Runnable's Name!");
	}

	public static void removeFromRenderThread(Runnable runnable) {
		addedRunnables.remove(runnable);
	}

	public static void renderPlayerHealthAndScore() {
		glColor3f((20 - player.getHealth()) / 20, player.getHealth() / 20, 0);
		new GUIText("Health: " + (int) player.getHealth(), DisplayHandler.width / 2, 32).DrawString();
		glColor3f(1, 1, 1);
		new GUIText("Score: " + currentScore, DisplayHandler.width / 2, 16).DrawString();
	}
}