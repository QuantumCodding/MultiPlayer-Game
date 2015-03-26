package com.GameName.World.Render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.GameName.Cube.Cube;
import com.GameName.Engine.GameEngine;
import com.GameName.Util.BufferUtil;
import com.GameName.Util.Side;
import com.GameName.World.Chunk;
import com.GameName.World.World;

@Deprecated
public class ChunkRenderGenerator_Old {
	@Deprecated
	public static int[] generateChunk(GameEngine ENGINE, Chunk c, int[] chunkData) {
		
		ArrayList<Float> chunkVertices = new ArrayList<>();		
		ArrayList<Float> chunkTexData = new ArrayList<>();
		ArrayList<Float> chunklightValues = new ArrayList<>();
		ArrayList<Float> chunkNormals = new ArrayList<>();			
				
		int vertexCount = 0;
		int xPos, yPos, zPos;		
		
		for(int x = 0; x < c.getSize(); x ++) {
			for(int y = 0; y < c.getSize(); y ++) {
				for(int z = 0; z < c.getSize(); z ++) {
					if(!c.isAccessible()) continue;
					
					xPos = x + (c.getX() * World.CHUNK_SIZE);
					yPos = y + (c.getY() * World.CHUNK_SIZE);
					zPos = z + (c.getZ() * World.CHUNK_SIZE);
					
					Cube cube = Cube.getCubeByID(c.getCube(x, y, z));					
					int metadata = c.getMetadata(x, y, z);			
					boolean[] visableFaces = getVisableFaces(x, y, z, c); //new boolean[] {true, true, true, true, true, true}; //
						
					if(!cube.isVisable(metadata)) continue;	
					
					chunkVertices.addAll(cube.getRender(metadata).getVertices(xPos, yPos, zPos, visableFaces));
					chunkTexData.addAll(cube.getRender(metadata).getTextureCoords(cube.getId(), metadata, c.getTextureMap(), visableFaces));
					chunkNormals.addAll(cube.getRender(metadata).getNormals(cube.getId(), metadata, visableFaces));				
					chunklightValues.addAll(getLightValue(x, y, z, cube, metadata, visableFaces, c));
					
					vertexCount += cube.getRender(metadata).getVerticeCount(visableFaces);
				}
			}
		}
		
		FloatBuffer verticeBuffer = BufferUtil.createFillipedFloatBuffer(chunkVertices);
		FloatBuffer texDataBuffer = BufferUtil.createFillipedFloatBuffer(chunkTexData);
		FloatBuffer lightBuffer = BufferUtil.createFillipedFloatBuffer(chunklightValues);
		FloatBuffer normalBuffer = BufferUtil.createFillipedFloatBuffer(chunkNormals);
				
		if(chunkData.length < 5) {
			int[] chunkData2 = new int[chunkData.length + 1];
			
			for(int i = 0; i < chunkData.length; i ++) {
				chunkData2[i] = chunkData[i];
			}
			
			chunkData2[chunkData.length] = 0;
			chunkData = chunkData2.clone();
		}
		
		ENGINE.getGLContext()
			.addBufferBind(verticeBuffer, GL_ARRAY_BUFFER, chunkData[0], GL_DYNAMIC_DRAW, 'f');
		
		ENGINE.getGLContext()
			.addBufferBind(texDataBuffer, GL_ARRAY_BUFFER, chunkData[1], GL_DYNAMIC_DRAW, 'f');

		ENGINE.getGLContext()
			.addBufferBind(lightBuffer, GL_ARRAY_BUFFER, chunkData[2], GL_DYNAMIC_DRAW, 'f');
		
		ENGINE.getGLContext()
			.addBufferBind(normalBuffer, GL_ARRAY_BUFFER, chunkData[3], GL_DYNAMIC_DRAW, 'f');
	    
	    chunkData[4] = vertexCount;
	    
	    return chunkData;
	}
	
	protected static List<Float> getLightValue(int x, int y, int z, Cube cube, int metadata, boolean[] visableFaces, Chunk c) {
		List<Float> lightValue = new ArrayList<Float>();
		List<Float> cubeColor = cube.getRender(metadata).getColors(cube.getId(), metadata, visableFaces);
		
		float[] color = c.getLightColor(x, y, z);
		float value = c.getLightValue(x, y, z);
		
		for(int i = 0; i < cubeColor.size(); i += 3) {
			lightValue.add((color[0] + cubeColor.get(i + 0)) * value);
			lightValue.add((color[1] + cubeColor.get(i + 1)) * value);
			lightValue.add((color[2] + cubeColor.get(i + 2)) * value);
			lightValue.add(1f);
		}
		
		return lightValue;
	}
	
	private static boolean[] getVisableFaces(int x, int y, int z, Chunk c) {
		boolean[] visableFaces = new boolean[6];		
		int metadata = c.getMetadata(x, y, z);
		Cube cube = Cube.getCubeByID(c.getCube(x, y, z));
		
		if(!cube.isVisable(metadata)) {
			return visableFaces;
		}
		
		Cube[] surroundingCubes = c.getSurroundingCubes(x, y, z);
			
		for(Side side : Side.values()) {
			int opposite = side.getOpposite().index();
			visableFaces[side.index()] = surroundingCubes[opposite] == null || !surroundingCubes[opposite].isVisable(metadata);
			
			if(!visableFaces[side.index()]) {
				if(cube.isLiquid(metadata)) {
					visableFaces[side.index()] = surroundingCubes[opposite].isLiquid(metadata) && surroundingCubes[opposite] != cube;
				} else if(surroundingCubes[opposite].isLiquid(metadata)) {
					visableFaces[side.index()] = true;
				}
			}
		}
		
//		visableFaces[0] = surroundingCubes[2] != null ? !surroundingCubes[2].isVisable(metadata) || surroundingCubes[2].getOpacity(metadata) < 1 : true; // 0 -x			1				z
//		visableFaces[1] = surroundingCubes[3] != null ? !surroundingCubes[3].isVisable(metadata) || surroundingCubes[3].getOpacity(metadata) < 1 : true; // 1 +z		0	C	2		-x	c	x
//		visableFaces[2] = surroundingCubes[0] != null ? !surroundingCubes[0].isVisable(metadata) || surroundingCubes[0].getOpacity(metadata) < 1 : true; // 2 +x			3			   -z
//		visableFaces[3] = surroundingCubes[1] != null ? !surroundingCubes[1].isVisable(metadata) || surroundingCubes[1].getOpacity(metadata) < 1 : true; // 3 -z					4			+y
//		visableFaces[4] = surroundingCubes[5] != null ? !surroundingCubes[5].isVisable(metadata) || surroundingCubes[5].getOpacity(metadata) < 1 : true; // 4 +y					C			 c
//		visableFaces[5] = surroundingCubes[4] != null ? !surroundingCubes[4].isVisable(metadata) || surroundingCubes[4].getOpacity(metadata) < 1 : true; // 5 -y					5			-y
		
		return visableFaces;
	}
}
