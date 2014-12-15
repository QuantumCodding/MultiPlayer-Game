package com.GameName.Physics.Object;

import com.GameName.Engine.GameEngine;
import com.GameName.Physics.Collision.BoundingArea;
import com.GameName.Util.Vectors.Vector3f;

public class PhysicsAccess {
	protected final GameEngine ENGINE;
	PhysicsObject object;
	
	public PhysicsAccess(GameEngine eng, PhysicsObject object) {
		ENGINE = eng;
		this.object = object;
	}

	public Vector3f getPos() {
		return object.pos;
	}

	public Vector3f getVel() {
		return object.vel;
	}

	public Vector3f getAcc() {
		return object.acc;
	}

	public Vector3f getForce() {
		return object.force;
	}

	public Vector3f getRot() {
		return object.rot;
	}

	public Vector3f getRotVel() {
		return object.rotVel;
	}

	public Vector3f getRotAcc() {
		return object.rotAcc;
	}

	public Vector3f getRotForce() {
		return object.rotForce;
	}

	public Material getMaterial() {
		return object.material;
	}

	public Material getMedian() {
		return object.median;
	}

	public BoundingArea getBounding() {
		return object.bounding;
	}

	public boolean isOnGround() {
		return object.onGround;
	}

	public void setPos(Vector3f pos) {
		object.pos = pos;
	}

	public void setVel(Vector3f vel) {
		object.vel = vel;
	}

	public void setAcc(Vector3f acc) {
		object.acc = acc;
	}

	public void setForce(Vector3f force) {
		object.force = force;
	}

	public void setRot(Vector3f rot) {
		object.rot = rot;
	}

	public void setRotVel(Vector3f rotVel) {
		object.rotVel = rotVel;
	}

	public void setRotAcc(Vector3f rotAcc) {
		object.rotAcc = rotAcc;
	}

	public void setRotForce(Vector3f rotForce) {
		object.rotForce = rotForce;
	}

	public void setMedian(Material median) {
		object.median = median;
	}

	public void setBounding(BoundingArea bounding) {
		object.bounding = bounding;
	}

	public void setOnGround(boolean onGround) {
		object.onGround = onGround;
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
