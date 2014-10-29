package com.GameName.Physics;

import com.GameName.Main.GameName;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class PhysicsUtil {


	public static final float GRAVITY = 0.01f;
	public static final float TERMINAL_VELOCITY = -4.0f;
	
	public static final int FORWARD = 0, BACKWARD = 1;
	public static final int LEFT 	= 2, RIGHT 	  = 3;
	public static final int UP 		= 4, DOWN 	  = 5;
	
	public static final int NORTH	= 0, SOUTH	= 2;
	public static final int EAST	= 3, WEST	= 1;	
	
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
		Vector3f check = pos.clone().setY(w.getSizeY() - pos.getY());
		
		int checkDistance = 0;
		
		try {
			while(checkDistance < maxDistance && !w.getCube(check.addAndSet(change)).isSolid(w.getCubeMetadata(check))) {checkDistance ++; }			
		} catch(IndexOutOfBoundsException | NullPointerException e) {return pos;}
		
		if(checkDistance >= maxDistance) return pos;
		
		check = check.truncate();
		return check;
	}
	
	public static Vector3f rayTrace(Vector3f pos1, Vector3f pos2, World w) {
		pos1.roundAndSet(); pos2.roundAndSet();
		
		float slopeY = (pos2.getY() - pos1.getY()) / (pos2.getX() - pos1.getX());
		float slopeZ = (pos2.getZ() - pos1.getZ()) / (pos2.getX() - pos1.getX());
		
		Vector3f step = new Vector3f(1, slopeY, slopeZ);
		Vector3f check = pos1.clone();
		
		if(!pos1.add(step.multiply(pos2.getX()- pos1.getX())).equals(pos2)) {
			System.out.println((pos2.getX() - pos1.getX()) + " " + (pos2.getY() - pos1.getY()) + " " + (pos2.getZ() - pos1.getZ()));
			System.out.println(pos1 + " " + pos2);
			System.out.println(step + " " + step.multiply(pos2.getX()));
			
			throw new IndexOutOfBoundsException("Slope is wrong");
		}
		
		while(!w.getCube(check).isSolid(w.getCubeMetadata(check)) && Math.abs(check.getX()) < Math.abs(pos2.getX())) {
			check.addAndSet(step).roundAndSet();
		}
		
		if (Math.abs(check.getX()) >= Math.abs(pos2.getX())) return null;
		return check;
	}
	
	
	
	public static int getDirection(Vector3f rot) {
		int compasDirX = getCompasDirection(rot.getX());
		int compasDirY = getCompasDirection(rot.getY());
		
		return compasDirX == WEST ? DOWN : compasDirX == EAST ? UP :
				compasToDirection(compasDirY);
	}
	
	public static int getCompasDirection(float rotY) {
		return (int) (Math.floor((rotY * 4f) / 360f) + 0.5f) & 3;
	}
	
	public static int compasToDirection(int compasDirection) {
		if(compasDirection == NORTH) return FORWARD;
		if(compasDirection == SOUTH) return BACKWARD;
		if(compasDirection == EAST) return RIGHT;
		if(compasDirection == WEST) return LEFT;
		
		return -1;
	}
	
	public static int directionToCompas(int direction) {
		if(direction == FORWARD) return NORTH;
		if(direction == BACKWARD) return SOUTH;
		if(direction == RIGHT) return EAST;
		if(direction == LEFT) return WEST;
		
		return -1;
	}
	
	public static Vector3f getPosNextToFace(int facingDir) {
		int dir = getOppositDirection(facingDir);
		
		if(dir == FORWARD) 	return new Vector3f(0, 0, -1);
		if(dir == BACKWARD) return new Vector3f(0, 0,  1);
		
		if(dir == RIGHT) 	return new Vector3f(-1, 0, 0);
		if(dir == LEFT)		return new Vector3f( 1, 0, 0);
		
		if(dir == UP) 		return new Vector3f(0,  1, 0);
		if(dir == DOWN) 	return new Vector3f(0, -1, 0);
		
		return null;		
	}
	
	public static int getOppositDirection(int dir) {
		return dir % 2 == 0 ? dir + 1 : dir - 1;
	}	
}
