package com.GameName.Render.GUI;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;

import static org.lwjgl.opengl.GL11.*;

public class GUI_StatusBar extends GUI {
	private final float WIDTH = 303;
	private final float HEIGHT = 111 - 52;		
	
	private final float END_X = 0;	
	private final float END_Y = 0;
	private final float START_X = 0;
	private final float START_Y = 0;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
	private Texture[] textureID;
	
	private float ratioX = 1;
	private float ratioY = 1;
	
	public GUI_StatusBar() {
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
				glTexCoord2f(0, r * 52);        glVertex2f(x, y);
				glTexCoord2f(r * 303, r * 52);  glVertex2f(x + width, y);
				glTexCoord2f(r * 303, r * 111); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 111);       glVertex2f(x, y + height);
				
				//Mana Bar
				glTexCoord2f(r * 247, r * 113); glVertex2f(x + (64 * ratioX), y + (9 * ratioY));
				glTexCoord2f(r * 478, r * 113); glVertex2f(x + (64 * ratioX) + ((float)((231f * ratioX) / (float)GameName.player.getAccess().getMaxMana()) * GameName.player.getAccess().getMana() * ratioX), y + (9 * ratioY));
				glTexCoord2f(r * 478, r * 125); glVertex2f(x + (64 * ratioX) + ((float)((231f * ratioX) / (float)GameName.player.getAccess().getMaxMana()) * GameName.player.getAccess().getMana() * ratioX), y + (9 * ratioY) + (12 * ratioY));
				glTexCoord2f(r * 247, r * 125); glVertex2f(x + (64 * ratioX), y + (9 * ratioY) + (12 * ratioY));
				
				//Health Bar
				glTexCoord2f(r * 247, r * 128); glVertex2f(x + (64 * ratioX), y + (24 * ratioY));
				glTexCoord2f(r * 478, r * 128); glVertex2f(x + (64 * ratioX) + ((float)((231f * ratioX) / (float)GameName.player.getAccess().getMaxHealth()) * GameName.player.getAccess().getHealth() * ratioX), y + (24 * ratioY));
				glTexCoord2f(r * 478, r * 140); glVertex2f(x + (64 * ratioX) + ((float)((231f * ratioX) / (float)GameName.player.getAccess().getMaxHealth()) * GameName.player.getAccess().getHealth() * ratioX), y + (24 * ratioY) + (12 * ratioY));
				glTexCoord2f(r * 247, r * 140); glVertex2f(x + (64 * ratioX), y + (24 * ratioY) + (12 * ratioY));
				
				//Hunger Bar
				glTexCoord2f(r * 247, r * 144); glVertex2f(x + (64 * ratioX), y + (40 * ratioY));
				glTexCoord2f(r * 478, r * 144); glVertex2f(x + (64 * ratioX) + ((float)((231f * ratioX) / (float)GameName.player.getAccess().getMaxHunger()) * GameName.player.getAccess().getHunger() * ratioX), y + (40 * ratioY));
				glTexCoord2f(r * 478, r * 155); glVertex2f(x + (64 * ratioX) + ((float)((231f * ratioX) / (float)GameName.player.getAccess().getMaxHunger()) * GameName.player.getAccess().getHunger() * ratioX), y + (40 * ratioY) + (12 * ratioY));
				glTexCoord2f(r * 247, r * 155); glVertex2f(x + (64 * ratioX), y + (40 * ratioY) + (12 * ratioY));				
			glEnd();
			
			glDisable(GL_TEXTURE_2D);

			super.render();			
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Main_HUD_Parts");		
	}
}
