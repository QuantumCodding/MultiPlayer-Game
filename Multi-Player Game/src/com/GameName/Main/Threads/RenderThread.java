package com.GameName.Main.Threads;

import com.GameName.Render.RenderEngine;

public class RenderThread extends Timer {

	private RenderEngine renderEngine;
	
	public RenderThread(int tickRate, RenderEngine renderEngine) {
		super(tickRate, "Render Thread");
		
		this.renderEngine = renderEngine;
	}

	void init() {

	}

	void tick() {
		renderEngine.render2D();
		renderEngine.render3D();
	}

}
