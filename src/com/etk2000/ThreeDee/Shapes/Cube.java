package com.etk2000.ThreeDee.Shapes;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import com.etk2000.ThreeDee.Point3D;

public class Cube extends Shape3D {
	protected final ArrayList<Point3D> corners = new ArrayList<>();

	public Cube(float width, float height, float depth, double x, double y, double z) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.x = x;
		this.y = y;
		this.z = z;
		corners.add(new Point3D(x - width / 2, y - height / 2, z - depth / 2));
		corners.add(new Point3D(x - width / 2, y - height / 2, z + depth / 2));
		corners.add(new Point3D(x - width / 2, y + height / 2, z - depth / 2));
		corners.add(new Point3D(x - width / 2, y + height / 2, z + depth / 2));
		corners.add(new Point3D(x + width / 2, y - height / 2, z - depth / 2));
		corners.add(new Point3D(x + width / 2, y - height / 2, z + depth / 2));
		corners.add(new Point3D(x + width / 2, y + height / 2, z - depth / 2));
		corners.add(new Point3D(x + width / 2, y + height / 2, z + depth / 2));
	}

	/** Render The Cube, The (X, Y, Z) Are The Center **/
	@Override
	public void render() {
		glPushMatrix();
		{
			glTranslated(x, y, z);
			/** Rotate The Object, Adjusts According To Parent **/
			if (parent != null) {
				rotationX += parent.rotationX;
				rotationY += parent.rotationY;
				rotationY += parent.rotationZ;
			}
			glRotatef(rotationX, 1, 0, 0);
			glRotatef(rotationY, 0, 1, 0);
			glRotatef(rotationZ, 0, 0, 1);
			if (parent != null) {
				rotationX -= parent.rotationX;
				rotationY -= parent.rotationY;
				rotationY -= parent.rotationZ;
			}
			glBegin(GL_QUADS);
			{
				glColor3f(0, 0, 1);
				glVertex3f(width / 2, height / 2, -depth / 2);// Top Right (Top) - NW
				glVertex3f(-width / 2, height / 2, -depth / 2);// Top Left (Top) - SW
				glVertex3f(-width / 2, height / 2, depth / 2);// Bottom Left (Top) - SE
				glVertex3f(width / 2, height / 2, depth / 2);// Bottom Right (Top) - NE

				glColor3f(0, 1, 1);
				glVertex3f(width / 2, -height / 2, depth / 2);// Top Right (Bottom) - NE
				glVertex3f(-width / 2, -height / 2, depth / 2);// Top Left (Bottom) - SE
				glVertex3f(-width / 2, -height / 2, -depth / 2);// Bottom Left (Bottom) - SW
				glVertex3f(width / 2, -height / 2, -depth / 2);// Bottom Right (Bottom) - NW

				glColor3f(0, 1, 0);
				glVertex3f(width / 2, height / 2, depth / 2);// Top Right (Front)
				glVertex3f(-width / 2, height / 2, depth / 2);// Top Left (Front)
				glVertex3f(-width / 2, -height / 2, depth / 2);// Bottom Left (Front)
				glVertex3f(width / 2, -height / 2, depth / 2);// Bottom Right (Front)

				glColor3f(1, 1, 0);
				glVertex3f(width / 2, -height / 2, -depth / 2);// Bottom Left (Back)
				glVertex3f(-width / 2, -height / 2, -depth / 2);// Bottom Right (Back)
				glVertex3f(-width / 2, height / 2, -depth / 2);// Top Right (Back)
				glVertex3f(width / 2, height / 2, -depth / 2);// Top Left (Back)

				glColor3f(1, 1, 1);
				glVertex3f(-width / 2, height / 2, depth / 2);// Top Right (Left)
				glVertex3f(-width / 2, height / 2, -depth / 2);// Top Left (Left)
				glVertex3f(-width / 2, -height / 2, -depth / 2);// Bottom Left (Left)
				glVertex3f(-width / 2, -height / 2, depth / 2);// Bottom Right (Left)

				glColor3f(1, 0, 1);
				glVertex3f(width / 2, height / 2, -depth / 2);// Top Right (Right)
				glVertex3f(width / 2, height / 2, depth / 2);// Top Left (Right)
				glVertex3f(width / 2, -height / 2, depth / 2);// Bottom Left (Right)
				glVertex3f(width / 2, -height / 2, -depth / 2);// Bottom Right (Right)
			}
			glEnd();
			glRotatef(0, 0, 0, 0);
		}
		glPopMatrix();
		renderChildren();
	}

	@Override
	public boolean contains(Point3D point) {
		if ((point.getX() < this.x + this.width / 2 || point.getX() > this.x - this.width / 2)
				&& (point.getY() < this.y + this.height / 2 || point.getY() > this.y - this.height / 2)
				&& (point.getZ() < this.z + this.depth / 2 || point.getZ() > this.z - this.depth / 2))
			return true;
		return false;
	}

	@Override
	public boolean intersect(Shape3D shape) {
		if (shape instanceof Cube) {
			for (int i = 0; i < this.corners.size(); i++) {
				if (shape.contains(this.corners.get(i)))
					return true;
			}
			for (int i = 0; i < ((Cube) shape).corners.size(); i++) {
				if (this.contains(((Cube) shape).corners.get(i)))
					return true;
			}
		}
		return false;
	}
}