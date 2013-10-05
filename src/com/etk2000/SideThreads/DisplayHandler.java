package com.etk2000.SideThreads;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.lwjgl.opengl.Display;

import com.etk2000.Levels.Level;
import com.etk2000.Levels.LevelHandler;
import com.etk2000.SpaceGame.GameBase;

public class DisplayHandler extends SideThread {
	// size of the screen
	public static final Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getMaximumWindowBounds();

	public static int width = 800, height = 600;// for ease

	public void Init() {
		run = new Runnable() {
			@Override
			public void run() {
				while (!stopSignal) {
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}

					height = Display.getHeight();
					width = Display.getWidth();

					if (GameBase.isMidLevel) {
						Level lvl = LevelHandler.getCurrentLevel();
						if (height < lvl.getHeight())
							height = lvl.getHeight() + 50;
						if (width < lvl.getWidth())
							width = lvl.getWidth() + 50;
					}
				}
				stopSignal = false;
			}
		};
		super.Init(this);
	}

	public static void clearDisplay() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}