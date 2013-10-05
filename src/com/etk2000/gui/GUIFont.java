package com.etk2000.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import com.etk2000.StringHandling;
import com.etk2000.SideThreads.DisplayHandler;

public class GUIFont {
	public static void center(UnicodeFont font, float x, float y, String text) {
		float tempX = x - font.getWidth(text) / 2;
		float tempY = y - font.getHeight(text) / 2;
		String tempText = text;
		if(StringHandling.isHebrew(text))
			tempText = StringHandling.flip(text);
		font.drawString(tempX, tempY, tempText);
	}
	
	public static void center(UnicodeFont font, float x, float y, String text, Color col) {
		float tempX = x - font.getWidth(text) / 2;
		float tempY = y - font.getHeight(text) / 2;
		String tempText = text;
		if(StringHandling.isHebrew(text))
			tempText = StringHandling.flip(text);
		font.drawString(tempX, tempY, tempText, col);
	}

	public static void render(UnicodeFont font) {
		center(font, DisplayHandler.width / 2, DisplayHandler.height / 2, "שלום");
	}
}