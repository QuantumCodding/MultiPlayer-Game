package com.GameName.Physics.Object;

import com.GameName.Engine.GameEngine;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.Engine.ResourceManager.Threads;
import com.GameName.Physics.PhysicsEngine;
import com.GameName.Physics.PhysicsUtil.CardinalDirection;
import com.GameName.Physics.PhysicsUtil.Collision;
import com.GameName.Physics.Collision.BoundingArea;
import com.GameName.Util.Vectors.MathVec3f;
import com.GameName.World.World;

public abstract class PhysicsObject {
	protected final GameEngine ENGINE;
	
	protected MathVec3f pos, vel, acc, force;	
	protected MathVec3f rot, rotVel, rotAcc, rotForce;
	protected Material material, median, surface;
	
	protected BoundingArea bounding;	
	private PhysicsAccess access;

	protected boolean onGround, noClip;
	
	public PhysicsObject(GameEngine eng) {
		ENGINE = eng;
		
		this.material = Materials.Stone;
		this.median = Materials.Air;
		this.surface = Materials.Stone;
		
		pos = new MathVec3f(0, 0, 0);     
		vel = new MathVec3f(0, 0, 0);     
		acc = new MathVec3f(0, 0, 0);     
		force = new MathVec3f(0, 0, 0);   
		                                 
		rot = new MathVec3f(0, 0, 0);     
		rotVel = new MathVec3f(0, 0, 0);  
		rotAcc = new MathVec3f(0, 0, 0);  
		rotForce = new MathVec3f(0, 0, 0);
		
		bounding = new BoundingArea();
		addBounding();
	}
	
	public void reset() {
		pos.reset();
		vel.reset();
		acc.reset();
		force.reset();

		rot.reset();
		rotVel.reset();
		rotAcc.reset();
		rotForce.reset();
	}
	
	public void update() {
		rot.modAndSet(360);
	}
	
	protected abstract void addBounding();
	
	public MathVec3f getLook() {
		float f1 = (float)  Math.cos(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f2 = (float)  Math.sin(-Math.toRadians(rot.getY()) - (float) Math.PI);
		float f3 = (float) -Math.cos(-Math.toRadians(rot.getX()));
		float f4 = (float)  Math.sin(-Math.toRadians(rot.getX()));
		
		return new MathVec3f((f2 * f3), f4, (f1 * f3));
	}
	
	public void intergrate(PhysicsEngine physics, float delta) {

//		Applies Velocity
		MathVec3f lastAcc = acc.clone();
		pos = pos.add(vel.multiply(delta).add(lastAcc.multiply(0.5f * delta * delta)));		
		pos = pos.capMax(physics.getWorld().getSizeAsVector().subtract(1)).capMin(new MathVec3f(0, 6, 0));

//		Calculates world interaction values
		MathVec3f newAcc = physics.getAcceleration(force, physics.getMass(material, bounding.getVolume()));	
		
		if(!noClip) {
			surface = physics.getWorld().getCube(pos.getX(), physics.getWorld().getSizeY() - (pos.getY() - 5), pos.getZ())
				.getMaterial(physics.getWorld().getCubeMetadata(pos.getX(), physics.getWorld().getSizeY() - (pos.getY() - 5), pos.getZ()));			
			onGround = surface.getDensity() >= material.getDensity();
			
			if(!onGround) {
				newAcc.setY(newAcc.getY() - 0.5f);//PhysicsEngine.GRAVITY);
			} 
			
			MathVec3f chunkPos = pos.divide(World.CHUNK_SIZE).truncate();			
			if(bounding.intersect(physics.getWorld().getChunk(chunkPos).getBoundingArea()).isColliding() ||					
				bounding.intersect(physics.getWorld().getChunk(chunkPos.add(new MathVec3f(1, 0, 0))
						.capMax(physics.getWorld().getChunkSizeAsVector().subtract(1))).getBoundingArea()).isColliding() || 					
				bounding.intersect(physics.getWorld().getChunk(chunkPos.add(new MathVec3f(0, 0, 1))
						.capMax(physics.getWorld().getChunkSizeAsVector().subtract(1))).getBoundingArea()).isColliding() || 					
				bounding.intersect(physics.getWorld().getChunk(chunkPos.add(new MathVec3f(-1, 0, 0))
						.capMin(0)).getBoundingArea()).isColliding() ||					
				bounding.intersect(physics.getWorld().getChunk(chunkPos.add(new MathVec3f(0, 0, -1))
						.capMin(0)).getBoundingArea()).isColliding()
			) {
//				newAcc.reset(); vel.reset(); force.reset();
//				pos = pos.add(lastAcc.multiply(1000000).capMax(1).capMin(-1).multiply(-0.1f));
//				reset();
				System.out.println("colliding");
			}			
		}
		
//		Drag
		vel = vel.subtract(new MathVec3f(
				physics.getDrag(median, vel.getX(), bounding.getSurfaceArea(vel.getX() < 0 ? CardinalDirection.West : CardinalDirection.East)),
				physics.getDrag(median, vel.getY(), bounding.getSurfaceArea(vel.getY() < 0 ? CardinalDirection.Bottom : CardinalDirection.Top)),
				physics.getDrag(median, vel.getZ(), bounding.getSurfaceArea(vel.getZ() < 0 ? CardinalDirection.South : CardinalDirection.North))	
			));
		
//		Friction
		vel = vel.add(new MathVec3f(
				physics.getFriction(surface, force.getY()), 0.0f, 
				physics.getFriction(surface, force.getY())
			).multiply(new MathVec3f(
				physics.getOppsiteDir(vel.getX()), 1.0f,
				physics.getOppsiteDir(vel.getZ())				
			)));
		
//		Apply Acceleration to Velocity
		MathVec3f avgAcc = lastAcc.add(newAcc).divide(2);
		vel = vel.add(avgAcc.multiply(delta));
		vel = vel.divide(Threads.PhysicsThread.getTPS());
		
//		Cap values
//		if(vel.abs().lessThen(0.9f)) vel = vel.capMax(0);
//		if(acc.abs().lessThen(0.9f)) acc = acc.capMax(0);
//		
//		if(vel.abs().greaterThen(PhysicsEngine.MAX_VELOCITY)) 
//			 vel = vel.capMax(PhysicsEngine.MAX_VELOCITY).capMin(-PhysicsEngine.MAX_VELOCITY);		
//		if(acc.abs().greaterThen(PhysicsEngine.MAX_VELOCITY)) 
//			 acc = acc.capMax(PhysicsEngine.MAX_VELOCITY).capMin(-PhysicsEngine.MAX_VELOCITY);
		
		//Reset for next cycle
		acc = newAcc.clone(); 		
		force.reset();
	}
	
	public void handelCollision(Collision collision) {
		if(noClip) return;
	}
	
	protected void setMaterial(Material material) {
		this.material = material;
	}
	
	public PhysicsAccess getAccess() {
		return access;
	}
}
