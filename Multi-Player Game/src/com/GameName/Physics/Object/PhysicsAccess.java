package com.GameName.Physics.Object;

import com.GameName.Util.Vector3f;

public class PhysicsAccess {
	PhysicsObject object;
	
	public PhysicsAccess(PhysicsObject object) {
		this.object = object;
	}
	
	public Vector3f getPos() {
		return object.pos;
	}
	
	public Vector3f getRot() {
		return object.rot;
	}
	
	public Vector3f getVel() {
		return object.vel;
	}	

	public boolean isOnGorund() {
		return object.onGround;
	}

	public void setPos(Vector3f pos) {
		object.pos = pos;
	}

	public void setRot(Vector3f rot) {
		object.rot = rot;
	}

	public void setVel(Vector3f vel) {
		object.vel = vel;
	}
	
	public void setX(float x) {
		object.pos.setX(x);
	}
	
	public void setY(float y) {
		object.pos.setY(y);
	}
	
	public void setZ(float z) {
		object.pos.setZ(z);
	}
	
	public void setRotX(float rotX) {
		object.rot.setX(rotX);
	}
	
	public void setRotY(float rotY) {
		object.rot.setY(rotY);
	}
	
	public void setRotZ(float rotZ) {
		object.rot.setZ(rotZ);
	}
}
