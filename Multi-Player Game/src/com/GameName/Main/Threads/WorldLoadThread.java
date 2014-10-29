package com.GameName.Main.Threads;

import com.GameName.Main.GameName;
import com.GameName.Main.Debugging.Logger;
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
		lastChunk = new Vector3f(-1, -1, -1);
	}

	void tick() {
//		System.out.println(GameName.player.getAccess().getChunk() + 
//				(lastChunk != null ? (!lastChunk.equals(GameName.player.getAccess().getChunk()) ? " == " : " != ") + lastChunk.valuesToString() : ""));
		
		if(lastChunk != null && !lastChunk.equals(GameName.player.getAccess().getChunk())) {
			Logger.println("Updating World through the thread");
			world.getLoadedWorld().getAccess().setCenter(GameName.player.getAccess().getChunk());
			world.getLoadedWorld().getAccess().getChunkLoaded().update();
		}
		
		lastChunk = GameName.player.getAccess().getChunk();
		if(lastChunk != null) lastChunk.setY(world.getSizeY() - lastChunk.getY());
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
