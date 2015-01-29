package com.GameName.Cube.Collision;

import java.util.ArrayList;

import com.GameName.Util.Vectors.MathVec3f;

public class DefaultCubeCollisionBox implements ICubeCollisionBox {

	public int getTriangleCount(boolean[] visableFaces) {
		int faceCount = 0;
		for(boolean bool : visableFaces) {
			if(bool) faceCount ++;
		}
		
		return faceCount * 6;
	}

	public ArrayList<Float> getVertices(float x, float y, float z, boolean[] visableFaces) {
		ArrayList<Float> vertices = new ArrayList<Float>();
		
		for(int i = 0; i < DEFAULT_CUBE.length; i ++) {
			if(visableFaces[i / 6]) {
				MathVec3f pos = DEFAULT_CUBE[i].add(new MathVec3f(x, y, z));
				
				vertices.add(pos.getX());
				vertices.add(pos.getY());
				vertices.add(pos.getZ());
			}
		}
		
		return vertices;
	}

}
