package com.GameName.GUI.Components;

import com.GameName.Engine.GameEngine;

public class GUIRadioButton extends GUIComponent {

	private boolean state;
	private boolean prvClickStart;
	
	public GUIRadioButton(GameEngine eng, int id, float x, float y, float width, float height) {
		super(eng, id, x, y, width, height);
	}
	
	public void activate() {
		state = !state;
		super.activate();
	}
	
	public void update() {
		if(ENGINE.getPlayer().getAccess().isPointerDown() != prvClickStart) {
			prvClickStart = ENGINE.getPlayer().getAccess().isPointerDown();
			super.update();
		}
	}
	
	public boolean getState() {
		return state;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
}
