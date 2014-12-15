package com.GameName.Engine.Threads;

import com.GameName.Main.Debugging.Logger;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class WorldLoadThread extends GameThread {

	private World world;
	private Vector3f lastChunk;
	
	public WorldLoadThread(int tickRate) {
		super(tickRate, "World Load Thread");
	}

	void init() {
		lastChunk = new Vector3f(-1, -1, -1);
	}

	void tick() {
		if(lastChunk != null && !lastChunk.equals(ENGINE.getPlayer().getAccess().getChunk())) {
			Logger.println("Updating World through the thread");
			world.getLoadedWorld().getAccess().setCenter(ENGINE.getPlayer().getAccess().getChunk());
			world.getLoadedWorld().getAccess().getChunkLoaded().update();
		}
		
		lastChunk = ENGINE.getPlayer().getAccess().getChunk();
		if(lastChunk != null) lastChunk.setY(world.getSizeY() - lastChunk.getY());
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
