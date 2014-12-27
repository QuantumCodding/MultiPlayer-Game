package com.GameName.Physics.Collision;

import com.GameName.Physics.PhysicsUtil.CardinalDirection;
import com.GameName.Util.Vectors.Vector3f;

public class BoundingBox extends Collidable {

	private Vector3f minPos;
	private Vector3f maxPos;
	private Vector3f length;
	
	public BoundingBox(Vector3f minPos, Vector3f maxPos) {
		super(Shape.BoundingBox);
		
		this.minPos = minPos;
		this.maxPos = maxPos;

		length = new Vector3f(minPos.getX() - maxPos.getX(), minPos.getY() - maxPos.getY(), minPos.getZ() - maxPos.getZ()).abs();
	}

	public CollisionEvent intersect(Collidable other) {
		switch(other.getShape()) {		
			case BoundingBox: 	 return intersectBoundingBoxWithBoundingBox(this, (BoundingBox) other);
//			case BoundingSphere: return intersectBoundingSphereWithBoundingBox((BoundingSphere) other, this); 
			
			default: System.err.println("Error: Tried to intersect a BoundingBox and an Unknown Shape"); 
					 return new CollisionEvent(false, new Vector3f(0, 0, 0));			
		}
	}

	public Collidable clone(Vector3f amount) {
		return new BoundingBox(minPos, minPos).translate(amount);
	}
	
	public Collidable translate(Vector3f amount) {
		minPos.add(amount);
		maxPos.add(amount);
		
		return this;
	}

	public Vector3f getMinPos() {
		return minPos;
	}

	public Vector3f getMaxPos() {
		return maxPos;
	}

	public Vector3f getCenter() {		
		return minPos.add(length.divide(2));
	}

	public float getVolume() {
		return length.getX() * length.getY() * length.getZ();
	}

	public float getSurfaceArea(CardinalDirection dir) {
		switch(dir) {
			case Bottom: case Top: 	return length.getX() * length.getZ();
			case East: case West:  	return length.getZ() * length.getY();
			case North: case South:	return length.getX() * length.getY();	
			
			default: return 0;
		}
	}
}
