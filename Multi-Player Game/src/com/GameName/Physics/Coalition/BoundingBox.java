package com.GameName.Physics.Coalition;

import com.GameName.Util.Vector3f;

public class BoundingBox extends Collidable {

	private Vector3f minPos;
	private Vector3f maxPos;
	
	public BoundingBox(Vector3f minPos, Vector3f maxPos) {
		super(Shape.BoundingBox);
		
		this.minPos = minPos;
		this.maxPos = maxPos;
	}

	public CollisionEvent intersect(Collidable other) {
		switch(other.getShape()) {		
			case BoundingBox: 	 return intersectBoundingBoxWithBoundingBox(this, (BoundingBox) other);
//			case BoundingSphere: return intersectBoundingSphereWithBoundingBox((BoundingSphere) other, this); 
			
			default: System.err.println("Error: Tried to intersect a BoundingBox and an Unknown Shape"); 
					 return new CollisionEvent(false, new Vector3f(0, 0, 0));			
		}
	}

	public void translate(Vector3f amount) {
		minPos.add(amount);
		maxPos.add(amount);
	}

	public Vector3f getMinPos() {
		return minPos;
	}

	public Vector3f getMaxPos() {
		return maxPos;
	}

	public Vector3f getCenter() {		
		return minPos.add(maxPos.subtract(minPos));
	}
}
