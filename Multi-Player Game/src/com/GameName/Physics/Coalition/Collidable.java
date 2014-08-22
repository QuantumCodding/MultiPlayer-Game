package com.GameName.Physics.Coalition;

import com.GameName.Util.Vector3f;

public abstract class Collidable {
	private Shape shape;
	
	protected enum Shape {
		BoundingSphere, BoundingBox//, BoundingPlain
	}
	
	protected Collidable(Shape shape) {
		this.shape = shape;
	}
	
	public abstract CollisionEvent intersect(Collidable other);
	
	CollisionEvent intersectBoundingSphereWithBoundingSphere(BoundingSphere sphere, BoundingSphere other) {
					
		float radiusLength = sphere.getRadius() + other.getRadius();
		float centerDistance = sphere.getPos().distance(other.getPos());
		float distance = centerDistance - radiusLength;
			
		return new CollisionEvent(distance < 0, distance);	
	}

	CollisionEvent intersectBoundingBoxWithBoundingBox(BoundingBox box, BoundingBox other) {
		float distance1 = Math.abs(box.getMaxPos().distance(other.getMinPos()));
		float distance2 = Math.abs(box.getMinPos().distance(other.getMaxPos()));
		
		float maxDistance = Math.max(distance1, distance2);
		
		return new CollisionEvent(maxDistance < 0, maxDistance);
	}
	
	CollisionEvent intersectBoundingSphereWithBoundingBox(BoundingSphere sphere, BoundingBox box) {
		return null;
	}
	
	public abstract void translate(Vector3f amount);
	
	public abstract Vector3f getCenter();
	
	public Shape getShape() {
		return shape;
	}
}
