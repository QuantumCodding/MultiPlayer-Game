package com.GameName.Physics.Object;

import com.GameName.Engine.GameEngine;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.Engine.ResourceManager.Threads;
import com.GameName.Physics.PhysicsEngine;
import com.GameName.Physics.PhysicsUtil;
import com.GameName.Physics.PhysicsUtil.CardinalDirection;
import com.GameName.Physics.Collision.BoundingArea;
import com.GameName.Util.Vectors.Vector3f;

public abstract class PhysicsObject {
	protected final GameEngine ENGINE;
	
	protected Vector3f pos, vel, acc, force;	
	protected Vector3f rot, rotVel, rotAcc, rotForce;
	protected Material material, median, surface;
	
	protected BoundingArea bounding;	
	private PhysicsAccess access;

	protected boolean onGround;
	
	public PhysicsObject(GameEngine eng) {
		ENGINE = eng;
		
		this.material = Materials.Stone;
		this.median = Materials.Air;
		this.surface = Materials.Stone;
		
		pos = new Vector3f(0, 0, 0);
		vel = new Vector3f(0, 0, 0);
		acc = new Vector3f(0, 0, 0);
		force = new Vector3f(0, 0, 0);

		rot = new Vector3f(0, 0, 0);
		rotVel = new Vector3f(0, 0, 0);
		rotAcc = new Vector3f(0, 0, 0);
		rotForce = new Vector3f(0, 0, 0);
		
		bounding = new BoundingArea();
		addBounding();
	}
	
	public void reset() {
		pos = new Vector3f(0, 0, 0);
		vel = new Vector3f(0, 0, 0);
		acc = new Vector3f(0, 0, 0);
		force = new Vector3f(0, 0, 0);

		rot = new Vector3f(0, 0, 0);
		rotVel = new Vector3f(0, 0, 0);
		rotAcc = new Vector3f(0, 0, 0);
		rotForce = new Vector3f(0, 0, 0);
	}
	
	public void update() {
		rot.modAndSet(360);
	}
	
	protected abstract void addBounding();
	
	public Vector3f getLook() {
		float f1 = (float)  Math.cos(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f2 = (float)  Math.sin(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f3 = (float) -Math.cos(-Math.toRadians(rot.getX()));
		float f4 = (float)  Math.sin(-Math.toRadians(rot.getX()));
		
		return new Vector3f((f2 * f3), f4, (f1 * f3));
	}
	
	public void intergrate(PhysicsEngine physics, float delta) {
//		System.out.println(force.truncate());
		Vector3f lastAcc = acc.clone();
		pos = pos.add(vel.multiply(delta).add(lastAcc.multiply(0.5f * delta * delta)));

		Vector3f newAcc = physics.getAcceleration(force, physics.getMass(material, bounding.getVolume()));	
//		onGround = physics.getWorld().getCube(pos.getX(), pos.getY() - 1, pos.getZ())
//				.getMaterial(physics.getWorld().getCubeMetadata(pos.getX(), pos.getY() - 1, pos.getZ()))
//				.getDensity() >= material.getDensity();
//		
//		if(!onGround) {
//			newAcc.setY(newAcc.getY() - PhysicsEngine.GRAVITY);
//		}
		
//		System.out.print(vel.valuesToString());
		
		vel = vel.subtract(new Vector3f(
				physics.getDrag(median, vel.getX(), bounding.getSurfaceArea(vel.getX() < 0 ? CardinalDirection.West : CardinalDirection.East)),
				physics.getDrag(median, vel.getY(), bounding.getSurfaceArea(vel.getY() < 0 ? CardinalDirection.Bottom : CardinalDirection.Top)),
				physics.getDrag(median, vel.getZ(), bounding.getSurfaceArea(vel.getZ() < 0 ? CardinalDirection.South : CardinalDirection.North))	
			));
		
//		System.out.print(newAcc.valuesToString());
		
		vel = vel.add(new Vector3f(
				physics.getFriction(surface, PhysicsEngine.GRAVITY), 0.0f, 
				physics.getFriction(surface, PhysicsEngine.GRAVITY)
			).multiply(new Vector3f(
				physics.getOppsiteDir(vel.getX()), 0.0f,
				physics.getOppsiteDir(vel.getZ())				
			))); // force.getY()
		
		Vector3f avgAcc = lastAcc.add(newAcc).divide(2);
		vel = vel.add(avgAcc.multiply(delta));
		vel = vel.divide(Threads.PhysicsThread.getTPS());
		
		if(vel.abs().lessThen(0.9f)) vel = vel.capMax(0);
		if(acc.abs().lessThen(0.9f)) acc = acc.capMax(0);
		
		if(vel.abs().greaterThen(PhysicsEngine.MAX_VELOCITY)) 
			 vel = vel.capMax(PhysicsEngine.MAX_VELOCITY).capMin(-PhysicsEngine.MAX_VELOCITY);		
		if(acc.abs().greaterThen(PhysicsEngine.MAX_VELOCITY)) 
			 acc = acc.capMax(PhysicsEngine.MAX_VELOCITY).capMin(-PhysicsEngine.MAX_VELOCITY);
		
		acc = newAcc.clone(); 		
		force.setX(0).setY(0).setZ(0);
	}
	
	public void handelCollision(PhysicsUtil.Collision collision) {
		
	}
	
	protected void setMaterial(Material material) {
		this.material = material;
	}
	
	public PhysicsAccess getAccess() {
		return access;
	}
}
