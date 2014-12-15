package com.GameName.World;

import com.GameName.Engine.GameEngine;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Object.WorldObject;

public class LoadedWorld {
	protected static int loadRadius = 10;//3;//5;
	private final GameEngine ENGINE;
	
	private LoadedWorldAccess access;
	protected World world;
		
	protected ChunkLoader chunkLoader;
	protected WorldObject[] objects;
	protected Vector3f center;
	
	public LoadedWorld(GameEngine eng, World world, String worldName) {
		ENGINE = eng;
		
		this.world = world;
//		worldFiles = new File(worldFilesRootDir + worldName);
		objects = new WorldObject[100];
		chunkLoader = new ChunkLoader(ENGINE, world, loadRadius);		
		
		access = new LoadedWorldAccess(this);
		center = chunkLoader.getCenter();
	}
		
	public LoadedWorldAccess getAccess() {
		return access;
	}
	public boolean checkChunks() {
		return chunkLoader.checkChunksVBO();
	}

	public void updataChunks() {
		chunkLoader.updataChunksVBO();
	}
	
	public void forceChunkUpdate() {
		chunkLoader.forceChunkUpdate();
	}
	
	public void cleanUp() {
//		saveLoadedChunks(); //TODO: Determine if this is needed
		
		chunkLoader.cleanUp();
	}
}
