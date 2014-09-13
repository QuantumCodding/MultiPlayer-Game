package com.GameName.Physics.Coalition;

import com.GameName.Util.Vectors.Vector3f;

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
		Vector3f direction = other.getCenter().subtract(getCenter());		
		float centerDistance = direction.length();
		
		direction.divideAndSet(centerDistance);
		float distance = centerDistance - radiusLength;
			
		return new CollisionEvent(distance < 0, direction.multiply(distance));	
	}

	CollisionEvent intersectBoundingBoxWithBoundingBox(BoundingBox box, BoundingBox other) {
		Vector3f distances1 = box.getMaxPos().subtract(other.getMinPos());
		Vector3f distances2 = box.getMinPos().subtract(other.getMaxPos());
		
		Vector3f distances = distances1.max(distances2);		
		float maxDistance = distances.max();
		
		return new CollisionEvent(maxDistance < 0, distances);
	}
	
	CollisionEvent intersectBoundingSphereWithBoundingBox(BoundingSphere sphere, BoundingBox box) {
		return null;
	}
	
	public abstract Collidable translate(Vector3f amount);
	
	public abstract Vector3f getCenter();
	
	public Shape getShape() {
		return shape;
	}
}
