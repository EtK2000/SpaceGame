package com.etk2000.ThreeDee;

import static org.lwjgl.opengl.GL11.*;

public class Point3D {
	private double x, y, z;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}

	public void render() {
		glBegin(GL_POINTS);
		{
			glVertex3d(x, y, z);
		}
		glEnd();
	}
}