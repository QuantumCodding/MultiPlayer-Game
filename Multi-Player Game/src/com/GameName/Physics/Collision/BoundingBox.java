package com.GameName.Physics.Collision;

import com.GameName.Physics.PhysicsUtil.CardinalDirection;
import com.GameName.Util.Vectors.MathVec3f;

public class BoundingBox extends Collidable {

	private MathVec3f minPos;
	private MathVec3f maxPos;
	private MathVec3f length;
	
	public BoundingBox(MathVec3f minPos, MathVec3f maxPos) {
		super(Shape.BoundingBox);
		
		this.minPos = minPos;
		this.maxPos = maxPos;

		length = new MathVec3f(minPos.getX() - maxPos.getX(), minPos.getY() - maxPos.getY(), minPos.getZ() - maxPos.getZ()).abs();
	}

	public CollisionEvent intersect(Collidable other) {
		switch(other.getShape()) {		
			case BoundingBox: 	 return intersectBoundingBoxWithBoundingBox(this, (BoundingBox) other);
//			case BoundingSphere: return intersectBoundingSphereWithBoundingBox((BoundingSphere) other, this); 
			
			default: System.err.println("Error: Tried to intersect a BoundingBox and an Unknown Shape"); 
					 return new CollisionEvent(false, new MathVec3f(0, 0, 0));			
		}
	}

	public Collidable clone(MathVec3f amount) {
		return new BoundingBox(minPos, minPos).translate(amount);
	}
	
	public Collidable translate(MathVec3f amount) {
		minPos.add(amount);
		maxPos.add(amount);
		
		return this;
	}

	public MathVec3f getMinPos() {
		return minPos;
	}

	public MathVec3f getMaxPos() {
		return maxPos;
	}

	public MathVec3f getCenter() {		
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
