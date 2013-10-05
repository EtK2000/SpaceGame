package com.etk2000.ThreeDee;

import java.util.ArrayList;

import com.etk2000.Entity.Entity2D;
import com.etk2000.Entity.Entity2DAbstract;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.ThreeDee.Shapes.Shape3D;

public class Entity3D extends Entity2DAbstract {
	protected float depth;
	protected double z;

	/** 3D Objects That Are Used For Rendering **/
	protected ArrayList<Shape3D> shapes = new ArrayList<>();

	public Entity3D(float width, float height, float depth, double x, double y, double z) {
		this.health = 1;
		this.fullHealth = 1;
		this.width = width;
		this.height = height;
		this.depth = depth;
		setLocation(x, y, z);
	}

	@Override
	public void destroy() {
		this.alive = false;
	}

	@Override
	public void kill() {
		// TODO spawn 3D explosion
	}

	@Override
	public void draw() {
		/** Render All Shapes **/
		for (int i = 0; i < shapes.size(); i++)
			shapes.get(i).render();
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
	}

	@Override
	public Entity3D getEntity() {
		return new Entity3D(width, height, depth, x, y, z);
	}

	public float getDepth() {
		return depth;
	}

	public double getZ() {
		return z;
	}

	@Override
	@Deprecated
	public boolean intersect(Entity2D entity2d) {
		GameBase.logger.severe("You Cannot Use \"intersect(entity2d)\" For A 3D Entity!");
		return false;
	}

	// TODO: make this function run better and faster
	public boolean intersect(Entity3D entity3d) {
		for(int i = 0; i < shapes.size(); i++) {
			for(int j = 0; j < entity3d.shapes.size(); j++) {
				if(shapes.get(i).intersect(entity3d.shapes.get(j)))
					return true;
			}
		}
		return false;
	}

	@Override
	@Deprecated
	public void setLocation(double x, double y) {
		GameBase.logger.severe("You Cannot Use \"setLocation(x, y)\" For A 3D Entity!");
	}

	public void setLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}