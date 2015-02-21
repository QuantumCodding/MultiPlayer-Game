package com.GameName.Physics.Collision;

import com.GameName.Util.Side;
import com.GameName.Util.Vectors.Vector3f;

public abstract class CollisionShape {
	public abstract AxisAligneBoundingBox getAABB();
	
	public abstract void rotate(Vector3f amount);
	public abstract void translate(Vector3f amount);
	public abstract Vector3f getCenter();
	
	public abstract boolean intersect(Vector3f vertex);
	
	public float getSurfaceArea(Side side) {
		Vector3f length = getAABB().getLength();
		
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
		AxisAligneBoundingBox aabb = getAABB();
		Vector3f length = aabb.getLength();
		return length.x * length.y * length.z;
	}
}
