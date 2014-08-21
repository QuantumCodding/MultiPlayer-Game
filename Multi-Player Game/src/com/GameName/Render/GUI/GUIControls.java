package com.GameName.Render.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class GUIControls extends GUI {

	private final float HEIGHT = 196;
	private final float WIDTH = 100;	
	
	private final float END_X = 100;	
	private final float END_Y = 100;
	private final float START_X = 0;
	private final float START_Y = 0;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	@SuppressWarnings("unused")
	private float width, height;
	private float xSpeed, ySpeed;
	
	private Texture[] textureID;
	
	public GUIControls() {
		textureID = new Texture[1];
		genTextures();
		
		setExtraRenders();		
		
		updateSize(Display.getWidth(), Display.getHeight());
		updateAll();
		
		isOpen = false;
	}
	
	public void updateSize(int orgX, int orgY) {
		float ratioX = (float) Display.getWidth() / (float) orgX;
		float ratioY = (float) Display.getHeight() / (float) orgY;
		
		endX = END_X * ratioX;
		endY = END_Y * ratioY;
		
		startX = START_X * ratioX;
		startY = START_Y * ratioY;
		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
		
		xSpeed = X_SPEED * ratioX;
		ySpeed = Y_SPEED * ratioY;
		
		for(GUIComponent component : comp) {
			component.setGui(this);
			component.updateSize(ratioX, ratioY);
		}
	}

	public void updateAll() {
		if(endX > startX ? x < endX : x > endX) { x += xSpeed; for(GUIComponent component : comp) component.x += xSpeed; }
		if(endY > startY ? y < endY : y > endY) { y += ySpeed; for(GUIComponent component : comp) component.y += ySpeed; } 		
		
		for(GUIComponent component : comp) {
			component.update();
		}
	}

	protected void actions(int id) {

	}
	
	private void setExtraRenders() {
		for(GUIComponent component : comp) {
			switch(component.getId()) {
				case 0: component.setUpFont(20, 0, Color.BLUE); break;
			}
		}
	}

	private void genTextures() {
		textureID[0] = null;
	}	
}
