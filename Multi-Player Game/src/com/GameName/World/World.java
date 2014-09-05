package com.GameName.World;

import com.GameName.Render.RenderEngine;
import com.GameName.Util.Time;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Cube.Cube;
import com.GameName.World.Object.WorldObject;

public class World {
	
	public static final float AMBIANT_LIGHT = 10;
	public static final float MAX_LIGHT = 10;
	public static final int CHUNK_SIZE = 10;
	public static final int VERTICE_SIZE = CHUNK_SIZE + 2;
	public static final float SCALE = 2;
	
	private int sizeX, sizeY, sizeZ;
	private int chunkX, chunkY, chunkZ;
	
	private String name;
	private LoadedWorld loadedWorld;
	private int id;
		
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
			
		loadedWorld = new LoadedWorld(this, name);
		
		double time = Time.getTime();
		
		System.out.print("World " + id + " Starting Generation: ");
		if(!isGenerated)
			generate();
		
		System.out.println("Done In " + RenderEngine.oneDecimal(((double) Time.getTime() - time) / Time.getSECONDS()) + " Seconds");
	}
	
	private void generate() {
		Chunk[] chunks = new Chunk[chunkX * chunkY * chunkZ];
		
		for(int cz = 0; cz < chunkZ; cz ++) {
		for(int cy = 0; cy < chunkY; cy ++) {
		for(int cx = 0; cx < chunkX; cx ++) {
		
			chunks[cx + (cy * chunkX) + (cz * chunkX * chunkY)] = new Chunk(CHUNK_SIZE, id, cx, cy, cz);
			
			for(int x = 0; x < CHUNK_SIZE; x ++) {
				for(int y = 0; y < CHUNK_SIZE; y ++) {
					for(int z = 0; z < CHUNK_SIZE; z ++) {// % 2
						chunks[cx + (cy * chunkX) + (cz * chunkX * chunkY)].setCube(x, y, z, 
								Math.random() > 0.9 ? Cube.GoldCube : Cube.StoneCube); // : Math.random() > 0.7 ? Cube.CopperCube//(int)(Math.random() * 10) > 6 ? Cube.Air : (int)(Math.random() * 10) > 6 ? Cube.ColorfulTestCube : Cube.TestCube); // 
					}
				}				
			}
		}}}
		
		for(int x = 0; x < sizeX / 2; x ++) {
			for(int y = 0; y < sizeY/2; y ++) {
				for(int z = 0; z < sizeZ; z ++) {
					setCube(x + sizeX/4, y + sizeY/4, z, Cube.Air, chunks);
				}
			}
		}
		
		for(int z = 0; z < sizeZ / 2; z ++) {
			for(int y = 0; y < sizeY/2; y ++) {
				for(int x = 0; x < sizeX; x ++) {
					setCube(x, y + sizeY/4, z + sizeZ/4, Cube.Air, chunks);
				}
			}
		}
		
		for(int z = 0; z < sizeZ / 2; z ++) {
			for(int x = 0; x < sizeX/2; x ++) {
				for(int y = 0; y < sizeY - (sizeY / 3); y ++) {
					setCube(x + sizeX/4, y, z + sizeZ/4, Cube.Air, chunks);
				}
			}
		}
		
		final int RADIUS = 10;
		
		float f1, f2, f3, f4;
		
		for(int rotY = 0; rotY < 360; rotY ++) {
		for(int rotX = 0; rotX < 360; rotX ++) {
				
			
		for(int radius = 0; radius < RADIUS; radius ++) {			
			
			f1 = (float)  Math.cos(Math.toRadians(rotY));
			f2 = (float)  Math.sin(Math.toRadians(rotY));
			f3 = (float)  Math.cos(Math.toRadians(rotX));                  
			f4 = (float)  Math.sin(Math.toRadians(rotX));                  
			
			int cubeID = Math.abs(Math.round(f4 * radius)) % 3 + 3;	
			
			Vector3f loadPos = new Vector3f(
					Math.round(f2 * f3 * radius), 
					Math.round(f4 * radius), 
					Math.round(f1 * f3 * radius))
				.add(new Vector3f(50, 50, 50));
			
			setCube((int) loadPos.getX(), (int) loadPos.getY(), (int) loadPos.getZ(), Cube.getCubeByID(cubeID), chunks);//(float) Math.sin(z) * 5
		}}}
		
		loadedWorld.getAccess().setCunks(chunks);
		isGenerated = true;
	}
	
	public boolean checkChunks() {return loadedWorld.checkChunks();}
	public void updataChunks()	 {loadedWorld.updataChunks();}
	
	public Cube getCube(float x, float y, float z)  {return loadedWorld.getAccess().getCube(x, y, z);}
	public Cube getCube(Vector3f pos) 				{return loadedWorld.getAccess().getCube(pos);}
	
	private void setCube(int x, int y, int z, Cube cubeId, Chunk[] chunks) {		
		int ix = (int) x, chunkCoordX = ix / CHUNK_SIZE, indexX = ix % CHUNK_SIZE;
		int iy = (int) y, chunkCoordY = iy / CHUNK_SIZE, indexY = iy % CHUNK_SIZE;
		int iz = (int) z, chunkCoordZ = iz / CHUNK_SIZE, indexZ = iz % CHUNK_SIZE;

		chunks[chunkCoordX + (chunkCoordY * chunkX) + (chunkCoordZ * chunkX * chunkY)]
				.setCube(indexX, indexY, indexZ, cubeId);
	}
	
	public void setCube(int x, int y, int z, int cubeId)  {loadedWorld.getAccess().setCube(x, y, z, cubeId);}
	public void setCube(Vector3f pos, int cubeId) 		  {loadedWorld.getAccess().setCube(pos, cubeId);}
	
	public float[] getLightColor(float x, float y, float z) {return loadedWorld.getAccess().getLightColor(x, y, z);}
	public float[] getLightColor(Vector3f pos) 				{return getLightColor(pos.getX(), pos.getY(), pos.getZ());}
	
	public float getLightValue(float x, float y, float z) 	{return loadedWorld.getAccess().getLightValue(x, y, z);}
	public float getLightValue(Vector3f pos) 				{return getLightValue(pos.getX(), pos.getY(), pos.getZ());}
	
	public Chunk getChunk(int x, int y, int z)  {return loadedWorld.getAccess().getChunk(x, y, z);}
	public Chunk getChunk(Vector3f pos) 	 	{return loadedWorld.getAccess().getChunk(pos);}
	
	public float getGroundHeight(int x, int y, int z)	{return loadedWorld.getAccess().getGroundHeight(x, y, z);}
	public float getGroundHeight(Vector3f pos)	{return loadedWorld.getAccess().getGroundHeight(pos);}
	
	public WorldObject getObject(int index) {return loadedWorld.getAccess().getObject(index);}
	
	public Chunk[] 		 getChunks()	   	{return loadedWorld.getAccess().getChunks();}
	public WorldObject[] getWorldObjects() 	{return loadedWorld.getAccess().getObjects();}
	
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

	public String getName() {
		return name;
	}

	public boolean isGenerated() {
		return isGenerated;
	}
	
	public String toString() {
		return name  + "[ID=" + id + "]";
	}	
	
	public LoadedWorld getLoadedWorld() {
		return loadedWorld;
	}
	
	public void cleanUp() {
		loadedWorld.cleanUp();
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
