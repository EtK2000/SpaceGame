package com.etk2000.Entity;

import java.awt.Rectangle;

public abstract class Entity2DAbstract implements Entity2D {
	public float width, height, speedX, speedY;// public for powerups
	protected double dx, dy, x, y;
	protected String textureName;
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
	public final float getHeight() {
		return height;
	}

	@Override
	public final float getWidth() {
		return width;
	}

	/** delta **/
	@Override
	public final double getDX() {
		return dx;
	}

	@Override
	public final void setDX(double dx) {
		this.dx = dx;
	}

	@Override
	public final double getDY() {
		return dy;
	}

	@Override
	public final void setDY(double dy) {
		this.dy = dy;
	}

	/** location **/
	@Override
	public final double getX() {
		return x;
	}

	@Override
	public final void setX(double x) {
		this.x = x;
	}

	@Override
	public final double getY() {
		return y;
	}

	@Override
	public final void setY(double y) {
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

	/** Health **/

	public final void fullHeal() {
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

	public final float getHealth() {
		return health;
	}
}