package com.GameName.Cube.Render;

import java.util.ArrayList;

import com.GameName.Util.Vectors.Vector3f;

public interface ICubeRender {
	public static final Vector3f[] DEFAULT_CUBE = new Vector3f[] {
		new Vector3f(1, 0, 1), new Vector3f(1, 0, 0), new Vector3f(1, 1, 0), new Vector3f(1, 1, 1), // Left		0
		new Vector3f(1, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1, 0), // Front	1
		new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 1), new Vector3f(0, 1, 0), // Right	2
		new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), new Vector3f(0, 1, 1), // Back		3
		new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 0),	// Top		4
		new Vector3f(0, 1, 0), new Vector3f(0, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0),	// Bottom	5
	};
	
	public ArrayList<Float> getVertices(float x, float y, float z, boolean[] visableFaces);	
	public ArrayList<Float> getTextureCoords(int cubeId, int metadata, CubeTextureMap textureMap, boolean[] visableFaces);
	public ArrayList<Float> getColors(int cubeId, int metadata, boolean[] visableFaces);
	public ArrayList<Float> getNormals(int cubeId, int metadata, boolean[] visableFaces);
	
	public int getVerticeCount(boolean[] visableFaces);
}