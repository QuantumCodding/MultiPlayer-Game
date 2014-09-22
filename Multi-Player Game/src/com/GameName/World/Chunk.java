package com.GameName.World;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.util.ArrayList;

import com.GameName.Cube.Cube;
import com.GameName.Main.GameName;
import com.GameName.Render.RenderUtil;
import com.GameName.Render.Effects.Shader;
import com.GameName.Render.Effects.Texture;
import com.GameName.Util.Tag.Tag;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector3f;

public class Chunk {
	private final float MINIMUM_LIGHT_DEFUTION = 0.5f;
	
	private boolean isInitialized;
	private boolean hasCubes, isLoaded;
	private int cubeCount;
	private int[] vboData;
	
	private final int worldId;
	private final int x, y, z;
	
	private int[] cubes;
	private int[] cubeMetadata;
	private final int size;
	
	private float[][] lightColorMap;
	private float[]   lightValueMap;
	private float 	  ambiantLight = (float) ((1d / (double) World.MAX_LIGHT) * World.AMBIANT_LIGHT);
	
	private boolean vboUpdataRequested, extraVBOUpdate;
	
	protected Chunk(int size, int worldId, int x, int y, int z) { 
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
		
		isInitialized = true;
	}
	
	public void update() {
		if(extraVBOUpdate) {
			vboUpdataRequested = true;
			extraVBOUpdate = false;
		}
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
			vboUpdataRequested = true;	return; 
		}
		
		updateLightMap(x - 1, y, z, color, intensity);
		updateLightMap(x + 1, y, z, color, intensity);
		updateLightMap(x, y, z - 1, color, intensity);
		updateLightMap(x, y, z + 1, color, intensity);
	}	
	
	public int getCube(int x, int y, int z) {
		if(!isInitialized) extraVBOUpdate = true;
		return cubes[x + (y * size) + (z * size * size)];
	}
	
	public int getCubeMetadata(int x, int y, int z) {
		if(!isInitialized) extraVBOUpdate = true;
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
		
		vboUpdataRequested = true;
	}
	
	public boolean isPosOnEdge(int x, int y, int z) {
		return x == 0 || y == 0 || z == 0 ||
				x == size - 1 || y == size - 1 || z == size - 1;
	}
	
	public synchronized void updataVBO() {	
		if(hasCubes) {
			vboData = RenderUtil.generateChunk(
					x, y, z, WorldRegistry.getWorld(worldId), vboData);
		}
		
		vboUpdataRequested = false;
	}
	
	public void render() {
//		System.out.println("Chunk " + x + " " + y + " " + z + " in world " + worldId + " rendered");
		if(!hasCubes || vboData == null) return;
		
		glPushMatrix();
			glDisable(GL_LIGHTING);
			
			glEnableVertexAttribArray(0); // Position
			glEnableVertexAttribArray(1); // Texture Data
			glEnableVertexAttribArray(2); // Color
			
		    glEnable(GL_TEXTURE_2D);
			
		    	Cube.getTextureSheet().bind();
		    	GameName.render.basicShader.bind();
	
		        glRotatef(180, 0, 1, 0);
		    	glTranslatef(0, -(WorldRegistry.getWorld(worldId).getSizeY() * (World.SCALE * 0.1f)), 0);
		    	glScalef(World.SCALE, World.SCALE, World.SCALE);
		        	    		
	    			glColor3f((float) Math.random() * 10 % 2 / 10, (float) Math.random() * 10 % 1 / 10, (float) Math.random() * 10 % 1 / 10);//3
		        	
		        	glBindBuffer(GL_ARRAY_BUFFER, vboData[0]);
		        		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);		
		        		
		        	glBindBuffer(GL_ARRAY_BUFFER, vboData[1]);
		        		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		        		
		        	glBindBuffer(GL_ARRAY_BUFFER, vboData[2]);
		        		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
	
		        	glDrawArrays(GL_QUADS, 0, vboData[3]);
	    		
	    		
		        Shader.unbind();
		        Texture.unbind();
		        
		    glDisableVertexAttribArray(0); // Position						
		    glDisableVertexAttribArray(1); // Texture Data
		    glDisableVertexAttribArray(2); // Color
			
		    glDisable(GL_TEXTURE_2D);	                
	    glPopMatrix();
	}
	
//	public void finalize() {
//		cleanUp();
//	}
	
	public void deleteBuffers() {
		if(vboData != null) {
			GameName.getGLContext().deleteBuffer(vboData[0]);
			GameName.getGLContext().deleteBuffer(vboData[1]);
			GameName.getGLContext().deleteBuffer(vboData[2]);
			GameName.getGLContext().deleteBuffer(vboData[3]);
		}
	}
	
	public void cleanUp() {
		if(vboData != null) {		
			glDeleteBuffers(vboData[0]);
			glDeleteBuffers(vboData[1]);
			glDeleteBuffers(vboData[2]);
			glDeleteBuffers(vboData[3]);
		}
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
	
	public boolean isVboUpdataRequested() {
		return vboUpdataRequested;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}

	public boolean hasCubes() {
		return hasCubes;
	}
	
	public int[] getVboData() {
		return vboData;
	}

	public synchronized void setVboData(int[] vboData) {
		this.vboData = vboData;
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
	
	public void forceVBOUpdate() {
		vboUpdataRequested = true;
	}
	
	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}
}
