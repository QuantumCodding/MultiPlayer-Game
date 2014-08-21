package com.GameName.Render.GUI;

import java.awt.Color;
import com.GameName.Render.GUI.GUIComponent;

public class GUIText extends GUIComponent {
	
	public GUIText(int id, String text, float x, float y, Color c, int affect, int size) {
		super(id, text, x, y);
		
		this.x = x;
		this.y = y;
		
		setUpFont(size, affect, c);
	}

	public void render() {
		font.drawString(x, y, text);
	}

	public void updateSize(float ratioX, float ratioY) {}
	public void setText(String text) {
		this.text = text;
	}
}
