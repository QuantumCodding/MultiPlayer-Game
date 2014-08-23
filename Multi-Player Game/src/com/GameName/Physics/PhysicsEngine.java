package com.GameName.Physics;

import java.util.ArrayList;

import com.GameName.Main.GameName;
import com.GameName.Physics.Coalition.CollisionEvent;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vector3f;
import com.GameName.World.World;

public class PhysicsEngine {
	
	public static final float GRAVITY = 0.01f;
	public static final float TERMINAL_VELOCITY = -4.0f;
	
	public static final int FORWARD = 0, BACKWARD = 1;
	public static final int LEFT 	= 2, RIGHT 	  = 3;
	public static final int UP 		= 4, DOWN 	  = 5;
	
	public static boolean canMove(Vector3f pos, int direction, World w) {	
		if(!GameName.player.getAccess().isGravityOn()) {
			return true;
		}
		
		float x = pos.getX(), y = pos.getY(), z = pos.getZ();
		
		if(
			(x == 0 && direction == LEFT) 	  || (x == w.getSizeX() && direction == RIGHT)   || 
			(y == 0 && direction == DOWN) 	  || (y == w.getSizeY() && direction == UP)      || 
			(z == 0 && direction == BACKWARD) || (z == w.getSizeZ() && direction == FORWARD) 
		) return false;
		
		if(direction == FORWARD) return w.getCube(x, y, z + 1).isSolid();
		if(direction == BACKWARD) return w.getCube(x, y, z - 1).isSolid();
		if(direction == RIGHT) return w.getCube(x + 1, y, z).isSolid();
		if(direction == LEFT) return w.getCube(x - 1, y, z).isSolid();
		if(direction == UP) return w.getCube(x, y + 1, z).isSolid();
		if(direction == DOWN) return w.getCube(x, y - 1, z).isSolid(); 
	
		return true;
	}
	
	public static Vector3f getLookPosition(Vector3f pos, float rotX, float rotY, float rotZ, World w, int maxDistance) {	
		Vector3f change = new Vector3f(
				(float) Math.cos(Math.toRadians(rotY + 90)),
				(float) 5,
				(float) Math.sin(Math.toRadians(rotY + 90))
			);
		
		
		int checkDistance = 0;
		while(checkDistance < maxDistance && !w.getCube((pos = pos.add(change))).isSolid()) {checkDistance ++;}				
		
		if(checkDistance >= maxDistance) return null;
		
		return pos;
	}
	
	public void simulate(ArrayList<PhysicsObject> objects, float delta) {
		for(PhysicsObject obj : objects) {
			obj.intergrate(delta);
		}
	}
	
	public ArrayList<Collision> detectCollisions(ArrayList<PhysicsObject> objects) {		
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		
		for(int i = 0; i < objects.size(); i ++) {
			for(int j = i + 1; j < objects.size(); j ++) {
				
				CollisionEvent event = objects.get(i).getAccess().getBoundingArea().intersect(
									   objects.get(j).getAccess().getBoundingArea());
				
				if(event.isColliding()) {
					collisions.add(new Collision(event, i, j));
				}
			}	
		}
		
		return collisions;
	}
	
	public void handelCollisions(ArrayList<PhysicsObject> objects, ArrayList<Collision> collisions) {
		for(Collision collision : collisions) {
			if(!collision.event.isColliding()) continue;
			
			Vector3f direction = collision.event.getDirection().normalized();
			Vector3f otherDirection = direction.reflect(objects.get(collision.object1).getAccess().getVel().normalized());
			
			objects.get(collision.object1).getAccess().getVel().multiplyE(otherDirection);
			objects.get(collision.object2).getAccess().getVel().multiplyE(direction);
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
}
