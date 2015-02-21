package com.GameName.Physics.Collision;

import com.GameName.Util.Vectors.Vector3f;

public class AxisAligneBoundingBox extends CollisionShape {
	private Vector3f minPos, maxPos, length;
	
	public AxisAligneBoundingBox(Vector3f minPos, Vector3f maxPos) {
		super();
		
		if(maxPos.lessThen(minPos)) {
			this.minPos = maxPos;
			this.maxPos = minPos;			
		} else {
			this.minPos = minPos;
			this.maxPos = maxPos;
		}
		
		length = maxPos.subtract(minPos);
	}

	public AxisAligneBoundingBox getAABB() {
		return this;
	}

	public void rotate(Vector3f amount) {
		throw new IllegalStateException("AxisAligneBoundingBox can not be Rotated!");
	}

	public void translate(Vector3f amount) {
		minPos = minPos.add(amount);
		maxPos = maxPos.add(amount);
	}

	public Vector3f getCenter() {
		return minPos.add(length.divide(2));
	}

	public Vector3f getMinPos() { return minPos; }
	public Vector3f getMaxPos() { return maxPos; }
	public Vector3f getLength() { return length; }
	
	public boolean intersect(Vector3f vertex) {
		return vertex.lessThenOrEqual(maxPos) && 
				vertex.greaterThenOrEqual(minPos);
	}
}
