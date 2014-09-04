package com.GameName.Main.Threads;

import com.GameName.Main.GameName;
import com.GameName.Render.RenderUtil;
import com.GameName.World.World;

public class VBOUpdateThread extends GameThread {

	private World world;
	
	public VBOUpdateThread(int tickRate, World world) {
		super(tickRate, "VBO Thread");
		this.world = world;
	}

	void init() {
		System.out.println("Generating World " + world.getId() + ": " + GameName.worlds.get(world.getId()));
		world.setWorldVBO(RenderUtil.generateWorldRender(world));
	}	

	void tick() {
		if(world.checkChunks()) {
			world.updataChunks();
		}
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
