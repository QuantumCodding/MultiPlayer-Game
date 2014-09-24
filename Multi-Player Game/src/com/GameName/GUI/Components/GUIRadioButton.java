package com.GameName.GUI.Components;

import com.GameName.Main.GameName;

public class GUIRadioButton extends GUIComponent {

	private boolean state;
	private boolean prvClickStart;
	
	public GUIRadioButton(int id, float x, float y, float width, float height) {
		super(id, x, y, width, height);
	}
	
	public void activate() {
		state = !state;
		super.activate();
	}
	
	public void update() {
		if(GameName.player.getAccess().isPointerDown() != prvClickStart) {
			prvClickStart = GameName.player.getAccess().isPointerDown();
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
