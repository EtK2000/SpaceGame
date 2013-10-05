package com.etk2000.ThreeDee;

import com.etk2000.ThreeDee.Shapes.Cube;
import com.etk2000.ThreeDee.Shapes.Pyramid;

public class Entity3DShip extends Entity3D {

	public Entity3DShip(float width, float height, float depth, double x, double y, double z) {
		super(width, height, depth, x, y, z);
		this.shapes.add(new Pyramid(width, height, depth, x, y + width, z));
		this.shapes.add(new Cube(width, height, depth, x, y, z));
		this.shapes.add(new Cube(width / 2, height / 2, depth / 2, x, y - height / 1.35, z));
	}
}