package com.GameName.Util;

import java.awt.Color;

public class StringEffect {
	private String effects = "";
	
	public StringEffect setColor(Color c) {
		effects += "<color=" + String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()) + ">";
		return this;
	}
	
	public StringEffect setBold() {effects += "<b>"; return this;}	
	public StringEffect setItalics() {effects += "<i>"; return this;}
	
	protected String addEffects(String str) {
		String lineEnd = "";
		
		if(effects.contains("<color=")) {lineEnd += "</color>";}
		if(effects.contains("<b>")) {lineEnd += "</b>";}
		if(effects.contains("<i>")) {lineEnd += "</i>";}
	
		return effects + str + lineEnd;
	}
	
	public String getEffects() {
		return effects;
	}
}