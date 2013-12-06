package com.etk2000.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.SideThreads.DisplayHandler;

public class Button {
	protected static UnicodeFont font = null;
	protected String text;
	protected double x, y;
	protected float width, height;
	private String textureName;
	protected boolean ready = false, selected = false, selectable = true;
	// ready to be selected, selected, can be selected
	private int oldScreenWidth = DisplayHandler.width, oldScreenHeight = DisplayHandler.height;

	public Button(String text, double x, double y, float width, float height) {
		textureName = "GUIButton";
		this.text = text;
		if (font == null)
			setupFont();
		this.x = x;
		this.y = DisplayHandler.height - y;
		this.width = width;
		this.height = height;
	}

	public Button(String text, double x, double y, float width, float height, String textureName) {
		this.textureName = textureName;
		this.text = text;
		if (font == null)
			setupFont();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean clicked() {
		if (!ready && mouseOver() && !Mouse.isButtonDown(0))
			ready = true;
		else if (ready && mouseOver() && Mouse.isButtonDown(0))
			selected = true;
		else if (ready && selected && mouseOver() && !Mouse.isButtonDown(0)) {
			selected = false;
			ready = false;
			return true;
		}
		else if (selected && Mouse.isButtonDown(0))
			;
		else if (!mouseOver() && Mouse.isButtonDown(0))
			ready = false;
		else
			selected = false;
		return false;
	}

	private boolean mouseOver() {
		int mouseY = DisplayHandler.height - Mouse.getY();
		if (Mouse.isInsideWindow() && Mouse.getX() >= this.x - this.width / 2
				&& Mouse.getX() <= this.x + this.width / 2 && mouseY >= this.y - this.height / 2
				&& mouseY <= this.y + this.height / 2)
			return true;
		return false;
	}

	public void render() {
		// TODO: fix a few resizing bugs (like y center)
		if (DisplayHandler.height != oldScreenHeight) {
			height = height * DisplayHandler.height / oldScreenHeight;
			y = DisplayHandler.height - y * DisplayHandler.height / oldScreenHeight;
			y = DisplayHandler.height - y * DisplayHandler.height / oldScreenHeight;// fix flip bug
			oldScreenHeight = DisplayHandler.height;
		}
		if (DisplayHandler.width != oldScreenWidth) {
			width = width * DisplayHandler.width / oldScreenWidth;
			x = x * DisplayHandler.width / oldScreenWidth;
			oldScreenWidth = DisplayHandler.width;
		}
		glEnable(GL_TEXTURE_2D);
		GamePackHandler.getTexture(textureName).bind();
		glBegin(GL_QUADS);
		{
			if (!selectable) {
				glColor3f(0.25f, 0.25f, 0.25f);
				selectable = true;
			}
			else if (mouseOver() && ready)
				glColor3f(0.5f, 1, 0.5f);
			glTexCoord2f(1, 1);// Texture: Upper-Left
			glVertex2d(x + width / 2, y + height / 2);
			glTexCoord2f(0, 1);// Texture: Upper-Right
			glVertex2d(x - width / 2, y + height / 2);// Upper-Right
			glTexCoord2f(0, 0);// Texture: Lower-Right
			glVertex2d(x - width / 2, y - height / 2);// Lower-Right
			glTexCoord2f(1, 0);// Texture: Lower-Left
			glVertex2d(x + width / 2, y - height / 2);// Lower-Left
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		GUIFont.center(font, (float) x, (float) y, text);
	}

	@SuppressWarnings("unchecked")
	public void setupFont() {// should only be called from Init of first button
		try {
			font = new UnicodeFont(GamePackHandler.fontLoc, 20, false, false);
			font.addAsciiGlyphs();
			font.addGlyphs('\u0591', '\u05F4');// add Hebrew chars
			font.getEffects().add(new ColorEffect(Color.black));
			font.loadGlyphs();
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < font.getGlyphPages().size(); i++)
			System.out.println(font.getGlyphPages().get(i));
	}

	public void setText(String text) {
		this.text = text;
		// setupFont();//removed due to static
	}

	public String getText() {
		return text;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
}