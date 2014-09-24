package com.GameName.GUI.Components;

import com.GameName.Main.GameName;

public class GUIButton extends GUIComponent {

	private boolean onlyOnce;
	private boolean isDown;
	
	protected GUIButton(int id, float x, float y, float width, float height, boolean onlyOnce) {
		super(id, x, y, width, height);
		this.onlyOnce = onlyOnce;
	}
	
	public void update() {
		if(!onlyOnce) {
			super.update();
			return;
		}
		
		if(isDown != GameName.player.getAccess().isPointerDown()) {
			super.update();
		}
		
		isDown = GameName.player.getAccess().isPointerDown();
	}
}
