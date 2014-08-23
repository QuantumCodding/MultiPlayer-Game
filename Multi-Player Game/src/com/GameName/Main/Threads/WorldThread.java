package com.GameName.Main.Threads;

import com.GameName.World.World;

public class WorldThread extends Timer {

	private World world;
	
	public WorldThread(int tickRate, World world) {
		super(tickRate, "World Thread");
		
		this.world = world;
	}

	void init() {

	}

	void tick() {

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
