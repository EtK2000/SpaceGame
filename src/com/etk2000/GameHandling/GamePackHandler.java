package com.etk2000.GameHandling;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.etk2000.FileHandling.FileDownloading;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.osClasses.OS;

public class GamePackHandler {
	public final static String TexturePackFolder = OS.appData() + "/.EtK2000/SpaceGame/texturepacks";
	private static File currentTexturePack;

	/** TEXTURES **/
	public static Texture nullTex = null;
	public static Texture BGBasic = null;
	public static Texture BGShooter = null;
	public static Texture GUIArrow_down = null;
	public static Texture GUIArrow_up = null;
	public static Texture GUIButton = null;
	public static Texture HealthBar_Boss = null;
	public static Texture laserBasic = null;
	public static Texture thePlayer = null;

	/** LOCALIZATIONS **/
	public static boolean usingCustomLocalization = false;
	public static Localization loc;
	public static String fontLoc = "res/fonts/Times New Roman.ttf";

	public static void Init() {
		File TPFolder = new File(TexturePackFolder);
		if (!TPFolder.exists())// create the TexturePack folder
			TPFolder.mkdirs();
		if (!(new File(TexturePackFolder + "/default.zip").exists())) {
			GameBase.logger.info("Downloading Default Textures...");
			FileDownloading
					.download(
							"https://dl.dropboxusercontent.com/s/bo95fomhsaffayj/default.zip?token_hash=AAHJ-kNjo2YB-4niZRzzNHxkdtxCP-D7IcEsmeTyD-GiAg&dl=1",
							TexturePackFolder + "/default.zip");
		}
	}

	public static void downloadDefaultSounds(boolean... force) {
		File f = new File(GameBase.dataFolder + "/res.zip");
		if (!f.exists() || (force.length > 0 && force[0]))// if no file, or forced
			FileDownloading.download("https://dl.dropboxusercontent.com/u/44829607/SpaceGame_Resources.zip",
					GameBase.dataFolder + "/res.zip");
	}
	
	public static ArrayList<String> listAllTexturePacks() {
		ArrayList<String> packs = new ArrayList<>();
		File[] tmp = (new File(TexturePackFolder).listFiles());
		for (int i = 0; i < tmp.length; i++) {// only find "zip" files
			if (tmp[i].getAbsolutePath().indexOf(".zip") == tmp[i].getAbsolutePath().length() - 4)
				packs.add(tmp[i].getName().substring(0, tmp[i].getName().indexOf(".zip")));
		}
		for (int i = 0; i < packs.size(); i++)
			System.out.println((i + 1) + ") " + packs.get(i));

		return packs;
	}

	public static void loadTexturePack(File TexturePack) {
		currentTexturePack = TexturePack;
		try {
			reloadTexturePack();// also works as load...
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reloadTexturePack() throws IOException {
		nullTex = TextureLoader.getTexture("PNG", GamePackHandler.class.getResourceAsStream("/textures/missing.png"));
		// read the "missing" texture from the Jar
		BGBasic = nullTex;
		BGShooter = nullTex;
		GUIArrow_down = nullTex;
		GUIArrow_up = nullTex;
		GUIButton = nullTex;
		HealthBar_Boss = nullTex;
		laserBasic = nullTex;
		thePlayer = nullTex;

		fontLoc = "fonts/Times New Roman.ttf";

		ZipFile zip = new ZipFile(currentTexturePack);
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			switch (entry.getName()) {
				case "badieBasic.png":
					BGBasic = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "badieShooter.png":
					BGShooter = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "GUI_arrowDown.png":
					GUIArrow_down = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "GUI_arrowUp.png":
					GUIArrow_up = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "GUI_button.png":
					GUIButton = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "HealthBar_Boss.png":
					HealthBar_Boss = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "laserBasic.png":
					laserBasic = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
				case "player.png":
					thePlayer = TextureLoader.getTexture("PNG", zip.getInputStream(entry));
					break;
			}
		}
		zip.close();
	}

	public static File getCurrentTexturePack() {
		return currentTexturePack;
	}

	public static String getLocalization() {
		if (usingCustomLocalization)
			return currentTexturePack.getAbsolutePath();
		return null;
	}
}