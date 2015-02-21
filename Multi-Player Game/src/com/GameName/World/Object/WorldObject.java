package com.GameName.World.Object;

import com.GameName.Physics.Material;
import com.GameName.Physics.PhysicsObject;
import com.GameName.Physics.Collision.CollisionShape;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public abstract class WorldObject extends PhysicsObject {

	private Vector3f chunk;
	private Vector3f renderPos, adjust;
	
	public WorldObject(CollisionShape shape, Material material) {
		super(shape, material);
		
		chunk = new Vector3f(0, 0, 0);
		renderPos = new Vector3f(0, 0, 0);
		adjust = new Vector3f(
				(World.SCALE / 10f),
				(World.SCALE / 10f),
				(World.SCALE / 10f)
			);
	}
	
	public void update() {
		
		renderPos = getPosition().multiply(adjust);		
		chunk = getPosition().divide(World.CHUNK_SIZE).truncate();
		
		if(chunk.x > getCurrentWorld().getChunkX() - 1) chunk.x = getCurrentWorld().getChunkX() - 1; 
		else if(chunk.x < 0) chunk.x = 0;			
		if(chunk.y > getCurrentWorld().getChunkY() - 1) chunk.y = getCurrentWorld().getChunkY() - 1; 
		else if(chunk.y < 0) chunk.y = 0;		
		if(chunk.z > getCurrentWorld().getChunkZ() - 1) chunk.z = getCurrentWorld().getChunkZ() - 1; 
		else if(chunk.z < 0) chunk.z = 0;
	}
	
	public Vector3f getChunk() { return chunk; }
	public Vector3f getRenderPos() { return renderPos; }
}