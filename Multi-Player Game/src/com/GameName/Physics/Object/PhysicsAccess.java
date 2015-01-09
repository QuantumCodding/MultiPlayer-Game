package com.GameName.Physics.Object;

import com.GameName.Engine.GameEngine;
import com.GameName.Physics.Collision.BoundingArea;
import com.GameName.Util.Vectors.MathVec3f;

public class PhysicsAccess {
	protected final GameEngine ENGINE;
	PhysicsObject object;
	
	public PhysicsAccess(GameEngine eng, PhysicsObject object) {
		ENGINE = eng;
		this.object = object;
	}

	public MathVec3f getPos() {
		return object.pos;
	}

	public MathVec3f getVel() {
		return object.vel;
	}

	public MathVec3f getAcc() {
		return object.acc;
	}

	public MathVec3f getForce() {
		return object.force;
	}

	public MathVec3f getRot() {
		return object.rot;
	}

	public MathVec3f getRotVel() {
		return object.rotVel;
	}

	public MathVec3f getRotAcc() {
		return object.rotAcc;
	}

	public MathVec3f getRotForce() {
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
	
	public boolean noClip() {
		return object.noClip;
	}

	public void setPos(MathVec3f pos) {
		object.pos = pos;
	}

	public void setVel(MathVec3f vel) {
		object.vel = vel;
	}

	public void setAcc(MathVec3f acc) {
		object.acc = acc;
	}

	public void setForce(MathVec3f force) {
		object.force = force;
	}

	public void setRot(MathVec3f rot) {
		object.rot = rot;
	}

	public void setRotVel(MathVec3f rotVel) {
		object.rotVel = rotVel;
	}

	public void setRotAcc(MathVec3f rotAcc) {
		object.rotAcc = rotAcc;
	}

	public void setRotForce(MathVec3f rotForce) {
		object.rotForce = rotForce;
	}

	public void setMedian(Material median) {
		object.median = median;
	}

	public void setBounding(BoundingArea bounding) {
		object.bounding = bounding;
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
	
	public void setNoClip(boolean noClip) {
		object.noClip = noClip;
	}
}
