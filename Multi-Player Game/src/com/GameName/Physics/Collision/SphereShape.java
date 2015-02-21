package com.GameName.Physics.Collision;

import com.GameName.Util.Side;
import com.GameName.Util.Vectors.Vector3f;

public class SphereShape extends CollisionShape {
	private Vector3f center;
	private float radius;
	
	public SphereShape(Vector3f center, float radius) {
		super();
		
		this.center = center;
		this.radius = radius;
	}

	public AxisAligneBoundingBox getAABB() {
		return new AxisAligneBoundingBox(center.subtract(radius), center.add(radius));
	}

	public void rotate(Vector3f amount) {}
	public void translate(Vector3f amount) {
		center = center.add(amount);
	}

	public Vector3f getCenter() { return center; }	
	public float getRadius() { return radius; }
	
	public float getVolume() {
		return (float) ((4f/3f) * Math.PI * Math.pow(radius, 3));
	}
	
	public float getSurfaceArea(Side side) {
		return (float) (2 * Math.PI * (radius * radius));
	}
	
	public boolean intersect(Vector3f vertex) {
		return vertex.difference(center).lessThenOrEqual(radius);
	}
}
