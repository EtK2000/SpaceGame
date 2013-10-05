package com.etk2000.ThreeDee;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.etk2000.SideThreads.DisplayHandler;

public class Test {
	private static Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main(String[] args) {
		int z = 0;
		/** Create The Display **/
		try {
			Display.setDisplayMode(new DisplayMode(screenDimensions.width, screenDimensions.height));
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		/** Initialize OpenGL **/
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(30f, DisplayHandler.width / DisplayHandler.height, 0.001f, 125);
		glMatrixMode(GL_MODELVIEW);

		/** Initialize The Dots **/
		Point[] points = new Point[100000];
		Random random = new Random();
		for (int i = 0; i < points.length; i++)
			points[i] = new Point((random.nextFloat() - 0.5f) * 100, (random.nextFloat() - 0.5f) * 100,
					random.nextInt(10000) - 10000, random);

		int frame = 0, switchNum = 120, curSpeed = 0;// switch every 2 seconds
		float[] speeds = { 2, 2, 2, 3, 4, 5, 5, 4, 3 };
		float speed = speeds[curSpeed];
		
		while (!Display.isCloseRequested()) {
			DisplayHandler.clearDisplay();
			Display.setLocation(-3, -9);

			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				setCursor(0xff870000);// red
			else
				setCursor(0xff008700);// green

			if (++frame % switchNum == 0)
				speed = speeds[++curSpeed % speeds.length];
			glTranslatef(0, 0, speed);
			z += speed;
			glBegin(GL_POINTS);
			{
				Display.setTitle(Mouse.getX() + ", " + Mouse.getY() + "; " + z + ", speed: " + speed);
				for (Point p : points) {
					if (p.z > 0 - z)// move points to places where they can be seen
						p.z = p.z - 10000;
					p.render();
				}
			}
			glEnd();

			Display.sync(60);
			Display.update();
		}

		Display.destroy();
		System.exit(0);
	}

	private static void setCursor(int color) {
		int cursorImageCount = 1, cursorWidth = 128, cursorHeight = 128, thikness = 8;
		IntBuffer cursorImages, cursorDelays;
		cursorImages = ByteBuffer.allocateDirect(cursorWidth * cursorHeight * cursorImageCount * 4)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		cursorDelays = ByteBuffer.allocateDirect(cursorImageCount * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		cursorDelays.put(100);

		for (int i = 0; i < cursorImageCount; i++) {
			for (int j = 0; j < cursorWidth; j++) {
				for (int l = 0; l < cursorHeight; l++) {
					if (j < thikness || j > cursorWidth - thikness || l < thikness || l > cursorHeight - thikness)
						cursorImages.put(color);
					else
						cursorImages.put(0x00000000);
				}
			}
		}
		cursorImages.flip();
		cursorDelays.flip();
		try {
			Mouse.setNativeCursor(new Cursor(cursorWidth, cursorHeight, cursorWidth / 2, cursorHeight / 2,
					cursorImageCount, cursorImages, cursorDelays));
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private static class Point {
		private float x, y, z;
		private float r, g, b;

		public Point(float x, float y, float z, Random random) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.r = this.g = this.b = random.nextFloat() / random.nextFloat();
			if (random.nextFloat() / random.nextFloat() < 0.125) {// a rare case
				this.r = random.nextFloat() / random.nextFloat();
				this.g = random.nextFloat() / random.nextFloat();
				this.b = random.nextFloat() / random.nextFloat();
			}
		}

		public void render() {
			glColor3f(r, g, b);
			glVertex3f(x, y, z);
		}
	}
}