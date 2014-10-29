package com.GameName.Main.Threads;

import java.util.ArrayList;

import com.GameName.Render.Types.Renderable;
import com.GameName.World.World;

public class VBOUpdateThread extends GameThread {

	private World world;
	private boolean forceUpdate;
	private ArrayList<Renderable> needUpdate;
	
	public VBOUpdateThread(int tickRate, World world) {
		super(tickRate, "VBO Thread");
		this.world = world;
		needUpdate = new ArrayList<Renderable>();
	}

	void init() {
		
	}	

	void tick() {
		if(forceUpdate) {
			world.forceChunkUpdate();
			forceUpdate = false;
		}
		
		for(int i = 0;i < needUpdate.size(); i ++) {
			if(!needUpdate.get(i).isVboUpdateNeeded()) return;
			
			if(needUpdate.get(i).needsVBOids()) {
				needUpdate.get(i).genVBOids();
			}
			
			needUpdate.get(i).updateVBOs();
			needUpdate.get(i).stopVboUpdating();
		}
		
		needUpdate.clear();
//		world.updataChunks();
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public void addRenderable(Renderable render) {
		needUpdate.add(render);
	} 
	
	public void forceUpdate() {
		forceUpdate = true;
	}
}
