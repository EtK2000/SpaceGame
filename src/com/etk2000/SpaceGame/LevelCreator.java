package com.etk2000.SpaceGame;

import java.awt.Color;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.GUIFont;

public class LevelCreator {
	@SuppressWarnings("unchecked")
	public static void open() {
		UnicodeFont font = null;
		DisplayMode oldDisplayMode = Display.getDisplayMode();
		Display.destroy();
		try {
			Display.setDisplayMode(new DisplayMode(750, 750));
			Display.create();
			Display.setResizable(false);
			font = new UnicodeFont(GamePackHandler.fontLoc, 28, false, false);
			font.addAsciiGlyphs();
			font.getEffects().add(new ColorEffect(Color.white));
			font.loadGlyphs();
		}
		catch (SlickException | LWJGLException e) {
			e.printStackTrace();
		}
		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			GUIFont.center(font, DisplayHandler.width / 2, font.getLineHeight() / 2, "SpaceGame Level Creator");
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		try {
			Display.setDisplayMode(oldDisplayMode);
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}