package com.GameName.Physics;

import java.util.ArrayList;

import com.GameName.Main.GameName;
import com.GameName.Physics.Coalition.CollisionEvent;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vectors.Vector3f;
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
//		
//		float x = pos.getX(), y = pos.getY(), z = pos.getZ();
//		
//		if(
//			(x == 0 && direction == LEFT) 	  || (x == w.getSizeX() && direction == RIGHT)   || 
//			(y == 0 && direction == DOWN) 	  || (y == w.getSizeY() && direction == UP)      || 
//			(z == 0 && direction == BACKWARD) || (z == w.getSizeZ() && direction == FORWARD) 
//		) return false;
//		
//		if(direction == FORWARD) return w.getCube(x, y, z + 1).isSolid();
//		if(direction == BACKWARD) return w.getCube(x, y, z - 1).isSolid();
//		if(direction == RIGHT) return w.getCube(x + 1, y, z).isSolid();
//		if(direction == LEFT) return w.getCube(x - 1, y, z).isSolid();
//		if(direction == UP) return w.getCube(x, y + 1, z).isSolid();
//		if(direction == DOWN) return w.getCube(x, y - 1, z).isSolid(); 
	
		return true;
	}
	
	public static Vector3f getLookPosition(Vector3f pos, Vector3f rot, World w, int maxDistance) {	
		float f1 = (float)  Math.cos(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f2 = (float)  Math.sin(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f3 = (float) -Math.cos(-Math.toRadians(rot.getX()));
		float f4 = (float)  Math.sin(-Math.toRadians(rot.getX()));
		
		Vector3f change = new Vector3f((f2 * f3), f4, (f1 * f3));	
		Vector3f check = pos.clone();
		
		int checkDistance = 0;
		
		try {
			while(checkDistance < maxDistance && !w.getCube(check.addE(change)).isSolid()) {checkDistance ++; }			
		} catch(IndexOutOfBoundsException | NullPointerException e) {return pos;}
		
		if(checkDistance >= maxDistance) return pos;
		
		check = check.truncate();
		return check;
	}
	
	public void simulate(ArrayList<PhysicsObject> objects, float delta) {
		for(PhysicsObject obj : objects) {
			if(obj == null) continue;
			
			obj.intergrate(delta);
		}
	}
	
	public ArrayList<Collision> detectCollisions(ArrayList<PhysicsObject> objects) {		
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
