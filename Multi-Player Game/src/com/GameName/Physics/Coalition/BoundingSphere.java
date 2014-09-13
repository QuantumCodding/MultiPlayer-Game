package com.GameName.Physics.Coalition;

import com.GameName.Util.Vectors.Vector3f;

public class BoundingSphere extends Collidable {

	private Vector3f pos; 
	private float radius;
	
	public BoundingSphere(Vector3f pos, float radius) {
		super(Shape.BoundingSphere);
		
		this.pos = pos;
		this.radius = radius;
	}

	public CollisionEvent intersect(Collidable other) {
		switch(other.getShape()) {		
//			case BoundingBox:	 return intersectBoundingSphereWithBoundingBox(this, (BoundingBox) other);
			case BoundingSphere: return intersectBoundingSphereWithBoundingSphere(this, (BoundingSphere) other); 
			
			default: System.err.println("Error: Tried to intersect a BoundingSphere and an Unknown Shape"); 
					 return new CollisionEvent(false, new Vector3f(0, 0, 0));			
		}
	}
	
	public Collidable translate(Vector3f amount) {
		pos.add(amount);
		return this;
	}

	public Vector3f getPos() {
		return pos;
	}

	public float getRadius() {
		return radius;
	}

	public Vector3f getCenter() {
		return pos;
	}
}
