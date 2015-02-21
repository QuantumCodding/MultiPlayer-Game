package com.GameName.Physics.Dispatch;

import java.util.ArrayList;

import com.GameName.Physics.PhysicsObject;
import com.GameName.Physics.Collision.ConvexShape;
import com.GameName.Physics.Collision.ConvexShape.Triangle;
import com.GameName.Util.Vectors.Vector3f;

public class ConvexPolygonCollisionDispatcher extends CollisionDispatcher {

	public ArrayList<IntersectionResult> checkCollisions(ArrayList<PhysicsObject> objects) {
		ArrayList<IntersectionResult> results = new ArrayList<>();
		
		for (int i = 0; i < objects.size(); i ++) {
			for(int j = i; j < objects.size(); i ++) {
				if(objects.get(i).getCollisionShape() instanceof ConvexShape &&
				   objects.get(j).getCollisionShape() instanceof ConvexShape) {
				
					results.add(intersect(
							(ConvexShape) objects.get(i).getCollisionShape(), 
							(ConvexShape) objects.get(j).getCollisionShape())
						);
				}
			}
		}
		
		return results;
	}

	private IntersectionResult intersect(ConvexShape shape_1, ConvexShape shape_2) {
		for(Triangle tri : shape_2.getFaces()) { 
			skip:
			for(Vector3f vert : tri.getVertsArray()) {				
				for(Triangle plane : shape_1.getFaces()) {
					if(!intersectsPlane(plane, vert)) continue skip;
				}
				
				return new IntersectionResult(true, shape_1.getCenter().subtract(vert));
			}
		}
		

		return new IntersectionResult(false, shape_1.getCenter().subtract(shape_2.getCenter()));
	}
	
	private boolean intersectsPlane(Triangle plane, Vector3f checking) {
		Vector3f normal = plane.getNormal(), vertex = plane.getVertA();
		Vector3f external = plane.getCenter().add(normal), intersectionPoint; 
		int t = 0;
		
		while(!(intersectionPoint = external.add(checking.subtract(external).multiply(t ++))).equals(checking)) {
			if(0 == normal.x * (intersectionPoint.x - vertex.x) +
					normal.y * (intersectionPoint.y - vertex.y) +
					normal.z * (intersectionPoint.z - vertex.z) ) 
				return true;
		}
		
		return false;
	}
}
