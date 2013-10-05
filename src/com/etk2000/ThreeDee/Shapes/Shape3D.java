package com.etk2000.ThreeDee.Shapes;

import java.util.ArrayList;

import com.etk2000.ThreeDee.Point3D;

public abstract class Shape3D {
	protected float width, height, depth;
	protected double x, y, z;
	protected float rotationX = 0, rotationY = 0, rotationZ = 0;
	protected ArrayList<Shape3D> children = new ArrayList<>();
	protected Shape3D parent = null;

	public abstract void render();

	/** You Set The Child's WorldWide Position, Adaption Is Done Automatically **/
	protected final void addChild(Shape3D shape) {// You Can't Override :P
		shape.x -= this.x;
		shape.y -= this.y;
		shape.z -= this.z;
		children.add(shape);
		shape.parent = this;// set this as the shape's parent
	}

	protected final void renderChildren() {// You Can't Override :P
		for (int i = 0; i < children.size(); i++)
			children.get(i).render();
	}

	public void rotate(float rotationX, float rotationY, float rotationZ) {
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
	}

	public abstract boolean contains(Point3D point);
	
	public abstract boolean intersect(Shape3D shape);
}