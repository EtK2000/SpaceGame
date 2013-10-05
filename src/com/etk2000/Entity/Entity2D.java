package com.etk2000.Entity;

public interface Entity2D {
	public void destroy();
	public void kill();
	public void draw();
	public void fire();
	public void update(int delta);
	
	//size
	public float getHeight();
	public float getWidth();
	
	// delta
	public double getDX();
	public void setDX(double dx);
	public double getDY();
	public void setDY(double dy);
	
	// location
	public double getX();
	public void setX(double x);
	public double getY();
	public void setY(double y);
	public void setLocation(double x, double y);
	
	public boolean intersect(Entity2D entity2D);
	//public String getName();
	public Entity2D getEntity();
	// life management in in the abstract
}