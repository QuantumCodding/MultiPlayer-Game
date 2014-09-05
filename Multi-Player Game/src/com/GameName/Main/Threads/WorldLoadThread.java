package com.GameName.Main.Threads;

import com.GameName.Main.GameName;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class WorldLoadThread extends GameThread {

	private World world;
	private Vector3f lastChunk;
	
	public WorldLoadThread(int tickRate, World world) {
		super(tickRate, "World Thread");
		
		this.world = world;
	}

	void init() {

	}

	void tick() {
//		if(!lastChunk.equals(GameName.player.getAccess().getChunk())) {
//			world.getLoadedWorld().getAccess().setCenter(GameName.player.getAccess().getChunk());
//			world.getLoadedWorld().loadWorld();
//		}
//		
		lastChunk = GameName.player.getAccess().getChunk();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
