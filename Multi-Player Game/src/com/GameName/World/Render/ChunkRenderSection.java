package com.GameName.World.Render;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties;
import com.GameName.Render.Types_2.Render3D;

public class ChunkRenderSection extends Render3D {
	private ChunkRender chunkRender;
	
	public ChunkRenderSection(GameEngine eng, RenderProperties properties, ChunkRender chunkRender) {
		super(eng);
		
		this.chunkRender = chunkRender;
		setRenderProperties(properties);
		setTexture(chunkRender.getTextureMap().getTextureMap());
	}
	
	public void buildRender() {}	
	public void generateBufferIds() {
		super.generateBufferIds();
		areBufferIdsGenerated = true;
	}
	
	public int[] getBufferIds() {
		return bufferIds;
	}
	
	public ChunkRender getChunkRender() {
		return chunkRender;
	}
}
