package com.GameName.Main.Threads;

import com.GameName.World.World;

public class ChunkUpdateThread extends GameThread {
	private World world;
	
	public ChunkUpdateThread(int tickRate, World world) {
		super(tickRate, "Chunk Update Thread");
		this.world = world;
	}

	void init() {
		
	}

	void tick() {
		for(int i = 0; i < world.getLoadedWorld().getAccess().getChunkLoaded().getLoadedChunks().size(); i ++) {
			world.getLoadedWorld().getAccess().getChunkLoaded().getLoadedChunks().get(i).update();
		}
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
