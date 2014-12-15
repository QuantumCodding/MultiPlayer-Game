package com.GameName.Cube.Render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import com.GameName.Cube.Cube;
import com.GameName.Engine.GameEngine;
import com.GameName.Util.Vectors.Vector2f;

public class CubeRenderUtil {
	public static CubeTextureMap generateTexturMap(GameEngine eng, HashSet<Cube> typesOfCubes) {
		Cube[] cubes = typesOfCubes.toArray(new Cube[typesOfCubes.size()]);
		
		int textureSheetMaxFrames = 0;
		
		int totalArea = 0;
		int maxTextureSize = 0;
		
		for(Cube cube : typesOfCubes) {			
			totalArea += Math.pow(cube.getTextureSize(), 2) * 6 * cube.getFrames();
			
			if(textureSheetMaxFrames < cube.getTextureSize()) textureSheetMaxFrames = cube.getFrames();
			if(maxTextureSize < cube.getTextureSize()) maxTextureSize = cube.getTextureSize();
		}
		
		int sideLength = (int) Math.ceil(Math.sqrt(totalArea));
		sideLength = sideLength < maxTextureSize * 6 ? maxTextureSize * 6 : sideLength;
		
		while((sideLength & -sideLength) != sideLength) {
			sideLength ++;
		}
		
		CubeTextureMap textureMap = new CubeTextureMap(null, new HashMap<String, Vector2f>(), sideLength, textureSheetMaxFrames);
		
		int[][] textureSheet = new int[sideLength][sideLength];		
		int emptyColor = new Color(15, 4, 15).getRGB();
		
		for(int x = 0; x < sideLength; x ++) {
			for(int y = 0; y < sideLength; y ++) {
				textureSheet[x][y] = emptyColor;
			}
		}
		
		int cubesAdded = 0;
		int index = 0;		
		boolean[] added = new boolean[typesOfCubes.size() * textureSheetMaxFrames];
		int x = 0, y = 0;
		boolean hitEnd = false;
		
		for(Cube cube : typesOfCubes) {
			for(int i = 0; i < textureSheetMaxFrames; i ++) {
				added[i] = cube.getFrames() < i;
			}
		}
				
		while(cubesAdded < added.length) {
			if(index >= added.length) {
				index = 0;
				
				while(added[index]) {
					index ++;
				}
				
				do {
					if(x < sideLength - 1) {
						x ++;
						
					} else if(y < sideLength - 1) {
						y ++;
						x = 0;
						
					} else {
						if(hitEnd) {
							throw new IndexOutOfBoundsException("Failed to Creat Texture Map");
						}
						
						y = 0;
						x = 0;
						
						hitEnd = true;
					}
				} while(textureSheet[x][y] != emptyColor);
				
				hitEnd = false;
			}
			
			while(added[index]) {
				index ++;
			}
			
			Cube cube = cubes[index / textureSheetMaxFrames];
			
			if(cube.getTextureSize() + y >= sideLength) {
				index ++;
				continue;
			}
			
			if(cube.getTextureSize() * 6 + x < sideLength) {
				for(int face = 0; face < 6; face ++) {		
				for(int xPos = 0; xPos < cube.getTextureSize(); xPos ++) {
				for(int yPos = 0; yPos < cube.getTextureSize(); yPos ++) {					
					textureSheet[(face * cube.getTextureSize()) + (x + xPos)][y + yPos] 
						= cube.getTextures()
							[index % textureSheetMaxFrames]	[face]
							[xPos + (yPos * cube.getTextureSize())];
				}}}
				
				textureMap.put(cube.getId() + ":" + index % textureSheetMaxFrames, new Vector2f(x, y));
				
				x += cube.getTextureSize() * 6;
				added[index] = true;
				
				index ++;
				cubesAdded ++;
				
				continue;
			}
			
			index ++;
		}
		
		BufferedImage textureSheetImage = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
		
		for(x = 0; x < sideLength; x ++) {
			for(y = 0; y < sideLength; y ++) {
				textureSheetImage.setRGB(x, y, textureSheet[x][y]);
			}
		}
		
		textureMap.setTexture(eng.getGLContext().genTexture(textureSheetImage, "ChunkTextureSheet", false));		
		return textureMap;
	}
}
