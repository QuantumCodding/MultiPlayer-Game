package com.GameName.Util.Vectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MathVec3f {
	public float x, y, z;

	public MathVec3f(float x, float y, float z) {
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
		if (!(obj instanceof MathVec3f)) return false;
		
		MathVec3f other = (MathVec3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) return false;
		
		return true;
	}
	
	public MathVec3f add(float x, float y, float z) {
		return add(new MathVec3f(x, y, z));}
	public MathVec3f add(MathVec3f add) {
		return new MathVec3f(
				x + add.getX(),
				y + add.getY(),
				z + add.getZ()
			);
	}
	
	public MathVec3f add(float add) {
		return new MathVec3f(
				x + add,
				y + add,
				z + add
			);
	}

	public MathVec3f addAndSet(MathVec3f add) {set(add(add)); return this;}
	public MathVec3f addAndSet(float add) {set(add(add));	   return this;}
	
	public MathVec3f subtract(float x, float y, float z) {
		return subtract(new MathVec3f(x, y, z));}
	public MathVec3f subtract(MathVec3f subtract) {
		return new MathVec3f(
				x - subtract.getX(),
				y - subtract.getY(),
				z - subtract.getZ()
			);
	}
	
	public MathVec3f subtract(float subtract) {
		return new MathVec3f(
				x - subtract,
				y - subtract,
				z - subtract
			);
	}

	public MathVec3f subtractAndSet(MathVec3f subtract) {set(subtract(subtract)); return this;}
	public MathVec3f subtractAndSet(float subtract) {set(subtract(subtract));	   return this;}
	
	public MathVec3f multiply(MathVec3f mult) {
		return new MathVec3f(
				x * mult.getX(),
				y * mult.getY(),
				z * mult.getZ()
			);
	}
	
	public MathVec3f multiply(float mult) {
		return new MathVec3f(
				x * mult,
				y * mult,
				z * mult
			);
	}
	
	public MathVec3f multiplyAndSet(MathVec3f mult) {set(multiply(mult)); return this;}
	public MathVec3f multiplyAndSet(float mult) {set(multiply(mult));	   return this;}
	
	public MathVec3f divide(MathVec3f divide) {
		return new MathVec3f(
				x / divide.getX(),
				y / divide.getY(),
				z / divide.getZ()
			);
	}
	
	public MathVec3f divide(float divide) {
		return new MathVec3f(
				x / divide,
				y / divide,
				z / divide
			);
	}
	
	public MathVec3f divideAndSet(MathVec3f divide) {set(divide(divide)); return this;}
	public MathVec3f divideAndSet(float divide) {set(divide(divide));	   return this;}
	
	public MathVec3f mod(MathVec3f mod) {
		return new MathVec3f(
				x % mod.getX(),
				y % mod.getY(),
				z % mod.getZ()
			);
	}
	
	public MathVec3f mod(float mod) {
		return new MathVec3f(
				x % mod,
				y % mod,
				z % mod
			);
	}
	
	public MathVec3f modAndSet(MathVec3f mod) {set(mod(mod)); return this;}
	public MathVec3f modAndSet(float mod) {set(mod(mod));    return this;}
	
	public float distance(MathVec3f other) {
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
	
	public float dot(MathVec3f other) {
		return (x * other.x) +
			   (y * other.y) +
			   (z * other.z) ;
	}
	
	public MathVec3f max(MathVec3f other) {
		return new MathVec3f(
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
	
	public MathVec3f truncate() {
		return new MathVec3f(
				(int) x, 
				(int) y, 
				(int) z
			);
	}
	
	public MathVec3f truncateAndSet() {set(truncate()); return this;}
	
	public MathVec3f round() {
		return new MathVec3f(
				Math.round(x),
				Math.round(y),
				Math.round(z)
			);
	}
	
	public MathVec3f roundAndSet() {set(round()); return this;}
	
	public MathVec3f abs() {
		return new MathVec3f(
				Math.abs(x),
				Math.abs(y),
				Math.abs(z)
			);
	}
	
	public MathVec3f normalized() {
		return divide(length());
	}
	
	public MathVec3f reflect(MathVec3f normal) {
		return subtract(normal.multiply(dot(normal) * 2));
	}
	
	public MathVec3f rotate(MathVec3f center, MathVec3f rot) {
		rot = rot.toRadians();
		
		return new MathVec3f(
				(float) (((cos(rot.getZ()) * cos(rot.getY()) + sin(rot.getY()) * -sin(rot.getZ()) * cos(rot.getX())) * x)		+ ((-sin(rot.getZ()) * cos(rot.getX())) * y)	+ ((-sin(rot.getY()) * cos(rot.getZ()) + cos(rot.getY()) * -sin(rot.getZ()) *  sin(rot.getX()) ) * z)),
				(float) (((sin(rot.getZ()) * cos(rot.getY()) + sin(rot.getY()) * -sin(rot.getX()) * cos(rot.getZ())) * x)		+ (( cos(rot.getZ()) * cos(rot.getX())) * y)	+ ((-sin(rot.getY()) * sin(rot.getZ()) + cos(rot.getY()) *  cos(rot.getZ()) * -sin(rot.getX()) ) * z)),
				(float) (((cos(rot.getX()) * sin(rot.getY())													   ) * x)		+ (( sin(rot.getX()) 				  ) * y)	+ (( cos(rot.getY()) * cos(rot.getX())														   ) * z))		
			);
	}
	
	public MathVec3f capMin(MathVec3f cap) {
		return new MathVec3f(
				x < cap.getX() ? cap.getX() : x, 
				y < cap.getY() ? cap.getY() : y, 
				z < cap.getZ() ? cap.getZ() : z
			);
	}
	
	public MathVec3f capMin(float cap) {
		return new MathVec3f(
				x < cap ? cap : x, 
				y < cap ? cap : y, 
				z < cap ? cap : z
			);
	}
	
	public MathVec3f capMinAndSet(MathVec3f cap) {set(capMin(cap)); return this;}
	public MathVec3f capMinAndSet(float cap) {set(capMin(cap)); return this;}
	

	public MathVec3f capMax(MathVec3f cap) {
		return new MathVec3f(
				x > cap.getX() ? cap.getX() : x, 
				y > cap.getY() ? cap.getY() : y, 
				z > cap.getZ() ? cap.getZ() : z
			);
	}
	
	public MathVec3f capMax(float cap) {
		return new MathVec3f(
				x > cap ? cap : x, 
				y > cap ? cap : y, 
				z > cap ? cap : z
			);
	}
	
	public MathVec3f capMaxAndSet(MathVec3f cap) {set(capMin(cap)); return this;}
	public MathVec3f capMaxAndSet(float cap) {set(capMin(cap)); return this;}
	
	public MathVec3f toRadians() {
		return new MathVec3f(
				(float) Math.toRadians(x),
				(float) Math.toRadians(y),
				(float) Math.toRadians(z)
			);
	}
	
	public boolean greaterThen(MathVec3f then) {return x > then.getX() && y > then.getY() && z > then.getZ();}	
	public boolean greaterThen(float then) {return x > then && y > then && z > then;}
	public boolean greaterThenOrEqual(MathVec3f then) {return x >= then.getX() && y >= then.getY() && z >= then.getZ();}
	public boolean greaterThenOrEqual(float then) {return x >= then && y >= then && z >= then;}
	
	public boolean lessThen(MathVec3f then) {return x < then.getX() && y < then.getY() && z < then.getZ();}	
	public boolean lessThen(float then) {return x < then && y < then && z < then;}
	public boolean lessThenOrEqual(MathVec3f then) {return x <= then.getX() && y <= then.getY() && z <= then.getZ();}
	public boolean lessThenlessThenOrEqual(float then) {return x <= then && y <= then && z <= then;}
	
	public boolean equalTo(MathVec3f then) {return x == then.getX() && y == then.getY() && z == then.getZ();}
	public boolean equalTo(float then) {return x == then && y == then && z == then;}
	
	public MathVec3f toDegrees() {
		return new MathVec3f(
				(float) Math.toDegrees(x),
				(float) Math.toDegrees(y),
				(float) Math.toDegrees(z)
			);
	}
	
	public void set(MathVec3f set) {
		this.x = set.x;
		this.y = set.y;
		this.z = set.z;
	}
	
	public static MathVec3f convert(javax.vecmath.Vector3f other) {
		return new MathVec3f(other.x, other.y, other.z);
	}
	
	public javax.vecmath.Vector3f convert() {
		return new javax.vecmath.Vector3f(x, y, z);
	}
	
	public MathVec3f clone() {
		return new MathVec3f(x, y, z);
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

	public MathVec3f setX(float x) {
		this.x = x;
		return this;
	}

	public MathVec3f setY(float y) {
		this.y = y;
		return this;
	}

	public MathVec3f setZ(float z) {
		this.z = z;
		return this;
	}
	
	public MathVec3f reset() {
		return this.setX(0).setY(0).setZ(0);
	}
}
