package com.GameName.Physics.Object;

import com.GameName.Physics.Physics;
import com.GameName.Util.Vector3f;
import com.GameName.World.World;

public abstract class PhysicsObject {
	protected Vector3f pos;
	protected Vector3f rot;
	protected Vector3f vel;

	private PhysicsAccess access;

	protected boolean onGround;
	
	public PhysicsObject() {
		pos = new Vector3f(0, 0, 0);
		rot = new Vector3f(0, 0, 0);
		vel = new Vector3f(0, 0, 0);
	}
	
	protected void applyGravity(World w) {
		float newVel = 0.0f;
		float groundHeight = w.getGroundHeight(pos);
		
		onGround = pos.getY() <= groundHeight;		
		if(onGround) { pos.setY(groundHeight); vel.setY(newVel); return;} 
		
		newVel = vel.getY() - Physics.GRAVITY;		
		if(newVel < Physics.TERMINAL_VELOCITY) newVel = Physics.TERMINAL_VELOCITY;
	}
	
	public PhysicsAccess getAccess() {
		return access;
	}
}
