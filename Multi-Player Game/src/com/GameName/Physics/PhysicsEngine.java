package com.GameName.Physics;

import java.util.ArrayList;

import com.GameName.Physics.Dispatch.CollisionDispatcher;
import com.GameName.Physics.Dispatch.IntersectionResult;
import com.GameName.Util.Vectors.Vector3f;
/*
 *	TODO: Add .obj to Convex Mesh 
 *	TODO: Add check to see if ConvexShape is convex
 */

public class PhysicsEngine {
	public static final Vector3f DEFAULT_GRAVITY = new Vector3f(0, -10, 0);
	
	private ArrayList<PhysicsObject> objects;
	private CollisionDispatcher collisionDispatch;
	
	public PhysicsEngine(CollisionDispatcher collisionDispatch) {
		objects = new ArrayList<>();
		this.collisionDispatch = collisionDispatch;
	}
	
	public void simulate(float delta) {
		for(PhysicsObject obj : objects) {
			obj.intergrate(delta);
		}
		
		ArrayList<IntersectionResult> collisions = collisionDispatch.checkCollisions(objects);
		for(IntersectionResult result : collisions) {
			if(result.isColliding()) {
				
			}
		}
	}
	
	public void add(PhysicsObject obj) {
		objects.add(obj);
	}
	
	public void cleanUp() {
		
	}
}
