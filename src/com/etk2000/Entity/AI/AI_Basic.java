package com.etk2000.Entity.AI;

import com.etk2000.Entity.Entity2D;
import com.etk2000.Entity.Entity2DAbstract;
import com.etk2000.Levels.LevelHandler;
import com.etk2000.SpaceGame.GameBase;

public class AI_Basic extends AI {
	private float speedX, speedY;

	public AI_Basic(Entity2DAbstract me, float dontGetCloserThan, float speedX, float speedY) {
		super(me, dontGetCloserThan);
		this.speedX = speedX;
		this.speedY = speedY;
	}

	@Override
	public void moveToTarget(int delta) {
		/** Move **/
		int movement = 0;
		if (target.getX() - dontGetCloserThan > me.getX())// X+
			movement += 1;
		else if (target.getX() + dontGetCloserThan < me.getX())// X-
			movement -= 1;
		if (target.getY() - dontGetCloserThan > me.getY())// Y+
			movement += 3;
		else if (target.getY() + dontGetCloserThan < me.getY())// Y-
			movement -= 3;
		switch (movement) {
			case -4:// X- Y-
				me.setDX(speedX * -1);
				me.setDY(speedY * -1);
				break;
			case -3:// Y-
				me.setDY(speedY * -1);
				break;
			case -2:// X+ Y-
				me.setDX(speedX);
				me.setDY(speedY * -1);
				break;
			case -1:// X-
				me.setDX(speedX * -1);
				break;
			case 1:// X+
				me.setDX(speedX);
				break;
			case 2:// X- Y+
				me.setDX(speedX * -1);
				me.setDY(speedY);
				break;
			case 3:// Y+
				me.setDY(speedY);
				break;
			case 4:// X+ Y+
				me.setDX(speedX);
				me.setDY(speedY);
				break;
		}
		/** Check For Collision **/
		// TODO: make entities not collide
		Entity2DAbstract me_test = (Entity2DAbstract) me.getEntity();
		me_test.setX(me.getX() + me.getDX() * delta);
		me_test.setY(me.getY() + me.getDY() * delta);
		for (int i = 0; i < LevelHandler.loadLevel(GameBase.currentLevel).numberOfEntities(); i++) {
			Entity2D e2d = LevelHandler.loadLevel(GameBase.currentLevel).getEntity(i);
			if (e2d instanceof Entity2DAbstract && !me.equals(e2d) && me_test.intersect(e2d)) {
				if (me_test.getX() >= e2d.getX() - e2d.getWidth() / 2 && me_test.getX() < e2d.getX() + e2d.getWidth() / 2)
					me.setDX(0);// collided on X, move back
				if (me_test.getY() >= e2d.getY() - e2d.getHeight() / 2 && me_test.getY() < e2d.getY() + e2d.getHeight() / 2)
					me.setDY(0);// collided on Y, move back
			}
		}
	}
}