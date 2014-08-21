package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRectf;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;

public class GUISlideBar extends GUIComponent {
	private float WIDTH;
	private float HEIGHT;	
	private float RUN;
	
	private float useX, useY;
	
	private float run;
	private int max, min;
	private boolean vertical;
	private float value;
	
	private int last = 0;
	private boolean hasFocus;
	
	private Texture normal;
	private int[] normalC;

	public GUISlideBar(int id, float x, float y, float width, float height, float run, int max, int min, boolean vertical) {
		super(id, "", x, y);
		
		WIDTH = width;
		HEIGHT = height;
		RUN = run;
		
		this.max = max;
		this.min = min;
		
		this.vertical = vertical;
		
		setUseX(x);
		setUseY(y);
	}

	public void render() {
//		if(isSelected()) {
//			glColor3f(0, 0, 1);
//			glRectf(getUseX(), getUseY(), getUseX() + width, getUseY() + height);
//			return;
//		}
		
		if(normal != null) {
			glEnable(GL_TEXTURE_2D);
			
			float r = 1f / (float)normal.getImageHeight();
			glColor3f(1, 1, 1);
				
			normal.bind();
				
			glBegin(GL_QUADS);
				glTexCoord2f(r * normalC[1], r * normalC[2]); glVertex2f(getUseX(), getUseY());
				glTexCoord2f(r * normalC[3], r * normalC[4]); glVertex2f(getUseX() + width, getUseY());
				glTexCoord2f(r * normalC[5], r * normalC[6]); glVertex2f(getUseX() + width, getUseY() + height);
				glTexCoord2f(r * normalC[7], r * normalC[8]); glVertex2f(getUseX(), getUseY() + height);
			glEnd();
				
			glDisable(GL_TEXTURE_2D);
			
		} else {
			glColor3f(0, 0, 1);
			glRectf(getUseX(), getUseY(), getUseX() + width, getUseY() + height);
		}
	}

	public void setValue(float v) {
		value = v < max ? v > min ? v : min : max;
		
		if(vertical) {
			setUseY(y - ((float)(run / (float)(max - min)) * value));
			setUseX(x);
		} else {
			setUseX(x + ((run / (float)(max - min)) * value));
			setUseY(y);
		}
	}
	
	public float getValue() {
		return value;
	}
	
	public void updateSize(float ratioX, float ratioY) {		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
		
		if(vertical) {
			run = RUN * ratioY;
		} else {
			run = run * ratioX;
		}
	}
		
	public boolean isSelected() {
		if(GameName.pointer.x > useX && GameName.pointer.x < useX + width)
			if(GameName.pointer.y > useY && GameName.pointer.y < useY + height)
				return true;
		
		return false;
	}
	
	public void update() {		
		if(isSelected() && GameName.click) { if(!hasFocus) last = GameName.pointer.y; hasFocus = true; }
		if(hasFocus && !GameName.click) hasFocus = false;
		
		if(hasFocus) {
//			System.out.println(getId() + ": " + last + ", " + GameName.pointer.y + " / " + value);
			if(vertical) {
				if(last == GameName.pointer.y) {
					return;
				} else {
					int change = (last - GameName.pointer.y);

					setValue(getValue() + ((((float)(max - min)) / run) * (float) change));
				}
				
				last = GameName.pointer.y;
				
			} else {
				if(last == GameName.pointer.x) {
					return;
				} else {
					int change = GameName.pointer.x - last;
					setValue((float)(run / (float)(max - min)) * (float)change);
				}
				
				last = GameName.pointer.x;
			}
			
			activate();
		} else {		
			setValue(getValue());
		}
	}
	
	public void reset() {
		x = startX;
		y = startY;
	}
	
	public float getUseX() {
		return useX;
	}

	public void setUseX(float useX) {
		this.useX = useX;
	}

	public float getUseY() {
		return useY;
	}

	public void setUseY(float useY) {
		this.useY = useY;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setUpImages(Texture n, int[] nC) {normal = n; normalC = nC;}
}

