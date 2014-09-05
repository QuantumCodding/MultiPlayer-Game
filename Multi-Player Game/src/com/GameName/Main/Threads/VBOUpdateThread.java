package com.GameName.Main.Threads;

import com.GameName.World.World;

public class VBOUpdateThread extends GameThread {

	private World world;
	
	public VBOUpdateThread(int tickRate, World world) {
		super(tickRate, "VBO Thread");
		this.world = world;
	}

	void init() {
		
	}	

	void tick() {
		world.updataChunks();
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
