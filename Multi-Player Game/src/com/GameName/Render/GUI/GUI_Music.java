package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class GUI_Music extends GUI {
	private final float HEIGHT = 77;
	private final float WIDTH = 265;	
	
	private final float END_X = 308;	
	private final float END_Y = 0;
	private final float START_X = 308;
	private final float START_Y = - 77;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 11f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
	private boolean fOpen;
	
	private Texture[] textureID;
	
	public GUI_Music() {
		textureID = new Texture[1];
		
		comp = new GUIComponent[]{
				new GUIText(0, "", 383 - 10, 3 - 77, Color.YELLOW, 0, 20),
				new GUIButton(1, "", 430 - 15  + 3, 77 - 77, 42, 12),
				
				new GUIButton(2, "", 399 - 15  + 3, 33 - 77, 30, 30, true),
				new GUIButton(3, "", 436 - 15  + 3, 33 - 77, 30, 30, true),
				new GUIButton(4, "", 473 - 15  + 3, 33 - 77, 30, 30, true),
				new GUIButton(5, "", 510 - 15  + 3, 33 - 77, 30, 30, true),
				
				new GUISlideBar(6, 317  + 3, 48 - 77, 15, 5, 39, 1, 0, true),
				new GUISlideBar(7, 336  + 3, 48 - 77, 15, 5, 39, 2, 0, true),
			};
		
		GUISlideBar temp = (GUISlideBar) comp[6];
		GUISlideBar temp2 = (GUISlideBar) comp[7];
		
		temp.setValue(1);
		actions(6);
		temp2.setValue(1);
		actions(7);
		
		genTextures();
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
			component.setGui(this);
			component.updateSize(ratioX, ratioY);
		}
	}

	public void open() {
		x = START_X;
		y = START_Y;
		
		for(GUIComponent component : comp) component.reset();
		
		isOpen = true;
	}

	public void updateAll() {
		if(fOpen) {
			if(endX > startX ? x < endX : x > endX) { x += xSpeed; for(GUIComponent component : comp) {component.x += xSpeed; if(component instanceof GUISlideBar) {GUISlideBar s = (GUISlideBar) component; s.setUseX(s.x);}}}
			if(endY > startY ? y < endY : y > endY) { y += ySpeed; for(GUIComponent component : comp) {component.y += ySpeed; if(component instanceof GUISlideBar) {GUISlideBar s = (GUISlideBar) component; s.setUseY(s.y);}}}	
		}
		
				
		for(GUIComponent component : comp) {
			component.update();
		}
	}
	
	protected void actions(int id) {
		
		try {
			switch(id) {	
				case 1: 
					x = START_X;
					y = START_Y;
					
					for(GUIComponent component : comp) component.reset();
					
					fOpen = !fOpen;
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
			glEnable(GL_TEXTURE_2D);
			textureID[0].bind();
			
			glBegin(GL_QUADS);
				float r = 1f / (float)textureID[0].getImageHeight();
				glColor3f(1, 1, 1);
				
				//BackGround
				glTexCoord2f(0, r * 37);      glVertex2f(x, y);
				glTexCoord2f(r * 280, r * 37); glVertex2f(x + width, y);
				glTexCoord2f(r * 280, r * 114); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 114);      glVertex2f(x, y + height);
				
			glEnd();
			
			glDisable(GL_TEXTURE_2D);		
			
			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Music_GUI_Parts");
		
		GUIButton b = (GUIButton) comp[1];
		int[] array = new int[] {0, 124,0, 166,0, 166,12,  124,12};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array, array);
	
		b = (GUIButton) comp[2];
		array = new int[] {0, 0,0, 30,0, 30,30,  0,30};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array, array);
		
		b = (GUIButton) comp[3];
		array = new int[] {0, 31,0, 61,0, 61,30,  31,30};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array, array);
		
		b = (GUIButton) comp[4];
		array = new int[] {0, 62,0, 92,0, 92,30,  62,30};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array, array);
		
		b = (GUIButton) comp[5];
		array = new int[] {0, 93,0, 123,0, 123,30,  93,30};
		b.setUpImages(textureID[0], textureID[0], textureID[0], array, array, array);
		
		GUISlideBar s = (GUISlideBar) comp[6];
		array = new int[] {0, 0,31, 15,31, 15,36,  0,36};
		s.setUpImages(textureID[0], array);
		
		s = (GUISlideBar) comp[7];
		s.setUpImages(textureID[0], array);
	}	
}
