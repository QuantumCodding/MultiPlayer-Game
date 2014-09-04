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

import com.GameName.Main.GameName;
import com.GameName.Render.RenderUtil;
import com.GameName.Render.Texture;
import com.GameName.Render.Shader.Shader;
import com.GameName.World.Cube.Cube;

public class Chunk {
	private final float MINIMUM_LIGHT_DEFUTION = 0.5f;
	
	private int[] vboData;
	
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
		vboData = RenderUtil.generateChunk(
				x, y, z, GameName.worlds.get(worldId), vboData);
		
		vboUpdataRequested = false;
	}
	
	public void render() {
//		System.out.println("Chunk " + x + " " + y + " " + z + " in world " + worldId + " rendered");
		
		glPushMatrix();
			glDisable(GL_LIGHTING);
			
			glEnableVertexAttribArray(0); // Position
			glEnableVertexAttribArray(1); // Texture Data
			glEnableVertexAttribArray(2); // Color
			
		    glEnable(GL_TEXTURE_2D);
			
		    	Cube.getTextureSheet().bind();
		    	GameName.render.basicShader.bind();
	
		        glRotatef(180, 0, 1, 0);
		    	glTranslatef(0, -(GameName.worlds.get(worldId).getSizeY() * (World.SCALE * 0.1f)), 0);
		    	glScalef(World.SCALE, World.SCALE, World.SCALE);
		        	    		
	    		if(vboData != null) {
	    			glColor3f((float) Math.random(), (float) Math.random(), (float) Math.random());
		        	
		        	glBindBuffer(GL_ARRAY_BUFFER, vboData[0]);
		        		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);		
		        		
		        	glBindBuffer(GL_ARRAY_BUFFER, vboData[1]);
		        		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		        		
		        	glBindBuffer(GL_ARRAY_BUFFER, vboData[2]);
		        		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
	
		        	glDrawArrays(GL_QUADS, 0, vboData[3]);
	    		}
	    		
		        Shader.unbind();
		        Texture.unbind();
		        
		    glDisableVertexAttribArray(0); // Position						
		    glDisableVertexAttribArray(1); // Texture Data
		    glDisableVertexAttribArray(2); // Color
			
		    glDisable(GL_TEXTURE_2D);	                
	    glPopMatrix();
	}
	
	public void cleanUp() {
		glDeleteBuffers(vboData[0]);
		glDeleteBuffers(vboData[1]);
		glDeleteBuffers(vboData[2]);
		glDeleteBuffers(vboData[3]);
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

	public int[] getVboData() {
		return vboData;
	}

	public void setVboData(int[] vboData) {
		this.vboData = vboData;
	}
}
