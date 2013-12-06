package com.etk2000.ThreeDee.Shapes;

import static org.lwjgl.opengl.GL11.*;

import com.etk2000.ThreeDee.Point3D;

public class Sphere extends Shape3D {
	private final org.lwjgl.util.glu.Sphere s = new org.lwjgl.util.glu.Sphere();
	protected float radius;
	protected int quads, strips;

	public Sphere(float radius, int quads, int strips, double x, double y, double z) {
		this.radius = radius;
		this.quads = quads;
		this.strips = strips;
		this.width = radius * 2;// JIC
		this.height = radius * 2;// JIC
		this.depth = radius * 2;// JIC
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void render() {
		glPushMatrix();
		{
			glTranslated(x, y, z);
			//glColor3f(1, 0, 0);
			s.draw(radius, quads, strips);
		}
		glPopMatrix();
	}

	@Override
	public boolean contains(Point3D point) {
		Point3D temp = new Point3D(Math.abs(point.getX() - this.x), Math.abs(point.getY() - this.y), Math.abs(point
				.getZ() - this.z));
		if (Math.sqrt(Math.pow(temp.getX(), 2) + Math.pow(temp.getY(), 2) + Math.pow(temp.getZ(), 2)) <= this.radius)
			return true;
		return false;
	}

	@Override
	public boolean intersect(Shape3D shape) {
		if (shape instanceof Sphere) {
			// draw a line from the centers, if =< radiuses, colliding
			/** get the vector from the two sphere's centers **/
			double newX = Math.abs(shape.x - this.x);
			double newY = Math.abs(shape.y - this.y);
			double newZ = Math.abs(shape.z - this.z);
			/** calculate the length of the vector **/
			double length = Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2) + Math.pow(newZ, 2));
			if (length <= ((Sphere) shape).radius + this.radius)
				return true;
		}
		return false;
	}
}