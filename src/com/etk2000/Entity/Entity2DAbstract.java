package com.etk2000.Entity;

import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

public abstract class Entity2DAbstract implements Entity2D {
	public float width, height, speedX, speedY;// public for powerups
	protected double dx, dy, x, y;
	protected Texture texture;
	protected Rectangle HitBox = new Rectangle();
	protected float health, fullHealth;
	public boolean alive = true, dying = false;// for handling

	// destroy should be called by every Entity
	// draw should be called by every Entity

	@Override
	public void update(int delta) {
		if (dying)
			return;
		if (health <= 0)
			this.kill();
		else {
			x += delta * dx;
			y += delta * dy;
		}
	}

	/** size **/
	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}

	/** delta **/
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

	/** location **/
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
		HitBox.setBounds((int) (x - width / 2), (int) (y + height / 2), (int) width, (int) height);
		return HitBox.intersects(entity2D.getX() - entity2D.getWidth() / 2, entity2D.getY() + entity2D.getHeight() / 2,
				entity2D.getWidth(), entity2D.getHeight());
	}

	public void fullHeal() {
		health = fullHealth;
	}

	public void heal(float health) {
		this.health += health;
		if (health > 0 && !alive)
			alive = true;
	}

	public void hurt(float damage) {
		this.health -= damage;
		if (health <= 0)
			this.kill();
	}

	public float getHealth() {
		return health;
	}
}