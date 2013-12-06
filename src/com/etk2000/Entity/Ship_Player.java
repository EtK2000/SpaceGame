package com.etk2000.Entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.etk2000.Entity.Laser.BasicLaser;
import com.etk2000.Entity.Laser.Laser;
import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.Levels.Level;
import com.etk2000.Levels.LevelHandler;
import com.etk2000.SpaceGame.GameBase;

public class Ship_Player extends Entity2DAbstract {
	protected ArrayList<Laser> lasers = new ArrayList<Laser>();
	private int firedLastFrame = 0;

	public Ship_Player(int width, int height, double x, double y) {
		this(width, height, x, y, 0.5f, 0.5f);
	}

	public Ship_Player(int width, int height, double x, double y, float speedX, float speedY) {
		textureName = "thePlayer";
		this.health = 20;// set max health
		this.fullHealth = 20;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		setLocation(x, y);
		this.speedX = speedX;
		this.speedY = speedY;
	}

	@Override
	public void destroy() {
		this.alive = false;
	}

	@Override
	public void kill() {
		this.dying = true;
	}

	@Override
	public void draw() {// x, y is the center
		glEnable(GL_TEXTURE_2D);
		GamePackHandler.getTexture(textureName).bind();
		/*
		 * glBegin(GL_QUADS);
		 * {
		 * glTexCoord2f(0, 0);// Texture: Upper-Left
		 * glVertex2d(x, y - height / 2);// Upper-Center
		 * glTexCoord2f(1, 0);// Texture: Upper-Right
		 * glVertex2d(x, y - height / 2);// Upper-Center
		 * glTexCoord2f(1, 1);// Texture: Lower-Right
		 * glVertex2d(x + width / 2, y + height / 2);// Lower-Right
		 * glTexCoord2f(0, 1);// Texture: Lower-Left
		 * glVertex2d(x - width / 2, y + height / 2);// Lower-Left
		 * }
		 */
		glBegin(GL_TRIANGLES);
		{
			glTexCoord2f(0.525f, 0);// Texture: Upper-Center
			glVertex2d(x, y - height / 2);// Upper-Center
			glTexCoord2f(0, 1);// Texture: Lower-Left
			glVertex2d(x - width / 2, y + height / 2);// Lower-Left
			glTexCoord2f(1, 1);// Texture: Lower-Right
			glVertex2d(x + width / 2, y + height / 2);// Lower-Right
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		renderLasers();
	}

	@Override
	public void fire() {
		lasers.add(new BasicLaser(this.x, this.y - BasicLaser.laserHeight / 2));
	}

	public void check4input() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			setDX(-speedX);
		else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			setDX(speedX);
		else
			setDX(0);

		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && !Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			setDY(-speedY);
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !Keyboard.isKeyDown(Keyboard.KEY_UP))
			setDY(speedY);
		else
			setDY(0);

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && firedLastFrame <= 0) {
			fire();
			firedLastFrame = 5;
		}
		else
			firedLastFrame--;
	}

	public void renderLasers() {
		for (int i = 0; i < lasers.size(); i++)
			lasers.get(i).draw();
	}

	@Override
	public void update(int delta) {
		if (health <= 0)
			this.destroy();

		keepInBoundries();
		x += delta * dx;
		y += delta * dy;// TODO: fix bug where player can move out a few pixels by also holding side
		keepInBoundries();

		// update lasers
		for (int i = 0; i < lasers.size(); i++) {
			if (!lasers.get(i).alive) {
				lasers.remove(i);
				i--;
				continue;
			}
			lasers.get(i).update(delta);
		}
	}

	@Override
	public boolean intersect(Entity2D entity2D) {
		HitBox.setBounds((int) (x - width / 2), (int) (y + height / 2), (int) (x + width / 2), (int) (y - height / 2));
		return HitBox.intersects(entity2D.getX(), entity2D.getY(), entity2D.getWidth(), entity2D.getHeight());
	}

	@Override
	public Ship_Player getEntity() {
		return new Ship_Player((int) width, (int) height, x, y, speedX, speedY);
	}

	@SuppressWarnings("incomplete-switch")
	private void keepInBoundries() {
		Level lvl = LevelHandler.loadLevel(GameBase.currentLevel);
		switch (lvl.isEntityOutOfBounds(this)) {
			case X:
				setX(width / 2);
				break;
			case _X:
				setX(lvl.getWidth() - width / 2);
				break;
			case Y:
				setY(height / 2);
				break;
			case _Y:
				setY(lvl.getHeight() - height / 2);
				break;
		}
	}

	public void setHealth(float health) {
		this.health = health;
	}
}