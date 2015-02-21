package com.GameName.Physics.Collision;

import com.GameName.Util.Vectors.Vector3f;

public class CylinderShape extends CollisionShape {
	private float radius, height;
	private Vector3f center, rotation;
	
	public CylinderShape(float radius, float height, Vector3f center) {
		super();
		
		this.radius = radius;
		this.height = height;
		this.center = center;
	}

	public AxisAligneBoundingBox getAABB() {
		Vector3f minPos = center.subtract(radius).subtract(height/2.0f).rotate(rotation);
		Vector3f maxPos = center.add(radius).add(height/2.0f).rotate(rotation);
		return new AxisAligneBoundingBox(minPos, maxPos);
	}

	public void rotate(Vector3f amount) {
		rotation.add(amount);
	}

	public void translate(Vector3f amount) {
		center.add(amount);
	}

	public float getVolume() {
		return (float) (Math.PI * radius * radius * height);
	}
	
	public Vector3f getCenter() { return center; }
	public float getRadius() { return radius; }
	public float getHeight() { return height; }
	public Vector3f getRotation() { return rotation; }
	
	public boolean intersect(Vector3f vertex) {
		Vector3f rotatedVertex = vertex.rotate(rotation.multiply(-1));
		rotatedVertex = rotatedVertex.difference(center);
		
		return rotatedVertex.x <= radius && rotatedVertex.z <= radius &&
					rotatedVertex.y <= height / 2.0f;
	}
}
