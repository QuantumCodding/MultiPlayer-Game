package com.GameName.Cube.Render;

import java.util.ArrayList;

import com.GameName.Cube.Cube;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class DefaultCubeRender implements ICubeRender {

	public ArrayList<Float> getVertices(float x, float y, float z, boolean[] visableFaces) {
		ArrayList<Float> vertices = new ArrayList<Float>();
		
		for(int i = 0; i < DEFAULT_CUBE.length; i ++) {
			if(visableFaces[i / 4]) {
				Vector3f pos = DEFAULT_CUBE[i].add(new Vector3f(x, y, z)).multiply(World.CUBE_SIZE);
				
				vertices.add(pos.getX());
				vertices.add(pos.getY());
				vertices.add(pos.getZ());
			}
		}
		
		return vertices;
	}
	
	public ArrayList<Float> getTextureCoords(int cubeId, int metadata, boolean[] visableFaces) {
		ArrayList<Float> texCoords = new ArrayList<Float>();
		Cube cube = Cube.getCubeByID(cubeId);
		
		int startX = Cube.getCubeTexCoords()[(cube.getId() * 2) + cube.getFrameFromMetadata(metadata)], 
			startY = Cube.getCubeTexCoords()[(cube.getId() * 2) + 1 + cube.getFrameFromMetadata(metadata)];
		
		double ratio = (1d / (double) Cube.getTextureSheetSideLength());
		double x = ratio * startX, y = ratio * startY;
		double textureSideLength = ratio * cube.getTextureSize();
		
		for(int i = 0; i < visableFaces.length; i ++) {
			if(visableFaces[i]) continue;
						
			for(int j = 0; j < 4; j ++) {
				float useX = (float) ((textureSideLength * i) + x);
				float useY = (float) y;
				
				float xAdd = 0.0f, yAdd = 0.0f;
				
				switch(j) {
					case 1: xAdd = (float) textureSideLength; break;
					case 2: xAdd = (float) textureSideLength; yAdd = (float) textureSideLength; break;
					case 3: yAdd = (float) textureSideLength; break;
						
					default: break;
				}
				
				texCoords.add(useX + xAdd);	texCoords.add(useY + yAdd);
			}
		}
		
		return texCoords;
	}
}
