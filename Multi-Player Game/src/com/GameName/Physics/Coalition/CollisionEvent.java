package com.GameName.Physics.Coalition;

public class CollisionEvent {
	private boolean isColliding;
	private float distance;
	
	public CollisionEvent(boolean isColliding, float distance) {
		this.isColliding = isColliding;
		this.distance = distance;
	}
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public float getDistance() {
		return distance;
	}
	
}
