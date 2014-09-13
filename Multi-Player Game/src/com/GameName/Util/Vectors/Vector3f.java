package com.GameName.Util.Vectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Vector3f {
	private float x, y, z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Vector3f)) return false;
		
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) return false;
		
		return true;
	}

	public Vector3f add(Vector3f add) {
		return new Vector3f(
				x + add.getX(),
				y + add.getY(),
				z + add.getZ()
			);
	}
	
	public Vector3f add(float add) {
		return new Vector3f(
				x + add,
				y + add,
				z + add
			);
	}

	public Vector3f addAndSet(Vector3f add) {set(add(add)); return this;}
	public Vector3f addAndSet(float add) {set(add(add));	   return this;}
	
	public Vector3f subtract(Vector3f subtract) {
		return new Vector3f(
				x - subtract.getX(),
				y - subtract.getY(),
				z - subtract.getZ()
			);
	}
	
	public Vector3f subtract(float subtract) {
		return new Vector3f(
				x - subtract,
				y - subtract,
				z - subtract
			);
	}

	public Vector3f subtractAndSet(Vector3f subtract) {set(subtract(subtract)); return this;}
	public Vector3f subtractAndSet(float subtract) {set(subtract(subtract));	   return this;}
	
	public Vector3f multiply(Vector3f mult) {
		return new Vector3f(
				x * mult.getX(),
				y * mult.getY(),
				z * mult.getZ()
			);
	}
	
	public Vector3f multiply(float mult) {
		return new Vector3f(
				x * mult,
				y * mult,
				z * mult
			);
	}
	
	public Vector3f multiplyAndSet(Vector3f mult) {set(multiply(mult)); return this;}
	public Vector3f multiplyAndSet(float mult) {set(multiply(mult));	   return this;}
	
	public Vector3f divide(Vector3f divide) {
		return new Vector3f(
				x / divide.getX(),
				y / divide.getY(),
				z / divide.getZ()
			);
	}
	
	public Vector3f divide(float divide) {
		return new Vector3f(
				x / divide,
				y / divide,
				z / divide
			);
	}
	
	public Vector3f divideAndSet(Vector3f divide) {set(divide(divide)); return this;}
	public Vector3f divideAndSet(float divide) {set(divide(divide));	   return this;}
	
	public Vector3f mod(Vector3f mod) {
		return new Vector3f(
				x % mod.getX(),
				y % mod.getY(),
				z % mod.getZ()
			);
	}
	
	public Vector3f mod(float mod) {
		return new Vector3f(
				x % mod,
				y % mod,
				z % mod
			);
	}
	
	public Vector3f modAndSet(Vector3f mod) {set(mod(mod)); return this;}
	public Vector3f modAndSet(float mod) {set(mod(mod));    return this;}
	
	public float distance(Vector3f other) {
		return (float) Math.sqrt(
				Math.pow(x - other.getX(), 2) +
				Math.pow(y - other.getY(), 2) + 
				Math.pow(z - other.getZ(), 2)
			);
	}
	
	public float length() {
		return (float) Math.sqrt(
				dot(this)
			);
	}
	
	public float dot(Vector3f other) {
		return (x * other.x) +
			   (y * other.y) +
			   (z * other.z) ;
	}
	
	public Vector3f max(Vector3f other) {
		return new Vector3f(
				x > other.getX() ? x : other.getX(),
				y > other.getY() ? y : other.getY(),
				z > other.getZ() ? z : other.getZ()
			);
	}
	
	public float max() {
		float max = 0;
		
		if(x > max) max = x;
		if(y > max) max = y;
		if(z > max) max = z;
		
		return max;
	}
	
	public Vector3f truncate() {
		return new Vector3f(
				(int) x, 
				(int) y, 
				(int) z
			);
	}
	
	public Vector3f truncateAndSet() {set(truncate()); return this;}
	
	public Vector3f round() {
		return new Vector3f(
				Math.round(x),
				Math.round(y),
				Math.round(z)
			);
	}
	
	public Vector3f roundAndSet() {set(round()); return this;}
	
	public Vector3f abs() {
		return new Vector3f(
				Math.abs(x),
				Math.abs(y),
				Math.abs(z)
			);
	}
	
	public Vector3f normalized() {
		return divide(length());
	}
	
	public Vector3f reflect(Vector3f normal) {
		return subtract(normal.multiply(dot(normal) * 2));
	}
	
	public Vector3f rotate(Vector3f center, Vector3f rot) {
		rot = rot.toRadians();
		
		return new Vector3f(
				(float) (((cos(rot.getZ()) * cos(rot.getY()) + sin(rot.getY()) * -sin(rot.getZ()) * cos(rot.getX())) * x)		+ ((-sin(rot.getZ()) * cos(rot.getX())) * y)	+ ((-sin(rot.getY()) * cos(rot.getZ()) + cos(rot.getY()) * -sin(rot.getZ()) *  sin(rot.getX()) ) * z)),
				(float) (((sin(rot.getZ()) * cos(rot.getY()) + sin(rot.getY()) * -sin(rot.getX()) * cos(rot.getZ())) * x)		+ (( cos(rot.getZ()) * cos(rot.getX())) * y)	+ ((-sin(rot.getY()) * sin(rot.getZ()) + cos(rot.getY()) *  cos(rot.getZ()) * -sin(rot.getX()) ) * z)),
				(float) (((cos(rot.getX()) * sin(rot.getY())													   ) * x)		+ (( sin(rot.getX()) 				  ) * y)	+ (( cos(rot.getY()) * cos(rot.getX())														   ) * z))		
			);
	}
	
	public Vector3f capMin(Vector3f cap) {
		return new Vector3f(
				x < cap.getX() ? cap.getX() : x, 
				y < cap.getY() ? cap.getY() : y, 
				z < cap.getZ() ? cap.getZ() : z
			);
	}
	
	public Vector3f capMin(float cap) {
		return new Vector3f(
				x < cap ? cap : x, 
				y < cap ? cap : y, 
				z < cap ? cap : z
			);
	}
	
	public Vector3f capMinAndSet(Vector3f cap) {set(capMin(cap)); return this;}
	public Vector3f capMinAndSet(float cap) {set(capMin(cap)); return this;}
	

	public Vector3f capMax(Vector3f cap) {
		return new Vector3f(
				x > cap.getX() ? cap.getX() : x, 
				y > cap.getY() ? cap.getY() : y, 
				z > cap.getZ() ? cap.getZ() : z
			);
	}
	
	public Vector3f capMax(float cap) {
		return new Vector3f(
				x > cap ? cap : x, 
				y > cap ? cap : y, 
				z > cap ? cap : z
			);
	}
	
	public Vector3f capMaxAndSet(Vector3f cap) {set(capMin(cap)); return this;}
	public Vector3f capMaxAndSet(float cap) {set(capMin(cap)); return this;}
	
	public Vector3f toRadians() {
		return new Vector3f(
				(float) Math.toRadians(x),
				(float) Math.toRadians(y),
				(float) Math.toRadians(z)
			);
	}
	
	public Vector3f toDegrees() {
		return new Vector3f(
				(float) Math.toDegrees(x),
				(float) Math.toDegrees(y),
				(float) Math.toDegrees(z)
			);
	}
	
	public void set(Vector3f set) {
		this.x = set.x;
		this.y = set.y;
		this.z = set.z;
	}
	
	public Vector3f clone() {
		return new Vector3f(x, y, z);
	}
	
	public String toString() {
		return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	public String valuesToString() {
		return "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Vector3f setX(float x) {
		this.x = x;
		return this;
	}

	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}

	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}
	
}
