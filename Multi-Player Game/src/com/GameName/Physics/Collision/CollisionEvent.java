package com.GameName.Physics.Collision;

import com.GameName.Util.Vectors.MathVec3f;

public class CollisionEvent {
	private boolean isColliding;
	private MathVec3f direction;
	
	public CollisionEvent(boolean isColliding, MathVec3f direction) {
		this.isColliding = isColliding;
		this.direction = direction;
	}
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public float getDistance() {
		return direction.length();
	}
	
	public MathVec3f getDirection() {
		return direction;
	}

	public String toString() {
		return "CollisionEvent [isColliding=" + isColliding + ", direction=" + direction + "]";
	}
}
