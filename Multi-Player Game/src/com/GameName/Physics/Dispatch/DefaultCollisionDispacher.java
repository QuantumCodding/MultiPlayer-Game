package com.GameName.Physics.Dispatch;

import java.util.ArrayList;

import com.GameName.Physics.PhysicsObject;
import com.GameName.Physics.Collision.AxisAligneBoundingBox;
import com.GameName.Physics.Collision.BoxShape;
import com.GameName.Physics.Collision.CollisionShape;
import com.GameName.Physics.Collision.ConvexShape;
import com.GameName.Physics.Collision.ConvexShape.Triangle;
import com.GameName.Physics.Collision.CylinderShape;
import com.GameName.Physics.Collision.SphereShape;
import com.GameName.Util.Vectors.Vector3f;

public class DefaultCollisionDispacher extends CollisionDispatcher {
	public ArrayList<IntersectionResult> checkCollisions(ArrayList<PhysicsObject> objects) {
		ArrayList<IntersectionResult> results = new ArrayList<>();
	
		for(int i = 0; i < objects.size(); i ++) {
		for(int j = 0; j < objects.size(); j ++) {
			if(i == j) continue;
			
			CollisionShape shape_1 = objects.get(i).getCollisionShape();
			CollisionShape shape_2 = objects.get(j).getCollisionShape();
			
			if(intersectAABB(shape_1.getAABB(), shape_2.getAABB())) {
				if(shape_1 instanceof ConvexShape) {
					results.add(intersect(shape_2, (ConvexShape) shape_1));
					
				} else if(shape_1 instanceof SphereShape) {										//---------------------------------//
					if(shape_2 instanceof SphereShape)													// Sphere Shape
						results.add(intersect((SphereShape) shape_1, (SphereShape) shape_2));
					else if(shape_2 instanceof ConvexShape)											 	// Convex Shape
						results.add(intersect((SphereShape) shape_1, (ConvexShape) shape_2));
					else if(shape_2 instanceof AxisAligneBoundingBox) 									// AxisAligneBoundingBox
						results.add(intersect((SphereShape) shape_1, (AxisAligneBoundingBox) shape_2));
					else if(shape_2 instanceof CylinderShape) 											// Cylinder Shape
						results.add(intersect((SphereShape) shape_1, (CylinderShape) shape_2));
					else if(shape_2 instanceof BoxShape) 												// Box Shape
						results.add(intersect((SphereShape) shape_1, (BoxShape) shape_2));					
					
				} else if(shape_1 instanceof CylinderShape) {									//---------------------------------//
					if(shape_2 instanceof SphereShape)													// Sphere Shape
						results.add(intersect((SphereShape) shape_2, (CylinderShape) shape_1));
					else if(shape_2 instanceof ConvexShape)											 	// Convex Shape
						results.add(intersect((CylinderShape) shape_1, (ConvexShape) shape_2));
					else if(shape_2 instanceof AxisAligneBoundingBox) 									// AxisAligneBoundingBox
						results.add(intersect((CylinderShape) shape_1, (AxisAligneBoundingBox) shape_2));
					else if(shape_2 instanceof CylinderShape) 											// Cylinder Shape
						results.add(intersect((CylinderShape) shape_1, (CylinderShape) shape_2));
					else if(shape_2 instanceof BoxShape) 												// Box Shape
						results.add(intersect((CylinderShape) shape_1, (BoxShape) shape_2));					
					
				} else if(shape_1 instanceof BoxShape) {										//---------------------------------//
					if(shape_2 instanceof SphereShape)													// Sphere Shape
						results.add(intersect((SphereShape) shape_2, (BoxShape) shape_1));
					else if(shape_2 instanceof ConvexShape)											 	// Convex Shape
						results.add(intersect((BoxShape) shape_1, (ConvexShape) shape_2));
					else if(shape_2 instanceof AxisAligneBoundingBox) 									// AxisAligneBoundingBox
						results.add(intersect((BoxShape) shape_1, (AxisAligneBoundingBox) shape_2));
					else if(shape_2 instanceof CylinderShape) 											// Cylinder Shape
						results.add(intersect((CylinderShape) shape_2, (BoxShape) shape_1));
					else if(shape_2 instanceof BoxShape) 												// Box Shape
						results.add(intersect((BoxShape) shape_1, (BoxShape) shape_2));					
					
				} else if(shape_1 instanceof AxisAligneBoundingBox) {							//---------------------------------//
					if(shape_2 instanceof SphereShape)													// Sphere Shape
						results.add(intersect((SphereShape) shape_2, (AxisAligneBoundingBox) shape_1));
					else if(shape_2 instanceof ConvexShape)											 	// Convex Shape
						results.add(intersect((AxisAligneBoundingBox) shape_1, (ConvexShape) shape_2));
					else if(shape_2 instanceof AxisAligneBoundingBox) 									// AxisAligneBoundingBox
						results.add(intersect((AxisAligneBoundingBox) shape_1, (AxisAligneBoundingBox) shape_2));
					else if(shape_2 instanceof CylinderShape) 											// Cylinder Shape
						results.add(intersect((CylinderShape) shape_2, (AxisAligneBoundingBox) shape_1));
					else if(shape_2 instanceof BoxShape) 												// Box Shape
						results.add(intersect((BoxShape) shape_2, (AxisAligneBoundingBox) shape_1));					
				}
			}
		}}
		
		return results;
	}
	
	private boolean intersectAABB(AxisAligneBoundingBox shape_1, AxisAligneBoundingBox shape_2) {
		Vector3f distances1 = shape_1.getMaxPos().subtract(shape_2.getMinPos());
		Vector3f distances2 = shape_1.getMinPos().subtract(shape_2.getMaxPos());
		
		Vector3f distances = distances1.max(distances2);		
		float maxDistance = distances.max();
		
		return maxDistance < 0;
	}
	
	private IntersectionResult intersect(CollisionShape shape, ConvexShape convex) {
		for(Triangle tri : convex.getFaces()) {
		for(Vector3f vertex : tri.getVertsArray()) {
			if(convex.intersect(vertex)) {
				return new IntersectionResult(true, convex.getCenter().subtract(vertex));
			}
		}}
		
		return new IntersectionResult(false, convex.getCenter().subtract(convex.getCenter()));
	}
	
	private IntersectionResult intersect(SphereShape sphere, AxisAligneBoundingBox AABB) {
		return new IntersectionResult(
				(AABB.getMaxPos().greaterThen(sphere.getCenter()) && AABB.getMinPos().lessThen(sphere.getCenter())) ||
				(AABB.getMaxPos().distance(sphere.getCenter()) <= sphere.getRadius()) ||
				(AABB.getMinPos().distance(sphere.getCenter()) <= sphere.getRadius()), 
				
				sphere.getCenter().subtract(AABB.getCenter())
			);
	}
	
	private IntersectionResult intersect(SphereShape sphere, CylinderShape cylinder) {
		Vector3f rotatedCenter = sphere.getCenter().rotate(cylinder.getRotation());
		Vector3f differcnce = rotatedCenter.difference(cylinder.getCenter());
		
		return new IntersectionResult(
				differcnce.x <= sphere.getRadius() + cylinder.getRadius() &&
				differcnce.z <= sphere.getRadius() + cylinder.getRadius() &&
				differcnce.y <= sphere.getRadius() + cylinder.getHeight() / 2.0f, 				
				differcnce
			);
	}
	
	private IntersectionResult intersect(SphereShape sphere_1, SphereShape sphere_2) {
		return new IntersectionResult(
				sphere_1.getCenter().distance(sphere_2.getCenter()) <= 
					sphere_1.getRadius() + sphere_2.getRadius(), 
				sphere_1.getCenter().subtract(sphere_2.getCenter())
			);
	}
	
	private IntersectionResult intersect(SphereShape sphere, BoxShape box) {
		Vector3f rotatedCenter = sphere.getCenter().rotate(box.getRotation());
		Vector3f minPos = box.getCenter().subtract(box.getRadius());
		Vector3f maxPos = box.getCenter().add(box.getRadius());
		
		return new IntersectionResult(
				(maxPos.greaterThen(rotatedCenter) && minPos.lessThen(rotatedCenter)) ||
				(maxPos.distance(rotatedCenter) <= sphere.getRadius()) ||
				(minPos.distance(rotatedCenter) <= sphere.getRadius()), 
				
				rotatedCenter.subtract(box.getCenter())
			);
	}
	
	private IntersectionResult intersect(CylinderShape cylinder, AxisAligneBoundingBox AABB) {
		Vector3f maxDiff = AABB.getMaxPos().difference(cylinder.getCenter());
		Vector3f minDiff = AABB.getMinPos().difference(cylinder.getCenter());
		
		return new IntersectionResult(
				(AABB.getMaxPos().greaterThen(cylinder.getCenter()) && AABB.getMinPos().lessThen(cylinder.getCenter())) ||
				(maxDiff.x <= cylinder.getRadius()) || (maxDiff.z <= cylinder.getRadius()) ||
				(minDiff.x <= cylinder.getRadius()) || (minDiff.z <= cylinder.getRadius()) ||
				(maxDiff.y <= cylinder.getHeight()/2.0f) || (minDiff.y <= cylinder.getHeight()/2.0f) ||
				(AABB.getMinPos().distance(cylinder.getCenter()) <= cylinder.getRadius()), 
				
				cylinder.getCenter().subtract(AABB.getCenter())
			);
	}
	
	private IntersectionResult intersect(CylinderShape cylinder_1, CylinderShape cylinder_2) {
		throw new IllegalStateException("Cylinder to Cylinder collision not suported");
	}
	
	private IntersectionResult intersect(CylinderShape cylinder, BoxShape box) {
		throw new IllegalStateException("Cylinder to Box collision not suported");
	}
	
	private IntersectionResult intersect(BoxShape box, AxisAligneBoundingBox AABB) {
		Vector3f maxDiff = AABB.getMaxPos().difference(box.getCenter());
		Vector3f minDiff = AABB.getMinPos().difference(box.getCenter());
		
		return new IntersectionResult(
				(AABB.getMaxPos().greaterThen(box.getCenter()) && AABB.getMinPos().lessThen(box.getCenter())) ||
				(maxDiff.x <= box.getRadius().x) || (maxDiff.z <= box.getRadius().z) ||
				(minDiff.x <= box.getRadius().x) || (minDiff.z <= box.getRadius().z) ||
				(maxDiff.y <= box.getRadius().y) || (minDiff.y <= box.getRadius().y) ||
				(AABB.getMinPos().difference(box.getCenter()).lessThenOrEqual(box.getRadius())), 
				
				box.getCenter().subtract(AABB.getCenter())
			);
	}
	
	private IntersectionResult intersect(BoxShape box_1, BoxShape box_2) {
		throw new IllegalStateException("Box to Box collision not suported");
	}
	
	private IntersectionResult intersect(AxisAligneBoundingBox shape_1, AxisAligneBoundingBox shape_2) {
		Vector3f distances1 = shape_1.getMaxPos().subtract(shape_2.getMinPos());
		Vector3f distances2 = shape_1.getMinPos().subtract(shape_2.getMaxPos());
		
		Vector3f distances = distances1.max(distances2);		
		float maxDistance = distances.max();
		
		return new IntersectionResult(maxDistance < 0, distances);
	}
}
