package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;
import com.GameName.Render.GUI.GUIComponent;

public class GUIButton extends GUIComponent {
	
	private float WIDTH;
	private float HEIGHT;
	
	private boolean once;
	private boolean prevState;
	
	private Texture normal;
	private Texture selected;
	private Texture active;

	private int[] normalC;
	private int[] selectedC;
	private int[] activeC;
	
	public GUIButton(int id, String text, float x, float y, float width, float height) {
		super(id, text, x, y);
		
		this.x = x;
		this.y = y;
		this.HEIGHT = height;
		this.WIDTH = width;
		
		once = false;
	}
	
	public GUIButton(int id, String text, float x, float y, float width, float height, boolean once) {
		super(id, text, x, y);
		
		this.x = x;
		this.y = y;
		this.HEIGHT = height;
		this.WIDTH = width;
		
		this.once = once;
	}
	
	public void render() {
		if(!isSelected()) {
			if(normal != null) {
				glEnable(GL_TEXTURE_2D);
				
				float r = 1f / (float)normal.getImageHeight();
				glColor3f(1, 1, 1);
				
				normal.bind();
				
				glBegin(GL_QUADS);
					glTexCoord2f(r * normalC[1], r * normalC[2]); glVertex2f(x, y);
					glTexCoord2f(r * normalC[3], r * normalC[4]); glVertex2f(x + width, y);
					glTexCoord2f(r * normalC[5], r * normalC[6]); glVertex2f(x + width, y + height);
					glTexCoord2f(r * normalC[7], r * normalC[8]); glVertex2f(x, y + height);
				glEnd();
				
				glDisable(GL_TEXTURE_2D);
			} else {
				glColor3f(1, 1, 1);
				glRectf(x, y, x + width, y + height);
			}
		} else if(!GameName.click) {
			if(selected != null) {
				glEnable(GL_TEXTURE_2D);
				
				float r = 1f / (float)selected.getImageHeight();
				glColor3f(1, 1, 1);
				
				selected.bind();
				
				glBegin(GL_QUADS);
					glTexCoord2f(r * selectedC[1], r * selectedC[2]); glVertex2f(x, y);
					glTexCoord2f(r * selectedC[3], r * selectedC[4]); glVertex2f(x + width, y);
					glTexCoord2f(r * selectedC[5], r * selectedC[6]); glVertex2f(x + width, y + height);
					glTexCoord2f(r * selectedC[7], r * selectedC[8]); glVertex2f(x, y + height);
				glEnd();
				
				glDisable(GL_TEXTURE_2D);
			} else { 
				glColor3f(0.5f, 0.5f, 0.5f);
				glRectf(x, y, x + width, y + height);
			}
		} else {
			if(active != null) {
				glEnable(GL_TEXTURE_2D);
				
				float r = 1f / (float)active.getImageHeight();
				glColor3f(1, 1, 1);
				
				active.bind();
				
				glBegin(GL_QUADS);
					glTexCoord2f(r * activeC[1], r * activeC[2]); glVertex2f(x, y);
					glTexCoord2f(r * activeC[3], r * activeC[4]); glVertex2f(x + width, y);
					glTexCoord2f(r * activeC[5], r * activeC[6]); glVertex2f(x + width, y + height);
					glTexCoord2f(r * activeC[7], r * activeC[8]); glVertex2f(x, y + height);
				glEnd();
				
				glDisable(GL_TEXTURE_2D);
			} else { 
				glColor3f(1f, 0f, 0f);
				glRectf(x, y, x + width, y + height);
			}
		}
				
		font.drawString(x, y, text);
		glDisable(GL_TEXTURE_2D);	
		
	}

	public void update() {
		if(!once) {
			super.update();
			return;
		}
		
		if(prevState != GameName.click) {
			super.update();
		}
		
		prevState = GameName.click;
	}
	
	public void updateSize(float ratioX, float ratioY) {		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
	}
	
	public void setUpImages(Texture n, Texture s, Texture a, int[] nC, int[] sC, int[] aC) {normal = n; selected = s; active = a; normalC = nC; selectedC = sC; activeC = aC;}
}
