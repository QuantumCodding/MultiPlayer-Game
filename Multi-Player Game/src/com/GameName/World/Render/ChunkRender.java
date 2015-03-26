package com.GameName.World.Render;

//import java.io.File;
import java.util.ArrayList;

import com.GameName.Cube.Cube;
import com.GameName.Cube.Render.CubeRenderUtil;
import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties.RenderPropertiesBuilder;
import com.GameName.Render.Effects.TextureMap;
import com.GameName.Render.Types_2.Render3D;
import com.GameName.World.Chunk;

public class ChunkRender extends Render3D {
	private Chunk chunk;
	private boolean hasCubes;
	private boolean newRenders;
	private TextureMap textureMap;
	
	private ArrayList<ChunkRenderSection> sections;
	
	public ChunkRender(GameEngine eng, Chunk chunk) {
		super(eng);
		this.chunk = chunk;
		textureMap = new TextureMap(ENGINE, false);
		
		sections = new ArrayList<>();
		setRenderProperties(new RenderPropertiesBuilder(getRenderProperties()).enableTexture3D().build());
	}
	
//	private boolean isSaved = false;
	public void preformRender() {
		if(!hasCubes) return;
		
//		if(!isSaved) { //					     frcAluminati
//			textureMap.getTextureMap().save(new File("C:\\Users\\user\\Desktop\\3D Texture Tests\\Chunks\\" + 
//					chunk.getX() + "-" + chunk.getY() + "-" + chunk.getZ() + "\\TextureMap")); isSaved = true;
//		}
		
		if(newRenders) {
			for(ChunkRenderSection section : sections)
				ENGINE.add(section);
		
			newRenders = false;
		}
	}

	public void buildRender() {
		for(ChunkRenderSection section : sections) {
			section.cleanUp();
		}
		
		sections = ChunkRenderGenerator.generateChunkSectors(ENGINE, chunk);
		newRenders = true;
	}
	
	public ArrayList<ChunkRenderSection> getSections() {
		return sections;
	}
	
	public Chunk getChuck() { return chunk; }
	public boolean hasCubes() { return hasCubes; }
	public TextureMap getTextureMap() { return textureMap; }
	
	public void addCubes(Cube... cubes) {
		hasCubes = true;
		for(Cube cube : cubes) {
			CubeRenderUtil.addCubeToTextureMap(cube, textureMap);
		}
	}
}
