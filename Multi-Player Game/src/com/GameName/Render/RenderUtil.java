package com.GameName.Render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.GameName.Cube.Cube;
import com.GameName.Main.GameName;
import com.GameName.Util.Util;
import com.GameName.World.World;

public class RenderUtil {
	
	public static int[] generateChunk(int startX, int startY, int startZ, World w, int[] chunkData) {
		
		List<Float> chunkVertices = new ArrayList<Float>();		
		List<Float> chunkTexData = new ArrayList<Float>();
		List<Float> chunklightValues = new ArrayList<Float>();
				
		int faceCount = 0;
		int xPos, yPos, zPos;		
		
		for(int x = 0; x < World.CHUNK_SIZE; x ++) {
			for(int y = 0; y < World.CHUNK_SIZE; y ++) {
				for(int z = 0; z < World.CHUNK_SIZE; z ++) {
					
					if(!w.getChunk(startX, startY, startZ).isLoaded()) continue;
					
					xPos = x + (startX * World.CHUNK_SIZE);
					yPos = y + (startX * World.CHUNK_SIZE);
					zPos = z + (startX * World.CHUNK_SIZE);
					
					Cube cube = w.getCube(xPos, yPos, zPos);					
					int metadata = w.getCubeMetadata(xPos, yPos, zPos);					
					boolean[] visableFaces = getVisableFaces(xPos, yPos, zPos, w);
									
					chunkVertices.addAll(cube.getRender(metadata).getVertices(xPos, yPos, zPos, visableFaces));
					chunkTexData.addAll(cube.getRender(metadata).getTextureCoords(cube.getId(), metadata, visableFaces));					
					chunklightValues.addAll(getLightValue(xPos, yPos, zPos, w));
					
					faceCount += getVisableFaceCount(visableFaces);
				}
			}
		}
		
		FloatBuffer verticeBuffer = Util.createFillipedFloatBuffer(chunkVertices);
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
		int metadata = w.getCubeMetadata(x, y, z);
		
		if(!w.getCube(x, y, z).isVisable(metadata)) {
			return visableFaces;
		}
		
		Cube[] surroundingCubes = w.getSurroundingCubes(x, y, z);
		
		visableFaces[0] = surroundingCubes[0] != null ? !surroundingCubes[0].isVisable(metadata) : true; // 0 -x			1				z
		visableFaces[1] = surroundingCubes[1] != null ? !surroundingCubes[1].isVisable(metadata) : true; // 1 +z		0	C	2		-x	c	x
		visableFaces[2] = surroundingCubes[2] != null ? !surroundingCubes[2].isVisable(metadata) : true; // 2 +x			3			   -z
		visableFaces[3] = surroundingCubes[3] != null ? !surroundingCubes[3].isVisable(metadata) : true; // 3 -z					5			+y
		visableFaces[4] = surroundingCubes[4] != null ? !surroundingCubes[4].isVisable(metadata) : true; // 4 -y					C			 c
		visableFaces[5] = surroundingCubes[5] != null ? !surroundingCubes[5].isVisable(metadata) : true; // 5 +y					4			-y
				
		return visableFaces;
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
		
		return lightValue;
	}
}
