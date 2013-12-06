package com.etk2000.Entity.AI;

import com.etk2000.Entity.Entity2DAbstract;

public abstract class AI {
	protected Entity2DAbstract me;
	protected Entity2DAbstract target;
	
	protected float dontGetCloserThan;
	
	public AI(Entity2DAbstract me, float dontGetCloserThan) {
		this.me = me;
		this.dontGetCloserThan = dontGetCloserThan;
	}
	
	public abstract void moveToTarget(int delta);
	
	/** Getters **/
	
	public final Entity2DAbstract getTarget() {
		return target;
	}
	
	/** Setters **/
	
	public final void setTarget(Entity2DAbstract newTarget) {
		target = newTarget;
	}
}