package com.GameName.Physics;

import static java.lang.Math.pow;

import java.util.ArrayList;

import com.GameName.Physics.PhysicsUtil.Collision;
import com.GameName.Physics.Collision.CollisionEvent;
import com.GameName.Physics.Object.Material;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.IEngine;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class PhysicsEngine implements IEngine<PhysicsObject> {
	public static final float GRAVITY = 9.807f;
	public static final float MAX_VELOCITY = 10.0f;
	
	private ArrayList<PhysicsObject> objects;
	private World world;
	
	public PhysicsEngine(World world) {
		objects = new ArrayList<>();
		this.world = world;
	}	
	
	public World getWorld() {return world;}
	
	public void add(PhysicsObject obj) { objects.add(obj); }
	public void remove(PhysicsObject obj) { objects.remove(obj); }

	public void step(float delta) {
		simulate(delta);		
		ArrayList<Collision> collisions = detectCollisions();
		handelCollisions(collisions);
	}

	public void simulate(float delta) {
		for(PhysicsObject obj : objects) {
			if(obj == null) continue;
			
			obj.intergrate(this, delta);
		}
	}
	
	public ArrayList<Collision> detectCollisions() {		
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		
		for(int i = 0; i < objects.size(); i ++) {
			if(objects.get(i) == null) continue;
			
			for(int j = i + 1; j < objects.size(); j ++) {
				if(objects.get(j) == null) continue;
				
				CollisionEvent event = objects.get(i).getAccess().getBounding().intersect(
									   objects.get(j).getAccess().getBounding());
				
				if(event.isColliding()) {
					collisions.add(new Collision(event, objects.get(i), objects.get(j)));
				}
			}	
		}
		
		return collisions;
	}
	
	public void handelCollisions(ArrayList<Collision> collisions) {
		for(Collision collision : collisions) {
			if(!collision.getEvent().isColliding()) continue;
			
			collision.getObject1().handelCollision(collision);
			collision.getObject2().handelCollision(collision);
			
//			Vector3f direction = collision.getEvent().getDirection().normalized();
//			Vector3f otherDirection = direction.reflect(objects.get(collision.getObject1()).getAccess().getVel().normalized());
//			
//			objects.get(collision.getObject1()).getAccess().getVel().multiplyAndSet(otherDirection);
//			objects.get(collision.getObject2()).getAccess().getVel().multiplyAndSet(direction);
		}
	}
	
	public float getMass(Material type, float volume) {
		return type.getDensity() * volume;
	}
	
	public float getForce(float acc, float mass) {
		return mass * acc;
	}
	
	public float getAcceleration(float force, float mass) {
		return force / mass;
	}
	
	public float getDrag(Material median, float vel, float crossSectionalArea) {
//		if(type.getPhase() == Phase.Solid) return 0.0f;		
		return (float) (0.5f * median.getDensity() * pow(vel, 2) * crossSectionalArea);
	}
	
	public float getFriction(Material surface, float perpendicularForce) {
		return surface.getFrictionalCoefficient() * perpendicularForce;
	}
	
	public float getOppsiteDir(float dir) {
		return dir > 0 ? -1 : dir < 0 ? 1 : 0;
	}
	
	public Vector3f getAcceleration(Vector3f force, float mass) {
		return force.divide(mass);
	}
	
	public void cleanUp() {
		
	}
}
