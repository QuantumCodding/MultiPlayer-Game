package com.GameName.Render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.GameName.Main.GameName;
import com.GameName.Util.Util;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;
import com.GameName.World.Cube.Cube;

public class RenderUtil {
	private static boolean isIntialized;
	private static List<Integer> cubeIndices;

	protected static void init() {
		if(isIntialized) return;
		
		cubeIndices = new ArrayList<Integer>();
		
		cubeIndices.add(0); cubeIndices.add(1); cubeIndices.add(3); cubeIndices.add(2); // Left
		cubeIndices.add(1); cubeIndices.add(5); cubeIndices.add(7); cubeIndices.add(3); // Front
		cubeIndices.add(5); cubeIndices.add(4); cubeIndices.add(6); cubeIndices.add(7); // Right
		cubeIndices.add(4); cubeIndices.add(0); cubeIndices.add(2); cubeIndices.add(6); // Back
		cubeIndices.add(0); cubeIndices.add(4); cubeIndices.add(5); cubeIndices.add(1); // Bottom
		cubeIndices.add(2); cubeIndices.add(3); cubeIndices.add(7); cubeIndices.add(6); // Top
		
		isIntialized = true;
	}

	public static int[][] generateWorldRender(World w) {
		
		int[][] worldVBOs = new int[w.getChunkX() * w.getChunkY() * w.getChunkZ()][5];
		int[] chunkData;
		
		for(int x = 0; x < w.getChunkX(); x ++) {
			for(int y = 0; y < w.getChunkY(); y ++) {
				for(int z = 0; z < w.getChunkZ(); z ++) {
					
					int[] ids = GameName.getGLContext().genBufferIds(3);	
					chunkData = new int[] {
							ids[0], ids[1], // 0: Vertices  	1: TexCoords							
							ids[2], 0		// 2: Light Values  3: Indices Size
						};
					
					worldVBOs[z + (y * w.getChunkZ()) + (x * w.getChunkZ()  * w.getChunkY())] = generateChunk(x, y, z, w, chunkData);
				}	
			}
		}
		
		return worldVBOs;
	}
	
	public static int[] generateChunk(int startX, int startY, int startZ, World w, int[] chunkData) {
		List<Vector3f> chunkVertices = new ArrayList<Vector3f>();
		
		List<Float> chunkTexData = new ArrayList<Float>();
		List<Float> chunklightValues = new ArrayList<Float>();
		
		List<Integer> chunkIndices = new ArrayList<Integer>();
				
		int faceCount = 0;
		chunkVertices.addAll(getDefaultChunk(
					  startX, startY, startZ,
				World.VERTICE_SIZE, World.CHUNK_SIZE
			));
		
		for(int x = 0; x < World.CHUNK_SIZE; x ++) {
			for(int y = 0; y < World.CHUNK_SIZE; y ++) {
				for(int z = 0; z < World.CHUNK_SIZE; z ++) {
					
					boolean[] visableFaces = 
						getVisableFaces(
							(startX * World.CHUNK_SIZE) + x,
							(startY * World.CHUNK_SIZE) + y, 
							(startZ * World.CHUNK_SIZE) + z, w
						);
											
					chunkTexData.addAll(getCubeTexture(
							startX, x, startY, y, startZ, z,
							World.CHUNK_SIZE, w, visableFaces
						));
					
					chunklightValues.addAll(getLightValue(
							(startX * World.CHUNK_SIZE) + x,
							(startY * World.CHUNK_SIZE) + y, 
							(startZ * World.CHUNK_SIZE) + z, w
						));
					
					chunkIndices.addAll(generateIndices(	
							x, y, z, visableFaces, 
							  World.VERTICE_SIZE
						));
					
					faceCount += getVisableFaceCount(visableFaces);
				}
			}
		}
		
		List<Float> vertices = new ArrayList<Float>();
		
		for(int i = 0; i < chunkIndices.size(); i ++) {
			vertices.add(chunkVertices.get(chunkIndices.get(i)).getX());
			vertices.add(chunkVertices.get(chunkIndices.get(i)).getY());
			vertices.add(chunkVertices.get(chunkIndices.get(i)).getZ());
		}
		
		FloatBuffer verticeBuffer = Util.createFillipedFloatBuffer(vertices);
		FloatBuffer texDataBuffer = Util.createFillipedFloatBuffer(chunkTexData);
		FloatBuffer lightBuffer = Util.createFillipedFloatBuffer(chunklightValues);
		
		if(chunkData == null) {
			int[] ids = GameName.getGLContext().genBufferIds(3);	
			chunkData = new int[] {
					ids[0], ids[1], // 0: Vertices  	1: TexCoords							
					ids[2], 0		// 2: Light Values  3: Indices Size
				};
		}
		
		GameName.getGLContext()
			.addBufferBind(verticeBuffer, GL_ARRAY_BUFFER, chunkData[0], GL_DYNAMIC_DRAW, 'f');
		
		GameName.getGLContext()
			.addBufferBind(texDataBuffer, GL_ARRAY_BUFFER, chunkData[1], GL_DYNAMIC_DRAW, 'f');

		GameName.getGLContext()
			.addBufferBind(lightBuffer, GL_ARRAY_BUFFER, chunkData[2], GL_DYNAMIC_DRAW, 'f');
	    
	    chunkData[3] = faceCount * 4;
	    
	    return chunkData;
	}

	private static boolean[] getVisableFaces(int x, int y, int z, World w) {
		boolean[] visableFaces = new boolean[6];
		
//		System.out.println(x + " " + y + " " + z);
		
		if(!w.getCube(x, y, z).isVisable()) return visableFaces;
		
		if(x == 0 || y == 0 || z == 0) {
			if(x == 0) visableFaces[0] = true;
			if(y == 0) visableFaces[4] = true;
			if(z == 0) visableFaces[3] = true;
		}
		
		if(x >= w.getSizeX() - 1 || y >= w.getSizeY() - 1 || z >= w.getSizeZ() - 1) {
			if(x >= w.getSizeX() - 1)  visableFaces[2] = true;
			if(y >= w.getSizeY() - 1)  visableFaces[5] = true;
			if(z >= w.getSizeZ() - 1)  visableFaces[1] = true;
		}
		
		if(!visableFaces[0]) visableFaces[0] = !w.getCube(x - 1, y, z).isVisable(); // 0			1
		if(!visableFaces[1]) visableFaces[1] = !w.getCube(x, y, z + 1).isVisable(); // 1		0	C	2
		if(!visableFaces[2]) visableFaces[2] = !w.getCube(x + 1, y, z).isVisable(); // 2			3
		if(!visableFaces[3]) visableFaces[3] = !w.getCube(x, y, z - 1).isVisable(); // 3						5
		if(!visableFaces[4]) visableFaces[4] = !w.getCube(x, y - 1, z).isVisable(); // 4						C
		if(!visableFaces[5]) visableFaces[5] = !w.getCube(x, y + 1, z).isVisable(); // 5						4			
				
		return visableFaces;
	}

	@SuppressWarnings("unused")
	private static boolean isVisable(int x, int y, int z, World w) {
		
		if(!w.getCube(x, y, z).isVisable()) return false;
		
		if(x == 0 || y == 0 || z == 0) return true;
		if(x >= w.getSizeX() - 1 || y >= w.getSizeY() - 1 || z >= w.getSizeZ() - 1) return true;		
		
		if(!w.getCube(x + 1, y, z).isVisable()) return true;
		if(!w.getCube(x - 1, y, z).isVisable()) return true;
		if(!w.getCube(x, y + 1, z).isVisable()) return true;
		if(!w.getCube(x, y - 1, z).isVisable()) return true;
		if(!w.getCube(x, y, z + 1).isVisable()) return true;
		if(!w.getCube(x, y, z - 1).isVisable()) return true;
				
		return false;
	}
	
	private static int getVisableFaceCount(boolean[] visableFaces) {
		int faceCount = 0;
		
		for(int i = 0; i < visableFaces.length; i ++) {
			if(visableFaces[i]) {
				faceCount ++;
			}
		}
		
		return faceCount;
	} 
	
	protected static List<Float> getLightValue(int x, int y, int z, World w) {
		List<Float> lightValue = new ArrayList<Float>();
		
		float[] color = w.getLightColor(x, y, z);
		float value = w.getLightValue(x, y, z);
		
		lightValue.add(color[0] * value);
		lightValue.add(color[1] * value);
		lightValue.add(color[2] * value);
		
//		lightValue.add(1f);
//		lightValue.add(1f);
//		lightValue.add(1f);
		
		return lightValue;
	}
	
	protected static List<Float> getCubeTexture(int cx, int px, int cy, int py, int cz, int pz, int cSize, World w, boolean[] visableFaces) {
		List<Float> texData = new ArrayList<Float>();		

		Cube cube = w.getCube(
				(cx * cSize) + px, 
				(cy * cSize) + py, 
				(cz * cSize) + pz
			);
		
		int startX = Cube.getCubeTexCoords()[(cube.getId() * 2)], startY = Cube.getCubeTexCoords()[(cube.getId() * 2) + 1];
		double ratio = (1d / (double) Cube.getTextureSheetSideLength());
		double x = ratio * startX, y = ratio * startY;
		double textureSideLength = ratio * cube.getTextureSize();
		
		for(int k = 0; k < 6; k ++) {
			if(!visableFaces[k]) continue;
						
			for(int i = 0; i < 4; i ++) {
				float useX = (float) ((textureSideLength * k) + x);
				float useY = (float) y;
				
				float xAdd = 0.0f, yAdd = 0.0f;
				
				switch(i) {
					case 1: xAdd = (float) textureSideLength; break;
					case 2: xAdd = (float) textureSideLength; yAdd = (float) textureSideLength; break;
					case 3: yAdd = (float) textureSideLength; break;
						
					default: break;
				}
				
				texData.add(useX + xAdd);	texData.add(useY + yAdd);
			}
		}
		
		return texData;
	}
	
	protected static List<Integer> generateIndices(int x, int y, int z, boolean[] visableFaces, int size) {
		List<Integer> indices = new ArrayList<Integer>();
		
		int[] possiblePoints = getPossiblePoints(x, y, z, size);
				
		for(int i = 0; i < cubeIndices.size(); i += 4) {
			if(visableFaces[i / 4]) {
				
				indices.add(possiblePoints[cubeIndices.get(i)]); 
				indices.add(possiblePoints[cubeIndices.get(i + 1)]);
				indices.add(possiblePoints[cubeIndices.get(i + 2)]);
				indices.add(possiblePoints[cubeIndices.get(i + 3)]);
				
//				System.out.println( i / 4 + ": " + 
//						possiblePoints[cubeIndices.get(i)] + "\t" + possiblePoints[cubeIndices.get(i + 1)] + "\t" +
//						possiblePoints[cubeIndices.get(i + 2)] + "\t" + possiblePoints[cubeIndices.get(i + 3)]);
			}
		}
		
//		if(getVisableFaceCount(visableFaces) > 0) System.out.println();
		
		return indices;
	}

	private static int[] getPossiblePoints(int x, int y, int z, int size) {
		int[] points = new int[8];
		int startIndex = 
				((z / 2) * (8)		  		       + (z % 2 * 1)) + 
				((y / 2) * ((8 * size) / 2)        + (y % 2 * 2)) +
				((x / 2) * ((8 * size * size) / 4) + (x % 2 * 4));
		
		int ys = ((8 * size) / 2) - 2, xs = ((8 * size * size) / 4) - 4;
		
		boolean zOdd = z % 2 == 1, yOdd = y % 2 == 1, xOdd = x % 2 == 1;
		int[] add = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
		
		if(xOdd && yOdd && zOdd) add = new int[] {0, 7, ys, ys + 7, xs, xs + 7, xs + ys, xs + ys + 7};
	
		else if(xOdd && zOdd) add = new int[] {0, 7, 2, 9, xs, xs + 7, xs + 2, xs + 9};               
		else if(xOdd && yOdd) add = new int[] {0, 1, ys, ys + 1, xs, xs + 1, xs + ys, xs + ys + 1};   
		else if(yOdd && zOdd) add = new int[] {0, 7, ys, ys + 7, 4, 11, ys + 4, ys + 11};           
		
		else if(xOdd) add = new int[] {0, 1, 2, 3, xs, xs + 1, xs + 2, xs + 3};
		else if(yOdd) add = new int[] {0, 1, ys, ys + 1, 4, 5, ys + 4, ys + 5};
		else if(zOdd) add = new int[] {0, 7, 2, 9, 4, 11, 6, 13};    
		
		for(int i = 0; i < points.length; i ++) {
			points[i] = startIndex + add[i];	
		}
		
		return points;
	} 
	
	private static List<Vector3f> getDefaultChunk(int cx, int cy, int cz, int vsize, int csize) {
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		
		for(int x = 0; x < vsize; x += 2) {
			for(int y = 0; y < vsize; y += 2) {
				for(int z = 0; z < vsize; z += 2) {
					
					for(int _x = 0; _x < 2; _x ++) {
					for(int _y = 0; _y < 2; _y ++) {
					for(int _z = 0; _z < 2; _z ++) {
						
						vertices.add(new Vector3f(
								x * 0.1f + (cx * csize) * 0.1f + _x * 0.1f,
								y * 0.1f + (cy * csize) * 0.1f + _y * 0.1f,
								z * 0.1f + (cz * csize) * 0.1f + _z * 0.1f
							));
					}}}
				}	
			}	
		}
				
		return vertices;
	}
}
