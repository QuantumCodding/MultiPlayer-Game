package com.GameName.Util.Vectors;

public class Vector2d {
	private double x;
	private double y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "Vector2d [x=" + x + ", y=" + y + "]";
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Vector2d)) return false;
		
		Vector2d other = (Vector2d) obj;
		
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) return false;
		
		return true;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	
}
