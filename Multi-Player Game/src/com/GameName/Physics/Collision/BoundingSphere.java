package com.GameName.Physics.Collision;

import com.GameName.Physics.PhysicsUtil.CardinalDirection;
import com.GameName.Util.Vectors.MathVec3f;

public class BoundingSphere extends Collidable {

	private MathVec3f pos; 
	private float radius;
	
	public BoundingSphere(MathVec3f pos, float radius) {
		super(Shape.BoundingSphere);
		
		this.pos = pos;
		this.radius = radius;
	}

	public CollisionEvent intersect(Collidable other) {
		switch(other.getShape()) {		
//			case BoundingBox:	 return intersectBoundingSphereWithBoundingBox(this, (BoundingBox) other);
			case BoundingSphere: return intersectBoundingSphereWithBoundingSphere(this, (BoundingSphere) other); 
			
			default: System.err.println("Error: Tried to intersect a BoundingSphere and an Unknown Shape"); 
					 return new CollisionEvent(false, new MathVec3f(0, 0, 0));			
		}
	}
	
	public Collidable clone(MathVec3f amount) {
		return new BoundingSphere(pos, radius).translate(amount);
	}
	
	public Collidable translate(MathVec3f amount) {
		pos.add(amount);
		return this;
	}

	public MathVec3f getPos() {
		return pos;
	}

	public float getRadius() {
		return radius;
	}

	public MathVec3f getCenter() {
		return pos;
	}

	public float getVolume() {
		return (float) ((4f/3f) * Math.PI * Math.pow(radius, 3));
	}
	
	public float getSurfaceArea(CardinalDirection dir) {
		return (float) (4 * Math.PI * (radius * radius));
	}
}
