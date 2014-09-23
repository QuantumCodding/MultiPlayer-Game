package com.GameName.Cube.Render;

import java.util.ArrayList;

import com.GameName.Util.Vectors.Vector3f;

public interface ICubeRender {
	public static final Vector3f[] DEFAULT_CUBE = new Vector3f[] {
			new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 1), new Vector3f(0, 1, 0), // Left
			new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), new Vector3f(0, 1, 1), // Front
			new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0), // Right
			new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 1, 0), new Vector3f(0, 1, 0), // Back
			new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(0, 0, 1),	// Bottom	
			new Vector3f(0, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, 1, 1), new Vector3f(0, 1, 1),	// Top			
	};
	
	public ArrayList<Float> getVertices(float x, float y, float z, boolean[] visableFaces);	
	public ArrayList<Float> getTextureCoords(int cubeId, int metadata, boolean[] visableFaces);
}
