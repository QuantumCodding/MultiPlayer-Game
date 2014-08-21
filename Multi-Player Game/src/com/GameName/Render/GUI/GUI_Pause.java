package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;
import com.GameName.Render.GUI.GUI;
import com.GameName.Render.GUI.GUIButton;

public class GUI_Pause extends GUI {

	private final float HEIGHT = 187.5f;
	private final float WIDTH = 401;	
	
	private final float END_X = 209;	
	private final float END_Y = 155;
	private final float START_X = 209;
	private final float START_Y = 155;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	@SuppressWarnings("unused")
	private float width, height;
	private float xSpeed, ySpeed;
	
	private Texture[] textureID;
	
	public GUI_Pause() {
		textureID = new Texture[1];
				
		comp = new GUIComponent[] {
				new GUIButton(0, "", 209, 155, 401, 54, true), 
				new GUIButton(1, "", 209, 221, 401, 54, true),
				new GUIButton(2, "", 209, 288, 401, 54, true),
			};
		
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

	public void open() {
		x = START_X;
		y = START_Y;
		
		for(GUIComponent component : comp) component.reset();
		
		GameName.lockMovement = true;
		isOpen = true;
	}

	public void updateAll() {
		if(endX > startX ? x < endX : x > endX) { x += xSpeed; for(GUIComponent component : comp) component.x += xSpeed; }
		if(endY > startY ? y < endY : y > endY) { y += ySpeed; for(GUIComponent component : comp) component.y += ySpeed; } 		
		
		for(GUIComponent component : comp) {
			component.update();
		}
	}
	
	protected void actions(int id) {
		try {
			switch(id) {	
				case 0: 
					GameName.guiManager.accessByName("Pause").close();
					GameName.lockMovement = false;
				break;
					
				case 1: 
					GameName.guiManager.accessByName("Pause").close();
//					GameName.guiManager.accessByName("Setting").open();
					GameName.guiManager.accessByName("Controls Config").open();
				break;
					
				case 2: 
					GameName.guiManager.accessByName("Pause").close();
					GameName.lockMovement = false;
				break;
					
				default: break;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
	
	private void setExtraRenders() {
		for(GUIComponent component : comp) {
			switch(component.getId()) {
			
			}
		}
	}
	
	public void render() {		
		glPushMatrix();	
			glDisable(GL_TEXTURE_2D);	
			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Pause_Menu_Parts");
		
		GUIButton b = (GUIButton) comp[0];
		int[] array = new int[] {0, 0,0, 401,0, 401,54,  0,54};
		int[] array2 = new int[] {0, 0,165, 401,165, 401,219,  0,219};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array2, array2);
		
		b = (GUIButton) comp[1];
		array = new int[] {0, 0,55, 401,55, 401,109,  0,109};
		array2 = new int[] {0, 0,220, 401,220, 401,274,  0,274};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array2, array2);
		
		b = (GUIButton) comp[2];
		array = new int[] {0, 0,110, 401,110, 401,164,  0,164};
		array2 = new int[] {0, 0,275, 401,275, 401,329,  0,329};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array2, array2);
	}

}
