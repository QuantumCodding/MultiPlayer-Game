package com.GameName.Physics;

import java.util.ArrayList;

import com.GameName.Physics.Coalition.CollisionEvent;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.IEngine;
import com.GameName.Util.Vectors.Vector3f;

public class PhysicsEngine implements IEngine<PhysicsObject> {
	
	private ArrayList<PhysicsObject> objects;
	
	public PhysicsEngine() {
		objects = new ArrayList<PhysicsObject>();
	}	
	
	public void add(PhysicsObject obj) {
		objects.add(obj);
	}

	public void remove(PhysicsObject obj) {
		objects.remove(obj);
	}	

	public void step(float delta) {
		simulate(delta);
		
		ArrayList<Collision> collisions = detectCollisions();
		handelCollisions(collisions);
	}
	
	public void simulate(float delta) {
		for(PhysicsObject obj : objects) {
			if(obj == null) continue;
			
			obj.intergrate(delta);
		}
	}
	
	public ArrayList<Collision> detectCollisions() {		
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		
		for(int i = 0; i < objects.size(); i ++) {
			if(objects.get(i) == null) continue;
			
			for(int j = i + 1; j < objects.size(); j ++) {
				if(objects.get(j) == null) continue;
				
				CollisionEvent event = objects.get(i).getAccess().getBoundingArea().intersect(
									   objects.get(j).getAccess().getBoundingArea());
				
				if(event.isColliding()) {
					collisions.add(new Collision(event, i, j));
				}
			}	
		}
		
		return collisions;
	}
	
	public void handelCollisions(ArrayList<Collision> collisions) {
		for(Collision collision : collisions) {
			if(!collision.event.isColliding()) continue;
			
			Vector3f direction = collision.event.getDirection().normalized();
			Vector3f otherDirection = direction.reflect(objects.get(collision.object1).getAccess().getVel().normalized());
			
			objects.get(collision.object1).getAccess().getVel().multiplyAndSet(otherDirection);
			objects.get(collision.object2).getAccess().getVel().multiplyAndSet(direction);
		}
	}
	
	public class Collision {
		private CollisionEvent event;
		
		private int object1;
		private int object2;
		
		public Collision(CollisionEvent event, int object1, int object2) {
			this.event = event;
			this.object1 = object1;
			this.object2 = object2;
		}		
	}

	public void cleanUp() {
		
	}
}
