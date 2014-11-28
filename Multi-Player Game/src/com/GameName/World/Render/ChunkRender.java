package com.GameName.World.Render;

import static org.lwjgl.opengl.GL11.glTranslatef;

import com.GameName.Cube.Render.CubeTextureMap;
import com.GameName.Main.GameName;
import com.GameName.Render.Types.Render3D;
import com.GameName.World.Chunk;
import com.GameName.World.World;
import com.GameName.World.WorldRegistry;

public class ChunkRender extends Render3D {
	private Chunk chunk;
	private boolean hasCubes;
	
	private CubeTextureMap textureMap;
	
	public ChunkRender(Chunk chunk) {
		super();
		this.chunk = chunk;
	}
	
	public void draw() {
		if(!hasCubes) return;
			
		glTranslatef(0, -(WorldRegistry.getWorld(chunk.getWorldId()).getSizeY() * (World.SCALE * 0.1f)), 0);
		super.draw();			
	}

	public void updateVBOs() {		
		if(vertexVBO == -1) {
			genVBOids();
		}
		
		int[] ids = ChunkRenderGenerator.generateChunk(chunk, new int[] {vertexVBO, textureVBO, colorVBO, normalVBO, 0});
		
		vertexVBO = ids[0];
		textureVBO = ids[1];
		colorVBO = ids[2];
		normalVBO = ids[3];
		
		vertexCount = ids[4];

		vboUpdateNeeded = false;
	}

	public void genVBOids() {
		int[] ids = GameName.getGLContext().genBufferIds(4);

		this.vertexVBO = ids[0];
		this.textureVBO = ids[1];
		this.colorVBO = ids[2];
		this.normalVBO = ids[3];
		
		needsVBOids = false;
	}

	protected void cleanUp_Render3D() {

	}
	
	public CubeTextureMap getTextureMap() {
		return textureMap;
	}

	public void setTextureMap(CubeTextureMap textureMap) {
		this.textureMap = textureMap;
		setTexture(textureMap.getTexture());
	}

	public void setHasCubes(boolean hasCubes) {
		this.hasCubes = hasCubes;
	}
}
