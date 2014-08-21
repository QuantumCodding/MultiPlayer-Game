package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;
import com.GameName.Render.GUI.GUIComponent;

public class GUIRadioButton extends GUIComponent {
	
	private float WIDTH;
	private float HEIGHT;
	
	private Texture normal;
	private Texture active;

	private int[] normalC;
	private int[] activeC;
	
	private boolean state;
	private boolean prvClickStart;
	
	public GUIRadioButton(int id, String text, float x, float y, float width, float height) {
		super(id, text, x, y);
		
		this.x = x;
		this.y = y;
		this.HEIGHT = width;
		this.WIDTH = height;
	}
	
	public GUIRadioButton(int id, String text, float x, float y) {
		this(id, text, x, y, 25, 25);
	}
	
	public void render() {
		if(state) {
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
				glColor3f(0, 1, 0);
				glRectf(x, y, x + width, y + height);
			}
		} else {
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
				glColor3f(1f, 0f, 0f);
				glRectf(x, y, x + width, y + height);
			}
		}
				
		font.drawString(x + width, y, text);
		glDisable(GL_TEXTURE_2D);	
		
	}

	public void updateSize(float ratioX, float ratioY) {		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
	}
	
	public void setUpImages(Texture n, Texture s, int[] nC, int[] sC) {normal = n; normalC = nC; active = s; activeC = sC;}
	public void setUpImages(Texture n, Texture s){setUpImages(n, s, new int[]{0,0, 1,0, 1,1, 0,1}, new int[]{0,0, 1,0, 1,1, 0,1});}

	protected boolean getState() {
		return state;
	}
	
	protected void setState(boolean state) {
		this.state = state;
	}
	
	public void activate() {
		state = !state;
		super.activate();
	}
	
	public void update() {
		if(GameName.click != prvClickStart) {
			prvClickStart = GameName.click;
			super.update();
		}
	}
}
