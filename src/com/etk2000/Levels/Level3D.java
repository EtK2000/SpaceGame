package com.etk2000.Levels;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Random;

import com.etk2000.Entity.Entity2D;
import com.etk2000.Entity.Entity2DAbstract;
import com.etk2000.Entity.BadGuy.BasicBadGuy;
import com.etk2000.Entity.BadGuy.Boss;
import com.etk2000.Entity.BadGuy.ShootingBadGuy;
import com.etk2000.Entity.Laser.BasicLaser;
import com.etk2000.Entity.Laser.Laser;

public class Level3D {
	private int width, height, depth;
	private ArrayList<Entity2D> entities = new ArrayList<Entity2D>();
	private ArrayList<Entity2D> allEntities = new ArrayList<Entity2D>();
	private int enemiesLeft;
	private boolean bossLevel = false;

	public Level3D(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public void addLaser(Entity2D entity) {// add an entity that won't be recreated upon reseting
		entities.add(entity);
	}

	public int numberOfEntities() {
		return entities.size();
	}

	public void registerEntity(Entity2D entity) {
		entities.add(entity);
		allEntities.add(entity.getEntity());
	}

	public void renderBorders() {
		// walls are a width of 5
		glBegin(GL_QUADS);
		{
			glColor3f(0, 0, 0.25f);
			glRectf(5, height, 0, 0);// Left
			glRectf(0, 5, width, 0);// Bottom
			glRectf(width - 5, height, width, 0);// Right
			glRectf(0, height - 5, width, height);// Top
		}
		glEnd();
		glColor3f(1, 1, 1);
	}

	public void renderLevel() {
		renderBorders();
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).draw();
	}

	public void reset() {
		entities.clear();
		for (int i = 0; i < allEntities.size(); i++)
			entities.add(allEntities.get(i).getEntity());
	}

	@SuppressWarnings("incomplete-switch")
	public boolean updateLevel(int delta) {// returns if player beat the level
		enemiesLeft = 0;
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof Entity2DAbstract) {
				Entity2DAbstract e2a = (Entity2DAbstract) entities.get(i);
				if (e2a.alive) {
					// is it out of bounds?
					switch (isEntityOutOfBounds(e2a)) {
						case X:
							e2a.setX(e2a.getWidth() / 2);
							break;
						case _X:
							e2a.setX(width - e2a.getWidth() / 2);
							break;
						case Y:
							e2a.setY(e2a.getHeight() / 2);
							break;
						case _Y:
							e2a.setY(height - e2a.getHeight() / 2);
							break;
					}
					if (e2a instanceof BasicBadGuy) {
						enemiesLeft++;
						if (e2a instanceof ShootingBadGuy) {
							if (e2a instanceof Boss && new Random().nextInt() % (60 - ((Boss) e2a).getFireRate()) == 0)
								((Boss) e2a).fire();
							else if (new Random().nextInt() % 50 == 0)
								((ShootingBadGuy) e2a).fire();
						}
						int x = new Random().nextInt() % 20;
						int y = new Random().nextInt() % 20;
						e2a.setDX(x == 19 ? -0.5 : (x == 10 ? 0.5 : 0));
						e2a.setDY(y == 19 ? -0.5 : (y == 10 ? 0.5 : 0));
						e2a.update(delta);
					}
				}
				else {
					entities.remove(i);
					i--;// remove THIS entity
				}
			}
			else if (entities.get(i) instanceof BasicLaser) {
				BasicLaser laser = (BasicLaser) entities.get(i);
				laser.update(delta);
				if (laser.getX() < laser.getWidth() / 2 || laser.getX() + laser.getWidth() / 2 > this.width
						|| laser.getY() < laser.getHeight() / 2 || laser.getY() + laser.getHeight() / 2 > this.height)
					laser.destroy();
				if (!laser.alive) {
					entities.remove(i);
					i--;
				}
			}
		}
		if (enemiesLeft == 0)
			return true;
		return false;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Entity2D getEntity(int entityNumber) {
		if (entityNumber < 0 || entityNumber >= entities.size())
			return null;
		return entities.get(entityNumber);
	}

	public Border isEntityOutOfBounds(Entity2DAbstract e2a) {
		if (e2a.getX() - e2a.getWidth() / 2 < 0)
			return Border.X;
		else if (e2a.getX() + e2a.getWidth() / 2 > width)
			return Border._X;
		if (e2a.getY() - e2a.getHeight() / 2 < 0)
			return Border.Y;
		else if (e2a.getY() + e2a.getHeight() / 2 > height)
			return Border._Y;
		return Border.noBorder;
	}

	public Border isEntityOutOfBounds(Laser laser) {
		if (laser.getX() - laser.getWidth() / 2 < 0)
			return Border.X;
		else if (laser.getX() + laser.getWidth() / 2 > width)
			return Border._X;
		if (laser.getY() - laser.getHeight() / 2 < 0)
			return Border.Y;
		else if (laser.getY() + laser.getHeight() / 2 > height)
			return Border._Y;
		return Border.noBorder;
	}

	public boolean isBossLevel() {
		return bossLevel;
	}

	public void setAsBossLevel() {
		this.bossLevel = true;
	}
}