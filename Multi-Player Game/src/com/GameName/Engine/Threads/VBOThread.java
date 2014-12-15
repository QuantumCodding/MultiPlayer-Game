package com.GameName.Engine.Threads;

import java.util.ArrayList;

import com.GameName.Render.Types.Renderable;

public class VBOThread extends GameThread {
	private ArrayList<Renderable> needUpdate;
	
	public VBOThread(int tickRate) {
		super(tickRate, "VBO Thread");
		needUpdate = new ArrayList<>();
	}

	void init() {
		
	}	

	void tick() {
		for(int i = 0; i < needUpdate.size(); i ++) {
			if(!needUpdate.get(i).isVboUpdateNeeded()) return;
			
			if(needUpdate.get(i).needsVBOids()) {
				needUpdate.get(i).genVBOids();
			}
			
			needUpdate.get(i).updateVBOs();
			needUpdate.get(i).stopVboUpdating();
		}
		
		needUpdate.clear();
	}
	
	public void addRenderable(Renderable render) {
		needUpdate.add(render);
	} 
}
