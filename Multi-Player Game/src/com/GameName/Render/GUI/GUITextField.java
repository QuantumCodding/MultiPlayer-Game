package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Main.GameName;
import com.GameName.Render.GUI.GUIComponent;

public class GUITextField extends GUIComponent {
	private static final int MAX_SIZE = 200; 
	
	private float WIDTH;
	private float HEIGHT;
	
	private float X_OFFSET, Y_OFFSET;
	private float xOffset, yOffset;
	
	private Texture normal;
	private int[] normalC;
	
	private boolean hasFocus;
	private static boolean[] keysDown = new boolean[Keyboard.getKeyCount()];
	
	private char[] store;
	private char[] view;
	private int lastS;
	
	private boolean uppercase;
	
	public GUITextField(int id, String text, float x, float y, float width, float height, int viewSize, float xOffset, float yOffset) {
		super(id, text, x, y);
		
		this.HEIGHT = height;
		this.WIDTH = width;
		
		X_OFFSET = xOffset;
		Y_OFFSET = yOffset;
		
		store = new char[MAX_SIZE];
		view = new char[viewSize > MAX_SIZE ? MAX_SIZE : viewSize];
		
		for(int i = 0; i < store.length; i ++) {
			store[i] = text.length() > i ? text.charAt(i) : ' ';
		}
		
		lastS = 0;
	}
	
	public void render() {
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
			glColor3f(0.5f, 0.5f, 0.5f);
			glRectf(x, y, x + width, y + height);
		}
						
		font.drawString(x + xOffset, y + yOffset, text);
		glDisable(GL_TEXTURE_2D);	
		
	}
	
	public void updateSize(float ratioX, float ratioY) {	
		x = startX * ratioX;
		y = startY * ratioY;
		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
		
		xOffset = X_OFFSET * ratioX;
		yOffset = Y_OFFSET * ratioY;
	}
	
	public void update() {
		boolean pre = hasFocus;
		
		if(isSelected() && GameName.click) {
			hasFocus = true;
			GameName.lockMovement = true;
		} else if(GameName.click) {
			hasFocus = false;
		}
		
		if(hasFocus != pre) {
			GameName.lockMovement = false;
		}
		
		if(hasFocus) {
			
			for(int i = 0; i < keysDown.length; i ++) {

				if(i == Keyboard.KEY_LSHIFT || i == Keyboard.KEY_RSHIFT) {
					uppercase = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
					continue;
				}
				
				if(!keysDown[i] && Keyboard.isKeyDown(i) && lastS < store.length) {
					
					if(i == Keyboard.KEY_BACK ) {
						if(lastS != 0) {
							store[lastS] = ' ';
							lastS --;
						}
						
					} else if(i == Keyboard.KEY_SPACE) {
						store[lastS] = ' ';
						lastS ++;
						
					} else if(i == Keyboard.KEY_PERIOD) {
						store[lastS] = '.';
						lastS ++;
						
					} else if(i == Keyboard.KEY_COMMA) {
						store[lastS] = ',';
						lastS ++;
						
					} else if(i == Keyboard.KEY_SLASH) {
						store[lastS] = '/';
						lastS ++;
						
					} else if(i == Keyboard.KEY_ADD) {
						store[lastS] = '+';
						lastS ++;
						
					} else if(i == Keyboard.KEY_SUBTRACT || i == Keyboard.KEY_MINUS) {
						store[lastS] = '-';
						lastS ++;
						
					} else if(i == Keyboard.KEY_MULTIPLY) {
						store[lastS] = '*';
						lastS ++;
						
					}  else if(i == Keyboard.KEY_EQUALS) {
						store[lastS] = '=';
						lastS ++;
						
					} else if(i == Keyboard.KEY_UNDERLINE) {
						store[lastS] = '_';
						lastS ++;
						
					} else if(i == Keyboard.KEY_CAPITAL) {
						
					} else if(i == Keyboard.KEY_COMMA) {
						store[lastS] = ',';
						lastS ++;
						
					} else if(i == Keyboard.KEY_SEMICOLON) {
						store[lastS] = ';';
						lastS ++;
						
					} else if(lastS < store.length - 1) {
						store[lastS] = uppercase ? 
								Keyboard.getKeyName(i).toUpperCase().charAt(0) : 
								Keyboard.getKeyName(i).toLowerCase().charAt(0) ;
								
						lastS ++;
					}
					
				}  
					
				keysDown[i] = Keyboard.isKeyDown(i);
			}
		}
		
		for(int i = 0; i < view.length; i ++) {
			view[i] = ' ';
		}
		
		for(int i = 0; i < (view.length <= lastS ? view.length : lastS); i ++) {
			view[i]	= store[(lastS - (view.length < lastS ? view.length : lastS)) + i];
		}
		
		text = String.valueOf(view);
	}
	
	public void setUpImages(Texture n, int[] nC) {normal = n; normalC = nC;}
	public void setUpImages(Texture n){setUpImages(n, new int[]{0,0, 1,0, 1,1, 0,1});}
}
