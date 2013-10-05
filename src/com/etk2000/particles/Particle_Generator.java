package com.etk2000.particles;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Particle_Generator {

	private int particleCount = 0;
	private int generationSpeed = 0;
	private int generationNow = 0;
	private Particle[] particles;

	private float x;
	private float y;
	private int life;

	private float r;
	private float g;
	private float b;
	private float a;

	private boolean emitter;

	private float speed;
	private float angle;
	private float range;

	private static ByteBuffer bufParticle = null;
	private static PNGDecoder decParticle = null;

	private class Particle {

		private Particle_Generator parent = null;
		private float x;
		private float y;
		private float speed;

		private float r;
		private float g;
		private float b;
		private float a;

		private float personalAngle;
		private float lifespan;
		private float angleDiff;

		private boolean dead = false;

		public Particle(Particle_Generator generator) {
			parent = generator;
			r = (float) (parent.r + (Math.random() / 10));
			g = (float) (parent.g + (Math.random() / 10));
			b = (float) (parent.b + (Math.random() / 10));
			a = (float) (parent.a + (Math.random() / 10));
			x = parent.x;
			y = parent.y;
			lifespan = parent.life;
			speed = (float) (parent.speed + (Math.random() * parent.speed / 10));
			personalAngle = parent.angle;

			float rangecalc = (float) Math.random();
			boolean isAboveZero = false;
			if (rangecalc > .5) {
				isAboveZero = true;
			}
			rangecalc = (float) Math.random();
			if (isAboveZero == true) {
				angleDiff = parent.range * rangecalc;
			}
			else {
				angleDiff = -range * rangecalc;
			}
			personalAngle = personalAngle + angleDiff;
		}

		private void update() {
			if (Math.random() > .5) {
				x += 1 * Math.random();
			}
			else {
				x -= 1 * Math.random();
			}
			if (Math.random() > .5) {
				y += 1 * Math.random();
			}
			else {
				y -= 1 * Math.random();
			}

			x -= Math.sin(personalAngle) * speed;
			y += Math.cos(personalAngle) * speed;

			draw();
			weaken();
		}

		private void draw() {
			float strenght = lifespan / parent.life;// helps with fading
			glColor4f(r * strenght, g * strenght, b * strenght, a * strenght);
			glTexCoord2d(1, 0);
			glVertex2d(-10 + x + Display.getX() / 2, -10 + y + Display.getY() / 2);
			glTexCoord2d(1, 1);
			glVertex2d(-10 + x + Display.getX() / 2, 10 + y + Display.getY() / 2);
			glTexCoord2d(0, 1);
			glVertex2d(10 + x + Display.getX() / 2, 10 + y + Display.getY() / 2);
			glTexCoord2d(0, 0);
			glVertex2d(10 + x + Display.getX() / 2, -10 + y + Display.getY() / 2);
		}

		private void weaken() {
			lifespan--;
			if (lifespan < 0) {
				dead = true;
			}
		}

	}

	public Particle_Generator(int particleCount, int generationSpeed, int life, float r, float g, float b, float a,
			boolean emit, float x, float y, float speed, float angle, int range) {
		if (particleCount <= 0) {
			return;
		}
		this.particleCount = particleCount;
		this.generationSpeed = generationSpeed;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.x = x - 275;// tweak
		this.y = y - 150;// tweak
		this.life = life;
		this.speed = speed;
		this.angle = angle;
		this.range = range;
		emitter = emit;
		generate();
		if (bufParticle == null || decParticle == null)
			particleTexture();
	}

	private void generate() {
		particles = new Particle[particleCount];
		for (int i = 0; i < particleCount; i++) {
			particles[i] = new Particle(this);
		}
		for (int i = 0; i < particleCount; i++) {
			particles[i].lifespan = 1;
		}
	}

	public void update(boolean produce) {
		for (int i = 0; i < particleCount; i++) {
			if (particles[i] != null) {
				if (particles[i].dead == true)
					particles[i] = null;
				else
					particles[i].update();
			}
			else {
				if (produce == true) {
					particles[i] = null;
					if (emitter) {
						if (generationSpeed > generationNow) {
							particles[i] = new Particle(this);
							generationNow++;
						}
					}
				}
			}
		}
		generationNow = 0;
	}

	public void draw() {
		// glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decParticle.getWidth(), decParticle.getHeight(),
		// 0, GL_RGBA,
		// GL_UNSIGNED_BYTE, bufParticle);
		// glBindTexture(GL_ADD, GL_LOAD);
		// glBindTexture(GL_TEXTURE_2D, 1);
		// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		for (int i = 0; i < particleCount; i++) {
			if (particles[i] != null) {
				glBegin(GL_QUADS);
				{
					particles[i].draw();
				}
				glEnd();
			}
		}
		glColor3f(1, 1, 1);
	}

	public void move(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void killme() {
		for (int i = 0; i < particleCount; i++) {
			if (particles[i] != null) {
				particles[i] = null;
			}
		}
	}

	public void setspeed(float Speed) {
		this.speed = Speed;
	}

	public void setAngle(float Angle) {
		this.angle = Angle;
	}

	public void setRange(float Range) {
		this.range = (float) (Range * Math.PI / 180);
	}

	public void particleTexture() {
		InputStream in = null;
		try {
			in = new FileInputStream("res/images/particle_floor.png");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			decParticle = new PNGDecoder(in);
			bufParticle = ByteBuffer.allocateDirect(4 * decParticle.getWidth() * decParticle.getHeight());
			decParticle.decode(bufParticle, decParticle.getWidth() * 4, PNGDecoder.Format.RGBA);
			bufParticle.flip();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}