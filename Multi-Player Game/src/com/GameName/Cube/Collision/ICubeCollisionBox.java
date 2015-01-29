package com.GameName.Cube.Collision;

import java.util.ArrayList;

import com.GameName.Util.Vectors.MathVec3f;

public interface ICubeCollisionBox {	
	public static final MathVec3f[] DEFAULT_CUBE = new MathVec3f[] {
		new MathVec3f(0, 0, 0), new MathVec3f(0, 0, 1), new MathVec3f(0, 1, 0), // Left		Bottom	0
		new MathVec3f(0, 0, 1), new MathVec3f(0, 1, 1), new MathVec3f(0, 1, 0), // Left		Top		1
		
		new MathVec3f(0, 0, 1), new MathVec3f(1, 0, 1), new MathVec3f(0, 1, 1), // Front	Bottom	2
		new MathVec3f(1, 0, 1), new MathVec3f(1, 1, 1), new MathVec3f(0, 1, 1), // Front	Top		3
		
		new MathVec3f(1, 0, 0), new MathVec3f(1, 0, 1), new MathVec3f(1, 1, 0), // Right	Bottom	4
		new MathVec3f(1, 0, 1), new MathVec3f(1, 1, 1), new MathVec3f(1, 1, 0), // Right	Top		5
		
		new MathVec3f(0, 0, 0), new MathVec3f(1, 0, 0), new MathVec3f(0, 1, 0), // Back		Bottom	6
		new MathVec3f(1, 0, 0), new MathVec3f(1, 1, 0), new MathVec3f(0, 1, 0), // Back		Top		7
		
		new MathVec3f(0, 1, 0), new MathVec3f(1, 1, 0), new MathVec3f(0, 1, 1),	// Top		Bottom	8
		new MathVec3f(1, 1, 0), new MathVec3f(1, 1, 1), new MathVec3f(0, 1, 1),	// Top		Top		9
		
		new MathVec3f(0, 0, 0), new MathVec3f(1, 0, 0), new MathVec3f(0, 0, 1),	// Bottom	Bottom	10
		new MathVec3f(1, 0, 0), new MathVec3f(1, 0, 1), new MathVec3f(0, 0, 1),	// Bottom	Top		11
	};

	public int getTriangleCount(boolean[] visableFaces);
	public ArrayList<Float> getVertices(float x, float y, float z, boolean[] visableFaces);
}
