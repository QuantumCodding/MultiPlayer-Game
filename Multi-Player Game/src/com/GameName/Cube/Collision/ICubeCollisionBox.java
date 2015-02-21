package com.GameName.Cube.Collision;

import java.util.ArrayList;

import com.GameName.Util.Vectors.Vector3f;

public interface ICubeCollisionBox {	
	public static final Vector3f[] DEFAULT_CUBE = new Vector3f[] {
		new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), // Left		Bottom	0
		new Vector3f(0, 0, 1), new Vector3f(0, 1, 1), new Vector3f(0, 1, 0), // Left		Top		1
		
		new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(0, 1, 1), // Front	Bottom	2
		new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), new Vector3f(0, 1, 1), // Front	Top		3
		
		new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(1, 1, 0), // Right	Bottom	4
		new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0), // Right	Top		5
		
		new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), // Back		Bottom	6
		new Vector3f(1, 0, 0), new Vector3f(1, 1, 0), new Vector3f(0, 1, 0), // Back		Top		7
		
		new Vector3f(0, 1, 0), new Vector3f(1, 1, 0), new Vector3f(0, 1, 1),	// Top		Bottom	8
		new Vector3f(1, 1, 0), new Vector3f(1, 1, 1), new Vector3f(0, 1, 1),	// Top		Top		9
		
		new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1),	// Bottom	Bottom	10
		new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(0, 0, 1),	// Bottom	Top		11
	};

	public int getTriangleCount(boolean[] visableFaces);
	public ArrayList<Float> getVertices(float x, float y, float z, boolean[] visableFaces);
}
