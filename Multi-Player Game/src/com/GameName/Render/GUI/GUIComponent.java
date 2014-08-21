package com.GameName.Render.GUI;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.GameName.Main.GameName;
import com.GameName.Main.Start;


public abstract class GUIComponent {
	
	protected String text;
	protected UnicodeFont font;
	
	private String name;
	private int id;	
	
	protected GUI gui;
	
	protected float x;
	protected float y;
	
	protected float startX;
	protected float startY;
	
	protected float width;
	protected float height;
	
	public GUIComponent(int id, String text, float x, float y) {
		this.id = id;
		this.text = text;
		
		startX = x;
		startY = y;
		
		setUpFont();
	}
	
	public String getText() {
		return text;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	protected void setGui(GUI gui) {
		this.gui = gui;
	}

	public boolean isSelected() {
		if(GameName.pointer.x > x && GameName.pointer.x < x + width)
			if(GameName.pointer.y > y && GameName.pointer.y < y + height)
				return true;
		
		return false;
	}
	
	public void update() {
		if(isSelected() && GameName.click) {
			activate();
		}
	}
	
	public void activate() {
		defaultActivate();
	}
	public void defaultActivate() {
		if(gui != null) gui.actions(id);
	}
	
	public void reset() {
		x = startX;
		y = startY;
	}
	
	public abstract void render();
	public abstract void updateSize(float ratioX, float ratioY);
	
	@SuppressWarnings("unchecked") 
	protected void setUpFont(int size, int affect, Color c) {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", affect, size);
        font = new UnicodeFont(awtFont);
        
        font.getEffects().add(new ColorEffect(c));
        font.addAsciiGlyphs();
        
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
            Start.cleanUp();
        }
    }
	
	private void setUpFont() {
		setUpFont(18, 0, Color.BLACK);
	}
}
