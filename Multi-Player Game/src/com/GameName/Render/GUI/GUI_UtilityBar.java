package com.GameName.Render.GUI;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class GUI_UtilityBar extends GUI {
	private final float HEIGHT = 51;
	private final float WIDTH = 470;	
	
	private final float END_X = 190;	
	private final float END_Y = 543;
	private final float START_X = 190;
	private final float START_Y = 543;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
	private Texture[] textureID;
	
	public GUI_UtilityBar() {		
		textureID = new Texture[1];
		genTextures();
		
		comp = new GUIComponent[]{
				new GUISlot(0, END_X + 11 + (52 * 0), END_Y + 6),
				new GUISlot(1, END_X + 11 + (52 * 1), END_Y + 6),
				new GUISlot(2, END_X + 11 + (52 * 2), END_Y + 6),
				new GUISlot(3, END_X + 11 + (52 * 3), END_Y + 6),
				new GUISlot(4, END_X + 11 + (52 * 4), END_Y + 6),
				new GUISlot(5, END_X + 11 + (52 * 5), END_Y + 6),
				new GUISlot(6, END_X + 11 + (52 * 6), END_Y + 6),
				new GUISlot(7, END_X + 11 + (52 * 7), END_Y + 6),
				new GUISlot(8, END_X + 11 + (52 * 8), END_Y + 6)
			};
		
		setExtraRenders();		
		
		updateSize(Display.getWidth(), Display.getHeight());
		updateAll();
		
		isOpen = false;
	}
	
	public void updateSize(int orgX, int orgY) {
		float ratioX = (float)(Display.getWidth() / orgX);
		float ratioY = (float)(Display.getHeight() / orgY);
		
		endX = END_X * ratioX;
		endY = END_Y * ratioY;
		
		startX = START_X * ratioX;
		startY = START_Y * ratioY;
		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
		
		xSpeed = X_SPEED * ratioX;
		ySpeed = Y_SPEED * ratioY;
		
		for(GUIComponent component : comp) {
			component.updateSize(ratioX, ratioY);
		}
	}

	public void open() {
		x = START_X;
		y = START_Y;
		
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
		switch(id) {		
			default: break;
		}
	}
	
	private void setExtraRenders() {
		for(GUIComponent component : comp) {
			switch(component.getId()) {
			}
		}
	}
	
	public void render() {		
		glPushMatrix();		
			glEnable(GL_TEXTURE_2D);
			textureID[0].bind();
			
			glBegin(GL_QUADS);
				float r = 1f / (float)textureID[0].getImageHeight();
				glColor3f(1, 1, 1);
				
				glTexCoord2f(0, 0);            glVertex2f(x, y);
				glTexCoord2f(r * 470, 0);      glVertex2f(x + width, y);
				glTexCoord2f(r * 470, r * 51); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 51);       glVertex2f(x, y + height);
			glEnd();
			
			glDisable(GL_TEXTURE_2D);
			
			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Main_HUD_Parts");
		
	}	
}
