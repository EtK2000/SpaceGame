package com.etk2000.Entity.BadGuy;

import com.etk2000.Entity.Laser.BasicLaser;
import com.etk2000.GameHandling.GamePackHandler;
import com.etk2000.Levels.LevelHandler;

public class ShootingBadGuy extends BasicBadGuy {
	
	public ShootingBadGuy(float width, float height, double x, double y) {
		super(width, height, x, y);
		texture = GamePackHandler.BGShooter;
		this.health = 2;
		this.fullHealth = 2;
		setLocation(x, y);
	}

	@Override
	public void fire() {
		LevelHandler.getCurrentLevel().addLaser(new BasicLaser(this.x, this.y - BasicLaser.laserHeight / 2, -0.25f));
	}
	
	@Override
	public ShootingBadGuy getEntity() {
		return new ShootingBadGuy(width, height, x, y);
	}
}