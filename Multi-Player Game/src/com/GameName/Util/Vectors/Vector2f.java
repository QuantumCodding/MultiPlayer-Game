package com.GameName.Util.Vectors;

public class Vector2f {
	private float x;
	private float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "Vector2f [x=" + x + ", y=" + y + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Vector2f)) return false;
		
		Vector2f other = (Vector2f) obj;
		
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		
		return true;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	
}
