package com.GameName.World.Object;

import com.GameName.Engine.GameEngine;
import com.GameName.Physics.Collision.BoundingBox;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vectors.MathVec3f;
import com.GameName.World.World;

public abstract class WorldObject extends PhysicsObject {

	protected MathVec3f chunk;
	protected MathVec3f renderPos, adjust;	
	protected float height, width, length;
	protected World currentWorld;
	
	WorldObjectAccess access;
	
	public WorldObject(GameEngine eng) {
		super(eng);
		
		height = 1;	width = 1; length = 1;		
		renderPos = new MathVec3f(0, 0, 0);
	}
	
	public void update() {
		super.update();
		
		renderPos = pos.multiply(adjust);
		
		MathVec3f tempChunk = pos.divide(World.CHUNK_SIZE).truncate();
		
		if(tempChunk.getX() > currentWorld.getChunkX() - 1) tempChunk.setX(currentWorld.getChunkX() - 1); 
		else if(tempChunk.getX() < 0) tempChunk.setX(0);			
		if(tempChunk.getY() > currentWorld.getChunkY() - 1) tempChunk.setY(currentWorld.getChunkY() - 1); 
		else if(tempChunk.getY() < 0) tempChunk.setY(0);		
		if(tempChunk.getZ() > currentWorld.getChunkZ() - 1) tempChunk.setZ(currentWorld.getChunkZ() - 1); 
		else if(tempChunk.getZ() < 0) tempChunk.setZ(0);
		
		chunk = tempChunk.clone();
	}
	
	public void addBounding() {
		bounding.add(new BoundingBox(
				new MathVec3f(0, 0, 0), new MathVec3f(1, 1, 1)
//				new Vector3f(pos.getX() - (width / 2f), pos.getY() - (height / 2f), pos.getZ() - (length / 2f)), 
//				new Vector3f(pos.getX() + (width / 2f), pos.getY() + (height / 2f), pos.getZ() + (length / 2f))
			));
	}

	public WorldObjectAccess getAccess() {
		return access;
	}
}
