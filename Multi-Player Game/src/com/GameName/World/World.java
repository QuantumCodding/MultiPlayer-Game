package com.GameName.World;

import com.GameName.Entity.Entity;
import com.GameName.Render.RenderEngin;
import com.GameName.Util.Time;
import com.GameName.Util.Vector3f;
import com.GameName.World.Cube.Cube;

public class World {
	
	public static final float AMBIANT_LIGHT = 10;
	public static final float MAX_LIGHT = 10;
	public static final int CHUNK_SIZE = 10;
	public static final int VERTICE_SIZE = CHUNK_SIZE + 2;
	public static final float SCALE = 2;
	
	private int sizeX, sizeY, sizeZ;
	private int chunkX, chunkY, chunkZ;
	
	private String name;
	private int id;
	
	private Chunk[] chunks;	
	private Entity[] EntityList;
	
	private boolean isGenerated = false;
	
	public World(int x, int y, int z, int id, String name) {
		sizeX = x * CHUNK_SIZE;
		sizeY = y * CHUNK_SIZE;
		sizeZ = z * CHUNK_SIZE;
		
		chunkX = x;
		chunkY = y;
		chunkZ = z;
		
		this.id = id;
		this.name = name;
		
		chunks = new Chunk[chunkX * chunkY * chunkZ];
		
		double time = Time.getTime();
		
		System.out.print("World " + id + " Starting Generation: ");
		if(!isGenerated)
			generate();
		
		System.out.println("Done In " + RenderEngin.oneDecimal(((double) Time.getTime() - time) / Time.getSECONDS()) + " Seconds");
	}
	
	private void generate() {
		for(int cz = 0; cz < chunkZ; cz ++) {
		for(int cy = 0; cy < chunkY; cy ++) {
		for(int cx = 0; cx < chunkX; cx ++) {
		
			chunks[cx + (cy * chunkX) + (cz * chunkX * chunkY)] = new Chunk(CHUNK_SIZE, id, cx, cy, cz);
			
			for(int x = 0; x < CHUNK_SIZE; x ++) {
				for(int y = 0; y < CHUNK_SIZE; y ++) {
					for(int z = 0; z < CHUNK_SIZE; z ++) {// % 2
						chunks[cx + (cy * chunkX) + (cz * chunkX * chunkY)].setCube(x, y, z, 
								Math.random() > 0.9 ? Cube.GoldCube : Cube.TestCube); // : Math.random() > 0.7 ? Cube.CopperCube//(int)(Math.random() * 10) > 6 ? Cube.Air : (int)(Math.random() * 10) > 6 ? Cube.ColorfulTestCube : Cube.TestCube); // 
					}
				}				
			}
		}}}
		
		for(int x = 0; x < sizeX / 2; x ++) {
			for(int y = 0; y < sizeY/2; y ++) {
				for(int z = 0; z < sizeZ; z ++) {
					setCube(x + sizeX/4, y + sizeY/4, z, Cube.Air.getId());
				}
			}
		}
		
		for(int z = 0; z < sizeZ / 2; z ++) {
			for(int y = 0; y < sizeY/2; y ++) {
				for(int x = 0; x < sizeX; x ++) {
					setCube(x, y + sizeY/4, z + sizeZ/4, Cube.Air.getId());
				}
			}
		}
		
		for(int z = 0; z < sizeZ / 2; z ++) {
			for(int x = 0; x < sizeX/2; x ++) {
				for(int y = 0; y < sizeY - ((int)(((float) sizeY / 8f) * 3f)); y ++) {
					setCube(x + sizeX/4, y, z + sizeZ/4, Cube.Air.getId());
				}
			}
		}
		
		setCube(0, 0, 0, Cube.ColorfulTestCube.getId());
		
		isGenerated = true;
	}

	public void setCube(int x, int y, int z, int cubeId) {		
		int ix = (int) x, chunkCoordX = ix / CHUNK_SIZE, indexX = ix % CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / CHUNK_SIZE, indexY = iy % CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / CHUNK_SIZE, indexZ = iz % CHUNK_SIZE;

		chunks[chunkCoordX + (chunkCoordY * chunkX) + (chunkCoordZ * chunkX * chunkY)]
				.setCube(indexX, indexY, indexZ, Cube.getCubeByID(cubeId));
	}

	public Cube getCube(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / CHUNK_SIZE, indexX = ix % CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / CHUNK_SIZE, indexY = iy % CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / CHUNK_SIZE, indexZ = iz % CHUNK_SIZE;
		
		if(chunkCoordX + (chunkCoordY * chunkX) + (chunkCoordZ * chunkX * chunkY) >= chunks.length) 
			System.out.println("Chunk: " + chunkCoordX + " " + chunkCoordY + " " + chunkCoordZ);
		
		return Cube.getCubeByID(
				chunks[chunkCoordX + (chunkCoordY * chunkX) + (chunkCoordZ * chunkX * chunkY)]
				.getCube(indexX, indexY, indexZ)
			);
	}
	
	public float[] getLightColor(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / CHUNK_SIZE, indexX = ix % CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / CHUNK_SIZE, indexY = iy % CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / CHUNK_SIZE, indexZ = iz % CHUNK_SIZE;
			
		return chunks[chunkCoordX + (chunkCoordY * chunkX) + (chunkCoordZ * chunkX * chunkY)]
				.getLightColor(indexX, indexY, indexZ);
	}
	
	public float getLightValue(float x, float y, float z) {
		int ix = (int) x, chunkCoordX = ix / CHUNK_SIZE, indexX = ix % CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / CHUNK_SIZE, indexY = iy % CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / CHUNK_SIZE, indexZ = iz % CHUNK_SIZE;
			
		return chunks[chunkCoordX + (chunkCoordY * chunkX) + (chunkCoordZ * chunkX * chunkY)]
				.getLightValue(indexX, indexY, indexZ);
	}
	
	public Chunk[] getChunks() {
		return chunks;
	}
	
	public float getGroundHeight(float x, float y, float z) {
		int groundHeight = Math.min(Math.round(y), sizeY - 1);
		while(groundHeight > 0 && !getCube(x, groundHeight, z).isSolid())  groundHeight --; 
		
		return groundHeight;
	}
	
	public float getGroundHeight(Vector3f pos) {
		return getGroundHeight(pos.getX(), pos.getY(), pos.getZ());
	}
		
	public Entity getEntity(int index) {
		return EntityList[index];
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getSizeZ() {
		return sizeZ;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	public int getId() {
		return id;
	}

	public Chunk getChunk(int x, int y, int z) {
		return chunks[x + (y * chunkX) + (z * chunkX * chunkY)];
	}

	public Entity[] getEntityList() {
		return EntityList;
	}

	public String getName() {
		return name;
	}

	public boolean isGenerated() {
		return isGenerated;
	}

	public void setEntityList(Entity[] entityList) {
		EntityList = entityList;
	}
	
	public String toString() {
		return name  + "[ID=" + id + "]";
	}

	public void checkChunks() {
		for(Chunk chunk : chunks) {
			if(chunk.isVboUpdataRequested()) {
				chunk.updataVBO();
			}	
		}		
	}

	public Cube getCube(Vector3f cord) {
		return getCube(cord.getX(), cord.getY(), cord.getZ());
	}
}


//PerlinNoise noise = new PerlinNoise(sizeX, sizeZ);
//
//int width = 512; // Width of the finished image.
//int height = 512; // Height of the finished image.
//
//BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // Image to store pixel data in.
//
//for (int y = 0; y < height; y++) {
//	for (int x = 0; x < width; x++) {
//		
//		float xx = (float) x / width * sizeX; // Where does the point lie in the noise space according to image space. 
//		float yy = (float) y / height * sizeZ; // Where does the point lie in the noise space according to image space. 
//		
//		float n = (float) noise.noise(xx, yy); // Noise values from Perlin's noise.
//		int col = (int) ((n + 1) * 255 / 2f); // Since noise value returned is -1 to 1, we make it so that -1 is black, and 1 is white.
//		
//		Color color = new Color(col, col, col); // java.AWT color to get RGB from.
//		image.setRGB(x, y, color.getRGB()); // set XY image value to our generated color.
//		
//	}
//}
//
//for(int z = 0; z < sizeZ; z ++) {
//	for(int x = 0; x < sizeX; x ++) {
//		Color c = new Color(image.getRGB(x, z));
//		
//		int maxHeight = (int) (sizeY / 255f * (float)(c.getBlue()));
////		System.out.println(maxHeight + ", " + c.getBlue());
//		
//		for(int y = maxHeight; y < sizeY; y ++) {
////			cubes[z + (y * sizeZ) + (x * sizeZ * sizeY)] = Cube.Air;
//		}
//	}	
//}
