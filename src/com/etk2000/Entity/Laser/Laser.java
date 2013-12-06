package com.etk2000.Entity.Laser;

import java.awt.Rectangle;

import com.etk2000.Entity.Entity2D;
import com.etk2000.Entity.Entity2DAbstract;
import com.etk2000.Entity.BadGuy.BasicBadGuy;
import com.etk2000.Levels.Border;
import com.etk2000.Levels.Level;
import com.etk2000.Levels.LevelHandler;
import com.etk2000.SpaceGame.GameBase;

public abstract class Laser implements Entity2D {
	public boolean alive = true;// for handling
	public boolean isMoving = true;// public for Laser-Freezers
	protected boolean fromBadie = false;
	protected final float width, height, speedX, speedY;
	protected float power;
	protected double dx, dy, x = 0, y = 0;
	protected Rectangle HitBox = new Rectangle();
	protected String textureName;

	public Laser(float width, float height, float speedX, float speedY) {
		this.width = width;
		this.height = height;
		this.speedX = speedX;
		this.speedY = speedY;
		this.dx = speedX;
		this.dy = speedY;
	}

	@Override
	public void destroy() {
		this.alive = false;
	}
	
	@Override
	public void kill() {
		this.destroy();
	}

	// draw is called by child

	@Override
	public void fire() {
		// A normal laser can't fire
	}

	@Override
	public void update(int delta) {
		if (LevelHandler.getCurrentLevel().isEntityOutOfBounds(this) != Border.noBorder)
			this.destroy();
		x += delta * dx;
		y -= delta * dy;// go up

		// check for collision:
		// if player -> stop at everything
		// if badie -> stop at player or static entity
		Level lvl = LevelHandler.getCurrentLevel();
		for (int i = 0; i < lvl.numberOfEntities(); i++) {
			Entity2D e2d = lvl.getEntity(i);
			if (this.intersect(e2d)) {
				if (e2d instanceof Entity2DAbstract) {
					if (e2d instanceof BasicBadGuy && !fromBadie) {// badies can't hit each other
						this.destroy();
						((BasicBadGuy) e2d).hurt(power);
					}
					else if(!(e2d instanceof BasicBadGuy)){// but they can hit other stuff
						this.destroy();
						((Entity2DAbstract) e2d).hurt(power);
					}
				}
				else if (!(e2d instanceof BasicLaser))//and lasers can't hit lasers...
					this.destroy();
			}
		}
		if (fromBadie && this.intersect(GameBase.player)) {
			this.destroy();
			GameBase.player.hurt(power);
		}
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public double getDX() {
		return dx;
	}

	@Override
	public void setDX(double dx) {
		this.dx = dx;
	}

	@Override
	public double getDY() {
		return dy;
	}

	@Override
	public void setDY(double dy) {
		this.dy = dy;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		this.y = y;

	}

	@Override
	public void setLocation(double x, double y) {
		setX(x);
		setY(y);
	}

	@Override
	public boolean intersect(Entity2D entity2D) {
		HitBox.setBounds((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
		return HitBox.intersects(entity2D.getX() - entity2D.getWidth() / 2, entity2D.getY() - entity2D.getHeight() / 2,
				entity2D.getWidth(), entity2D.getHeight());
	}
}