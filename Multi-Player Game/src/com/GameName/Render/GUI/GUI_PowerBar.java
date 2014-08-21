package com.GameName.Render.GUI;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;

import static org.lwjgl.opengl.GL11.*;

public class GUI_PowerBar extends GUI {
	private final float WIDTH = 800 - 740;
	private final float HEIGHT = 600 - 413;		
	
	private final float END_X = 740;	
	private final float END_Y = 413;
	private final float START_X = 740;
	private final float START_Y = 413;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
	private float ratioX = 1;
	private float ratioY = 1;
	
	private Texture[] textureID;
	
	public GUI_PowerBar() {
		textureID = new Texture[1];
		genTextures();
		
		comp = new GUIComponent[]{
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
		
		this.ratioX = ratioX;
		this.ratioY = ratioY;
		
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
	
	public void render() {		
		glPushMatrix();		
			glEnable(GL_TEXTURE_2D);
			textureID[0].bind();
			
			glBegin(GL_QUADS);
				float r = 1f / (float)textureID[0].getImageHeight();
				glColor3f(1, 1, 1);
				
				//BackGround
				glTexCoord2f(0, r * 174);      glVertex2f(x, y);
				glTexCoord2f(r * 60, r * 174); glVertex2f(x + width, y);
				glTexCoord2f(r * 60, r * 361); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 361);      glVertex2f(x, y + height);
				
				//Power Bar
				glTexCoord2f(r * 486, r * 0);   glVertex2f(x + (18 * ratioX), y + (123 * ratioX) - ((float)((117f * ratioY) / 100f) * GameName.player.getAccess().getPower()));
				glTexCoord2f(r * 512, r * 0);   glVertex2f(x + (44 * ratioX), y + (123 * ratioX) - ((float)((117f * ratioY) / 100f) * GameName.player.getAccess().getPower()));
				glTexCoord2f(r * 512, r * 117); glVertex2f(x + (44 * ratioX), y + (123 * ratioX));
				glTexCoord2f(r * 486, r * 117); glVertex2f(x + (18 * ratioX), y + (123 * ratioX));
			glEnd();
			
			glDisable(GL_TEXTURE_2D);

			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Main_HUD_Parts");		
	}
}
