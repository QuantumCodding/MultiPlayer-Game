package com.GameName.Render.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;

import static org.lwjgl.opengl.GL11.*;

public class GUI_Money extends GUI {
	private final float WIDTH = 246;
	private final float HEIGHT = 60;		
	
	private final float END_X = 554;	
	private final float END_Y = 0;
	private final float START_X = 554;
	private final float START_Y = 0;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
//	private float ratioX = 1;
//	private float ratioY = 1;
	
	private Texture[] textureID;
	
	public GUI_Money() {
		textureID = new Texture[1];
		genTextures();
		
		comp = new GUIComponent[]{
				new GUIText(0, GameName.player.getAccess().getMoney() + "", 618, 12, new Color(218, 128, 0), 0, 30)
			};
			
		
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
		
//		this.ratioX = ratioX;
//		this.ratioY = ratioY;
		
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
		
		GUIText t = (GUIText) comp[0];
		t.setText(resize(12, GameName.player.getAccess().getMoney() + ""));
		
		for(GUIComponent component : comp) {
			component.update();
		}
	}
	
	protected void actions(int id) {
		switch(id) {		
			default: break;
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
				glTexCoord2f(0, r * 113);      glVertex2f(x, y);
				glTexCoord2f(r * 246, r * 113); glVertex2f(x + width, y);
				glTexCoord2f(r * 246, r * 173); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 173);      glVertex2f(x, y + height);
			glEnd();
			
			glDisable(GL_TEXTURE_2D);

			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Main_HUD_Parts");		
	}
	
	private String resize(int size, String in) {
		if(in.length() >= size) return in;
		if(in.contains(".")) if(in.charAt(in.length() - 2) == '.') in += "0";
			
		char[] toRep = new char[size];
		for(int i = 0; i < size - in.length(); i ++) {
			toRep[i] = ' ';
		}
		
		for(int i = size - in.length(); i < size; i ++) {
			toRep[i] = in.charAt(i - (size - in.length()));
		}
		
		return String.valueOf(toRep);
	}
}
