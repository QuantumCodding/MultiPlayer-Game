package com.GameName.Physics.Collision;

import com.GameName.Util.Side;
import com.GameName.Util.Vectors.Vector3f;

public class BoxShape extends CollisionShape {
	private Vector3f center, radius, rotation;
	
	public BoxShape(Vector3f center, Vector3f radius) {
		super();
		
		this.center = center;
		this.radius = radius;
	}

	public AxisAligneBoundingBox getAABB() {
		rotation = new Vector3f(0, 0, 0);
		
		Vector3f minPos = radius.rotate(rotation).add(center);
		Vector3f maxPos = radius.rotate(rotation).subtract(center);
		
		return new AxisAligneBoundingBox(minPos, maxPos);
	}

	public void rotate(Vector3f amount) {
		rotation = rotation.add(amount);
	}

	public void translate(Vector3f amount) {
		center = center.add(amount);
	}

	public Vector3f getCenter()   { return center; }
	public Vector3f getRadius()   { return radius; }
	public Vector3f getRotation() { return rotation; }
	
	public float getSurfaceArea(Side side) {
		Vector3f length = radius.multiply(2);
		
		switch(side) {
			case BackFace: case FrontFace:
				return length.x * length.y;
				
			case BottomFace: case TopFace:
				return length.x * length.z;
			
			case LeftFace: case RightFace:
				return length.z * length.y;
				
			default: return 0.0f;
		}
	}
	
	public float getVolume() {
		Vector3f length = radius.multiply(2);
		return length.x * length.y * length.z;
	}
	
	public boolean intersect(Vector3f vertex) {
		Vector3f rotatedVertex = vertex.rotate(rotation.multiply(-1));
		rotatedVertex = rotatedVertex.difference(center);
		return rotatedVertex.lessThenOrEqual(radius);
	}
}
