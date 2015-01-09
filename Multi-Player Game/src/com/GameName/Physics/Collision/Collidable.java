package com.GameName.Physics.Collision;

import static com.GameName.Physics.PhysicsUtil.CardinalDirection;
import com.GameName.Util.Vectors.MathVec3f;

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
		MathVec3f direction = other.getCenter().subtract(getCenter());		
		float centerDistance = direction.length();
		
		direction.divideAndSet(centerDistance);
		float distance = centerDistance - radiusLength;
			
		return new CollisionEvent(distance < 0, direction.multiply(distance));	
	}

	CollisionEvent intersectBoundingBoxWithBoundingBox(BoundingBox box, BoundingBox other) {
		MathVec3f distances1 = box.getMaxPos().subtract(other.getMinPos());
		MathVec3f distances2 = box.getMinPos().subtract(other.getMaxPos());
		
		MathVec3f distances = distances1.max(distances2);		
		float maxDistance = distances.max();
		
		return new CollisionEvent(maxDistance < 0, distances);
	}
	
	CollisionEvent intersectBoundingSphereWithBoundingBox(BoundingSphere sphere, BoundingBox box) {
		return null;
	}
	
	public abstract Collidable clone(MathVec3f amount);	
	public abstract Collidable translate(MathVec3f amount);	
	public abstract MathVec3f getCenter();
	public abstract float getSurfaceArea(CardinalDirection dir);
	public abstract float getVolume();
	
	public Shape getShape() {
		return shape;
	}
}
