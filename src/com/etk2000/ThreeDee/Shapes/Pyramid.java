package com.etk2000.ThreeDee.Shapes;

import static org.lwjgl.opengl.GL11.*;

import com.etk2000.ThreeDee.Point3D;

public class Pyramid extends Shape3D {// same collision

	public Pyramid(float width, float height, float depth, double x, double y, double z) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Render The Pyramid, The (X, Y, Z) Are The Center **/
	@Override
	public void render() {
		glPushMatrix();
		{
			glTranslated(x, y, z);
			/** Rotate The Pyramid, Adjusts According To Parent **/
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
			/** Render The Pyramid **/
			glBegin(GL_TRIANGLES);// Start Drawing The Pyramid
			{
				glColor3f(1.0f, 0.0f, 0.0f);// Red
				glVertex3f(0, height / 2, 0);// Top Of Triangle (Front)
				glColor3f(0.0f, 1.0f, 0.0f);// Green
				glVertex3f(-width / 2, -height / 2, depth / 2);// Left Of Triangle (Front)
				glColor3f(0.0f, 0.0f, 1.0f);// Blue
				glVertex3f(width / 2, -height / 2, depth / 2);// Right Of Triangle (Front)

				glColor3f(1.0f, 0.0f, 0.0f);// Red
				glVertex3f(0, height / 2, 0);// Top Of Triangle (Right)
				glColor3f(0.0f, 0.0f, 1.0f);// Blue
				glVertex3f(width / 2, -height / 2, depth / 2);// Left Of Triangle (Right)
				glColor3f(0.0f, 1.0f, 0.0f);// Green
				glVertex3f(width / 2, -height / 2, -depth / 2);// Right Of Triangle (Right)

				glColor3f(1.0f, 0.0f, 0.0f);// Red
				glVertex3f(0, height / 2, 0);// Top Of Triangle (Back)
				glColor3f(0.0f, 1.0f, 0.0f);// Green
				glVertex3f(width / 2, -height / 2, -depth / 2);// Left Of Triangle (Back)
				glColor3f(0.0f, 0.0f, 1.0f);// Blue
				glVertex3f(-width / 2, -height / 2, -depth / 2);// Right Of Triangle (Back)

				glColor3f(1.0f, 0.0f, 0.0f);// Red
				glVertex3f(0, height / 2, 0);// Top Of Triangle (Left)
				glColor3f(0.0f, 0.0f, 1.0f);// Blue
				glVertex3f(-width / 2, -height / 2, -depth / 2);// Left Of Triangle (Left)
				glColor3f(0.0f, 1.0f, 0.0f);// Green
				glVertex3f(-width / 2, -height / 2, depth / 2);// Right Of Triangle (Left)
			}
			glEnd();// Done Drawing The Pyramid
			/** Render The Base **/
			glBegin(GL_QUADS);
			{
				glColor3f(0, 0, 1);// Blue
				glVertex3f(width / 2, -height / 2, depth / 2);// Top Right - NE
				glColor3f(0, 1, 0);// Green
				glVertex3f(-width / 2, -height / 2, depth / 2);// Top Left - SE
				glColor3f(0, 0, 1);// Blue
				glVertex3f(-width / 2, -height / 2, -depth / 2);// Bottom Left - SW
				glColor3f(0, 1, 0);// Green
				glVertex3f(width / 2, -height / 2, -depth / 2);// Bottom Right - NW
			}
			glEnd();
			glColor3f(1, 1, 1);// reset color
		}
		glPopMatrix();
		renderChildren();
		glRotatef(0, 0, 0, 0);// so the children are also rotated
	}

	@Override
	public boolean contains(Point3D point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersect(Shape3D shape) {
		// TODO Auto-generated method stub
		return false;
	}
}