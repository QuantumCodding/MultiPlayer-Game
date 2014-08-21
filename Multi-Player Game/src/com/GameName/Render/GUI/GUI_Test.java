package com.GameName.Render.GUI;

import java.awt.Color;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;

import static org.lwjgl.opengl.GL11.*;

public class GUI_Test extends GUI {
	private final float HEIGHT = 196;
	private final float WIDTH = 100;	
	
	private final float END_X = 100;	
	private final float END_Y = 100;
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
	
	public GUI_Test() {
		textureID = new Texture[1];
		genTextures();
		
		comp = new GUIComponent[]{
				new GUITextField(0, "", 5, 5, 90, 30, 6, 3, 0),
				new GUIButton(1, "Set", 5, 31, 90, 30),
				new GUIRadioButton(2, " Money", 5, 62),
				new GUIRadioButton(3, " Health", 5, 88),
				new GUIRadioButton(4, " Mana", 5, 114),
				new GUIRadioButton(5, " Hunger", 5, 140),
				new GUIRadioButton(6, " Power", 5, 166)
			};
		
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
				case 1: 
					double value;
					try { value = Double.parseDouble(comp[0].getText()); } catch(Exception e) {return;}
					
					GUIRadioButton temp = (GUIRadioButton) comp[2]; 
					GUIRadioButton temp1 = (GUIRadioButton) comp[3]; 
					GUIRadioButton temp2 = (GUIRadioButton) comp[4]; 
					GUIRadioButton temp3 = (GUIRadioButton) comp[5]; 
					GUIRadioButton temp4 = (GUIRadioButton) comp[6];
					
					if(temp.getState()) GameName.player.getAccess().setMoney(value, true); 
					if(temp1.getState()) GameName.player.getAccess().setHealth((int) value, true); 
					if(temp2.getState()) GameName.player.getAccess().setMana((int) value, true); 
					if(temp3.getState()) GameName.player.getAccess().setHunger((int) value, true); 
					if(temp4.getState()) GameName.player.getAccess().setPower((int) value, true); 
				break;
					
				default: break;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
	
	private void setExtraRenders() {
		for(GUIComponent component : comp) {
			switch(component.getId()) {
				case 0: component.setUpFont(20, 0, Color.BLUE); break;
			}
		}
	}
	
	public void render() {		
		glPushMatrix();		
//			glEnable(GL_TEXTURE_2D);
			glDisable(GL_TEXTURE_2D);
			
			glBegin(GL_QUADS);
				glColor3f(1, 1, 0);
				
				glVertex2f(x, y);
				glVertex2f(x + width, y);
				glVertex2f(x + width, y + height);
				glVertex2f(x, y + height);
			glEnd();
			
			
			
			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = null;
	}	
}
