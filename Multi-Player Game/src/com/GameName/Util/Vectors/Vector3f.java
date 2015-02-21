package com.GameName.Util.Vectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Vector3f {
	public float x, y, z;

	public Vector3f() {
		this(0, 0, 0);
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector2f clone) {
		this(clone.x, clone.y, 0);
	}
	
	public Vector3f(Vector3f clone) {
		this(clone.x, clone.y, clone.z);
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
	
	public Vector3f add(float x, float y, float z) {
		return add(new Vector3f(x, y, z));}
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

	public Vector3f subtract(float x, float y, float z) {
		return subtract(new Vector3f(x, y, z));}
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
	
	public Vector3f pow(float pow) {
		return new Vector3f(
				(float) Math.pow(x, pow),
				(float) Math.pow(y, pow),
				(float) Math.pow(z, pow)
			);
	}
	
	public Vector3f pow(Vector3f pow) {
		return new Vector3f(
				(float) Math.pow(x, pow.getX()),
				(float) Math.pow(y, pow.getY()),
				(float) Math.pow(z, pow.getZ())
			);
	}
	
	public Vector3f direction() {
		return new Vector3f(
				x > 0 ? 1 : x < 0 ? -1 : 0, 
				y > 0 ? 1 : y < 0 ? -1 : 0,
				z > 0 ? 1 : z < 0 ? -1 : 0
			);
	}
	
	public Vector3f difference(Vector3f other) {
		return subtract(other).abs();
	}
	
	public float distance(Vector3f other) {
		return (float) Math.sqrt(
				Math.pow(x - other.x, 2) +
				Math.pow(y - other.y, 2) + 
				Math.pow(z - other.z, 2)
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
	
	public Vector3f crossProduct(Vector3f other) {
		return new Vector3f(
				 (y * other.z) - (z * other.y), 
				-(x * other.z) - (z * other.x), 
				 (x * other.y) - (y * other.x)
			);
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
	
	public Vector3f round() {
		return new Vector3f(
				Math.round(x),
				Math.round(y),
				Math.round(z)
			);
	}
	
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
	
	public Vector3f rotate(Vector3f rot) {
		rot = rot.toRadians();
		return multiply(new Vector3f(
				(float) (cos(rot.y) + cos(rot.z)), 
				(float) (sin(rot.z) + sin(rot.x)), 
				(float) (sin(rot.y) + cos(rot.x))
			));
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
	
	public boolean greaterThen(Vector3f then) {return x > then.getX() && y > then.getY() && z > then.getZ();}	
	public boolean greaterThen(float then) {return x > then && y > then && z > then;}
	public boolean greaterThenOrEqual(Vector3f then) {return x >= then.getX() && y >= then.getY() && z >= then.getZ();}
	public boolean greaterThenOrEqual(float then) {return x >= then && y >= then && z >= then;}
	
	public boolean lessThen(Vector3f then) {return x < then.getX() && y < then.getY() && z < then.getZ();}	
	public boolean lessThen(float then) {return x < then && y < then && z < then;}
	public boolean lessThenOrEqual(Vector3f then) {return x <= then.getX() && y <= then.getY() && z <= then.getZ();}
	public boolean lessThenOrEqual(float then) {return x <= then && y <= then && z <= then;}
	
	public boolean equalTo(Vector3f then) {return x == then.getX() && y == then.getY() && z == then.getZ();}
	public boolean equalTo(float then) {return x == then && y == then && z == then;}
	
	public Vector3f toDegrees() {
		return new Vector3f(
				(float) Math.toDegrees(x),
				(float) Math.toDegrees(y),
				(float) Math.toDegrees(z)
			);
	}
	
	public Vector3f toRadians() {
		return new Vector3f(
				(float) Math.toRadians(x),
				(float) Math.toRadians(y),
				(float) Math.toRadians(z)
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
	
	public Vector3f reset() {
		return this.setX(0).setY(0).setZ(0);
	}
}
