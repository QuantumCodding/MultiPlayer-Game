package com.GameName.GUI.Components;

import com.GameName.Util.Vectors.Vector3f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class GUIText extends GUIComponent {
	
	private UnicodeFont font;
	private String text;
	
	protected GUIText(int id, float x, float y, String text) {
		super(id, x, y, 1, 1);
		
		this.text = text;
	}

	public void renderForground() {
		font.drawString(getX(), getY(), text);
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
	
	public void setText(String text) {
		this.text = text;
	} 
}
