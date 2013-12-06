package com.etk2000.Entity.BadGuy;

import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.gui.GUIText;

import static org.lwjgl.opengl.GL11.glColor3f;

public class Boss extends ShootingBadGuy {
	private float fireRate;

	public Boss(float width, float height, double x, double y, float fullHealth, float fireRate, String textureName) {
		super(width, height, x, y);
		this.textureName = textureName;
		this.health = fullHealth;// start with full health
		this.fullHealth = fullHealth;
		this.fireRate = fireRate;
		setLocation(x, y);
	}

	@Override
	public void draw() {
		if (health > 0)
			renderHealthBar();
		super.draw();
	}

	public void renderHealthBar() {
		glColor3f(health / fullHealth, (fullHealth - health) / fullHealth, 0);
		new GUIText("" + (int) health, (float) x, (float) (DisplayHandler.height - y - height / 1.25)).DrawString();
		glColor3f(1, 1, 1);
	}

	public float getFireRate() {
		return fireRate;
	}

	@Override
	public Boss getEntity() {
		return new Boss(width, height, x, y, fullHealth, fireRate, textureName);
	}
}