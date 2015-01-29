package com.GameName.GUI.Components;

import com.GameName.Engine.GameEngine;

public class GUIButton extends GUIComponent {

	private boolean onlyOnce;
	private boolean isDown;
	
	protected GUIButton(GameEngine eng, int id, float x, float y, float width, float height, boolean onlyOnce) {
		super(eng, id, x, y, width, height);
		this.onlyOnce = onlyOnce;
	}
	
	public void update() {
		if(!onlyOnce) {
			super.update();
			return;
		}
		
		if(isDown != ENGINE.getPlayer().isPointerDown()) {
			super.update();
		}
		
		isDown = ENGINE.getPlayer().isPointerDown();
	}
}
