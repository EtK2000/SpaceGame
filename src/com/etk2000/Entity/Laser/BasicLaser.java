package com.etk2000.Entity.Laser;

import static org.lwjgl.opengl.GL11.*;

import com.etk2000.Entity.Entity2D;
import com.etk2000.GameHandling.GamePackHandler;

public class BasicLaser extends Laser {
	public static final float laserWidth = 2, laserHeight = 10;

	public BasicLaser(double x, double y) {
		super(laserWidth, laserHeight, 0, 0.25f);
		texture = GamePackHandler.laserBasic;
		power = 1;
		this.x = x;
		this.y = y;
	}

	public BasicLaser(double x, double y, float speedY) {
		super(laserWidth, laserHeight, 0, speedY);
		texture = GamePackHandler.laserBasic;
		power = 1;
		if (speedY < 0)
			fromBadie = true;
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw() {
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2f(1, 1);// Texture: Upper-Left
			glVertex2d(x + width / 2, y + height / 2);// Upper-Left
			glTexCoord2f(0, 1);// Texture: Upper-Right
			glVertex2d(x - width / 2, y + height / 2);// Upper-Right
			glTexCoord2f(0, 0);// Texture: Lower-Right
			glVertex2d(x - width / 2, y - height / 2);// Lower-Right
			glTexCoord2f(1, 0);// Texture: Lower-Left
			glVertex2d(x + width / 2, y - height / 2);// Lower-Left
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}

	@Override
	public Entity2D getEntity() {
		return new BasicLaser(laserWidth, laserHeight, speedY);
	}
}