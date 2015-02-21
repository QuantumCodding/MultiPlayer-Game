package com.GameName.Physics.Dispatch;

import java.util.ArrayList;

import com.GameName.Physics.PhysicsObject;
import com.GameName.Physics.Collision.AxisAligneBoundingBox;
import com.GameName.Util.Vectors.Vector3f;

public class AABBCollisionDispacher extends CollisionDispatcher {
	public ArrayList<IntersectionResult> checkCollisions(ArrayList<PhysicsObject> objects) {
		ArrayList<IntersectionResult> results = new ArrayList<>();
		
		for(int i = 0; i < objects.size(); i ++) {
			for(int j = i; j < objects.size(); j ++) {
				results.add(intersect(
						objects.get(i).getCollisionShape().getAABB(), 
						objects.get(j).getCollisionShape().getAABB())
					);
			}
		}
		
		return results;
	}
	
	private IntersectionResult intersect(AxisAligneBoundingBox shape_1, AxisAligneBoundingBox shape_2) {
		Vector3f distances1 = shape_1.getMaxPos().subtract(shape_2.getMinPos());
		Vector3f distances2 = shape_1.getMinPos().subtract(shape_2.getMaxPos());
		
		Vector3f distances = distances1.max(distances2);		
		float maxDistance = distances.max();
		
		return new IntersectionResult(maxDistance < 0, distances);
	}
}
