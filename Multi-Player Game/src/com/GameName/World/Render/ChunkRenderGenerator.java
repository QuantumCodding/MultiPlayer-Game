package com.GameName.World.Render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.GameName.Cube.Cube;
import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties;
import com.GameName.Util.BufferUtil;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class ChunkRenderGenerator {
	
	public static ArrayList<ChunkRenderSection> generateChunkSectors(GameEngine ENGINE, Chunk c) {
		HashMap<RenderProperties, ArrayList<Vector3f>> sectionsMap = new HashMap<>();
		
		for(int x = 0; x < c.getSize(); x ++) {
		for(int y = 0; y < c.getSize(); y ++) {
		for(int z = 0; z < c.getSize(); z ++) {
			if(!c.isAccessible()) continue;
			
			Cube cube = Cube.getCubeByID(c.getCube(x, y, z));					
			int metadata = c.getMetadata(x, y, z);			
			if(!cube.isVisable(metadata)) continue;	
			
			RenderProperties key = cube.getRenderProperties(metadata);
			ArrayList<Vector3f> simillarCubes = sectionsMap.get(key);
			if(simillarCubes == null)
				simillarCubes = new ArrayList<>();
			simillarCubes.add(new Vector3f(x, y, z));
			sectionsMap.put(key, simillarCubes);
		}}}
		
		ArrayList<ChunkRenderSection> sections = new ArrayList<>();
		for(RenderProperties properties : sectionsMap.keySet()) {
			sections.add(generateSector(ENGINE, c, properties, sectionsMap.get(properties)));
		}
		
		return sections;
	}
	
	public static ChunkRenderSection generateSector(GameEngine ENGINE, Chunk c, RenderProperties properties, ArrayList<Vector3f> cubes) {
		ArrayList<Float> chunkVertices = new ArrayList<>();		
		ArrayList<Float> chunkTexData = new ArrayList<>();
		ArrayList<Float> chunklightValues = new ArrayList<>();
		ArrayList<Float> chunkNormals = new ArrayList<>();

		int vertexCount = 0;
		int xPos, yPos, zPos;	
		
		for(Vector3f pos : cubes) {
			int x = (int) pos.x, y = (int) pos.y, z = (int) pos.z;
			if(!c.isAccessible()) continue;
			
			xPos = x + (c.getX() * World.CHUNK_SIZE);
			yPos = y + (c.getY() * World.CHUNK_SIZE);
			zPos = z + (c.getZ() * World.CHUNK_SIZE);
			
			Cube cube = Cube.getCubeByID(c.getCube(x, y, z));					
			int metadata = c.getMetadata(x, y, z);
			boolean[] visableFaces = cube.getRender(metadata).getVisableFaces(x, y, z, c);
			
			if(!cube.isVisable(metadata)) continue;	
			
			chunkVertices.addAll(cube.getRender(metadata).getVertices(xPos, yPos, zPos, visableFaces));
			chunkTexData.addAll(cube.getRender(metadata).getTextureCoords(cube.getId(), metadata, c.getTextureMap(), visableFaces));
			chunkNormals.addAll(cube.getRender(metadata).getNormals(cube.getId(), metadata, visableFaces));				
			chunklightValues.addAll(getLightValue(x, y, z, cube, metadata, visableFaces, c));
			
			vertexCount += cube.getRender(metadata).getVerticeCount(visableFaces);
		}
		
		FloatBuffer verticeBuffer = BufferUtil.createFillipedFloatBuffer(chunkVertices);
		FloatBuffer texDataBuffer = BufferUtil.createFillipedFloatBuffer(chunkTexData);
		FloatBuffer lightBuffer = BufferUtil.createFillipedFloatBuffer(chunklightValues);
		FloatBuffer normalBuffer = BufferUtil.createFillipedFloatBuffer(chunkNormals);
		
		ChunkRenderSection renderSection = new ChunkRenderSection(ENGINE, properties, (ChunkRender) c.getRender());
		renderSection.generateBufferIds();
		
		int[] bufferIDs = renderSection.getBufferIds();
		
		ENGINE.getGLContext()
			.addBufferBind(verticeBuffer, GL_ARRAY_BUFFER, bufferIDs[0], GL_STATIC_DRAW, 'f');
		
		ENGINE.getGLContext()
			.addBufferBind(texDataBuffer, GL_ARRAY_BUFFER, bufferIDs[1], GL_STATIC_DRAW, 'f');

		ENGINE.getGLContext()
			.addBufferBind(lightBuffer, GL_ARRAY_BUFFER, bufferIDs[2], GL_STATIC_DRAW, 'f');
		
		ENGINE.getGLContext()
			.addBufferBind(normalBuffer, GL_ARRAY_BUFFER, bufferIDs[3], GL_STATIC_DRAW, 'f');
	    
		bufferIDs[4] = vertexCount;
	    
	    return renderSection;
	}
	
	protected static List<Float> getLightValue(int x, int y, int z, Cube cube, int metadata, boolean[] visableFaces, Chunk c) {
		List<Float> lightValue = new ArrayList<Float>();
		List<Float> cubeColor = cube.getRender(metadata).getColors(cube.getId(), metadata, visableFaces);
		
//		float[] colordasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasanidasani = c.getLightColor(x, y, z);
//		float value = c.getLightValue(x, y, z);
		
		for(int i = 0; i < cubeColor.size(); i += 3) {
			lightValue.add(1f); lightValue.add(1f); lightValue.add(1f);
			
//			lightValue.add((color[0] + cubeColor.get(i + 0)) * value);
//			lightValue.add((color[1] + cubeColor.get(i + 1)) * value);
//			lightValue.add((color[2] + cubeColor.get(i + 2)) * value);
			lightValue.add(1f);
		}
		
		return lightValue;
	}
}