package com.etk2000.Entity.BadGuy;

import static org.lwjgl.opengl.GL11.*;

import com.etk2000.Entity.Entity2DAbstract;
import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.SideThreads.DisplayHandler;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.particles.Particle_Generator;

public class BasicBadGuy extends Entity2DAbstract {
	protected Particle_Generator generator;

	protected int dyingFrames = 30;

	public BasicBadGuy(float width, float height, double x, double y) {
		texture = GamePackHandler.BGBasic;
		this.health = 1;
		this.fullHealth = 1;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = DisplayHandler.height - y;
		setLocation(x, y);
	}

	@Override
	public void destroy() {
		this.alive = false;
		GameBase.currentScore += fullHealth;
	}

	@Override
	public void kill() {
		this.dying = true;
		generator = new Particle_Generator(10, 20, 30, 0.25f, 0.25f, 0.25f, 0.5f, true, (float) x, (float) y, 1, 135,
				(int) ((width > height ? width : height) / 25 * 10));
	}

	@Override
	public void update(int delta) {
		if (!dying)
			super.update(delta);
		else
			generator.update(true);
	}

	@Override
	public void draw() {
		if (dying) {// shrink and explode
			if (--dyingFrames == 0)
				this.destroy();
			generator.draw();
			width = dyingFrames / width;
			height = dyingFrames / height;
		}
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
	public void fire() {
		// TODO Auto-generated method stub
	}

	@Override
	public BasicBadGuy getEntity() {
		return new BasicBadGuy(width, height, x, y);
	}
}