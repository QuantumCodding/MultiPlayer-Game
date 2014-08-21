package com.GameName.Render.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Input.Control;
import com.GameName.Main.GameName;

import static org.lwjgl.opengl.GL11.*;

public class GUI_Control extends GUI {
	private final float WIDTH = 593;
	private final float HEIGHT = 70;		
	
	private float X;
	private float Y;
	
	private float endX, endY;
	
	private float x, y;	

	private float width, height;
	
	private float ratioX, ratioY;
		
	private Texture[] textureID;
	private boolean isSelected;
	private boolean waitingForAddClose;
	
	private Control control;
	
	public GUI_Control(float X, float Y, Control control) {
		textureID = new Texture[1];		
		
		this.X = X;
		this.Y = Y;
		
		this.control = control;
				
		comp = new GUIComponent[]{
				new GUIButton(0, "", 487, 14, 83, 39),
				new GUIText(1, control.control, 20, 17, Color.WHITE, 1, 30),
			};
			
		genTextures();
		updateSize(Display.getWidth(), Display.getHeight());
		updateAll();
		
		isOpen = false;
	}
	
	public void setEndX(float endX) {
		this.endX = endX;
	}

	public void setEndY(float endY) {
		this.endY = endY;
		updateSize(Display.getWidth(), Display.getHeight());
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void updateSize(int orgX, int orgY) {
		float ratioX = (float)(Display.getWidth() / orgX);
		float ratioY = (float)(Display.getHeight() / orgY);
		
		endX = X * ratioX;
		endY = Y * ratioY;
				
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
		
		this.ratioX = ratioX;
		this.ratioY = ratioY;
				
		for(GUIComponent component : comp) {
			component.updateSize(ratioX, ratioY);
			component.setGui(this);
		}
	}

	public void open() {
		x = endX;
		y = endY;
		
		isOpen = true;
	}

	public void updateAll() {
//		System.out.println("Updata: " + isSelected);
		
		comp[0].x = x + 487 * ratioX;	comp[0].y = y + 14 * ratioY;	
		comp[1].x = x + 20 * ratioX;	comp[1].y = y + 17 * ratioY;	
		
		if(GameName.click) {
			isSelected = GameName.pointer.x > x && GameName.pointer.x < x + width &&
					GameName.pointer.y > y && GameName.pointer.y < y + height;
		}
		
		for(GUIComponent component : comp) {
			component.update();
		}
	}
	
	protected void actions(int id) {			
		if(waitingForAddClose && !GameName.guiManager.accessByName("Add Control").isOpen) {
			control = ((GUI_AddControl) GameName.guiManager.accessByName("Add Control")).getControl();
			((GUIText) comp[1]).setText(control.control);
		}
		
		switch(id) {
			case 0: 
					((GUI_AddControl) GameName.guiManager.accessByName("Add Control")).setControl(control);
					GameName.guiManager.accessByName("Add Control").open();
					waitingForAddClose = true;
				break;
			
			default: break;
		}
	}
	
	public void render() {		
		glPushMatrix();		
			glEnable(GL_TEXTURE_2D);
			textureID[0].bind();
			float r = 1f / (float)textureID[0].getImageHeight();
			
			glBegin(GL_QUADS);
				glColor3f(1, 1, 1);
				
				//BackGround
				glTexCoord2f(0, r * 501);       glVertex2f(x, y);
				glTexCoord2f(r * 593, r * 501); glVertex2f(x + width, y);
				glTexCoord2f(r * 593, r * 571); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 571);       glVertex2f(x, y + height);
			glEnd();
			
			glDisable(GL_TEXTURE_2D);

			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Controls_Menu_Parts");		
		
		GUIButton b = (GUIButton) comp[0];
		int[] arr = new int[]{0, 149,969, 232,969, 232,1008, 149,1008};
		b.setUpImages(textureID[0], textureID[0], textureID[0], arr, arr, arr);
	}

	public float getEndY() {
		return endY;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
