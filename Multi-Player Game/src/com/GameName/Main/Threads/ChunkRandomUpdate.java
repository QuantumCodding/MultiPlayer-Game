package com.GameName.Main.Threads;

import java.util.Random;

import com.GameName.World.World;

public class ChunkRandomUpdate extends GameThread {
	private World world;
	private Random rand;
	
	public ChunkRandomUpdate(int tickRate, World world) {
		super(tickRate, "Random Update");
		this.world = world;
	}

	void init() {
		rand = new Random();
	}

	void tick() {
		world.getLoadedWorld().getAccess().getChunkLoaded().getLoadedChunks().get(
				rand.nextInt(world.getLoadedWorld().getAccess().getChunkLoaded().getLoadedChunks().size()
			));
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
