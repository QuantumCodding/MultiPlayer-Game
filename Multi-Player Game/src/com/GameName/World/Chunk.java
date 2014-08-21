package com.GameName.World;

import com.GameName.Main.GameName;
import com.GameName.World.Cube.Cube;

public class Chunk {
	private final float MINIMUM_LIGHT_DEFUTION = 0.5f;
	
	private int worldId;
	private int x, y, z;
	
	private int[] cubes;
	private int size;
	
	private float[][] lightColorMap;
	private float[]   lightValueMap;
	private float 	  ambiantLight = (float) ((1d / (double) World.MAX_LIGHT) * World.AMBIANT_LIGHT);
	
	private boolean vboUpdataRequested;
	
	protected Chunk(int size, int worldId, int x, int y, int z) { 
		this.worldId = worldId;
		
		this.x = x; this.y = y; this.z = z;
		
		this.size = size;
		cubes = new int[size * size * size];
		
		lightColorMap = new float[size * size * size][3];
		lightValueMap = new float[size * size * size];
		
		for(int i = 0; i < size * size * size; i ++) {
			lightColorMap[i] = new float[] {0.0f, 0.0f, 0.0f};
			lightValueMap[i] = ambiantLight;
		}
	}
	
	public void updateLightMap(int x, int y, int z, float[] color, float intensity) {
		if(x < 0 || y < 0 || z < 0 || x == size || y == size || z == size) {
			GameName.worlds.get(worldId)
				.getChunk(
						this.x + (x < 0 ? -1 : x == size ? 1 : 0), 
						this.y + (y < 0 ? -1 : y == size ? 1 : 0),
						this.z + (z < 0 ? -1 : z == size ? 1 : 0))
				.updateLightMap(
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
		
		intensity -= ((1 / intensity) * Cube.getCubeByID(getCube(x, y, z)).getOpastity()) + MINIMUM_LIGHT_DEFUTION;
		
		if(intensity <= ambiantLight){
			vboUpdataRequested = true;	return; 
		}
		
		updateLightMap(x - 1, y, z, color, intensity);
		updateLightMap(x + 1, y, z, color, intensity);
		updateLightMap(x, y, z - 1, color, intensity);
		updateLightMap(x, y, z + 1, color, intensity);
	}	
	
	public int getCube(int x, int y, int z) {
		return cubes[x + (y * size) + (z * size * size)];
	}
	
	public void setCube(int x, int y, int z, Cube cube) {
		Cube lastCube = Cube.getCubeByID(getCube(x, y, z));
		cubes[x + (y * size) + (z * size * size)] = cube.getId();
		
		if(cube.isLightSorce()) {
			updateLightMap(x, y, z, cube.getLightColor(), cube.getLightValue());
			
		} else if(lastCube.isLightSorce()) {
			
		}
		
		vboUpdataRequested = true;
	}
	
	public void updataVBO() {
		GameName.render.updataChunk(getX(), getY(), getZ(), getWorldId());
		
		vboUpdataRequested = false;
	}
	
	public float[] getLightColor(int x, int y, int z) {
		return lightColorMap[x + (y * size) + (z * size * size)];
	}
	
	public float getLightValue(int x, int y, int z) {
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

	public boolean isVboUpdataRequested() {
		return vboUpdataRequested;
	}
}
