package com.GameName.World;

import java.util.ArrayList;

import com.GameName.Cube.Cube;
import com.GameName.Main.GameName;
import com.GameName.Util.Tag.Tag;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Render.ChunkRender;

public class Chunk {
	private final float MINIMUM_LIGHT_DEFUTION = 0.5f;
	
	private boolean isInitialized;
	private boolean hasCubes, isLoaded;
	private int cubeCount;
	
	private final int worldId;
	private final int x, y, z;
	
	private ChunkRender render;
	
	private int[] cubes;
	private int[] cubeMetadata;
	private final int size;
	
	private float[][] lightColorMap;
	private float[]   lightValueMap;
	private float 	  ambiantLight = (float) ((1d / (double) World.MAX_LIGHT) * World.AMBIANT_LIGHT);
	
//	private boolean vboUpdataRequested, extraVBOUpdate;
	
	public Chunk(int size, int worldId, int x, int y, int z) { 
		this.worldId = worldId;
		
		this.x = x; this.y = y; this.z = z;
		
		this.size = size;
		cubes = new int[size * size * size];
		cubeMetadata = new int[size * size * size];
		
		lightColorMap = new float[size * size * size][3];
		lightValueMap = new float[size * size * size];
		
		for(int i = 0; i < size * size * size; i ++) {
			lightColorMap[i] = new float[] {1.0f, 1.0f, 1.0f};
			lightValueMap[i] = ambiantLight;
		}
		
		render = new ChunkRender(this);
		isInitialized = true;
	}
	
	public void update() {
//		if(extraVBOUpdate) {
//			vboUpdataRequested = true;
//			extraVBOUpdate = false;
//		}
	}
	
	public void randomUpdate() {
		
	}
	
	public void updateLightMap(int x, int y, int z, float[] color, float intensity) {
		if(x < 0 || y < 0 || z < 0 || x == size || y == size || z == size) {
			Chunk chunk = WorldRegistry.getWorld(worldId)
				.getChunk(
						this.x + (x < 0 ? -1 : x == size ? 1 : 0), //x=5 size=3 boolean ? true : false
						this.y + (y < 0 ? -1 : y == size ? 1 : 0),
						this.z + (z < 0 ? -1 : z == size ? 1 : 0));
			
			if(chunk == null) return;			
			
			chunk.updateLightMap(
						(x < 0 ? size - 1 : x == size ? 0 : x), 
						(y < 0 ? size - 1 : y == size ? 0 : y),
						(z < 0 ? size - 1 : z == size ? 0 : z),
						color, intensity);
			return;
		}

		float lastIntensity = lightValueMap[x + (y * size) + (z * size * size)];
		float[] lastColor = lightColorMap[x + (y * size) + (z * size * size)];	
		
		float[] newColor = {
				(lastColor[0] * lastIntensity) + (color[0] * intensity), 
				(lastColor[1] * lastIntensity) + (color[1] * intensity),
				(lastColor[2] * lastIntensity) + (color[2] * intensity)
			};
		
		float newIntensity = Math.min(intensity + lastIntensity, 1);
		
		lightColorMap[x + (y * size) + (z * size * size)] = newColor;
		lightValueMap[x + (y * size) + (z * size * size)] = newIntensity;
		
		intensity -= ((1 / intensity) * Cube.getCubeByID(getCube(x, y, z)).getOpacity(getCubeMetadata(x, y, z))) + MINIMUM_LIGHT_DEFUTION;
		
		if(intensity <= ambiantLight){
			render.forceVBOUpdate(); return; 
		}
		
		updateLightMap(x - 1, y, z, color, intensity);
		updateLightMap(x + 1, y, z, color, intensity);
		updateLightMap(x, y, z - 1, color, intensity);
		updateLightMap(x, y, z + 1, color, intensity);
	}	
	
	public int getCube(int x, int y, int z) {
//		if(!isInitialized) extraVBOUpdate = true;
		System.out.println(new Vector3f(x, y, z).valuesToString());
		return cubes[x + (y * size) + (z * size * size)];
	}
	
	public int getCubeMetadata(int x, int y, int z) {
//		if(!isInitialized) extraVBOUpdate = true;
		return cubeMetadata[x + (y * size) + (z * size * size)];
	}
	
	public void setCube(int x, int y, int z, Cube cube) {
		Cube lastCube = Cube.getCubeByID(getCube(x, y, z));
		cubes[x + (y * size) + (z * size * size)] = cube.getId();
		
		handelUpdate(x, y, z, lastCube, getCubeMetadata(x, y, z));
	}
	
	public void setCubeMetadata(int x, int y, int z, int metadata) {
		int lastMetadata = getCubeMetadata(x, y, z);
		cubeMetadata[x + (y * size) + (z * size * size)] = metadata;
		
		handelUpdate(x, y, z, Cube.getCubeByID(getCube(x, y, z)), lastMetadata);
	}
	
	public void setCubeWithMetadata(int x, int y, int z, Cube cube, int metadata) {
		Cube lastCube = Cube.getCubeByID(getCube(x, y, z));
		int lastMetadata = getCubeMetadata(x, y, z);

		cubes[x + (y * size) + (z * size * size)] = cube.getId();
		cubeMetadata[x + (y * size) + (z * size * size)] = metadata;
		
		handelUpdate(x, y, z, lastCube, lastMetadata);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param lastCube
	 * @param lastMetadata
	 */
	private void handelUpdate(int x, int y, int z, Cube lastCube, int lastMetadata) {
		Cube cube = Cube.getCubeByID(getCube(x, y, z));
		int metadata = getCubeMetadata(x, y, z);		
		
		if(cube.isSolid(metadata)) {
			hasCubes = true;			
			if(!lastCube.isSolid(lastMetadata)) cubeCount ++;
			
		} else {
			if(lastCube.isSolid(lastMetadata)) cubeCount --;
			if(cubeCount <= 0) hasCubes = false;
			
		}
		
		if(cube.isLightSorce(metadata)) {
			updateLightMap(x, y, z, cube.getLightColor(metadata), cube.getLightValue(metadata));
			
		} else if(lastCube.isLightSorce(lastMetadata)) {
			
		}
		
		if(GameName.isRunning && WorldRegistry.getWorld(worldId).isGenerated() && isPosOnEdge(x, y, z)) {
			Chunk chunk;
			
			if(x == 0) {chunk = WorldRegistry.getWorld(worldId).getChunk(this.x - 1, this.y, this.z); if(chunk != null && chunk.isLoaded()) chunk.forceVBOUpdate();}
			if(y == 0) {chunk = WorldRegistry.getWorld(worldId).getChunk(this.x, this.y - 1, this.z); if(chunk != null && chunk.isLoaded()) chunk.forceVBOUpdate();}
			if(z == 0) {chunk = WorldRegistry.getWorld(worldId).getChunk(this.x, this.y, this.z - 1); if(chunk != null && chunk.isLoaded()) chunk.forceVBOUpdate();}

			if(x == size - 1) {chunk = WorldRegistry.getWorld(worldId).getChunk(this.x + 1, this.y, this.z); if(chunk != null && chunk.isLoaded()) chunk.forceVBOUpdate();}
			if(y == size - 1) {chunk = WorldRegistry.getWorld(worldId).getChunk(this.x, this.y + 1, this.z); if(chunk != null && chunk.isLoaded()) chunk.forceVBOUpdate();}
			if(z == size - 1) {chunk = WorldRegistry.getWorld(worldId).getChunk(this.x, this.y, this.z + 1); if(chunk != null && chunk.isLoaded()) chunk.forceVBOUpdate();}
		}
		
		render.setHasCubes(hasCubes);		
		render.forceVBOUpdate();
	}
	
	public boolean isPosOnEdge(int x, int y, int z) {
		return x == 0 || y == 0 || z == 0 ||
				x == size - 1 || y == size - 1 || z == size - 1;
	}
	
	public Cube[] getSurroundingCubes(int x, int y, int z) {
		int[] cubes = new int[6];
		World world = WorldRegistry.getWorld(worldId);
		LoadedWorldAccess access = world.getLoadedWorld().getAccess();
		
		Vector3f minPos = access.getCenter().subtract(LoadedWorldAccess.getRenderRadius()).capMin(0);		
		Vector3f maxPos = access.getCenter().add(LoadedWorldAccess.getRenderRadius()).capMax(world.getSizeAsVector()).subtract(1);
			
		if(!isPosOnEdge(x, y, z)) {
			cubes[0] = getCube(x - 1, y, z); // 0 -x			1				z         
			cubes[1] = getCube(x, y, z + 1); // 1 +z		0	C	2		-x	c	x     
			cubes[2] = getCube(x + 1, y, z); // 2 +x			3			   -z         
			cubes[3] = getCube(x, y, z - 1); // 3 -z					4				+y
			cubes[4] = getCube(x, y + 1, z); // 4 +y					C				 c
			cubes[5] = getCube(x, y - 1, z); // 5 -y					5				-y
		
		} else {
			try {
			
			if(getX() - 1 > minPos.getX()) cubes[0] = world.getChunk(getX() - 1, getY(), getZ()).getCube(getSize(), y, z);
			if(getZ() + 1 < maxPos.getZ()) cubes[1] = world.getChunk(getX(), getY(), getZ() + 1).getCube(x, y, 0);
			if(getX() + 1 < maxPos.getX()) cubes[2] = world.getChunk(getX() + 1, getY(), getZ()).getCube(0, y, z);
			if(getZ() - 1 > minPos.getZ()) cubes[3] = world.getChunk(getX(), getY(), getZ() - 1).getCube(x, y, getSize());
			if(getY() + 1 < maxPos.getY()) cubes[4] = world.getChunk(getX(), getY() + 1, getZ()).getCube(x, 0, z);
			if(getY() - 1 > minPos.getY()) cubes[5] = world.getChunk(getX(), getY() - 1, getZ()).getCube(x, getSize(), z);
			
			} catch(NullPointerException e) {
				System.err.println("\n" + new Vector3f(x, y, z).valuesToString() + "\n\tMax: " + maxPos.valuesToString() + "\n\tMin: " + minPos.valuesToString() + "\n\tPos: " + getPos().valuesToString());				
				System.err.println(e.getStackTrace()[0]);
			}
		}
		
		return Cube.getCubesByID(cubes);	
	}
	
	public float[] getLightColor(int x, int y, int z) {
		if(!isInitialized) return new float[] {1.0f, 1.0f, 1.0f};
		return lightColorMap[x + (y * size) + (z * size * size)];
	}
	
	public float getLightValue(int x, int y, int z) {
		if(!isInitialized) return ambiantLight;
		return lightValueMap[x + (y * size) + (z * size * size)];
	}

	public int getWorldId() {
		return worldId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public Vector3f getPos() {
		return new Vector3f(x, y, z);
	}

	public boolean isInitialized() {
		return isInitialized;
	}
	
//	public boolean isVboUpdataRequested() {
//		return vboUpdataRequested;
//	}
	
	public boolean isLoaded() {
		return isLoaded;
	}

	public boolean hasCubes() {
		return hasCubes;
	}

	public ArrayList<TagGroup> getTagGroup() {
		ArrayList<TagGroup> tagLines = new ArrayList<TagGroup>();
		
		for(int z = 0; z < size; z ++) {
		for(int y = 0; y < size; y ++) {
		for(int x = 0; x < size; x ++) {
			
			int cube = getCube(x, y, z);
			if(cube == 0) continue;
			
			tagLines.add(new TagGroup(new Tag("type", "cube"), new Tag[] {
				new Tag("cubeId", cube),
				new Tag("pos", new Vector3f(x, y, z))
			}));
		}}}
		
		return tagLines;
	}

	public ChunkRender getRender() {
		return render;
	}
	
	public int getSize() {
		return size;
	}
	
	public void forceVBOUpdate() {
		render.forceVBOUpdate();
	}
	
	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}
}
