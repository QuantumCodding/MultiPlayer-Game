package com.GameName.GUI.Components;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.GameName.Engine.GameEngine;
import com.GameName.Util.Vectors.Vector3f;

public class GUITextField extends GUIComponent {
	private static final int MAX_SIZE = 200; 

	private boolean hasFocus;
	private static boolean[] keysDown = new boolean[Keyboard.getKeyCount()];
	
	private String text;
	private UnicodeFont font;
	
	private char[] store;
	private int viewSize;
	private int lastS;
	
	private boolean uppercase;
	
	protected GUITextField(GameEngine eng, int id, float x, float y, float width, float height, int viewSize, String startText) {
		super(eng, id, x, y, width, height);
		
		this.viewSize = viewSize;
		store = new char[MAX_SIZE];
		text = startText;
		
		for(int i = 0; i < store.length; i ++) {
			store[i] = startText.length() > i ? startText.charAt(i) : ' ';
		}
		
		lastS = 0;
	}

	public void renderForground() {
		font.drawString(getX(), getY(), text);
	}
	
	public void update() {
		boolean pre = hasFocus;
		
		if(isSelected() && ENGINE.getPlayer().isPointerDown()) {
			hasFocus = true;
			ENGINE.getPlayer().setCanMove(false);
		} else if(ENGINE.getPlayer().isPointerDown()) {
			hasFocus = false;
		}
		
		if(hasFocus != pre) {
			ENGINE.getPlayer().setCanMove(true);
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
		
		text = "";

		for(int i = lastS; i < store.length; i ++) {
			text += store[i];
			
			if(font.getWidth(text + store[i + (i + 1 >= MAX_SIZE ? 0 : 1)]) >= viewSize) {
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setFont(java.awt.Font awtFont, java.awt.Color color) {		
		font = new UnicodeFont(awtFont);
	        
        font.getEffects().add(new ColorEffect(color));
        font.addAsciiGlyphs();
        
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}
	
	public void setFont(String fontName, int modifiers, int size, java.awt.Color color) {
		setFont(new java.awt.Font(fontName, modifiers, size), color);
	}
	
	public void setFont(String fontName, int modifiers, int size, Vector3f color) {
		setFont(new java.awt.Font(fontName, modifiers, size), new java.awt.Color(color.getX(), color.getY(), color.getZ()));
	}
	
	public String getText() {
		return text;
	}
}
