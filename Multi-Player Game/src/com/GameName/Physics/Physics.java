package com.GameName.Physics;

import com.GameName.Main.GameName;
import com.GameName.Util.Vector3f;
import com.GameName.World.World;

public class Physics {
	
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
	
	public static float applyGravity(float x, float y, float z, float vel, World w, float groundHeight) {
		float newVel = 0.0f;
				
		if(y <= groundHeight) return newVel; 
		
		newVel = vel - GRAVITY;		
		if(newVel < TERMINAL_VELOCITY) newVel = TERMINAL_VELOCITY;
		
		return newVel;
	}
	
	public static Vector3f getLookPosition(float x, float y, float z, float rotX, float rotY, float rotZ, World w, int maxDistance) {
		Vector3f pos = new Vector3f(x, y, z);		
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
}
