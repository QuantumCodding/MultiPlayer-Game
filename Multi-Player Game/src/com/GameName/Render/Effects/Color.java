package com.GameName.Render.Effects;

public class Color {
	private int r, g, b, a;

	public Color(int r, int g, int b) {
		this(r, g, b, 255); }
	public Color(int r, int g, int b, int a) {
		this.r = r;	this.g = g;
		this.b = b;	this.a = a;
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1.0f); }
	public Color(float r, float g, float b, float a) {
		this.r = (int) (r * 255);
		this.g = (int) (g * 255);
		this.b = (int) (b * 255);
		this.a = (int) (a * 255);
	}
	
	/**
	 * Creates a color out of a RGBA formated int
	 * @param color
	 */@Deprecated
	public Color(int color) {
		r = color << 24 & 0xFF;
		g = color << 16 & 0xFF;
		b = color <<  8 & 0xFF;
		a = color <<  0 & 0xFF;
	}
	
	public Color(Color clone) {
		r = clone.r; g = clone.g;
		b = clone.b; a = clone.a;
	}
	
	public Color(java.awt.Color awtColor) {
		r = awtColor.getRed();
		g = awtColor.getGreen();
		b = awtColor.getBlue();
		a = awtColor.getAlpha();
	}
	
	@Deprecated
	public int getRGBA() {
		return (r & 0xFF << 24) | (g & 0xFF << 16) |
			   (b & 0xFF <<  8) | (a & 0xFF <<  0) ;
	}
	
	@Deprecated
	public int getARGB() {
		return (a & 0xFF << 24) | (r & 0xFF << 16) |
			   (g & 0xFF <<  8) | (b & 0xFF <<  0) ;
	}
	
	@Deprecated
	public int getBGRA() {
		return (b & 0xFF << 24) | (g & 0xFF << 16) |
			   (r & 0xFF <<  8) | (a & 0xFF <<  0) ;
	}
	
	public java.awt.Color getAWTColor() {
		return  new java.awt.Color(r, g, b, a);
	}
	
	public int getRed() {return r;}
	public int getGreen() {return g;}
	public int getBlue() {return b;}
	public int getAlpha() {return a;}

	public float getRedF() {return r / 255.0f;}
	public float getGreenF() {return g / 255.0f;}
	public float getBlueF() {return b / 255.0f;}
	public float getAlphaF() {return a / 255.0f;}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + a;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Color)) return false;
		
		Color other = (Color) obj;
		if (a != other.a) return false;
		if (b != other.b) return false;
		if (g != other.g) return false;
		if (r != other.r) return false;
		
		return true;
	}

	public String toString() {
		return "Color [r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]";
	}
	
	public Color clone() {
		return new Color(r, g, b, a);
	}
	
	@Deprecated
	public static Color fromARGB(int ARGB) {
		return new Color(ARGB << 16 & 0xFF, ARGB << 8 & 0xFF, 
				ARGB & 0xFF, ARGB << 24 & 0xFF
			);
	}
	
	@Deprecated
	public static int fromARGBtoRGBA(int ARGB) {
		return ((ARGB & 0x00FFFFFF) << 8) | ((ARGB >> 24) & 0xFF);
	}
	
	@Deprecated
	public static int fromRGBAtoARGB(int RGBA) {
		return ((RGBA & 0xFFFFFF00) >> 8) | ((RGBA & 0xFF) << 24);
	}
	
	public static int flipByteOrder(int flip) {
		return (((flip >> 24) & 0xFF) << 0) | (((flip >> 16) & 0xFF) << 8) |
				(((flip >> 8) & 0xFF) << 16) | (((flip >> 0) & 0xFF) << 24) ;
	}
	
	public int get(ColorFormat format) {
		String formatS = format.toString().toLowerCase();
		int color = 0;
		
		for(int i = 0; i < formatS.length(); i ++) {
			color |= ((
				formatS.charAt(i) == 'a' ? a :
				formatS.charAt(i) == 'r' ? r :
				formatS.charAt(i) == 'g' ? g : b
			) & 0xFF) << (24 - 8 * i);
		}
		
		return color;
	}
	
	public Color(int color, ColorFormat format) {
		String formatS = format.toString().toLowerCase();
		
		for(int i = 0; i < formatS.length(); i ++) {
			int value = (color >> (24 - 8 * i)) & 0xFF;
			
			switch(formatS.charAt(i)) {
				case 'a': a = value; break;
				case 'r': r = value; break;
				case 'g': g = value; break;
				case 'b': b = value; break;
			}
		}
	} 
	
	public static int convert(int color, ColorFormat from, ColorFormat to) {
		return new Color(color, from).get(to);
	}
	
	public static enum ColorFormat {
		RGBA, ARGB, ABGR, BGRA;
	}
}
