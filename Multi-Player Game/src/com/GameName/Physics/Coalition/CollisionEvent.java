package com.GameName.Physics.Coalition;

import com.GameName.Util.Vector3f;

public class CollisionEvent {
	private boolean isColliding;
	private Vector3f direction;
	
	public CollisionEvent(boolean isColliding, Vector3f direction) {
		this.isColliding = isColliding;
		this.direction = direction;
	}
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public float getDistance() {
		return direction.length();
	}
	
	public Vector3f getDirection() {
		return direction;
	}
}
