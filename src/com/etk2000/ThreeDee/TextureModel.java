/*
 * Copyright (c) 2013, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package com.etk2000.ThreeDee;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.ThreeDee.Shapes.Pyramid;
import com.etk2000.ThreeDee.Shapes.Sphere;
import com.etk2000.util.*;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

/** Shows how to add textures to an obj/mtl file. */
public class TextureModel {

	private static final String MODEL_LOCATION = "res/models/21693_Brilliant_Pebble.obj";// "res/models/bunny.obj";
	private static final String VERTEX_SHADER_LOCATION = "res/shaders/pixel_phong_lighting.vs";
	private static final String FRAGMENT_SHADER_LOCATION = "res/shaders/pixel_phong_lighting.fs";
	private static EulerCamera cam;
	private static int shaderProgram;
	private static int modelDisplayList;
	private static Model model;

	private static Entity3D e3d;
	private static Pyramid p;
	private static Sphere s;

	public static void main(String[] args) {
		setUpDisplay();
		setUpModel();
		setUpCamera();
		// setUpShaders();
		setUpLighting();

		GamePackHandler.listAllTexturePacks();
		GamePackHandler.loadTexturePack(new File(GameBase.dataFolder + "/texturepacks/default.zip"));

		// X+ is North, Z+ is East
		e3d = new Entity3DShip(1, 1, 1, 0, 0, 0);
		p = new Pyramid(2, 2, 2, 5, 5, 5);
		s = new Sphere(2, 16, 16, 0, 0, 0);
		//s = new org.lwjgl.util.glu.Sphere();

		float i = 0, j =0, k = 0;
		while (!Display.isCloseRequested()) {
			Display.setTitle("X: " + (int) cam.x() + ", Y: " + (int) cam.y() + ", Z: " + (int) cam.z() + "; i: " + i);
			render();
			e3d.draw();
			//glTranslatef(-1.5f, 0.0f, -6.0f);// Move Left And Into The Screen
			
			//glRotatef(i, 0, 1, 0);// Rotate The Pyramid On It's Y Axis
			p.render();
			
			if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
				if((i += 5) > 359)
					i = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_Y)) {
				if((j += 5) > 359)
					j = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
				if((k += 5) > 359)
					k = 0;
			}
			p.rotate(i, j, k);
			
			s.render();
			//s.draw(5, 16, 16);
			
			
			checkInput();
			Display.update();
			Display.sync(60);
		}
		cleanUp();
		System.exit(0);
	}

	private static void checkInput() {
		if (Mouse.isGrabbed()) {
			cam.processMouse(1, 80, -80);
		}
		cam.processKeyboard(16, 5, 5, 5);
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		}
		else if (Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}
	}

	private static void cleanUp() {
		glDeleteProgram(shaderProgram);
		glDeleteLists(modelDisplayList, 1);
		Display.destroy();
	}

	private static void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		cam.applyTranslations();
		glUseProgram(shaderProgram);
		glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(cam.x(), cam.y(), cam.z(), 1));
		// glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		// glVertexPointer(3, GL_FLOAT, 0, 0L);
		// glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
		// glNormalPointer(GL_FLOAT, 0, 0L);
		// glEnableClientState(GL_VERTEX_ARRAY);
		// glEnableClientState(GL_NORMAL_ARRAY);
		glColor3f(0.4f, 0.27f, 0.17f);
		glMaterialf(GL_FRONT, GL_SHININESS, 10f);
		glPushMatrix();
		glScalef(.3f, .3f, .3f);
		glTranslatef(0, 0, -2);
		glEnable(GL_LIGHTING);
		glCallList(modelDisplayList);
		glDisable(GL_LIGHTING);
		glPopMatrix();
		// glDrawArrays(GL_TRIANGLES, 0, model.getFaces().size() * 3);
		// glDisableClientState(GL_VERTEX_ARRAY);
		// glDisableClientState(GL_NORMAL_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glUseProgram(0);
	}

	private static void setUpLighting() {
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);//makes smaller models act weird
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float[] { 0.05f, 0.05f, 0.05f, 1f }));
		glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(new float[] { 0, 0, 0, 1 }));
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	private static void setUpModel() {
		try {
			model = OBJLoader.loadTexturedModel(new File(MODEL_LOCATION));
			model.enableStates();
			modelDisplayList = OBJLoader.createTexturedDisplayList(model);
		}
		catch (IOException e) {
			e.printStackTrace();
			cleanUp();
		}
	}

	private static void setUpShaders() {
		shaderProgram = ShaderLoader.loadShaderPair(VERTEX_SHADER_LOCATION, FRAGMENT_SHADER_LOCATION);
	}

	private static void setUpCamera() {
		cam = new EulerCamera((float) Display.getWidth() / (float) Display.getHeight(), -2.19f, 1.36f, 11.45f);
		cam.setFieldOfView(70);
		cam.applyPerspectiveMatrix();
	}

	private static void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setVSyncEnabled(true);
			Display.setTitle("VBO Model Demo");
			Display.create();
		}
		catch (LWJGLException e) {
			System.err.println("The display wasn't initialized correctly. :(");
			Display.destroy();
			System.exit(1);
		}
	}
}
