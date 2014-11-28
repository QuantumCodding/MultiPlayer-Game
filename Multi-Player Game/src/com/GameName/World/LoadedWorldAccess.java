package com.GameName.World;

import com.GameName.Cube.Cube;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Object.WorldObject;

public class LoadedWorldAccess {
	LoadedWorld world;
	
	protected LoadedWorldAccess(LoadedWorld world) {
		this.world = world;
	}
	
	public Cube getCube(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;
				
		Chunk chunk = getChunk(chunkCoordX, chunkCoordY, chunkCoordZ); if(chunk == null) return null; //TODO: Temporary?
		return Cube.getCubeByID(chunk.getCube(indexX, indexY, indexZ));
	}
	
	public Cube getCube(Vector3f cord) {
		return getCube(cord.getX(), cord.getY(), cord.getZ());
	}
	
	public int getMetadata(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;
				
		Chunk chunk = getChunk(chunkCoordX, chunkCoordY, chunkCoordZ); if(chunk == null) return 0; //TODO: Temporary?
		return chunk.getMetadata(indexX, indexY, indexZ);
	}
	
	public int getMetadata(Vector3f pos) {
		return getMetadata(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public Chunk getChunk(int x, int y, int z) {
		return getChunk(new Vector3f(x, y, z));
	}
	
	public Chunk getChunk(Vector3f pos) {
		return world.chunkLoader.getChunk(pos);
	}
	
	public WorldObject getObject(int index) {
		return world.objects[index];
	}
		
	public WorldObject[] getObjects() {
		return world.objects;
	}
	
	public float getGroundHeight(float x, float y, float z) {
		int groundHeight = Math.min(Math.round(y), world.world.getSizeY() - 1);
		
		while(groundHeight > 0 && !getCube(x, groundHeight, z).isSolid(getMetadata((int) x, groundHeight, (int) z))) {
			groundHeight --; 
		}
		
		return groundHeight;
	}
	
	public float getGroundHeight(Vector3f pos) {
		return getGroundHeight(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public float[] getLightColor(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;

		Chunk chunk = getChunk(chunkCoordX, chunkCoordY, chunkCoordZ); if(chunk == null) return null; //TODO: Temporary?
		return chunk.getLightColor(indexX, indexY, indexZ);
	}
	
	public float getLightValue(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;

		Chunk chunk = getChunk(chunkCoordX, chunkCoordY, chunkCoordZ); if(chunk == null) return 0; //TODO: Temporary?
		return chunk.getLightValue(indexX, indexY, indexZ);
	}
	
	public boolean checkChunk(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;
		
		if(ix < 0 || iy < 0 || iz < 0) return false;
		if(chunkCoordX > world.world.getChunkX() || chunkCoordY > world.world.getChunkY() || chunkCoordZ > world.world.getChunkZ()) return false;
		
		Chunk chunk = getChunk(chunkCoordX, chunkCoordY, chunkCoordZ); if(chunk == null) return false;
		int cube = chunk.getCube(indexX, indexY, indexZ); if(cube == -1) return false;
		
		return true;
	}
	
	public boolean checkChunk(Vector3f pos) {
		return checkChunk(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public Cube[] getSurroundingCubes(float x, float y, float z) {
		Cube[] cubes = new Cube[6];
		
		Vector3f minPos = getCenter().subtract(getRenderRadius()).multiply(World.CHUNK_SIZE).capMin(0);		
		Vector3f maxPos = getCenter().add(getRenderRadius()).multiply(World.CHUNK_SIZE)
				.capMax(world.world.getSizeAsVector()).subtract(1);
			
		try {
			if(x > minPos.getX()) cubes[0] = getCube(x - 1, y, z); // 0 -x			1				z         
			if(z < maxPos.getZ()) cubes[1] = getCube(x, y, z + 1); // 1 +z		0	C	2		-x	c	x     
			if(x < maxPos.getX()) cubes[2] = getCube(x + 1, y, z); // 2 +x			3			   -z         
			if(z > minPos.getZ()) cubes[3] = getCube(x, y, z - 1); // 3 -z					4				+y
			if(y < maxPos.getY()) cubes[4] = getCube(x, y + 1, z); // 4 +y					C				 c
			if(y > minPos.getY()) cubes[5] = getCube(x, y - 1, z); // 5 -y					5				-y
		} catch(NullPointerException e) {
			System.err.println(new Vector3f(x, y, z));
			
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		}
		return cubes;	
	}
	
	public Cube[] getSurroundingCubes(Vector3f pos) {
		return getSurroundingCubes(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public ChunkLoader getChunkLoaded() {
		return world.chunkLoader;
	}
	
	public Vector3f getCenter() {
		return world.chunkLoader.getCenter();
	}
	
	public static int getRenderRadius() {
		return LoadedWorld.loadRadius;
	}
	
	public void setCenter(Vector3f center) {
		world.center = center;
		getChunkLoaded().setCenter(center);
	}
		
	public void setCube(float x, float y, float z, int cubeId) {		
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;

		getChunk(chunkCoordX, chunkCoordY, chunkCoordZ).setCube(indexX, indexY, indexZ, Cube.getCubeByID(cubeId));
	}
	
	public void setCube(Vector3f pos, int cubeId) {
		setCube(pos.getX(), pos.getY(), pos.getZ(), cubeId);
	}
	
	public void setMetadata(float x, float y, float z, int metadata) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;

		getChunk(chunkCoordX, chunkCoordY, chunkCoordZ).setMetadata(indexX, indexY, indexZ, metadata);
	}
	
	public void setMetadata(Vector3f pos, int metadata) {
		setMetadata(pos.getX(), pos.getY(), pos.getZ(), metadata);
	}
	
	public void setCubeWithMetadata(float x, float y, float z, int cube, int metadata) {
		int ix = (int) x, chunkCoordX = ix / World.CHUNK_SIZE, indexX = ix % World.CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / World.CHUNK_SIZE, indexY = iy % World.CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / World.CHUNK_SIZE, indexZ = iz % World.CHUNK_SIZE;

		getChunk(chunkCoordX, chunkCoordY, chunkCoordZ).setCubeWithMetadata(indexX, indexY, indexZ, Cube.getCubeByID(cube), metadata);
	}

	public void setCubeWithMetadata(Vector3f pos, int cube, int metadata) {
		setCubeWithMetadata(pos.getX(), pos.getY(), pos.getZ(), cube, metadata);
	}
}
