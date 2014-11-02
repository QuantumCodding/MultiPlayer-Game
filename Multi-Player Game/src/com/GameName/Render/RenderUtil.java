package com.GameName.Render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.GameName.Cube.Cube;
import com.GameName.Main.GameName;
import com.GameName.Main.Debugging.Logger;
import com.GameName.Util.BufferUtil;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class RenderUtil {
	/**
	 * @see ChunkRenderGerator
	 */
	@Deprecated
	public static int[] generateChunk(int startX, int startY, int startZ, World w, int[] chunkData) {
		
		List<Float> chunkVertices = new ArrayList<Float>();		
		List<Float> chunkTexData = new ArrayList<Float>();
		List<Float> chunklightValues = new ArrayList<Float>();
		List<Float> chunkNormals = new ArrayList<Float>();		
				
		int vertexCount = 0;
		int xPos, yPos, zPos;		
		
		for(int x = 0; x < World.CHUNK_SIZE; x ++) {
			for(int y = 0; y < World.CHUNK_SIZE; y ++) {
				for(int z = 0; z < World.CHUNK_SIZE; z ++) {
					
					if(!w.getChunk(startX, startY, startZ).isInitialized()) continue;
					if(!w.getChunk(startX, startY, startZ).isLoaded()) continue;
					
					xPos = x + (startX * World.CHUNK_SIZE);
					yPos = y + (startX * World.CHUNK_SIZE);
					zPos = z + (startX * World.CHUNK_SIZE);
					
					if(w.getCube(xPos, yPos, zPos) == null) continue;
					
					Cube cube = w.getCube(xPos, yPos, zPos);					
					int metadata = w.getCubeMetadata(xPos, yPos, zPos);					
					boolean[] visableFaces = getVisableFaces(xPos, yPos, zPos, w);
									
					chunkVertices.addAll(cube.getRender(metadata).getVertices(xPos, yPos, zPos, visableFaces));
//					chunkTexData.addAll(cube.getRender(metadata).getTextureCoords(cube.getId(), metadata, visableFaces));
					chunkNormals.addAll(cube.getRender(metadata).getNormals(cube.getId(), metadata, visableFaces));				
					chunklightValues.addAll(getLightValue(xPos, yPos, zPos, cube, metadata, visableFaces, w));
					
					vertexCount += cube.getRender(metadata).getVerticeCount(visableFaces);
				}
			}
		}
		
		FloatBuffer verticeBuffer = BufferUtil.createFillipedFloatBuffer(chunkVertices);
		FloatBuffer texDataBuffer = BufferUtil.createFillipedFloatBuffer(chunkTexData);
		FloatBuffer lightBuffer = BufferUtil.createFillipedFloatBuffer(chunklightValues);
		FloatBuffer normalBuffer = BufferUtil.createFillipedFloatBuffer(chunkNormals);
		
		if(chunkData == null) {
			int[] ids = GameName.getGLContext().genBufferIds(4);	
			chunkData = new int[] {
					ids[0], ids[1], // 0: Vertices  	1: TexCoords							
					ids[2], ids[3],	// 2: Light Values  3: Normals
					0				// 4: Vertex Count
				};
		}
		
		if(chunkData.length < 5) {
			int[] chunkData2 = new int[chunkData.length + 1];
			
			for(int i = 0; i < chunkData.length; i ++) {
				chunkData2[i] = chunkData[i];
			}
			
			chunkData2[chunkData.length] = 0;
			chunkData = chunkData2.clone();
		}
		
		Logger.println(chunkData.length + " " + new Vector3f(startX, startY, startZ).valuesToString());
		
		GameName.getGLContext()
			.addBufferBind(verticeBuffer, GL_ARRAY_BUFFER, chunkData[0], GL_DYNAMIC_DRAW, 'f');
		
		GameName.getGLContext()
			.addBufferBind(texDataBuffer, GL_ARRAY_BUFFER, chunkData[1], GL_DYNAMIC_DRAW, 'f');

		GameName.getGLContext()
			.addBufferBind(lightBuffer, GL_ARRAY_BUFFER, chunkData[2], GL_DYNAMIC_DRAW, 'f');
		
		GameName.getGLContext()
			.addBufferBind(normalBuffer, GL_ARRAY_BUFFER, chunkData[3], GL_DYNAMIC_DRAW, 'f');
	    
	    chunkData[4] = vertexCount;
	    
	    return chunkData;
	}

	private static boolean[] getVisableFaces(int x, int y, int z, World w) {
		boolean[] visableFaces = new boolean[6];		
		int metadata = w.getCubeMetadata(x, y, z);
		
		if(!w.getCube(x, y, z).isVisable(metadata)) {
			return visableFaces;
		}
		
		Cube[] surroundingCubes = w.getSurroundingCubes(x, y, z);
		
		visableFaces[0] = surroundingCubes[0] != null ? !surroundingCubes[0].isVisable(metadata) : true; // 0 -x			1				z
		visableFaces[1] = surroundingCubes[1] != null ? !surroundingCubes[1].isVisable(metadata) : true; // 1 +z		0	C	2		-x	c	x
		visableFaces[2] = surroundingCubes[2] != null ? !surroundingCubes[2].isVisable(metadata) : true; // 2 +x			3			   -z
		visableFaces[3] = surroundingCubes[3] != null ? !surroundingCubes[3].isVisable(metadata) : true; // 3 -z					4			+y
		visableFaces[4] = surroundingCubes[4] != null ? !surroundingCubes[4].isVisable(metadata) : true; // 4 +y					C			 c
		visableFaces[5] = surroundingCubes[5] != null ? !surroundingCubes[5].isVisable(metadata) : true; // 5 -y					5			-y
				
		return visableFaces;
	}
	
	protected static List<Float> getLightValue(int x, int y, int z, Cube cube, int metadata, boolean[] visableFaces, World w) {
		List<Float> lightValue = new ArrayList<Float>();
		List<Float> cubeColor = cube.getRender(metadata).getColors(cube.getId(), metadata, visableFaces);
		
		float[] color = w.getLightColor(x, y, z);
		float value = w.getLightValue(x, y, z);
		
		for(int i = 0; i < cubeColor.size(); i += 3) {
			lightValue.add((color[0] + cubeColor.get(i + 0)) * value);
			lightValue.add((color[1] + cubeColor.get(i + 1)) * value);
			lightValue.add((color[2] + cubeColor.get(i + 2)) * value);
		}
		
		return lightValue;
	}
		
	public static Vector3f calculatePolygonNormal(Vector3f... vertices) {
		Vector3f normal = new Vector3f(0, 0, 0);
		
		for(int i = 0; i < vertices.length; i ++) {
			Vector3f current = vertices[i], next = vertices[(i + 1) % vertices.length];
			
			normal.addAndSet(new Vector3f(
					(current.getY() - next.getY()) * (current.getZ() + next.getZ()), 
					(current.getZ() - next.getZ()) * (current.getX() + next.getX()), 
					(current.getX() - next.getX()) * (current.getY() + next.getY())					
				));
		}
		
		return normal;
	}
}
