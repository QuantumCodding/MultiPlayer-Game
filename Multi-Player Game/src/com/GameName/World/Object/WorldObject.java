package com.GameName.World.Object;

import javax.vecmath.Vector3f;

import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vectors.MathVec3f;
import com.GameName.World.World;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

public abstract class WorldObject extends PhysicsObject {

	private MathVec3f chunk;
	private MathVec3f renderPos, adjust;
	private MathVec3f rotation;
	private World currentWorld;
	
	public WorldObject(RigidBodyConstructionInfo info) {
		super(info);
		
		chunk = new MathVec3f(0, 0, 0);
		renderPos = new MathVec3f(0, 0, 0);
		rotation = new MathVec3f(0, 0, 0);
		adjust = new MathVec3f(
				(World.SCALE / 10f),
				(World.SCALE / 10f),
				(World.SCALE / 10f)
			);
	}
	
	public void update() {
		super.update();
		
		renderPos = getPos().multiply(adjust);		
		chunk = getPos().divide(World.CHUNK_SIZE).truncate();
		
		if(chunk.x > currentWorld.getChunkX() - 1) chunk.x = currentWorld.getChunkX() - 1; 
		else if(chunk.x < 0) chunk.x = 0;			
		if(chunk.y > currentWorld.getChunkY() - 1) chunk.y = currentWorld.getChunkY() - 1; 
		else if(chunk.y < 0) chunk.y = 0;		
		if(chunk.z > currentWorld.getChunkZ() - 1) chunk.z = currentWorld.getChunkZ() - 1; 
		else if(chunk.z < 0) chunk.z = 0;
	}
	
	public void applyRotation(Vector3f torque) {
		applyRotation(MathVec3f.convert(torque));}
	public void applyRotation(MathVec3f torque) {
		rotation = rotation.add(torque);
		rotation = rotation.mod(360);
	}
	
	public void setRotation(Vector3f rotation) {
		setRotation(MathVec3f.convert(rotation));}
	public void setRotation(MathVec3f rotation) {
		this.rotation.set(rotation);
	}
	
	public MathVec3f getRot() {
		return rotation;
	}

	public MathVec3f getChunk() { return chunk; }
	public MathVec3f getRenderPos() { return renderPos; }
	public World getCurrentWorld() { return currentWorld; }
	public void setCurrentWorld(World currentWorld) {
		this.currentWorld = currentWorld;
	}
}
