package com.GameName.Cube.Render;

import java.util.ArrayList;

import com.GameName.Cube.Cube;
import com.GameName.Render.RenderUtil;
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
	
	public ArrayList<Float> getTextureCoords(int cubeId, int metadata, CubeTextureMap textureMap, boolean[] visableFaces) {
		ArrayList<Float> texCoords = new ArrayList<Float>();
		Cube cube = Cube.getCubeByID(cubeId);
		
		int startX = (int) textureMap.get(cubeId + ":" + cube.getFrameFromMetadata(metadata)).getX(), 
			startY = (int) textureMap.get(cubeId + ":" + cube.getFrameFromMetadata(metadata)).getY();
		
		double ratio = (1d / (double) textureMap.getTextureSheetSideLength());
		double x = ratio * startX, y = ratio * startY;
		double textureSideLength = ratio * cube.getTextureSize();
		
		for(int i = 0; i < visableFaces.length; i ++) {
			if(!visableFaces[i]) continue;
						
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


	public ArrayList<Float> getColors(int cubeId, int metadata,	boolean[] visableFaces) {
		ArrayList<Float> colors = new ArrayList<Float>();
				
		for(int i = 0; i < visableFaces.length; i ++) {
			if(!visableFaces[i]) continue;
						
			for(int j = 0; j < 4; j ++) {
				colors.add(1.0f);
				colors.add(1.0f);
				colors.add(1.0f);
			}
		}
		
		return colors;
	}
	
	public ArrayList<Float> getNormals(int cubeId, int metadata, boolean[] visableFaces) {
		ArrayList<Float> normals = new ArrayList<Float>();
		ArrayList<Float> vertices = getVertices(0, 0, 0, new boolean[] {true, true, true, true, true, true});		
		
		ArrayList<Vector3f> faceNormals = new ArrayList<Vector3f>();		
		for(int i = 0; i < 6; i ++) {
			int index = i * 4 - 1;
			
			faceNormals.add(RenderUtil.calculatePolygonNormal(
					new Vector3f(vertices.get(++index), vertices.get(++index), vertices.get(++index)),
					new Vector3f(vertices.get(++index), vertices.get(++index), vertices.get(++index)),
					new Vector3f(vertices.get(++index), vertices.get(++index), vertices.get(++index)),
					new Vector3f(vertices.get(++index), vertices.get(++index), vertices.get(++index))
				));
		}
		
		ArrayList<Vector3f> vertexNormals = new ArrayList<Vector3f>();
		for(int i = 0; i < 8; i ++) {
			vertexNormals.add(
					faceNormals.get(i % 4).add(faceNormals.get((i + 1) % 4)).add(faceNormals.get((i / 4) + 4))
				.divide(3));
		}
		
		for(int i = 0; i < visableFaces.length; i ++) {
			if(!visableFaces[i]) continue;
				
			for(int j = 0; j < 4; j ++) {
				int index = 0;
				
				switch(j) {				
					case 0: index = j; break;
					case 1: index = (j + 1) % 4; break;
					case 2: index = j + 4; break;
					case 3: index = ((j + 1) % 4) + 4; break;
						
					case 5: index = 4;
					case 4: index += j; break;
					
					default: break;				
				}
				
				Vector3f normal = vertexNormals.get(index);
				normals.add(normal.getX()); normals.add(normal.getY()); normals.add(normal.getZ());
			}
		}
		
		return normals;
	}
	
	public int getVerticeCount(boolean[] visableFaces) {
		int count = 0;
		
		for(boolean bool : visableFaces) {
			if(bool) {
				count += 4;
			}
		}
		
		return count;
	}
}