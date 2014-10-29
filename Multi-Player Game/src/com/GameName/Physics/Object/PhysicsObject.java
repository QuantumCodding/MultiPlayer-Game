package com.GameName.Physics.Object;

import com.GameName.Physics.PhysicsUtil;
import com.GameName.Physics.Coalition.BoundingArea;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public abstract class PhysicsObject {
	protected Vector3f pos;
	protected Vector3f rot;
	
	protected Vector3f vel;
	protected Vector3f rotVel;

	protected BoundingArea bounding;
	
	private PhysicsAccess access;

	protected boolean onGround;
	
	public PhysicsObject() {
		pos = new Vector3f(0, 0, 0);
		rot = new Vector3f(0, 0, 0);
		vel = new Vector3f(0, 0, 0);
		
		bounding = new BoundingArea();
		addBounding();
	}
	
	public void updata() {
		rot.modAndSet(360);
	}
	
	protected abstract void addBounding();
	
	protected void applyGravity(World w) {
		float newVel = 0.0f;
		float groundHeight = w.getGroundHeight(pos);
		
		onGround = pos.getY() <= groundHeight;		
		if(onGround) { pos.setY(groundHeight); vel.setY(newVel); return;} 
		
		newVel = vel.getY() - PhysicsUtil.GRAVITY;		
		if(newVel < PhysicsUtil.TERMINAL_VELOCITY) newVel = PhysicsUtil.TERMINAL_VELOCITY;
		
		vel.setY(newVel);
	}
	
	public Vector3f getLook() {
		float f1 = (float)  Math.cos(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f2 = (float)  Math.sin(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f3 = (float) -Math.cos(-Math.toRadians(rot.getX()));
		float f4 = (float)  Math.sin(-Math.toRadians(rot.getX()));
		
		return new Vector3f((f2 * f3), f4, (f1 * f3));
	}
	
	public void intergrate(float delta) {
		pos.addAndSet(vel.multiply(delta));
		rot.addAndSet(rotVel.multiply(delta));
	}
	
	public PhysicsAccess getAccess() {
		return access;
	}
}
